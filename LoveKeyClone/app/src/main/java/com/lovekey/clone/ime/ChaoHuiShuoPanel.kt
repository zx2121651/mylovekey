package com.lovekey.clone.ime

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ChaoHuiShuoPanel(
    state: KeyboardState,
    onClose: () -> Unit,
    onReplySelect: (String) -> Unit,
    onRefreshReply: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp) // Takes the space of the normal keyboard
            .background(Color(0xFFF2F4F9))
    ) {
        // Toolbar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .clickable { /* mock changing scene */ }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text("通用 ⇌", color = Color(0xFF1A1A1A), fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White)
                    .clickable { onClose() },
                contentAlignment = Alignment.Center
            ) {
                Text("✕", color = Color(0xFF888888), fontSize = 14.sp)
            }
        }

        if (state.aiLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF4B66FF), strokeWidth = 3.dp)
            }
        } else if (state.aiMockResults.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("请输入或复制对方的话，我会帮你高情商回复", color = Color(0xFFA0A5B5), fontSize = 14.sp)
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(state.aiMockResults) { category ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = category.categoryName,
                            color = Color(0xFF8C9AD6),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        category.replies.forEachIndexed { index, reply ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = reply,
                                    color = Color(0xFF1A1A1A),
                                    fontSize = 15.sp,
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { onReplySelect(reply) }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFFF2F4F9))
                                        .clickable { onRefreshReply(category.categoryName) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("↻", color = Color(0xFFA0A5B5), fontSize = 14.sp)
                                }
                            }
                            if (index < category.replies.size - 1) {
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}
