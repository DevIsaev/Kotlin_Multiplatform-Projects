package com.example.bluetoothcodecsconfigurator.resources

// commonMain
data class BtDevice(
    val name: String,
    val address: String,
    val isAudioDevice: Boolean
)

data class CodecInfo(
    val name: String,
    val codecType: Int,
    val sampleRates: List<Int>,
    val bitsPerSample: List<Int>,
    val channelModes: List<String>,
    val isSelected: Boolean,
    val currentSampleRate: Int? = null,
    val currentBits: Int? = null
)

enum class BtState { OFF, ON, TURNING_ON, TURNING_OFF, UNKNOWN }

data class CodecSelection(
    val codecType: Int,
    val sampleRate: Int,
    val bitsPerSample: Int
)