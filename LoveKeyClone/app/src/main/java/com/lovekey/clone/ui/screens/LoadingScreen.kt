package com.lovekey.clone.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
fun LoadingScreen(onNext: () -> Unit) {
    var phase by remember { mutableStateOf("loading") }
    var showCheck by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(3500)
        phase = "success"
        showCheck = true
        delay(3000)
        onNext()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFFEAEFFF), Color(0xFFE2E8FF)))),
        contentAlignment = Alignment.Center
    ) {
        // Star Background
        StarFlicker(delayMillis = 0, size = 20, offsetX = 80, offsetY = (-150))
        StarFlicker(delayMillis = 800, size = 12, offsetX = (-60), offsetY = 0)
        StarFlicker(delayMillis = 1500, size = 16, offsetX = 100, offsetY = 120)

        Column(
            modifier = Modifier.fillMaxWidth().offset(y = (-64).dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                // Floating tags (Fly in to keyboard)
                FlyTag(icon = "😊", text = "温柔体贴", delayMillis = 200, startX = -60f, rot = -8f)
                FlyTag(icon = "😘", text = "暧昧拉扯", delayMillis = 600, startX = 50f, rot = 5f)
                FlyTag(icon = "👍", text = "幽默", delayMillis = 1000, startX = -40f, rot = -4f)
                FlyTag(icon = "✨", text = "高情商", delayMillis = 1400, startX = 60f, rot = 6f)
                FlyTag(icon = "🙄", text = "怼一下", delayMillis = 1800, startX = -50f, rot = -5f)
                FlyTag(icon = "💎", text = "双商在线", delayMillis = 2200, startX = 30f, rot = 3f)

                // 3D Keyboard graphic
                val breathScale by rememberInfiniteTransition().animateFloat(
                    initialValue = 1f, targetValue = 1.02f,
                    animationSpec = infiniteRepeatable(tween(1500, easing = LinearOutSlowInEasing), RepeatMode.Reverse)
                )
                val breathY by rememberInfiniteTransition().animateFloat(
                    initialValue = 0f, targetValue = -8f,
                    animationSpec = infiniteRepeatable(tween(1500, easing = LinearOutSlowInEasing), RepeatMode.Reverse)
                )

                Box(
                    modifier = Modifier
                        .padding(bottom = 40.dp)
                        .scale(breathScale)
                        .offset(y = breathY.dp)
                ) {
                    Keyboard3D()
                }

                if (showCheck) {
                    var popScale by remember { mutableFloatStateOf(0f) }
                    LaunchedEffect(Unit) {
                        androidx.compose.animation.core.animate(
                            initialValue = 0f,
                            targetValue = 1f,
                            animationSpec = tween(500, easing = CubicBezierEasing(0.34f, 1.56f, 0.64f, 1f))
                        ) { value, _ ->
                            popScale = value
                        }
                    }
                    Box(modifier = Modifier.padding(bottom = 110.dp).scale(popScale)) {
                        CheckCircle3D()
                        FireworkBurst()
                    }
                }
            }

            // Text section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(top = 40.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                if (phase == "loading") {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("正在为您组建键盘...", color = Color(0xFF3A4B86), fontSize = 18.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                        Text("正在根据您的选择组建专属键盘", color = Color(0xFF8C9AD6), fontSize = 13.sp, fontWeight = FontWeight.Medium, letterSpacing = 1.sp, modifier = Modifier.padding(top = 6.dp))
                    }
                } else {
                    Text("组建完成", color = Color(0xFF3A4B86), fontSize = 18.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                }
            }
        }
    }
}

@Composable
fun StarFlicker(delayMillis: Int, size: Int, offsetX: Int, offsetY: Int) {
    val infiniteTransition = rememberInfiniteTransition()
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f, targetValue = 1.1f,
        animationSpec = infiniteRepeatable(tween(1000, delayMillis = delayMillis), RepeatMode.Reverse)
    )
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.2f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1000, delayMillis = delayMillis), RepeatMode.Reverse)
    )

    Box(
        modifier = Modifier
            .offset(x = offsetX.dp, y = offsetY.dp)
            .size(size.dp)
            .scale(scale)
            .alpha(alpha)
    ) {
        com.lovekey.clone.ui.components.SparkleIcon()
    }
}

@Composable
fun FlyTag(icon: String, text: String, delayMillis: Int, startX: Float, rot: Float) {
    var isStarted by remember { mutableStateOf(false) }
    var animationState by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        delay(delayMillis.toLong())
        isStarted = true
        androidx.compose.animation.core.animate(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = tween(1200, easing = CubicBezierEasing(0.34f, 1.56f, 0.64f, 1f))
        ) { value, _ ->
            animationState = value
        }
    }

    if (isStarted && animationState < 1f) {
        val xOffset = startX * (1f - animationState)
        val yOffset = -240f + (240f * animationState) + (if(animationState > 0.8f) 30f * (animationState - 0.8f)/0.2f else 0f)
        val currentScale = when {
            animationState < 0.2f -> 0.5f + (0.55f * (animationState / 0.2f))
            animationState < 0.6f -> 1.05f - (0.1f * ((animationState - 0.2f) / 0.4f))
            animationState < 0.8f -> 0.95f - (0.15f * ((animationState - 0.6f) / 0.2f))
            else -> 0.8f - (0.8f * ((animationState - 0.8f) / 0.2f))
        }
        val currentRot = (rot * 2f) * (1f - animationState)
        val alpha = when {
            animationState < 0.2f -> animationState / 0.2f
            animationState < 0.8f -> 1f
            else -> 1f - ((animationState - 0.8f) / 0.2f)
        }

        Row(
            modifier = Modifier
                .padding(bottom = 40.dp)
                .offset(x = xOffset.dp, y = yOffset.dp)
                .rotate(currentRot)
                .scale(currentScale)
                .alpha(alpha)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.6f))
                .border(1.dp, Color.White.copy(alpha = 0.7f), CircleShape)
                .shadow(20.dp, spotColor = Color(0x1F6E82F0))
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(icon, fontSize = 16.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Text(text, color = Color(0xFF555555), fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun Keyboard3D() {
    Box(
        modifier = Modifier
            .size(140.dp, 100.dp)
            .shadow(32.dp, spotColor = Color(0x665C73FF))
    ) {
        // Base layers
        Box(modifier = Modifier.fillMaxSize().padding(top = 8.dp).clip(RoundedCornerShape(22.dp)).background(Color(0xFF3D50E6)))
        Box(modifier = Modifier.fillMaxSize().padding(top = 4.dp, bottom = 4.dp).clip(RoundedCornerShape(22.dp)).background(Color(0xFF5C73FF)))
        Box(modifier = Modifier.fillMaxSize().padding(bottom = 8.dp).clip(RoundedCornerShape(22.dp)).background(Color(0xFF7A8FFF)))
        // Keys
        Row(modifier = Modifier.padding(top = 16.dp, start = 12.dp, end = 12.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            repeat(5) { Box(modifier = Modifier.size(14.dp, 12.dp).clip(RoundedCornerShape(4.dp)).background(Color.White)) }
        }
        Row(modifier = Modifier.padding(top = 36.dp, start = 22.dp, end = 22.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            repeat(4) { Box(modifier = Modifier.size(14.dp, 12.dp).clip(RoundedCornerShape(4.dp)).background(Color.White)) }
        }
        Row(modifier = Modifier.padding(top = 56.dp, start = 32.dp, end = 32.dp).fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Box(modifier = Modifier.size(56.dp, 12.dp).clip(RoundedCornerShape(4.dp)).background(Color.White))
        }
    }
}


@Composable
fun FireworkBurst() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        ParticleBurst(delayOffset = 0)
        Box(modifier = Modifier.offset(x = (-100).dp, y = (-80).dp).scale(0.7f)) { ParticleBurst(delayOffset = 200) }
        Box(modifier = Modifier.offset(x = 110.dp, y = (-50).dp).scale(0.8f)) { ParticleBurst(delayOffset = 400) }
        Box(modifier = Modifier.offset(x = (-30).dp, y = 100.dp).scale(0.6f)) { ParticleBurst(delayOffset = 500) }
        Box(modifier = Modifier.offset(x = 80.dp, y = 90.dp).scale(0.7f)) { ParticleBurst(delayOffset = 700) }
    }
}

@Composable
fun ParticleBurst(delayOffset: Int) {
    val colors = listOf(Color(0xFFFF4B6B), Color(0xFFFFD233), Color(0xFF5C73FF), Color(0xFF4ECDC4), Color(0xFFA855F7), Color(0xFFFF923D), Color.White, Color(0xFF00E676))
    val dots = remember { List(40) {
        val angle = Math.random() * Math.PI * 2
        val distance = 80 + Math.random() * 160
        Triple(Math.cos(angle) * distance, Math.sin(angle) * distance, colors.random())
    } }

    var animProgress by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        delay(delayOffset.toLong())
        androidx.compose.animation.core.animate(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = tween(1000, easing = CubicBezierEasing(0.25f, 1f, 0.3f, 1f))
        ) { value, _ ->
            animProgress = value
        }
    }

    if (animProgress > 0f && animProgress < 1f) {
        dots.forEach { (tx, ty, color) ->
            val pX = tx * animProgress
            val pY = ty * animProgress + (40 * animProgress * animProgress)
            val pScale = if (animProgress < 0.4f) animProgress / 0.4f else 1f - ((animProgress - 0.4f) / 0.6f)
            Box(
                modifier = Modifier
                    .offset(x = pX.dp, y = pY.dp)
                    .size(6.dp)
                    .scale(pScale.toFloat())
                    .clip(CircleShape)
                    .background(color)
                    .shadow(8.dp, spotColor = color)
            )
        }
    }
}

@Composable
fun CheckCircle3D() {
    Box(
        modifier = Modifier
            .size(80.dp)
            .shadow(24.dp, spotColor = Color(0x80788AF4))
    ) {
        Box(modifier = Modifier.fillMaxSize().padding(top = 4.dp).clip(CircleShape).background(Color(0xFF3D50E6)))
        Box(modifier = Modifier.fillMaxSize().padding(bottom = 4.dp).clip(CircleShape).background(Color(0xFF6E85FF)))
        // Checkmark
        Text("✓", color = Color.White, fontSize = 48.sp, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Center))
    }
}
