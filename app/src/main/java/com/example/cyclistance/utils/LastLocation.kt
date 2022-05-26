package com.example.cyclistance.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import im.delight.android.location.SimpleLocation
import java.util.*

class LastLocation(private val context: Context) {

    private val location = SimpleLocation(context, false, false, 3000)
    private var isUpdateBegan: Boolean = false


    fun beginLocationUpdates(){
        if(!isUpdateBegan){
            location.beginUpdates()
            isUpdateBegan = true
        }

    }

    fun getUserLocation():List<Address>{
        val geocoder = Geocoder(context, Locale.ENGLISH)
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
    }

}