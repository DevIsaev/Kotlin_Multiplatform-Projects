package org.example.project.Demo.Map

import androidx.compose.runtime.Composable
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex

@Composable
fun MapDemo(viewModel: MapViewModel = remember { MapViewModel() }) {
    val state by viewModel.uiState.collectAsState()
    val currentFloor = state.floors.firstOrNull { it.number == state.selectedFloor }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF5F6FA))) {

        // ── Шапка ──────────────────────────────────────────────
        MapHeader()

        // ── Поиск ──────────────────────────────────────────────
        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp).zIndex(10f)) {
            MapSearchBar(
                query = state.searchQuery,
                results = state.searchResults,
                isActive = state.isSearchActive,
                onQueryChange = viewModel::onSearch,
                onRoomSelect = viewModel::selectRoom,
                onClear = viewModel::clearSearch
            )
        }

        // ── Переключатель этажей ────────────────────────────────
        FloorSelector(
            floors = state.floors,
            selectedFloor = state.selectedFloor,
            onFloorSelect = viewModel::selectFloor
        )

        // ── Карта ──────────────────────────────────────────────
        Box(modifier = Modifier.weight(1f).padding(8.dp)) {
            if (currentFloor != null) {
                MapCanvas(
                    floor = currentFloor,
                    transform = state.mapTransform,
                    userLocation = state.userLocation,
                    highlightedRoom = state.highlightedRoom,
                    currentFloor = state.selectedFloor,
                    onTransformChange = viewModel::updateTransform,
                    onRoomClick = viewModel::selectRoom,
                    modifier = Modifier.fillMaxSize()
                        .background(Color.White, RoundedCornerShape(16.dp))
                        .clip(RoundedCornerShape(16.dp))  // добавь import clip
                )
            }

            // Кнопка сброса вида
            FloatingActionButton(
                onClick = viewModel::resetView,
                modifier = Modifier.align(Alignment.BottomEnd).padding(12.dp).size(44.dp),
                containerColor = Color.White,
                contentColor = Color(0xFF2196F3)
            ) {
                Text("⌖", fontSize = 22.sp)
            }

            // Легенда
            MapLegend(modifier = Modifier.align(Alignment.BottomStart).padding(12.dp))
        }

        // ── Карточка выбранного кабинета ────────────────────────
        AnimatedVisibility(
            visible = state.highlightedRoom != null,
            enter = slideInVertically { it } + fadeIn(),
            exit = slideOutVertically { it } + fadeOut()
        ) {
            state.highlightedRoom?.let { room ->
                RoomInfoCard(room = room, onDismiss = viewModel::clearSearch)
            }
        }
    }
}

@Composable
private fun MapHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1565C0))
            .padding(16.dp)
    ) {
        Text(
            text = "🏛️  Карта корпуса",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun FloorSelector(
    floors: List<Floor>,
    selectedFloor: Int,
    onFloorSelect: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        floors.forEach { floor ->
            val isSelected = floor.number == selectedFloor
            Surface(
                onClick = { onFloorSelect(floor.number) },
                shape = RoundedCornerShape(20.dp),
                color = if (isSelected) Color(0xFF1565C0) else Color.White,
                shadowElevation = if (isSelected) 4.dp else 1.dp,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = floor.label,
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                    color = if (isSelected) Color.White else Color(0xFF555555),
                    fontSize = 13.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun RoomInfoCard(room: Room, onDismiss: () -> Unit) {
    val icon = when (room.type) {
        RoomType.CLASSROOM -> "🎓"
        RoomType.LAB       -> "🔬"
        RoomType.CAFETERIA -> "🍽️"
        RoomType.LIBRARY   -> "📚"
        RoomType.OFFICE    -> "🏢"
        else               -> "📍"
    }
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(icon, fontSize = 32.sp)
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (room.number.isNotEmpty()) "Кабинет ${room.number}" else room.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(room.name, color = Color.Gray, fontSize = 14.sp)
                Text(
                    text = "${room.floor} этаж · ${roomTypeLabel(room.type)}",
                    color = Color(0xFF2196F3),
                    fontSize = 12.sp
                )
            }
            TextButton(onClick = onDismiss) { Text("✕") }
        }
    }
}

private fun roomTypeLabel(type: RoomType) = when (type) {
    RoomType.CLASSROOM -> "Аудитория"
    RoomType.LAB       -> "Лаборатория"
    RoomType.CAFETERIA -> "Столовая"
    RoomType.LIBRARY   -> "Библиотека"
    RoomType.OFFICE    -> "Кабинет"
    RoomType.TOILET    -> "Санузел"
    RoomType.CORRIDOR  -> "Коридор"
    else               -> "Помещение"
}

@Composable
private fun MapLegend(modifier: Modifier = Modifier) {
    Card(modifier = modifier, shape = RoundedCornerShape(10.dp)) {
        Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            LegendItem(Color(0xFFD6EAF8), "Аудитория")
            LegendItem(Color(0xFFD5F5E3), "Лаборатория")
            LegendItem(Color(0xFFFAD7A0), "Кабинеты")
            LegendItem(Color(0xFF2196F3), "Вы здесь")
        }
    }
}

@Composable
private fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier.size(12.dp).background(color, RoundedCornerShape(3.dp))
                .border(1.dp, Color.Gray.copy(0.4f), RoundedCornerShape(3.dp))
        )
        Spacer(Modifier.width(4.dp))
        Text(label, fontSize = 10.sp, color = Color.DarkGray)
    }
}