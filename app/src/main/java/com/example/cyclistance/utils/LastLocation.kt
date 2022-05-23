package com.example.cyclistance.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import im.delight.android.location.SimpleLocation
import java.util.*

object LastLocation {
    fun getUserLocation(context: Context):List<Address>{
        val location = SimpleLocation(context)
        val geocoder = Geocoder(context, Locale.ENGLISH)
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
    }
}