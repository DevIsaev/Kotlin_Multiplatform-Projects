package com.example.infrastructure.storage.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.infrastructure.storage.room.entities.AudioAlbumEntity
import com.example.infrastructure.storage.room.entities.AudioEntity
import com.example.infrastructure.storage.room.relations.AudioWithAlbum
import kotlinx.coroutines.flow.Flow

@Dao
interface AudioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAudio(audioList: List<AudioEntity>)
    @Delete
    suspend fun deleteAudio(audioList: List<AudioEntity>)
    @Insert
    suspend fun insertAlbums(audioList: List<AudioAlbumEntity>): List<Long>
    @Update
    suspend fun updateAudio(audio: AudioEntity)

    @Transaction

    @Query("SELECT * FROM audio_table")
    fun getAllAudio(): Flow<List<AudioWithAlbum>>

    @Query("SELECT * FROM audio_table WHERE isFavourite=1")
    fun getFavouriteAudio(): Flow<List<AudioWithAlbum>>
}