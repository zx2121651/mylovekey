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
fun T9KeyboardLayout(
    theme: KeyboardTheme,
    onKeyPress: (String) -> Unit,
    onDelete: () -> Unit,
    onEnter: () -> Unit,
    onSwitchMode: (KeyboardMode) -> Unit
) {
    val t9Keys = listOf(
        Pair("1", ""), Pair("2", "ABC"), Pair("3", "DEF"),
        Pair("4", "GHI"), Pair("5", "JKL"), Pair("6", "MNO"),
        Pair("7", "PQRS"), Pair("8", "TUV"), Pair("9", "WXYZ"),
        Pair("符", ""), Pair("0", "␣"), Pair("中/英", "")
    )
    val leftKeys = listOf("，", "。", "？", "！")

    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // Left Punctuation Column
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            leftKeys.forEach { key ->
                T9ActionKey(text = key, theme = theme, onClick = { onKeyPress(key) }, modifier = Modifier.weight(1f))
            }
        }

        // Center T9 Grid
        Column(modifier = Modifier.weight(3f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            for (i in 0 until 4) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.weight(1f)) {
                    for (j in 0 until 3) {
                        val index = i * 3 + j
                        val keyData = t9Keys[index]
                        T9MainKey(
                            mainText = keyData.first,
                            subText = keyData.second,
                            theme = theme,
                            onClick = {
                                if (keyData.first == "中/英") onSwitchMode(KeyboardMode.QWERTY_EN)
                                else if (keyData.first == "符") onSwitchMode(KeyboardMode.SYMBOLS)
                                else onKeyPress(keyData.first)
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // Right Action Column
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            T9ActionKey(text = "⌫", theme = theme, onClick = onDelete, modifier = Modifier.weight(1f))
            T9ActionKey(text = "清空", theme = theme, onClick = { /* TODO clear composing */ }, modifier = Modifier.weight(1f))

            // Enter Key with accent
            Box(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxWidth()
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
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = mainText, color = theme.keyTextColor, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            if (subText.isNotEmpty()) {
                Text(text = subText, color = theme.keyTextColor.copy(alpha = 0.6f), fontSize = 10.sp)
            }
        }
    }
}
