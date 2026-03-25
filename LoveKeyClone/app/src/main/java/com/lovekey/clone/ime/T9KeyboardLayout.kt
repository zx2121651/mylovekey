package com.lovekey.clone.ime

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons



import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.sp

@Composable
fun T9KeyboardLayout(
    theme: KeyboardTheme,
    onKeyPress: (String) -> Unit,
    onDelete: () -> Unit,
    onClear: () -> Unit,
    onEnter: () -> Unit,
    onSwitchMode: (KeyboardMode) -> Unit
) {
    val haptic = LocalHapticFeedback.current

    // 5-column layout similar to modern Sogou (Left Punctuation + 3x4 Center + Right Actions)
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // --- 1. Left Punctuation Column (1 column) ---
        Column(modifier = Modifier.weight(0.9f).fillMaxHeight(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            T9ActionKey(text = "，", theme = theme, onClick = { haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove); onKeyPress("，") }, modifier = Modifier.weight(0.8f))
            T9ActionKey(text = "。", theme = theme, onClick = { haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove); onKeyPress("。") }, modifier = Modifier.weight(0.8f))
            T9ActionKey(text = "？", theme = theme, onClick = { haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove); onKeyPress("？") }, modifier = Modifier.weight(0.8f))
            T9ActionKey(text = "！", theme = theme, onClick = { haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove); onKeyPress("！") }, modifier = Modifier.weight(0.8f))
            T9ActionKey(text = "符号", theme = theme, onClick = { haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove); onSwitchMode(KeyboardMode.SYMBOLS) }, modifier = Modifier.weight(0.8f))
        }

        // --- 2. Center T9 Grid (3 columns) ---
        Column(modifier = Modifier.weight(3.5f).fillMaxHeight(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.weight(1f)) {
                T9MainKey(mainText = "@#", theme = theme, onClick = { haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove); onKeyPress("1") }, modifier = Modifier.weight(1f))
                T9MainKey(mainText = "ABC", theme = theme, onClick = { haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove); onKeyPress("2") }, modifier = Modifier.weight(1f))
                T9MainKey(mainText = "DEF", theme = theme, onClick = { haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove); onKeyPress("3") }, modifier = Modifier.weight(1f))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.weight(1f)) {
                T9MainKey(mainText = "GHI", theme = theme, onClick = { haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove); onKeyPress("4") }, modifier = Modifier.weight(1f))
                T9MainKey(mainText = "JKL", theme = theme, onClick = { haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove); onKeyPress("5") }, modifier = Modifier.weight(1f))
                T9MainKey(mainText = "MNO", theme = theme, onClick = { haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove); onKeyPress("6") }, modifier = Modifier.weight(1f))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.weight(1f)) {
                T9MainKey(mainText = "PQRS", theme = theme, onClick = { haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove); onKeyPress("7") }, modifier = Modifier.weight(1f))
                T9MainKey(mainText = "TUV", theme = theme, onClick = { haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove); onKeyPress("8") }, modifier = Modifier.weight(1f))
                T9MainKey(mainText = "WXYZ", theme = theme, onClick = { haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove); onKeyPress("9") }, modifier = Modifier.weight(1f))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.weight(1f)) {
                T9ActionKey(text = "123", theme = theme, onClick = { haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove); onSwitchMode(KeyboardMode.NUMBERS) }, modifier = Modifier.weight(1f))

                // Voice / Space Key
                Box(
                    modifier = Modifier.weight(1f).fillMaxHeight()
                        .shadow(1.dp, RoundedCornerShape(6.dp)).clip(RoundedCornerShape(6.dp))
                        .background(theme.actionKeyBackground).clickable { haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove); onKeyPress(" ") },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "空格", color = theme.actionKeyTextColor, fontSize = 16.sp)
                }

                // EN/CH Switch
                T9ActionKey(text = "中/英", theme = theme, onClick = { haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove); onSwitchMode(KeyboardMode.QWERTY_EN) }, modifier = Modifier.weight(1f))
            }
        }

        // --- 3. Right Action Column (1 column) ---
        Column(modifier = Modifier.weight(1.2f).fillMaxHeight(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            T9ActionKey(text = "⌫", theme = theme, onClick = { haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove); onDelete() }, modifier = Modifier.weight(1f))
            T9ActionKey(text = "重输", theme = theme, onClick = { haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove); onClear() }, modifier = Modifier.weight(1f))

            // Large Enter Key spanning 2 rows
            Box(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxWidth()
                    .shadow(1.dp, RoundedCornerShape(6.dp))
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFFB0B5C1)) // A classic grayish blue for enter like Sogou
                    .clickable {
                         haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                         onEnter()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "换行",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun T9ActionKey(text: String, theme: KeyboardTheme, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(1.dp, RoundedCornerShape(6.dp))
            .clip(RoundedCornerShape(6.dp))
            .background(theme.actionKeyBackground)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (text == "⌫") {
            Text(text = "⌫", color = theme.actionKeyTextColor, fontSize = 18.sp)
        } else {
            Text(text = text, color = theme.actionKeyTextColor, fontSize = 14.sp)
        }
    }
}

@Composable
fun T9MainKey(mainText: String, theme: KeyboardTheme, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .shadow(1.dp, RoundedCornerShape(6.dp))
            .clip(RoundedCornerShape(6.dp))
            .background(theme.keyBackground)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
         Text(
             text = mainText,
             color = theme.keyTextColor,
             fontSize = if (mainText.length > 3) 14.sp else 18.sp,
             fontWeight = FontWeight.Normal
         )
    }
}
