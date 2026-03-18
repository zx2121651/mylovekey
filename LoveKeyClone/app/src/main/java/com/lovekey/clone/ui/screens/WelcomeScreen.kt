package com.lovekey.clone.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
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
        Box(modifier = Modifier.fillMaxSize().padding(top = 40.dp)) {
            Text(
                "Love",
                color = Color.White.copy(alpha = 0.08f),
                fontSize = 140.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier
                    .offset(x = (-40).dp, y = (-20).dp)
                    .rotate(-12f)
            )
            Text(
                "Key",
                color = Color.White.copy(alpha = 0.08f),
                fontSize = 140.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier
                    .offset(x = 40.dp, y = 180.dp)
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
                            .clip(RoundedCornerShape(28.dp))
                            .background(Color.White.copy(alpha = 0.4f))
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .offset(x = 24.dp)
                            .size(88.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFE03D))
                    )
                }

                Text(
                    text = "欢迎来到Lovekey",
                    color = Color.White,
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
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
                    .height(56.dp)
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
