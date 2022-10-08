package com.example.cyclistance.core.utils.location
import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.os.Looper
import com.example.cyclistance.core.utils.location.ConnectionStatus.hasGPSConnection
import com.example.cyclistance.core.utils.location.ConnectionStatus.hasLocationPermission
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.utils.getAddress
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

class DefaultLocationClient(
    private val locationRequest: LocationRequest,
    val context: Context,
    private val fusedLocationProviderClient: FusedLocationProviderClient):LocationClient {

    private val geocoder = Geocoder(context, Locale.ENGLISH)

    @SuppressLint("MissingPermission")
    override fun getLocationUpdates(): Flow<SharedLocationModel> {
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
                    result.locations.lastOrNull()?.let { location ->

                            geocoder.getAddress(
                                location.latitude,
                                location.longitude
                            ){ address ->
                                launch() {

                                send(
                                    SharedLocationModel(
                                        latLng = LatLng(
                                            location.latitude,
                                            location.longitude),
                                        addresses = address
                                    )
                                )

                            }
                        }

                    }
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
