package com.example.infrastructure.storage.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.infrastructure.storage.room.entities.AudioAlbumEntity
import com.example.infrastructure.storage.room.entities.AudioEntity

@Database(entities = [AudioEntity::class, AudioAlbumEntity::class], version = 1)
abstract class MyDB: RoomDatabase() {
    abstract var audioDao: AudioDao
    companion object{
        @Volatile
        private var INSTANCE: MyDB?=null
        fun getDB(context: Context?): MyDB{
            return INSTANCE?:synchronized(this){
                var instance=Room.databaseBuilder(context!!.applicationContext, MyDB::class.java,"audio_database").build()
                INSTANCE=instance
                instance
            }
        }
    }
}