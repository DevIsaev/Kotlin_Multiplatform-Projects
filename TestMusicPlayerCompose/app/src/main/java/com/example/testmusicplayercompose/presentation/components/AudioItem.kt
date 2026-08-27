package com.example.testmusicplayercompose.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.domain.models.AudioDomain
import com.example.infrastructure.R
import com.example.testmusicplayercompose.presentation.MainViewModel
import com.example.testmusicplayercompose.ui.presentation.Dark
import com.example.testmusicplayercompose.ui.presentation.White

@Composable
fun AudioItem(
    audio: AudioDomain,
    onAudioClick: () -> Unit,
    onToggleFavouriteClick: () -> Unit,
    cropTrackTitle: () -> String
) {
    var flippedArrow by remember { mutableStateOf(false) }
    val scaleYArrow by animateFloatAsState(targetValue = if (flippedArrow) -1f else 1f, label = "")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(2.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Dark),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.5f).clickable {
                    onAudioClick()
                }
        ) {
            AudioCover(audio = audio)
            Column(modifier = Modifier
                .fillMaxHeight()
                .padding(5.dp), verticalArrangement = Arrangement.SpaceEvenly) {
                Text(text = cropTrackTitle(), style = TextStyle(color = White))
                Text(text = audio.artist ?: "", style = TextStyle(color = White, fontWeight = FontWeight.Thin))
            }
        }
        Row(modifier = Modifier
            .weight(0.11f)
            .fillMaxHeight(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End) {
            Image(
                painter = painterResource(id =
                    if(audio.isFavourite) com.example.testmusicplayercompose.R.drawable.mdi_favourite
                    else com.example.testmusicplayercompose.R.drawable.mdi_favourite_border),
                contentDescription = "Favourites",
                modifier = Modifier
                    .width(30.dp)
                    .padding(end = 15.dp)
                    .clickable {
                        onToggleFavouriteClick()
                    }
            )
            Image(
                painter = painterResource(id = com.example.testmusicplayercompose.R.drawable.bi_eye_fill), contentDescription = "Open",
                modifier = Modifier
                    .width(30.dp)
                    .padding(end = 15.dp)
                    .graphicsLayer(rotationX = 180f, scaleY = scaleYArrow)
                    .clickable {
                        flippedArrow = !flippedArrow
                    }
            )
        }
    }
}