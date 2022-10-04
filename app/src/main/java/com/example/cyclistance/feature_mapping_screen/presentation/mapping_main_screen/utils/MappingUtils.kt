package com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.utils

import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build

fun Geocoder.getAddress(location: Location, onCallbackAddress: (List<Address>) -> Unit) {

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
}
