package com.lovekey.clone.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.ui.text.TextStyle
import com.lovekey.clone.data.PERSONAS_REPLY
import com.lovekey.clone.data.PERSONAS_TALK
import com.lovekey.clone.data.PersonaData

data class CategoryItem(
    val id: String,
    val label: String,
    val special: String = "normal"
)

val CATEGORIES_REPLY = listOf(
    CategoryItem("rank", "本周排行", "gold"),
    CategoryItem("新上架", "新上架", "pink"),
    CategoryItem("春节嘴替", "春节嘴替", "red"),
    CategoryItem("心动情人节", "心动情人节"),
    CategoryItem("日常必备", "日常必备"),
    CategoryItem("感情升温", "感情升温"),
    CategoryItem("深夜热聊", "深夜热聊"),
    CategoryItem("“嘴毒”王者", "“嘴毒”王者"),
    CategoryItem("个人设", "个人设"),
    CategoryItem("校园恋习生", "校园恋习生"),
    CategoryItem("趣味扮演", "趣味扮演"),
    CategoryItem("特色方言", "特色方言"),
    CategoryItem("十二星座", "十二星座"),
    CategoryItem("MBTI", "MBTI"),
    CategoryItem("玩转职场", "玩转职场"),
    CategoryItem("恋爱零距离", "恋爱零距离")
)

val CATEGORIES_TALK = listOf(
    CategoryItem("all", "全部", "orange"),
    CategoryItem("新上架", "新上架", "pink"),
    CategoryItem("春节嘴替", "春节嘴替", "normal"),
    CategoryItem("聊天必备", "聊天必备", "normal"),
    CategoryItem("感情升温", "感情升温", "normal"),
    CategoryItem("嘴毒王者", "嘴毒王者", "normal"),
    CategoryItem("搞怪逗趣", "搞怪逗趣", "normal"),
    CategoryItem("朋友圈", "朋友圈", "normal"),
    CategoryItem("玩转职场", "玩转职场", "normal")
)

@Composable
fun MarketCategoryTag(cat: CategoryItem, active: Boolean, onClick: () -> Unit) {
    val scale = if (active) 1.03f else 1f
    val modifier = Modifier
        .padding(horizontal = 4.dp)
        .clip(CircleShape)
        .clickable(onClick = onClick)

    when (cat.special) {
        "gold" -> {
            Box(
                modifier = modifier
                    .background(Brush.horizontalGradient(listOf(Color(0xFFFFF4D6), Color(0xFFFFE0A3))))
                    .border(1.5.dp, Color(0xFFFFD980), CircleShape)
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text("🌿 ${cat.label} 🌿", color = Color(0xFFA66E00), fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
        }
        "pink" -> {
            Box(
                modifier = modifier
                    .background(Color(0xFFFFF0F5))
                    .border(1.5.dp, Color(0xFFFFD1E3), CircleShape)
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("New", fontStyle = FontStyle.Italic, fontWeight = FontWeight.Black, color = Color(0xFFFF85E3), fontSize = 13.sp, modifier = Modifier.padding(end = 4.dp))
                    Text(cat.label, color = Color(0xFFFF4B8B), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
        "red" -> {
            Box(
                modifier = modifier
                    .background(Color(0xFFFFF0F0))
                    .border(1.5.dp, Color(0xFFFFD6D6), CircleShape)
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text("🧧 ${cat.label} 🧧", color = Color(0xFFE02020), fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
        }
        "orange" -> {
            Box(
                modifier = modifier
                    .background(Color(0xFFFFA033))
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Text(cat.label, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
        }
        else -> {
            Box(
                modifier = modifier
                    .background(if (active) Color(0xFFF9FAFB) else Color.White)
                    .border(1.5.dp, if (active) Color(0xFF5C73FF) else Color(0xFFF3F4F6), CircleShape)
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text(cat.label, color = if (active) Color(0xFF5C73FF) else Color(0xFF555555), fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun RankBadge(rank: Int?) {
    if (rank == null) return
    val isTop1 = rank == 1
    val isTop2 = rank == 2
    val isTop3 = rank == 3

    if (rank <= 3) {
        val bgGradient = when {
            isTop1 -> listOf(Color(0xFFFFEDA6), Color(0xFFFFC94D))
            isTop2 -> listOf(Color(0xFFE1ECFF), Color(0xFFA3C4FF))
            else -> listOf(Color(0xFFFFDEC2), Color(0xFFFFA673))
        }
        val textColor = when {
            isTop1 -> Color(0xFF8A5A00)
            isTop2 -> Color(0xFF365A96)
            else -> Color(0xFF8A4613)
        }

        val ribbonShape = GenericShape { size, _ ->
            moveTo(0f, 0f)
            lineTo(size.width, 0f)
            lineTo(size.width, size.height * 0.75f)
            lineTo(size.width / 2f, size.height)
            lineTo(0f, size.height * 0.75f)
            close()
        }

        Box(
            modifier = Modifier
                .width(28.dp)
                .height(36.dp)
                .shadow(2.dp, ribbonShape, spotColor = Color(0x26000000))
                .clip(ribbonShape)
                .background(Brush.verticalGradient(bgGradient)),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(top = 3.dp)) {
                Text("TOP", fontSize = 8.sp, fontWeight = FontWeight.Black, color = textColor, lineHeight = 8.sp, letterSpacing = (-0.5).sp)
                Text(rank.toString(), fontSize = 15.sp, fontWeight = FontWeight.Black, color = textColor, lineHeight = 15.sp)
            }
        }
    } else {
        Text(rank.toString(), fontSize = 20.sp, fontStyle = FontStyle.Italic, fontWeight = FontWeight.Black, color = Color(0xFF0F172A), modifier = Modifier.width(28.dp), textAlign = androidx.compose.ui.text.style.TextAlign.Center)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PersonaMarketScreen(onShowKeyboard: () -> Unit) {
    var subTab by remember { mutableStateOf("reply") } // "reply" | "talk"
    var category by remember { mutableStateOf("rank") }
    var showCategorySheet by remember { mutableStateOf(false) }

    val activePersonas = if (subTab == "reply") PERSONAS_REPLY else PERSONAS_TALK
    val activeCategories = if (subTab == "reply") CATEGORIES_REPLY else CATEGORIES_TALK

    val addedIds = remember { mutableStateListOf<String>().apply {
        addAll((PERSONAS_REPLY + PERSONAS_TALK).filter { it.added }.map { it.id })
    }}

    fun toggleAdd(id: String) {
        if (addedIds.contains(id)) addedIds.remove(id) else addedIds.add(id)
    }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF4F5FB))) {
        // Top gradient background
        Box(modifier = Modifier.fillMaxWidth().height(280.dp).background(
            Brush.verticalGradient(listOf(Color(0xFFE5EBFF), Color(0xFFF4F5FB)))
        ))

        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 48.dp, start = 20.dp, end = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("人设市场", fontSize = 22.sp, fontWeight = FontWeight.Black, color = Color(0xFF1A1A1A))
                Box(modifier = Modifier.clip(CircleShape).background(Color(0xE6FFFFFF)).border(1.dp, Color.White, CircleShape).clickable { onShowKeyboard() }.padding(horizontal = 14.dp, vertical = 6.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("⌨ ", fontSize = 12.sp)
                        Text("我的键盘", color = Color(0xFF555555), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Tabs
            Row(modifier = Modifier.fillMaxWidth().height(50.dp).padding(horizontal = 16.dp).padding(top = 16.dp)) {
                Box(
                    modifier = Modifier.weight(1f).fillMaxHeight().clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp)).background(if (subTab == "reply") Color.White else Color.Transparent).clickable { subTab = "reply" },
                    contentAlignment = Alignment.Center
                ) {
                    Text("帮你回", color = if (subTab == "reply") Color(0xFF1A1A1A) else Color(0xFFA0A5B5), fontSize = 19.sp, fontWeight = FontWeight.Black)
                    if (subTab == "reply") {
                        Box(modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 6.dp).width(24.dp).height(3.5.dp).clip(CircleShape).background(Color(0xFFFFD233)))
                    }
                }
                Box(
                    modifier = Modifier.weight(1f).fillMaxHeight().clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp)).background(if (subTab == "talk") Color.White else Color.Transparent).clickable { subTab = "talk" },
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.Top) {
                        Text("超会说", color = if (subTab == "talk") Color(0xFF1A1A1A) else Color(0xFFA0A5B5), fontSize = 19.sp, fontWeight = FontWeight.Black)
                        Text("✨", fontSize = 10.sp, modifier = Modifier.padding(start = 2.dp, top = 2.dp))
                    }
                    if (subTab == "talk") {
                        Box(modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 6.dp).width(24.dp).height(3.5.dp).clip(CircleShape).background(Color(0xFFFFD233)))
                    }
                }
            }

            // Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .height(90.dp)
                    .shadow(1.dp, RoundedCornerShape(20.dp))
                    .clip(RoundedCornerShape(20.dp))
                    .background(Brush.horizontalGradient(listOf(Color(0xFFFFD1ED), Color(0xFFFFE8F3), Color(0xFFFFD1ED))))
            ) {
                // Background Pattern (simplified)
                Box(modifier = Modifier.fillMaxHeight().fillMaxWidth(0.5f).align(Alignment.CenterEnd).background(Color.White.copy(alpha = 0.2f)))

                // Tag
                Box(modifier = Modifier.align(Alignment.TopStart).background(Color(0xFFC88B4B), RoundedCornerShape(bottomEnd = 10.dp)).padding(horizontal = 8.dp, vertical = 2.dp)) {
                    Text("热门推荐", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }

                // Title
                Column(modifier = Modifier.align(Alignment.CenterStart).padding(start = 20.dp, top = 8.dp)) {
                    Text("键盘轻松应对", color = Color(0xFF1A1A1A), fontSize = 20.sp, fontStyle = FontStyle.Italic, fontWeight = FontWeight.Black, modifier = Modifier.rotate(-2f))
                    Text("各种场景", color = Color(0xFF1A1A1A), fontSize = 20.sp, fontStyle = FontStyle.Italic, fontWeight = FontWeight.Black, modifier = Modifier.rotate(-2f))
                }

                // Decorative cards
                Box(modifier = Modifier.align(Alignment.CenterEnd).padding(end = 12.dp).fillMaxHeight(), contentAlignment = Alignment.Center) {
                    Row {
                        Box(
                            modifier = Modifier
                                .size(50.dp, 60.dp)
                                .rotate(-10f)
                                .shadow(2.dp, RoundedCornerShape(14.dp))
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFFFFB6C1))
                                .border(2.dp, Color.White, RoundedCornerShape(14.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("👩", fontSize = 28.sp)
                            Box(modifier = Modifier.align(Alignment.BottomEnd).offset(x = 4.dp, y = 4.dp).background(Color(0xFFE8A5FF), RoundedCornerShape(2.dp)).padding(horizontal = 6.dp, vertical = 2.dp).rotate(10f)) {
                                Text("脱单局", color = Color.White, fontSize = 8.sp)
                            }
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Box(
                            modifier = Modifier
                                .size(50.dp, 60.dp)
                                .rotate(10f)
                                .offset(y = 12.dp)
                                .shadow(2.dp, RoundedCornerShape(14.dp))
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFFB0C4DE))
                                .border(2.dp, Color.White, RoundedCornerShape(14.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("👦", fontSize = 28.sp)
                            Box(modifier = Modifier.align(Alignment.TopStart).offset(x = (-4).dp, y = (-4).dp).background(Color(0xFFB5BFFF), RoundedCornerShape(2.dp)).padding(horizontal = 6.dp, vertical = 2.dp).rotate(-10f)) {
                                Text("社交局", color = Color.White, fontSize = 8.sp)
                            }
                        }
                    }
                }
            }

            // Categories
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                Row(modifier = Modifier.weight(1f).horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    activeCategories.forEach { cat ->
                        MarketCategoryTag(cat = cat, active = category == cat.id, onClick = { category = cat.id })
                    }
                }
                Text(" ≡ ", color = Color(0xFFA0A5B5), fontSize = 22.sp, modifier = Modifier.padding(start = 8.dp).clickable { showCategorySheet = true })
            }

            // List
            if (subTab == "reply") {
                LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f).padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(activePersonas) { p ->
                        val isAdded = addedIds.contains(p.id)
                        Row(
                            modifier = Modifier.fillMaxWidth().shadow(2.dp, RoundedCornerShape(24.dp), spotColor = Color(0x08000000)).clip(RoundedCornerShape(24.dp)).background(Color.White).clickable { toggleAdd(p.id) }.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Avatar
                            Box(modifier = Modifier.size(84.dp, 64.dp), contentAlignment = Alignment.CenterStart) {
                                Box(modifier = Modifier.size(64.dp).align(Alignment.CenterEnd).shadow(4.dp, CircleShape).clip(CircleShape).border(2.dp, Color.White, CircleShape).background(Color(0xFFF4F5FB))) {
                                    Text("👤", fontSize = 32.sp, modifier = Modifier.align(Alignment.Center))
                                }
                                if (p.rank != null) {
                                    Box(modifier = Modifier.align(Alignment.CenterStart)) {
                                        RankBadge(rank = p.rank)
                                    }
                                }
                            }

                            // Info
                            Column(modifier = Modifier.weight(1f).padding(horizontal = 12.dp)) {
                                Text(p.title, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A1A), maxLines = 1, overflow = TextOverflow.Ellipsis)
                                Text(p.desc, fontSize = 13.sp, color = Color(0xFF999999), maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.padding(vertical = 4.dp))
                                Box(modifier = Modifier.clip(RoundedCornerShape(6.dp)).background(if (p.id == "r2") Color(0xFFFFF6E5) else Color(0xFFF4F6F9)).padding(horizontal = 6.dp, vertical = 2.dp)) {
                                    Text("${p.usage ?: "100w"}人在用", fontSize = 11.sp, fontWeight = FontWeight.Medium, color = if (p.id == "r2") Color(0xFFB28200) else Color(0xFF999999))
                                }
                            }

                            // Button
                            Box(
                                modifier = Modifier.size(32.dp).clip(CircleShape).background(if (isAdded) Color(0xFFF4F5FB) else Color(0xFF5C73FF)).clickable { toggleAdd(p.id) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(if (isAdded) "✓" else "+", color = if (isAdded) Color(0xFF333333) else Color.White, fontSize = if (isAdded) 16.sp else 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.offset(y = if(isAdded) 0.dp else (-1).dp))
                            }
                        }
                    }
                    item { Spacer(modifier = Modifier.height(120.dp)) }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxWidth().weight(1f).padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(activePersonas.size) { index ->
                        val p = activePersonas[index]
                        val isAdded = addedIds.contains(p.id)
                        Box(
                            modifier = Modifier.height(175.dp).shadow(2.dp, RoundedCornerShape(24.dp), spotColor = Color(0x08000000)).clip(RoundedCornerShape(24.dp)).background(Color.White).clickable { toggleAdd(p.id) }.padding(16.dp)
                        ) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Box(modifier = Modifier.wrapContentSize()) {
                                    androidx.compose.foundation.Canvas(modifier = Modifier.matchParentSize().offset(y = 12.dp)) {
                                        val wavePath = androidx.compose.ui.graphics.Path().apply {
                                            moveTo(0f, size.height)
                                            quadraticBezierTo(size.width * 0.12f, size.height - 4.dp.toPx(), size.width * 0.25f, size.height)
                                            quadraticBezierTo(size.width * 0.5f, size.height, size.width * 0.75f, size.height)
                                        }
                                        drawPath(wavePath, color = Color(0xFFFFD233), style = androidx.compose.ui.graphics.drawscope.Stroke(width = 3.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round))
                                    }
                                    Text(p.title, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A1A), modifier = Modifier.padding(bottom = 2.dp))
                                }
                                Text(p.desc, fontSize = 13.sp, color = Color(0xFF888888), modifier = Modifier.padding(top = 10.dp).fillMaxWidth(0.7f), lineHeight = 18.sp)
                            }
                            Text("“", fontSize = 80.sp, color = Color(0xFFF4F5FB), fontFamily = androidx.compose.ui.text.font.FontFamily.Serif, fontWeight = FontWeight.Black, modifier = Modifier.align(Alignment.BottomStart).offset(x = 0.dp, y = 12.dp))

                            Box(modifier = Modifier.align(Alignment.TopEnd).offset(x = 4.dp, y = 32.dp).size(52.dp).clip(RoundedCornerShape(16.dp)).background(Color(0xFFF4F5FB)), contentAlignment = Alignment.Center) {
                                Text("👤", fontSize = 28.sp)
                            }

                            Box(
                                modifier = Modifier.align(Alignment.BottomEnd).size(28.dp).clip(CircleShape).background(if (isAdded) Color(0xFFF4F5FB) else Color(0xFFFFA033)).clickable { toggleAdd(p.id) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(if (isAdded) "✓" else "+", color = if (isAdded) Color(0xFF333333) else Color.White, fontSize = if (isAdded) 14.sp else 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.offset(y = if(isAdded) 0.dp else (-1).dp))
                            }
                        }
                    }
                    item { Spacer(modifier = Modifier.height(120.dp)) }
                    item { Spacer(modifier = Modifier.height(120.dp)) }
                }
            }
        }

        // Category Sheet
        if (showCategorySheet) {
            Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha=0.5f)).clickable { showCategorySheet = false }, contentAlignment = Alignment.BottomCenter) {
                Column(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)).background(Color(0xFFF4F5FB)).padding(16.dp).padding(bottom = 48.dp).clickable(enabled = false) {}) {
                    Box(modifier = Modifier.width(40.dp).height(6.dp).clip(CircleShape).background(Color(0xFFD1D5DB)).align(Alignment.CenterHorizontally))

                    Row(modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("全部标签", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A1A))
                        Text("✕", fontSize = 20.sp, color = Color(0xFF999999), modifier = Modifier.clickable { showCategorySheet = false })
                    }

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        activeCategories.forEach { cat ->
                            MarketCategoryTag(cat = cat, active = category == cat.id, onClick = {
                                category = cat.id
                                showCategorySheet = false
                            })
                        }
                    }
                }
            }
        }
    }
}
