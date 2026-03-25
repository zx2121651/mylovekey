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

    // Classic Sogou 9-key grid layout (3x4 center + right actions)
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // Center T9 Grid (3 columns)
        Column(modifier = Modifier.weight(3f).fillMaxHeight(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.weight(1f)) {
                T9MainKey(mainText = "1", subText = "符/分词", theme = theme, onClick = { haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove); onKeyPress("1") }, modifier = Modifier.weight(1f))
                T9MainKey(mainText = "2", subText = "ABC", theme = theme, onClick = { haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove); onKeyPress("2") }, modifier = Modifier.weight(1f))
                T9MainKey(mainText = "3", subText = "DEF", theme = theme, onClick = { haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove); onKeyPress("3") }, modifier = Modifier.weight(1f))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.weight(1f)) {
                T9MainKey(mainText = "4", subText = "GHI", theme = theme, onClick = { haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove); onKeyPress("4") }, modifier = Modifier.weight(1f))
                T9MainKey(mainText = "5", subText = "JKL", theme = theme, onClick = { haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove); onKeyPress("5") }, modifier = Modifier.weight(1f))
                T9MainKey(mainText = "6", subText = "MNO", theme = theme, onClick = { haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove); onKeyPress("6") }, modifier = Modifier.weight(1f))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.weight(1f)) {
                T9MainKey(mainText = "7", subText = "PQRS", theme = theme, onClick = { haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove); onKeyPress("7") }, modifier = Modifier.weight(1f))
                T9MainKey(mainText = "8", subText = "TUV", theme = theme, onClick = { haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove); onKeyPress("8") }, modifier = Modifier.weight(1f))
                T9MainKey(mainText = "9", subText = "WXYZ", theme = theme, onClick = { haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove); onKeyPress("9") }, modifier = Modifier.weight(1f))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.weight(1f)) {
                T9ActionKey(text = "符", theme = theme, onClick = { haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove); onSwitchMode(KeyboardMode.SYMBOLS) }, modifier = Modifier.weight(1f))
                T9MainKey(mainText = "0", subText = "␣", theme = theme, onClick = { haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove); onKeyPress("0") }, modifier = Modifier.weight(1f))
                T9ActionKey(text = "中/英", theme = theme, onClick = { haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove); onSwitchMode(KeyboardMode.QWERTY_EN) }, modifier = Modifier.weight(1f))
            }
        }

        // Right Action Column (1 column)
        Column(modifier = Modifier.weight(1f).fillMaxHeight(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            T9ActionKey(text = "⌫", theme = theme, onClick = { haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove); onDelete() }, modifier = Modifier.weight(1f))
            T9ActionKey(text = "重输", theme = theme, onClick = { haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove); onClear() }, modifier = Modifier.weight(1f))

            // Large Enter/Search Key spanning 2 rows
            Box(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxWidth()
                    .shadow(1.dp, RoundedCornerShape(6.dp))
                    .clip(RoundedCornerShape(6.dp))
                    .background(theme.accentColor)
                    .clickable {
                         haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                         onEnter()
                    },
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
        Text(text = text, color = theme.actionKeyTextColor, fontSize = 16.sp)
    }
}

@Composable
fun T9MainKey(mainText: String, subText: String, theme: KeyboardTheme, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(1.dp, RoundedCornerShape(6.dp))
            .clip(RoundedCornerShape(6.dp))
            .background(theme.keyBackground)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        // Sogou Style: letters large in the middle/bottom, numbers small on top
         Box(modifier = Modifier.fillMaxSize()) {
             Text(
                 text = mainText,
                 color = theme.keyTextColor.copy(alpha = 0.5f),
                 fontSize = 12.sp,
                 modifier = Modifier.align(Alignment.TopCenter).padding(top = 4.dp)
             )
             if (subText.isNotEmpty()) {
                 Text(
                     text = subText,
                     color = theme.keyTextColor,
                     fontSize = if (subText.length > 3) 14.sp else 18.sp,
                     fontWeight = FontWeight.Bold,
                     modifier = Modifier.align(Alignment.Center).padding(top = 8.dp)
                 )
             }
         }
    }
}
