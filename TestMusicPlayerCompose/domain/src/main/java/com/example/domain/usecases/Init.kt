package com.example.domain.usecases

import com.example.domain.AudioRepository
import com.example.domain.logger.Logger
import com.example.domain.utils.ExtractArtist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow

class Init(private var audioRepository: AudioRepository, private var extractArtist: ExtractArtist, private var logger: Logger) {
    operator fun invoke(): Flow<Unit> = flow{
        var audioFromDBflow=audioRepository.getAudioFromDB().distinctUntilChanged()
        var audioFromDeviceFlow=audioRepository.getAudioFromExternalStorage()

        audioFromDBflow.combine(audioFromDeviceFlow){
            audioFromDB, audioFromDevice ->

            var audioFromDeviceWithArtist=audioFromDevice.map {
                if(it.artist=="<unknown>"){
                    var pair=extractArtist(it.title)
                    it.copy(title = pair.first, artist = pair.second)
                }
                else it
            }.filter { it.artist!=null }

            var audioFromDBurisSet=audioFromDB.map{it.uri}.toSet()
            var audioFromDeviceUrisSet=audioFromDeviceWithArtist.map{it.uri}.toSet()

            var newAudio=audioFromDeviceWithArtist.filter { it.uri !in audioFromDBurisSet }
            var deletedAudio=audioFromDeviceWithArtist.filter { it.uri !in audioFromDeviceUrisSet }

            if(newAudio.isNotEmpty()){
                var albums=newAudio.mapNotNull { it.album }
                var albumsIDs=audioRepository.addAudioAlbumsToDB(albums)
                var updatedAudio=newAudio.mapIndexed { index, domain ->
                    domain.copy(albumID = albumsIDs[index])
                }

                audioRepository.addAudioToDB(updatedAudio)
            }
            if(deletedAudio.isNotEmpty()){
                audioRepository.deleteAudioInDB(deletedAudio)
            }
        }.collect {  }

    }
}