package org.example.project.Demo.News

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Поисковая строка
@Composable
fun NewsSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value            = query,
        onValueChange    = onQueryChange,
        placeholder      = { Text("Поиск новостей", fontSize = 14.sp) },
        leadingIcon      = {
            Icon(
                Icons.Default.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        singleLine = true,
        shape      = RoundedCornerShape(12.dp),
        colors     = OutlinedTextFieldDefaults.colors(
            focusedContainerColor   = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedBorderColor      = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor    = MaterialTheme.colorScheme.outline,
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
    )
}

// Горизонтальная лента всех тегов с выбором
@Composable
fun NewsTagsRow(
    tags: List<HashTag>,
    selectedTag: String?,
    onTagSelected: (HashTag) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment     = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        tags.forEach { tag ->
            NewsTagChip(
                tag        = tag,
                isSelected = tag.label == selectedTag,
                onClick    = { onTagSelected(tag) }
            )
        }
    }
}

// Чип тега нажимаемый, с выделением
@Composable
private fun NewsTagChip(
    tag: HashTag,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val bg      = if (isSelected) tag.color else tag.color.copy(alpha = 0.75f)
    val border  = if (isSelected) 2.dp else 0.dp

    Surface(
        onClick      = onClick,
        shape        = RoundedCornerShape(20.dp),
        color        = bg,
        tonalElevation = if (isSelected) 4.dp else 0.dp,
        modifier     = Modifier
            .height(30.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(horizontal = 12.dp)
        ) {
            Text(
                text       = tag.label,
                color      = Color.White,
                fontSize   = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                maxLines   = 1,
            )
        }
    }
}

// Заголовок секции
@Composable
fun NewsSectionLabel(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text       = title,
        style      = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color      = MaterialTheme.colorScheme.onBackground,
        modifier   = modifier
    )
}

// Сетка карточек 2 колонки
@Composable
fun NewsGrid(
    items: List<NewsItem>,
    modifier: Modifier = Modifier,
) {
    if (items.isEmpty()) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .fillMaxWidth()
                .height(100.dp)
        ) {
            Text(
                text  = "Ничего не найдено",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 14.sp
            )
        }
        return
    }

    val rows = items.chunked(2)
    Column(modifier = modifier) {
        rows.forEach { row ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                row.forEach { item ->
                    NewsCard(
                        item     = item,
                        modifier = Modifier.weight(1f)
                    )
                }
                if (row.size == 1) Spacer(Modifier.weight(1f))
            }
            Spacer(Modifier.height(12.dp))
        }
    }
}

// Карточка новости
@Composable
private fun NewsCard(
    item: NewsItem,
    modifier: Modifier = Modifier,
) {
    Card(
        shape  = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = modifier
    ) {
        Column {
            NewsImagePlaceholder(isVideo = item.isVideo)
            Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)) {
                Text(
                    text       = item.title,
                    style      = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color      = MaterialTheme.colorScheme.onSurface,
                    maxLines   = 2,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text  = item.date,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

// Заглушка картинки / видео
@Composable
private fun NewsImagePlaceholder(isVideo: Boolean = false) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16f / 10f)
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        if (isVideo) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.8f))
            ) {
                Text("▶", color = Color.White, fontSize = 16.sp)
            }
        }
    }
}