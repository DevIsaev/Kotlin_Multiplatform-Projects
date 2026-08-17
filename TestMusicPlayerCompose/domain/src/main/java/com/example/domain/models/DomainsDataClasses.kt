package com.example.domain.models

data class AudioDomain(val id:Long,var title: String, var duration:Long, var artist: String?, var genre:String?, var uri:String,
                      var album: AudioAlbumDomain?, var albumID:Long,
                       var isPlaying: Boolean, var isOnOuse: Boolean, var isFavourite:Boolean)

data class AudioAlbumDomain(var id:Long?,var title:String, var imgUri:String?)