package com.example.cyclistance.address

import android.location.Address
import java.util.*

class MockAddress(

): Address(Locale.getDefault()){
    override fun getLocality(): String? {
        return "null"
    }

    override fun getSubAdminArea(): String? {
        return "subAdminArea"
    }

    override fun getSubThoroughfare(): String? {
        return "null"
    }

    override fun getThoroughfare(): String? {
        return null
    }
}