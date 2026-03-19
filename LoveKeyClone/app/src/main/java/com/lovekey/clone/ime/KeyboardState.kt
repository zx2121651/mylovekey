package com.lovekey.clone.ime

enum class KeyboardMode {
    QWERTY_EN,
    QWERTY_PINYIN,
    T9_PINYIN,
    NUMBERS,
    SYMBOLS
}

data class KeyboardState(
    val mode: KeyboardMode = KeyboardMode.QWERTY_PINYIN,
    val isTraditional: Boolean = false,
    val isShifted: Boolean = false,
    val composingText: String = "",
    val candidates: List<String> = emptyList()
)
