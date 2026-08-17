package com.example.domain

import com.example.domain.models.AudioAlbumDomain
import com.example.domain.models.AudioDomain
import kotlinx.coroutines.flow.Flow

interface AudioRepository {
    fun getAudioFromExternalStorage(): Flow<List<AudioDomain>>
    fun getAudioFromDB(): Flow<List<AudioDomain>>
    fun getFavouriteAudio(): Flow<List<AudioDomain>>

    suspend fun addAudioToDB(audioList: List<AudioDomain>)
    suspend fun addAudioAlbumsToDB(audioList: List<AudioAlbumDomain>): List<Long>
    suspend fun updateAudioInDB(audio: AudioDomain)
    suspend fun deleteAudioInDB(audioList:List<AudioDomain>)
}