package com.example.cyclistance.feature_messaging.domain.model.helper

import com.example.cyclistance.core.utils.etc.EtcExtent.swap
import com.example.cyclistance.feature_messaging.domain.model.ui.chats.ChatItemModel
import com.example.cyclistance.feature_messaging.domain.model.ui.chats.MessagingUserItemModel

class MessagingUserHandler(
    private val currentChatUser: MessagingUserItemModel = MessagingUserItemModel(),
    private val chatInfo: ChatItemModel,
    val chats: MutableList<Pair<MessagingUserItemModel, ChatItemModel>>
) {

    fun handleNewAddedChat() {
        chats.add(
            Pair(first = currentChatUser,
                second = chatInfo))

    }

    fun handleModifiedChat() {
        val modifiedIndex = chats.indexOfFirst { chatInfo.senderId == it.second.senderId && chatInfo.receiverId == it.second.receiverId }
        val hasFound = modifiedIndex != -1
        if (hasFound) {

            chats.set(modifiedIndex, element = Pair(first = currentChatUser, second = chatInfo))
            chats.swap(modifiedIndex, 0)
        }
    }
}