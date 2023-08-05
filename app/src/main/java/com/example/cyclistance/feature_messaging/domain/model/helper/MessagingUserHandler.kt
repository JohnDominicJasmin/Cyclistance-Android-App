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

        val index = chats.indexOfFirst { it.second.conversionId == chatItem.conversionId }
        val hasFound = index != -1
        if (hasFound) {
            chats.set(index, element = Pair(first = messagingUserItem, second = chatItem))
            return
        }
        chats.add(
            Pair(first = messagingUserItem,
                second = chatItem))

    }

    fun handleModifiedChat() {
        val modifiedIndex = chats.indexOfFirst { chatItem.senderId == it.second.senderId && chatItem.receiverId == it.second.receiverId }
        val hasFound = modifiedIndex != -1
        if (hasFound) {
            chats[modifiedIndex] = chats[modifiedIndex].copy(first = messagingUserItem, second = chatItem
            )
            chats.swap(modifiedIndex, 0)
        }
    }
}