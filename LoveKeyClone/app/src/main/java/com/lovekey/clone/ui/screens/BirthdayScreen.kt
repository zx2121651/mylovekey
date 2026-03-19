package com.lovekey.clone.ui.screens

import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lovekey.clone.ui.components.WheelPicker
import com.lovekey.clone.data.MockData.getZodiac
import java.util.Calendar

@Composable
fun CakeIcon() {
    Canvas(modifier = Modifier.size(90.dp)) {
        val width = size.width
        val height = size.height

        // Base
        drawRoundRect(
            color = Color(0xFFF8B2AA),
            topLeft = androidx.compose.ui.geometry.Offset(width * 0.25f, height * 0.7f),
            size = androidx.compose.ui.geometry.Size(width * 0.5f, height * 0.08f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(2.dp.toPx(), 2.dp.toPx())
        )
        drawRoundRect(
            color = Color(0xFFF8B2AA),
            topLeft = androidx.compose.ui.geometry.Offset(width * 0.2f, height * 0.78f),
            size = androidx.compose.ui.geometry.Size(width * 0.6f, height * 0.06f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(2.dp.toPx(), 2.dp.toPx())
        )

        // Cake Body
        drawRoundRect(
            color = Color(0xFFC6D3FC),
            topLeft = androidx.compose.ui.geometry.Offset(width * 0.28f, height * 0.52f),
            size = androidx.compose.ui.geometry.Size(width * 0.44f, height * 0.18f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(4.dp.toPx(), 4.dp.toPx())
        )

        // Frosting details (waves)
        val frostingPath = Path().apply {
            moveTo(width * 0.28f, height * 0.62f)
            quadraticBezierTo(width * 0.33f, height * 0.56f, width * 0.38f, height * 0.62f)
            quadraticBezierTo(width * 0.43f, height * 0.56f, width * 0.48f, height * 0.62f)
            quadraticBezierTo(width * 0.53f, height * 0.56f, width * 0.58f, height * 0.62f)
            quadraticBezierTo(width * 0.63f, height * 0.56f, width * 0.68f, height * 0.62f)
            lineTo(width * 0.72f, height * 0.62f)
        }
        drawPath(
            path = frostingPath,
            color = Color(0xFF1A1A1A),
            style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round)
        )

        // Candle
        drawRoundRect(
            color = Color.White,
            topLeft = androidx.compose.ui.geometry.Offset(width * 0.47f, height * 0.34f),
            size = androidx.compose.ui.geometry.Size(width * 0.06f, height * 0.18f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(1.dp.toPx(), 1.dp.toPx()),
            style = androidx.compose.ui.graphics.drawscope.Fill
        )
        drawRoundRect(
            color = Color(0xFF1A1A1A),
            topLeft = androidx.compose.ui.geometry.Offset(width * 0.47f, height * 0.34f),
            size = androidx.compose.ui.geometry.Size(width * 0.06f, height * 0.18f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(1.dp.toPx(), 1.dp.toPx()),
            style = Stroke(width = 2.5.dp.toPx())
        )

        // Flame
        val flamePath = Path().apply {
            moveTo(width * 0.5f, height * 0.14f)
            cubicTo(width * 0.44f, height * 0.24f, width * 0.44f, height * 0.32f, width * 0.56f, height * 0.28f)
            cubicTo(width * 0.56f, height * 0.24f, width * 0.5f, height * 0.14f, width * 0.5f, height * 0.14f)
            close()
        }
        drawPath(flamePath, color = Color.White, style = androidx.compose.ui.graphics.drawscope.Fill)
        drawPath(flamePath, color = Color(0xFF1A1A1A), style = Stroke(width = 2.5.dp.toPx(), join = StrokeJoin.Round))
    }
}

@Composable
fun BirthdayScreen(onPrev: () -> Unit, onNext: () -> Unit) {
    var year by remember { mutableStateOf(2006) }
    var month by remember { mutableStateOf(3) }
    var day by remember { mutableStateOf(16) }

    val years = remember { (1970..2024).toList() }
    val months = remember { (1..12).toList() }

    val daysInMonth = remember(year, month) {
        val cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.MONTH, month - 1)
        cal.getActualMaximum(Calendar.DAY_OF_MONTH)
    }
    val days = remember(daysInMonth) { (1..daysInMonth).toList() }

    val age = Calendar.getInstance().get(Calendar.YEAR) - year

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
                Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color(0xFF1A1A1A)))
                Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color(0xFFDCDFE6)))
            }
            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(42.dp))
        }

        Spacer(modifier = Modifier.height(56.dp))

        // Title Area
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CakeIcon()
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "你的生日在哪一天呢",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A1A),
                letterSpacing = 1.sp
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Date Picker
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(168.dp),
            contentAlignment = Alignment.Center
        ) {
            // Highlight background
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp)
                    .shadow(16.dp, RoundedCornerShape(14.dp), spotColor = Color(0x05000000))
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White)
            )

            Row(
                modifier = Modifier.fillMaxWidth().height(168.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                WheelPicker(items = years, value = year, onChange = { year = it }, unit = "年", modifier = Modifier.weight(1f))
                WheelPicker(items = months, value = month, onChange = { month = it }, unit = "月", modifier = Modifier.weight(1f))
                WheelPicker(items = days, value = day, onChange = { day = it }, unit = "日", modifier = Modifier.weight(1f))
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Bottom Info & Button
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = "${age}岁",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A1A),
                    letterSpacing = 2.sp
                )
                Text(
                    text = getZodiac(month, day),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A1A),
                    letterSpacing = 2.sp
                )
            }

            Spacer(modifier = Modifier.height(56.dp))

            Box(
                modifier = Modifier
                    .width(110.dp)
                    .height(54.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF667EFE))
                    .clickable { onNext() }
                    .shadow(20.dp, spotColor = Color(0x4D667EFE)),
                contentAlignment = Alignment.Center
            ) {
                Text("→", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
