package org.example.project.Demo.Map

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import kotlin.math.max
import kotlin.math.min

// Цветовая схема комнат
fun roomColor(type: RoomType, isHighlighted: Boolean): Color = when {
    isHighlighted -> Color(0xFFFFD700)  // золото — выбранный кабинет
    type == RoomType.CLASSROOM  -> Color(0xFFD6EAF8)
    type == RoomType.LAB        -> Color(0xFFD5F5E3)
    type == RoomType.TOILET     -> Color(0xFFF2F3F4)
    type == RoomType.CAFETERIA  -> Color(0xFFFDEBD0)
    type == RoomType.LIBRARY    -> Color(0xFFE8DAEF)
    type == RoomType.OFFICE     -> Color(0xFFFAD7A0)
    type == RoomType.CORRIDOR   -> Color(0xFFF8F9FA)
    else                        -> Color(0xFFECF0F1)
}

fun roomBorderColor(type: RoomType, isHighlighted: Boolean): Color = when {
    isHighlighted               -> Color(0xFFF39C12)
    type == RoomType.CORRIDOR   -> Color(0xFFBDC3C7)
    else                        -> Color(0xFF7F8C8D)
}

@Composable
fun MapCanvas(
    floor: Floor,
    transform: MapTransform,
    userLocation: UserLocation?,
    highlightedRoom: Room?,
    currentFloor: Int,
    onTransformChange: (MapTransform) -> Unit,
    onRoomClick: (Room) -> Unit,
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()

    // Локальное состояние трансформации для плавного взаимодействия
    var scale by remember(transform) { mutableStateOf(transform.scale) }
    var offsetX by remember(transform) { mutableStateOf(transform.offsetX) }
    var offsetY by remember(transform) { mutableStateOf(transform.offsetY) }

    Canvas(
        modifier = modifier
            // Pinch-to-zoom + pan
            .pointerInput(Unit) {
                detectTransformGestures { centroid, pan, zoom, _ ->
                    val newScale = (scale * zoom).coerceIn(0.5f, 5f)
                    // Масштабирование относительно центра жеста
                    val scaleChange = newScale / scale
                    offsetX = centroid.x + (offsetX - centroid.x) * scaleChange + pan.x
                    offsetY = centroid.y + (offsetY - centroid.y) * scaleChange + pan.y
                    scale = newScale
                    onTransformChange(MapTransform(scale, offsetX, offsetY))
                }
            }
            // Tap для выбора кабинета
            .pointerInput(floor) {
                detectTapGestures { tapOffset ->
                    // Переводим tap-координаты в координаты карты
                    val mapX = (tapOffset.x - offsetX) / scale
                    val mapY = (tapOffset.y - offsetY) / scale
                    val tappedRoom = floor.rooms.firstOrNull { room ->
                        room.type != RoomType.CORRIDOR &&
                                room.rect.contains(Offset(mapX, mapY))
                    }
                    tappedRoom?.let { onRoomClick(it) }
                }
            }
    ) {
        withTransform({
            translate(offsetX, offsetY)
            scale(scale, scale, Offset.Zero)
        }) {
            // 1. Фон здания
            drawRect(
                color = Color(0xFFF0F0F0),
                topLeft = Offset(0f, 0f),
                size = androidx.compose.ui.geometry.Size(floor.width, floor.height)
            )

            // 2. Комнаты
            floor.rooms.forEach { room ->
                val isHighlighted = room.id == highlightedRoom?.id
                drawRoom(room, isHighlighted)
            }

            // 3. Стены
            floor.walls.forEach { wall ->
                drawLine(
                    color = Color(0xFF2C3E50),
                    start = wall.start,
                    end = wall.end,
                    strokeWidth = wall.thickness,
                    cap = StrokeCap.Square
                )
            }

            // 4. Подписи комнат
            floor.rooms.forEach { room ->
                if (room.type != RoomType.CORRIDOR) {
                    drawRoomLabel(room, textMeasurer, scale)
                }
            }

            // 5. Иконка пользователя
            if (userLocation != null && userLocation.floor == currentFloor) {
                drawUserMarker(userLocation.position)
            }

            // 6. Пульсирующая рамка вокруг выбранного кабинета
            highlightedRoom?.let {
                drawHighlightBorder(it.rect)
            }
        }
    }
}

// Отрисовка комнаты
private fun DrawScope.drawRoom(room: Room, isHighlighted: Boolean) {
    val fillColor = roomColor(room.type, isHighlighted)
    val borderColor = roomBorderColor(room.type, isHighlighted)
    val strokeWidth = if (isHighlighted) 3f else 1.5f

    drawRect(
        color = fillColor,
        topLeft = room.rect.topLeft,
        size = room.rect.size
    )
    drawRect(
        color = borderColor,
        topLeft = room.rect.topLeft,
        size = room.rect.size,
        style = Stroke(width = strokeWidth)
    )
}

// Подпись кабинета — номер + название (при достаточном зуме)
private fun DrawScope.drawRoomLabel(
    room: Room,
    textMeasurer: TextMeasurer,
    currentScale: Float
) {
    val center = room.rect.center
    val roomW = room.rect.width
    val roomH = room.rect.height

    if (roomW < 20f || roomH < 20f) return

    // Номер кабинета — всегда
    if (room.number.isNotEmpty()) {
        val numberLayout = textMeasurer.measure(
            text = room.number,
            style = TextStyle(
                fontSize = (10f / currentScale).coerceIn(6f, 14f).sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2C3E50)
            )
        )
        drawText(
            textLayoutResult = numberLayout,
            topLeft = Offset(
                center.x - numberLayout.size.width / 2f,
                center.y - numberLayout.size.height / 2f - if (currentScale > 1.5f) 6f else 0f
            )
        )
    }

    // Название — только при достаточном зуме
    if (currentScale > 1.8f && room.name.isNotEmpty() && room.number.isNotEmpty()) {
        val nameLayout = textMeasurer.measure(
            text = room.name,
            style = TextStyle(
                fontSize = (7f / currentScale).coerceIn(4f, 10f).sp,
                color = Color(0xFF7F8C8D)
            )
        )
        drawText(
            textLayoutResult = nameLayout,
            topLeft = Offset(
                center.x - nameLayout.size.width / 2f,
                center.y + 4f
            )
        )
    }
}

// Маркер пользователя — синяя точка с кольцом
private fun DrawScope.drawUserMarker(position: Offset) {
    // Внешнее кольцо (полупрозрачное)
    drawCircle(
        color = Color(0x442196F3),
        radius = 18f,
        center = position
    )
    // Белый ободок
    drawCircle(
        color = Color.White,
        radius = 10f,
        center = position
    )
    // Синяя точка
    drawCircle(
        color = Color(0xFF2196F3),
        radius = 8f,
        center = position
    )
    // Маленький блик
    drawCircle(
        color = Color.White,
        radius = 3f,
        center = position - Offset(2f, 2f)
    )
}

// Анимированная рамка выбранного кабинета
private fun DrawScope.drawHighlightBorder(rect: Rect) {
    drawRect(
        color = Color(0x80F39C12),
        topLeft = rect.topLeft - Offset(4f, 4f),
        size = rect.size.let {
            androidx.compose.ui.geometry.Size(it.width + 8f, it.height + 8f)
        },
        style = Stroke(width = 3f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 4f)))
    )
}