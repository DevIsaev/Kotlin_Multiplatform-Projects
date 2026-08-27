package com.example.testmusicplayercompose.presentation.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.testmusicplayercompose.R
import com.example.testmusicplayercompose.ui.presentation.White

@Composable
fun AppTitle(){
    Text(stringResource(id = R.string.app_name), style = TextStyle(color=White, fontSize = 26.sp,
        fontFamily=FontFamily.SansSerif, fontWeight=FontWeight.Thin))
}