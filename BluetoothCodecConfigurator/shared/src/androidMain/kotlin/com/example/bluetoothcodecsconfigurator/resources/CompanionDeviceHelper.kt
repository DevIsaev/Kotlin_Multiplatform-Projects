package com.example.bluetoothcodecsconfigurator.resources

import android.annotation.SuppressLint
import android.companion.AssociationRequest
import android.companion.BluetoothDeviceFilter
import android.companion.CompanionDeviceManager
import android.content.Context
import android.content.IntentSender
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.util.regex.Pattern

private const val TAG = "CdmHelper"

@RequiresApi(Build.VERSION_CODES.O)
class CompanionDeviceHelper(private val context: Context) {

    private val cdm = context.getSystemService(Context.COMPANION_DEVICE_SERVICE) as CompanionDeviceManager

    fun isAssociated(address: String): Boolean =
        cdm.associations.any { it.equals(address, ignoreCase = true) }

    @SuppressLint("MissingPermission")
    fun requestAssociation(
        deviceName: String,
        onIntentSenderReady: (IntentSender) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        val filter = BluetoothDeviceFilter.Builder()
            .setNamePattern(Pattern.compile(Pattern.quote(deviceName)))
            .build()

        val request = AssociationRequest.Builder()
            .addDeviceFilter(filter)
            .setSingleDevice(true)
            .build()

        cdm.associate(request, object : CompanionDeviceManager.Callback() {
            override fun onDeviceFound(intentSender: IntentSender) {
                Log.d(TAG, "CDM device found, launching chooser")
                onIntentSenderReady(intentSender)
            }
            override fun onFailure(error: CharSequence?) {
                Log.e(TAG, "CDM association failed: $error")
                onFailure(Exception(error?.toString() ?: "Unknown CDM error"))
            }
        }, null)
    }
}