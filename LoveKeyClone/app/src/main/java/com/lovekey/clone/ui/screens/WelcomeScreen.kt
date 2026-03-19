package com.lovekey.clone.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WelcomeScreen(onNext: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF4A64FF), Color(0xFF6E82FF))))
            .padding(top = 64.dp, bottom = 32.dp, start = 36.dp, end = 36.dp)
    ) {
        // Background large texts
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                "Love",
                color = Color.White.copy(alpha = 0.08f),
                fontSize = 180.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Black,
                modifier = Modifier
                    .offset(x = (-60).dp, y = (-40).dp)
                    .rotate(-12f)
            )
            Text(
                "Key",
                color = Color.White.copy(alpha = 0.08f),
                fontSize = 180.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Black,
                modifier = Modifier
                    .offset(x = 80.dp, y = 140.dp)
                    .rotate(-12f)
            )
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Top Graphic
                Box(modifier = Modifier.size(144.dp).padding(bottom = 40.dp)) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .size(130.dp, 86.dp)
                            .shadow(8.dp, RoundedCornerShape(28.dp))
                            .clip(RoundedCornerShape(28.dp))
                            .background(Color.White.copy(alpha = 0.4f))
                            .border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(28.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(4),
                            modifier = Modifier.padding(16.dp).fillMaxSize(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            userScrollEnabled = false
                        ) {
                            items(12) {
                                Box(modifier = Modifier.size(16.dp).clip(RoundedCornerShape(4.dp)).background(Color.White.copy(alpha = 0.3f)))
                            }
                        }
                    }
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .offset(x = 24.dp)
                            .size(88.dp)
                            .shadow(4.dp, CircleShape)
                            .clip(CircleShape)
                            .background(Color(0xFFFFE03D)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(top = 8.dp)) {
                            Row(horizontalArrangement = Arrangement.spacedBy(20.dp), modifier = Modifier.padding(bottom = 4.dp)) {
                                Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(Color(0xFF1F2937)))
                                Box(modifier = Modifier.width(10.dp).height(6.dp).clip(RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp)).background(Color(0xFF1F2937)))
                            }
                            Box(modifier = Modifier.width(32.dp).height(16.dp).border(4.dp, Color(0xFF1F2937), CircleShape).clip(CircleShape))
                        }
                    }
                }

                Text(
                    text = "欢迎来到Lovekey",
                    color = Color.White,
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-1).sp,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                Text(
                    text = buildAnnotatedString {
                        append("为了给您提供更全面的服务。我们将通过")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.White)) {
                            append("《用户协议》")
                        }
                        append("和")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.White)) {
                            append("《隐私协议》")
                        }
                        append("了解您的个人信息情况。")
                    },
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp,
                    lineHeight = 22.sp
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .height(60.dp)
                    .clip(CircleShape)
                    .background(Color.Black)
                    .clickable { onNext() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "同意并进入",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
