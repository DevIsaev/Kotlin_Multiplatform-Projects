package com.example.infrastructure.di

import android.content.Context
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.domain.AudioPlayer
import com.example.domain.AudioRepository
import com.example.infrastructure.AudioPlayerImpl
import com.example.infrastructure.AudioRepositoryImpl
import com.example.infrastructure.player.ExoPlayerImpl
import com.example.infrastructure.player.IPlayer
import com.example.infrastructure.storage.DB
import com.example.infrastructure.storage.ExternalStorage
import com.example.infrastructure.storage.RoomStorage
import com.example.infrastructure.storage.room.AudioDao
import com.example.infrastructure.storage.room.MediaStore
import com.example.infrastructure.storage.room.MyDB
import com.example.infrastructure.utils.IsFileAvailableByUri
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class InfrastructureModule {
    @Provides
    @Singleton
    fun provideDB(@ApplicationContext context: Context): MyDB{return MyDB.getDB(context) }

    @Provides
    @Singleton
    fun provideAudioDao(database: MyDB): AudioDao {return database.audioDao()}

    @Provides
    @Singleton
    fun provideExternalStorage(@ApplicationContext context: Context, isFileAvailableByUri: IsFileAvailableByUri): ExternalStorage {
        return MediaStore(context, isFileAvailableByUri)
    }

    @Provides
    @Singleton
    fun provideDBStorage(audioDao: AudioDao): DB {return RoomStorage(audioDao) }

    @Provides
    @Singleton
    fun provideExoPlayer(exoPlayer: ExoPlayer): IPlayer{return ExoPlayerImpl(exoPlayer) }

    @Provides
    @Singleton
    fun provideExoPlayerLibrary(@ApplicationContext context: Context): ExoPlayer{return ExoPlayer.Builder(context).build()}

    @Provides
    @Singleton
    fun provideAudioRepository(storage: ExternalStorage,db: DB): AudioRepository{return AudioRepositoryImpl(storage,db) }

    @Provides
    @Singleton
    fun provideAudioPlayer(player: IPlayer): AudioPlayer{return AudioPlayerImpl(player) }

    @Provides
    @Singleton
    fun provideIsFileAvailableByUri(@ApplicationContext context: Context): IsFileAvailableByUri {return IsFileAvailableByUri(context) }
}