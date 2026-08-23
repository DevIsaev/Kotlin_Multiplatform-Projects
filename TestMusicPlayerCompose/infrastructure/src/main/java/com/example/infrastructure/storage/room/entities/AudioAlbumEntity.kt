package com.example.infrastructure.storage.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("album_table")
data class AudioAlbumEntity(@PrimaryKey(true) var id:Long?, var title:String, var imgUri: String?)