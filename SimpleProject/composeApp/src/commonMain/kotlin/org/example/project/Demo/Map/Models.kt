package org.example.project.Demo.Map

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect

// Тип помещения
enum class RoomType {
    CLASSROOM, LAB, TOILET, STAIRS, ELEVATOR,
    CAFETERIA, LIBRARY, OFFICE, CORRIDOR, UNKNOWN
}

// Комната/кабинет
data class Room(
    val id: String,
    val number: String,           // "101", "А-205"
    val name: String,             // "Аудитория высшей математики"
    val type: RoomType,
    val floor: Int,
    val rect: Rect,               // позиция на карте (в условных единицах)
    val capacity: Int = 0,
    val description: String = ""
)

// Этаж
data class Floor(
    val number: Int,
    val label: String,            // "1 этаж", "Цоколь"
    val rooms: List<Room>,
    val walls: List<WallSegment>, // контуры стен
    val width: Float,             // размер плана в условных единицах
    val height: Float
)

// Сегмент стены
data class WallSegment(
    val start: Offset,
    val end: Offset,
    val thickness: Float = 4f
)

// Положение пользователя
data class UserLocation(
    val floor: Int,
    val position: Offset         // в условных единицах карты
)

// Состояние трансформации карты
data class MapTransform(
    val scale: Float = 1f,
    val offsetX: Float = 0f,
    val offsetY: Float = 0f
)