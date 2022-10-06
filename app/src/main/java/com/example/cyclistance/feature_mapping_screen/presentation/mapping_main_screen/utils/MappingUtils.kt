package com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.utils

import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import androidx.annotation.WorkerThread
import timber.log.Timber
import java.io.IOException

@WorkerThread
fun Geocoder.getAddress(location: Location, onCallbackAddress: (List<Address>) -> Unit) {
    
    try {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getFromLocation(
                location.latitude, location.longitude, 1,
            ) { addresses ->
                onCallbackAddress(addresses)
            }
        } else {
            onCallbackAddress(
                getFromLocation(location.latitude, location.longitude, 1) ?: emptyList())
        }

    } catch (e: IOException) {
        Timber.e("GET ADDRESS: ${e.message}")
    }
}
