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
fun T9KeyboardLayout(
    onKeyPress: (String) -> Unit,
    onDelete: () -> Unit,
    onEnter: () -> Unit,
    onSwitchMode: (KeyboardMode) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 4.dp, end = 4.dp, bottom = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // Left Column: Symbols
        Column(
            modifier = Modifier
                .weight(1.2f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            val symbols = listOf("，", "。", "?", "!", "符号")
            symbols.forEachIndexed { index, sym ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFFD3D8E6))
                        .clickable(interactionSource = remember { MutableInteractionSource() }, indication = rememberRipple()) {
                            if (sym == "符号") onSwitchMode(KeyboardMode.SYMBOLS)
                            else onKeyPress(sym)
                        }
                        .shadow(2.dp, spotColor = Color(0x1A000000)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(sym, fontSize = 14.sp, color = Color(0xFF333333))
                }
            }
        }

        // Middle Columns: T9 Grid (3x4)
        Column(
            modifier = Modifier
                .weight(3f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            val t9Keys = listOf(
                listOf("1" to "", "2" to "ABC", "3" to "DEF"),
                listOf("4" to "GHI", "5" to "JKL", "6" to "MNO"),
                listOf("7" to "PQRS", "8" to "TUV", "9" to "WXYZ"),
                listOf("拼" to "全键", "0" to "空格", "中" to "EN")
            )
            t9Keys.forEach { row ->
                Row(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    row.forEach { (main, sub) ->
                        val isActionKey = main == "拼" || main == "中"
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isActionKey) Color(0xFFD3D8E6) else Color.White)
                                .clickable(interactionSource = remember { MutableInteractionSource() }, indication = rememberRipple()) {
                                    when (main) {
                                        "0" -> onKeyPress(" ")
                                        "拼" -> onSwitchMode(KeyboardMode.QWERTY_PINYIN)
                                        "中" -> onSwitchMode(KeyboardMode.QWERTY_EN)
                                        else -> onKeyPress(main)
                                    }
                                }
                                .shadow(2.dp, spotColor = Color(0x1A000000)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(main, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A1A))
                                if (sub.isNotEmpty()) {
                                    Text(sub, fontSize = 10.sp, color = Color(0xFF888888))
                                }
                            }
                        }
                    }
                }
            }
        }

        // Right Column: Actions
        Column(
            modifier = Modifier
                .weight(1.2f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFFD3D8E6))
                    .clickable(interactionSource = remember { MutableInteractionSource() }, indication = rememberRipple()) { onDelete() }
                    .shadow(2.dp, spotColor = Color(0x1A000000)),
                contentAlignment = Alignment.Center
            ) {
                Text("⌫", fontSize = 18.sp, color = Color(0xFF333333))
            }
            Box(
                modifier = Modifier
                    .weight(1.5f)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFFD3D8E6))
                    .clickable(interactionSource = remember { MutableInteractionSource() }, indication = rememberRipple()) { onEnter() }
                    .shadow(2.dp, spotColor = Color(0x1A000000)),
                contentAlignment = Alignment.Center
            ) {
                Text("换行", fontSize = 14.sp, color = Color(0xFF333333))
            }
            Box(
                modifier = Modifier
                    .weight(1.5f)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFF5C73FF))
                    .clickable(interactionSource = remember { MutableInteractionSource() }, indication = rememberRipple()) { onEnter() }
                    .shadow(2.dp, spotColor = Color(0x1A000000)),
                contentAlignment = Alignment.Center
            ) {
                Text("发送", fontSize = 14.sp, color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}
