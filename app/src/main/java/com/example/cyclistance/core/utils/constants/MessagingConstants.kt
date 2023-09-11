package com.example.cyclistance.core.utils.constants

import androidx.datastore.preferences.core.stringPreferencesKey

object MessagingConstants {
    const val MESSAGING_VM_STATE_KEY = "messaging_vm_state_key"
    const val CONVERSATION_VM_STATE_KEY = "conversation_vm_state_key"
    const val SEARCH_USER_VM_STATE_KEY = "search_user_vm_state_key"
    const val KEY_FCM_TOKEN = "fcmToken"

    const val KEY_SENDER_ID = "senderId"
    const val KEY_RECEIVER_ID = "receiverId"
    const val KEY_MESSAGE = "message"
    const val KEY_TIMESTAMP = "timestamp"
    val SAVED_TOKEN = stringPreferencesKey("saved_token")


    const val KEY_CONVERSATIONS_COLLECTION = "conversations"
    const val KEY_COLLECTION_CHATS = "chats"
    const val KEY_LAST_MESSAGE = "lastMessage"
    const val KEY_AVAILABILITY = "availability"

    const val REMOTE_MSG_AUTHORIZATION = "Authorization"
    const val REMOTE_MSG_CONTENT_TYPE = "Content-Type"
    const val REMOTE_MSG_CONTENT_FORMAT = "application/json"
    const val REMOTE_MSG_DATA = "data"
    const val REMOTE_MSG_REGISTRATION_IDS = "registration_ids"



    //notifications
    const val NOTIFICATION_ID = 101
    const val CHANNEL_ID = "messaging_channel_id"
    const val CHANNEL_DESCRIPTION = "Messaging Notifications"
    const val NOTIFICATION_REQUEST_CODE = 200
    const val CHANNEL_NAME = "Messaging Notification"


    const val MESSAGING_URI = "cyclistance://messaging/conversation/"
    const val RECEIVER_MESSAGE_OBJ = "receiver_message"
    const val SENDER_MESSAGE_OBJ = "sender_message"




}