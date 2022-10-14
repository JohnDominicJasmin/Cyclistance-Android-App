package com.example.cyclistance.core.utils.constants

import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.BottomSheetType

object MappingConstants {
    const val MAX_ZOOM_LEVEL_MAPS = 20.00
    const val MIN_ZOOM_LEVEL_MAPS = 3.00

    const val MAP_ZOOM = 6.00
    const val DEFAULT_MAP_ZOOM_LEVEL = 4.00
    const val TRACKING_MAP_ZOOM_LEVEL = 16.0
    const val LOCATE_USER_ZOOM_LEVEL = 14.80

    const val CAMERA_TILT_DEGREES = 30.00
    const val DEFAULT_LATITUDE = 12.8797
    const val DEFAULT_LONGITUDE = 121.7740

    const val DEFAULT_CAMERA_ANIMATION_DURATION = 700L

    const val CHARACTER_LIMIT = 110
    const val IMAGE_PLACEHOLDER_URL = "https://raw.githubusercontent.com/JohnDominicJasmin/JohnDominicJasmin/main/ic_empty_profile_placeholder.jpg"
    const val NO_SIM_CARD_RESULT_CODE = 1002
    const val NONE_OF_THE_ABOVE_RESULT_CODE = 1001

    const val LOCATION_UPDATES_INTERVAL: Long = 10000L
    const val FASTEST_LOCATION_UPDATES_INTERVAL: Long = 7000L
    const val INTERVAL_UPDATE_USERS = 5000L

    val BIKE_TYPE_KEY = stringPreferencesKey("bike_type")
    val ADDRESS_KEY = stringPreferencesKey("address")
    val SEARCH_BOTTOM_SHEET = BottomSheetType.SearchAssistance.type

    const val LOCATION_NAME = "cyclistance-location"
    const val LOCATION_SERVICE_CHANNEL_ID = "cyclistance-location-channel-id"
    const val NOTIFICATION_FOREGROUND_ID = 1
    const val ACTION_START = "start"
    const val ACTION_STOP = "stop"


}