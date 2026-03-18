package com.lovekey.clone.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.horizontalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import com.lovekey.clone.data.MockData.PERSONAS_REPLY
import com.lovekey.clone.data.MockData.PERSONAS_TALK

@Composable
fun MainAppShell() {
    var activeTab by remember { mutableStateOf("home") }
    var showLogin by remember { mutableStateOf(false) }
    var showKeyboard by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F5FB))
    ) {
        // Main Content Area
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 75.dp) // Leave space for bottom nav
        ) {
            when (activeTab) {
                "home" -> AppHomeScreen(onShowLogin = { showLogin = true }, onShowKeyboard = { showKeyboard = true })
                "market" -> PersonaMarketScreen(onShowKeyboard = { showKeyboard = true })
                "keyboard" -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("我的键盘模块开发中...", color = Color.Gray) }
            }
        }

        // Bottom Navigation Bar
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(75.dp)
                .background(Color(0xFFF4F5FB))
                .shadow(20.dp, spotColor = Color(0x05000000))
                .padding(bottom = 20.dp, top = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            NavTabButton(active = activeTab == "home", label = "首页", icon = "💬", onClick = { activeTab = "home" })
            NavTabButton(active = activeTab == "market", label = "人设市场", icon = "🛒", onClick = { activeTab = "market" })
            NavTabButton(active = activeTab == "keyboard", label = "我的键盘", icon = "⌨", onClick = { activeTab = "keyboard" })
        }

        // Modals
        if (showLogin) LoginModal(onClose = { showLogin = false })
        if (showKeyboard) KeyboardSwitchModal(onClose = { showKeyboard = false })
    }
}

@Composable
fun NavTabButton(active: Boolean, label: String, icon: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(if (active) 48.dp else 32.dp)
                .offset(y = if (active) (-12).dp else 0.dp)
                .clip(RoundedCornerShape(if (active) 16.dp else 8.dp))
                .then(if (active) Modifier.background(Brush.linearGradient(colors = listOf(Color.White, Color(0xFFE2E6F2)))) else Modifier.background(Color.Transparent))
                .border(if (active) 1.dp else 0.dp, if (active) Color.White else Color.Transparent, RoundedCornerShape(16.dp))
                .shadow(if (active) 12.dp else 0.dp, spotColor = Color(0x99A0AFD2)),
            contentAlignment = Alignment.Center
        ) {
            Text(icon, fontSize = if (active) 24.sp else 20.sp)
        }
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = if (active) Color(0xFF5C73FF) else Color(0xFF999999),
            modifier = Modifier.offset(y = if (active) (-8).dp else 4.dp)
        )
    }
}

@Composable
fun AppHomeScreen(onShowLogin: () -> Unit, onShowKeyboard: () -> Unit) {
    var timeLeft by remember { mutableIntStateOf(11 * 3600 + 59 * 45) }
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        while (timeLeft > 0) {
            delay(1000)
            timeLeft--
        }
    }

    val formatTime = { seconds: Int ->
        val h = (seconds / 3600).toString().padStart(2, '0')
        val m = ((seconds % 3600) / 60).toString().padStart(2, '0')
        val s = (seconds % 60).toString().padStart(2, '0')
        "$h:$m:$s.8"
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxWidth().height(550.dp).background(Brush.verticalGradient(listOf(Color(0xFFE2E6FF), Color(0xFFEEF0FA), Color(0xFFF4F5FB)))))

        Column(modifier = Modifier.fillMaxSize().verticalScroll(scrollState).padding(bottom = 120.dp)) {
            // Header
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) {
                Row(modifier = Modifier.clip(CircleShape).background(Brush.horizontalGradient(listOf(Color(0xFFFF6B22), Color(0xFFFF4500)))).padding(horizontal = 14.dp, vertical = 6.dp).shadow(4.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("L+", color = Color(0xFFFF4500), fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(Color(0xFFFFE066)).padding(horizontal = 4.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("立减 90", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Box(modifier = Modifier.size(36.dp).clip(CircleShape).background(Color.White).clickable { onShowLogin() }.shadow(2.dp), contentAlignment = Alignment.Center) {
                    Text("👤", fontSize = 18.sp)
                }
            }

            // Big Heart Area
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
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 20.dp).clip(RoundedCornerShape(24.dp)).background(Color.White).padding(16.dp).shadow(2.dp, spotColor = Color(0x05000000)), verticalAlignment = Alignment.CenterVertically) {
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

            // App settings cards, mock implementation
            Spacer(modifier = Modifier.height(200.dp))
        }
    }
}

@Composable
fun PersonaMarketScreen(onShowKeyboard: () -> Unit) {
    var subTab by remember { mutableStateOf("reply") }
    var category by remember { mutableStateOf("rank") }

    val activePersonas = if (subTab == "reply") PERSONAS_REPLY else PERSONAS_TALK

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF4F5FB))) {
        // Header
        Row(modifier = Modifier.fillMaxWidth().padding(top = 48.dp, start = 20.dp, end = 20.dp, bottom = 8.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("人设市场", fontSize = 22.sp, fontWeight = FontWeight.Black, color = Color(0xFF1A1A1A))
            Box(modifier = Modifier.clip(CircleShape).background(Color.White).border(1.dp, Color.White, CircleShape).padding(horizontal = 14.dp, vertical = 6.dp).shadow(2.dp)) {
                Text("⌨ 我的键盘", color = Color(0xFF555555), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        // Tabs
        Row(modifier = Modifier.fillMaxWidth().height(50.dp).padding(horizontal = 16.dp)) {
            Box(modifier = Modifier.weight(1f).fillMaxHeight().clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp)).background(if (subTab == "reply") Color.White else Color.Transparent).clickable { subTab = "reply" }, contentAlignment = Alignment.Center) {
                Text("帮你回", color = if (subTab == "reply") Color(0xFF1A1A1A) else Color(0xFFA0A5B5), fontSize = 19.sp, fontWeight = FontWeight.Black)
            }
            Box(modifier = Modifier.weight(1f).fillMaxHeight().clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp)).background(if (subTab == "talk") Color.White else Color.Transparent).clickable { subTab = "talk" }, contentAlignment = Alignment.Center) {
                Text("超会说", color = if (subTab == "talk") Color(0xFF1A1A1A) else Color(0xFFA0A5B5), fontSize = 19.sp, fontWeight = FontWeight.Black)
            }
        }

        // List
        LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f).background(Color.White).padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item { Spacer(modifier = Modifier.height(16.dp)) }
            items(activePersonas.size) { index ->
                val p = activePersonas[index]
                Row(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(24.dp)).background(Color.White).border(1.dp, Color(0xFFF3F4F6), RoundedCornerShape(24.dp)).padding(16.dp).shadow(4.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(64.dp).clip(CircleShape).background(Color(0xFFF4F5FB)), contentAlignment = Alignment.Center) { Text("👤", fontSize = 32.sp) }
                    Column(modifier = Modifier.weight(1f).padding(horizontal = 16.dp)) {
                        Text(p.title, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A1A))
                        Text(p.desc, fontSize = 13.sp, color = Color(0xFF999999), modifier = Modifier.padding(top = 4.dp))
                    }
                    Box(modifier = Modifier.size(32.dp).clip(CircleShape).background(if (p.added) Color(0xFFF4F5FB) else Color(0xFF5C73FF)), contentAlignment = Alignment.Center) {
                        Text(if (p.added) "✓" else "+", color = if (p.added) Color(0xFF333333) else Color.White, fontSize = if (p.added) 16.sp else 20.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(120.dp)) }
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
