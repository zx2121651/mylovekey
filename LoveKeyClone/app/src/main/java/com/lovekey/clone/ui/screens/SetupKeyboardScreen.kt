package com.lovekey.clone.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
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
fun SetupKeyboardScreen(onNext: () -> Unit) {
    var setupStep by remember { mutableStateOf(1) }
    var showBottomSheet by remember { mutableStateOf(false) }

    val handleStep1Click = {
        if (setupStep == 1) {
            setupStep = 2
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF5C73FF), Color(0xFF455DF8))))
    ) {
        // Background large texts
        Column(
            modifier = Modifier.fillMaxSize().padding(top = 8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text("KNOW", color = Color.White.copy(alpha = 0.06f), fontSize = 140.sp, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic, fontWeight = FontWeight.Black, modifier = Modifier.offset(x = (-24).dp).rotate(-12f))
            Text("KNOW", color = Color.White.copy(alpha = 0.06f), fontSize = 140.sp, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic, fontWeight = FontWeight.Black, modifier = Modifier.offset(x = (-24).dp).rotate(-12f).padding(bottom = 32.dp))
        }

        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 80.dp, start = 32.dp)
        ) {
            Column {
                Text("Lovekey", color = Color.White, fontSize = 42.sp, fontWeight = FontWeight.Black, letterSpacing = (-1).sp)
                Text("一个可以帮你回复消息的键盘", color = Color.White.copy(alpha = 0.8f), fontSize = 13.sp, fontWeight = FontWeight.Medium)
            }
            // 3D Keycap Canvas
            Box(modifier = Modifier.align(Alignment.CenterEnd).offset(x = 30.dp, y = (-10).dp)) {
                androidx.compose.foundation.Canvas(modifier = Modifier.size(150.dp).shadow(30.dp, spotColor = Color(0x4D1E32C8), shape = RoundedCornerShape(32.dp))) {
                    val w = size.width
                    val h = size.height

                    // Outer Dark Blue
                    drawRoundRect(
                        color = Color(0xFF3D50E6),
                        topLeft = androidx.compose.ui.geometry.Offset(w * 0.13f, h * 0.2f),
                        size = androidx.compose.ui.geometry.Size(w * 0.73f, h * 0.66f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(24.dp.toPx(), 24.dp.toPx())
                    )
                    // Inner Lighter Blue
                    drawRoundRect(
                        color = Color(0xFF6B8BFF),
                        topLeft = androidx.compose.ui.geometry.Offset(w * 0.13f, h * 0.16f),
                        size = androidx.compose.ui.geometry.Size(w * 0.73f, h * 0.64f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(24.dp.toPx(), 24.dp.toPx())
                    )
                    // Inner Off-White Base
                    drawRoundRect(
                        color = Color(0xFFD9E2FF),
                        topLeft = androidx.compose.ui.geometry.Offset(w * 0.21f, h * 0.24f),
                        size = androidx.compose.ui.geometry.Size(w * 0.57f, h * 0.5f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(16.dp.toPx(), 16.dp.toPx())
                    )
                    // Inner White Top
                    drawRoundRect(
                        color = Color(0xFFF8FAFF),
                        topLeft = androidx.compose.ui.geometry.Offset(w * 0.21f, h * 0.18f),
                        size = androidx.compose.ui.geometry.Size(w * 0.57f, h * 0.5f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(16.dp.toPx(), 16.dp.toPx())
                    )

                    // The smile
                    val path = androidx.compose.ui.graphics.Path().apply {
                        moveTo(w * 0.25f, h * 0.21f)
                        cubicTo(w * 0.4f, h * 0.18f, w * 0.6f, h * 0.18f, w * 0.74f, h * 0.21f)
                        cubicTo(w * 0.75f, h * 0.3f, w * 0.75f, h * 0.36f, w * 0.74f, h * 0.43f)
                        cubicTo(w * 0.6f, h * 0.36f, w * 0.4f, h * 0.36f, w * 0.25f, h * 0.43f)
                        cubicTo(w * 0.24f, h * 0.36f, w * 0.24f, h * 0.3f, w * 0.25f, h * 0.21f)
                        close()
                    }
                    drawPath(path, Color.White)
                    drawCircle(color = Color(0xFF2C334A), radius = 3.5.dp.toPx(), center = androidx.compose.ui.geometry.Offset(w * 0.41f, h * 0.41f))
                    drawCircle(color = Color(0xFF2C334A), radius = 3.5.dp.toPx(), center = androidx.compose.ui.geometry.Offset(w * 0.58f, h * 0.41f))
                    val smilePath = androidx.compose.ui.graphics.Path().apply {
                        moveTo(w * 0.44f, h * 0.5f)
                        quadraticBezierTo(w * 0.5f, h * 0.55f, w * 0.56f, h * 0.5f)
                    }
                    drawPath(smilePath, color = Color(0xFF2C334A), style = androidx.compose.ui.graphics.drawscope.Stroke(width = 3.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round))
                }
            }
        }

        // Main Content Area
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 180.dp, start = 24.dp, end = 24.dp)
        ) {
            if (setupStep == 1) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(16.dp, RoundedCornerShape(20.dp), spotColor = Color(0x33000000))
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(modifier = Modifier.fillMaxWidth().height(52.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFFF7F8FA)).padding(horizontal = 12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Box(modifier = Modifier.size(32.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFFEAECEF)))
                                Box(modifier = Modifier.size(96.dp, 16.dp).clip(CircleShape).background(Color(0xFFEAECEF)))
                            }
                            Text("未启用", color = Color(0xFFA0A5B5), fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.clip(CircleShape).background(Color(0xFFE6E8EC)).padding(horizontal = 12.dp, vertical = 4.dp))
                        }

                        Row(modifier = Modifier.fillMaxWidth().height(60.dp).shadow(2.dp, RoundedCornerShape(12.dp), spotColor = Color(0x1A000000)).clip(RoundedCornerShape(12.dp)).background(Color.White).border(1.5.dp, Color(0xFFEBF0F5), RoundedCornerShape(12.dp)).padding(horizontal = 12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Box(modifier = Modifier.size(36.dp).clip(RoundedCornerShape(10.dp)).background(Color(0xFF4B66FF)))
                                Text("Lovekey", color = Color(0xFF1A1A1A), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            }
                            Text("未启用", color = Color(0xFFA0A5B5), fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.clip(CircleShape).background(Color(0xFFF2F3F7)).padding(horizontal = 12.dp, vertical = 4.dp))
                        }

                        Row(modifier = Modifier.fillMaxWidth().height(52.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFFF7F8FA)).padding(horizontal = 12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Box(modifier = Modifier.size(32.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFFEAECEF)))
                                Box(modifier = Modifier.size(64.dp, 16.dp).clip(CircleShape).background(Color(0xFFEAECEF)))
                            }
                            Text("未启用", color = Color(0xFFA0A5B5), fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.clip(CircleShape).background(Color(0xFFE6E8EC)).padding(horizontal = 12.dp, vertical = 4.dp))
                        }
                    }

                    Box(modifier = Modifier.fillMaxSize()) {
                        val infiniteTransition = androidx.compose.animation.core.rememberInfiniteTransition()
                        val hintOffset by infiniteTransition.animateFloat(
                            initialValue = 0f, targetValue = -10f,
                            animationSpec = androidx.compose.animation.core.infiniteRepeatable(androidx.compose.animation.core.tween(750, easing = androidx.compose.animation.core.EaseInOut), androidx.compose.animation.core.RepeatMode.Reverse)
                        )
                        val hintScale by infiniteTransition.animateFloat(
                            initialValue = 1f, targetValue = 0.9f,
                            animationSpec = androidx.compose.animation.core.infiniteRepeatable(androidx.compose.animation.core.tween(750, easing = androidx.compose.animation.core.EaseInOut), androidx.compose.animation.core.RepeatMode.Reverse)
                        )

                        Text("开启Lovekey", color = Color.White, fontSize = 30.sp, fontWeight = FontWeight.Black, modifier = Modifier.align(Alignment.Center).offset(y = 20.dp).rotate(-2f))
                        Text("👆", fontSize = 34.sp, modifier = Modifier.align(Alignment.CenterEnd).offset(x = hintOffset.dp, y = hintOffset.dp).scale(hintScale))
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(16.dp, RoundedCornerShape(20.dp), spotColor = Color(0x33000000))
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFFE3E4E8))
                        .border(1.dp, Color.White.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                        .padding(16.dp)
                ) {
                    Column {
                        Row(modifier = Modifier.fillMaxWidth().padding(start = 4.dp, end = 4.dp, bottom = 12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("其他输入法", color = Color(0xFF888888), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text("^", color = Color(0xFF888888))
                        }
                        Row(modifier = Modifier.fillMaxWidth().height(50.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFFC6D2E8)).border(1.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(12.dp)).padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Lovekey键盘", color = Color(0xFF0066FF), fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            Text("✓", color = Color(0xFF0066FF), fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        }
                        Row(modifier = Modifier.fillMaxWidth().padding(top = 20.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Box(modifier = Modifier.weight(1f).height(38.dp).clip(RoundedCornerShape(10.dp)).background(Color(0xFFCFD2DC)), contentAlignment = Alignment.Center) { Text("取消", color = Color(0xFF888888), fontSize = 13.sp, fontWeight = FontWeight.Bold) }
                            Box(modifier = Modifier.weight(1f).height(38.dp).clip(RoundedCornerShape(10.dp)).background(Color(0xFF486BFF)), contentAlignment = Alignment.Center) { Text("确定", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold) }
                        }
                    }
                    Box(modifier = Modifier.fillMaxSize()) {
                        val infiniteTransition = androidx.compose.animation.core.rememberInfiniteTransition()
                        val hintOffset by infiniteTransition.animateFloat(
                            initialValue = 0f, targetValue = -10f,
                            animationSpec = androidx.compose.animation.core.infiniteRepeatable(androidx.compose.animation.core.tween(750, easing = androidx.compose.animation.core.EaseInOut), androidx.compose.animation.core.RepeatMode.Reverse)
                        )
                        val hintScale by infiniteTransition.animateFloat(
                            initialValue = 1f, targetValue = 0.9f,
                            animationSpec = androidx.compose.animation.core.infiniteRepeatable(androidx.compose.animation.core.tween(750, easing = androidx.compose.animation.core.EaseInOut), androidx.compose.animation.core.RepeatMode.Reverse)
                        )
                        Text("选择Lovekey键盘", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Black, modifier = Modifier.align(Alignment.Center).offset(y = 10.dp).rotate(-2f))
                        Text("👆", fontSize = 34.sp, modifier = Modifier.align(Alignment.CenterEnd).offset(x = hintOffset.dp, y = hintOffset.dp).scale(hintScale))
                    }
                }
            }

            Text(
                text = if (setupStep == 1) "第1步：在「输入法」管理中，启用Lovekey" else "第2步：切换到Lovekey键盘",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 16.dp)
            )
        }

        // Bottom Actions
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, bottom = 48.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(if (setupStep == 1) 20.dp else 0.dp, RoundedCornerShape(14.dp), spotColor = Color(0x33000000))
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (setupStep == 1) Color(0xFF121212) else Color.White.copy(alpha = 0.1f))
                    .clickable(enabled = setupStep == 1) { handleStep1Click() }
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("第一步 启用Lovekey键盘", color = if (setupStep == 1) Color.White else Color.White.copy(alpha = 0.4f), fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    Text(if (setupStep == 1) "→" else "✓", color = if (setupStep == 1) Color.White else Color.White.copy(alpha = 0.4f), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(if (setupStep == 2) 20.dp else 0.dp, RoundedCornerShape(14.dp), spotColor = Color(0x33000000))
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (setupStep == 2) Color(0xFF121212) else Color.White.copy(alpha = 0.1f))
                    .clickable(enabled = setupStep == 2) { showBottomSheet = true }
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("第二步 切换到Lovekey键盘", color = if (setupStep == 2) Color.White else Color.White.copy(alpha = 0.4f), fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    Text("→", color = if (setupStep == 2) Color.White else Color.White.copy(alpha = 0.4f), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Bottom Sheet
        AnimatedVisibility(
            visible = showBottomSheet,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable { showBottomSheet = false }
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                        .background(Color(0xFF1C1C1E))
                        .padding(horizontal = 24.dp, vertical = 24.dp)
                        .clickable(enabled = false) {} // Prevent click-through
                ) {
                    Text("更改键盘", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 32.dp))

                    Row(modifier = Modifier.fillMaxWidth().padding(start = 8.dp, end = 8.dp, bottom = 28.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("搜狗输入法定制版", color = Color.White.copy(alpha = 0.9f), fontSize = 15.sp)
                        Box(modifier = Modifier.size(20.dp).clip(CircleShape).background(Color(0xFF0A84FF)).border(1.5.dp, Color(0xFF1C1C1E), CircleShape), contentAlignment = Alignment.Center) {
                            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color.White))
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                            .clickable { showBottomSheet = false; onNext() },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("👉 ", fontSize = 16.sp)
                            Text("Lovekey键盘", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        }
                        Box(modifier = Modifier.size(20.dp).clip(CircleShape).border(1.5.dp, Color(0xFF48484A), CircleShape))
                    }
                    Spacer(modifier = Modifier.height(48.dp))
                }
            }
        }
    }
}
