package com.example.domain.usecases

import com.example.domain.AudioRepository
import com.example.domain.models.AudioDomain

class ToggleFavourites(private var audioRepository: AudioRepository){
    suspend operator fun invoke(audio: AudioDomain){
        audioRepository.updateAudioInDB(audio.copy(isFavourite = !audio.isFavourite))
    }
}