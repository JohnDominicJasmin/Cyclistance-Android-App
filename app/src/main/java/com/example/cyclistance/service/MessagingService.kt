package com.example.cyclistance.service

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.cyclistance.MainActivity
import com.example.cyclistance.R
import com.example.cyclistance.core.utils.constants.MessagingConstants
import com.example.cyclistance.core.utils.constants.MessagingConstants.KEY_FCM_TOKEN
import com.example.cyclistance.core.utils.constants.MessagingConstants.KEY_MESSAGE
import com.example.cyclistance.core.utils.constants.MessagingConstants.KEY_NAME
import com.example.cyclistance.core.utils.constants.MessagingConstants.KEY_UID
import com.example.cyclistance.core.utils.constants.MessagingConstants.MESSAGING_NOTIFICATION_ID
import com.example.cyclistance.core.utils.constants.MessagingConstants.NOTIFICATION_REQUEST_CODE
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber
import javax.inject.Inject

class MessagingService @Inject constructor(

) : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.v("FirebaseMessagingService New token: $token")
    }


    @SuppressLint("MissingPermission")
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Timber.v("Message received: $message" + message.notification?.body)

        val data = message.data
        val uid = data[KEY_UID] ?: "Empty Uid"
        val name = data[KEY_NAME] ?: "Empty Name"
        val fcmToken = data[KEY_FCM_TOKEN] ?: "Empty FcmToken"
        val message = data[KEY_MESSAGE] ?: ""

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(KEY_UID, uid)
            putExtra(KEY_NAME, name)
            putExtra(KEY_FCM_TOKEN, fcmToken)
        }
        val notificationBuilder = NotificationCompat.Builder(
            this,
            MessagingConstants.MESSAGING_NOTIFICATION_CHANNEL).apply {
            setSmallIcon(R.drawable.ic_app_icon_cyclistance)
            priority = NotificationCompat.PRIORITY_HIGH
            setAutoCancel(true)
            setShowWhen(true)
            setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            NOTIFICATION_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationStyle = NotificationCompat.BigTextStyle().bigText(message)
        val notificationCompat = notificationBuilder.apply {
            setContentIntent(pendingIntent)
            setContentTitle("Message from $name")
            setContentText(message)
            setStyle(notificationStyle)
        }


        val notificationManagerCompat = NotificationManagerCompat.from(this)
        notificationManagerCompat.notify(MESSAGING_NOTIFICATION_ID, notificationCompat.build())


    }
}


