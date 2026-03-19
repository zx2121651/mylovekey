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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun VipPaywallOverlay(
    onClose: () -> Unit,
    onPurchase: () -> Unit
) {
    // Semi-transparent backdrop
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x99000000))
            .clickable(onClick = onClose),
        contentAlignment = Alignment.BottomCenter
    ) {
        // Paywall Bottom Sheet
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(Color(0xFF0A0A12))
                .padding(24.dp)
                .clickable(enabled = false) {}, // Consume clicks so they don't close the overlay
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.TopEnd
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0x1AFFFFFF))
                        .clickable { onClose() },
                    contentAlignment = Alignment.Center
                ) {
                    Text("✕", color = Color(0x99FFFFFF), fontSize = 16.sp)
                }
            }

            Text("限时", color = Color(0xFFB4C5FF), fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 4.dp))
            Text("买一月送一月", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Black, modifier = Modifier.padding(bottom = 8.dp))
            Text("解锁键盘全部功能不限次", color = Color(0xCCFFFFFF), fontSize = 15.sp, modifier = Modifier.padding(bottom = 24.dp))

            // Purchase Card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF1D1D28))
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("特惠月卡买一送一", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Text("买一月送一月", color = Color(0xFF888888), fontSize = 13.sp)
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFFF4B6B))
                            .padding(horizontal = 12.dp, vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("新用户优惠", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text("￥96", color = Color(0xFF555555), fontSize = 16.sp, fontWeight = FontWeight.Bold) // Strikethrough ideally
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text("优惠后 ", color = Color(0xFFFF7A92), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text("￥", color = Color(0xFFFF7A92), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text("24", color = Color(0xFFFF7A92), fontSize = 26.sp, fontWeight = FontWeight.Bold)
                        Text("/月", color = Color(0xFFFF7A92), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(Color(0xFF6E7BFF))
                    .clickable { onPurchase() },
                contentAlignment = Alignment.Center
            ) {
                Text("领取优惠", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
