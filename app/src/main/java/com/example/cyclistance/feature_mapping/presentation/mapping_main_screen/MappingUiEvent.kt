package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen




sealed class MappingUiEvent{
    object RequestHelpSuccess: MappingUiEvent()
    object InsufficientUserCredential: MappingUiEvent()
    object SignOutSuccess: MappingUiEvent()
    object AcceptRescueRequestSuccess: MappingUiEvent()
    data class RespondToHelpFailed(val reason:String = "Couldn't respond to help"): MappingUiEvent()
    data class TrackingLocation(val reason:String = "Tracking your Location"): MappingUiEvent()
    data class RescuerLocationNotAvailable(val reason:String = "Can't reach Rescuer"): MappingUiEvent()
    data class RemovingRespondentFailed(val reason:String = "Failed to Remove Respondent"): MappingUiEvent()
    data class UnexpectedError(val reason:String? = "An unexpected error occurred."): MappingUiEvent()
    data class UserFailed(val reason: String? = "User not found"): MappingUiEvent()
    data class RescueTransactionFailed(val reason: String? = "Rescue transaction not found"): MappingUiEvent()

}
