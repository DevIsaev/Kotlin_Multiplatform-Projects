package com.example.testmusicplayercompose.di

import com.example.domain.AudioPlayer
import com.example.domain.AudioRepository
import com.example.domain.logger.Logger
import com.example.domain.usecases.Init
import com.example.domain.usecases.ToggleAudioPlayback
import com.example.domain.usecases.ToggleFavourites
import com.example.domain.utils.ExtractArtist
import com.example.infrastructure.utils.IsFileAvailableByUri
import com.example.testmusicplayercompose.logger.AndroidLogger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class DomainModule {
    @Provides
    fun provideLogger(): Logger = AndroidLogger()

    @Provides
    fun provideToggleAudioPlayback(audioPlayer: AudioPlayer, isFileAvailableByUri: IsFileAvailableByUri,logger: Logger): ToggleAudioPlayback { return ToggleAudioPlayback(audioPlayer,isFileAvailableByUri,logger) }

    @Provides
    fun provideToggleFavourites(audioRepository: AudioRepository): ToggleFavourites{ return ToggleFavourites(audioRepository) }

    @Provides
    fun provideExtractArtist(): ExtractArtist{ return ExtractArtist() }

    @Provides
    fun provideInit(audioRepository: AudioRepository, extractArtist: ExtractArtist, logger: Logger): Init {
        return Init(audioRepository, extractArtist, logger)
    }
}