package com.example.infrastructure

import com.example.domain.AudioRepository
import com.example.domain.models.AudioAlbumDomain
import com.example.domain.models.AudioDomain
import com.example.infrastructure.mappers.toDomain
import com.example.infrastructure.mappers.toInfrastructureAlbum
import com.example.infrastructure.mappers.toInfrastructureAudio
import com.example.infrastructure.storage.DB
import com.example.infrastructure.storage.ExternalStorage
import kotlinx.coroutines.flow.Flow

class AudioRepositoryImpl(private var externalStorage: ExternalStorage, private var db: DB): AudioRepository {
    override fun getAudioFromExternalStorage(): Flow<List<AudioDomain>> {
        return externalStorage.getAudio().toDomain()
    }

    override fun getAudioFromDB(): Flow<List<AudioDomain>> {
        return db.getAudio().toDomain()
    }

    override fun getFavouriteAudio(): Flow<List<AudioDomain>> {
       return db.getFavouriteAudio().toDomain()
    }

    override suspend fun addAudioToDB(audioList: List<AudioDomain>) {
        return db.addAudio(audioList.toInfrastructureAudio())
    }

    override suspend fun addAudioAlbumsToDB(albumList: List<AudioAlbumDomain>): List<Long> {
        return db.addAudioAlbums(albumList.toInfrastructureAlbum())
    }

    override suspend fun updateAudioInDB(audio: AudioDomain) {
        return db.updateAudio(audio.toInfrastructureAudio())
    }

    override suspend fun deleteAudioInDB(audioList: List<AudioDomain>) {
        return db.deleteAudio(audioList.toInfrastructureAudio())
    }
}