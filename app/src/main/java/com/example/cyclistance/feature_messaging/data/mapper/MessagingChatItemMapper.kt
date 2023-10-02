package com.example.cyclistance.feature_messaging.data.mapper

import com.example.cyclistance.core.utils.constants.MessagingConstants
import com.example.cyclistance.core.utils.constants.MessagingConstants.KEY_LAST_MESSAGE
import com.example.cyclistance.core.utils.constants.MessagingConstants.KEY_RECEIVER_ID
import com.example.cyclistance.core.utils.constants.MessagingConstants.KEY_SENDER_ID
import com.example.cyclistance.core.utils.constants.MessagingConstants.KEY_TIMESTAMP
import com.example.cyclistance.feature_messaging.domain.exceptions.MessagingExceptions
import com.example.cyclistance.feature_messaging.domain.model.ui.chats.ChatItemModel
import com.google.firebase.firestore.DocumentSnapshot

object MessagingChatItemMapper {
    fun DocumentSnapshot.toConversionChatItem(uid: String):ChatItemModel{

        val senderId = getString(KEY_SENDER_ID) ?: throw MessagingExceptions.GetChatsFailure(message = "Sender id not found")
        val receiverId = getString(KEY_RECEIVER_ID) ?: throw MessagingExceptions.GetChatsFailure(message = "Receiver id not found")
        val isSeen = getBoolean(MessagingConstants.KEY_IS_SEEN) ?: false

        return ChatItemModel(
            messageId = id,
            senderId = senderId,
            receiverId = receiverId,
            conversionId =  getString(if (senderId == uid) KEY_RECEIVER_ID else KEY_SENDER_ID) ?: throw MessagingExceptions.GetChatsFailure(message = "Conversion id not found"),
            lastMessage = getString(KEY_LAST_MESSAGE) ?: throw MessagingExceptions.GetChatsFailure(message = "Last message not found"),
            timeStamp = getTimestamp(KEY_TIMESTAMP)?.toDate(),
            isSeen = isSeen
        )
    }
}