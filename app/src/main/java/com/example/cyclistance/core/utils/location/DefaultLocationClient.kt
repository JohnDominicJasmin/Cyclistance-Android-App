package com.example.cyclistance.core.utils.location
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import com.example.cyclistance.core.utils.location.ConnectionStatus.hasGPSConnection
import com.example.cyclistance.core.utils.location.ConnectionStatus.hasLocationPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber

class DefaultLocationClient(
    private val locationRequest: LocationRequest,
    val context: Context):LocationClient {

    private val fusedLocationProviderClient: FusedLocationProviderClient by lazy {
        FusedLocationProviderClient(context)
    }


    @SuppressLint("MissingPermission")
    override fun getLocationUpdates(): Flow<Location> {
        return callbackFlow {

            if(!context.hasLocationPermission()) {
                Timber.v("Location permission not granted")
                return@callbackFlow
            }


            if(!context.hasGPSConnection()){
                Timber.v("GPS not enabled")
                return@callbackFlow
            }



            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    super.onLocationResult(result)
                    trySend(result.lastLocation?:return)
                }
            }


            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )

            awaitClose {
                fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            }

        }
    }
}
