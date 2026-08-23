package com.example.infrastructure.storage

import com.example.infrastructure.models.AudioAlbumInfrastructure
import com.example.infrastructure.models.AudioInfrastructure
import kotlinx.coroutines.flow.Flow

interface DB {
    fun getAudio(): Flow<List<AudioInfrastructure>>
    fun getFavouriteAudio(): Flow<List<AudioInfrastructure>>
    suspend fun addAudio(audioList:List<AudioInfrastructure>)
    suspend fun addAudioAlbums(albumList:List<AudioAlbumInfrastructure>): List<Long>
    suspend fun updateAudio(audio: AudioInfrastructure)
    suspend fun deleteAudio(audioList:List<AudioInfrastructure>)
}