package com.example.infrastructure.storage

import com.example.infrastructure.models.AudioInfrastructure
import kotlinx.coroutines.flow.Flow

interface ExternalStorage {
    fun getAudio(): Flow<List<AudioInfrastructure>>
}