package com.example.cyclistance.core.utils.location

import android.content.Context
import android.location.Geocoder
import com.example.cyclistance.core.utils.constants.MappingConstants.LOCATION_UPDATES_INTERVAL
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.utils.getAddress
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


    private val _locationUpdates = callbackFlow {

        location.beginUpdates()
        val updateLocation = {
            geocoder.getAddress(
                latitude = location.latitude,
                longitude = location.longitude) { address ->
                trySend(
                    element = SharedLocationModel(
                        latLng = LatLng(
                            location.latitude,
                            location.longitude),
                        addresses = address
                    ))
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