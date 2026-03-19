package com.lovekey.clone.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun DownsellModal(onClose: () -> Unit, onNext: () -> Unit) {
    var timeLeft by remember { mutableIntStateOf(59 * 60 + 58) }

    LaunchedEffect(Unit) {
        while (timeLeft > 0) {
            delay(1000)
            timeLeft--
        }
    }

    val formatTime = { seconds: Int ->
        val m = (seconds / 60).toString().padStart(2, '0')
        val s = (seconds % 60).toString().padStart(2, '0')
        "$m:$s"
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f))
            .clickable { onClose() },
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .background(Color(0xFF0A0A12))
                .clickable(enabled = false) {}
        ) {
            // Radial gradient placeholder using vertical gradient due to limitations
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(Brush.verticalGradient(listOf(Color(0xFF4A3B7A).copy(alpha=0.8f), Color(0xFF15112A).copy(alpha=0.8f), Color(0xFF0A0A12).copy(alpha=0.8f))))
            )

            // Close button
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(20.dp)
                    .size(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White.copy(alpha = 0.1f))
                    .clickable { onClose() },
                contentAlignment = Alignment.Center
            ) {
                Text("×", color = Color.White.copy(alpha = 0.6f), fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.offset(y = (-2).dp))
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 64.dp, start = 24.dp, end = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("限时", color = Color(0xFFB4C5FF), fontSize = 32.sp, fontWeight = FontWeight.Black)
                Text("买一月送一月", color = Color.White, fontSize = 46.sp, fontWeight = FontWeight.Black, letterSpacing = (-1).sp, modifier = Modifier.padding(bottom = 16.dp))
                Text("解锁键盘全部功能不限次", color = Color.White.copy(alpha = 0.8f), fontSize = 15.sp, fontWeight = FontWeight.Medium)

                Column(
                    modifier = Modifier.padding(top = 48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("距活动结束", color = Color(0xFF888888), fontSize = 12.sp, modifier = Modifier.padding(bottom = 8.dp))
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color(0xFF2A2A35))
                            .border(1.dp, Color.White.copy(alpha = 0.05f), CircleShape)
                            .padding(horizontal = 24.dp, vertical = 8.dp)
                    ) {
                        Text(formatTime(timeLeft), color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFF1D1D28))
                        .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(20.dp))
                        .padding(20.dp)
                        .shadow(16.dp)
                ) {
                    Column {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                            Column {
                                Text("特惠月卡买一送一", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 4.dp))
                                Text("买一月送一月", color = Color(0xFF888888), fontSize = 13.sp)
                            }
                            Text("新用户优惠", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.clip(CircleShape).background(Color(0xFFFF4B6B)).padding(horizontal = 12.dp, vertical = 4.dp))
                        }

                        Row(modifier = Modifier.fillMaxWidth().padding(top = 24.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
                            Text("￥96", color = Color(0xFF555555), fontSize = 16.sp, fontWeight = FontWeight.Bold, textDecoration = TextDecoration.LineThrough)
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text("优惠后 ", color = Color(0xFFFF7A92), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Text("￥", color = Color(0xFFFF7A92), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                Text("24", color = Color(0xFFFF7A92), fontSize = 26.sp, fontWeight = FontWeight.Bold)
                                Text("/月", color = Color(0xFFFF7A92), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, bottom = 48.dp, top = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .clip(CircleShape)
                        .background(Brush.horizontalGradient(listOf(Color(0xFF6E7BFF), Color(0xFF8C64FF))))
                        .clickable { onNext() }
                        .shadow(20.dp, spotColor = Color(0x666E7BFF)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("领取优惠", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                }
            }
        }
    }
}
