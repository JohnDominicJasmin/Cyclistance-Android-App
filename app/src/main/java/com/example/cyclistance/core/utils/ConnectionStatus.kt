package com.example.cyclistance.core.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.IntentSender
import android.location.LocationManager
import android.net.ConnectivityManager
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import timber.log.Timber

object ConnectionStatus {


    @SuppressLint("MissingPermission")
    @Suppress("Deprecation")
    fun hasInternetConnection(context: Context): Boolean =
        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo.let {networkInfo->
            networkInfo?.isConnected == true && networkInfo.isAvailable
        }


    fun hasGPSConnection(context: Context): Boolean =
        (context.getSystemService(Context.LOCATION_SERVICE) as LocationManager).let { locationManager ->
            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER)
        }



     fun checkLocationSetting(
        context: Context,
        onDisabled: (IntentSenderRequest) -> Unit,
        onEnabled: () -> Unit
    ) {

        val locationRequest = LocationRequest.create().apply {
            interval = 1000
            fastestInterval = 1000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val client: SettingsClient = LocationServices.getSettingsClient(context)
        val builder: LocationSettingsRequest.Builder = LocationSettingsRequest
            .Builder()
            .addLocationRequest(locationRequest)

        val gpsSettingTask: Task<LocationSettingsResponse> =
            client.checkLocationSettings(builder.build())

        gpsSettingTask.addOnSuccessListener { onEnabled() }
        gpsSettingTask.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    val intentSenderRequest = IntentSenderRequest
                        .Builder(exception.resolution)
                        .build()
                    onDisabled(intentSenderRequest)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // ignore here
                }
            }
        }

    }
}