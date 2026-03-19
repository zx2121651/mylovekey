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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import com.lovekey.clone.data.PERSONAS_REPLY
import com.lovekey.clone.data.PERSONAS_TALK
import com.lovekey.clone.data.PersonaData


    @OptIn(ExperimentalLayoutApi::class)
@Composable
fun PersonaMarketScreen(onShowKeyboard: () -> Unit) {
    var subTab by remember { mutableStateOf("reply") } // "reply" | "talk"
    var category by remember { mutableStateOf("rank") }
    var showCategorySheet by remember { mutableStateOf(false) }

    val activePersonas = if (subTab == "reply") PERSONAS_REPLY else PERSONAS_TALK

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

            // Categories
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                Row(modifier = Modifier.weight(1f).horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    val cats = if (subTab == "reply") listOf("本周排行", "新上架", "春节嘴替", "心动情人节", "日常必备", "感情升温") else listOf("全部", "新上架", "春节嘴替", "聊天必备", "感情升温")
                    cats.forEach { cat ->
                        val isSelected = category == cat || (category == "rank" && cat == "本周排行") || (category == "all" && cat == "全部")
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(if (isSelected) Color.White else Color.White.copy(alpha=0.7f))
                                .border(1.5.dp, if (isSelected) Color(0xFF5C73FF) else Color(0xFFF3F4F6), CircleShape)
                                .clickable { category = if (cat == "本周排行") "rank" else if (cat == "全部") "all" else cat }
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text(cat, color = if (isSelected) Color(0xFF5C73FF) else Color(0xFF555555), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
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
                            Box(modifier = Modifier.size(64.dp), contentAlignment = Alignment.Center) {
                                Box(modifier = Modifier.size(60.dp).align(Alignment.CenterEnd).clip(CircleShape).border(2.dp, Color.White, CircleShape).background(Color(0xFFF4F5FB))) {
                                    Text("👤", fontSize = 32.sp, modifier = Modifier.align(Alignment.Center))
                                }
                                if (p.rank != null) {
                                    Box(modifier = Modifier.align(Alignment.CenterStart).offset(x = (-4).dp)) {
                                        Text(p.rank.toString(), fontSize = 24.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
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
                            modifier = Modifier.height(175.dp).shadow(2.dp, RoundedCornerShape(24.dp), spotColor = Color(0x08000000)).clip(RoundedCornerShape(24.dp)).background(Color.White).clickable { toggleAdd(p.id) }.padding(12.dp)
                        ) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Text(p.title, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A1A))
                                Text(p.desc, fontSize = 13.sp, color = Color(0xFF888888), modifier = Modifier.padding(top = 12.dp).fillMaxWidth(0.7f), lineHeight = 18.sp)
                            }
                            Text("“", fontSize = 80.sp, color = Color(0xFFF4F5FB), fontWeight = FontWeight.Black, modifier = Modifier.align(Alignment.BottomStart).offset(x = 4.dp, y = 10.dp))

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
                        val tags = if (subTab == "reply") listOf("本周排行", "New 新上架", "春节嘴替 🥳", "心动情人节", "日常必备", "感情升温", "深夜热聊", "“嘴毒”王者", "个人人设", "校园恋习生", "趣味扮演", "特色方言", "十二星座", "MBTI", "玩转职场", "恋爱零距离") else listOf("全部", "New 新上架", "春节嘴替 🥳", "聊天必备", "感情升温", "深夜热聊")
                        tags.forEach { tag ->
                            val isSelected = category == tag || (category == "rank" && tag == "本周排行") || (category == "all" && tag == "全部")
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) Color(0xFFE8F0FF) else Color.White)
                                    .border(1.dp, if (isSelected) Color(0xFF5C73FF) else Color.Transparent, RoundedCornerShape(8.dp))
                                    .clickable {
                                        category = if (tag == "本周排行") "rank" else if (tag == "全部") "all" else tag
                                        showCategorySheet = false
                                    }
                                    .padding(horizontal = 16.dp, vertical = 10.dp)
                            ) {
                                Text(tag, color = if (isSelected) Color(0xFF5C73FF) else Color(0xFF1A1A1A), fontSize = 14.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                            }
                        }
                    }
                }
            }
        }
    }
}
