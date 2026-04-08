package com.lovekey.clone.ime

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun T9KeyboardLayout(
    theme: KeyboardTheme,
    onKeyPress: (String) -> Unit,
    onDelete: () -> Unit,
    onClear: () -> Unit,
    onMoveCursor: (Int) -> Unit,
    onKeyActionStart: (String, Offset) -> Unit,
    onKeyActionEnd: () -> Unit,
    onEnter: () -> Unit,
    onSwitchMode: (KeyboardMode) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var isDeleting by remember { mutableStateOf(false) }
    var isClearing by remember { mutableStateOf(false) }

    // Column contains 4 rows
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp)) {

        // Row 1
        Row(modifier = Modifier.fillMaxWidth().weight(1f).padding(vertical = 2.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            // Left col: Symbols
            T9KeyBox(modifier = Modifier.weight(1.5f), mainText = "符", subText = "", theme = theme, isSpecial = true, onClick = { })
            // Center grid: 1, 2, 3
            T9KeyBox(modifier = Modifier.weight(2f), mainText = "分词", subText = "1", theme = theme, onClick = { onKeyPress("1") }, onActionStart = onKeyActionStart, onActionEnd = onKeyActionEnd)
            T9KeyBox(modifier = Modifier.weight(2f), mainText = "ABC", subText = "2", theme = theme, onClick = { onKeyPress("2") }, onActionStart = onKeyActionStart, onActionEnd = onKeyActionEnd)
            T9KeyBox(modifier = Modifier.weight(2f), mainText = "DEF", subText = "3", theme = theme, onClick = { onKeyPress("3") }, onActionStart = onKeyActionStart, onActionEnd = onKeyActionEnd)
            // Right col: Delete
            T9KeyBoxWithRepeat(modifier = Modifier.weight(1.5f), mainText = "⌫", subText = "", theme = theme, isSpecial = true,
                onPress = { isDeleting = true; onDelete() },
                onRelease = { isDeleting = false },
                onRepeat = { onDelete() },
                coroutineScope = coroutineScope)
        }

        // Row 2
        Row(modifier = Modifier.fillMaxWidth().weight(1f).padding(vertical = 2.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            // Left col: English Mode
            T9KeyBox(modifier = Modifier.weight(1.5f), mainText = "EN", subText = "", theme = theme, isSpecial = true, onClick = { onSwitchMode(KeyboardMode.QWERTY_EN) })
            // Center grid: 4, 5, 6
            T9KeyBox(modifier = Modifier.weight(2f), mainText = "GHI", subText = "4", theme = theme, onClick = { onKeyPress("4") }, onActionStart = onKeyActionStart, onActionEnd = onKeyActionEnd)
            T9KeyBox(modifier = Modifier.weight(2f), mainText = "JKL", subText = "5", theme = theme, onClick = { onKeyPress("5") }, onActionStart = onKeyActionStart, onActionEnd = onKeyActionEnd)
            T9KeyBox(modifier = Modifier.weight(2f), mainText = "MNO", subText = "6", theme = theme, onClick = { onKeyPress("6") }, onActionStart = onKeyActionStart, onActionEnd = onKeyActionEnd)
            // Right col: Clear
            T9KeyBoxWithRepeat(modifier = Modifier.weight(1.5f), mainText = "重输", subText = "", theme = theme, isSpecial = true,
                onPress = { isClearing = true; onClear() },
                onRelease = { isClearing = false },
                onRepeat = { onClear() },
                coroutineScope = coroutineScope)
        }

        // Row 3
        Row(modifier = Modifier.fillMaxWidth().weight(1f).padding(vertical = 2.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            // Left col: QWERTY Pinyin Mode
            T9KeyBox(modifier = Modifier.weight(1.5f), mainText = "全拼", subText = "", theme = theme, isSpecial = true, onClick = { onSwitchMode(KeyboardMode.QWERTY_PINYIN) })
            // Center grid: 7, 8, 9
            T9KeyBox(modifier = Modifier.weight(2f), mainText = "PQRS", subText = "7", theme = theme, onClick = { onKeyPress("7") }, onActionStart = onKeyActionStart, onActionEnd = onKeyActionEnd)
            T9KeyBox(modifier = Modifier.weight(2f), mainText = "TUV", subText = "8", theme = theme, onClick = { onKeyPress("8") }, onActionStart = onKeyActionStart, onActionEnd = onKeyActionEnd)
            T9KeyBox(modifier = Modifier.weight(2f), mainText = "WXYZ", subText = "9", theme = theme, onClick = { onKeyPress("9") }, onActionStart = onKeyActionStart, onActionEnd = onKeyActionEnd)
            // Right col: Top half of Enter
            Box(modifier = Modifier.weight(1.5f).fillMaxHeight()
                .clip(RoundedCornerShape(8.dp)).background(theme.accentColor)
                .clickable { onEnter() },
                contentAlignment = Alignment.Center
            ) {
                Text("发送", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }

        // Row 4
        Row(modifier = Modifier.fillMaxWidth().weight(1f).padding(vertical = 2.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            // Left col: Numbers/Symbols Mode
            T9KeyBox(modifier = Modifier.weight(1.5f), mainText = "?123", subText = "", theme = theme, isSpecial = true, onClick = { onSwitchMode(KeyboardMode.SYMBOLS) })
            // Center grid: Comma, 0, Period (or Space cursor control)
            T9KeyBox(modifier = Modifier.weight(2f), mainText = ",", subText = "", theme = theme, onClick = { onKeyPress(",") }, onActionStart = onKeyActionStart, onActionEnd = onKeyActionEnd)

            var lastDragAmount = 0f
            Box(
                modifier = Modifier.weight(2f).fillMaxHeight()
                    .clip(RoundedCornerShape(8.dp)).background(theme.keyBackground)
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = { onKeyPress("0") })
                    }
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures(
                            onDragStart = { lastDragAmount = 0f },
                            onHorizontalDrag = { change, dragAmount ->
                                change.consume()
                                lastDragAmount += dragAmount
                                val threshold = 30f
                                if (lastDragAmount > threshold) {
                                    onMoveCursor(1)
                                    lastDragAmount = 0f
                                } else if (lastDragAmount < -threshold) {
                                    onMoveCursor(-1)
                                    lastDragAmount = 0f
                                }
                            }
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("0", color = theme.keyTextColor, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text("空格", color = theme.keyTextColor.copy(alpha = 0.6f), fontSize = 10.sp)
                }
            }
            T9KeyBox(modifier = Modifier.weight(2f), mainText = ".", subText = "", theme = theme, onClick = { onKeyPress(".") }, onActionStart = onKeyActionStart, onActionEnd = onKeyActionEnd)
            // Right col: Bottom half of Enter (Since compose doesn't support row-span easily, we make this a spacer or another enter, or we handle it visually. We'll make it part of Enter)
            Box(modifier = Modifier.weight(1.5f).fillMaxHeight()
                .clip(RoundedCornerShape(8.dp)).background(theme.accentColor)
                .clickable { onEnter() },
                contentAlignment = Alignment.Center
            ) {
                Text("↵", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun T9KeyBox(
    modifier: Modifier,
    mainText: String,
    subText: String,
    theme: KeyboardTheme,
    isSpecial: Boolean = false,
    onClick: () -> Unit,
    onActionStart: ((String, Offset) -> Unit)? = null,
    onActionEnd: (() -> Unit)? = null
) {
    var keyPosition by remember { mutableStateOf(Offset.Zero) }

    Box(
        modifier = modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSpecial) theme.actionKeyBackground else theme.keyBackground)
            .onGloballyPositioned { coordinates ->
                keyPosition = coordinates.positionInWindow()
            }
            .pointerInput(mainText) {
                detectTapGestures(
                    onPress = {
                        if (onActionStart != null) {
                            onActionStart(mainText, keyPosition)
                        }
                        try {
                            awaitRelease()
                        } finally {
                            if (onActionEnd != null) {
                                onActionEnd()
                            }
                            onClick()
                        }
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text(
                text = mainText,
                color = theme.keyTextColor,
                fontSize = if (isSpecial) 16.sp else 18.sp,
                fontWeight = if (isSpecial) FontWeight.Medium else FontWeight.Bold
            )
            if (subText.isNotEmpty()) {
                Text(
                    text = subText,
                    color = theme.keyTextColor.copy(alpha = 0.6f),
                    fontSize = 10.sp
                )
            }
        }
    }
}

@Composable
fun T9KeyBoxWithRepeat(
    modifier: Modifier,
    mainText: String,
    subText: String,
    theme: KeyboardTheme,
    isSpecial: Boolean = false,
    onPress: () -> Unit,
    onRelease: () -> Unit,
    onRepeat: () -> Unit,
    coroutineScope: kotlinx.coroutines.CoroutineScope
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSpecial) theme.actionKeyBackground else theme.keyBackground)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        onPress()
                        val job = coroutineScope.launch {
                            delay(400)
                            while (true) {
                                onRepeat()
                                delay(50)
                            }
                        }
                        try {
                            awaitRelease()
                        } finally {
                            job.cancel()
                            onRelease()
                        }
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text(
                text = mainText,
                color = theme.keyTextColor,
                fontSize = if (isSpecial) 16.sp else 18.sp,
                fontWeight = if (isSpecial) FontWeight.Medium else FontWeight.Bold
            )
            if (subText.isNotEmpty()) {
                Text(
                    text = subText,
                    color = theme.keyTextColor.copy(alpha = 0.6f),
                    fontSize = 10.sp
                )
            }
        }
    }
}
