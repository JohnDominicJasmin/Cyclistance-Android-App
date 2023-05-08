package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.utils

sealed class BottomSheetType(val type: String = ""){
    object RescuerArrived: BottomSheetType(type = "rescuer_arrived")
    object DestinationReached : BottomSheetType(type = "destination_reached")
    object SearchAssistance : BottomSheetType(type = "search_assistance")
    object OnGoingRescue: BottomSheetType(type = "on_going_rescue")
    object Collapsed: BottomSheetType(type = "collapsed")
}
