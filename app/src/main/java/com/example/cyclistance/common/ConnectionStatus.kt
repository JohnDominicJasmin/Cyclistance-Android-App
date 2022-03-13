package com.example.cyclistance.common

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import android.net.ConnectivityManager

object ConnectionStatus {


    @SuppressLint("MissingPermission")
    @Suppress("Deprecation")
    fun hasInternetConnection(context: Context): Boolean? =
        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo?.let { networkInfo ->
            networkInfo.isConnected && networkInfo.isAvailable
        }


    fun hasGPSConnection(context: Context): Boolean =
        (context.getSystemService(Context.LOCATION_SERVICE) as LocationManager).let { locationManager ->
            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER)
        }


}