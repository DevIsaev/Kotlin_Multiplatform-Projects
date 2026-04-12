package org.example.project.Demo

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
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
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.fuyuz.svgicon.SvgIcon
import io.github.fuyuz.svgicon.core.Svg
import org.example.project.resources.colors.Blue
import org.example.project.resources.colors.DarkBlue
import org.example.project.resources.colors.DarkOrange
import org.example.project.resources.colors.Orange
import org.jetbrains.compose.resources.painterResource
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.ui.text.style.TextOverflow

//bottombar

data class BottomBarItem(
    val title: String,
    val selectedIcon: Svg,
    val unselectedIcon: Svg,
    val route: String
)
@Composable
fun DemoBottomBar(
    items: List<BottomBarItem>,
    currentRoute: String?,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier.height(80.dp),
        containerColor = Color.Green,
        tonalElevation = 8.dp
    ) {
        items.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                selected = selected,
                onClick = { onItemSelected(item.route) },
                icon = {
                    SvgIcon(
                        svg = if (selected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.title,
                        tint = Color.Gray,
                        modifier = Modifier.size(30.dp),
                    )
                },
                label = {
                    Text(item.title, style = MaterialTheme.typography.labelSmall)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurface,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    }
}

//smooth
data class SmoothBottomNavItem(
    val title: String,
    val selectedIcon: Svg,
    val unselectedIcon: Svg,
    val route: String
)
// Цвета навбара
private val NavBarBackground = Color(0xFF3D35C8)
private val NavItemActiveBackground = Color(0xFF5548D9)
private val NavItemActiveContent = Color.White
private val NavItemInactiveContent = Color(0xFF8B85E8)
@Composable
fun SmoothBottomNavBar(
    items: List<SmoothBottomNavItem>,
    currentRoute: String,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .shadow(
                elevation = 20.dp,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                ambientColor = NavBarBackground.copy(alpha = 0.4f),
                spotColor = NavBarBackground.copy(alpha = 0.4f)
            )
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(NavBarBackground)
            .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                SmoothNavItem(
                    item = item,
                    isSelected = currentRoute == item.route,
                    onClick = { onItemSelected(item.route) }
                )
            }
        }
    }
}

@Composable
private fun SmoothNavItem(
    item: SmoothBottomNavItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    // Анимация ширины — активный элемент расширяется
    val itemWidth by animateDpAsState(
        targetValue = if (isSelected) 110.dp else 44.dp,
        animationSpec = tween(durationMillis = 350),
        label = "itemWidth"
    )

    // Анимация фона
    val backgroundAlpha by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0f,
        animationSpec = tween(durationMillis = 350),
        label = "bgAlpha"
    )

    // Анимация цвета иконки
    val iconTint by animateColorAsState(
        targetValue = if (isSelected) NavItemActiveContent else NavItemInactiveContent,
        animationSpec = tween(durationMillis = 350),
        label = "iconTint"
    )

    // Анимация появления текста
    val textAlpha by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0f,
        animationSpec = tween(durationMillis = if (isSelected) 250 else 100, delayMillis = if (isSelected) 150 else 0),
        label = "textAlpha"
    )

    Box(
        modifier = Modifier
            .width(itemWidth)
            .height(44.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(NavItemActiveBackground.copy(alpha = backgroundAlpha))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            SvgIcon(
                svg = if (isSelected) item.selectedIcon else item.unselectedIcon,
                contentDescription = item.title,
                tint = Color.Gray,
                modifier = Modifier.size(21.dp),
            )

            // Текст появляется только у активного элемента
            if (textAlpha > 0f) {
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = item.title,
                    color = NavItemActiveContent,
                    fontSize = 12.sp,
                    lineHeight = 12.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = (-0.2).sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .widthIn(max = 60.dp)
                        .graphicsLayer { alpha = textAlpha }
                )
            }
        }
    }
}



//кнопки
object ButtonStyles {
    // Синий стиль
    @Composable
    fun BlueButton(
        text: String,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        size: ButtonSize = ButtonSize.NORMAL,
        icon: Svg? = null,
        iconOnly: Boolean = false,
        width: Dp? = null,
        height: Dp? = null,
        fontSize: TextUnit? = null,
        cornerRadius: Dp? = null,
        enabled: Boolean = true
    ) {
        DemoButton(
            text = text,
            onClick = onClick,
            modifier = modifier,
            backgroundColor = Blue,
            contentColor = Color.White,
            size = size,
            icon = icon,
            iconOnly = iconOnly,
            width = width,
            height = height,
            fontSize = fontSize,
            cornerRadius = cornerRadius,
            enabled = enabled
        )
    }
    // Темно-синий стиль
    @Composable
    fun DarkBlueButton(
        text: String,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        size: ButtonSize = ButtonSize.NORMAL,
        icon: Svg? = null,
        iconOnly: Boolean = false,
        width: Dp? = null,
        height: Dp? = null,
        fontSize: TextUnit? = null,
        cornerRadius: Dp? = null,
        enabled: Boolean = true
    ) {
       DemoButton(
            text = text,
            onClick = onClick,
            modifier = modifier,
            backgroundColor = DarkBlue,
            contentColor = Color.White,
            size = size,
            icon = icon,
            iconOnly = iconOnly,
            width = width,
            height = height,
            fontSize = fontSize,
            cornerRadius = cornerRadius,
            enabled = enabled
        )
    }

    // Оранжевый стиль
    @Composable
    fun OrangeButton(
        text: String,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        size: ButtonSize = ButtonSize.NORMAL,
        icon: Svg? = null,
        iconOnly: Boolean = false,
        width: Dp? = null,
        height: Dp? = null,
        fontSize: TextUnit? = null,
        cornerRadius: Dp? = null,
        enabled: Boolean = true
    ) {
        DemoButton(
            text = text,
            onClick = onClick,
            modifier = modifier,
            backgroundColor = Orange,
            contentColor = Color.White,
            size = size,
            icon = icon,
            iconOnly = iconOnly,
            width = width,
            height = height,
            fontSize = fontSize,
            cornerRadius = cornerRadius,
            enabled = enabled
        )
    }

    // Коричневый стиль
    @Composable
    fun BrownButton(
        text: String,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        size: ButtonSize = ButtonSize.NORMAL,
        icon: Svg? = null,
        iconOnly: Boolean = false,
        width: Dp? = null,
        height: Dp? = null,
        fontSize: TextUnit? = null,
        cornerRadius: Dp? = null,
        enabled: Boolean = true
    ) {
        DemoButton(
            text = text,
            onClick = onClick,
            modifier = modifier,
            backgroundColor = DarkOrange,
            contentColor = Color.White,
            size = size,
            icon = icon,
            iconOnly = iconOnly,
            width = width,
            height = height,
            fontSize = fontSize,
            cornerRadius = cornerRadius,
            enabled = enabled
        )
    }
}
enum class ButtonSize(
    val height: Dp,
    val fontSize: TextUnit,
    var horizontalPadding: Dp,
    val cornerRadius: Dp
) {
    LARGE(height = 56.dp, fontSize = 16.sp, horizontalPadding = 32.dp, cornerRadius = 28.dp),
    MEDIUM(height = 48.dp, fontSize = 15.sp, horizontalPadding = 28.dp, cornerRadius = 24.dp),
    NORMAL(height = 40.dp, fontSize = 14.sp, horizontalPadding = 24.dp, cornerRadius = 20.dp),
    SMALL(height = 32.dp, fontSize = 13.sp, horizontalPadding = 20.dp, cornerRadius = 16.dp),
    TINY(height = 28.dp, fontSize = 12.sp, horizontalPadding = 16.dp, cornerRadius = 14.dp)
}

@Composable
fun DemoButton(
    text: String = "",
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFF3D5A80),
    contentColor: Color = Color.White,
    size: ButtonSize = ButtonSize.NORMAL,
    cornerRadius: Dp? = null,
    height: Dp? = null,
    width: Dp? = null,
    fontSize: TextUnit? = null,
    icon: Svg? = null,
    iconOnly: Boolean = false,
    enabled: Boolean = true
) {
    val actualHeight = height ?: size.height
    val actualFontSize = fontSize ?: size.fontSize
    val actualCornerRadius = cornerRadius ?: size.cornerRadius
    val actualPadding = if (iconOnly) PaddingValues(0.dp) else PaddingValues(horizontal = size.horizontalPadding)

    Button(
        onClick = onClick,
        modifier = modifier
            .height(actualHeight)
            .then(
                if (width != null) Modifier.width(width)
                else if (iconOnly) Modifier.width(actualHeight) // Квадратная кнопка для иконки
                else Modifier
            ),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = contentColor,
            disabledContainerColor = backgroundColor.copy(alpha = 0.5f),
            disabledContentColor = contentColor.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(actualCornerRadius),
        contentPadding = actualPadding,
        enabled = enabled
    ) {
        when {
            iconOnly && icon != null -> {
                SvgIcon(
                    svg = icon,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
            icon != null && text.isNotEmpty() -> {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = text,
                        fontSize = actualFontSize,
                        fontWeight = FontWeight.Medium
                    )
                    SvgIcon(
                        svg = icon,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            else -> {
                Text(
                    text = text,
                    fontSize = actualFontSize,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}


//текстовое поле с настраиваемыми параметрами
class DemoTextField(
    private val placeholderText: String,
    private val cornerRadius: Dp = 14.dp,
    private val height: Dp = 28.dp,
    private val width: Dp? = null,
    private val isPassword: Boolean = false,
    private val keyboardType: KeyboardType = KeyboardType.Text
) {

    @Composable
    fun Create(
        value: String,
        onValueChange: (String) -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        singleLine: Boolean = true,
        leadingIcon: @Composable (() -> Unit)? = null,
        trailingIcon: @Composable (() -> Unit)? = null,
        colors: TextFieldColors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        )
    ) {
        var passwordVisible by remember { mutableStateOf(false) }

        val finalModifier = if (width != null) {
            modifier
                .width(width)
                .height(height)
        } else {
            modifier
                .fillMaxWidth()
                .height(height)
        }

        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = finalModifier,
            enabled = enabled,
            singleLine = singleLine,
            placeholder = {
                Text(
                    text = placeholderText,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            },
            leadingIcon = leadingIcon,
            trailingIcon = {
                if (isPassword) {
                    PasswordVisibilityIcon(
                        passwordVisible = passwordVisible,
                        onToggle = { passwordVisible = !passwordVisible }
                    )
                } else {
                    trailingIcon?.invoke()
                }
            },
            visualTransformation = if (isPassword && !passwordVisible) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = if (isPassword) KeyboardType.Password else keyboardType
            ),
            colors = colors,
            shape = RoundedCornerShape(cornerRadius)
        )
    }

    @Composable
    private fun PasswordVisibilityIcon(
        passwordVisible: Boolean,
        onToggle: () -> Unit
    ) {
        IconButton(onClick = onToggle) {
            Icon(
                imageVector = if (passwordVisible) {
                    Icons.Filled.Visibility
                } else {
                    Icons.Filled.VisibilityOff
                },
                contentDescription = if (passwordVisible) {
                    "Скрыть пароль"
                } else {
                    "Показать пароль"
                },
                tint = Color.Gray
            )
        }
    }
}

// Builder-паттерн для более удобного создания
class CustomTextFieldBuilder {
    private var placeholderText: String = ""
    private var cornerRadius: Dp = 28.dp
    private var height: Dp = 56.dp
    private var width: Dp? = null
    private var isPassword: Boolean = false
    private var keyboardType: KeyboardType = KeyboardType.Text

    fun placeholder(text: String) = apply { this.placeholderText = text }
    fun cornerRadius(radius: Dp) = apply { this.cornerRadius = radius }
    fun height(height: Dp) = apply { this.height = height }
    fun width(width: Dp) = apply { this.width = width }
    fun asPassword() = apply {
        this.isPassword = true
        this.keyboardType = KeyboardType.Password
    }
    fun keyboardType(type: KeyboardType) = apply { this.keyboardType = type }

    fun build() = DemoTextField(
        placeholderText = placeholderText,
        cornerRadius = cornerRadius,
        height = height,
        width = width,
        isPassword = isPassword,
        keyboardType = keyboardType
    )
}
