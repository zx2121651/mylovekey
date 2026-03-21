package com.lovekey.clone.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class ChatMessage(val id: Long, val type: String, val text: String)


@Composable
fun BrushStrokeBg(content: @Composable () -> Unit) {
    Box(contentAlignment = Alignment.Center) {
        androidx.compose.foundation.Canvas(modifier = Modifier.matchParentSize()) {
            val w = size.width
            val h = size.height
            val path1 = androidx.compose.ui.graphics.Path().apply {
                moveTo(w * 0.05f, h * 0.5f)
                quadraticBezierTo(w * 0.2f, h * 0.25f, w * 0.5f, h * 0.33f)
                quadraticBezierTo(w * 0.95f, h * 0.41f, w * 0.6f, h * 0.66f)
                quadraticBezierTo(w * 0.075f, h * 0.75f, w * 0.05f, h * 0.5f)
                close()
            }
            drawPath(path1, color = Color(0xFF3B5BFF).copy(alpha=0.95f))

            val path2 = androidx.compose.ui.graphics.Path().apply {
                moveTo(w * 0.025f, h * 0.41f)
                quadraticBezierTo(w * 0.25f, h * 0.08f, w * 0.75f, h * 0.25f)
                quadraticBezierTo(w * 0.975f, h * 0.58f, w * 0.85f, h * 0.91f)
                quadraticBezierTo(w * 0.4f, h * 0.83f, w * 0.025f, h * 0.58f)
                close()
            }
            drawPath(path2, color = Color(0xFF4B6BFF).copy(alpha=0.8f))
        }
        content()
    }
}

@Composable
fun ChatKeyboardScreen(isTutorial: Boolean, onComplete: () -> Unit) {
    var tutPhase by remember { mutableStateOf(if (isTutorial) 0 else 5) }
    var showToast by remember { mutableStateOf(false) }
    var inputText by remember { mutableStateOf("") }
    var clipboardUsed by remember { mutableStateOf(false) }

    var messages by remember {
        mutableStateOf(
            listOf(
                ChatMessage(1, "bot", "👋 欢迎使用「Lovekey键盘」\n点击任一对话去粘贴，选择任意回复方式去试用吧~"),
                ChatMessage(2, "user", "在干嘛？"),
                ChatMessage(3, "user", "我去洗澡了")
            )
        )
    }

    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    val handleNextPhase = {
        if (isTutorial) {
            if (tutPhase == 0) {
                showToast = true
                coroutineScope.launch {
                    delay(2000)
                    showToast = false
                }
                tutPhase = 1
            } else if (tutPhase < 4) {
                tutPhase++
            }
        }
    }

    val handleAppTagClick = { tagText: String ->
        if (!isTutorial) {
            val mockReplies = mapOf(
                "高情商" to "在呼吸，在心跳，在想你呀~",
                "心动狙击" to "在想怎么回复才能让你心动💓",
                "幽默" to "在思考宇宙的终极奥秘...顺便想你",
                "暖男" to "刚忙完，正准备找你呢，你今天累不累？",
                "暧昧拉扯" to "你猜猜看？猜对有奖哦~",
                "情场高手" to "本来在发呆，看到你的消息心跳就漏了一拍",
                "温柔大叔" to "乖，别闹，正想着带你去吃什么好吃的",
                "风流浪子" to "在看手机，等一个漂亮女孩的消息，哎，这不就来了嘛",
                "幽默有梗" to "在进行光合作用，维持生命体征"
            )
            inputText = mockReplies[tagText] ?: "[$tagText] 生成的回复..."
        }
    }

    val handleAppSend = {
        if (!isTutorial && inputText.isNotEmpty()) {
            messages = messages + ChatMessage(System.currentTimeMillis(), "me", inputText)
            inputText = ""
            clipboardUsed = true
            coroutineScope.launch {
                delay(100)
                listState.animateScrollToItem(messages.size - 1)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F2F7))
    ) {
        // Top App Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(if (isTutorial) Color(0xFFF2F2F7) else Color.White)
                .border(0.dp, Color.Transparent)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .clickable { if (!isTutorial) onComplete() }
                    .shadow(2.dp, spotColor = Color(0x1A000000)),
                contentAlignment = Alignment.Center
            ) {
                Text("<", color = Color(0xFF333333), fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            if (isTutorial) {
                Row(modifier = Modifier.clickable { onComplete() }, verticalAlignment = Alignment.CenterVertically) {
                    Text("我会用，跳过 ", color = Color(0xFF888888), fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    Text("→", color = Color(0xFF888888), fontSize = 14.sp)
                }
            } else {
                Text("日常聊天", color = Color(0xFF1A1A1A), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(32.dp))
            }
        }

        // Chat List Area
        Box(modifier = Modifier.weight(1f)) {
            if (isTutorial && tutPhase < 4) {
                Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.4f)).clickable { handleNextPhase() })
            }

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (isTutorial) {
                    item {
                        Row(verticalAlignment = Alignment.Top) {
                            Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(Color(0xFFE5E9FF)).border(1.dp, Color(0xFFD0D9FF), CircleShape), contentAlignment = Alignment.Center) { Text("😊", fontSize = 20.sp) }
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(modifier = Modifier.widthIn(max = 240.dp).clip(RoundedCornerShape(topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp)).background(Color.White).padding(14.dp).shadow(2.dp, spotColor = Color(0x0A000000))) {
                                Text("👋 欢迎使用「Lovekey键盘」\n点击任一对话去粘贴，选择任意回复方式去试用吧~", color = Color(0xFF333333), fontSize = 14.sp, lineHeight = 20.sp)
                            }
                        }
                    }
                    item {
                        Box(
                            modifier = Modifier
                                .padding(start = 48.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.White)
                                .border(if (tutPhase == 0) 2.dp else 0.dp, if (tutPhase == 0) Color(0xFF3B5BFF) else Color.Transparent, RoundedCornerShape(16.dp))
                                .clickable { if (tutPhase == 0) handleNextPhase() }
                                .padding(14.dp)
                                .shadow(2.dp, spotColor = Color(0x0A000000))
                                .fillMaxWidth(0.8f)
                        ) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("在干嘛？", color = Color(0xFF333333), fontSize = 15.sp)
                                Text("📋", color = Color(0xFF888888), fontSize = 14.sp)
                            }
                        }
                    }
                    item {
                        Box(
                            modifier = Modifier
                                .padding(start = 48.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.White)
                                .padding(14.dp)
                                .shadow(2.dp, spotColor = Color(0x0A000000))
                                .fillMaxWidth(0.8f)
                        ) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("我去洗澡了", color = Color(0xFF333333), fontSize = 15.sp)
                                Text("📋", color = Color(0xFF888888), fontSize = 14.sp)
                            }
                        }
                    }
                    if (tutPhase >= 3) {
                        item {
                            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                                Box(modifier = Modifier.widthIn(max = 240.dp).clip(RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp)).background(Color(0xFF5C73FF)).padding(14.dp).shadow(2.dp, spotColor = Color(0x1A000000))) {
                                    Text("如果我说我在等你的回复呢", color = Color.White, fontSize = 15.sp)
                                }
                            }
                        }
                        item {
                            Row(verticalAlignment = Alignment.Top, modifier = Modifier.padding(top = 16.dp)) {
                                Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(Color(0xFFE5E9FF)).border(1.dp, Color(0xFFD0D9FF), CircleShape), contentAlignment = Alignment.Center) { Text("😊", fontSize = 20.sp) }
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(modifier = Modifier.widthIn(max = 240.dp).clip(RoundedCornerShape(topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp)).background(Color.White).padding(14.dp).shadow(2.dp, spotColor = Color(0x0A000000))) {
                                    Text("🎉如您遇到其他问题，可点击在线客服，帮您解决", color = Color(0xFF333333), fontSize = 14.sp, lineHeight = 20.sp)
                                }
                            }
                        }
                    }
                } else {
                    items(messages) { msg ->
                        when (msg.type) {
                            "bot" -> {
                                Row(verticalAlignment = Alignment.Top) {
                                    Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(Color(0xFFE5E9FF)).border(1.dp, Color(0xFFD0D9FF), CircleShape), contentAlignment = Alignment.Center) { Text("😊", fontSize = 20.sp) }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Box(modifier = Modifier.widthIn(max = 240.dp).clip(RoundedCornerShape(topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp)).background(Color.White).padding(14.dp).shadow(2.dp, spotColor = Color(0x0A000000))) {
                                        Text(msg.text, color = Color(0xFF333333), fontSize = 14.sp, lineHeight = 20.sp)
                                    }
                                }
                            }
                            "me" -> {
                                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                                    Box(modifier = Modifier.widthIn(max = 240.dp).clip(RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp)).background(Color(0xFF5C73FF)).padding(14.dp).shadow(2.dp, spotColor = Color(0x1A000000))) {
                                        Text(msg.text, color = Color.White, fontSize = 15.sp)
                                    }
                                }
                            }
                            "user" -> {
                                Box(
                                    modifier = Modifier
                                        .padding(start = 48.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color.White)
                                        .clickable { if (!clipboardUsed) inputText = msg.text }
                                        .padding(14.dp)
                                        .shadow(2.dp, spotColor = Color(0x0A000000))
                                        .fillMaxWidth(0.8f)
                                ) {
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text(msg.text, color = Color(0xFF333333), fontSize = 15.sp)
                                        Text("📋", color = Color(0xFF888888), fontSize = 14.sp)
                                    }
                                }
                            }
                        }
                    }
                }
                item { Spacer(modifier = Modifier.height(20.dp)) }
            }

            // Toast
            androidx.compose.animation.AnimatedVisibility(
                visible = showToast,
                modifier = Modifier.align(Alignment.Center)
            ) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.6f))
                        .padding(horizontal = 24.dp, vertical = 10.dp)
                ) {
                    Text("复制成功", color = Color.White, fontSize = 14.sp)
                }
            }
        }

        // Bottom Keyboard Area
        if (isTutorial) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF2F2F7))
                    .padding(bottom = 24.dp)
            ) {
                // Input Bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .border(1.dp, Color(0xFFE5E5EA), RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 10.dp)
                ) {
                    if (tutPhase >= 2) {
                        Text("如果我说我在等你的回复呢", color = Color(0xFF333333), fontSize = 14.sp)
                    } else {
                        Text("👇 在键盘粘贴问题后，选择回复方式", color = Color(0xFFA0A5B5), fontSize = 13.sp)
                    }
                }

                // Keyboard Top Action Row
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (tutPhase == 3) Color.White else Color(0xFFEAECEF))
                            .border(if (tutPhase == 3) 2.dp else 0.dp, if (tutPhase == 3) Color(0xFF3B5BFF) else Color.Transparent, CircleShape)
                            .clickable { if (tutPhase == 3) handleNextPhase() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("⌨", color = Color(0xFF5C73FF), fontSize = 16.sp)
                    }
                    Box(modifier = Modifier.height(32.dp).clip(CircleShape).background(Color(0xFF4B66FF)).padding(horizontal = 14.dp), contentAlignment = Alignment.Center) { Text("帮你回", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold) }
                    Box(modifier = Modifier.height(32.dp).clip(CircleShape).background(Color.White).padding(horizontal = 14.dp), contentAlignment = Alignment.Center) { Text("超会说", color = Color(0xFF555555), fontSize = 13.sp, fontWeight = FontWeight.Bold) }
                    Spacer(modifier = Modifier.weight(1f))
                    Box(modifier = Modifier.size(32.dp).clip(CircleShape).border(1.dp, Color(0xFFD3D8E6), CircleShape), contentAlignment = Alignment.Center) { Text("Hi", color = Color(0xFF888888), fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                    Box(modifier = Modifier.size(32.dp).clip(CircleShape).border(1.dp, Color(0xFFD3D8E6), CircleShape), contentAlignment = Alignment.Center) { Text("⊞", color = Color(0xFF888888), fontSize = 16.sp) }

                    // Heart Badge simplified
                    Box(modifier = Modifier.height(26.dp).clip(CircleShape).background(Color(0xFFFF4B6B)).padding(horizontal = 8.dp), contentAlignment = Alignment.Center) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("❤", color = Color.White, fontSize = 10.sp)
                            Text("100%", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Keyboard Paste Row
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier.weight(1f).height(48.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFFEAECEF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(if (tutPhase >= 1) "在干嘛？" else "+ 请粘贴TA的话", color = Color(0xFF555555), fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }
                    Box(
                        modifier = Modifier.width(60.dp).height(48.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFF4B66FF).copy(alpha=0.8f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("粘贴", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Keyboard Grid
                Column(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Box(modifier = Modifier.weight(1f).height(44.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFFEAECEF)), contentAlignment = Alignment.Center) { Text("情绪价值", color = Color(0xFF555555), fontSize = 13.sp) }
                        Box(modifier = Modifier.weight(1f).height(44.dp).clip(RoundedCornerShape(12.dp)).background(if (tutPhase == 1) Color.White else Color(0xFFEAECEF)).border(if (tutPhase == 1) 2.dp else 0.dp, if (tutPhase == 1) Color(0xFF3B5BFF) else Color.Transparent, RoundedCornerShape(12.dp)).clickable { if (tutPhase == 1) handleNextPhase() }, contentAlignment = Alignment.Center) {
                            Text("😊 幽默", color = Color(0xFF555555), fontSize = 13.sp, fontWeight = if (tutPhase==1) FontWeight.Bold else FontWeight.Normal)
                            if (tutPhase == 1) {
                                Box(modifier = Modifier.offset(x = 100.dp, y = (-50).dp)) {
                                    BrushStrokeBg {
                                        Text("试试幽默的回复吧", color = Color.White, fontSize = 20.sp, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic, fontWeight = FontWeight.Black, modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp))
                                    }
                                }
                            }
                        }
                        Box(modifier = Modifier.weight(1f).height(44.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFFEAECEF)), contentAlignment = Alignment.Center) { Text("⌫", color = Color(0xFF555555), fontSize = 18.sp) }
                        Box(modifier = Modifier.weight(1f).height(44.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFFEAECEF)), contentAlignment = Alignment.Center) { Text("🌞 暖男", color = Color(0xFF555555), fontSize = 13.sp) }
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Box(modifier = Modifier.weight(1f).height(44.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFFEAECEF)), contentAlignment = Alignment.Center) { Text("🔥 热烈深情", color = Color(0xFF555555), fontSize = 13.sp) }
                        Box(modifier = Modifier.weight(1f).height(44.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFFEAECEF)), contentAlignment = Alignment.Center) { Text("👑 成熟", color = Color(0xFF555555), fontSize = 13.sp) }
                        Box(modifier = Modifier.weight(1f).height(44.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFFD3D8E6)), contentAlignment = Alignment.Center) { Text("清空", color = Color(0xFF555555), fontSize = 13.sp) }
                        Box(modifier = Modifier.weight(1f).height(44.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFFEAECEF)), contentAlignment = Alignment.Center) { Text("👏 夸夸", color = Color(0xFF555555), fontSize = 13.sp) }
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Box(modifier = Modifier.weight(1f).height(44.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFFEAECEF)), contentAlignment = Alignment.Center) { Text("🕶️ 情场高手", color = Color(0xFF555555), fontSize = 13.sp) }
                        Box(modifier = Modifier.weight(1f).height(44.dp).clip(RoundedCornerShape(12.dp)).background(Color.White), contentAlignment = Alignment.Center) { Text("+", color = Color(0xFF555555), fontSize = 20.sp, fontWeight = FontWeight.Bold) }
                        Box(modifier = Modifier.weight(2f).height(44.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFF4B66FF)).border(if (tutPhase == 2) 2.dp else 0.dp, if (tutPhase == 2) Color(0xFF3B5BFF) else Color.Transparent, RoundedCornerShape(12.dp)).clickable { if (tutPhase == 2) handleNextPhase() }, contentAlignment = Alignment.Center) { Text("发送", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold) }
                    }
                }
            }

            // Celebration Modal
            androidx.compose.animation.AnimatedVisibility(
                visible = tutPhase == 4,
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.6f)).padding(horizontal = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("👏", fontSize = 120.sp, modifier = Modifier.padding(bottom = 8.dp))
                        Text("哇！你好棒呀", color = Color.White, fontSize = 42.sp, fontWeight = FontWeight.Black, modifier = Modifier.padding(bottom = 48.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(64.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color(0xFF5C73FF))
                                .clickable { onComplete() }
                                .shadow(20.dp, spotColor = Color(0x665C73FF)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("去使用吧", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        } else {
            // Real Chat Keyboard Area
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF2A2A35))
                    .padding(bottom = 24.dp)
            ) {
                // Input Area
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFE5E5EA))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .padding(horizontal = 12.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (inputText.isNotEmpty()) {
                            Text(inputText, color = Color(0xFF333333), fontSize = 15.sp)
                        } else {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("👇 ", fontSize = 16.sp)
                                Text("在键盘粘贴问题后，选择回复方式", color = Color(0xFFA0A5B5), fontSize = 13.sp)
                            }
                        }
                    }
                }

                // Toolbar
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.size(36.dp).clip(CircleShape).background(Color(0xFF414259)), contentAlignment = Alignment.Center) { Text("⌨", color = Color(0xFFE0E0E0), fontSize = 16.sp) }
                    Box(modifier = Modifier.height(32.dp).clip(CircleShape).background(Color(0xFF4B66FF)).padding(horizontal = 14.dp), contentAlignment = Alignment.Center) { Text("帮你回", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold) }
                    Box(modifier = Modifier.height(32.dp).clip(CircleShape).background(Color(0xFF414259)).padding(horizontal = 14.dp), contentAlignment = Alignment.Center) { Text("超会说", color = Color(0xFFE0E0E0), fontSize = 13.sp, fontWeight = FontWeight.Bold) }
                    Spacer(modifier = Modifier.weight(1f))
                    Box(modifier = Modifier.size(32.dp).clip(CircleShape).border(1.dp, Color(0xFF50516A), CircleShape), contentAlignment = Alignment.Center) { Text("Hi", color = Color(0xFFE0E0E0), fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                    Box(modifier = Modifier.size(32.dp).clip(CircleShape).border(1.dp, Color(0xFF50516A), CircleShape), contentAlignment = Alignment.Center) { Text("⊞", color = Color(0xFFE0E0E0), fontSize = 16.sp) }
                    Box(modifier = Modifier.height(26.dp).clip(CircleShape).background(Color(0xFFFFEBEE)).padding(horizontal = 8.dp), contentAlignment = Alignment.Center) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("❤", color = Color(0xFFFF4B6B), fontSize = 10.sp)
                            Text("30%", color = Color(0xFFFF4B6B), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Paste
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier.weight(1f).height(52.dp).clip(RoundedCornerShape(14.dp)).background(Color(0xFF404153)).clickable { if (!clipboardUsed) inputText = "在干嘛？" },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(if (clipboardUsed) "" else "在干嘛？", color = Color(0xFF6E8BFF), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                    Box(
                        modifier = Modifier.width(66.dp).height(52.dp).clip(RoundedCornerShape(14.dp)).background(Color(0xFF4B66FF)).clickable { if (!clipboardUsed) inputText = "在干嘛？" },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("粘贴", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // AI Options Grid
                Column(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Box(modifier = Modifier.weight(1f).height(46.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFF404153)).clickable { handleAppTagClick("高情商") }, contentAlignment = Alignment.Center) { Text("🍬 高情商", color = Color(0xFFE0E0E0), fontSize = 13.5f.sp) }
                        Box(modifier = Modifier.weight(1f).height(46.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFF404153)).clickable { handleAppTagClick("心动狙击") }, contentAlignment = Alignment.Center) { Text("😘 心动狙击", color = Color(0xFFE0E0E0), fontSize = 13.5f.sp) }
                        Box(modifier = Modifier.weight(1f).height(46.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFF404153)).clickable { handleAppTagClick("幽默") }, contentAlignment = Alignment.Center) { Text("😆 幽默", color = Color(0xFFE0E0E0), fontSize = 13.5f.sp) }
                        Box(modifier = Modifier.weight(1f).height(46.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFF404153)).clickable { inputText = "" }, contentAlignment = Alignment.Center) { Text("⌫", color = Color(0xFFE0E0E0), fontSize = 18.sp) }
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Box(modifier = Modifier.weight(1f).height(46.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFF404153)).clickable { handleAppTagClick("暖男") }, contentAlignment = Alignment.Center) { Text("🌞 暖男", color = Color(0xFFE0E0E0), fontSize = 13.5f.sp) }
                        Box(modifier = Modifier.weight(1f).height(46.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFF404153)).clickable { handleAppTagClick("暧昧拉扯") }, contentAlignment = Alignment.Center) { Text("💋 暧昧拉扯", color = Color(0xFFE0E0E0), fontSize = 13.5f.sp) }
                        Box(modifier = Modifier.weight(1f).height(46.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFF404153)).clickable { handleAppTagClick("情场高手") }, contentAlignment = Alignment.Center) { Text("🎀 情场高手", color = Color(0xFFE0E0E0), fontSize = 13.5f.sp) }
                        Box(modifier = Modifier.weight(1f).height(46.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFF404153)).clickable { inputText = "" }, contentAlignment = Alignment.Center) { Text("清空", color = Color(0xFFE0E0E0), fontSize = 13.5f.sp) }
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Box(modifier = Modifier.weight(1f).height(46.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFF404153)).clickable { handleAppTagClick("温柔大叔") }, contentAlignment = Alignment.Center) { Text("👨‍⚖️ 温柔大叔", color = Color(0xFFE0E0E0), fontSize = 13.5f.sp) }
                        Box(modifier = Modifier.weight(1f).height(46.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFF404153)).clickable { handleAppTagClick("风流浪子") }, contentAlignment = Alignment.Center) { Text("🎁 风流浪子", color = Color(0xFFE0E0E0), fontSize = 13.5f.sp) }
                        Box(modifier = Modifier.weight(1f).height(46.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFF404153)).clickable { handleAppTagClick("幽默有梗") }, contentAlignment = Alignment.Center) { Text("😋 幽默有梗", color = Color(0xFFE0E0E0), fontSize = 13.5f.sp) }
                        Box(modifier = Modifier.weight(1f).height(46.dp).clip(RoundedCornerShape(12.dp)).background(if (inputText.isNotEmpty()) Color(0xFF4B66FF) else Color(0xFF404153)).clickable { handleAppSend() }, contentAlignment = Alignment.Center) { Text("发送", color = if (inputText.isNotEmpty()) Color.White else Color(0xFFE0E0E0), fontSize = 14.sp, fontWeight = FontWeight.Bold) }
                    }
                }
            }
        }
    }
}
