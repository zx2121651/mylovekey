package com.lovekey.clone.ime

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
    onKeyPress: (String) -> Unit,
    onDelete: () -> Unit,
    onEnter: () -> Unit,
    onSwitchMode: (KeyboardMode) -> Unit,
    onToggleShift: () -> Unit,
    onToggleTraditional: () -> Unit,
    isTraditional: Boolean
) {
    val rows = if (mode == KeyboardMode.NUMBERS) {
        listOf(
            listOf("1","2","3","4","5","6","7","8","9","0"),
            listOf("-","/",":",";","(",")","$","&","@","\""),
            listOf("符号", ".",",","?","!","'","⌫")
        )
    } else if (mode == KeyboardMode.SYMBOLS) {
        listOf(
            listOf("[","]","{","}","#","%","^","*","+","="),
            listOf("_","\\","|","~","<",">","€","£","¥","•"),
            listOf("数字", ".",",","?","!","'","⌫")
        )
    } else {
        listOf(
            listOf("Q","W","E","R","T","Y","U","I","O","P"),
            listOf("A","S","D","F","G","H","J","K","L"),
            listOf("⇧", "Z","X","C","V","B","N","M", "⌫")
        )
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(start = 4.dp, end = 4.dp, bottom = 4.dp),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        rows.forEachIndexed { rowIndex, rowKeys ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = if (rowIndex == 1 && (mode == KeyboardMode.QWERTY_EN || mode == KeyboardMode.QWERTY_PINYIN)) 16.dp else 0.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                rowKeys.forEach { key ->
                    val isSpecial = key == "⇧" || key == "⌫" || key == "符号" || key == "数字"
                    Box(
                        modifier = Modifier
                            .weight(if (isSpecial) 1.5f else 1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isSpecial) Color(0xFFB4BACC) else Color.White)
                            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = rememberRipple()) {
                                when (key) {
                                    "⌫" -> onDelete()
                                    "⇧" -> onToggleShift()
                                    "符号" -> onSwitchMode(KeyboardMode.SYMBOLS)
                                    "数字" -> onSwitchMode(KeyboardMode.NUMBERS)
                                    else -> {
                                        val output = if (isShifted && key.length == 1) key.uppercase() else key.lowercase()
                                        onKeyPress(output)
                                    }
                                }
                            }
                            .shadow(if (isSpecial) 0.dp else 2.dp, spotColor = Color(0x1A000000)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(if (isShifted && key.length == 1 && !isSpecial) key.uppercase() else key, fontSize = 18.sp, color = Color(0xFF1A1A1A))
                    }
                }
            }
        }

        // Bottom Action Row
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            val isPinyinMode = mode == KeyboardMode.QWERTY_PINYIN
            val isEnglishMode = mode == KeyboardMode.QWERTY_EN

            val toggleKey = when {
                isPinyinMode -> "中"
                isEnglishMode -> "EN"
                else -> "ABC"
            }

            Box(
                modifier = Modifier.weight(1.5f).fillMaxHeight().clip(RoundedCornerShape(6.dp)).background(Color(0xFFB4BACC)).clickable {
                    if (mode == KeyboardMode.NUMBERS || mode == KeyboardMode.SYMBOLS) {
                        onSwitchMode(KeyboardMode.QWERTY_PINYIN)
                    } else {
                        onSwitchMode(KeyboardMode.NUMBERS)
                    }
                },
                contentAlignment = Alignment.Center
            ) {
                Text(if (mode == KeyboardMode.NUMBERS || mode == KeyboardMode.SYMBOLS) "返回" else "?123", fontSize = 14.sp)
            }

            Box(
                modifier = Modifier.weight(1f).fillMaxHeight().clip(RoundedCornerShape(6.dp)).background(Color(0xFFB4BACC)).clickable {
                    if (isPinyinMode) onSwitchMode(KeyboardMode.QWERTY_EN) else onSwitchMode(KeyboardMode.QWERTY_PINYIN)
                },
                contentAlignment = Alignment.Center
            ) {
                Text(toggleKey, fontSize = 14.sp)
            }

            if (isPinyinMode) {
                 Box(
                    modifier = Modifier.weight(1f).fillMaxHeight().clip(RoundedCornerShape(6.dp)).background(if(isTraditional) Color(0xFF5C73FF) else Color(0xFFB4BACC)).clickable { onToggleTraditional() },
                    contentAlignment = Alignment.Center
                ) {
                    Text("繁", fontSize = 14.sp, color = if(isTraditional) Color.White else Color(0xFF1A1A1A))
                }
            }

            Box(
                modifier = Modifier.weight(if(isPinyinMode) 3f else 4f).fillMaxHeight().clip(RoundedCornerShape(6.dp)).background(Color.White).clickable { onKeyPress(" ") }.shadow(2.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("空格", fontSize = 14.sp)
            }

            Box(
                modifier = Modifier.weight(1.5f).fillMaxHeight().clip(RoundedCornerShape(6.dp)).background(Color(0xFF5C73FF)).clickable { onEnter() },
                contentAlignment = Alignment.Center
            ) {
                Text("发送", fontSize = 14.sp, color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}
