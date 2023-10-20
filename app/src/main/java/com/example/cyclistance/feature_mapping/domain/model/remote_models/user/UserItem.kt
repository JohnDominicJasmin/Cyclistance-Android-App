package com.example.cyclistance.feature_mapping.domain.model.remote_models.user

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import com.example.cyclistance.feature_mapping.domain.model.Role
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

    fun assignTransaction(role:String, transactionId:String) = UserItem(
        id = id, userAssistance = userAssistance?.copy(needHelp = false),
        transaction = TransactionModel(role = role, transactionId = transactionId),
        rescueRequest = RescueRequest()
    )

    companion object {

        fun cancelUserHelpRequest(id: String): UserItem {
            return UserItem(
                id = id,
                userAssistance = UserAssistanceModel(
                    needHelp = false,
                    confirmationDetail = ConfirmationDetailModel()
                )
            )
        }

        fun removeUserTransaction(id: String): UserItem{
            return UserItem(
                id = id,
                transaction = TransactionModel(),
                userAssistance = UserAssistanceModel(needHelp = false),
                rescueRequest = RescueRequest()
            )
        }

    }


}