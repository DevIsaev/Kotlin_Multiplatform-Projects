package org.example.project.Demo.Map

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import kotlinx.coroutines.flow.update

data class MapUiState(
    val floors: List<Floor> = emptyList(),
    val selectedFloor: Int = 1,
    val mapTransform: MapTransform = MapTransform(),
    val userLocation: UserLocation? = null,
    val searchQuery: String = "",
    val searchResults: List<Room> = emptyList(),
    val highlightedRoom: Room? = null,
    val isSearchActive: Boolean = false
)

class MapViewModel {

    private val _uiState = MutableStateFlow(MapUiState())
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    init {
        loadMapData()
        // Симулируем позицию пользователя
        _uiState.update { it.copy(
            userLocation = UserLocation(floor = 1, position = Offset(120f, 80f))
        )}
    }

    private fun loadMapData() {
        val floors = buildSampleFloors()
        _uiState.update { it.copy(
            floors = floors,
            selectedFloor = 1
        )}
    }

    fun selectFloor(floor: Int) {
        _uiState.update { it.copy(selectedFloor = floor, highlightedRoom = null) }
    }

    fun onSearch(query: String) {
        _uiState.update { state ->
            val results = if (query.length >= 2) {
                state.floors
                    .flatMap { it.rooms }
                    .filter { room ->
                        room.number.contains(query, ignoreCase = true) ||
                                room.name.contains(query, ignoreCase = true)
                    }
            } else emptyList()
            state.copy(searchQuery = query, searchResults = results, isSearchActive = query.isNotEmpty())
        }
    }

    fun selectRoom(room: Room) {
        _uiState.update { it.copy(
            highlightedRoom = room,
            selectedFloor = room.floor,
            isSearchActive = false,
            searchQuery = "${room.number} — ${room.name}"
        )}
        navigateToRoom(room)
    }

    fun clearSearch() {
        _uiState.update { it.copy(
            searchQuery = "",
            searchResults = emptyList(),
            isSearchActive = false,
            highlightedRoom = null
        )}
    }

    // Центрируем карту на выбранном кабинете
    private fun navigateToRoom(room: Room) {
        val targetScale = 2.5f
        val centerX = room.rect.center.x
        val centerY = room.rect.center.y
        _uiState.update { it.copy(
            mapTransform = MapTransform(
                scale = targetScale,
                offsetX = -centerX * targetScale + 200f,
                offsetY = -centerY * targetScale + 300f
            )
        )}
    }

    fun updateTransform(transform: MapTransform) {
        _uiState.update { it.copy(mapTransform = transform) }
    }

    fun resetView() {
        _uiState.update { it.copy(mapTransform = MapTransform()) }
    }

    // Тестовые данные — замените на реальный план
    private fun buildSampleFloors(): List<Floor> {
        return listOf(
            Floor(
                number = 1,
                label = "1 этаж",
                width = 400f,
                height = 300f,
                walls = buildWalls(400f, 300f),
                rooms = listOf(
                    Room("101", "101", "Деканат", RoomType.OFFICE, 1,
                        Rect(10f, 10f, 100f, 70f)),
                    Room("102", "102", "Аудитория", RoomType.CLASSROOM, 1,
                        Rect(110f, 10f, 210f, 70f)),
                    Room("103", "103", "Лаборатория физики", RoomType.LAB, 1,
                        Rect(220f, 10f, 340f, 70f)),
                    Room("wc1", "WC", "Туалет", RoomType.TOILET, 1,
                        Rect(350f, 10f, 390f, 70f)),
                    Room("104", "104", "Аудитория", RoomType.CLASSROOM, 1,
                        Rect(10f, 180f, 110f, 290f)),
                    Room("105", "105", "Лаборатория ИТ", RoomType.LAB, 1,
                        Rect(120f, 180f, 240f, 290f)),
                    Room("cafe", "—", "Столовая", RoomType.CAFETERIA, 1,
                        Rect(250f, 180f, 390f, 290f)),
                    Room("corr1", "", "Коридор", RoomType.CORRIDOR, 1,
                        Rect(10f, 80f, 390f, 170f)),
                )
            ),
            Floor(
                number = 2,
                label = "2 этаж",
                width = 400f,
                height = 300f,
                walls = buildWalls(400f, 300f),
                rooms = listOf(
                    Room("201", "201", "Кафедра математики", RoomType.OFFICE, 2,
                        Rect(10f, 10f, 130f, 70f)),
                    Room("202", "202", "Аудитория", RoomType.CLASSROOM, 2,
                        Rect(140f, 10f, 250f, 70f)),
                    Room("203", "203", "Аудитория", RoomType.CLASSROOM, 2,
                        Rect(260f, 10f, 390f, 70f)),
                    Room("204", "204", "Библиотека", RoomType.LIBRARY, 2,
                        Rect(10f, 180f, 200f, 290f)),
                    Room("205", "205", "Лаборатория химии", RoomType.LAB, 2,
                        Rect(210f, 180f, 390f, 290f)),
                    Room("corr2", "", "Коридор", RoomType.CORRIDOR, 2,
                        Rect(10f, 80f, 390f, 170f)),
                )
            ),
            Floor(
                number = 3,
                label = "3 этаж",
                width = 400f,
                height = 300f,
                walls = buildWalls(400f, 300f),
                rooms = listOf(
                    Room("301", "301", "Ректорат", RoomType.OFFICE, 3,
                        Rect(10f, 10f, 180f, 90f)),
                    Room("302", "302", "Конференц-зал", RoomType.CLASSROOM, 3,
                        Rect(190f, 10f, 390f, 90f)),
                    Room("303", "303", "Аудитория", RoomType.CLASSROOM, 3,
                        Rect(10f, 180f, 150f, 290f)),
                    Room("304", "304", "Аудитория", RoomType.CLASSROOM, 3,
                        Rect(160f, 180f, 300f, 290f)),
                    Room("wc3", "WC", "Туалет", RoomType.TOILET, 3,
                        Rect(310f, 180f, 390f, 290f)),
                    Room("corr3", "", "Коридор", RoomType.CORRIDOR, 3,
                        Rect(10f, 100f, 390f, 170f)),
                )
            )
        )
    }

    private fun buildWalls(w: Float, h: Float): List<WallSegment> = listOf(
        WallSegment(Offset(0f, 0f), Offset(w, 0f)),
        WallSegment(Offset(w, 0f), Offset(w, h)),
        WallSegment(Offset(w, h), Offset(0f, h)),
        WallSegment(Offset(0f, h), Offset(0f, 0f))
    )
}