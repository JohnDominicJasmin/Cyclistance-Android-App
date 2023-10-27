package com.example.cyclistance.feature_mapping.domain.model.remote_models.rescue_transaction

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import com.example.cyclistance.feature_mapping.domain.model.remote_models.user.LocationModel
import kotlinx.parcelize.Parcelize

@Parcelize
@StableState
data class RescueTransactionItem(
    val id: String? = null,
    val cancellation: CancellationModel? = null,
    val rescueeId: String? = null,
    val rescuerId: String? = null,
    val status: StatusModel? = null,
    val route: RouteModel? = null,
    val startingMillis: Long? = null,
    val endingMillis: Long? = null
):Parcelable{

    fun getCancellationMessage() = cancellation?.cancellationReason?.message ?: ""
    fun getCancellationReason() = cancellation?.cancellationReason?.reason ?: ""
    fun getCancellationId() = cancellation?.idCancelledBy ?: ""
    fun getCancellationName() = cancellation?.nameCancelledBy ?: ""
    fun isRescueCancelled() = cancellation?.rescueCancelled ?: false

    fun isRescueOnGoing() = status?.onGoing ?: false
    fun isRescueFinished() = status?.finished ?: false
    fun isRescueStarted() = status?.started ?: false

    fun getStartingLocation(): LocationModel? {
        return route?.startingLocation
    }

    fun getStartingLatitude(): Double? {
        return route?.startingLocation?.latitude
    }

    fun getStartingLongitude(): Double? {
        return route?.startingLocation?.longitude
    }

    fun getDestinationLatitude(): Double? {
        return route?.destinationLocation?.latitude
    }

    fun getDestinationLongitude(): Double? {
        return route?.destinationLocation?.longitude
    }

    fun getDestinationLocation(): LocationModel?{
        return route?.destinationLocation
    }
}
