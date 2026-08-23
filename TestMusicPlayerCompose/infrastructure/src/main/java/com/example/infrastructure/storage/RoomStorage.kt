package com.example.infrastructure.storage

import com.example.infrastructure.mappers.toEntity
import com.example.infrastructure.mappers.toEntityAlbumList
import com.example.infrastructure.mappers.toEntityAudio
import com.example.infrastructure.mappers.toInfrastructure
import com.example.infrastructure.models.AudioAlbumInfrastructure
import com.example.infrastructure.models.AudioInfrastructure
import com.example.infrastructure.storage.room.AudioDao
import kotlinx.coroutines.flow.Flow

class RoomStorage(private var dao: AudioDao): DB {
    override fun getAudio(): Flow<List<AudioInfrastructure>> {
        return dao.getAllAudio().toInfrastructure()
    }

    override fun getFavouriteAudio(): Flow<List<AudioInfrastructure>> {
        return dao.getFavouriteAudio().toInfrastructure()
    }

    override suspend fun addAudio(audioList: List<AudioInfrastructure>) {
        dao.insertAudio(audioList.toEntityAudio())
    }

    override suspend fun addAudioAlbums(albumList: List<AudioAlbumInfrastructure>): List<Long> {
        return dao.insertAlbums(albumList.toEntityAlbumList())
    }

    override suspend fun updateAudio(audio: AudioInfrastructure) {
        dao.updateAudio(audio.toEntity())
    }

    override suspend fun deleteAudio(audioList: List<AudioInfrastructure>) {
        dao.deleteAudio(audioList.toEntityAudio())
    }

}