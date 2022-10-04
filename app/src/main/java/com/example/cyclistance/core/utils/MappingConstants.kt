package com.example.cyclistance.core.utils

import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.cyclistance.feature_main_screen.presentation.mapping_main_screen.BottomSheetType

object MappingConstants {
    const val MAX_ZOOM_LEVEL_MAPS = 18.85
    const val MIN_ZOOM_LEVEL_MAPS = 3.00

    const val CAMERA_TILT_DEGREES = 30.00
    const val DEFAULT_LATITUDE = 12.8797
    const val DEFAULT_LONGITUDE = 121.7740
    const val DEFAULT_CAMERA_ANIMATION_DURATION = 700
    const val MAP_ZOOM = 6.00
    const val MAX_CHARACTERS = 110
    const val IMAGE_PLACEHOLDER_URL =
        "https://instagram.fmaa12-1.fna.fbcdn.net/v/t51.2885-19/44884218_345707102882519_2446069589734326272_n.jpg?efg=eyJybWQiOiJpZ19hbmRyb2lkX21vYmlsZV9uZXR3b3JrX3N0YWNrX25vbl9zZWxlY3RpdmVfcmV0cnlfNDoxX25vbl9zZWxlY3RpdmVfcmV0cnkifQ&_nc_ht=instagram.fmaa12-1.fna.fbcdn.net&_nc_cat=1&_nc_ohc=zTh2jNA7vSwAX_PMsvg&edm=AA0lj5EBAAAA&ccb=7-5&ig_cache_key=YW5vbnltb3VzX3Byb2ZpbGVfcGlj.2-ccb7-5&oh=00_AT9UYPnCSMGDiD1xcE3fGvXsVcrEvuLu5oG77ZZhTm8IoA&oe=62B1298F&_nc_sid=3add00"
    const val NO_SIM_CARD_RESULT_CODE = 1002
    const val NONE_OF_THE_ABOVE_RESULT_CODE = 1001
    const val LOCATION_UPDATES_INTERVAL: Long = 3500
    const val FASTEST_LOCATION_UPDATES_INTERVAL: Long = 1000L
    val BIKE_TYPE_KEY = stringPreferencesKey("bike_type")
    val ADDRESS_KEY = stringPreferencesKey("address")
    val SEARCH_BOTTOM_SHEET = BottomSheetType.SearchAssistance.type
    const val ENHANCE_LOCATION_PROVIDER = "enhance_location_provider"
    const val INTERVAL_UPDATE_USERS = 5000L


}