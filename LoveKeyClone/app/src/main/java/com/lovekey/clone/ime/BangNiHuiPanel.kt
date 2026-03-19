package com.lovekey.clone.ime

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BangNiHuiPanel(
    state: KeyboardState,
    onClose: () -> Unit,
    onReplySelect: (String) -> Unit
) {
    val styles = listOf("🍬 高情商", "😘 心动狙击", "😆 幽默", "☀️ 暖男", "💋 暧昧拉扯", "🎀 情场高手", "👨\u200D⚖️ 温柔大叔", "🎁 风流浪子")
    var selectedStyle by remember { mutableStateOf(styles[0]) }

    val quickReplies = mapOf(
        "🍬 高情商" to listOf("在呼吸，在心跳，在想你呀~", "看到你的消息心跳就漏了一拍", "在想怎么回复才能让你心动💓", "刚刚发呆，你一找我就回神了"),
        "😘 心动狙击" to listOf("在想你呢，想让你现在就出现", "在等你的消息，终于等到了", "想抱抱你", "正在积攒见你的运气"),
        "😆 幽默" to listOf("在进行光合作用，维持生命体征", "在思考宇宙的终极奥秘...顺便想你", "在想你，并试图用意念回复", "地球不爆炸，我都不放假"),
        "☀️ 暖男" to listOf("刚忙完，正准备找你呢，你今天累不累？", "今天一切顺利吗？", "记得按时吃饭哦", "不管怎样，我在呢")
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp) // Takes the space of the normal keyboard
            .background(Color(0xFF2A2A35))
    ) {
        // Toolbar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF414259))
                    .clickable { onClose() },
                contentAlignment = Alignment.Center
            ) {
                Text("✕", color = Color(0xFFE0E0E0), fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.weight(1f))
            Text("常用语", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.weight(1f))
            Box(modifier = Modifier.size(28.dp)) // Spacer for balance
        }

        // Horizontal Tags
        LazyRow(
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(styles) { style ->
                val isSelected = style == selectedStyle
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) Color(0xFF4B66FF) else Color(0xFF404153))
                        .clickable { selectedStyle = style }
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = style,
                        color = if (isSelected) Color.White else Color(0xFFE0E0E0),
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }

        // Vertical Grid of Quick Replies
        val currentReplies = quickReplies[selectedStyle] ?: quickReplies.values.first()
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(currentReplies) { reply ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF404153))
                        .clickable { onReplySelect(reply) }
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = reply,
                        color = Color(0xFFE0E0E0),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
