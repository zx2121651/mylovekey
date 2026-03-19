package com.lovekey.clone.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke

@Composable
fun MaleAvatar(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val pink = Color(0xFFF8C5CD)
        val blue = Color(0xFFB9C7FF)
        val black = Color(0xFF1A1A1A)

        // Basic shapes mimicking SVG paths
        drawCircle(color = pink, radius = size.minDimension * 0.15f, center = Offset(size.width * 0.5f, size.height * 0.35f))

        // Glasses
        drawCircle(color = black, radius = size.minDimension * 0.05f, center = Offset(size.width * 0.4f, size.height * 0.35f), style = Stroke(width = 5f))
        drawCircle(color = black, radius = size.minDimension * 0.05f, center = Offset(size.width * 0.6f, size.height * 0.35f), style = Stroke(width = 5f))
        drawLine(color = black, start = Offset(size.width * 0.45f, size.height * 0.35f), end = Offset(size.width * 0.55f, size.height * 0.35f), strokeWidth = 5f)

        // Body
        drawArc(color = blue, startAngle = 180f, sweepAngle = 180f, useCenter = false, topLeft = Offset(size.width * 0.2f, size.height * 0.5f), size = Size(size.width * 0.6f, size.height * 0.5f))

        // Simple Smile
        drawArc(
            color = black,
            startAngle = 0f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = Offset(size.width * 0.45f, size.height * 0.42f),
            size = Size(size.width * 0.1f, size.height * 0.05f),
            style = Stroke(width = 5f, cap = StrokeCap.Round)
        )
    }
}

@Composable
fun FemaleAvatar(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val skin = Color(0xFFFFD5CF)
        val black = Color(0xFF1A1A1A)

        // Face
        drawCircle(color = skin, radius = size.minDimension * 0.18f, center = Offset(size.width * 0.5f, size.height * 0.4f))

        // Hair bun
        drawCircle(color = black, radius = size.minDimension * 0.15f, center = Offset(size.width * 0.75f, size.height * 0.4f))

        // Bangs (Simplified)
        val path = Path().apply {
            moveTo(size.width * 0.4f, size.height * 0.25f)
            quadraticBezierTo(size.width * 0.6f, size.height * 0.2f, size.width * 0.8f, size.height * 0.4f)
            lineTo(size.width * 0.8f, size.height * 0.5f)
            quadraticBezierTo(size.width * 0.5f, size.height * 0.35f, size.width * 0.4f, size.height * 0.25f)
            close()
        }
        drawPath(path, color = black)

        // Eyes (closed)
        val eyePath = Path().apply {
            moveTo(size.width * 0.4f, size.height * 0.4f)
            quadraticBezierTo(size.width * 0.45f, size.height * 0.42f, size.width * 0.5f, size.height * 0.4f)
        }
        drawPath(eyePath, color = black, style = Stroke(width = 4f, cap = StrokeCap.Round))

        // Smile
        val smilePath = Path().apply {
            moveTo(size.width * 0.45f, size.height * 0.5f)
            quadraticBezierTo(size.width * 0.5f, size.height * 0.52f, size.width * 0.55f, size.height * 0.5f)
        }
        drawPath(smilePath, color = black, style = Stroke(width = 4f, cap = StrokeCap.Round))
    }
}

// Sparkle Icon used in loading screen
@Composable
fun SparkleIcon(modifier: Modifier = Modifier, color: Color = Color(0xFFFFF280)) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val path = Path().apply {
            moveTo(w * 0.5f, 0f)
            quadraticBezierTo(w * 0.5f, h * 0.5f, w, h * 0.5f)
            quadraticBezierTo(w * 0.5f, h * 0.5f, w * 0.5f, h)
            quadraticBezierTo(w * 0.5f, h * 0.5f, 0f, h * 0.5f)
            quadraticBezierTo(w * 0.5f, h * 0.5f, w * 0.5f, 0f)
            close()
        }
        drawPath(path, color)
    }
}
