package com.example.testmusicplayercompose.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testmusicplayercompose.R
import com.example.testmusicplayercompose.ui.presentation.White

@Composable
fun NoTracksFound() {
    Text(
        modifier = Modifier.padding(5.dp),
        text = stringResource(id = R.string.no_tracks_found),
        style = TextStyle(
            color = White,
            fontSize = 20.sp,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Thin
        )
    )
}