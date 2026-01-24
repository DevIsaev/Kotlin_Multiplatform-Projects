package org.example.project.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Screen1(onClickToScreen2: () -> Unit, onClickToScreen3: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Blue),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "экран1", fontStyle = FontStyle.Italic, fontSize = 30.sp)
        Spacer(modifier = Modifier.height(30.dp))
        Button(onClick = onClickToScreen2) {
            Text("откр-экран2")
        }
        Button(onClick = onClickToScreen3) {
            Text("откр-экран3")
        }
    }
}

@Composable
fun Screen2(onClickToScreen1: () -> Unit, onClickToScreen3: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Yellow),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "экран2", fontStyle = FontStyle.Italic, fontSize = 30.sp)
        Spacer(modifier = Modifier.height(30.dp))
        Button(onClick = onClickToScreen1) {
            Text("откр-экран1")
        }
        Button(onClick = onClickToScreen3) {
            Text("откр-экран3")
        }
    }
}

@Composable
fun Screen3(
    onClickToScreen1: () -> Unit,
    onClickToScreen2: () -> Unit,
    openDrawer: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Red),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "экран3", fontStyle = FontStyle.Italic, fontSize = 30.sp)
        Spacer(modifier = Modifier.height(30.dp))
        Button(onClick = onClickToScreen1) {
            Text("откр-экран1")
        }
        Button(onClick = onClickToScreen2) {
            Text("откр-экран2")
        }
        Button(onClick = openDrawer) {
            Text("open drawer")
        }
    }
}