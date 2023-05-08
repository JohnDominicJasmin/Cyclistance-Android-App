package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen

import com.example.cyclistance.feature_mapping.domain.model.MapSelectedRescuee
import com.example.cyclistance.feature_mapping.domain.model.RouteDirection


sealed class MappingUiEvent{
    object RequestHelpSuccess: MappingUiEvent()
    object InsufficientUserCredential: MappingUiEvent()
    object SignOutSuccess: MappingUiEvent()
    object AcceptRescueRequestSuccess: MappingUiEvent()
    //
    data class LocationNotAvailable(val reason:String = "Tracking your Location"): MappingUiEvent()
    data class RescuerLocationNotAvailable(val reason:String = "Can't reach Rescuer"): MappingUiEvent()
    data class UnexpectedError(val reason:String? = "An unexpected error occurred."): MappingUiEvent()
    data class UserFailed(val reason: String? = "User not found"): MappingUiEvent()

    data class RespondToHelpSuccess(val reason:String = "Rescue request sent"): MappingUiEvent()
    data class AddressFailed(val reason: String? = "Rescue transaction not found"): MappingUiEvent()
    object NoInternetConnection:MappingUiEvent()


    data class NewSelectedRescuee(val selectedRescuee: MapSelectedRescuee): MappingUiEvent()


    object GetUserIdFailed: MappingUiEvent()
    object GetUserNameFailed: MappingUiEvent()
    object GetUserPhotoFailed: MappingUiEvent()
    object SignOutFailed: MappingUiEvent()

    data class NewRouteDirection(val routeDirection: RouteDirection): MappingUiEvent()
    object RemoveAssignedTransactionSuccess: MappingUiEvent()
    object RescueRequestAccepted: MappingUiEvent()
    object CancelHelpRequestSuccess: MappingUiEvent()

    object RescueHasTransaction: MappingUiEvent()
    object UserHasCurrentTransaction: MappingUiEvent()

    object DestinationReached: MappingUiEvent()
    object FailedToCalculateDistance: MappingUiEvent()
    data class RemoveRespondentFailed(val reason: String = "Failed to remove respondent"): MappingUiEvent()



}
