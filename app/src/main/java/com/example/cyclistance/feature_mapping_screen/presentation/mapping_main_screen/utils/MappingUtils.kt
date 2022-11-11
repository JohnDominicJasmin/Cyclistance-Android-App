package com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.utils

import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.annotation.WorkerThread
import timber.log.Timber
import java.io.IOException

@WorkerThread
inline fun Geocoder.getAddress(
    latitude: Double,
    longitude: Double,
    crossinline onCallbackAddress: (List<Address>) -> Unit) {

    try {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getFromLocation(
                latitude, longitude, 1,
            ) { addresses ->
                onCallbackAddress(addresses)
            }
        } else {
            onCallbackAddress(
                getFromLocation(latitude, longitude, 1) ?: emptyList())
        }

    } catch (e: IOException) {
        Timber.e("GET ADDRESS: ${e.message}")
    }
}

fun Double.distanceFormat(): String {
    if(this <= 0.0){
        throw IllegalArgumentException("Distance must be greater than 0")
    }

    return if (this < 1000) {
        "%.2f m".format(this)
    } else {
        "%.2f km".format((this / 1000))
    }
}

fun getETA(distanceMeters: Double, averageSpeedKm: Double): String {
    val distanceToKm = distanceMeters / 1000
    if(distanceToKm <= 0.0){
        throw IllegalArgumentException("Distance must be greater than 0")
    }
    val eta = distanceToKm / averageSpeedKm
    val hours = eta.toInt()
    val minutes = (eta - hours) * 60
    val minutesInt = minutes.toInt()
    val minsFormat = if (minutesInt <= 1) "$minutesInt min" else "$minutesInt mins"
    val hourFormat = if (hours >= 1) "$hours hrs " else ""
    return "$hourFormat$minsFormat"
}

