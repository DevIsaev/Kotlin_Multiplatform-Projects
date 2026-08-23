package com.example.infrastructure.storage.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "audio_table")
data class AudioEntity(@PrimaryKey(autoGenerate = true) val id: Long, var title: String, var duration: Long, var artist: String?, var uri: String, var albumID: Long?, var isFavourite: Boolean)

