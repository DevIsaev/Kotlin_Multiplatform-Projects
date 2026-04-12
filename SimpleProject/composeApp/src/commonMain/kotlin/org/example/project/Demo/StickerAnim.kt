package org.example.project.Demo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.example.project.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi

@Composable
expect fun TgsSticker(
    bytes: ByteArray,
    modifier: Modifier = Modifier,
    loop: Boolean = true
)

@OptIn(ExperimentalResourceApi::class)
@Composable
fun StickerFromResources(
    fileName: String,       // "AnimatedSticker.tgs" или "okak.webm"
    modifier: Modifier = Modifier,
    size: Dp = 200.dp,
    loop: Boolean = true
) {
    var bytes by remember { mutableStateOf<ByteArray?>(null) }

    LaunchedEffect(fileName) {
        bytes = Res.readBytes("files/$fileName")
    }

    bytes?.let { data ->
        TgsSticker(
            bytes = data,
            modifier = modifier,
            loop = loop
        )
    }
}