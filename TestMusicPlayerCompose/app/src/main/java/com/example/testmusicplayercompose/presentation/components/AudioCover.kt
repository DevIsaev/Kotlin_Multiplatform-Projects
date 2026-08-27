package com.example.testmusicplayercompose.presentation.components
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.domain.models.AudioDomain
import com.example.testmusicplayercompose.R

@Composable
fun AudioCover(audio: AudioDomain){
    val uri=audio.album?.imgUri
    val isPlaying=audio.isPlaying
    val isOnPause=audio.isOnPause

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(if(isPlaying) R.raw.audio_waves_animation else R.raw.audio_play_animation))

    Box(modifier = Modifier.fillMaxHeight().aspectRatio(1f).padding(3.dp).clip(RoundedCornerShape(10.dp))) {
        Image(
            painter = if (uri != null) rememberAsyncImagePainter(uri) else painterResource(R.drawable.group),
            contentDescription = "",
            modifier = Modifier.matchParentSize().clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.Crop
        )

        if(isPlaying||isOnPause){
            Box(Modifier.matchParentSize().background(Color.Black.copy(0.5f))){
                LottieAnimation(
                    composition = composition,
                    isPlaying = true,
                    iterations = LottieConstants.IterateForever
                )
            }
        }
    }
}