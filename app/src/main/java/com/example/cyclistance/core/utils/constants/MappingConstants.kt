package com.example.cyclistance.core.utils.constants

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object MappingConstants {
    const val ROAD_LABEL_NAVIGATION = "road-label-navigation"
    const val MAX_ZOOM_LEVEL_MAPS = 20.00
    const val MIN_ZOOM_LEVEL_MAPS = 3.00

    const val MAP_ZOOM = 6.00
    const val DEFAULT_MAP_ZOOM_LEVEL = 4.50
    const val DEFAULT_LATITUDE = 12.879700000000023
    const val DEFAULT_LONGITUDE = 121.774
    const val TRACKING_MAP_ZOOM_LEVEL = 16.0
    const val LOCATE_USER_ZOOM_LEVEL = 15.20

    const val CAMERA_TILT_DEGREES = 30.00


    const val DEFAULT_CAMERA_ANIMATION_DURATION: Int = 1500
    const val FAST_CAMERA_ANIMATION_DURATION:Int = 1

    const val CHARACTER_LIMIT = 110
    const val IMAGE_PLACEHOLDER_URL = "https://raw.githubusercontent.com/JohnDominicJasmin/JohnDominicJasmin/main/ic_empty_profile_placeholder.jpg"

    const val LOCATION_UPDATES_INTERVAL: Long = 30000L
    const val FASTEST_LOCATION_UPDATES_INTERVAL: Long = 20000L

    val BIKE_TYPE_KEY = stringPreferencesKey("bike_type")
    val ADDRESS_KEY = stringPreferencesKey("address")
    val MAP_TYPE_KEY = stringPreferencesKey("map_type")
    val SHOW_HAZARDOUS_STARTING_INFO_KEY = booleanPreferencesKey("show_hazardous_starting_info")

    const val LOCATION_NAME = "Cyclistance Location"
    const val LOCATION_SERVICE_CHANNEL_ID = "cyclistance-location-channel-id"
    const val ACTION_START = "start"
    const val ACTION_STOP = "stop"
    const val ACTION_START_FOREGROUND = "startForeground"
    const val ACTION_STOP_FOREGROUND = "stopForeground"

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


    const val CONSTRUCTION = "Construction"
    const val LANE_CLOSURE = "Lane closure"
    const val CRASH = "Crash"
    const val NEED_ASSISTANCE = "Need Assistance"
    const val OBJECT_ON_ROAD = "Object on Road"
    const val SLOWDOWN = "Slowdown"





    const val HEADER_CACHE_CONTROL = "Cache-Control"
    const val HEADER_PRAGMA = "Pragma"

    const val SELECTION_RESCUER_TYPE = "rescuer_type"
    const val SELECTION_RESCUEE_TYPE = "rescuee_type"

    const val NEAREST_METERS = 70.00
    const val DEFAULT_RADIUS = 7000.00
    const val API_CALL_RETRY_COUNT: Long = 7


    /*Map*/

    const val DEFAULT_LOCATION_CIRCLE_PULSE_DURATION_MS = 2000f
    const val DEFAULT_LOCATION_CIRCLE_PULSE_RADIUS = 50f

    const val NUMBER_OF_STARS = 5
    const val ROUTE_LAYER_ID = "route-layer-id"
    const val ROUTE_SOURCE_ID = "route-source-id"
    const val ICON_SOURCE_ID = "icon-source-id"
    const val TRANSACTION_ICON_ID = "red-pin-icon-id"
    const val ICON_LAYER_ID = "icon-layer-id"








    const val KEY_HAZARDOUS_LANE_COLLECTION = "hazardous_lanes"
    const val KEY_TIMESTAMP_FIELD = "timestamp"
    const val KEY_MARKER_FIELD = "marker"

    const val KEY_ID = "id"
    const val KEY_DATE_POSTED = "datePosted"
    const val KEY_ID_CREATOR = "idCreator"
    const val KEY_ID_LABEL = "label"
    const val KEY_ID_LONGITUDE = "longitude"
    const val KEY_ID_LATITUDE = "latitude"



    //location notification




    //rescue notification
    const val RESCUE_NOTIFICATION_ID = 102
    const val RESCUE_NOTIFICATION_CHANNEL_ID = "rescue_notification_channel_id"
    const val RESCUE_NOTIFICATION_CHANNEL_NAME = "Rescue Confirmation"
    const val RESCUE_NOTIFICATION_CHANNEL_DESCRIPTION = "Rescue Confirmation Notification"



    const val MAXIMUM_HAZARDOUS_MARKER = 3
    const val MAPPING_URI = "cyclistance://mapping/"
    const val ACTION = "action"
    const val ACTION_OPEN_CONVERSATION = "conversation"
    const val ACTION_OPEN_RESCUE_REQUEST = "rescue_request"
    const val DEFAULT_ACTION = "default_action"

}