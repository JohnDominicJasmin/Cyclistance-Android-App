package com.myapp.cyclistance.feature_messaging.presentation.conversation.state

import android.os.Parcelable
import com.myapp.cyclistance.core.utils.annotations.StableState
import com.myapp.cyclistance.feature_messaging.domain.model.ui.chats.MessagingUserItemModel
import com.myapp.cyclistance.feature_messaging.domain.model.ui.conversation.ConversationsModel
import kotlinx.parcelize.Parcelize


@Parcelize
@StableState
data class ConversationState(
    val userReceiverMessage: MessagingUserItemModel? = null,
    val userSenderMessage: MessagingUserItemModel? = null,
    val userUid: String = "",
    val userName: String = "",
    val conversionId: String? = null,
    val isLoading: Boolean = false,
    val conversationsModel: ConversationsModel? = null,
) : Parcelable{
    fun getReceiverId():String {
        return userReceiverMessage?.getUid() ?: ""
    }

    fun getReceiverToken(): String{
        return userReceiverMessage?.fcmToken!!
    }
}
