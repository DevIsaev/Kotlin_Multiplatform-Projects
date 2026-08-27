package com.example.testmusicplayercompose.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

@Composable
fun NavigationItem(resourceId: Int, onClick: () -> Unit) {
    Tab(selected = true, onClick = onClick, text = {
        Text(
            text = stringResource(id = resourceId),
            style = TextStyle(
                color = MaterialTheme.colorScheme.primary,
                fontSize = 13.sp,
            ),
        )
    })
}