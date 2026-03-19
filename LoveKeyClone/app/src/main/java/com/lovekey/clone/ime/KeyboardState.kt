package com.lovekey.clone.ime

enum class KeyboardMode {
    QWERTY_EN,
    QWERTY_PINYIN,
    T9_PINYIN,
    NUMBERS,
    SYMBOLS
}

enum class ActivePanel {
    KEYBOARD,
    CHAO_HUI_SHUO,
    BANG_NI_HUI,
    THEME_SELECTION
}

data class AiReplyCategory(
    val categoryName: String,
    val icon: String,
    val replies: List<String>
)

data class KeyboardState(
    val mode: KeyboardMode = KeyboardMode.QWERTY_PINYIN,
    val activePanel: ActivePanel = ActivePanel.KEYBOARD,
    val isTraditional: Boolean = false,
    val isShifted: Boolean = false,
    val composingText: String = "",
    val candidates: List<String> = emptyList(),
    // AI Mock Data State
    val freeUsagesLeft: Int = 3,
    val showPaywall: Boolean = false,
    val aiLoading: Boolean = false,
    val contextText: String = "",
    val aiMockResults: List<AiReplyCategory> = emptyList(),
    // Themes
    val currentTheme: KeyboardTheme = ThemePresets.DefaultBlue
)
