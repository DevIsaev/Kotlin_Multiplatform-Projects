package org.example.project.Demo.News

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.rememberScrollState as vScrollState

@Composable
fun demoNews() {
    var searchQuery by remember { mutableStateOf("") }
    var selectedTag by remember { mutableStateOf<String?>(null) }

    val filteredNews = remember(searchQuery, selectedTag) {
        demoNewsList.filter { item ->
            val matchesSearch = searchQuery.isBlank() ||
                    item.title.contains(searchQuery, ignoreCase = true)
            val matchesTag = selectedTag == null ||
                    item.tags.any { it.equals(selectedTag, ignoreCase = true) }
            matchesSearch && matchesTag
        }
    }

    val filteredVideos = remember(selectedTag) {
        demoVideoList.filter { item ->
            selectedTag == null ||
                    item.tags.any { it.equals(selectedTag, ignoreCase = true) }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(vScrollState())
            .padding(bottom = 16.dp)
    ) {
        // Поиск
        NewsSearchBar(
            query         = searchQuery,
            onQueryChange = { searchQuery = it },
            modifier      = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        )



        Row(Modifier.padding(vertical = 8.dp)){
            // Секция Новости
            NewsSectionLabel(
                title    = "Новости",
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            // Горизонтальная прокрутка всех тегов
            NewsTagsRow(
                tags           = demoTags,
                selectedTag    = selectedTag,
                onTagSelected  = { tag ->
                    selectedTag = if (selectedTag == tag.label) null else tag.label
                },
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        NewsGrid(
            items       = filteredNews,
            modifier    = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(Modifier.height(20.dp))

        // Секция Видео
        NewsSectionLabel(
            title    = "Видео",
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        NewsGrid(
            items    = filteredVideos,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}