package com.example.cyclistance.service

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber

class MessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.v("New token: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Timber.v("Message received: $message" + message.notification?.body)
    }
}


//fGtbXXW6Qp22e26yumWnU5:APA91bEgPAOKv-RMnQwlHPXze6mdoaLlxnML8-LUiazLECNWTxX3KhVGMcvG1F9mS0SdUay0IqB-OtQvkLOPVspOx32SZ32gYLQujV42P9mfINmK20nBr-XynRWf5hIpIbSf1MHmDPor