package org.example.project.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose.onPrimaryDark
import org.example.project.resources.fonts.Fonts.Zapussans
import org.example.project.resources.fonts.Fonts.consolamono

@Composable
fun SimpleScreen(){
    val topPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val bottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    Column(
        Modifier.fillMaxSize().padding(bottom = bottomPadding, top=topPadding)
    ){
        Text("Верхняя сторона", fontSize = 25.sp, modifier = Modifier.weight(1f), fontFamily = Zapussans(), fontWeight = FontWeight.Medium, fontStyle = FontStyle.Italic)
        Text("Нижняя сторона", fontSize = 25.sp,  fontFamily = consolamono())
    }
}