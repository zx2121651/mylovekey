package com.lovekey.clone.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lovekey.clone.data.MockData.GROUPS
import com.lovekey.clone.data.Persona
import com.lovekey.clone.ui.components.Tag
import kotlinx.coroutines.isActive

@Composable
fun PersonaScreen(onPrev: () -> Unit, onNext: () -> Unit, selectedIds: List<String>, onToggle: (String) -> Unit) {
    val isValidSelection = selectedIds.isNotEmpty()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F8FD))
            .padding(top = 48.dp, bottom = 40.dp)
    ) {
        // Top Nav
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White)
                    .clickable { onPrev() }
                    .shadow(8.dp, spotColor = Color(0x0A000000)),
                contentAlignment = Alignment.Center
            ) {
                Text("<", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A1A))
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color(0xFFDCDFE6)))
                Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color(0xFFDCDFE6)))
                Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color(0xFF1A1A1A)))
            }
            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(42.dp))
        }

        Spacer(modifier = Modifier.height(48.dp))

        // Title
        Text(
            text = "添加喜欢的「人设」到键盘",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1A1A),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp),
            textAlign = TextAlign.Center,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Infinite Marquee Groups
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            GROUPS.forEachIndexed { index, group ->
                val duration = 30000 + index * 8000
                MarqueeRow(items = group, durationMillis = duration, selectedIds = selectedIds, onToggle = onToggle)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Submit Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 40.dp)
                .shadow(if (isValidSelection) 20.dp else 0.dp, CircleShape, spotColor = Color(0x4D667EFE))
                .clip(CircleShape)
                .background(if (isValidSelection) Color(0xFF667EFE) else Color(0xFFA8ADB8).copy(alpha = 0.9f))
                .clickable(enabled = isValidSelection) { onNext() }
                .height(56.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (isValidSelection) "生成专属聊天键盘" else "请至少选择 1 个人设",
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }
    }
}

@Composable
fun MarqueeRow(items: List<Persona>, durationMillis: Int, selectedIds: List<String>, onToggle: (String) -> Unit) {
    val scrollState = rememberScrollState()
    val infiniteTransition = rememberInfiniteTransition()

    val infiniteItems = remember(items) { items + items + items + items + items + items + items + items }

    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "marqueeOffset"
    )

    LaunchedEffect(offset) {
        if (isActive) {
           val maxScroll = scrollState.maxValue
           if (maxScroll > 0) {
               scrollState.scrollTo((maxScroll * offset).toInt())
           }
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState, enabled = false),
        horizontalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        infiniteItems.forEachIndexed { _, item ->
            Tag(
                icon = item.icon,
                text = item.text,
                hot = item.hot,
                isSelected = selectedIds.contains(item.id.toString()),
                onToggle = { onToggle(item.id.toString()) }
            )
        }
    }
}
