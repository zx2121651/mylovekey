package com.lovekey.clone.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lovekey.clone.ui.components.FemaleAvatar
import com.lovekey.clone.ui.components.MaleAvatar

@Composable
fun GenderScreen(onNext: () -> Unit) {
    var selected by remember { mutableStateOf("male") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F8FD))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Step Indicator
        Row(
            modifier = Modifier.padding(top = 40.dp, bottom = 56.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color(0xFF1A1A1A)))
            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color(0xFFDCDFE6)))
            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color(0xFFDCDFE6)))
        }

        Text(
            text = "选择性别",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1A1A),
            modifier = Modifier.padding(bottom = 48.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            GenderCard(
                type = "male",
                label = "男",
                isSelected = selected == "male",
                onClick = { selected = "male" },
                modifier = Modifier.padding(end = 20.dp)
            )
            GenderCard(
                type = "female",
                label = "女",
                isSelected = selected == "female",
                onClick = { selected = "female" }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .padding(bottom = 40.dp)
                .size(110.dp, 54.dp)
                .clip(CircleShape)
                .background(Color(0xFF667EFE))
                .clickable { onNext() }
                .shadow(20.dp, spotColor = Color(0xFF667EFE)),
            contentAlignment = Alignment.Center
        ) {
            // Right Arrow
            Text("→", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun GenderCard(
    type: String,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(if (isSelected) 1.05f else 1.0f)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(145.dp)
                .clip(RoundedCornerShape(26.dp))
                .background(Color.White)
                .border(
                    width = 2.5.dp,
                    color = if (isSelected) Color(0xFF7F95FF) else Color.Transparent,
                    shape = RoundedCornerShape(26.dp)
                )
                .shadow(if (isSelected) 0.dp else 16.dp, spotColor = Color(0x0A000000)),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .scale(scale)
                    .padding(20.dp)
            ) {
                if (type == "male") MaleAvatar() else FemaleAvatar()
            }
        }
        Text(
            text = label,
            fontSize = 17.sp,
            fontWeight = FontWeight.Medium,
            color = if (isSelected) Color(0xFF1A1A1A) else Color(0xFF666666),
            modifier = Modifier.padding(top = 24.dp)
        )
    }
}
