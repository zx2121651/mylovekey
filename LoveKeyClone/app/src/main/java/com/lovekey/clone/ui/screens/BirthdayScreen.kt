package com.lovekey.clone.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lovekey.clone.data.MockData.getZodiac
import com.lovekey.clone.ui.components.WheelPicker
import java.util.Calendar

@Composable
fun BirthdayScreen(onPrev: () -> Unit, onNext: () -> Unit) {
    var year by remember { mutableStateOf(2006) }
    var month by remember { mutableStateOf(3) }
    var day by remember { mutableStateOf(16) }

    val years = (1970..2024).toList()
    val months = (1..12).toList()
    val daysInMonth = when (month) {
        2 -> if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) 29 else 28
        4, 6, 9, 11 -> 30
        else -> 31
    }
    val days = (1..daysInMonth).toList()

    // Adjust day if selected day is out of bounds for new month
    LaunchedEffect(daysInMonth) {
        if (day > daysInMonth) day = daysInMonth
    }

    val age = Calendar.getInstance().get(Calendar.YEAR) - year

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F8FD))
            .padding(top = 48.dp, start = 24.dp, end = 24.dp, bottom = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Nav
        Row(
            modifier = Modifier.fillMaxWidth(),
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
                Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color(0xFF1A1A1A)))
                Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color(0xFFDCDFE6)))
            }
            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(42.dp))
        }

        Spacer(modifier = Modifier.height(56.dp))

        // Title
        Text(
            text = "你的生日在哪一天呢",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1A1A),
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Picker Area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(168.dp),
            contentAlignment = Alignment.Center
        ) {
            // Highlight bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White)
                    .shadow(16.dp, spotColor = Color(0x05000000))
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                WheelPicker(items = years, value = year, onChange = { year = it }, unit = "年", modifier = Modifier.weight(1f))
                WheelPicker(items = months, value = month, onChange = { month = it }, unit = "月", modifier = Modifier.weight(1f))
                WheelPicker(items = days, value = day, onChange = { day = it }, unit = "日", modifier = Modifier.weight(1f))
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Bottom Results & Next
        Row(
            modifier = Modifier.padding(bottom = 56.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("${age}岁", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A1A))
            Text(getZodiac(month, day), fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A1A))
        }

        Box(
            modifier = Modifier
                .size(110.dp, 54.dp)
                .clip(CircleShape)
                .background(Color(0xFF667EFE))
                .clickable { onNext() }
                .shadow(20.dp, spotColor = Color(0xFF667EFE)),
            contentAlignment = Alignment.Center
        ) {
            Text("→", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }
    }
}
