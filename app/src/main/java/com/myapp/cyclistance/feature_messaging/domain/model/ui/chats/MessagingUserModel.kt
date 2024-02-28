package com.myapp.cyclistance.feature_messaging.domain.model.ui.chats

import android.os.Parcelable
import com.myapp.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize

@StableState
@Parcelize
data class MessagingUserModel(
    val users: List<MessagingUserItemModel>
) : Parcelable{
    @StableState
    constructor(): this(
        users = emptyList())


    companion object{
        fun MessagingUserModel.filterWithout(userUid: String): MessagingUserModel {
            return MessagingUserModel(users.filter { it.userDetails.uid != userUid })
        }

        fun MessagingUserModel.findUser(userUid: String): MessagingUserItemModel? {
            return users.find { it.userDetails.uid == userUid }
        }
    }
}
