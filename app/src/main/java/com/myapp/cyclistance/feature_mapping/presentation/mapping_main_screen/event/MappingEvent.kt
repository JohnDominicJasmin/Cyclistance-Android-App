package com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.event

import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.hazardous_lane.HazardousLaneMarkerDetails
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.rescue_transaction.RouteDirection
import com.myapp.cyclistance.feature_mapping.domain.model.ui.rescue.MapSelectedRescuee
import com.myapp.cyclistance.feature_report_account.domain.model.BannedAccountDetails


sealed class MappingEvent{
    data object RequestHelpSuccess: MappingEvent()
    data object InsufficientUserCredential: MappingEvent()
    data object AcceptRescueRequestSuccess: MappingEvent()
    //
    data class LocationNotAvailable(val reason:String = "Tracking your Location"): MappingEvent()
    data class RescuerLocationNotAvailable(val reason:String = "Can't reach Rescuer"): MappingEvent()
    data class UnexpectedError(val reason:String? = "An unexpected error occurred."): MappingEvent()
    data class UserFailed(val reason: String? = "User not found"): MappingEvent()

    data class RespondToHelpSuccess(val reason:String = "Rescue request sent"): MappingEvent()
    data class AddressFailed(val reason: String? = "Rescue transaction not found"): MappingEvent()
    data object NoInternetConnection: MappingEvent()
    data class AccountBanned(val bannedAccountDetails: BannedAccountDetails): MappingEvent()


    data class NewSelectedRescuee(val selectedRescuee: MapSelectedRescuee): MappingEvent()
    data class NewBottomSheetType(val bottomSheetType: String): MappingEvent()



    data object CancelRescueTransactionSuccess: MappingEvent()
    data object RescueRequestAccepted: MappingEvent()
    data object CancelHelpRequestSuccess: MappingEvent()

    data object RescueHasTransaction: MappingEvent()
    data object UserHasCurrentTransaction: MappingEvent()

    data object FailedToCalculateDistance: MappingEvent()
    data class RemoveRespondentFailed(val reason: String = "Failed to remove respondent"): MappingEvent()

    data object ReportIncidentSuccess: MappingEvent()
    data object IncidentDistanceTooFar: MappingEvent()
    data class ReportIncidentFailed(val reason: String): MappingEvent()
    data class SelectHazardousLaneMarker(val marker: HazardousLaneMarkerDetails): MappingEvent()
    data object UpdateIncidentSuccess: MappingEvent()
    data class UpdateIncidentFailed(val reason: String): MappingEvent()

    data object DeleteHazardousLaneMarkerSuccess: MappingEvent()
    data class DeleteHazardousLaneMarkerFailed(val reason: String): MappingEvent()

    data class GenerateRouteNavigationSuccess(val routeDirection: RouteDirection): MappingEvent()
    data class GenerateRouteNavigationFailed(val reason: String = "Failed to generate route navigation"): MappingEvent()

    data object RescueArrivedSuccess: MappingEvent()
    data class RescueArrivedFailed(val reason: String): MappingEvent()



    data object CancelRespondSuccess: MappingEvent()
}
