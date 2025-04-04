package com.myapp.cyclistance.core.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build


class NetworkConnectivityUtil(val context: Context)  {
    private val connectivityManager: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


    @Suppress("Deprecation")
    private fun hasInternetConnection(): Boolean =
        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo.let { networkInfo->
            networkInfo?.isConnected == true && networkInfo.isAvailable
        }

    @Suppress("DEPRECATION")
    fun hasInternet(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)?.let {
                it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || it.hasTransport(
                    NetworkCapabilities.TRANSPORT_WIFI) || it.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
            } ?: false
        } else {
            return hasInternetConnection()
        }
    }


    companion object {
        fun ConnectivityObserver.Status.toInternetStatusToText():String{
            return when(this){
                ConnectivityObserver.Status.Available -> "Connected"
                ConnectivityObserver.Status.Unavailable, ConnectivityObserver.Status.Lost -> "No Connection"
                ConnectivityObserver.Status.Losing -> "Connecting..."
            }
        }

    }
}