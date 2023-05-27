package com.example.cyclistance.feature_mapping.domain.model.api.user

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize

@Parcelize
@StableState
data class UserItem(
    val address: String? = null,
    val contactNumber: String? = null,
    val id: String? = null,
    val location: LocationModel? = null,
    val name: String? = null,
    val profilePictureUrl: String? = null,
    val rescueRequest: RescueRequest? = null,
    val transaction: TransactionModel? = null,
    val userAssistance: UserAssistanceModel? = null,
): Parcelable{
    companion object {
        fun empty(id:String?, role:String, transactionId:String) = UserItem(
            id = id, userAssistance = UserAssistanceModel(needHelp = false),
            transaction = TransactionModel(role = role, transactionId = transactionId),
            rescueRequest = RescueRequest()
        )
    }


}