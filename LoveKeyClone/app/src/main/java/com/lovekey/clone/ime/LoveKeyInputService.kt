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
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.LifecycleOwner
import androidx.savedstate.SavedStateRegistryOwner
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

    override fun onDestroy() {
        super.onDestroy()
        composeLifecycle.onDestroy()
    }

    override fun onCreateInputView(): View {
        composeLifecycle.onStart()
        composeView = ComposeView(this).apply {
            this.setViewTreeLifecycleOwner(composeLifecycle)
            this.setViewTreeViewModelStoreOwner(composeLifecycle)
            this.setViewTreeSavedStateRegistryOwner(composeLifecycle)
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindow)

            setContent {
                LoveKeyCloneTheme {
                    RealKeyboardUI(
                        state = keyboardState,
                        onKeyPress = { handleKeyPress(it) },
                        onDelete = { handleDelete() },
                        onClear = { handleClear() },
                        onEnter = { handleEnter() },
                        onAiAction = { triggerAiAction(it) },
                        onSwitchMode = { newMode -> keyboardState = keyboardState.copy(mode = newMode) },
                        onToggleShift = { keyboardState = keyboardState.copy(isShifted = !keyboardState.isShifted) },
                        onToggleTraditional = { keyboardState = keyboardState.copy(isTraditional = !keyboardState.isTraditional) },
                        onCandidateSelect = { handleCandidateSelect(it) },
                        onT9SyllableSelect = { handleT9SyllableSelect(it) },
                        onToggleT9SyllableSelector = { keyboardState = keyboardState.copy(isSyllableSelectorExpanded = !keyboardState.isSyllableSelectorExpanded) }
                    )
                }
            }
        }
        return composeView
    }

    override fun onStartInputView(info: EditorInfo?, restarting: Boolean) {
        super.onStartInputView(info, restarting)
        composeLifecycle.onResume()
        keyboardState = keyboardState.copy(
            composingText = "",
            candidates = emptyList(),
            t9PinyinCombinations = emptyList(),
            selectedT9Syllable = null,
            isSyllableSelectorExpanded = false,
            activePanel = ActivePanel.KEYBOARD,
            showPaywall = false,
            contextText = ""
        )
    }

    override fun onUpdateSelection(
        oldSelStart: Int, oldSelEnd: Int, newSelStart: Int, newSelEnd: Int,
        candidatesStart: Int, candidatesEnd: Int
    ) {
        super.onUpdateSelection(oldSelStart, oldSelEnd, newSelStart, newSelEnd, candidatesStart, candidatesEnd)
        debounceJob?.cancel()
        debounceJob = debounceScope.launch {
            delay(300)
            val ic = currentInputConnection ?: return@launch
            val textBeforeCursor = ic.getTextBeforeCursor(500, 0)?.toString() ?: ""
            if (keyboardState.activePanel == ActivePanel.KEYBOARD) {
                keyboardState = keyboardState.copy(contextText = textBeforeCursor)
            }
        }
    }

    override fun onFinishInputView(finishingInput: Boolean) {
        super.onFinishInputView(finishingInput)
        composeLifecycle.onPause()
        composeLifecycle.onStop()
    }

    private fun handleKeyPress(text: String) {
        var actualText = text
        // Handle special T9 "@#" input when there is no composing text
        if (keyboardState.mode == KeyboardMode.T9_PINYIN && text == "1") {
             // if there's no composing text, 1 could trigger punctuation panel, but for now we fallback to standard handling
             // or just ignore if it's strictly a pinyin composer trigger
        }

        if (keyboardState.mode == KeyboardMode.QWERTY_PINYIN || keyboardState.mode == KeyboardMode.T9_PINYIN) {
            val isT9Number = keyboardState.mode == KeyboardMode.T9_PINYIN && actualText.matches(Regex("[2-9]"))
            if (actualText.matches(Regex("[a-zA-Z]+")) || isT9Number) {
                val newComposing = keyboardState.composingText + actualText

                if (isT9Number) {
                    val combinations = ChineseUtils.getT9SyllableCombinations(newComposing)
                    val currentSyllable = combinations.firstOrNull() ?: ""
                    val cands = if (currentSyllable.isNotEmpty()) ChineseUtils.getCandidates(currentSyllable) else emptyList()
                    keyboardState = keyboardState.copy(
                        composingText = newComposing,
                        candidates = cands,
                        t9PinyinCombinations = combinations,
                        selectedT9Syllable = null,
                        isSyllableSelectorExpanded = combinations.size > 1
                    )
                } else {
                    PinyinEngineAdapter.clearComposing()
                    val cands = ChineseUtils.getCandidates(newComposing)
                    keyboardState = keyboardState.copy(
                        composingText = newComposing,
                        candidates = cands,
                        t9PinyinCombinations = emptyList(),
                        isSyllableSelectorExpanded = false
                    )
                }
                return
            }
        }
        commitDirectly(actualText)
    }

    private fun handleDelete() {
        if (keyboardState.composingText.isNotEmpty()) {
            val newComposing = keyboardState.composingText.dropLast(1)

            if (keyboardState.mode == KeyboardMode.T9_PINYIN && newComposing.matches(Regex("[2-9]+"))) {
                val combinations = ChineseUtils.getT9SyllableCombinations(newComposing)
                val currentSyllable = combinations.firstOrNull() ?: ""
                val cands = if (currentSyllable.isNotEmpty()) ChineseUtils.getCandidates(currentSyllable) else emptyList()
                keyboardState = keyboardState.copy(
                    composingText = newComposing,
                    candidates = cands,
                    t9PinyinCombinations = combinations,
                    selectedT9Syllable = null,
                    isSyllableSelectorExpanded = combinations.size > 1
                )
            } else {
                PinyinEngineAdapter.clearComposing()
                val cands = if (newComposing.isNotEmpty()) ChineseUtils.getCandidates(newComposing) else emptyList()
                keyboardState = keyboardState.copy(
                    composingText = newComposing,
                    candidates = cands,
                    t9PinyinCombinations = emptyList(),
                    isSyllableSelectorExpanded = false
                )
            }
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

    private fun handleClear() {
        if (keyboardState.composingText.isNotEmpty()) {
            PinyinEngineAdapter.clearComposing()
            keyboardState = keyboardState.copy(
                composingText = "", candidates = emptyList(),
                t9PinyinCombinations = emptyList(),
                isSyllableSelectorExpanded = false
            )
        }
    }

    private fun handleT9SyllableSelect(syllable: String) {
        val cands = ChineseUtils.getCandidates(syllable)
        keyboardState = keyboardState.copy(
            selectedT9Syllable = syllable,
            candidates = cands,
            isSyllableSelectorExpanded = false // Collapse after selection
        )
    }

    private fun handleCandidateSelect(word: String) {
        val output = if (keyboardState.isTraditional) ChineseUtils.convertToTraditional(word) else word
        commitDirectly(output)
        keyboardState = keyboardState.copy(
            composingText = "", candidates = emptyList(),
            t9PinyinCombinations = emptyList(), isSyllableSelectorExpanded = false
        )
    }

    private fun commitDirectly(text: String) {
        currentInputConnection?.commitText(text, 1)
        if (keyboardState.composingText.isNotEmpty()) {
            currentInputConnection?.commitText(keyboardState.composingText + text, 1)
            keyboardState = keyboardState.copy(
                composingText = "", candidates = emptyList(),
                t9PinyinCombinations = emptyList(), isSyllableSelectorExpanded = false
            )
        }
    }

    private fun handleEnter() {
        if (keyboardState.composingText.isNotEmpty()) {
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
        if (keyboardState.freeUsagesLeft <= 0) {
            keyboardState = keyboardState.copy(showPaywall = true)
            return
        }
        val currentContext = if (keyboardState.composingText.isNotEmpty()) {
            keyboardState.composingText
        } else {
            keyboardState.contextText
        }

        val mockData = listOf(
            AiReplyCategory("高情商", "🍬", listOf("在呼吸，在心跳，在想你呀~", "本来在发呆，看到你的消息心跳就漏了一拍", "在想怎么回复才能让你心动💓")),
            AiReplyCategory("幽默", "😆", listOf("在思考宇宙的终极奥秘...顺便想你", "在进行光合作用", "用意念给你回复中...")),
            AiReplyCategory("暖男", "☀️", listOf("刚忙完，正准备找你呢", "今天过得怎么样？", "无论在哪，我都在你身边"))
        )

        when (action) {
            "帮你回" -> keyboardState = keyboardState.copy(activePanel = ActivePanel.BANG_NI_HUI, contextText = currentContext)
            "超会说", "换个说法" -> {
                keyboardState = keyboardState.copy(activePanel = ActivePanel.CHAO_HUI_SHUO, contextText = currentContext, aiLoading = true, aiMockResults = emptyList())
                composeView.postDelayed({
                    keyboardState = keyboardState.copy(aiLoading = false, aiMockResults = mockData, freeUsagesLeft = keyboardState.freeUsagesLeft - 1)
                }, 1000)
            }
            "Refresh" -> {
                keyboardState = keyboardState.copy(aiLoading = true)
                composeView.postDelayed({
                    keyboardState = keyboardState.copy(aiLoading = false, aiMockResults = mockData.shuffled())
                }, 600)
            }
            "Themes" -> keyboardState = keyboardState.copy(activePanel = ActivePanel.THEME_SELECTION)
            "CloseAi" -> keyboardState = keyboardState.copy(activePanel = ActivePanel.KEYBOARD)
            "PaywallClose" -> keyboardState = keyboardState.copy(showPaywall = false)
            "Purchase" -> keyboardState = keyboardState.copy(showPaywall = false, freeUsagesLeft = 9999)
            else -> {
                if (action.startsWith("SelectTheme:")) {
                    val themeId = action.substringAfter("SelectTheme:")
                    val selectedTheme = ThemePresets.allThemes.find { it.id == themeId } ?: ThemePresets.DefaultBlue
                    keyboardState = keyboardState.copy(currentTheme = selectedTheme, activePanel = ActivePanel.KEYBOARD)
                } else {
                    val finalReply = if (keyboardState.isTraditional) ChineseUtils.convertToTraditional(action) else action
                    commitDirectly(finalReply)
                    keyboardState = keyboardState.copy(activePanel = ActivePanel.KEYBOARD)
                }
            }
        }
    }
}
