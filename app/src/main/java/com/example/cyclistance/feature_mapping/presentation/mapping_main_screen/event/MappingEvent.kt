package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.event

import com.example.cyclistance.feature_mapping.domain.model.MapSelectedRescuee
import com.example.cyclistance.feature_mapping.domain.model.RouteDirection


sealed class MappingEvent{
    object RequestHelpSuccess: MappingEvent()
    object InsufficientUserCredential: MappingEvent()
    object AcceptRescueRequestSuccess: MappingEvent()
    //
    data class LocationNotAvailable(val reason:String = "Tracking your Location"): MappingEvent()
    data class RescuerLocationNotAvailable(val reason:String = "Can't reach Rescuer"): MappingEvent()
    data class UnexpectedError(val reason:String? = "An unexpected error occurred."): MappingEvent()
    data class UserFailed(val reason: String? = "User not found"): MappingEvent()

    data class RespondToHelpSuccess(val reason:String = "Rescue request sent"): MappingEvent()
    data class AddressFailed(val reason: String? = "Rescue transaction not found"): MappingEvent()
    object NoInternetConnection: MappingEvent()


    data class NewSelectedRescuee(val selectedRescuee: MapSelectedRescuee): MappingEvent()


    object GetUserIdFailed: MappingEvent()
    object GetUserNameFailed: MappingEvent()
    object GetUserPhotoFailed: MappingEvent()

    data class NewRouteDirection(val routeDirection: RouteDirection): MappingEvent()
    object RemoveAssignedTransactionSuccess: MappingEvent()
    object RescueRequestAccepted: MappingEvent()
    object CancelHelpRequestSuccess: MappingEvent()

    object RescueHasTransaction: MappingEvent()
    object UserHasCurrentTransaction: MappingEvent()

    object DestinationReached: MappingEvent()
    object FailedToCalculateDistance: MappingEvent()
    data class RemoveRespondentFailed(val reason: String = "Failed to remove respondent"): MappingEvent()



}
