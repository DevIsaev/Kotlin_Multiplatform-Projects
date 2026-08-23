package com.example.domain.usecases

import com.example.domain.AudioPlayer
import com.example.domain.Lists
import com.example.domain.logger.Logger
import com.example.domain.models.AudioDomain
import com.example.domain.utils.IsFileAlailableByUri

class ToggleAudioPlayback(private var audioPlayer: AudioPlayer, private var isFileAlailableByUri: IsFileAlailableByUri, private var logger: Logger) {
    operator fun invoke(
        onDeviceLists: List<AudioDomain>,
        favouriteList: List<AudioDomain>,
        selectedAudio: AudioDomain,
        currentListType: Lists,
        onError:()->Unit,
        isOnTrackEnd: Boolean,
        stopCurrentTrack: Boolean,
        currentTrackNonInPlaylist:Boolean):Triple <List<AudioDomain>,List<AudioDomain>, AudioDomain?> {
        var currentTrack: AudioDomain? = null
        var updatedOnDeviceList = onDeviceLists.map { audio ->
            if (audio.id == selectedAudio.id) {
                if(!isFileAlailableByUri(audio.uri)) { onError()
                    return@map audio.copy(isPlaying = true, isOnPause = false)}
                if(stopCurrentTrack) { return@map audio.copy(isPlaying = false, isOnPause = false) }

                var willPlay=if(isOnTrackEnd&&(if(currentListType==Lists.ON_DEVICE) onDeviceLists.size else favouriteList.size)==1&&!currentTrackNonInPlaylist)
                    audio.isPlaying else !audio.isPlaying
                var willBeOnPause=audio.isPlaying&&!isOnTrackEnd
                var wasOnPause=audio.isOnPause

                if(willPlay&&wasOnPause) {
                    audioPlayer.play()
                }
                    else if (willPlay) {
                        audioPlayer.prepare(audio)
                        audioPlayer.play()
                    } else if (willBeOnPause) {
                        audioPlayer.pause()
                    }
                    var track = audio.copy(isPlaying = willPlay, isOnPause = willBeOnPause)
                    currentTrack = track
                    track
                }
                else
                    audio.copy(isPlaying = false, isOnPause = false)
        }

        var updatedFalouritesList=favouriteList.map { audio->
            if(audio.id==currentTrack?.id)
                currentTrack!!.copy()
            else
                audio.copy(isPlaying = false, isOnPause = false)

        }
        return Triple(updatedOnDeviceList,updatedFalouritesList,currentTrack)
    }
}