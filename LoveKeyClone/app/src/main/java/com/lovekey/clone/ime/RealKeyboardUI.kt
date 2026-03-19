package com.lovekey.clone.ime

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun RealKeyboardUI(
    onKeyPress: (String) -> Unit,
    onDelete: () -> Unit,
    onEnter: () -> Unit,
    onAiAction: (String) -> Unit
) {
    var isPinyinMode by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(290.dp) // Fixed height typical for Android keyboards
            .background(Color(0xFFE2E6EF)) // System keyboard grayish background
    ) {
        // --- 1. AI Toolbar Top ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp)
                .height(40.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(36.dp).clip(CircleShape).background(Color(0xFFFFFFFF)), contentAlignment = Alignment.Center) { Text("⌨", color = Color(0xFF5C73FF), fontSize = 16.sp) }
            Box(modifier = Modifier.height(32.dp).clip(CircleShape).background(Color(0xFF4B66FF)).padding(horizontal = 14.dp).clickable { /* toggle something */ }, contentAlignment = Alignment.Center) { Text("帮你回", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold) }
            Box(modifier = Modifier.height(32.dp).clip(CircleShape).background(Color.White).padding(horizontal = 14.dp), contentAlignment = Alignment.Center) { Text("超会说", color = Color(0xFF555555), fontSize = 13.sp, fontWeight = FontWeight.Bold) }
            Spacer(modifier = Modifier.weight(1f))
            Box(modifier = Modifier.size(32.dp).clip(CircleShape).border(1.dp, Color(0xFFD3D8E6), CircleShape), contentAlignment = Alignment.Center) { Text("Hi", color = Color(0xFF888888), fontSize = 12.sp, fontWeight = FontWeight.Bold) }
            Box(modifier = Modifier.size(32.dp).clip(CircleShape).border(1.dp, Color(0xFFD3D8E6), CircleShape), contentAlignment = Alignment.Center) { Text("⊞", color = Color(0xFF888888), fontSize = 16.sp) }
        }

        // --- 2. AI Quick Replies Grid (Optional/Toggleable) ---
        // Here we just hardcode the AI tag buttons for demonstration of '帮你回'
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val tags = listOf(
                "🍬 高情商" to "高情商", "😘 心动狙击" to "心动狙击", "😆 幽默" to "幽默",
                "🌞 暖男" to "暖男", "💋 暧昧拉扯" to "暧昧拉扯", "🎀 情场高手" to "情场高手"
            )
            items(tags.size) { idx ->
                Box(
                    modifier = Modifier
                        .height(38.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White)
                        .clickable(interactionSource = remember { MutableInteractionSource() }, indication = rememberRipple()) { onAiAction(tags[idx].second) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(tags[idx].first, color = Color(0xFF555555), fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // --- 3. Actual QWERTY English Layout for fallback typing ---
        val rows = listOf(
            listOf("Q","W","E","R","T","Y","U","I","O","P"),
            listOf("A","S","D","F","G","H","J","K","L"),
            listOf("⇧", "Z","X","C","V","B","N","M", "⌫")
        )

        Column(
            modifier = Modifier.fillMaxSize().padding(start = 4.dp, end = 4.dp, bottom = 4.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            rows.forEachIndexed { rowIndex, rowKeys ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = if (rowIndex == 1) 16.dp else 0.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    rowKeys.forEach { key ->
                        val isSpecial = key == "⇧" || key == "⌫"
                        Box(
                            modifier = Modifier
                                .weight(if (isSpecial) 1.5f else 1f)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSpecial) Color(0xFFB4BACC) else Color.White)
                                .clickable(interactionSource = remember { MutableInteractionSource() }, indication = rememberRipple()) {
                                    if (key == "⌫") onDelete()
                                    else if (!isSpecial) onKeyPress(key.lowercase())
                                }
                                .shadow(if (isSpecial) 0.dp else 2.dp, spotColor = Color(0x1A000000)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(key, fontSize = 18.sp, color = Color(0xFF1A1A1A))
                        }
                    }
                }
            }

            // Bottom Action Row
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(modifier = Modifier.weight(1.5f).fillMaxHeight().clip(RoundedCornerShape(6.dp)).background(Color(0xFFB4BACC)), contentAlignment = Alignment.Center) { Text("?123", fontSize = 14.sp) }
                Box(modifier = Modifier.weight(1f).fillMaxHeight().clip(RoundedCornerShape(6.dp)).background(Color(0xFFB4BACC)).clickable { onKeyPress(",") }, contentAlignment = Alignment.Center) { Text(",", fontSize = 16.sp) }
                Box(modifier = Modifier.weight(4f).fillMaxHeight().clip(RoundedCornerShape(6.dp)).background(Color.White).clickable { onKeyPress(" ") }.shadow(2.dp), contentAlignment = Alignment.Center) { Text("空格", fontSize = 14.sp) }
                Box(modifier = Modifier.weight(1f).fillMaxHeight().clip(RoundedCornerShape(6.dp)).background(Color(0xFFB4BACC)).clickable { onKeyPress(".") }, contentAlignment = Alignment.Center) { Text(".", fontSize = 16.sp) }
                Box(modifier = Modifier.weight(1.5f).fillMaxHeight().clip(RoundedCornerShape(6.dp)).background(Color(0xFF5C73FF)).clickable { onEnter() }, contentAlignment = Alignment.Center) { Text("发送", fontSize = 14.sp, color = Color.White, fontWeight = FontWeight.Bold) }
            }
        }
    }
}
