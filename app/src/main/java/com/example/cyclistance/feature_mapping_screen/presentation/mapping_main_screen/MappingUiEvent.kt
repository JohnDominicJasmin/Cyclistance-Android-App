package com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen


sealed class BottomSheetType(val type: String = ""){
    object RescuerArrived: BottomSheetType(type = "rescuer_arrived")
    object DestinationReached : BottomSheetType(type = "destination_reached")
    object SearchAssistance : BottomSheetType(type = "search_assistance")
    object OnGoingRescue: BottomSheetType(type = "on_going_rescue")
}

sealed class MappingUiEvent{
    object ShowConfirmDetailsScreen: MappingUiEvent()
    object ShowEditProfileScreen: MappingUiEvent()
    object ShowSignInScreen: MappingUiEvent()
    data class ShowToastMessage(val message: String): MappingUiEvent()


}
