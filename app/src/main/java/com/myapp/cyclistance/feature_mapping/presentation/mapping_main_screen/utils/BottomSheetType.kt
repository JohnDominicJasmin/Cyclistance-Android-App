package com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.utils

sealed class BottomSheetType(val type: String) {
    data object RescuerArrived : BottomSheetType(type = "rescuer_arrived")
    data object DestinationReached : BottomSheetType(type = "destination_reached")
    data object SearchAssistance : BottomSheetType(type = "search_assistance")
    data object OnGoingRescue : BottomSheetType(type = "on_going_rescue")
    data object MapType : BottomSheetType(type = "map_type")

}
