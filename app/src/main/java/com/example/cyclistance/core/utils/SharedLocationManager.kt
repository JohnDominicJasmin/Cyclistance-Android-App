package com.example.cyclistance.core.utils

import android.content.Context
import android.location.Geocoder
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
    private val location = SimpleLocation(context, false, false, LOCATION_UPDATES_INTERVAL)
    private val geocoder = Geocoder(context, Locale.ENGLISH)
    private var isUpdateBegan: Boolean = false

    private val _locationUpdates = callbackFlow {

        if (!isUpdateBegan) {
            location.beginUpdates()
            isUpdateBegan = true
        }
        location.setListener {
            if (location.hasLocationEnabled()) {
                val latitude = location.latitude
                val longitude = location.longitude
                trySend(element = SharedLocationModel(latLng = LatLng(latitude, longitude),
                        addresses = geocoder.getFromLocation(latitude,longitude, 1)))
            }
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