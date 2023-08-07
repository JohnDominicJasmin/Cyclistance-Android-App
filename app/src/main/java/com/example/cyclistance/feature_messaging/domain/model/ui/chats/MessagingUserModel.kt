package com.example.cyclistance.feature_messaging.domain.model.ui.chats

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize

@StableState
@Parcelize
data class MessagingUserModel(
    val users: List<MessagingUserItemModel> = emptyList()
) : Parcelable{
    companion object{
        fun MessagingUserModel.filterWithout(userUid: String): MessagingUserModel {
            return MessagingUserModel(users.filter { it.userDetails.uid != userUid })
        }

        fun MessagingUserModel.findUser(userUid: String): MessagingUserItemModel? {
            return users.find { it.userDetails.uid == userUid }
        }
    }
}
