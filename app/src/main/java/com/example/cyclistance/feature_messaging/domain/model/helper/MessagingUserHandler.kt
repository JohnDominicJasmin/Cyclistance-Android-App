package com.example.cyclistance.feature_messaging.domain.model.helper

import com.example.cyclistance.core.utils.etc.EtcExtent.swap
import com.example.cyclistance.feature_messaging.domain.model.ui.chats.ChatItemModel
import com.example.cyclistance.feature_messaging.domain.model.ui.chats.MessagingUserItemModel

class MessagingUserHandler(
    private val messagingUserItem: MessagingUserItemModel = MessagingUserItemModel(),
    private val chatItem: ChatItemModel,
    val chats: MutableList<Pair<MessagingUserItemModel, ChatItemModel>>
) {

    fun handleNewAddedChat() {
        chats.add(
            Pair(first = messagingUserItem,
                second = chatItem))

    }

    fun handleModifiedChat() {
        val modifiedIndex = chats.indexOfFirst { chatItem.senderId == it.second.senderId && chatItem.receiverId == it.second.receiverId }
        val hasFound = modifiedIndex != -1
        if (hasFound) {

            chats.set(modifiedIndex, element = Pair(first = messagingUserItem, second = chatItem))
            chats.swap(modifiedIndex, 0)
        }
    }
}