package com.example.infrastructure.storage.room.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.infrastructure.storage.room.entities.AudioAlbumEntity
import com.example.infrastructure.storage.room.entities.AudioEntity

data class AudioWithAlbum(
    @Embedded val audio: AudioEntity,
    @Relation(
        parentColumn = "albumID",
        entityColumn = "id"
    )
    val album: AudioAlbumEntity?
)
