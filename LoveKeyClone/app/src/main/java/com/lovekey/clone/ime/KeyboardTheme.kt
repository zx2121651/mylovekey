package com.lovekey.clone.ime

import androidx.compose.ui.graphics.Color

data class KeyboardTheme(
    val id: String,
    val name: String,
    val keyboardBackground: Color,
    val keyBackground: Color,
    val keyTextColor: Color,
    val actionKeyBackground: Color,
    val actionKeyTextColor: Color,
    val candidateStripBackground: Color,
    val candidateTextColor: Color,
    val accentColor: Color,
    val toolbarBackground: Color,
    val toolbarIconColor: Color
)

object ThemePresets {
    val DefaultBlue = KeyboardTheme(
        id = "default_blue",
        name = "经典蓝",
        keyboardBackground = Color(0xFFE2E6EF),
        keyBackground = Color(0xFFFFFFFF),
        keyTextColor = Color(0xFF1A1A1A),
        actionKeyBackground = Color(0xFFD3D8E6),
        actionKeyTextColor = Color(0xFF333333),
        candidateStripBackground = Color(0xFFFFFFFF),
        candidateTextColor = Color(0xFF1A1A1A),
        accentColor = Color(0xFF5C73FF),
        toolbarBackground = Color(0xFFFFFFFF),
        toolbarIconColor = Color(0xFF555555)
    )

    val RomanticPink = KeyboardTheme(
        id = "romantic_pink",
        name = "粉色浪漫",
        keyboardBackground = Color(0xFFFCE4EC),
        keyBackground = Color(0xFFFFFFFF),
        keyTextColor = Color(0xFF4A148C),
        actionKeyBackground = Color(0xFFF8BBD0),
        actionKeyTextColor = Color(0xFF880E4F),
        candidateStripBackground = Color(0xFFFFF0F5),
        candidateTextColor = Color(0xFF4A148C),
        accentColor = Color(0xFFE91E63),
        toolbarBackground = Color(0xFFFFFFFF),
        toolbarIconColor = Color(0xFFC2185B)
    )

    val DarkNight = KeyboardTheme(
        id = "dark_night",
        name = "暗夜黑",
        keyboardBackground = Color(0xFF1C1C1E),
        keyBackground = Color(0xFF2C2C2E),
        keyTextColor = Color(0xFFFFFFFF),
        actionKeyBackground = Color(0xFF3A3A3C),
        actionKeyTextColor = Color(0xFFEBEBF5),
        candidateStripBackground = Color(0xFF2C2C2E),
        candidateTextColor = Color(0xFFFFFFFF),
        accentColor = Color(0xFF0A84FF),
        toolbarBackground = Color(0xFF2C2C2E),
        toolbarIconColor = Color(0xFFAFAFAF)
    )

    val MatchaGreen = KeyboardTheme(
        id = "matcha_green",
        name = "抹茶绿",
        keyboardBackground = Color(0xFFE8F5E9),
        keyBackground = Color(0xFFFFFFFF),
        keyTextColor = Color(0xFF1B5E20),
        actionKeyBackground = Color(0xFFC8E6C9),
        actionKeyTextColor = Color(0xFF2E7D32),
        candidateStripBackground = Color(0xFFF1F8E9),
        candidateTextColor = Color(0xFF1B5E20),
        accentColor = Color(0xFF4CAF50),
        toolbarBackground = Color(0xFFFFFFFF),
        toolbarIconColor = Color(0xFF388E3C)
    )

    val allThemes = listOf(DefaultBlue, RomanticPink, DarkNight, MatchaGreen)
}
