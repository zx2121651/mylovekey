package com.example.lovekeyclone

import android.inputmethodservice.InputMethodService
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.Toast

class LoveKeyService : InputMethodService() {

    private lateinit var keyboardView: View

    override fun onCreateInputView(): View {
        keyboardView = layoutInflater.inflate(R.layout.keyboard_view, null)

        val btnAiReply = keyboardView.findViewById<Button>(R.id.btn_ai_reply)
        val btnSimulateTyping = keyboardView.findViewById<Button>(R.id.btn_simulate_typing)
        val btnPersonality = keyboardView.findViewById<Button>(R.id.btn_personality)

        btnAiReply.setOnClickListener {
            // Mock AI Reply behavior
            val mockAiResponse = "哈哈，你真幽默！"
            val inputConnection = currentInputConnection
            if (inputConnection != null) {
                inputConnection.commitText(mockAiResponse, 1)
                Toast.makeText(this, "AI已生成回复", Toast.LENGTH_SHORT).show()
            }
        }

        btnSimulateTyping.setOnClickListener {
            val inputConnection = currentInputConnection
            inputConnection?.commitText("Hello ", 1)
        }

        btnPersonality.setOnClickListener {
            Toast.makeText(this, "打开人设设置界面", Toast.LENGTH_SHORT).show()
        }

        return keyboardView
    }

    override fun onStartInputView(info: EditorInfo?, restarting: Boolean) {
        super.onStartInputView(info, restarting)
        // Keyboard is shown, can do something here if needed
    }
}
