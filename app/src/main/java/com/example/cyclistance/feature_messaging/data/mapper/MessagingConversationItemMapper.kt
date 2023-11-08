package com.example.cyclistance.feature_messaging.data.mapper

import com.example.cyclistance.core.utils.constants.MessagingConstants
import com.example.cyclistance.core.utils.date.DateUtils.toReadableDateTime
import com.example.cyclistance.feature_messaging.domain.exceptions.MessagingExceptions
import com.example.cyclistance.feature_messaging.domain.model.ui.conversation.ConversationItemModel
import com.google.firebase.firestore.DocumentSnapshot

object MessagingConversationItemMapper {

    fun DocumentSnapshot.toConversationItem(): ConversationItemModel{
        val messageId = id
        val senderId = getString(MessagingConstants.KEY_SENDER_ID) ?: throw MessagingExceptions.ListenMessagingFailure(
                message = "Sender id not found")
        val receiverId = getString(MessagingConstants.KEY_RECEIVER_ID) ?: throw MessagingExceptions.ListenMessagingFailure(
                message = "Receiver id not found")
        val messageText = getString(MessagingConstants.KEY_MESSAGE) ?: throw MessagingExceptions.ListenMessagingFailure(
                message = "Message not found")
        val timeStamp = getDate(MessagingConstants.KEY_TIMESTAMP) ?: throw MessagingExceptions.ListenMessagingFailure(
                message = "Timestamp not found")
        val isSeen = getBoolean(MessagingConstants.KEY_IS_SEEN) ?: false

        return ConversationItemModel(
            messageId = messageId,
            senderId = senderId,
            receiverId = receiverId,
            message = messageText,
            timestamp = timeStamp.toReadableDateTime(pattern = "MMM dd hh:mm a"),
            isSeen = isSeen,
            messageDuration = null,
            isSent = false
        )
    }

}