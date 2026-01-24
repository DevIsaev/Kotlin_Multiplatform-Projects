package org.example.project.UI

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.example.project.resources.fonts.Fonts.Zapussans

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopBar(
    title: String,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable () -> Unit = {},
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(),
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        },
        navigationIcon = navigationIcon,
        colors = colors,
        modifier = modifier
    )
}

// Вариант с дополнительными параметрами
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopBarWithActions(
    title: String,
    onBackClick: (() -> Unit)? = null,
    actionIcon: @Composable (() -> Unit)? = null,
    actionOnClick: (() -> Unit)? = null
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                fontFamily = Zapussans(),
                fontWeight = FontWeight.ExtraBold,
                style = MaterialTheme.typography.headlineSmall
            )
        },
        navigationIcon = {
            if (onBackClick != null) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ViewList,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        contentDescription = "Back"
                    )
                }
            }
        },
        actions = {
            if (actionIcon != null && actionOnClick != null) {
                IconButton(onClick = actionOnClick) {
                    actionIcon()
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    )
}


@Composable
fun AnimatedTopBar(
    title: String,
    scrollState: LazyListState, // Изменено с ScrollState на LazyListState
    onBackClick: (() -> Unit)? = null
) {
    val showShadow by remember {
        derivedStateOf {
            scrollState.firstVisibleItemIndex > 0 || scrollState.firstVisibleItemScrollOffset > 0
        }
    }

    val elevation by animateDpAsState(
        targetValue = if (showShadow) 4.dp else 0.dp,
        label = "topBarElevation"
    )

    CustomTopBar(
        title = title,
        navigationIcon = {IconButton(onClick = {}) { Icon(Icons.Default.ArrowBack, "Назад")}},
        modifier = Modifier.shadow(elevation)
    )
}