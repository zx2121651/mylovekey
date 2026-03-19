package com.lovekey.clone.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lovekey.clone.ui.theme.ActiveBlue
import com.lovekey.clone.ui.theme.TextMain

@Composable
fun Tag(
    icon: String,
    text: String,
    hot: Boolean,
    isSelected: Boolean,
    onToggle: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 4.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null, // remove ripple to simulate active:scale-95 (will add scale modifier later)
                onClick = onToggle
            )
    ) {
        Row(
            modifier = Modifier
                .height(54.dp)
                .clip(CircleShape)
                .background(if (isSelected) ActiveBlue else Color.White)
                .shadow(if (isSelected) 16.dp else 12.dp, ambientColor = if (isSelected) ActiveBlue else Color.Black, spotColor = if (isSelected) ActiveBlue else Color.Black)
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon.isNotEmpty()) {
                Text(
                    text = icon,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(end = 10.dp)
                )
            }
            Text(
                text = text,
                color = if (isSelected) Color.White else Color(0xFF333333),
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )
        }
        if (hot) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 4.dp, y = (-4).dp)
                    .clip(CircleShape)
                    .background(Brush.horizontalGradient(listOf(Color(0xFFFF923D), Color(0xFFFF452F))))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "HOT",
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
