package com.example.cyclistance.core.utils

import android.content.Context
import android.location.Geocoder
import android.os.Build
import com.example.cyclistance.core.utils.MappingConstants.LOCATION_UPDATES_INTERVAL
import com.google.android.gms.maps.model.LatLng
import im.delight.android.location.SimpleLocation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.shareIn
import java.util.*

class SharedLocationManager constructor(
    context: Context,
    externalScope: CoroutineScope
) {
    private val location = SimpleLocation(context, false, false, LOCATION_UPDATES_INTERVAL).apply {
        beginUpdates()
    }
    private val geocoder = Geocoder(context, Locale.ENGLISH)


    private val _locationUpdates = callbackFlow {


        val updateLocation = {
            if (Build.VERSION.SDK_INT >= 33) {
                geocoder.getFromLocation(
                    location.latitude, location.longitude, 1,
                ) { addresses ->
                    trySend(
                        element = SharedLocationModel(
                            latLng = LatLng(
                                location.latitude,
                                location.longitude), addresses = addresses))
                }
            } else {
                trySend(
                    element = SharedLocationModel(
                        latLng = LatLng(location.latitude, location.longitude),
                        addresses = geocoder.getFromLocation(
                            location.latitude,
                            location.longitude,
                            1) ?: emptyList()))
            }
        }



        updateLocation()
        location.setListener {
            updateLocation()
        }

        awaitClose {
            location.endUpdates()
        }

    }.shareIn(
        scope = externalScope,
        replay = 0,
        started = SharingStarted.WhileSubscribed()
    )


    fun locationFlow(): Flow<SharedLocationModel> = _locationUpdates
}