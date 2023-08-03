package com.example.cyclistance.feature_messaging.domain.model.helper

import com.example.cyclistance.core.utils.etc.EtcExtent.swap
import com.example.cyclistance.feature_messaging.domain.model.ui.chats.ChatItemModel
import com.example.cyclistance.feature_messaging.domain.model.ui.chats.MessagingUserItemModel

class MessagingUserHandler(
    private val messagingUserItem: MessagingUserItemModel = MessagingUserItemModel(),
    private val chatItem: ChatItemModel,
    val chats: MutableList<ChatItemModel>
){

    fun handleNewAddedChat(){
        with(messagingUserItem) {
            val name = userDetails.name
            val photo = userDetails.photo
            val isUserAvailable = userAvailability
            val index = chats.indexOfFirst { it.conversionId ==  chatItem.conversionId}
            val hasFound = index != -1
            if(hasFound){
                chats.set(index, element = chatItem.copy(conversionName = name, conversionPhoto = photo, isUserAvailable = isUserAvailable))
                return
            }
            chats.add(
                chatItem.copy(
                    conversionName = name,
                    conversionPhoto = photo,
                    isUserAvailable = isUserAvailable))
        }


    }

    fun handleModifiedChat(){
        val modifiedIndex = chats.indexOfFirst { chatItem.senderId == it.senderId && chatItem.receiverId == it.receiverId }
        val hasFound = modifiedIndex != -1
        if (hasFound) {
            chats[modifiedIndex] = chats[modifiedIndex].copy(
                lastMessage = chatItem.lastMessage,
                timeStamp = chatItem.timeStamp,
                isUserAvailable = chatItem.isUserAvailable
            )
            chats.swap(modifiedIndex, 0)
        }
    }
}