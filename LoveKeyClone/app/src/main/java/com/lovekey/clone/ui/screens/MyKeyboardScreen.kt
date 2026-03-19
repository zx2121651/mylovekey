package com.lovekey.clone.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lovekey.clone.data.MockData.PERSONAS_REPLY
import com.lovekey.clone.data.Persona

@Composable
fun MyKeyboardScreen() {
    var subTab by remember { mutableStateOf("reply") }

    val myPersonas = listOf(
        Persona(id = "1", icon = "🍬", title = "高情商"),
        Persona(id = "2", icon = "😘", title = "心动狙击"),
        Persona(id = "3", icon = "😆", title = "幽默"),
        Persona(id = "4", icon = "☀️", title = "暖男"),
        Persona(id = "5", icon = "💋", title = "暧昧拉扯"),
        Persona(id = "6", icon = "🎀", title = "情场高手"),
        Persona(id = "7", icon = "👨‍💼", title = "温柔大叔"),
        Persona(id = "8", icon = "🎁", title = "风流浪子"),
        Persona(id = "9", icon = "😋", title = "幽默有梗"),
        Persona(id = "10", icon = "🙄", title = "怼一下"),
        Persona(id = "11", icon = "🌱", title = "温柔体贴"),
        Persona(id = "12", icon = "🧥", title = "成熟稳重"),
        Persona(id = "13", icon = "👏", title = "高情商拒绝"),
        Persona(id = "14", icon = "🐒", title = "爆梗王"),
        Persona(id = "15", icon = "👻", title = "发疯文学"),
        Persona(id = "16", icon = "🧭", title = "双商在线"),
        Persona(id = "17", icon = "🍭", title = "话题延伸"),
        Persona(id = "add", icon = "+", title = "添加")
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F8FC))
    ) {
        // Gradient Top Background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFE2E6FF),
                            Color(0xFFF7F8FC)
                        )
                    )
                )
        )

        // Mock winking face background graphic
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 60.dp, end = 20.dp)
                .size(180.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.4f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "😉",
                fontSize = 120.sp,
                color = Color(0xFF5C73FF).copy(alpha = 0.2f),
                modifier = Modifier.offset(x = 10.dp, y = (-10).dp)
            )
        }

        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 60.dp, start = 24.dp, end = 24.dp)
            ) {
                Text(
                    text = "我的键盘",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF1A1A1A),
                    letterSpacing = 1.sp
                )
                // Fake underline
                Box(
                    modifier = Modifier
                        .padding(top = 2.dp)
                        .width(130.dp)
                        .height(3.dp)
                        .background(Color(0xFF5C73FF))
                )

                Text(
                    text = "长按拖拽调整键盘顺序",
                    fontSize = 14.sp,
                    color = Color(0xFF888888),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp, start = 20.dp, end = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TabItem(
                    text = "帮你回",
                    active = subTab == "reply",
                    onClick = { subTab = "reply" }
                )
                TabItem(
                    text = "超会说✨",
                    active = subTab == "talk",
                    onClick = { subTab = "talk" }
                )
                TabItem(
                    text = "键盘设置",
                    active = subTab == "settings",
                    onClick = { subTab = "settings" }
                )
            }

            // Grid Content
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(top = 24.dp, start = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 120.dp)
            ) {
                items(myPersonas) { persona ->
                    PersonaGridItem(persona)
                }
            }
        }
    }
}

@Composable
fun TabItem(text: String, active: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (active) Color(0xFFD6E0FF) else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = if (active) FontWeight.Black else FontWeight.Bold,
            color = if (active) Color(0xFF1A1A1A) else Color(0xFFA0A5B5)
        )
    }
}

@Composable
fun PersonaGridItem(persona: Persona) {
    val isAdd = persona.id == "add"
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .shadow(
                elevation = if (isAdd) 0.dp else 4.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = Color(0x1A000000)
            )
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .clickable { /* Handle click */ },
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = persona.icon,
                fontSize = if (isAdd) 20.sp else 16.sp,
                color = if (isAdd) Color(0xFF5C73FF) else Color.Unspecified
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = persona.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (isAdd) Color(0xFF5C73FF) else Color(0xFF333333)
            )
        }
    }
}
