package com.lovekey.clone.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun PaywallMainScreen(onNext: () -> Unit, onDownsell: () -> Unit) {
    var timeLeft by remember { mutableIntStateOf(11 * 3600 + 59 * 58) }
    var selectedPlan by remember { mutableStateOf("lifetime") }
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        while (timeLeft > 0) {
            delay(1000)
            timeLeft--
        }
    }

    val formatTime = { seconds: Int ->
        val h = (seconds / 3600).toString().padStart(2, '0')
        val m = ((seconds % 3600) / 60).toString().padStart(2, '0')
        val s = (seconds % 60).toString().padStart(2, '0')
        "$h:$m:$s"
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFFFFA77A), Color(0xFFFFF8F0), Color(0xFFFFF8F0))))
    ) {
        // Close Button
        Box(
            modifier = Modifier
                .padding(top = 40.dp, start = 20.dp)
                .size(32.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.4f))
                .clickable { onDownsell() }
                .shadow(2.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("×", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.offset(y = (-2).dp))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = 220.dp)
        ) {
            // Header Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 64.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "00",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .rotate(-12f)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.3f))
                            .border(1.dp, Color.White.copy(alpha = 0.4f), CircleShape)
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))

                    val pulse by rememberInfiniteTransition().animateFloat(
                        initialValue = 0.95f, targetValue = 1.05f,
                        animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Reverse), label = "pulse"
                    )
                    Text(
                        "限时",
                        color = Color.White,
                        fontSize = 44.sp,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.rotate(-6f).scale(pulse)
                    )
                }

                Box(
                    modifier = Modifier.padding(top = 10.dp)
                ) {
                    Text("🎠", fontSize = 60.sp, modifier = Modifier.align(Alignment.CenterStart).offset(x = (-24).dp).rotate(-10f).shadow(10.dp, CircleShape, spotColor = Color(0x33000000)))
                    Box(
                        modifier = Modifier
                            .rotate(3f)
                            .shadow(16.dp, RoundedCornerShape(32.dp), spotColor = Color(0x33000000))
                            .clip(RoundedCornerShape(32.dp))
                            .background(Color.White)
                            .border(4.dp, Color(0xFFE02020), RoundedCornerShape(32.dp))
                            .padding(horizontal = 24.dp, vertical = 8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text("立减", color = Color(0xFFE02020), fontSize = 40.sp, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic, fontWeight = FontWeight.Black)
                            Text("90", color = Color(0xFFE02020), fontSize = 58.sp, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic, fontWeight = FontWeight.Black, modifier = Modifier.offset(y = 4.dp))
                            Text("元", color = Color(0xFFE02020), fontSize = 24.sp, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic, fontWeight = FontWeight.Black, modifier = Modifier.padding(bottom = 4.dp))
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .rotate(-2f)
                        .clip(CircleShape)
                        .background(Brush.horizontalGradient(listOf(Color(0xFFFFD233), Color(0xFFFFB700))))
                        .border(1.dp, Color.White.copy(alpha = 0.5f), CircleShape)
                        .padding(horizontal = 20.dp, vertical = 6.dp)
                        .shadow(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("✨", fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("L+ 永久会员", color = Color(0xFF8B3A00), fontSize = 15.sp, fontWeight = FontWeight.Black)
                }
            }

            // Features List
            Column(
                modifier = Modifier.padding(top = 40.dp, start = 32.dp, end = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val features = listOf(
                    "键盘实时帮回复，不限次",
                    "幽默、高情商...海量人设，免费使用",
                    "会员专享，定制专属人设",
                    "亲密度调节，自动把控聊天分寸",
                    "会员优先体验新聊天能力"
                )
                features.forEach { text ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("✓", color = Color(0xFFA93B22), fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(24.dp))
                        Text(text, color = Color(0xFFA93B22), fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Pricing Cards
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp, start = 24.dp, end = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Lifetime Plan
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .shadow(if (selectedPlan == "lifetime") 20.dp else 0.dp, RoundedCornerShape(24.dp), spotColor = Color(0x33FF3B30))
                        .clip(RoundedCornerShape(24.dp))
                        .background(if (selectedPlan == "lifetime") Color.White else Color.White.copy(alpha = 0.6f))
                        .border(2.dp, if (selectedPlan == "lifetime") Color(0xFFFF3B30) else Color.Transparent, RoundedCornerShape(24.dp))
                        .clickable { selectedPlan = "lifetime" }
                ) {
                    Column(
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 32.dp)
                    ) {
                        Text("永久会员", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A1A), modifier = Modifier.padding(top = 8.dp))
                        Text("一次购买，终身免费", fontSize = 11.sp, color = Color(0xFF888888), fontWeight = FontWeight.Medium, modifier = Modifier.padding(top = 2.dp))

                        Row(modifier = Modifier.padding(top = 16.dp), verticalAlignment = Alignment.Bottom) {
                            Text("￥", color = Color(0xFFFF3B30), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Text("128", color = Color(0xFFFF3B30), fontSize = 36.sp, fontWeight = FontWeight.Bold, letterSpacing = (-1).sp)
                        }
                        Text("低至 ￥1/月", color = Color(0xFFD0A678), fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 4.dp))
                    }

                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .clip(RoundedCornerShape(topStart = 20.dp, bottomEnd = 12.dp))
                            .background(Color(0xFFFF3B30))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text("活动倒计时 ${formatTime(timeLeft)}", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }

                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .offset(y = 12.dp)
                            .shadow(4.dp, CircleShape, spotColor = Color(0x33FF3B30))
                            .clip(CircleShape)
                            .background(Color(0xFFFF3B30))
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        Text("立减 ￥90", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Monthly Plan
                Column(
                    modifier = Modifier
                        .weight(0.8f)
                        .height(160.dp)
                        .shadow(if (selectedPlan == "monthly") 20.dp else 0.dp, RoundedCornerShape(24.dp), spotColor = Color(0x33FF3B30))
                        .clip(RoundedCornerShape(24.dp))
                        .background(if (selectedPlan == "monthly") Color.White else Color.White.copy(alpha = 0.6f))
                        .border(2.dp, if (selectedPlan == "monthly") Color(0xFFFF3B30) else Color.White.copy(alpha = 0.5f), RoundedCornerShape(24.dp))
                        .clickable { selectedPlan = "monthly" }
                        .padding(16.dp)
                ) {
                    Text("月度会员", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A1A))
                    Spacer(modifier = Modifier.weight(1f))
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text("￥", color = Color(0xFF1A1A1A), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text("48", color = Color(0xFF1A1A1A), fontSize = 28.sp, fontWeight = FontWeight.Bold, letterSpacing = (-1).sp)
                    }
                }
            }
        }

        // Bottom Actions pinned
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Brush.verticalGradient(listOf(Color.Transparent, Color(0xFFFFF8F0), Color(0xFFFFF8F0))))
                .padding(start = 24.dp, end = 24.dp, top = 48.dp, bottom = 32.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .border(1.dp, Color(0xFFF2E8E0), RoundedCornerShape(16.dp))
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(22.dp).clip(RoundedCornerShape(4.dp)).background(Color(0xFF1677FF)), contentAlignment = Alignment.Center) {
                            Text("支", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("支付宝", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A1A))
                    }
                    Text(">", color = Color(0xFFA0A5B5), fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .shadow(20.dp, CircleShape, spotColor = Color(0x4DD62020))
                        .clip(CircleShape)
                        .background(Color(0xFFD62020))
                        .clickable { onNext() },
                    contentAlignment = Alignment.Center
                ) {
                    Text("立即解锁", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                }

                Text(
                    text = "点击支付即表示已阅读并同意《会员协议》",
                    color = Color(0xFFA0A5B5),
                    fontSize = 11.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 16.dp)
                )
            }
        }
    }
}
