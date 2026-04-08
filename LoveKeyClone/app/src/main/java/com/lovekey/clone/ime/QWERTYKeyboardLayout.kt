package com.lovekey.clone.ime

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun QWERTYKeyboardLayout(
    mode: KeyboardMode,
    isShifted: Boolean,
    theme: KeyboardTheme,
    onKeyPress: (String) -> Unit,
    onDelete: () -> Unit,
    onClear: () -> Unit,
    onMoveCursor: (Int) -> Unit,
    onKeyActionStart: (String, Offset) -> Unit,
    onKeyActionEnd: () -> Unit,
    onEnter: () -> Unit,
    onSwitchMode: (KeyboardMode) -> Unit,
    onToggleShift: () -> Unit,
    onToggleTraditional: () -> Unit,
    isTraditional: Boolean
) {
    val coroutineScope = rememberCoroutineScope()
    var isDeleting by remember { mutableStateOf(false) }

    val rows = when (mode) {
        KeyboardMode.QWERTY_EN -> listOf(
            listOf("q", "w", "e", "r", "t", "y", "u", "i", "o", "p"),
            listOf("a", "s", "d", "f", "g", "h", "j", "k", "l"),
            listOf("SHIFT", "z", "x", "c", "v", "b", "n", "m", "DEL")
        )
        KeyboardMode.QWERTY_PINYIN -> listOf(
            listOf("q", "w", "e", "r", "t", "y", "u", "i", "o", "p"),
            listOf("a", "s", "d", "f", "g", "h", "j", "k", "l"),
            listOf("SHIFT", "z", "x", "c", "v", "b", "n", "m", "DEL")
        )
        KeyboardMode.SYMBOLS -> listOf(
            listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0"),
            listOf("-", "/", ":", ";", "(", ")", "$", "&", "@", "\""),
            listOf("SYMBOLS2", ".", ",", "?", "!", "'", "DEL")
        )
        else -> emptyList()
    }

    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp)) {
        rows.forEachIndexed { rowIndex, row ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                row.forEach { key ->
                    val isSpecialKey = key in listOf("SHIFT", "DEL", "SYMBOLS2")
                    val keyWeight = if (isSpecialKey) 1.5f else 1f

                    var keyPosition by remember { mutableStateOf(Offset.Zero) }

                    Box(
                        modifier = Modifier
                            .weight(keyWeight)
                            .padding(horizontal = 2.dp)
                            .height(48.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSpecialKey) theme.actionKeyBackground else theme.keyBackground)
                            .onGloballyPositioned { coordinates ->
                                keyPosition = coordinates.positionInWindow()
                            }
                            .pointerInput(key) {
                                detectTapGestures(
                                    onPress = {
                                        if (key == "DEL") {
                                            isDeleting = true
                                            onDelete()
                                            coroutineScope.launch {
                                                delay(400)
                                                while (isDeleting) {
                                                    onDelete()
                                                    delay(50)
                                                }
                                            }
                                        } else if (!isSpecialKey) {
                                            val displayKey = if (isShifted) key.uppercase() else key
                                            onKeyActionStart(displayKey, keyPosition)
                                        }

                                        try {
                                            awaitRelease()
                                        } finally {
                                            if (key == "DEL") {
                                                isDeleting = false
                                            } else if (!isSpecialKey) {
                                                onKeyActionEnd()
                                                val displayKey = if (isShifted) key.uppercase() else key
                                                onKeyPress(displayKey)
                                            } else {
                                                when (key) {
                                                    "SHIFT" -> onToggleShift()
                                                    "SYMBOLS2" -> onSwitchMode(KeyboardMode.QWERTY_EN)
                                                }
                                            }
                                        }
                                    }
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = when (key) {
                                "SHIFT" -> if (isShifted) "⬆" else "⇧"
                                "DEL" -> "⌫"
                                "SYMBOLS2" -> "#+="
                                else -> if (isShifted && mode != KeyboardMode.SYMBOLS) key.uppercase() else key
                            },
                            color = theme.keyTextColor,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // Bottom Row
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            // Mode Switch Key
            Box(
                modifier = Modifier.weight(1.5f).padding(horizontal = 2.dp).height(48.dp)
                    .clip(RoundedCornerShape(8.dp)).background(theme.actionKeyBackground)
                    .clickable {
                        onSwitchMode(if (mode == KeyboardMode.SYMBOLS) KeyboardMode.QWERTY_EN else KeyboardMode.SYMBOLS)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(if (mode == KeyboardMode.SYMBOLS) "ABC" else "?123", color = theme.keyTextColor, fontSize = 16.sp)
            }

            // Pinyin/English Toggle or T9 Toggle
            if (mode != KeyboardMode.SYMBOLS) {
                Box(
                    modifier = Modifier.weight(1f).padding(horizontal = 2.dp).height(48.dp)
                        .clip(RoundedCornerShape(8.dp)).background(theme.actionKeyBackground)
                        .clickable {
                            if (mode == KeyboardMode.QWERTY_EN) onSwitchMode(KeyboardMode.QWERTY_PINYIN)
                            else onSwitchMode(KeyboardMode.QWERTY_EN)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(if (mode == KeyboardMode.QWERTY_EN) "中" else "EN", color = theme.keyTextColor, fontSize = 16.sp)
                }

                if (mode == KeyboardMode.QWERTY_PINYIN) {
                    Box(
                        modifier = Modifier.weight(1f).padding(horizontal = 2.dp).height(48.dp)
                            .clip(RoundedCornerShape(8.dp)).background(theme.actionKeyBackground)
                            .clickable { onSwitchMode(KeyboardMode.T9_PINYIN) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("T9", color = theme.keyTextColor, fontSize = 16.sp)
                    }
                    Box(
                        modifier = Modifier.weight(1f).padding(horizontal = 2.dp).height(48.dp)
                            .clip(RoundedCornerShape(8.dp)).background(theme.actionKeyBackground)
                            .clickable { onToggleTraditional() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(if (isTraditional) "简" else "繁", color = theme.keyTextColor, fontSize = 16.sp)
                    }
                }
            }

            // Spacebar Key
            var lastDragAmount = 0f
            Box(
                modifier = Modifier.weight(if (mode == KeyboardMode.QWERTY_PINYIN) 3f else 4f).padding(horizontal = 2.dp).height(48.dp)
                    .clip(RoundedCornerShape(8.dp)).background(theme.keyBackground)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = { onKeyPress(" ") }
                        )
                    }
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures(
                            onDragStart = { lastDragAmount = 0f },
                            onHorizontalDrag = { change, dragAmount ->
                                change.consume()
                                lastDragAmount += dragAmount
                                val threshold = 30f // Adjust threshold for sensitivity
                                if (lastDragAmount > threshold) {
                                    onMoveCursor(1)
                                    lastDragAmount = 0f
                                } else if (lastDragAmount < -threshold) {
                                    onMoveCursor(-1)
                                    lastDragAmount = 0f
                                }
                            }
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(if (mode == KeyboardMode.QWERTY_EN) "English" else "Space", color = theme.keyTextColor, fontSize = 16.sp)
            }

            // Enter Key
            Box(
                modifier = Modifier.weight(1.5f).padding(horizontal = 2.dp).height(48.dp)
                    .clip(RoundedCornerShape(8.dp)).background(theme.accentColor)
                    .clickable { onEnter() },
                contentAlignment = Alignment.Center
            ) {
                Text("Enter", color = Color.White, fontSize = 16.sp)
            }
        }
    }
}
