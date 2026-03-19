package com.lovekey.clone.ime

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ThemeSelectionPanel(
    currentTheme: KeyboardTheme,
    onThemeSelect: (String) -> Unit,
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp) // Maintain consistent panel height
            .background(currentTheme.keyboardBackground)
    ) {
        // Toolbar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(currentTheme.toolbarBackground)
                    .clickable { onClose() },
                contentAlignment = Alignment.Center
            ) {
                Text("✕", color = currentTheme.toolbarIconColor, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.weight(1f))
            Text("个性皮肤", color = currentTheme.keyTextColor, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.weight(1f))
            Box(modifier = Modifier.size(28.dp)) // Spacer for balance
        }

        // Theme Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(ThemePresets.allThemes) { themeOption ->
                ThemeCard(
                    themeOption = themeOption,
                    isSelected = themeOption.id == currentTheme.id,
                    onClick = { onThemeSelect(themeOption.id) }
                )
            }
        }
    }
}

@Composable
fun ThemeCard(
    themeOption: KeyboardTheme,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .shadow(2.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(themeOption.keyboardBackground)
            .run {
                if (isSelected) this.border(3.dp, themeOption.accentColor, RoundedCornerShape(16.dp))
                else this
            }
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        // Preview mini-keyboard inside the card
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                MiniKey(themeOption.keyBackground)
                MiniKey(themeOption.keyBackground)
                MiniKey(themeOption.keyBackground)
                MiniKey(themeOption.keyBackground)
                MiniKey(themeOption.keyBackground)
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                MiniKey(themeOption.actionKeyBackground)
                MiniKey(themeOption.keyBackground)
                MiniKey(themeOption.keyBackground)
                MiniKey(themeOption.accentColor) // Enter key color
            }
        }

        // Theme Name Overlay
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.3f))
                .padding(vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = themeOption.name,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun MiniKey(color: Color) {
    Box(
        modifier = Modifier
            .width(20.dp)
            .height(14.dp)
            .clip(RoundedCornerShape(3.dp))
            .background(color)
    )
}
