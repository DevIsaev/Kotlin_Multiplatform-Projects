package com.example.bluetoothcodecsconfigurator.resources

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

actual class BluetoothController actual constructor(context: Any?) {

    private val mockState = MutableStateFlow(BtState.ON)
    private val mockDevices = listOf(
        BtDevice("Sony WH-1000XM5", "AA:BB:CC:11:22:33", true),
        BtDevice("Logitech Mouse", "AA:BB:CC:77:88:99", false)
    )
    private val mockCodecsState = MutableStateFlow(
        mapOf(
            "AA:BB:CC:11:22:33" to listOf(
                CodecInfo("SBC", 0, listOf(44100), listOf(16), listOf("Stereo"), false),
                CodecInfo("AAC", 1, listOf(44100), listOf(16), listOf("Stereo"), true, 44100, 16),
                CodecInfo("LDAC", 4, listOf(44100, 48000, 96000), listOf(16, 24, 32), listOf("Stereo"), false)
            )
        )
    )

    actual fun observeState(): Flow<BtState> = mockState.asStateFlow()
    actual suspend fun setEnabled(enabled: Boolean) { delay(300); mockState.value = if (enabled) BtState.ON else BtState.OFF }
    actual fun observePairedAudioDevices(): Flow<List<BtDevice>> = flowOf(mockDevices)
    actual suspend fun connect(device: BtDevice): Result<Unit> {
        if (!device.isAudioDevice) return Result.failure(IllegalArgumentException("Не аудио-устройство"))
        delay(300); return Result.success(Unit)
    }
    actual fun observeCodecs(device: BtDevice): Flow<List<CodecInfo>> =
        mockCodecsState.map { it[device.address] ?: emptyList() }

    actual suspend fun applyCodecSelection(device: BtDevice, selection: CodecSelection): Result<Unit> {
        val current = mockCodecsState.value[device.address] ?: return Result.failure(IllegalStateException("Unknown"))
        val updated = current.map {
            it.copy(
                isSelected = it.codecType == selection.codecType,
                currentSampleRate = if (it.codecType == selection.codecType) selection.sampleRate else null,
                currentBits = if (it.codecType == selection.codecType) selection.bitsPerSample else null
            )
        }
        mockCodecsState.update { it + (device.address to updated) }
        return Result.success(Unit)
    }
}