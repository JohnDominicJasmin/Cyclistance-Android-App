package com.example.cyclistance.feature_mapping.domain.location


import android.location.Location
import kotlinx.coroutines.flow.Flow


interface LocationClient {
    fun getLocationUpdates(): Flow<Location>

}