package com.example.testmusicplayercompose.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.domain.Lists
import com.example.testmusicplayercompose.R
import com.example.testmusicplayercompose.ui.presentation.Transparent
import kotlinx.coroutines.launch

@Composable
fun Pager() {
    Column(modifier = Modifier
        .clip(RoundedCornerShape(5.dp))
        .padding(top = 5.dp)) {
        val tabList = listOf(R.string.music_on_device, R.string.favourites)
        val pagerState = rememberPagerState { 2 }
        val tabIndex = pagerState.currentPage
        val coroutineScope = rememberCoroutineScope()

        TabRow(
            selectedTabIndex = tabIndex,
            containerColor = Transparent,
            indicator = { pos ->
                SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(pos[tabIndex]),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        ) {
            tabList.forEachIndexed { index, resourceId ->
                NavigationItem(resourceId = resourceId) {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }

                }
            }
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp, start = 5.dp, end = 5.dp),
            verticalAlignment = Alignment.Top
        ) { index ->
            Content(listType =
                when(index) {
                    0 -> Lists.ON_DEVICE
                    1 -> Lists.FAVOURITES
                    else -> Lists.ON_DEVICE
                })
        }
    }
}