package com.example.bluetoothcodecsconfigurator.resources

import kotlinx.coroutines.flow.Flow

expect class BluetoothController(context: Any?) {
    fun observeState(): Flow<BtState>
    suspend fun setEnabled(enabled: Boolean)
    fun observePairedAudioDevices(): Flow<List<BtDevice>>
    suspend fun connect(device: BtDevice): Result<Unit>
    fun observeCodecs(device: BtDevice): Flow<List<CodecInfo>>
    suspend fun applyCodecSelection(device: BtDevice, selection: CodecSelection): Result<Unit>
}