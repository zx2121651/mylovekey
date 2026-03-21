package com.lovekey.clone.ui.screens

import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun KeyboardSelectScreen(onNext: () -> Unit) {
    var selectedType by remember { mutableStateOf<String?>(null) }
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFFEAEFFF), Color(0xFFF4F6FC))))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 70.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "选择你喜欢的中文键盘",
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF1A1A1A),
                letterSpacing = 1.sp,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                KeyboardTypeCard(
                    type = "nine",
                    title = "九宫格拼音",
                    isSelected = selectedType == "nine",
                    onClick = { selectedType = "nine" }
                ) {
                    NineGridPreview()
                }

                KeyboardTypeCard(
                    type = "full",
                    title = "全键盘拼音",
                    isSelected = selectedType == "full",
                    onClick = { selectedType = "full" }
                ) {
                    FullKeyboardPreview()
                }

                KeyboardTypeCard(
                    type = "hand",
                    title = "手写键盘",
                    isNew = true,
                    isSelected = selectedType == "hand",
                    onClick = { selectedType = "hand" }
                ) {
                    HandwritingPreview()
                }

                Spacer(modifier = Modifier.height(120.dp))
            }
        }

        // Bottom Gradient and Button
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Brush.verticalGradient(listOf(Color.Transparent, Color(0xFFF4F6FC), Color(0xFFF4F6FC))))
                .padding(start = 20.dp, end = 20.dp, top = 40.dp, bottom = 32.dp)
        ) {
            val isEnabled = selectedType != null
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .shadow(if (isEnabled) 20.dp else 0.dp, RoundedCornerShape(24.dp), spotColor = Color(0x33000000))
                    .clip(RoundedCornerShape(24.dp))
                    .background(if (isEnabled) Color(0xFF121212) else Color(0xFF4B4B4B).copy(alpha = 0.9f))
                    .clickable(enabled = isEnabled) { onNext() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "开始体验Lovekey键盘",
                    color = if (isEnabled) Color.White else Color.White.copy(alpha = 0.8f),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

@Composable
fun KeyboardTypeCard(
    type: String,
    title: String,
    isNew: Boolean = false,
    isSelected: Boolean,
    onClick: (String) -> Unit,
    content: @Composable () -> Unit
) {
    val scale by animateFloatAsState(targetValue = if (isSelected) 0.98f else 1f, label = "cardScale")

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .shadow(if (isSelected) 20.dp else 16.dp, RoundedCornerShape(20.dp), spotColor = if (isSelected) Color(0x265C73FF) else Color(0x0A000000))
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .border(
                width = 2.dp,
                color = if (isSelected) Color(0xFF5C73FF) else Color.Transparent,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable { onClick(type) }
            .padding(18.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = title, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = if (isSelected) Color(0xFF5C73FF) else Color(0xFF1A1A1A))
                    if (isNew) {
                        Box(
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFFEEF2FF))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text("New", color = Color(0xFF5C73FF), fontSize = 10.sp, fontWeight = FontWeight.Black)
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) Color(0xFF5C73FF) else Color.Transparent)
                        .border(if (isSelected) 0.dp else 2.dp, Color(0xFFE5E7EB), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    if (isSelected) Text("✓", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
            content()
        }
    }
}

@Composable
fun KeyboardPreviewWrapper(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF2F4F9))
            .padding(8.dp)
    ) {
        // Blur shapes
        Box(modifier = Modifier.offset(x = (-40).dp, y = (-40).dp).size(160.dp).clip(CircleShape).background(Color(0xFFE1C8FF).copy(alpha = 0.6f)))
        Box(modifier = Modifier.align(Alignment.TopEnd).offset(x = 40.dp, y = 40.dp).size(192.dp).clip(CircleShape).background(Color(0xFFC8DBFF).copy(alpha = 0.6f)))
        Box(modifier = Modifier.align(Alignment.BottomStart).offset(x = 40.dp, y = 40.dp).size(128.dp).clip(CircleShape).background(Color(0xFFFFE1E1).copy(alpha = 0.5f)))

        Box(modifier = Modifier.fillMaxSize()) {
            content()
        }
    }
}

// Simple mock layouts for previews
@Composable
fun NineGridPreview() {
    KeyboardPreviewWrapper {
        Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Column(modifier = Modifier.weight(1.3f).fillMaxHeight(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf("，", "。", "?", "!", "符号").forEach {
                    Box(modifier = Modifier.weight(1f).fillMaxWidth().clip(RoundedCornerShape(6.dp)).background(Color(0xFFD3D8E6)), contentAlignment = Alignment.Center) { Text(it, fontSize = 12.sp, color = Color(0xFF333333)) }
                }
            }
            Column(modifier = Modifier.weight(3f).fillMaxHeight(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(modifier = Modifier.weight(1f).fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("@#", "ABC", "DEF").forEach { Box(modifier = Modifier.weight(1f).fillMaxHeight().clip(RoundedCornerShape(8.dp)).background(Color.White), contentAlignment = Alignment.Center) { Text(it, fontSize = 12.sp, color = Color(0xFF1A1A1A)) } }
                }
                Row(modifier = Modifier.weight(1f).fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("GHI", "JKL", "MNO").forEach { Box(modifier = Modifier.weight(1f).fillMaxHeight().clip(RoundedCornerShape(8.dp)).background(Color.White), contentAlignment = Alignment.Center) { Text(it, fontSize = 12.sp, color = Color(0xFF1A1A1A)) } }
                }
                Row(modifier = Modifier.weight(1f).fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("PQRS", "TUV", "WXYZ").forEach { Box(modifier = Modifier.weight(1f).fillMaxHeight().clip(RoundedCornerShape(8.dp)).background(Color.White), contentAlignment = Alignment.Center) { Text(it, fontSize = 12.sp, color = Color(0xFF1A1A1A)) } }
                }
                Row(modifier = Modifier.weight(1f).fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("123", "MIC", "中英").forEach { Box(modifier = Modifier.weight(1f).fillMaxHeight().clip(RoundedCornerShape(8.dp)).background(Color.White), contentAlignment = Alignment.Center) { Text(it, fontSize = 12.sp, color = Color(0xFF1A1A1A)) } }
                }
            }
            Column(modifier = Modifier.weight(1.5f).fillMaxHeight(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(modifier = Modifier.weight(1f).fillMaxWidth().clip(RoundedCornerShape(6.dp)).background(Color(0xFFD3D8E6)), contentAlignment = Alignment.Center) { Text("退格", fontSize = 12.sp, color = Color(0xFF333333)) }
                Box(modifier = Modifier.weight(1.5f).fillMaxWidth().clip(RoundedCornerShape(6.dp)).background(Color(0xFFD3D8E6)), contentAlignment = Alignment.Center) { Text("换行", fontSize = 12.sp, color = Color(0xFF333333)) }
                Box(modifier = Modifier.weight(1.5f).fillMaxWidth().clip(RoundedCornerShape(6.dp)).background(Color(0xFFAEC0FF)), contentAlignment = Alignment.Center) { Text("发送", fontSize = 12.sp, color = Color.White) }
            }
        }
    }
}

@Composable
fun FullKeyboardPreview() {
    KeyboardPreviewWrapper {
        Column(modifier = Modifier.fillMaxSize().padding(top = 4.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                listOf("Q","W","E","R","T","Y","U","I","O","P").forEach {
                    Box(modifier = Modifier.weight(1f).height(34.dp).clip(RoundedCornerShape(6.dp)).background(Color.White), contentAlignment = Alignment.Center) { Text(it, fontSize = 12.sp, color = Color(0xFF1A1A1A)) }
                }
            }
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                listOf("A","S","D","F","G","H","J","K","L").forEach {
                    Box(modifier = Modifier.weight(1f).height(34.dp).clip(RoundedCornerShape(6.dp)).background(Color.White), contentAlignment = Alignment.Center) { Text(it, fontSize = 12.sp, color = Color(0xFF1A1A1A)) }
                }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Box(modifier = Modifier.weight(1.5f).height(34.dp).clip(RoundedCornerShape(6.dp)).background(Color(0xFFD3D8E6)), contentAlignment = Alignment.Center) { Text("↑", fontSize = 12.sp, color = Color(0xFF333333)) }
                listOf("Z","X","C","V","B","N","M").forEach {
                    Box(modifier = Modifier.weight(1f).height(34.dp).clip(RoundedCornerShape(6.dp)).background(Color.White), contentAlignment = Alignment.Center) { Text(it, fontSize = 12.sp, color = Color(0xFF1A1A1A)) }
                }
                Box(modifier = Modifier.weight(1.5f).height(34.dp).clip(RoundedCornerShape(6.dp)).background(Color(0xFFD3D8E6)), contentAlignment = Alignment.Center) { Text("退", fontSize = 12.sp, color = Color(0xFF333333)) }
            }
            Row(modifier = Modifier.fillMaxWidth().padding(top = 4.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Box(modifier = Modifier.weight(2f).height(34.dp).clip(RoundedCornerShape(6.dp)).background(Color(0xFFD3D8E6)), contentAlignment = Alignment.Center) { Text("123", fontSize = 12.sp, color = Color(0xFF333333)) }
                Box(modifier = Modifier.weight(1f).height(34.dp).clip(RoundedCornerShape(6.dp)).background(Color(0xFFD3D8E6)), contentAlignment = Alignment.Center) { Text(",", fontSize = 12.sp, color = Color(0xFF333333)) }
                Box(modifier = Modifier.weight(5f).height(34.dp).clip(RoundedCornerShape(6.dp)).background(Color.White), contentAlignment = Alignment.Center) { Text("MIC", fontSize = 12.sp, color = Color(0xFFA0A5B5)) }
                Box(modifier = Modifier.weight(1f).height(34.dp).clip(RoundedCornerShape(6.dp)).background(Color(0xFFD3D8E6)), contentAlignment = Alignment.Center) { Text("中/英", fontSize = 10.sp, color = Color(0xFF333333)) }
                Box(modifier = Modifier.weight(2f).height(34.dp).clip(RoundedCornerShape(6.dp)).background(Color(0xFFAEC0FF)), contentAlignment = Alignment.Center) { Text("发送", fontSize = 12.sp, color = Color.White) }
            }
        }
    }
}

@Composable
fun HandwritingPreview() {
    KeyboardPreviewWrapper {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(modifier = Modifier.weight(1f).fillMaxWidth()) {
                Box(modifier = Modifier.weight(1f).fillMaxHeight(), contentAlignment = Alignment.Center) {
                    Text("你好", fontSize = 60.sp, fontWeight = FontWeight.Light, color = Color(0xFF2C334A), modifier = Modifier.rotate(-8f), letterSpacing = 10.sp)
                }
                Column(modifier = Modifier.width(30.dp).fillMaxHeight().padding(end = 4.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Box(modifier = Modifier.height(24.dp).fillMaxWidth().clip(RoundedCornerShape(6.dp)).background(Color(0xFFD3D8E6)), contentAlignment = Alignment.Center) { Text("退", fontSize = 12.sp, color = Color(0xFF333333)) }
                    Column(modifier = Modifier.weight(1f).fillMaxWidth(), verticalArrangement = Arrangement.SpaceEvenly, horizontalAlignment = Alignment.CenterHorizontally) {
                        listOf("，", "。", "?", "!").forEach { Text(it, color = Color(0xFF555555), fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                    }
                }
            }
            Row(modifier = Modifier.fillMaxWidth().height(34.dp).padding(top = 4.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Box(modifier = Modifier.weight(1f).fillMaxHeight().clip(RoundedCornerShape(6.dp)).background(Color(0xFFD3D8E6)), contentAlignment = Alignment.Center) { Text("符号", fontSize = 11.sp, color = Color(0xFF333333)) }
                Box(modifier = Modifier.weight(1f).fillMaxHeight().clip(RoundedCornerShape(6.dp)).background(Color(0xFFD3D8E6)), contentAlignment = Alignment.Center) { Text("123", fontSize = 11.sp, color = Color(0xFF333333)) }
                Box(modifier = Modifier.weight(1f).fillMaxHeight().clip(RoundedCornerShape(6.dp)).background(Color(0xFFD3D8E6)), contentAlignment = Alignment.Center) { Text("ABC", fontSize = 11.sp, color = Color(0xFF333333)) }
                Box(modifier = Modifier.weight(2f).fillMaxHeight().clip(RoundedCornerShape(6.dp)).background(Color.White))
                Box(modifier = Modifier.weight(1f).fillMaxHeight().clip(RoundedCornerShape(6.dp)).background(Color(0xFFD3D8E6)), contentAlignment = Alignment.Center) { Text("写", fontSize = 11.sp, color = Color(0xFF333333)) }
                Box(modifier = Modifier.weight(1.5f).fillMaxHeight().clip(RoundedCornerShape(6.dp)).background(Color(0xFF5C73FF)), contentAlignment = Alignment.Center) { Text("发送", fontSize = 12.sp, color = Color.White) }
            }
        }
    }
}
