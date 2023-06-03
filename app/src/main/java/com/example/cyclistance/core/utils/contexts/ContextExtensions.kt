package com.example.cyclistance.core.utils.contexts

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.example.cyclistance.core.utils.constants.MappingConstants
import com.example.cyclistance.service.LocationService

fun Context.openAppSettings() {
    startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        val uri = Uri.fromParts("package", packageName, null)
        data = uri
    })
}

fun Context.startLocationServiceIntentAction(intentAction: String = MappingConstants.ACTION_START) {
    Intent(this, LocationService::class.java).apply {
        action = intentAction
        startService(this)
    }
}

fun Context.openWifiSettings() {
    startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
}

fun Context.callPhoneNumber(phoneNumber: String) {
    val intent = Intent(Intent.ACTION_CALL)

    intent.data = Uri.parse("tel:$phoneNumber")
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(intent)
}

