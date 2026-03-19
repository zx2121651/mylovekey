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

class LoveKeyInputService : InputMethodService() {

    private lateinit var composeView: ComposeView
    private val composeLifecycle = ComposeIMELifecycle()
    private var keyboardState by mutableStateOf(KeyboardState())

    override fun onCreate() {
        super.onCreate()
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
                        onAiAction = { triggerAiReply(it) },
                        onSwitchMode = { newMode -> keyboardState = keyboardState.copy(mode = newMode) },
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
        keyboardState = keyboardState.copy(composingText = "", candidates = emptyList())
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
            // Letters go into composing text if in Pinyin mode
            if (text.matches(Regex("[a-zA-Z]+"))) {
                val newComposing = keyboardState.composingText + text
                val cands = ChineseUtils.getCandidates(newComposing)
                keyboardState = keyboardState.copy(composingText = newComposing, candidates = cands)
                return
            }
        }

        // Non-letter or not in Pinyin mode: commit directly
        commitDirectly(text)
    }

    private fun handleDelete() {
        if (keyboardState.composingText.isNotEmpty()) {
            val newComposing = keyboardState.composingText.dropLast(1)
            val cands = if (newComposing.isNotEmpty()) ChineseUtils.getCandidates(newComposing) else emptyList()
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

    private fun triggerAiReply(tag: String) {
        val ic = currentInputConnection ?: return
        val contextText = ic.getTextBeforeCursor(200, 0)?.toString() ?: ""
        val mockReplies = mapOf(
            "高情商" to "在呼吸，在心跳，在想你呀~",
            "心动狙击" to "在想怎么回复才能让你心动💓",
            "幽默" to "在思考宇宙的终极奥秘...顺便想你",
            "暖男" to "刚忙完，正准备找你呢，你今天累不累？",
            "暧昧拉扯" to "你猜猜看？猜对有奖哦~",
            "情场高手" to "本来在发呆，看到你的消息心跳就漏了一拍"
        )
        val reply = mockReplies[tag] ?: "[$tag] 回复: \${if(contextText.isNotEmpty()) contextText else \"你好\"}"
        val finalReply = if (keyboardState.isTraditional) ChineseUtils.convertToTraditional(reply) else reply
        ic.commitText(finalReply, 1)
    }
}
