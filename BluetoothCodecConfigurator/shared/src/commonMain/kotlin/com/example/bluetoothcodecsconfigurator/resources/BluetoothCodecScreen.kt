package com.example.bluetoothcodecsconfigurator.resources

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@Composable
fun BluetoothCodecScreen(controller: BluetoothController) {
    val scope = rememberCoroutineScope()

    val btState by controller.observeState().collectAsState(initial = BtState.UNKNOWN)
    val devices by controller.observePairedAudioDevices().collectAsState(initial = emptyList())

    var selectedDevice by remember { mutableStateOf<BtDevice?>(null) }
    var connectError by remember { mutableStateOf<String?>(null) }
    var applyError by remember { mutableStateOf<String?>(null) }
    var isApplying by remember { mutableStateOf(false) }

    val codecs by (selectedDevice?.let { controller.observeCodecs(it) } ?: flowOf(emptyList()))
        .collectAsState(initial = emptyList())

    // Что пользователь выбрал руками (может отличаться от того, что реально сейчас активно)
    var chosenCodec by remember(selectedDevice) { mutableStateOf<CodecInfo?>(null) }
    var chosenSampleRate by remember(chosenCodec) { mutableStateOf<Int?>(null) }
    var chosenBits by remember(chosenCodec) { mutableStateOf<Int?>(null) }

    // При обновлении списка кодеков — синхронизируем "выбранный" с реально активным
    LaunchedEffect(codecs) {
        val active = codecs.firstOrNull { it.isSelected }
        if (active != null && chosenCodec?.codecType != active.codecType) {
            chosenCodec = active
            chosenSampleRate = active.currentSampleRate ?: active.sampleRates.maxOrNull()
            chosenBits = active.currentBits ?: active.bitsPerSample.maxOrNull()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Bluetooth: $btState", style = MaterialTheme.typography.titleMedium)
            Switch(checked = btState == BtState.ON, onCheckedChange = { scope.launch { controller.setEnabled(it) } })
        }

        HorizontalDivider()

        Text("Сопряжённые устройства", style = MaterialTheme.typography.titleMedium)
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            devices.forEach { device ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(device.name, fontWeight = FontWeight.Bold)
                        Text(device.address, style = MaterialTheme.typography.labelSmall)
                    }
                    Button(onClick = {
                        scope.launch {
                            connectError = null
                            val result = controller.connect(device)
                            result.onSuccess { selectedDevice = device }
                                .onFailure { connectError = it.message }
                        }
                    }) { Text(if (selectedDevice?.address == device.address) "Выбрано" else "Подключить") }
                }
            }
        }
        connectError?.let { Text(it, color = MaterialTheme.colorScheme.error) }

        selectedDevice?.let { device ->
            HorizontalDivider()
            Text("Кодек: ${device.name}", style = MaterialTheme.typography.titleMedium)

            // Ряд 1 — выбор кодека
            Text("Кодек", style = MaterialTheme.typography.labelLarge)
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                codecs.forEach { codec ->
                    FilterChip(
                        selected = chosenCodec?.codecType == codec.codecType,
                        onClick = {
                            chosenCodec = codec
                            chosenSampleRate = codec.currentSampleRate ?: codec.sampleRates.maxOrNull()
                            chosenBits = codec.currentBits ?: codec.bitsPerSample.maxOrNull()
                        },
                        label = {
                            Text(codec.name + if (codec.isSelected) " (активен)" else "")
                        }
                    )
                }
            }

            // Ряд 2 — частота дискретизации (зависит от выбранного кодека)
            chosenCodec?.let { codec ->
                Text("Частота дискретизации", style = MaterialTheme.typography.labelLarge)
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    codec.sampleRates.forEach { rate ->
                        FilterChip(
                            selected = chosenSampleRate == rate,
                            onClick = { chosenSampleRate = rate },
                            label = { Text("$rate Гц") }
                        )
                    }
                }

                // Ряд 3 — разрядность
                Text("Разрядность", style = MaterialTheme.typography.labelLarge)
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    codec.bitsPerSample.forEach { bits ->
                        FilterChip(
                            selected = chosenBits == bits,
                            onClick = { chosenBits = bits },
                            label = { Text("$bits бит") }
                        )
                    }
                }

                Spacer(Modifier.height(4.dp))
                Button(
                    enabled = !isApplying && chosenSampleRate != null && chosenBits != null,
                    onClick = {
                        val rate = chosenSampleRate ?: return@Button
                        val bits = chosenBits ?: return@Button
                        scope.launch {
                            isApplying = true
                            applyError = null
                            val result = controller.applyCodecSelection(
                                device, CodecSelection(codec.codecType, encodeRateForApply(rate), encodeBitsForApply(bits))
                            )
                            result.onFailure { applyError = it.message }
                            isApplying = false
                        }
                    }
                ) {
                    Text(if (isApplying) "Применяем..." else "Применить: ${codec.name} / $chosenSampleRate Гц / $chosenBits бит")
                }

                applyError?.let {
                    Spacer(Modifier.height(4.dp))
                    Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

// Кодирование конкретного человекочитаемого значения обратно в битовую маску Android API
private fun encodeRateForApply(rate: Int): Int = when (rate) {
    44100 -> 0x1
    48000 -> 0x2
    88200 -> 0x4
    96000 -> 0x8
    else -> 0x1
}

private fun encodeBitsForApply(bits: Int): Int = when (bits) {
    16 -> 0x1
    24 -> 0x2
    32 -> 0x4
    else -> 0x1
}