package com.example.bluetoothcodecsconfigurator.resources

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothA2dp
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothCodecConfig
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

private const val TAG = "BtController"

actual class BluetoothController actual constructor(context: Any?) {

    private val appContext: Context = context as? Context
        ?: error("BluetoothController on Android requires a valid Context")

    private val adapter: BluetoothAdapter? =
        (appContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter

    private var a2dpProxy: BluetoothA2dp? = null
    private var pendingProxyCallback: (() -> Unit)? = null

    private fun ensureA2dpProxy(onReady: () -> Unit) {
        if (a2dpProxy != null) {
            Log.d(TAG, "[PROXY] already connected, reuse")
            onReady()
            return
        }
        Log.d(TAG, "[PROXY] requesting new A2DP proxy")
        pendingProxyCallback = onReady
        adapter?.getProfileProxy(appContext, object : BluetoothProfile.ServiceListener {
            override fun onServiceConnected(profile: Int, proxy: BluetoothProfile) {
                Log.d(TAG, "[PROXY] connected")
                a2dpProxy = proxy as BluetoothA2dp
                pendingProxyCallback?.invoke()
                pendingProxyCallback = null
            }
            override fun onServiceDisconnected(profile: Int) {
                Log.d(TAG, "[PROXY] disconnected")
                a2dpProxy = null
            }
        }, BluetoothProfile.A2DP)
    }

    actual fun observeState(): Flow<BtState> = callbackFlow {
        trySend(mapState(adapter?.state))
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(c: Context, i: Intent) {
                trySend(mapState(i.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1)))
            }
        }
        appContext.registerReceiver(receiver, IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED))
        awaitClose { appContext.unregisterReceiver(receiver) }
    }

    private fun mapState(s: Int?) = when (s) {
        BluetoothAdapter.STATE_ON -> BtState.ON
        BluetoothAdapter.STATE_OFF -> BtState.OFF
        BluetoothAdapter.STATE_TURNING_ON -> BtState.TURNING_ON
        BluetoothAdapter.STATE_TURNING_OFF -> BtState.TURNING_OFF
        else -> BtState.UNKNOWN
    }

    @SuppressLint("MissingPermission")
    actual suspend fun setEnabled(enabled: Boolean) {
        if (enabled) {
            appContext.startActivity(
                Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        } else {
            appContext.startActivity(
                Intent(Settings.ACTION_BLUETOOTH_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        }
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    private fun BluetoothDevice.isAudioDevice(): Boolean {
        val major = bluetoothClass?.majorDeviceClass
        val minor = bluetoothClass?.deviceClass
        if (major != BluetoothClass.Device.Major.AUDIO_VIDEO) return false
        return minor in setOf(
            BluetoothClass.Device.AUDIO_VIDEO_WEARABLE_HEADSET,
            BluetoothClass.Device.AUDIO_VIDEO_HEADPHONES,
            BluetoothClass.Device.AUDIO_VIDEO_LOUDSPEAKER,
            BluetoothClass.Device.AUDIO_VIDEO_HIFI_AUDIO,
            BluetoothClass.Device.AUDIO_VIDEO_PORTABLE_AUDIO
        )
    }

    @SuppressLint("MissingPermission")
    actual fun observePairedAudioDevices(): Flow<List<BtDevice>> = callbackFlow {
        fun push() {
            try {
                val list = adapter?.bondedDevices.orEmpty().map {
                    BtDevice(it.name ?: it.address, it.address, it.isAudioDevice())
                }
                trySend(list)
            } catch (e: SecurityException) {
                Log.e(TAG, "Missing permission reading bonded devices", e)
                trySend(emptyList())
            }
        }
        push()
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(c: Context, i: Intent) = push()
        }
        val filter = IntentFilter().apply {
            addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
            addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
            addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
        }
        appContext.registerReceiver(receiver, filter)
        awaitClose { appContext.unregisterReceiver(receiver) }
    }

    @SuppressLint("MissingPermission")
    actual suspend fun connect(device: BtDevice): Result<Unit> {
        val btDevice = adapter?.getRemoteDevice(device.address)
            ?: return Result.failure(IllegalStateException("Adapter unavailable"))

        val isAudio = try {
            btDevice.isAudioDevice()
        } catch (e: SecurityException) {
            return Result.failure(e)
        }
        if (!isAudio) {
            return Result.failure(IllegalArgumentException("Устройство не является аудио-девайсом"))
        }

        return suspendCancellableCoroutine { cont ->
            ensureA2dpProxy {
                val connected = a2dpProxy?.connectedDevices.orEmpty()
                val alreadyConnected = connected.any { it.address == device.address }
                Log.d(TAG, "[CONNECT] ${device.name} alreadyConnected=$alreadyConnected")
                if (alreadyConnected) {
                    cont.resume(Result.success(Unit))
                } else {
                    cont.resume(Result.failure(IllegalStateException("Устройство не подключено по A2DP. Подключите его в системных настройках.")))
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    actual fun observeCodecs(device: BtDevice): Flow<List<CodecInfo>> = callbackFlow {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            Log.d(TAG, "[CODECS] SDK < 33, unavailable")
            trySend(emptyList()); awaitClose {}; return@callbackFlow
        }

        fun refresh(reason: String) {
            Log.d(TAG, "[CODECS] refresh triggered: $reason")
            ensureA2dpProxy {
                pushCodecs(a2dpProxy, device.address) { trySend(it) }
            }
        }
        refresh("initial")

        val receiver = object : BroadcastReceiver() {
            override fun onReceive(c: Context, i: Intent) {
                Log.d(TAG, "[BROADCAST] ACTION_CODEC_CONFIG_CHANGED received for ${device.name}")
                refresh("codec_config_changed_broadcast")
            }
        }
        appContext.registerReceiver(
            receiver,
            IntentFilter("android.bluetooth.a2dp.profile.action.CODEC_CONFIG_CHANGED")
        )
        awaitClose { appContext.unregisterReceiver(receiver) }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("MissingPermission")
    private fun pushCodecs(a2dp: BluetoothA2dp?, address: String, emit: (List<CodecInfo>) -> Unit) {
        if (a2dp == null) { Log.e(TAG, "[CODECS] a2dp proxy null"); emit(emptyList()); return }
        val remote = adapter?.getRemoteDevice(address)
        if (remote == null) { Log.e(TAG, "[CODECS] remote device null"); emit(emptyList()); return }

        try {
            val getCodecStatusMethod = BluetoothA2dp::class.java.getMethod("getCodecStatus", BluetoothDevice::class.java)
            val status = getCodecStatusMethod.invoke(a2dp, remote)
            if (status == null) {
                Log.w(TAG, "[CODECS] getCodecStatus returned null — device not A2DP-active right now")
                emit(emptyList())
                return
            }

            val statusClass = status.javaClass
            val getCodecConfig = statusClass.getMethod("getCodecConfig")
            val getSelectableCapabilities = statusClass.getMethod("getCodecsSelectableCapabilities")

            val selectedConfig = getCodecConfig.invoke(status)
            val selectableConfigs = getSelectableCapabilities.invoke(status) as List<*>

            val configClass = Class.forName("android.bluetooth.BluetoothCodecConfig")
            val getCodecType = configClass.getMethod("getCodecType")
            val getSampleRate = configClass.getMethod("getSampleRate")
            val getBitsPerSample = configClass.getMethod("getBitsPerSample")
            val getChannelMode = configClass.getMethod("getChannelMode")

            val selectedType = selectedConfig?.let { getCodecType.invoke(it) as Int }
            val selectedSampleRateMask = selectedConfig?.let { getSampleRate.invoke(it) as Int }
            val selectedBitsMask = selectedConfig?.let { getBitsPerSample.invoke(it) as Int }

            Log.d(TAG, "[CODECS] currently selected: type=$selectedType sampleRateMask=$selectedSampleRateMask bitsMask=$selectedBitsMask")

            val result = selectableConfigs.filterNotNull().map { cfg ->
                val codecType = getCodecType.invoke(cfg) as Int
                CodecInfo(
                    name = codecTypeToName(codecType),
                    codecType = codecType,
                    sampleRates = decodeSampleRates(getSampleRate.invoke(cfg) as Int),
                    bitsPerSample = decodeBits(getBitsPerSample.invoke(cfg) as Int),
                    channelModes = decodeChannelModes(getChannelMode.invoke(cfg) as Int),
                    isSelected = codecType == selectedType,
                    currentSampleRate = if (codecType == selectedType) decodeSingleSampleRate(selectedSampleRateMask) else null,
                    currentBits = if (codecType == selectedType) decodeSingleBits(selectedBitsMask) else null
                )
            }
            Log.d(TAG, "[CODECS] resolved list: $result")
            emit(result)
        } catch (e: Exception) {
            Log.e(TAG, "[CODECS] failed to read status", e)
            emit(emptyList())
        }
    }

    /**
     * Главная функция. Логируется КАЖДЫЙ шаг:
     * 1) состояние до
     * 2) точный конфиг, который отправляем
     * 3) факт вызова
     * 4) форс-реконнект
     * 5) состояние после (с явным сравнением codecType/sampleRate/bits до и после)
     */
    @SuppressLint("MissingPermission")
    actual suspend fun applyCodecSelection(device: BtDevice, selection: CodecSelection): Result<Unit> {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return Result.failure(UnsupportedOperationException("Требуется Android 13+"))
        }

        return suspendCancellableCoroutine { cont ->
            ensureA2dpProxy {
                val a2dp = a2dpProxy
                val remote = adapter?.getRemoteDevice(device.address)
                if (a2dp == null || remote == null) {
                    cont.resume(Result.failure(IllegalStateException("A2DP недоступен")))
                    return@ensureA2dpProxy
                }

                try {
                    val configClass = Class.forName("android.bluetooth.BluetoothCodecConfig")
                    val getCodecStatusMethod = BluetoothA2dp::class.java.getMethod("getCodecStatus", BluetoothDevice::class.java)
                    val getCodecType = configClass.getMethod("getCodecType")
                    val getSampleRate = configClass.getMethod("getSampleRate")
                    val getBitsPerSample = configClass.getMethod("getBitsPerSample")

                    // --- ШАГ 1: состояние ДО ---
                    val statusBefore = getCodecStatusMethod.invoke(a2dp, remote)
                    val configBefore = statusBefore?.javaClass?.getMethod("getCodecConfig")?.invoke(statusBefore)
                    val typeBefore = configBefore?.let { getCodecType.invoke(it) as Int }
                    val sampleBefore = configBefore?.let { getSampleRate.invoke(it) as Int }
                    val bitsBefore = configBefore?.let { getBitsPerSample.invoke(it) as Int }
                    Log.d(TAG, "[APPLY][STEP 1] BEFORE: type=$typeBefore(${codecTypeToName(typeBefore ?: -1)}) sampleMask=$sampleBefore bitsMask=$bitsBefore")

                    // --- ШАГ 2: строим целевой конфиг ---
                    val builderClass = Class.forName("android.bluetooth.BluetoothCodecConfig\$Builder")
                    val builder = builderClass.getConstructor().newInstance()
                    builderClass.getMethod("setCodecType", Int::class.javaPrimitiveType).invoke(builder, selection.codecType)
                    builderClass.getMethod("setCodecPriority", Int::class.javaPrimitiveType)
                        .invoke(builder, BluetoothCodecConfig.CODEC_PRIORITY_HIGHEST)
                    builderClass.getMethod("setSampleRate", Int::class.javaPrimitiveType).invoke(builder, selection.sampleRate)
                    builderClass.getMethod("setBitsPerSample", Int::class.javaPrimitiveType).invoke(builder, selection.bitsPerSample)
                    builderClass.getMethod("setChannelMode", Int::class.javaPrimitiveType)
                        .invoke(builder, BluetoothCodecConfig.CHANNEL_MODE_STEREO)
                    val newConfig = builderClass.getMethod("build").invoke(builder)

                    Log.d(TAG, "[APPLY][STEP 2] TARGET: codecType=${selection.codecType}(${codecTypeToName(selection.codecType)}) " +
                            "sampleRateMask=${selection.sampleRate} bitsMask=${selection.bitsPerSample} config=$newConfig")

                    // --- ШАГ 3: вызов ---
                    val setMethod = BluetoothA2dp::class.java.getMethod(
                        "setCodecConfigPreference", BluetoothDevice::class.java, configClass
                    )
                    setMethod.invoke(a2dp, remote, newConfig)
                    Log.d(TAG, "[APPLY][STEP 3] setCodecConfigPreference invoked, no exception thrown")

                    // --- ШАГ 4: форс renegotiation ---
                    try {
                        val setActiveMethod = BluetoothA2dp::class.java.getMethod("setActiveDevice", BluetoothDevice::class.java)
                        Log.d(TAG, "[APPLY][STEP 4a] clearing active device to force renegotiation")
                        setActiveMethod.invoke(a2dp, null as BluetoothDevice?)
                        Thread.sleep(400)
                        Log.d(TAG, "[APPLY][STEP 4b] restoring active device")
                        setActiveMethod.invoke(a2dp, remote)
                        Thread.sleep(400)
                    } catch (e: Exception) {
                        Log.w(TAG, "[APPLY][STEP 4] renegotiation forcing failed", e)
                    }

                    // --- ШАГ 5: состояние ПОСЛЕ ---
                    val statusAfter = getCodecStatusMethod.invoke(a2dp, remote)
                    val configAfter = statusAfter?.javaClass?.getMethod("getCodecConfig")?.invoke(statusAfter)
                    val typeAfter = configAfter?.let { getCodecType.invoke(it) as Int }
                    val sampleAfter = configAfter?.let { getSampleRate.invoke(it) as Int }
                    val bitsAfter = configAfter?.let { getBitsPerSample.invoke(it) as Int }

                    val changed = typeAfter != typeBefore || sampleAfter != sampleBefore || bitsAfter != bitsBefore
                    Log.d(TAG, "[APPLY][STEP 5] AFTER: type=$typeAfter(${codecTypeToName(typeAfter ?: -1)}) " +
                            "sampleMask=$sampleAfter bitsMask=$bitsAfter")
                    Log.d(TAG, "[APPLY][RESULT] REAL CHANGE DETECTED = $changed " +
                            "(requested type=${selection.codecType}, got type=$typeAfter)")

                    if (changed && typeAfter == selection.codecType) {
                        cont.resume(Result.success(Unit))
                    } else {
                        cont.resume(Result.failure(
                            IllegalStateException("Система приняла запрос, но реальный кодек не изменился (before=$typeBefore, after=$typeAfter). Устройство/прошивка могли проигнорировать предпочтение.")
                        ))
                    }
                } catch (e: java.lang.reflect.InvocationTargetException) {
                    Log.e(TAG, "[APPLY] failed (target exception)", e.targetException)
                    cont.resume(Result.failure(e.targetException ?: e))
                } catch (e: Exception) {
                    Log.e(TAG, "[APPLY] failed", e)
                    cont.resume(Result.failure(e))
                }
            }
        }
    }

    private fun codecTypeToName(type: Int) = when (type) {
        BluetoothCodecConfig.SOURCE_CODEC_TYPE_SBC -> "SBC"
        BluetoothCodecConfig.SOURCE_CODEC_TYPE_AAC -> "AAC"
        BluetoothCodecConfig.SOURCE_CODEC_TYPE_APTX -> "aptX"
        BluetoothCodecConfig.SOURCE_CODEC_TYPE_APTX_HD -> "aptX HD"
        BluetoothCodecConfig.SOURCE_CODEC_TYPE_LDAC -> "LDAC"
        else -> "Unknown($type)"
    }

    private fun decodeSampleRates(mask: Int): List<Int> = buildList {
        if (mask and BluetoothCodecConfig.SAMPLE_RATE_44100 != 0) add(44100)
        if (mask and BluetoothCodecConfig.SAMPLE_RATE_48000 != 0) add(48000)
        if (mask and BluetoothCodecConfig.SAMPLE_RATE_88200 != 0) add(88200)
        if (mask and BluetoothCodecConfig.SAMPLE_RATE_96000 != 0) add(96000)
    }

    private fun decodeBits(mask: Int): List<Int> = buildList {
        if (mask and BluetoothCodecConfig.BITS_PER_SAMPLE_16 != 0) add(16)
        if (mask and BluetoothCodecConfig.BITS_PER_SAMPLE_24 != 0) add(24)
        if (mask and BluetoothCodecConfig.BITS_PER_SAMPLE_32 != 0) add(32)
    }

    private fun decodeChannelModes(mask: Int): List<String> = buildList {
        if (mask and BluetoothCodecConfig.CHANNEL_MODE_MONO != 0) add("Mono")
        if (mask and BluetoothCodecConfig.CHANNEL_MODE_STEREO != 0) add("Stereo")
    }

    private fun decodeSingleSampleRate(mask: Int?): Int? = when {
        mask == null -> null
        mask and BluetoothCodecConfig.SAMPLE_RATE_96000 != 0 -> 96000
        mask and BluetoothCodecConfig.SAMPLE_RATE_88200 != 0 -> 88200
        mask and BluetoothCodecConfig.SAMPLE_RATE_48000 != 0 -> 48000
        mask and BluetoothCodecConfig.SAMPLE_RATE_44100 != 0 -> 44100
        else -> null
    }

    private fun decodeSingleBits(mask: Int?): Int? = when {
        mask == null -> null
        mask and BluetoothCodecConfig.BITS_PER_SAMPLE_32 != 0 -> 32
        mask and BluetoothCodecConfig.BITS_PER_SAMPLE_24 != 0 -> 24
        mask and BluetoothCodecConfig.BITS_PER_SAMPLE_16 != 0 -> 16
        else -> null
    }
}