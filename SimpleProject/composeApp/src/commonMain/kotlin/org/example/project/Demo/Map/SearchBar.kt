package org.example.project.Demo.Map

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MapSearchBar(
    query: String,
    results: List<Room>,
    isActive: Boolean,
    onQueryChange: (String) -> Unit,
    onRoomSelect: (Room) -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Поле ввода
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = { Text("Найти кабинет...") },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = onClear) {
                        Text("✕", fontSize = 16.sp)
                    }
                }
            },
            leadingIcon = {
                Text("🔍", fontSize = 16.sp, modifier = Modifier.padding(start = 8.dp))
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // Список результатов
        AnimatedVisibility(visible = isActive && results.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                LazyColumn(modifier = Modifier.heightIn(max = 280.dp)) {
                    items(results) { room ->
                        SearchResultItem(room = room, onClick = { onRoomSelect(room) })
                        HorizontalDivider(color = Color(0xFFEEEEEE))
                    }
                }
            }
        }

        // Нет результатов
        AnimatedVisibility(visible = isActive && query.length >= 2 && results.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Box(Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                    Text("Кабинет не найден", color = Color.Gray)
                }
            }
        }
    }
}

@Composable
private fun SearchResultItem(room: Room, onClick: () -> Unit) {
    val icon = when (room.type) {
        RoomType.CLASSROOM -> "🎓"
        RoomType.LAB       -> "🔬"
        RoomType.CAFETERIA -> "🍽️"
        RoomType.LIBRARY   -> "📚"
        RoomType.OFFICE    -> "🏢"
        RoomType.TOILET    -> "🚻"
        else               -> "📍"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(icon, fontSize = 20.sp)
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = if (room.number.isNotEmpty()) "№${room.number}" else room.name,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp
            )
            if (room.number.isNotEmpty()) {
                Text(room.name, fontSize = 13.sp, color = Color.Gray)
            }
        }
        Text("${room.floor} эт.", fontSize = 12.sp, color = Color(0xFF2196F3))
    }
}