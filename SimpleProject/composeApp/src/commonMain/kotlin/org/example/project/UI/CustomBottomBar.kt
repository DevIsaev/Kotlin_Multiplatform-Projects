package org.example.project.UI

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Animation
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose.errorLight
import com.example.compose.onPrimaryContainerLight
import com.example.compose.onPrimaryLight
import com.example.compose.onSecondaryContainerLight
import com.example.compose.onSurfaceVariantLight
import com.example.compose.primaryContainerLight
import com.example.compose.surfaceLight
import com.example.compose.tertiaryLight
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

data class BottomBarItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: String
)

@Composable
fun CustomBottomBar(
    items: List<BottomBarItem>,
    currentRoute: String?,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier.height(50.dp),
        containerColor = Color.Green,
        tonalElevation = 8.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {
            items.forEach { item ->
                val selected = currentRoute == item.route

                NavigationBarItem(
                    selected = selected,
                    onClick = { onItemSelected(item.route) },
                    icon = {
                        Icon(
                            imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                            contentDescription = item.title
                        )
                    },
                    label = {
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }
}


// скругленный в воздухе

@Composable
fun RoundedCornerShapeBottomBar(
    items: List<BottomBarItem>,
    currentRoute: String,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    elevation: Dp = 16.dp,
    cornerRadius: Dp = 20.dp,
    verticalPadding: Dp = 8.dp,
    horizontalPadding: Dp = 16.dp
) {
    Card(modifier = modifier
            .fillMaxWidth()
            .height(70.dp)
            .shadow(elevation = elevation,
                shape = RoundedCornerShape(
                    cornerRadius
                ),
                clip = false
            ),
        shape = RoundedCornerShape(
            cornerRadius
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.Yellow
        )
    ) {
        NavigationBar(
            containerColor = Color.Transparent,
            modifier = Modifier
                .fillMaxSize().background(Color.LightGray)
                .clip(
                    RoundedCornerShape(
                        topStart = 20.dp,
                        topEnd = 20.dp
                    )
                )
        ) {
            items.forEach { item ->
                val selected = currentRoute == item.route
                NavigationBarItem(
                    selected = selected,
                    onClick = { onItemSelected(item.route) },
                    icon = {
                        Icon(
                            imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                            contentDescription = item.title
                        )
                    },
                    label = { Text(item.title) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FloatingRoundedBottomBar(
    items: List<BottomBarItem>,
    currentRoute: String,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.LightGray.copy(alpha = 0.95f),
    selectedColor: Color = Color.Yellow.copy(alpha = 0.8f),
    elevation: Dp = 16.dp,
    cornerRadius: Dp = 16.dp
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .navigationBarsPadding()
            .padding(horizontal = 24.dp, vertical = 18.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .shadow(
                    elevation = elevation,
                    shape = RoundedCornerShape(cornerRadius),
                    clip = true,
                ),
            shape = RoundedCornerShape(cornerRadius),
            colors = CardDefaults.cardColors(
                containerColor = backgroundColor,
                contentColor = Color.White
            )
        ) {
            NavigationBar(
                containerColor = Color.Transparent,
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor)
                    .clip(RoundedCornerShape(cornerRadius))
            ) {
                items.forEach { item ->
                    val selected = currentRoute == item.route

                    // Создаем кастомный стиль для NavigationBarItem
                    NavigationBarItem(
                        selected = selected,
                        onClick = { onItemSelected(item.route) },
                        icon = {
                            Box(
                                modifier = Modifier
                                    .size(85.dp)
                                    .background(
                                        if (selected) selectedColor else Color.Transparent,
                                        RoundedCornerShape(20.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                        contentDescription = item.title
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = item.title,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        },
                        label = { }, // Пустой лейбл, так как текст уже внутри иконки
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Black,
                            selectedTextColor = Color.Black,
                            unselectedIconColor = Color.White,
                            unselectedTextColor = Color.White,
                            indicatorColor = Color.Transparent // Отключаем стандартный индикатор
                        ),
                        modifier = Modifier.background(Color.Transparent).padding(top=7.dp)
                    )
                }
            }
        }
    }
}


// Компонент кнопки для BottomBar
@Composable
fun BottomBarButton(
    selected: Boolean,
    onClick: () -> Unit,
    icon: ImageVector,
    label: String
) {
    val color = if (selected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
    }

    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = color
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = color
        )
    }
}






data class WavyBottomBarItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: String,
    val badgeCount: Int = 0
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WavyBottomBar(
    items: List<WavyBottomBarItem>,
    currentRoute: String?,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color= onSurfaceVariantLight,
    selectedContainerColor: Color = primaryContainerLight,
    selectedContentColor: Color = onPrimaryContainerLight,
    unselectedContentColor: Color = onSurfaceVariantLight,
    waveColor: Color = tertiaryLight,
    waveAmplitude: Dp = 20.dp,
    ballElevation: Dp = 8.dp,
    ballSize: Dp = 64.dp, // Увеличенный размер шара
    ballLift: Dp = (-24).dp // Подъем шара
) {
    val selectedIndex = items.indexOfFirst { it.route == currentRoute }
    val waveAnimationProgress by animateFloatAsState(
        targetValue = if (selectedIndex >= 0) 1f else 0f,
        animationSpec = tween(800)
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(90.dp) // Увеличена общая высота

    ) {
        // Волнистая линия под шаром
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(25.dp)
                .align(Alignment.TopCenter)
        ) {
            if (selectedIndex >= 0) {
                val itemWidth = size.width / items.size
                val centerX = itemWidth * (selectedIndex + 0.5f)
                val amplitude = waveAmplitude.toPx() * waveAnimationProgress
                val waveLength = itemWidth * 0.8f

                val path = Path().apply {
                    moveTo(0f, size.height)

                    // Левая часть волны
                    for (x in 0 until (centerX - waveLength * 1.5).toInt() step 5) {
                        val y = size.height
                        lineTo(x.toFloat(), y)
                    }

                    // Центральная волнистая часть
                    for (x in (centerX - waveLength * 1.5).toInt()..(centerX + waveLength * 1.5).toInt() step 2) {
                        val progress = (x - (centerX - waveLength * 1.5)) / (waveLength * 3)
                        val waveX = progress *PI * 3
                        val y = size.height - (amplitude * sin(waveX).toFloat())
                        lineTo(x.toFloat(), y)
                    }

                    // Правая часть волны
                    for (x in (centerX + waveLength * 1.5).toInt()..size.width.toInt() step 5) {
                        val y = size.height
                        lineTo(x.toFloat(), y)
                    }
                }

                drawPath(
                    path = path,
                    color = waveColor,
                    style = Stroke(width = 3.dp.toPx())
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            items.forEachIndexed { index, item ->
                val isSelected = currentRoute == item.route
                val interactionSource = remember { MutableInteractionSource() }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .height(70.dp)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null, // Убираем стандартную анимацию ripple
                            onClick = { onItemSelected(item.route) }
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (isSelected) {
                        // Выбранный элемент - шар с анимацией подъема
                        Box(
                            modifier = Modifier
                                .size(ballSize)
                                .offset(y = ballLift * waveAnimationProgress)
                                .shadow(
                                    elevation = ballElevation * waveAnimationProgress,
                                    shape = CircleShape,
                                    clip = true
                                )
                                .clip(CircleShape)
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            selectedContainerColor.lighten(0.2f),
                                            selectedContainerColor
                                        )
                                    )
                                )
                                .drawBehind {
                                    // Добавляем световое свечение
                                    drawCircle(
                                        brush = Brush.radialGradient(
                                            colors = listOf(
                                                selectedContainerColor.copy(alpha = 0.3f),
                                                Color.Transparent
                                            ),
                                            center = Offset(size.width / 2, size.height / 2),
                                            radius = size.width * 0.8f
                                        )
                                    )
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            BadgedBox(
                                badge = {
                                    if (item.badgeCount > 0) {
                                        Badge(
                                            containerColor = MaterialTheme.colorScheme.error,
                                            contentColor = MaterialTheme.colorScheme.onError
                                        ) {
                                            Text(
                                                text = min(item.badgeCount, 99).toString(),
                                                fontSize = 10.sp
                                            )
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = item.selectedIcon,
                                    contentDescription = item.title,
                                    tint = selectedContentColor,
                                    modifier = Modifier.size(28.dp) // Увеличенная иконка
                                )
                            }
                        }

                        Text(
                            text = item.title,
                            color = selectedContentColor,
                            fontSize = 12.sp,
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.offset(y = (-4).dp * waveAnimationProgress)
                        )
                    } else {
                        // Невыбранные элементы
                        Box(
                            modifier = Modifier
                                .size(48.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            BadgedBox(
                                badge = {
                                    if (item.badgeCount > 0) {
                                        Badge(
                                            containerColor = MaterialTheme.colorScheme.error,
                                            contentColor = MaterialTheme.colorScheme.onError
                                        ) {
                                            Text(
                                                text = min(item.badgeCount, 99).toString(),
                                                fontSize = 8.sp
                                            )
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = item.unselectedIcon,
                                    contentDescription = item.title,
                                    tint = unselectedContentColor,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }

                        Text(
                            text = item.title,
                            color = unselectedContentColor,
                            fontSize = 11.sp,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }

                // Декоративные точки между элементами (кроме последнего)
                if (index < items.size - 1) {
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(20.dp)
                            .align(Alignment.CenterVertically)
                    ) {
                        Canvas(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            val dotSpacing = 5.dp.toPx()
                            val dotRadius = 1.5.dp.toPx()

                            for (y in dotRadius.toInt() until size.height.toInt() step dotSpacing.toInt()) {
                                drawCircle(
                                    color = unselectedContentColor.copy(alpha = 0.3f),
                                    radius = dotRadius,
                                    center = Offset(size.width / 2, y.toFloat())
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// Функция для осветления цвета
private fun Color.lighten(factor: Float): Color {
    return Color(
        red = (red * (1 - factor) + factor).coerceIn(0f, 1f),
        green = (green * (1 - factor) + factor).coerceIn(0f, 1f),
        blue = (blue * (1 - factor) + factor).coerceIn(0f, 1f),
        alpha = alpha
    )
}

// Альтернативный более простой вариант с лучшей обработкой нажатий
@Composable
fun SimpleWavyBottomBar(
    items: List<WavyBottomBarItem>,
    currentRoute: String?,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedIndex = items.indexOfFirst { it.route == currentRoute }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(90.dp)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp, top = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            items.forEachIndexed { index, item ->
                val isSelected = currentRoute == item.route
                val interactionSource = remember { MutableInteractionSource() }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = { onItemSelected(item.route) }
                        ),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        val animatedOffset by animateDpAsState(
                            targetValue = if (isSelected) (-22).dp else 0.dp,
                            animationSpec = spring(
                                dampingRatio = 0.6f,
                                stiffness = 300f
                            )
                        )

                        val animatedSize by animateDpAsState(
                            targetValue = if (isSelected) 68.dp else 48.dp,
                            animationSpec = spring(
                                dampingRatio = 0.6f,
                                stiffness = 300f
                            )
                        )

                        Box(
                            modifier = Modifier
                                .size(animatedSize)
                                .offset(y = animatedOffset)
                                .shadow(
                                    elevation = if (isSelected) 12.dp else 0.dp,
                                    shape = CircleShape,
                                    clip = true
                                )
                                .clip(CircleShape)
                                .background(
                                    if (isSelected) {
                                        MaterialTheme.colorScheme.primaryContainer
                                    } else {
                                        Color.Transparent
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.title,
                                tint = if (isSelected) {
                                    MaterialTheme.colorScheme.onPrimaryContainer
                                } else {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                },
                                modifier = Modifier.size(if (isSelected) 30.dp else 22.dp)
                            )

                            // Бейдж
                            if (item.badgeCount > 0) {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(top = 4.dp, end = 4.dp)
                                        .size(18.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.error),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = min(item.badgeCount, 99).toString(),
                                        color = MaterialTheme.colorScheme.onError,
                                        fontSize = 9.sp,
                                        maxLines = 1
                                    )
                                }
                            }
                        }

                        Text(
                            text = item.title,
                            color = if (isSelected) {
                                MaterialTheme.colorScheme.onPrimaryContainer
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            },
                            fontSize = 12.sp,
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.offset(y = animatedOffset * 0.5f)
                        )
                    }
                }
            }
        }

        // Рисуем волнистую линию внизу
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .align(Alignment.BottomCenter)
        ) {
            if (selectedIndex >= 0) {
                val itemWidth = size.width / items.size
                val centerX = itemWidth * (selectedIndex + 0.5f)
                val amplitude = 3.dp.toPx()

                val path = Path().apply {
                    moveTo(0f, size.height)

                    for (x in 0..size.width.toInt() step 2) {
                        val distanceFromCenter = abs(x - centerX)
                        val waveFactor = if (distanceFromCenter < itemWidth * 1.5) {
                            cos((x - centerX) * PI / (itemWidth * 1.5)).toFloat()
                        } else {
                            0f
                        }

                        val y = size.height - (amplitude * waveFactor)
                        lineTo(x.toFloat(), y)
                    }

                    lineTo(size.width, size.height)
                }

                drawPath(
                    path = path,
                    color = Color.Red.copy(alpha = 0.3f),
                    style = Stroke(width = 2.dp.toPx())
                )
            }
        }
    }
}

// Альтернативный вариант с более сложной волнистой формой
@Composable
fun FancyWavyBottomBar(
    items: List<WavyBottomBarItem>,
    currentRoute: String?,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedIndex = items.indexOfFirst { it.route == currentRoute }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(90.dp),
        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 16.dp
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Анимированный фон под шаром
            if (selectedIndex >= 0) {
                val itemWidth = remember { 0f }
                LaunchedEffect(selectedIndex) {
                    // Анимация для фона
                }

                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .align(Alignment.TopCenter)
                ) {
                    val waveCount = 3
                    val amplitude = 15f
                    val itemWidth = size.width / items.size
                    val centerX = itemWidth * (selectedIndex + 0.5f)

                    val path = Path().apply {
                        moveTo(0f, size.height)

                        // Создаем сложную волнистую форму
                        for (x in 0..size.width.toInt() step 2) {
                            val distanceFromCenter = (x - centerX).absoluteValue
                            val waveFactor = if (distanceFromCenter < itemWidth * 2) {
                                cos((x - centerX) * PI / (itemWidth * 2)).toFloat()
                            } else {
                                0f
                            }

                            val wave1 = amplitude * sin(x * waveCount * PI / size.width).toFloat()
                            val wave2 = amplitude * 0.5f * sin(x * (waveCount + 1) * PI / size.width).toFloat()
                            val y = size.height - (wave1 + wave2) * waveFactor

                            lineTo(x.toFloat(), y)
                        }

                        lineTo(size.width, size.height)
                        close()
                    }

                    drawPath(
                        path = path,
                        color = Color.Green.copy(alpha = 0.1f),
                        alpha = 0.7f
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                items.forEachIndexed { index, item ->
                    val isSelected = currentRoute == item.route

                    FancyBottomBarItem(
                        item = item,
                        isSelected = isSelected,
                        onClick = { onItemSelected(item.route) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun FancyBottomBarItem(
    item: WavyBottomBarItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        val animatedOffset by animateDpAsState(
            targetValue = if (isSelected) (-12).dp else 0.dp,
            animationSpec = spring(
                dampingRatio = 0.6f,
                stiffness = 300f
            )
        )

        Box(
            modifier = Modifier
                .size(if (isSelected) 56.dp else 40.dp)
                .offset(y = animatedOffset)
                .shadow(
                    elevation = if (isSelected) 12.dp else 0.dp,
                    shape = CircleShape,
                    clip = true
                )
                .clip(CircleShape)
                .background(
                    if (isSelected) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        Color.Transparent
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                contentDescription = item.title,
                tint = if (isSelected) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                modifier = Modifier.size(if (isSelected) 24.dp else 20.dp)
            )
        }

        Text(
            text = item.title,
            color = if (isSelected) {
                MaterialTheme.colorScheme.onPrimaryContainer
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            },
            fontSize = 11.sp,
            modifier = Modifier.offset(y = animatedOffset * 0.5f)
        )
    }
}

