package com.lovekey.clone.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun MainAppShell() {
    var activeTab by remember { mutableStateOf("home") }
    var showLogin by remember { mutableStateOf(false) }
    var showKeyboard by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF4F5FB))) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.weight(1f)) {
                when (activeTab) {
                    "home" -> AppHomeScreen(onShowLogin = { showLogin = true }, onShowKeyboard = { showKeyboard = true })
                    "market" -> PersonaMarketScreen(onShowKeyboard = { showKeyboard = true })
                    "keyboard" -> MyKeyboardScreen()
                }
            }

            // Bottom Nav
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(75.dp)
                    .background(Color(0xFFF4F5FB))
                    .shadow(20.dp, spotColor = Color(0x05000000))
                .padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                NavTabButton("首页", "💬", activeTab == "home") { activeTab = "home" }
                NavTabButton("人设市场", "🌟", activeTab == "market") { activeTab = "market" }
                NavTabButton("我的键盘", "⌨", activeTab == "keyboard") { activeTab = "keyboard" }
            }
        }

        if (showLogin) LoginModal(onClose = { showLogin = false })
        if (showKeyboard) KeyboardSwitchModal(onClose = { showKeyboard = false })
    }
}

@Composable
fun RowScope.NavTabButton(label: String, icon: String, active: Boolean, onClick: () -> Unit) {
    Column(
        modifier = Modifier.weight(1f).clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val color = if (active) Color(0xFF5C73FF) else Color(0xFF999999)
        if (active) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Brush.linearGradient(listOf(Color.White, Color(0xFFE2E6F2))))
                    .border(1.dp, Color.White.copy(alpha=0.8f), RoundedCornerShape(16.dp))
                    .shadow(16.dp, spotColor = Color(0x99A0AFD2)),
                contentAlignment = Alignment.Center
            ) {
                Text(icon, fontSize = 24.sp, color = color)
            }
        } else {
            Text(icon, fontSize = 24.sp, color = color, modifier = Modifier.padding(bottom = 4.dp))
        }
        Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = color)
    }
}

@Composable
fun AppHomeScreen(onShowLogin: () -> Unit, onShowKeyboard: () -> Unit) {
    var timeLeft by remember { mutableStateOf(11 * 3600 + 59 * 45) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            if (timeLeft > 0) timeLeft--
        }
    }

    fun formatTime(seconds: Int): String {
        val h = (seconds / 3600).toString().padStart(2, '0')
        val m = ((seconds % 3600) / 60).toString().padStart(2, '0')
        val s = (seconds % 60).toString().padStart(2, '0')
        return "$h:$m:$s.8"
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxWidth().height(550.dp).background(
            Brush.verticalGradient(listOf(Color(0xFFE2E6FF), Color(0xFFEEF0FA), Color(0xFFF4F5FB)))
        ))

        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
            // Header
            Row(modifier = Modifier.fillMaxWidth().height(60.dp).padding(horizontal = 20.dp).padding(top = 8.dp), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.clip(CircleShape).background(Brush.horizontalGradient(listOf(Color(0xFFFF6B22), Color(0xFFFF4500)))).padding(horizontal = 14.dp, vertical = 6.dp)) {
                        Text("立减 90", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Box(modifier = Modifier.size(36.dp).clip(CircleShape).background(Color.White).border(1.dp, Color(0xFFF3F4F6), CircleShape).clickable { onShowLogin() }.shadow(2.dp), contentAlignment = Alignment.Center) {
                        Text("👤", fontSize = 18.sp)
                    }
                }
            }

            // Main Intimacy Area
            Column(modifier = Modifier.fillMaxWidth().padding(vertical = 40.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Box(modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(Color(0xFFB3CAFF)).padding(horizontal = 14.dp, vertical = 6.dp)) {
                    Text("推荐亲密度", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
                Row(verticalAlignment = Alignment.Bottom) {
                    Text("30", fontSize = 80.sp, fontWeight = FontWeight.Black, color = Color(0xFF1A1A1A))
                    Text("%", fontSize = 54.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A1A), modifier = Modifier.padding(bottom = 12.dp))
                }
                Text("聊天亲密度", color = Color(0xFF666666), fontSize = 15.sp, letterSpacing = 2.sp)

                // Mock 3D Heart Graphic
                Box(modifier = Modifier.size(280.dp).padding(vertical = 20.dp), contentAlignment = Alignment.Center) {
                    Box(modifier = Modifier.size(160.dp).clip(CircleShape).background(Color(0xFFFF4B6B).copy(alpha=0.3f)))
                    Text("❤", fontSize = 140.sp, color = Color(0xFFFF4B6B))
                }

                Box(modifier = Modifier.fillMaxWidth(0.85f).height(58.dp).clip(CircleShape).background(Color(0xFF5C73FF)).shadow(20.dp, spotColor = Color(0x405C73FF)), contentAlignment = Alignment.Center) {
                    Text("启用Lovekey键盘", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                }
            }

            // Upsell Banner
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 20.dp).shadow(2.dp, RoundedCornerShape(24.dp), spotColor = Color(0x05000000)).border(1.dp, Color(0xFFF9FAFB), RoundedCornerShape(24.dp)).clip(RoundedCornerShape(24.dp)).background(Color.White).padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("🎟", fontSize = 32.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("限时优惠活动，买一年送一年 🎉", color = Color(0xFF333333), fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    Row(verticalAlignment = Alignment.Bottom, modifier = Modifier.padding(top = 4.dp)) {
                        Text("仅需98元 ", color = Color(0xFF5C73FF), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text("得24个月会员", color = Color(0xFF333333), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                    Text("${formatTime(timeLeft)} 后优惠失效", color = Color(0xFFFF7A92), fontSize = 11.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(top = 4.dp).clip(RoundedCornerShape(6.dp)).background(Color(0xFFFFF0F3)).padding(horizontal = 8.dp, vertical = 2.dp))
                }
                Box(modifier = Modifier.clip(CircleShape).background(Color(0xFFF4EBFF)).padding(horizontal = 14.dp, vertical = 6.dp)) {
                    Text("一键升级", color = Color(0xFFA855F7), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }

            // App settings cards
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Column(modifier = Modifier.weight(1f).height(125.dp).shadow(2.dp, RoundedCornerShape(24.dp), spotColor = Color(0x05000000)).clip(RoundedCornerShape(24.dp)).background(Color.White).padding(20.dp), verticalArrangement = Arrangement.SpaceBetween) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Box(modifier = Modifier.size(46.dp).clip(RoundedCornerShape(16.dp)).background(Brush.verticalGradient(listOf(Color(0xFFB8CDFF), Color(0xFF6A8EFF)))).border(1.dp, Color.White, RoundedCornerShape(16.dp)))
                        Text("→", color = Color(0xFFD1D5DB), modifier = Modifier.rotate(-45f))
                    }
                    Column {
                        Text("聊天人设市场", color = Color(0xFF222222), fontSize = 17.sp, fontWeight = FontWeight.Bold)
                        Text("添加更多人设", color = Color(0xFF999999), fontSize = 12.sp, modifier = Modifier.padding(top = 2.dp))
                    }
                }
                Column(modifier = Modifier.weight(1f).height(125.dp).shadow(2.dp, RoundedCornerShape(24.dp), spotColor = Color(0x05000000)).clip(RoundedCornerShape(24.dp)).background(Color.White).padding(20.dp), verticalArrangement = Arrangement.SpaceBetween) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Box(modifier = Modifier.size(46.dp).clip(RoundedCornerShape(16.dp)).background(Brush.verticalGradient(listOf(Color(0xFFFFD2C2), Color(0xFFFF8C7A)))).border(1.dp, Color.White, RoundedCornerShape(16.dp)))
                        Text("→", color = Color(0xFFD1D5DB), modifier = Modifier.rotate(-45f))
                    }
                    Column {
                        Text("定制聊天人设", color = Color(0xFF222222), fontSize = 17.sp, fontWeight = FontWeight.Bold)
                        Text("量身打造", color = Color(0xFF999999), fontSize = 12.sp, modifier = Modifier.padding(top = 2.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.height(200.dp))
        }

        // Floating Buy One Get One Free Widget
        Box(modifier = Modifier.align(Alignment.CenterEnd).offset(x = 15.dp, y = 100.dp).size(105.dp).shadow(30.dp, RoundedCornerShape(28.dp), spotColor = Color(0x665C73FF)).clip(RoundedCornerShape(28.dp)).background(Brush.linearGradient(listOf(Color(0xFFE8EEFF), Color(0xFFA0B8FF)))).border(3.dp, Color.White, RoundedCornerShape(28.dp))) {
            Box(modifier = Modifier.align(Alignment.TopEnd).offset(x = (-8).dp, y = 4.dp).rotate(-6f).clip(CircleShape).background(Color(0xFF5C73FF)).padding(horizontal = 6.dp, vertical = 2.dp)) {
                Text("新户专享", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            }
            Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Text("买一月", color = Color.Transparent, fontSize = 18.sp, fontWeight = FontWeight.Black, fontStyle = FontStyle.Italic)
                Text("送一月", color = Color.Transparent, fontSize = 18.sp, fontWeight = FontWeight.Black, fontStyle = FontStyle.Italic)
                Box(modifier = Modifier.padding(top = 6.dp).clip(CircleShape).background(Color(0xFFFF4B6B)).padding(horizontal = 12.dp, vertical = 2.dp)) {
                    Text("59:20", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
            Row(modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().padding(horizontal = 12.dp).height(12.dp), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(modifier = Modifier.weight(1f).fillMaxHeight().background(Color(0xFFFFE03B)))
                Box(modifier = Modifier.weight(1f).fillMaxHeight().background(Color(0xFF00E676)))
                Box(modifier = Modifier.weight(1f).fillMaxHeight().background(Color(0xFFFF4081)))
            }
        }
    }
}

@Composable
fun LoginModal(onClose: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.4f)).clickable { onClose() }, contentAlignment = Alignment.Center) {
        Column(modifier = Modifier.fillMaxWidth(0.85f).clip(RoundedCornerShape(28.dp)).background(Color.White).padding(32.dp).clickable(enabled = false) {}, horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopEnd) { Text("×", fontSize = 24.sp, modifier = Modifier.clickable { onClose() }) }
            Text("登录LOVEKEY\n添加聊天人设到键盘", fontSize = 24.sp, fontWeight = FontWeight.Black, color = Color(0xFF1A1A1A), textAlign = TextAlign.Start, modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp))
            Box(modifier = Modifier.fillMaxWidth().height(52.dp).clip(CircleShape).background(Color(0xFF5C73FF)).shadow(16.dp, spotColor = Color(0x4D5C73FF)), contentAlignment = Alignment.Center) {
                Text("📱 手机号登录", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            Column(modifier = Modifier.padding(vertical = 40.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(Color(0xFFE5E5E5)), contentAlignment = Alignment.Center) { Text("💬", fontSize = 20.sp) }
                Text("微信", color = Color(0xFF999999), fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))
            }
            Text("我已阅读并同意 用户协议、用户隐私协议和号码认证服务隐私协议", color = Color(0xFF999999), fontSize = 11.sp, textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun KeyboardSwitchModal(onClose: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)).clickable { onClose() }, contentAlignment = Alignment.BottomCenter) {
        Column(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)).background(Color(0xFF2C2C2C)).padding(24.dp).clickable(enabled = false) {}) {
            Text("更改键盘", color = Color.White.copy(alpha = 0.9f), fontSize = 17.sp, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 32.dp))
            Row(modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("搜狗输入法定制版", color = Color.White.copy(alpha = 0.7f), fontSize = 16.sp)
                Box(modifier = Modifier.size(22.dp).clip(CircleShape).border(2.dp, Color.Gray, CircleShape))
            }
            Row(modifier = Modifier.fillMaxWidth().clickable { onClose() }, horizontalArrangement = Arrangement.SpaceBetween) {
                Row { Text("👉 ", fontSize = 18.sp); Text("Lovekey键盘", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold) }
                Box(modifier = Modifier.size(22.dp).clip(CircleShape).background(Color(0xFF5C73FF)), contentAlignment = Alignment.Center) { Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color.White)) }
            }
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}
