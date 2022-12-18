package com.example.cyclistance.core.utils.constants

import androidx.datastore.preferences.core.stringPreferencesKey

object MappingConstants {
    const val ROAD_LABEL_NAVIGATION = "road-label-navigation"
    const val MAX_ZOOM_LEVEL_MAPS = 20.00
    const val MIN_ZOOM_LEVEL_MAPS = 3.00

    const val MAP_ZOOM = 6.00
    const val DEFAULT_MAP_ZOOM_LEVEL = 4.00
    const val TRACKING_MAP_ZOOM_LEVEL = 16.0
    const val LOCATE_USER_ZOOM_LEVEL = 15.20

    const val CAMERA_TILT_DEGREES = 30.00
    const val DEFAULT_LATITUDE = 12.8797
    const val DEFAULT_LONGITUDE = 121.7740

    const val DEFAULT_CAMERA_ANIMATION_DURATION = 1500L
    const val FAST_CAMERA_ANIMATION_DURATION = 0L

    const val CHARACTER_LIMIT = 110
    const val IMAGE_PLACEHOLDER_URL = "https://raw.githubusercontent.com/JohnDominicJasmin/JohnDominicJasmin/main/ic_empty_profile_placeholder.jpg"

    const val LOCATION_UPDATES_INTERVAL: Long = 10000L
    const val FASTEST_LOCATION_UPDATES_INTERVAL: Long = 7000L

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
    const val DEFAULT_BIKE_AVERAGE_SPEED_KM = 24.5
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
}