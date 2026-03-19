package com.lovekey.clone.ime

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun QWERTYKeyboardLayout(
    mode: KeyboardMode,
    isShifted: Boolean,
    theme: KeyboardTheme,
    onKeyPress: (String) -> Unit,
    onDelete: () -> Unit,
    onEnter: () -> Unit,
    onSwitchMode: (KeyboardMode) -> Unit,
    onToggleShift: () -> Unit,
    onToggleTraditional: () -> Unit,
    isTraditional: Boolean
) {
    val (row1, row2, row3) = when (mode) {
        KeyboardMode.NUMBERS -> listOf(
            listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0"),
            listOf("-", "/", ":", ";", "(", ")", "$", "&", "@", "\""),
            listOf(".", ",", "?", "!", "'")
        )
        KeyboardMode.SYMBOLS -> listOf(
            listOf("[", "]", "{", "}", "#", "%", "^", "*", "+", "="),
            listOf("_", "\\", "|", "~", "<", ">", "€", "£", "¥", "•"),
            listOf(".", ",", "?", "!", "'")
        )
        else -> listOf(
            listOf("q", "w", "e", "r", "t", "y", "u", "i", "o", "p"),
            listOf("a", "s", "d", "f", "g", "h", "j", "k", "l"),
            listOf("z", "x", "c", "v", "b", "n", "m")
        ).map { row ->
            if (isShifted) row.map { it.uppercase() } else row
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
            row1.forEach { key ->
                KeyboardKey(text = key, theme = theme, onClick = { onKeyPress(key) }, modifier = Modifier.weight(1f))
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        ) {
            row2.forEach { key ->
                KeyboardKey(text = key, theme = theme, onClick = { onKeyPress(key) }, modifier = Modifier.weight(1f))
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
            KeyboardKey(text = if (isShifted) "⇧" else "⬆", theme = theme, isActionKey = true, onClick = onToggleShift, modifier = Modifier.weight(1.5f))
            row3.forEach { key ->
                KeyboardKey(text = key, theme = theme, onClick = { onKeyPress(key) }, modifier = Modifier.weight(1f))
            }
            KeyboardKey(text = "⌫", theme = theme, isActionKey = true, onClick = onDelete, modifier = Modifier.weight(1.5f))
        }

        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
            KeyboardKey(
                text = if (mode == KeyboardMode.NUMBERS || mode == KeyboardMode.SYMBOLS) "ABC" else "123",
                isActionKey = true,
                theme = theme,
                onClick = {
                    if (mode == KeyboardMode.NUMBERS || mode == KeyboardMode.SYMBOLS) onSwitchMode(KeyboardMode.QWERTY_PINYIN)
                    else onSwitchMode(KeyboardMode.NUMBERS)
                },
                modifier = Modifier.weight(2f)
            )
            KeyboardKey(text = ",", theme = theme, isActionKey = true, onClick = { onKeyPress(",") }, modifier = Modifier.weight(1f))
            KeyboardKey(text = "Space", theme = theme, isActionKey = false, onClick = { onKeyPress(" ") }, modifier = Modifier.weight(5f))

            val langLabel = when (mode) {
                KeyboardMode.QWERTY_EN -> "En"
                KeyboardMode.QWERTY_PINYIN -> if (isTraditional) "繁" else "简"
                else -> "En"
            }
            KeyboardKey(text = langLabel, theme = theme, isActionKey = true, onClick = {
                if (mode == KeyboardMode.QWERTY_PINYIN) {
                    onToggleTraditional()
                } else {
                    onSwitchMode(if (mode == KeyboardMode.QWERTY_EN) KeyboardMode.QWERTY_PINYIN else KeyboardMode.QWERTY_EN)
                }
            }, modifier = Modifier.weight(1f))

            // "Enter" key has special accent color treatment
            Box(
                modifier = Modifier
                    .weight(2f)
                    .height(42.dp)
                    .shadow(1.dp, RoundedCornerShape(6.dp))
                    .clip(RoundedCornerShape(6.dp))
                    .background(theme.accentColor)
                    .clickable { onEnter() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "发送",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun KeyboardKey(
    text: String,
    theme: KeyboardTheme,
    modifier: Modifier = Modifier,
    isActionKey: Boolean = false,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(42.dp)
            .shadow(1.dp, RoundedCornerShape(6.dp))
            .clip(RoundedCornerShape(6.dp))
            .background(if (isActionKey) theme.actionKeyBackground else theme.keyBackground)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        val textColor = if (isActionKey) theme.actionKeyTextColor else theme.keyTextColor
        Text(
            text = text,
            color = textColor,
            fontSize = if (isActionKey) 14.sp else 20.sp,
            fontWeight = if (isActionKey) FontWeight.Medium else FontWeight.Normal
        )
    }
}
