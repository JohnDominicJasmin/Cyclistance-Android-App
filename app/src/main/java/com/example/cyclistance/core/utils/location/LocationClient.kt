package com.example.cyclistance.core.utils.location


import android.location.Address
import kotlinx.coroutines.flow.Flow


interface LocationClient {
    fun getLocationUpdates(): Flow<List<Address>>

}