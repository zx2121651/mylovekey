package com.lovekey.clone.ime

import android.inputmethodservice.InputMethodService
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.lovekey.clone.ui.theme.LoveKeyCloneTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoveKeyInputService : InputMethodService() {

    private lateinit var composeView: ComposeView
    private val composeLifecycle = ComposeIMELifecycle()
    private var keyboardState by mutableStateOf(KeyboardState())

    private val debounceScope = CoroutineScope(Dispatchers.Main)
    private var debounceJob: Job? = null

    override fun onCreate() {
        super.onCreate()
        PinyinEngineAdapter.init(this)
        composeLifecycle.onCreate()
    }

    override fun onCreateInputView(): View {
        composeView = ComposeView(this).apply {
            setViewTreeLifecycleOwner(composeLifecycle)
            setViewTreeViewModelStoreOwner(composeLifecycle)
            setViewTreeSavedStateRegistryOwner(composeLifecycle)
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                LoveKeyCloneTheme {
                    RealKeyboardUI(
                        state = keyboardState,
                        onKeyPress = { handleKeyPress(it) },
                        onDelete = { handleDelete() },
                        onEnter = { handleEnter() },
                        onAiAction = { triggerAiAction(it) },
                        onSwitchMode = { newMode -> keyboardState = keyboardState.copy(mode = newMode) },
                        onClearComposing = {
                            PinyinEngineAdapter.clearComposing()
                            keyboardState = keyboardState.copy(composingText = "", candidates = emptyList())
                        },
                        onToggleShift = { keyboardState = keyboardState.copy(isShifted = !keyboardState.isShifted) },
                        onToggleTraditional = { keyboardState = keyboardState.copy(isTraditional = !keyboardState.isTraditional) },
                        onCandidateSelect = { handleCandidateSelect(it) }
                    )
                }
            }
        }
        composeLifecycle.onStart()
        return composeView
    }

    override fun onStartInputView(info: EditorInfo?, restarting: Boolean) {
        super.onStartInputView(info, restarting)
        composeLifecycle.onResume()
        // Reset composing state
        keyboardState = keyboardState.copy(
            composingText = "",
            candidates = emptyList(),
            activePanel = ActivePanel.KEYBOARD, // Reset to standard keyboard
            showPaywall = false,
            contextText = ""
        )
    }

    override fun onUpdateSelection(
        oldSelStart: Int,
        oldSelEnd: Int,
        newSelStart: Int,
        newSelEnd: Int,
        candidatesStart: Int,
        candidatesEnd: Int
    ) {
        super.onUpdateSelection(oldSelStart, oldSelEnd, newSelStart, newSelEnd, candidatesStart, candidatesEnd)

        // Cancel previous job
        debounceJob?.cancel()

        // Start a new debounce job to fetch the text
        debounceJob = debounceScope.launch {
            delay(300) // 300ms debounce
            // Fetch text before cursor, limit to a reasonable amount (e.g., 500 chars)
            val ic = currentInputConnection ?: return@launch
            val textBeforeCursor = ic.getTextBeforeCursor(500, 0)?.toString() ?: ""
            // Only update context if we are in normal keyboard mode to avoid thrashing
            if (keyboardState.activePanel == ActivePanel.KEYBOARD) {
                keyboardState = keyboardState.copy(contextText = textBeforeCursor)
            }
        }
    }

    override fun onFinishInputView(finishingInput: Boolean) {
        super.onFinishInputView(finishingInput)
        composeLifecycle.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        composeLifecycle.onStop()
        composeLifecycle.onDestroy()
    }


    private fun handleKeyPress(text: String) {
        if (keyboardState.mode == KeyboardMode.QWERTY_PINYIN || keyboardState.mode == KeyboardMode.T9_PINYIN) {
            val isQwertyLetter = keyboardState.mode == KeyboardMode.QWERTY_PINYIN && text.matches(Regex("[a-zA-Z]+"))
            val isT9Number = keyboardState.mode == KeyboardMode.T9_PINYIN && text.matches(Regex("[2-9]"))

            // Letters go into composing text if in Pinyin mode
            if (isQwertyLetter || isT9Number) {
                val newComposing = keyboardState.composingText + text
                PinyinEngineAdapter.clearComposing()
                val cands = ChineseUtils.getCandidates(newComposing, keyboardState.mode)
                keyboardState = keyboardState.copy(composingText = newComposing, candidates = cands)
                return
            }
        }

        // Non-letter/non-T9-number or not in Pinyin mode: commit directly
        commitDirectly(text)
    }


    private fun handleDelete() {
        if (keyboardState.composingText.isNotEmpty()) {
            val newComposing = keyboardState.composingText.dropLast(1)
            PinyinEngineAdapter.clearComposing()
            val cands = if (newComposing.isNotEmpty()) ChineseUtils.getCandidates(newComposing, keyboardState.mode) else emptyList()
            keyboardState = keyboardState.copy(composingText = newComposing, candidates = cands)
            return
        }

        val ic = currentInputConnection ?: return
        val selectedText = ic.getSelectedText(0)
        if (selectedText.isNullOrEmpty()) {
            ic.deleteSurroundingText(1, 0)
        } else {
            ic.commitText("", 1)
        }
    }

    private fun handleCandidateSelect(word: String) {
        val output = if (keyboardState.isTraditional) ChineseUtils.convertToTraditional(word) else word
        commitDirectly(output)
        // Reset composing state after selection
        keyboardState = keyboardState.copy(composingText = "", candidates = emptyList())
    }

    private fun commitDirectly(text: String) {
        currentInputConnection?.commitText(text, 1)
        // If we typed punctuation while composing, we commit the raw letters first then the punctuation.
        if (keyboardState.composingText.isNotEmpty()) {
            currentInputConnection?.commitText(keyboardState.composingText + text, 1)
            keyboardState = keyboardState.copy(composingText = "", candidates = emptyList())
        }
    }

    private fun handleEnter() {
        if (keyboardState.composingText.isNotEmpty()) {
            // Commit raw English text if user hits enter while composing
            commitDirectly("")
            return
        }

        val ic = currentInputConnection ?: return
        val info = currentInputEditorInfo
        val actionId = info?.imeOptions?.and(EditorInfo.IME_MASK_ACTION) ?: EditorInfo.IME_ACTION_DONE
        if (actionId != EditorInfo.IME_ACTION_NONE && actionId != EditorInfo.IME_ACTION_UNSPECIFIED) {
            ic.performEditorAction(actionId)
        } else {
            ic.commitText("\n", 1)
        }
    }

    private fun triggerAiAction(action: String) {
        // Intercept based on free usage
        if (keyboardState.freeUsagesLeft <= 0) {
            keyboardState = keyboardState.copy(showPaywall = true)
            return
        }


        // Use either the composing text or the existing context text tracked via onUpdateSelection
        val currentContext = if (keyboardState.composingText.isNotEmpty()) {
            keyboardState.composingText
        } else {
            keyboardState.contextText
        }

        // Mock AI Data
        val mockData = listOf(
            AiReplyCategory("高情商", "🍬", listOf("在呼吸，在心跳，在想你呀~", "本来在发呆，看到你的消息心跳就漏了一拍", "在想怎么回复才能让你心动💓")),
            AiReplyCategory("幽默", "😆", listOf("在思考宇宙的终极奥秘...顺便想你", "在进行光合作用", "用意念给你回复中...")),
            AiReplyCategory("暖男", "☀️", listOf("刚忙完，正准备找你呢", "今天过得怎么样？", "无论在哪，我都在你身边"))
        )

        when (action) {
            "帮你回" -> {
                keyboardState = keyboardState.copy(
                    activePanel = ActivePanel.BANG_NI_HUI,
                    contextText = currentContext
                )
            }
            "超会说", "换个说法" -> {
                keyboardState = keyboardState.copy(
                    activePanel = ActivePanel.CHAO_HUI_SHUO,
                    contextText = currentContext,
                    aiLoading = true, // Start loading
                    aiMockResults = emptyList() // Clear previous
                )
                // Simulate network request
                composeView.postDelayed({
                    keyboardState = keyboardState.copy(
                        aiLoading = false,
                        aiMockResults = mockData,
                        freeUsagesLeft = keyboardState.freeUsagesLeft - 1 // Consume a usage
                    )
                }, 1000)
            }
            "Refresh" -> {
                keyboardState = keyboardState.copy(aiLoading = true)
                composeView.postDelayed({
                    keyboardState = keyboardState.copy(
                        aiLoading = false,
                        aiMockResults = mockData.shuffled() // Mock a refresh
                    )
                }, 600)
            }
            "Themes" -> {
                keyboardState = keyboardState.copy(
                    activePanel = ActivePanel.THEME_SELECTION
                )
            }
            "CloseAi" -> {
                keyboardState = keyboardState.copy(activePanel = ActivePanel.KEYBOARD)
            }
            "PaywallClose" -> {
                keyboardState = keyboardState.copy(showPaywall = false)
            }
            "Purchase" -> {
                // Mock purchase success -> infinite uses
                keyboardState = keyboardState.copy(
                    showPaywall = false,
                    freeUsagesLeft = 9999
                )
            }
            else -> {
                if (action.startsWith("SelectTheme:")) {
                    val themeId = action.substringAfter("SelectTheme:")
                    val selectedTheme = ThemePresets.allThemes.find { it.id == themeId } ?: ThemePresets.DefaultBlue
                    keyboardState = keyboardState.copy(
                        currentTheme = selectedTheme,
                        activePanel = ActivePanel.KEYBOARD
                    )
                } else {
                    val finalReply = if (keyboardState.isTraditional) ChineseUtils.convertToTraditional(action) else action
                    commitDirectly(finalReply)
                    keyboardState = keyboardState.copy(activePanel = ActivePanel.KEYBOARD)
                }
            }
        }
    }
}
