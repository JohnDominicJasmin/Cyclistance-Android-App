package com.example.cyclistance.feature_mapping.domain.model

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.example.cyclistance.feature_mapping.data.remote.dto.user_dto.Location
import com.example.cyclistance.feature_mapping.data.remote.dto.user_dto.RescueRequest
import com.example.cyclistance.feature_mapping.data.remote.dto.user_dto.Transaction
import com.example.cyclistance.feature_mapping.data.remote.dto.user_dto.UserAssistance
import kotlinx.parcelize.Parcelize

@Parcelize
@Immutable
@Stable
data class UserItem(
    val address: String? = null,
    val contactNumber: String? = null,
    val id: String? = null,
    val location: Location? = null,
    val name: String? = null,
    val profilePictureUrl: String? = null,
    val rescueRequest: RescueRequest? = null,
    val transaction: Transaction? = null,
    val userAssistance: UserAssistance? = null,
): Parcelable{
    companion object{
        fun empty(id:String?, role:String, transactionId:String) = UserItem(
            id = id, userAssistance = UserAssistance(needHelp = false),
            transaction = Transaction(role = role, transactionId = transactionId),
            rescueRequest = RescueRequest()
        )

    }
}