package com.myapp.cyclistance.feature_mapping.domain.model.remote_models.user

import android.os.Parcelable
import com.myapp.cyclistance.core.utils.annotations.StableState
import com.myapp.cyclistance.feature_mapping.domain.model.Role
import kotlinx.parcelize.Parcelize

@Parcelize
@StableState
data class UserItem(
    val address: String? = null,
    val id: String? = null,
    val location: LocationModel? = null,
    val name: String? = null,
    val profilePictureUrl: String? = null,
    val rescueRequest: RescueRequest? = null,
    val transaction: TransactionModel? = null,
    val userAssistance: UserAssistanceModel? = null,
    val rescuePending: RescuePending? = null
): Parcelable{


    fun getRole() = transaction?.role

    fun isRescuee() = transaction?.role == Role.Rescuee.name
    fun isRescuer() = transaction?.role == Role.Rescuer.name

    fun getTransactionId() = transaction?.transactionId
    fun isUserNeedHelp() = userAssistance?.needHelp
    fun getMessage() = userAssistance?.confirmationDetail?.message
    fun getBikeType() = userAssistance?.confirmationDetail?.bikeType
    fun getDescription() = userAssistance?.confirmationDetail?.description
    fun isRescueRequestPending(id: String?) = rescuePending?.respondents?.any { it.clientId == id }



}