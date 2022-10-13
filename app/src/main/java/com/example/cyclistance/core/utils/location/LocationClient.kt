package com.example.cyclistance.core.utils.location


import android.location.Location
import kotlinx.coroutines.flow.Flow


interface LocationClient {
    fun getLocationUpdates(): Flow<Location>

}