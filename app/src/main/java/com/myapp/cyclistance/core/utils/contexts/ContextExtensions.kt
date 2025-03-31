package com.myapp.cyclistance.core.utils.contexts

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.myapp.cyclistance.core.utils.constants.MappingConstants
import com.myapp.cyclistance.service.LocationService

fun Context.openAppSettings() {
    startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        val uri = Uri.fromParts("package", packageName, null)
        data = uri
    })
}

fun Context.startBackgroundLocationService(intentAction: String = MappingConstants.ACTION_START) {
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

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "preferences")

fun Context.shareLocation(latitude: Double, longitude: Double){
    val uri = "http://maps.google.com/maps?saddr=$latitude,$longitude"

    val sharingIntent = Intent(Intent.ACTION_SEND)
    sharingIntent.type = "text/plain"
    val subject = "My Location"
    val message = "Check out my location:\n$uri"

    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
    sharingIntent.putExtra(Intent.EXTRA_TEXT, message)

    val chooserIntent = Intent.createChooser(sharingIntent, "Share your Location via")
    startActivity(chooserIntent)
}

fun Context.openEmailApp() {
    val intent = Intent(Intent.ACTION_MAIN).apply {
        addCategory(Intent.CATEGORY_APP_EMAIL)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    startActivity(intent)
}