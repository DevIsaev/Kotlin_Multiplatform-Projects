package com.example.infrastructure.models

import android.net.Uri

data class AudioInfrastructure(
    val id: Long, var title: String, var duration: Long, var artist: String?, var uri: Uri,
    var albumID: Long?, var album: AudioAlbumInfrastructure?, var isFavourite: Boolean
)

data class AudioAlbumInfrastructure(var id:Long?,var title:String, var imgUri: Uri?)