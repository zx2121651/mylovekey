package com.lovekey.clone.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
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
import com.lovekey.clone.data.MockData.PERSONA_DATA
import com.lovekey.clone.data.Persona

@Composable
fun MyKeyboardScreen() {
    val myPersonas = remember { mutableStateListOf(*PERSONA_DATA.take(6).toTypedArray()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F8FD))
            .padding(top = 48.dp)
    ) {
        // Top Nav
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White)
                    .clickable { /* go back */ }
                    .shadow(8.dp, spotColor = Color(0x0A000000)),
                contentAlignment = Alignment.Center
            ) {
                Text("<", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A1A))
            }
            Spacer(modifier = Modifier.weight(1f))
            Text("我的键盘", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A1A))
            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(42.dp))
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            "长按拖动可以调整排序",
            fontSize = 13.sp,
            color = Color(0xFF999999),
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(myPersonas.size) { index ->
                val persona = myPersonas[index]
                KeyboardPersonaItem(
                    persona = persona,
                    onDelete = { myPersonas.remove(persona) }
                )
            }
        }
    }
}

@Composable
fun KeyboardPersonaItem(persona: Persona, onDelete: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFF3F4F6), RoundedCornerShape(20.dp))
            .padding(16.dp)
            .shadow(4.dp, spotColor = Color(0x05000000)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("≡", fontSize = 24.sp, color = Color(0xFFDCDFE6))
        Spacer(modifier = Modifier.width(16.dp))
        Text(persona.icon, fontSize = 24.sp)
        Spacer(modifier = Modifier.width(12.dp))
        Text(persona.text, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A1A), modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(Color(0xFFFFF0F0))
                .clickable { onDelete() },
            contentAlignment = Alignment.Center
        ) {
            Text("-", color = Color(0xFFFF4B4B), fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.offset(y = (-1).dp))
        }
    }
}
