package org.example.project.Demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
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