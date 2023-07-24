package com.example.cyclistance.core.utils.constants

import androidx.datastore.preferences.core.stringPreferencesKey

object MessagingConstants {
    const val MESSAGING_VM_STATE_KEY = "messaging_vm_state_key"
    const val CONVERSATION_VM_STATE_KEY = "conversation_vm_state_key"
    const val KEY_FCM_TOKEN = "fcmToken"
    const val KEY_EMAIL = "email"
    const val KEY_PHOTO = "photo"
    const val KEY_NAME = "name"
    const val KEY_UID = "key_uid"
    const val KEY_CHAT_COLLECTION = "chat"
    const val KEY_SENDER_ID = "senderId"
    const val KEY_RECEIVER_ID = "receiverId"
    const val KEY_MESSAGE = "message"
    const val KEY_TIMESTAMP = "timestamp"
    val SAVED_TOKEN = stringPreferencesKey("saved_token")

    const val CHAT_ID = "chatId"
    const val CHAT_PHOTO_URL = "chatPhotoUrl"
    const val CHAT_NAME = "chatName"
}