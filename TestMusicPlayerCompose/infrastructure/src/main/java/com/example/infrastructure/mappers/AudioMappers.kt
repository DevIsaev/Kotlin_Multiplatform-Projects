package com.example.infrastructure.mappers

import android.net.Uri
import androidx.core.net.toUri
import com.example.domain.models.AudioAlbumDomain
import com.example.domain.models.AudioDomain
import com.example.infrastructure.models.AudioAlbumInfrastructure
import com.example.infrastructure.models.AudioInfrastructure
import com.example.infrastructure.storage.room.entities.AudioAlbumEntity
import com.example.infrastructure.storage.room.entities.AudioEntity
import com.example.infrastructure.storage.room.relations.AudioWithAlbum
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun AudioInfrastructure.toDomain(): AudioDomain{ return AudioDomain(this.id,this.title,this.duration,this.artist,this.uri.toString(),this.album?.toDomain(),this.albumID, false,false,this.isFavourite) }

fun AudioInfrastructure.toEntity(): AudioEntity{return AudioEntity(this.id,this.title,this.duration,this.artist,this.uri.toString(),this.albumID,this.isFavourite)}

fun AudioDomain.toInfrastructureAudio(): AudioInfrastructure{return AudioInfrastructure(this.id,this.title,this.duration,this.artist, Uri.parse(this.uri),this.albumID,this.album?.toInfrastructureAlbum(),this.isFavourite) }

fun AudioWithAlbum.toInfrastructure(): AudioInfrastructure{ return AudioInfrastructure(this.audio.id,this.audio.title,this.audio.duration,this.audio.artist,Uri.parse(this.audio.uri),this.album?.id,
    this.album?.let {
        AudioAlbumInfrastructure(this.album!!.id, this.album!!.title, this.album!!.imgUri?.toUri()) } as AudioAlbumInfrastructure,this.audio.isFavourite)}

fun AudioAlbumDomain.toInfrastructureAlbum(): AudioAlbumInfrastructure { return AudioAlbumInfrastructure(this.id, this.title, this.imgUri?.toUri()) }

fun AudioAlbumInfrastructure.toDomain(): AudioAlbumDomain { return AudioAlbumDomain(this.id, this.title, this.imgUri?.toString()) }

fun AudioAlbumInfrastructure.toEntityAlbum(): AudioAlbumEntity { return AudioAlbumEntity(this.id, this.title, this.imgUri?.toString()) }




fun List<AudioWithAlbum>.toInfrastructure(): List<AudioInfrastructure> { return this.map { it.toInfrastructure() } }

fun List<AudioInfrastructure>.toDomain(): List<AudioDomain> { return this.map { it.toDomain() } }

fun List<AudioDomain>.toInfrastructureAudio(): List<AudioInfrastructure> { return this.map { it.toInfrastructureAudio() } }

fun List<AudioInfrastructure>.toEntityAudio(): List<AudioEntity> { return this.map { it.toEntity() } }

fun List<AudioAlbumDomain>.toInfrastructureAlbum(): List<AudioAlbumInfrastructure> { return this.map { it.toInfrastructureAlbum() } }

fun List<AudioAlbumInfrastructure>.toEntityAlbumList(): List<AudioAlbumEntity> { return this.map { it.toEntityAlbum() } }

fun Flow<List<AudioWithAlbum>>.toInfrastructure(): Flow<List<AudioInfrastructure>> {
    return this.map { audioWithAlbums ->
        audioWithAlbums.map { audioWithAlbum ->
            audioWithAlbum.toInfrastructure()
        }
    }
}

fun Flow<List<AudioInfrastructure>>.toDomain(): Flow<List<AudioDomain>> {
    return this.map { audioWithAlbums ->
        audioWithAlbums.map { audioWithAlbum ->
            audioWithAlbum.toDomain()
        }
    }
}
