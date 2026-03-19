package com.lovekey.clone.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> WheelPicker(
    items: List<T>,
    value: T,
    onChange: (T) -> Unit,
    unit: String,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)
    val itemHeight = 56.dp

    // Sync initial value
    LaunchedEffect(items) {
        val index = items.indexOf(value)
        if (index != -1) {
            listState.scrollToItem(index)
        }
    }

    // React to scroll changes
    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .map { it }
            .distinctUntilChanged()
            .collect { index ->
                if (index in items.indices) {
                    onChange(items[index])
                }
            }
    }

    Box(modifier = modifier.height(168.dp).width(100.dp)) {
        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = itemHeight) // Space for top and bottom to allow centering
        ) {
            items(items.size) { index ->
                val item = items[index]
                val displayValue = if (unit != "年") {
                    item.toString().padStart(2, '0')
                } else {
                    item.toString()
                }

                // Simple derived state for selection based on scroll position
                val isSelected = remember { derivedStateOf { listState.firstVisibleItemIndex == index } }

                Box(
                    modifier = Modifier
                        .height(itemHeight)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$displayValue$unit",
                        color = if (isSelected.value) Color(0xFF1A1A1A) else Color(0xFFC0C4D0),
                        fontWeight = FontWeight.Bold,
                        fontSize = if (isSelected.value) 20.sp else 16.sp
                    )
                }
            }
        }
    }
}
