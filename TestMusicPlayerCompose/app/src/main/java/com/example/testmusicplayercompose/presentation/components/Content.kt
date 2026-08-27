package com.example.testmusicplayercompose.presentation.components

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.testmusicplayercompose.presentation.MainViewModel
import com.example.domain.Lists
import com.example.testmusicplayercompose.R
import com.example.testmusicplayercompose.ui.presentation.White

@Composable
fun Content(viewModel: MainViewModel= viewModel(), listType: Lists) {
    val audioOnDeviceList by viewModel.audioOnDeviceList.collectAsState()
    val audioFavouritesList by viewModel.audioOnFavouritesList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isInitializing by viewModel.isLoading.collectAsState()

    val currentList = when(listType) {
        Lists.ON_DEVICE -> {
            Log.e("TAG", "ON DEVICE")
            viewModel.setListType(listType)
            audioOnDeviceList
        }
        Lists.FAVOURITES -> {
            Log.e("TAG", "FAVOURITES")
            viewModel.setListType(listType)
            audioFavouritesList
        }
    }
    AudioList(currentList)
    Column {
        if(isLoading || isInitializing) {
            Loader()
        } else if(currentList.isEmpty()) {
            NoTracksFound()
        }

        if(isInitializing && audioOnDeviceList.isEmpty() && audioFavouritesList.isEmpty()) {
            Text(
                text = stringResource(id = R.string.initializing),
                style = TextStyle(
                    color = White,
                    fontSize = 18.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Thin
                )
            )
        }
    }

}