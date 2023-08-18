package com.example.cyclistance.core.utils.constants

import androidx.datastore.preferences.core.stringPreferencesKey

object MappingConstants {
    const val ROAD_LABEL_NAVIGATION = "road-label-navigation"
    const val MAX_ZOOM_LEVEL_MAPS = 20.00
    const val MIN_ZOOM_LEVEL_MAPS = 3.00

    const val MAP_ZOOM = 6.00
    const val DEFAULT_MAP_ZOOM_LEVEL = 4.00
    const val DEFAULT_LATITUDE = 12.879700000000023
    const val DEFAULT_LONGITUDE = 121.774
    const val TRACKING_MAP_ZOOM_LEVEL = 16.0
    const val LOCATE_USER_ZOOM_LEVEL = 15.20

    const val CAMERA_TILT_DEGREES = 30.00


    const val DEFAULT_CAMERA_ANIMATION_DURATION: Int = 1500
    const val FAST_CAMERA_ANIMATION_DURATION:Int = 1

    const val CHARACTER_LIMIT = 110
    const val IMAGE_PLACEHOLDER_URL = "https://raw.githubusercontent.com/JohnDominicJasmin/JohnDominicJasmin/main/ic_empty_profile_placeholder.jpg"

    const val LOCATION_UPDATES_INTERVAL: Long = 4000L
    const val FASTEST_LOCATION_UPDATES_INTERVAL: Long = 1500L

    val BIKE_TYPE_KEY = stringPreferencesKey("bike_type")
    val ADDRESS_KEY = stringPreferencesKey("address")


    const val LOCATION_NAME = "cyclistance-location"
    const val LOCATION_SERVICE_CHANNEL_ID = "cyclistance-location-channel-id"
    const val ACTION_START = "start"
    const val ACTION_STOP = "stop"

    const val CONFIRM_DETAILS_VM_STATE_KEY = "confirm_details_vm_state_key"
    const val MAPPING_VM_STATE_KEY = "mapping_vm_state_key"
    const val CANCELLATION_VM_STATE_KEY = "cancellation_vm_state_key"
    const val BROADCAST_USERS = "broadcast_users"
    const val BROADCAST_RESCUE_TRANSACTION = "broadcast_rescue_transaction"
    const val BROADCAST_LOCATION = "broadcasting_location"
    const val JOIN_LIVE_LOCATION_UPDATES = "joinLiveLocationUpdates"
    const val DEFAULT_BIKE_AVERAGE_SPEED_KM = 20.5
    const val BUTTON_ANIMATION_DURATION = 1500L

    const val INJURY_TEXT = "Injury"
    const val BROKEN_FRAME_TEXT  = "Broken Frame"
    const val INCIDENT_TEXT = "Incident"
    const val BROKEN_CHAIN_TEXT = "Broken Chain"
    const val FLAT_TIRES_TEXT = "Flat tires"
    const val FAULTY_BRAKES_TEXT = "Faulty Brakes"



    const val HEADER_CACHE_CONTROL = "Cache-Control"
    const val HEADER_PRAGMA = "Pragma"

    const val SELECTION_RESCUER_TYPE = "rescuer_type"
    const val SELECTION_RESCUEE_TYPE = "rescuee_type"

    const val NEAREST_METERS = 10.00
    const val DEFAULT_RADIUS = 7000.00
    const val API_CALL_RETRY_COUNT: Long = 3


    /*Map*/

    const val DEFAULT_LOCATION_CIRCLE_PULSE_DURATION_MS = 1500f
    const val DEFAULT_LOCATION_CIRCLE_PULSE_RADIUS = 50f

    const val NUMBER_OF_STARS = 5
    const val ROUTE_LAYER_ID = "route-layer-id"
    const val ROUTE_SOURCE_ID = "route-source-id"
    const val ICON_SOURCE_ID = "icon-source-id"
    const val TRANSACTION_ICON_ID = "red-pin-icon-id"
    const val ICON_LAYER_ID = "icon-layer-id"

}