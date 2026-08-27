package com.example.testmusicplayercompose.presentation

import android.service.quicksettings.Tile
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.AudioPlayer
import com.example.domain.AudioRepository
import com.example.domain.Lists
import com.example.domain.models.AudioDomain
import com.example.domain.usecases.Init
import com.example.domain.usecases.ToggleAudioPlayback
import com.example.domain.usecases.ToggleFavourites
import com.example.infrastructure.utils.IsFileAvailableByUri
import com.example.testmusicplayercompose.utils.CropString
import com.example.testmusicplayercompose.utils.SingleEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@HiltViewModel
class MainViewModel @Inject constructor(private var player: AudioPlayer, private var repository: AudioRepository, private var init: Init,
    private var toggleAudioPlayback: ToggleAudioPlayback, private var toggleFavourites: ToggleFavourites, private var isFileAvailableByUri: IsFileAvailableByUri):
    ViewModel() {
    companion object{
        const val MAX_TRACK_TITLE_LENGHT=27
    }
    private var _audioOnDeviceList= MutableStateFlow<List<AudioDomain>>(emptyList())
    var audioOnDeviceList: StateFlow<List<AudioDomain>> = _audioOnDeviceList

    private var _audioOnFavouritesList= MutableStateFlow<List<AudioDomain>>(emptyList())
    var audioOnFavouritesList: StateFlow<List<AudioDomain>> = _audioOnFavouritesList

    private var _permissionRequired= MutableStateFlow(false)
    var permissionRequired:StateFlow<Boolean> = _permissionRequired

    private var _toastError= MutableStateFlow<SingleEvent<Int>?>(null)
    var toastError:StateFlow<SingleEvent<Int>?> = _toastError

    private var _isLoading= MutableStateFlow(true)
    var isLoading:StateFlow<Boolean> = _isLoading

    private var currentTrackIndex=-1
    private var currentTrack: AudioDomain?=null
    private var currentListType: Lists= Lists.ON_DEVICE
    private var playlist:List<AudioDomain> = emptyList()

    init {
        player.setOnTrackEndCallback { onTrackEnded() }
    }

    fun checkPermissions(isPermissionGranted: Boolean){
        if(isPermissionGranted) loadData() else _permissionRequired.value=true
    }

    private fun loadData() {
        viewModelScope.launch {
            init().launchIn(this)
        }
        viewModelScope.launch {
            repository.getAudioFromDB().onEach { list->
                onListCollect(list,Lists.ON_DEVICE)
                if(_isLoading.value) playlist=list
                _isLoading.value=false
            }
                .launchIn(this)
        }

        viewModelScope.launch {
            repository.getFavouriteAudio().onEach { list->
                onListCollect(list,Lists.FAVOURITES)
            }
                .launchIn(this)
        }
    }

    fun onTrackEnded() {
        if(currentTrack != null) {
            val currentList = getList(currentListType)
            val onDeviceList = getList(Lists.ON_DEVICE)
            val index = getCurrentTrackIndexFromList(currentList)
            val onDeviceIndex = getCurrentTrackIndexFromList(onDeviceList)
            currentTrackIndex = if(playlist.find { it.id == currentTrack?.id } == null) {
                playlist = onDeviceList
                val idx = if(onDeviceIndex in onDeviceList.indices) onDeviceIndex else 0
                togglePlayPause(onDeviceList[idx], isOnTrackEnded = true, currentTrackNotInPlaylist = true)
                idx
            } else if(index in playlist.indices && currentList.isNotEmpty()) {
                togglePlayPause(playlist[index], isOnTrackEnded = true)
                index
            } else if((index == currentList.size && currentList.isNotEmpty()) || playlist.size == 1) {
                togglePlayPause(playlist[0], isOnTrackEnded = true)
                0
            } else 0
        } else {
            viewModelScope.launch {
                repository.deleteAudioInDB(getList(Lists.ON_DEVICE).filter { audio -> !isFileAvailableByUri(audio.uri) })
            }
        }
    }

    fun onListCollect(list: List<AudioDomain>, listType: Lists) {
        val updatedList = if(currentTrack != null) {
            list.map {
                if(it.id == currentTrack?.id) {
                    return@map it.copy(isPlaying = currentTrack!!.isPlaying, isOnPause = currentTrack!!.isOnPause)
                } else it
            }
        } else list
        if(listType == Lists.ON_DEVICE) {
            updateLists(updatedOnDeviceList = updatedList)
        } else {
            updateLists(updatedFavouritesList = updatedList)
        }
    }

    private fun togglePlayPause(audio: AudioDomain,isOnTrackEnded: Boolean?=false,stopCurrentTrack:Boolean?=false,currentTrackNotInPlaylist: Boolean?=false) {
        var triple=toggleAudioPlayback(getList(Lists.ON_DEVICE),getList(Lists.FAVOURITES),audio,currentListType,
            {_toastError.value == SingleEvent("")}, isOnTrackEnded==true, stopCurrentTrack==true, currentTrackNotInPlaylist==true)

        updateLists(triple.first,triple.second)
        currentTrack=triple.third

    }

    private fun getCurrentTrackIndexFromList(list:List<AudioDomain>): Int{
        return  list.indexOf(list.find { it.id==currentTrack?.id })+1
    }

    fun onAudioClick(clickedAudio: AudioDomain){
        currentTrackIndex=getList(currentListType).indexOf(clickedAudio)
        togglePlayPause(clickedAudio)
    }

    fun onToggleFavouriteClick(audio: AudioDomain){
        viewModelScope.launch { toggleFavourites(audio) }
    }

    fun setListType(listType:Lists){ 
        currentListType=listType
        playlist=getList(currentListType)
    }

    private fun getList(listType: Lists): List<AudioDomain> {
        return if (listType == Lists.ON_DEVICE) _audioOnDeviceList.value
        else _audioOnFavouritesList.value
    }
    private fun updateLists(updatedOnDeviceList:List<AudioDomain>?=null, updatedFavouritesList:List<AudioDomain>?=null){
        if(updatedOnDeviceList!=null){
            _audioOnDeviceList.value=updatedOnDeviceList
            if(currentListType==Lists.ON_DEVICE) playlist=updatedOnDeviceList
        }
        if(updatedFavouritesList!=null){
            _audioOnFavouritesList.value=updatedFavouritesList
            if(currentListType==Lists.FAVOURITES) playlist=updatedFavouritesList
        }
    }

    fun cropTrackTitle(title: String):String{
        return CropString(title,MAX_TRACK_TITLE_LENGHT)
    }


    override fun onCleared() {
        super.onCleared()
        player.release()
    }
}