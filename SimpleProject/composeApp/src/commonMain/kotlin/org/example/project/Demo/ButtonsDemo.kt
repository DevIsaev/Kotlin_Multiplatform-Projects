package org.example.project.Demo

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.fuyuz.svgicon.SvgIcon
import org.example.project.resources.fonts.Fonts.Zapussans

@Composable
fun buttonDemo(){
    SvgIcon(
        svg = SimpleProject.icons.Icons.Person,
        contentDescription = "SVG ICON",
        tint = Color.Gray,
        modifier = Modifier.size(96.dp),
    )
    Text("TEXT", fontFamily = Zapussans())
}