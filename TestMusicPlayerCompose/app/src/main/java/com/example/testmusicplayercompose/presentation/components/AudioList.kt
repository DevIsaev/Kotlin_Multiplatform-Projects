package com.example.testmusicplayercompose.presentation.components


import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.domain.models.AudioDomain
import com.example.testmusicplayercompose.presentation.MainViewModel

@Composable
fun AudioList(audios: List<AudioDomain>, viewModel: MainViewModel = viewModel()) {
    LazyColumn(modifier = Modifier.padding(bottom = 50.dp)) {
        items(audios) { audio ->
            AudioItem(
                audio,
                { viewModel.onAudioClick(audio) },
                { viewModel.onToggleFavouriteClick(audio) },
                 {viewModel.cropTrackTitle(audio.title)}
            )
        }
    }
}