package com.example.cyclistance.core.utils

import android.location.Address
import com.google.android.gms.maps.model.LatLng

data class SharedLocationModel(
    val latLng: LatLng,
    val addresses: List<Address>
)
