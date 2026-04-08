package com.lovekey.clone.ime

import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.Crossfade
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback

@Composable
fun RealKeyboardUI(
    state: KeyboardState,
    onKeyPress: (String) -> Unit,
    onDelete: () -> Unit,
    onClear: () -> Unit = {},
    onMoveCursor: (Int) -> Unit = {},
    onEnter: () -> Unit,
    onAiAction: (String) -> Unit,
    onSwitchMode: (KeyboardMode) -> Unit,
    onToggleShift: () -> Unit,
    onToggleTraditional: () -> Unit,
    onCandidateSelect: (String) -> Unit,
    onT9SyllableSelect: (String) -> Unit = {},
    onToggleT9SyllableSelector: () -> Unit = {}
) {
    val theme = state.currentTheme
    val haptic = LocalHapticFeedback.current

    // State to hold the currently pressed key for the popup preview
    var pressedKeyText by remember { mutableStateOf<String?>(null) }
    var pressedKeyPosition by remember { mutableStateOf<Offset?>(null) }

    Box(modifier = Modifier.fillMaxWidth().height(280.dp).background(theme.keyboardBackground)) {
        Column(modifier = Modifier.fillMaxSize()) {
            // --- Top Toolbar ---
            Row(
                modifier = Modifier.fillMaxWidth().height(44.dp).background(theme.toolbarBackground).padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("♥️ LoveKey", color = theme.toolbarIconColor, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("🤖 超会说", color = theme.toolbarIconColor, modifier = Modifier.clickable { onAiAction("超会说") })
                    Text("💡 帮你回", color = theme.toolbarIconColor, modifier = Modifier.clickable { onAiAction("帮你回") })
                    Text("🎨 主题", color = theme.toolbarIconColor, modifier = Modifier.clickable { onAiAction("Themes") })
                }
            }

            // --- Candidates Strip & Context Actions ---
            AnimatedVisibility(
                visible = state.isSyllableSelectorExpanded && state.t9PinyinCombinations.size > 1,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut() // using exit placeholder if needed
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp)
                        .background(theme.candidateStripBackground.copy(alpha = 0.95f))
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(state.t9PinyinCombinations) { syllable ->
                            val isSelected = state.selectedT9Syllable == syllable
                            Text(
                                text = syllable,
                                color = if (isSelected) Color(0xFFFFD980) else theme.candidateTextColor,
                                fontSize = 15.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                modifier = Modifier
                                    .clickable {
                                        try {
                                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                        } catch(e: Exception) {}
                                        onT9SyllableSelect(syllable)
                                    }
                                    .padding(vertical = 4.dp, horizontal = 4.dp)
                            )
                        }
                    }
                }
            }

            // We show this row if there is composing text OR if there is context text (to show the magic button)
            if (state.composingText.isNotEmpty() || state.contextText.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth().height(44.dp).background(theme.candidateStripBackground).padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (state.composingText.isNotEmpty()) {
                        Text(state.composingText, color = theme.accentColor, fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(end = 8.dp))

                        Box(modifier = Modifier.weight(1f)) {
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                items(state.candidates) { cand ->
                                    val displayCand = if (state.isTraditional) ChineseUtils.convertToTraditional(cand) else cand
                                    Text(
                                        text = displayCand,
                                        color = theme.candidateTextColor,
                                        fontSize = 16.sp,
                                        modifier = Modifier.clickable { onCandidateSelect(cand) }.padding(vertical = 8.dp)
                                    )
                                }
                            }
                        }
                        if (state.mode == KeyboardMode.T9_PINYIN && state.t9PinyinCombinations.size > 1) {
                            Icon(
                                imageVector = if (state.isSyllableSelectorExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = "Toggle Syllables",
                                tint = theme.accentColor,
                                modifier = Modifier.padding(horizontal = 4.dp).clickable { onToggleT9SyllableSelector() }
                            )
                        }
                    } else {
                        // If no composing text, consume space so button stays on the right
                        Spacer(modifier = Modifier.weight(1f))
                    }

                    // --- “✨ 换个说法” 悬浮触发器 ---
                    AnimatedVisibility(
                        visible = state.composingText.isNotEmpty() || state.contextText.isNotEmpty(),
                        enter = fadeIn() + slideInHorizontally(initialOffsetX = { it }),
                        exit = fadeOut() + slideOutHorizontally(targetOffsetX = { it })
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFFFF6E5))
                                .border(1.dp, Color(0xFFFFD980), RoundedCornerShape(12.dp))
                                .clickable { onAiAction("换个说法") }
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("✨ 换个说法", color = Color(0xFFA66E00), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // --- Main Content Area ---
            Box(modifier = Modifier.fillMaxSize().weight(1f)) {
                Crossfade(targetState = state.activePanel, label = "panel_fade") { panel ->
                    when (panel) {
                        ActivePanel.KEYBOARD -> {
                            Column {
                                Spacer(modifier = Modifier.height(8.dp))
                                if (state.mode == KeyboardMode.T9_PINYIN) {
                                    T9KeyboardLayout(
                                        theme = theme,
                                        onKeyPress = onKeyPress,
                                        onDelete = onDelete,
                                        onClear = onClear,
                                        onMoveCursor = onMoveCursor,
                                        onKeyActionStart = { text, pos ->
                                            pressedKeyText = text
                                            pressedKeyPosition = pos
                                        },
                                        onKeyActionEnd = { pressedKeyText = null },
                                        onEnter = onEnter,
                                        onSwitchMode = onSwitchMode
                                    )
                                } else {
                                    QWERTYKeyboardLayout(
                                        mode = state.mode,
                                        isShifted = state.isShifted,
                                        theme = theme,
                                        onKeyPress = onKeyPress,
                                        onDelete = onDelete,
                                        onClear = onClear,
                                        onMoveCursor = onMoveCursor,
                                        onKeyActionStart = { text, pos ->
                                            pressedKeyText = text
                                            pressedKeyPosition = pos
                                        },
                                        onKeyActionEnd = { pressedKeyText = null },
                                        onEnter = onEnter,
                                        onSwitchMode = onSwitchMode,
                                        onToggleShift = onToggleShift,
                                        onToggleTraditional = onToggleTraditional,
                                        isTraditional = state.isTraditional
                                    )
                                }
                            }
                        }
                        ActivePanel.CHAO_HUI_SHUO -> ChaoHuiShuoPanel(state, onClose = { onAiAction("ClosePanel") }, onReplySelect = { onAiAction("SelectReply") }, onRefreshReply = { onAiAction("RefreshReply") })
                        ActivePanel.BANG_NI_HUI -> BangNiHuiPanel(state, onClose = { onAiAction("ClosePanel") }, onReplySelect = { onAiAction("SelectReply") })
                        ActivePanel.THEME_SELECTION -> ThemeSelectionPanel(state.currentTheme, { onAiAction("Theme_$it") }, { onAiAction("ClosePanel") })
                    }
                }
            }
        }

        // --- Overlays ---
        if (state.showPaywall) {
            VipPaywallOverlay(
                onPurchase = { onAiAction("Purchase") },
                onClose = { onAiAction("PaywallClose") }
            )
        }

        // --- Key Popup Preview Overlay ---
        pressedKeyText?.let { text ->
            pressedKeyPosition?.let { pos ->
                val density = LocalDensity.current.density
                Box(
                    modifier = Modifier
                        .offset(x = (pos.x / density - 8).dp, y = (pos.y / density - 60).dp) // Offset above the key
                        .size(56.dp, 68.dp)
                        .shadow(8.dp, RoundedCornerShape(12.dp))
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = text,
                        fontSize = 32.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                }
            }
        }
    }
}
