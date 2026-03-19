package com.lovekey.clone.ime

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun RealKeyboardUI(
    state: KeyboardState,
    onKeyPress: (String) -> Unit,
    onDelete: () -> Unit,
    onEnter: () -> Unit,
    onAiAction: (String) -> Unit,
    onSwitchMode: (KeyboardMode) -> Unit,
    onToggleShift: () -> Unit,
    onToggleTraditional: () -> Unit,
    onCandidateSelect: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(if (state.composingText.isNotEmpty()) 330.dp else 290.dp)
            .background(Color(0xFFE2E6EF))
    ) {
        // --- Candidates Strip (only show when composing pinyin) ---
        if (state.composingText.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth().height(40.dp).background(Color.White).padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(state.composingText, color = Color(0xFF5C73FF), fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(end = 8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(state.candidates) { cand ->
                        val displayCand = if (state.isTraditional) ChineseUtils.convertToTraditional(cand) else cand
                        Text(
                            text = displayCand,
                            color = Color(0xFF1A1A1A),
                            fontSize = 16.sp,
                            modifier = Modifier.clickable { onCandidateSelect(cand) }.padding(vertical = 8.dp)
                        )
                    }
                }
            }
        }

        // --- 1. AI Toolbar Top ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp)
                .height(40.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(36.dp).clip(CircleShape).background(Color(0xFFFFFFFF)), contentAlignment = Alignment.Center) { Text("⌨", color = Color(0xFF5C73FF), fontSize = 16.sp) }
            Box(modifier = Modifier.height(32.dp).clip(CircleShape).background(Color(0xFF4B66FF)).padding(horizontal = 14.dp).clickable { /* toggle something */ }, contentAlignment = Alignment.Center) { Text("帮你回", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold) }
            Box(modifier = Modifier.height(32.dp).clip(CircleShape).background(Color.White).padding(horizontal = 14.dp), contentAlignment = Alignment.Center) { Text("超会说", color = Color(0xFF555555), fontSize = 13.sp, fontWeight = FontWeight.Bold) }
            Spacer(modifier = Modifier.weight(1f))
            Box(modifier = Modifier.size(32.dp).clip(CircleShape).border(1.dp, Color(0xFFD3D8E6), CircleShape), contentAlignment = Alignment.Center) { Text("Hi", color = Color(0xFF888888), fontSize = 12.sp, fontWeight = FontWeight.Bold) }
            Box(modifier = Modifier.size(32.dp).clip(CircleShape).border(1.dp, Color(0xFFD3D8E6), CircleShape), contentAlignment = Alignment.Center) { Text("⊞", color = Color(0xFF888888), fontSize = 16.sp) }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // --- 2. Keyboard Panels ---
        if (state.mode == KeyboardMode.T9_PINYIN) {
            T9KeyboardLayout(
                onKeyPress = onKeyPress,
                onDelete = onDelete,
                onEnter = onEnter,
                onSwitchMode = onSwitchMode
            )
        } else {
            QWERTYKeyboardLayout(
                mode = state.mode,
                isShifted = state.isShifted,
                onKeyPress = onKeyPress,
                onDelete = onDelete,
                onEnter = onEnter,
                onSwitchMode = onSwitchMode,
                onToggleShift = onToggleShift,
                onToggleTraditional = onToggleTraditional,
                isTraditional = state.isTraditional
            )
        }
    }
}
