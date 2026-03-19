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
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.Crossfade

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
    val theme = state.currentTheme

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(theme.keyboardBackground)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(bottom = 4.dp)
    ) {
        // --- Candidates Strip & Context Actions ---
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

        // Only show Toolbar and Keyboard if we are in KEYBOARD panel
        Crossfade(targetState = state.activePanel, label = "PanelTransition") { panel ->
            when (panel) {
                ActivePanel.CHAO_HUI_SHUO -> {
                    ChaoHuiShuoPanel(
                        state = state,
                        onClose = { onAiAction("CloseAi") },
                        onReplySelect = { onAiAction(it) },
                        onRefreshReply = { onAiAction("Refresh") }
                    )
                }
                ActivePanel.BANG_NI_HUI -> {
                    BangNiHuiPanel(
                        state = state,
                        onClose = { onAiAction("CloseAi") },
                        onReplySelect = { onAiAction(it) }
                    )
                }
                ActivePanel.KEYBOARD -> {
                    Column {
                        // --- 1. AI Toolbar Top ---
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 6.dp)
                                .height(40.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier.size(36.dp).clip(CircleShape).background(theme.toolbarBackground), contentAlignment = Alignment.Center) { Text("⌨", color = theme.accentColor, fontSize = 16.sp) }
                            Box(modifier = Modifier.height(32.dp).clip(CircleShape).background(theme.accentColor).padding(horizontal = 14.dp).clickable { onAiAction("帮你回") }, contentAlignment = Alignment.Center) { Text("帮你回", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold) }
                            Box(modifier = Modifier.height(32.dp).clip(CircleShape).background(theme.toolbarBackground).padding(horizontal = 14.dp).clickable { onAiAction("超会说") }, contentAlignment = Alignment.Center) { Text("超会说", color = theme.toolbarIconColor, fontSize = 13.sp, fontWeight = FontWeight.Bold) }
                            Spacer(modifier = Modifier.weight(1f))
                            // Remaining free usages badge placeholder
                            Box(modifier = Modifier.clip(CircleShape).background(Color(0xFFFFEBEE)).padding(horizontal = 6.dp, vertical = 2.dp)) {
                                Text("❤️ ${state.freeUsagesLeft}", color = Color(0xFFFF4B6B), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                            Box(modifier = Modifier.size(32.dp).clip(CircleShape).border(1.dp, theme.toolbarIconColor.copy(alpha = 0.2f), CircleShape).clickable { onAiAction("Themes") }, contentAlignment = Alignment.Center) { Text("👕", color = theme.toolbarIconColor, fontSize = 14.sp) }
                            Box(modifier = Modifier.size(32.dp).clip(CircleShape).border(1.dp, theme.toolbarIconColor.copy(alpha = 0.2f), CircleShape), contentAlignment = Alignment.Center) { Text("⊞", color = theme.toolbarIconColor, fontSize = 16.sp) }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // --- 2. Keyboard Panels ---
                        if (state.mode == KeyboardMode.T9_PINYIN) {
                            T9KeyboardLayout(
                                theme = theme,
                                onKeyPress = onKeyPress,
                                onDelete = onDelete,
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
                                onEnter = onEnter,
                                onSwitchMode = onSwitchMode,
                                onToggleShift = onToggleShift,
                                onToggleTraditional = onToggleTraditional,
                                isTraditional = state.isTraditional
                            )
                        }
                    }
                }
                ActivePanel.THEME_SELECTION -> {
                    ThemeSelectionPanel(
                        currentTheme = theme,
                        onThemeSelect = { onAiAction("SelectTheme:$it") },
                        onClose = { onAiAction("CloseAi") }
                    )
                }
            }
        }
    }

    // --- Vip Paywall Overlay ---
    if (state.showPaywall) {
        VipPaywallOverlay(
            onClose = { onAiAction("PaywallClose") },
            onPurchase = { onAiAction("Purchase") }
        )
    }
}
