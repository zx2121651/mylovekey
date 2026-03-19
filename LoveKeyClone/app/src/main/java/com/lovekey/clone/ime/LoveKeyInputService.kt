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

class LoveKeyInputService : InputMethodService() {

    private lateinit var composeView: ComposeView
    private val composeLifecycle = ComposeIMELifecycle()

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
                        onKeyPress = { handleKeyPress(it) },
                        onDelete = { handleDelete() },
                        onEnter = { handleEnter() },
                        onAiAction = { triggerAiReply(it) }
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
        currentInputConnection?.commitText(text, 1)
    }

    private fun handleDelete() {
        val ic = currentInputConnection ?: return
        val selectedText = ic.getSelectedText(0)
        if (selectedText.isNullOrEmpty()) {
            ic.deleteSurroundingText(1, 0)
        } else {
            ic.commitText("", 1)
        }
    }

    private fun handleEnter() {
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

        // 读取光标前面的上下文
        val contextText = ic.getTextBeforeCursor(200, 0)?.toString() ?: ""

        // 根据不同标签 Mock 出对应的 AI 回复
        val mockReplies = mapOf(
            "高情商" to "在呼吸，在心跳，在想你呀~",
            "心动狙击" to "在想怎么回复才能让你心动💓",
            "幽默" to "在思考宇宙的终极奥秘...顺便想你",
            "暖男" to "刚忙完，正准备找你呢，你今天累不累？",
            "暧昧拉扯" to "你猜猜看？猜对有奖哦~",
            "情场高手" to "本来在发呆，看到你的消息心跳就漏了一拍"
        )

        val reply = mockReplies[tag] ?: "[$tag] 回复: ${if(contextText.isNotEmpty()) contextText else "你好"}"

        // 将 AI 回复输入到当前应用
        ic.commitText(reply, 1)
    }
}
