package com.example.infrastructure.storage.room

import android.content.ContentUris
import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import android.provider.MediaStore
import com.example.infrastructure.models.AudioAlbumInfrastructure
import com.example.infrastructure.models.AudioInfrastructure
import com.example.infrastructure.storage.ExternalStorage
import com.example.infrastructure.utils.IsFileAvailableByUri
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class MediaStore(private var context: Context, private var isFileAvailableByUri: IsFileAvailableByUri): ExternalStorage {
    companion object{
        const val MIN_DURATION=10000
        const val ALBUM_ART_URI="content://media/external/audio/albumart"
    }
    override fun getAudio(): Flow<List<AudioInfrastructure>> = callbackFlow {
        var uri= MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        var observer=object: ContentObserver(null){
            override fun onChange(selfChange: Boolean) {
                trySend(fetchAllAudio())
            }
        }
        context.contentResolver.registerContentObserver(uri,true,observer)

        trySend(fetchAllAudio())
            awaitClose { context.contentResolver.unregisterContentObserver(observer) }
    }

    private fun fetchAllAudio():List<AudioInfrastructure>{
        var audioUri= MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        var audioList=mutableListOf<AudioInfrastructure>()
        var projection=arrayOf(MediaStore.Audio.Media._ID,MediaStore.Audio.Media.TITLE,MediaStore.Audio.Media.ARTIST,MediaStore.Audio.Media.DATA,MediaStore.Audio.Media.DURATION,MediaStore.Audio.Media.ALBUM)
        var selection="${MediaStore.Audio.Media.DURATION}>?"
        var selectionArgs=arrayOf("$MIN_DURATION")
        var sortOrder="${MediaStore.Audio.Media.TITLE} ASC"

        var cursor=context.contentResolver.query(audioUri,projection,selection,selectionArgs,sortOrder)
        cursor.use {
            var idColumn=it?.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            var titleColumn=it?.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            var artistColumn=it?.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            var durationColumn=it?.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            var albumColumn=it?.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)

            while(it?.moveToNext()!!){
                var id=it.getLong(idColumn!!)
                var title=it.getString(titleColumn!!)
                var artist=it.getString(artistColumn!!)
                var duration=it.getLong(durationColumn!!)
                var albumTitle=it.getString(albumColumn!!)
                var albumImgUri=getAlbumImageUriByAudio(id)
                var isAlbumImgAvailable=isFileAvailableByUri(albumImgUri.toString())

                audioList.add(AudioInfrastructure(id,title,duration,artist,Uri.withAppendedPath(audioUri,id.toString()),null,
                    AudioAlbumInfrastructure(null,albumTitle,if(isAlbumImgAvailable) albumImgUri else null),false))
            }
        }
        return audioList
    }

    private fun getAlbumImageUriByAudio(id: Long): Uri? {
        var projection=arrayOf(MediaStore.Audio.Media.ALBUM_ID)
        var selection="${MediaStore.Audio.Media._ID}=?"
        var selectionArgs=arrayOf(id.toString())

        var audioCursor=context.contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,projection,selection,selectionArgs,null)

        audioCursor?.use{cursor->
            if(cursor.moveToFirst()){
                var albumIDcolumn=cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
                var albumID=cursor.getLong(albumIDcolumn)
                var albumArtUri= ContentUris.withAppendedId(Uri.parse(ALBUM_ART_URI),albumID)

                return albumArtUri
            }
        }
        return null
    }
}