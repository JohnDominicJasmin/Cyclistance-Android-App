package com.example.cyclistance.core.utils.location


import kotlinx.coroutines.flow.Flow


interface LocationClient {
    fun getLocationUpdates(): Flow<SharedLocationModel>

}