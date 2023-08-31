package com.example.cyclistance.service

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import com.example.cyclistance.MainActivity
import com.example.cyclistance.R
import com.example.cyclistance.core.utils.constants.MessagingConstants
import com.example.cyclistance.core.utils.constants.MessagingConstants.KEY_MESSAGE
import com.example.cyclistance.core.utils.constants.MessagingConstants.MESSAGING_NOTIFICATION_ID
import com.example.cyclistance.core.utils.constants.MessagingConstants.MESSAGING_URI
import com.example.cyclistance.core.utils.constants.MessagingConstants.RECEIVER_MESSAGE_ARG
import com.example.cyclistance.core.utils.constants.MessagingConstants.SENDER_MESSAGE_ARG
import com.example.cyclistance.core.utils.constants.UtilConstants.KEY_NAME
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



        //need to swap the sender and receiver
        val data = message.data
        val userReceiverMessage = data[RECEIVER_MESSAGE_ARG]!!
        val userSenderMessage = data[SENDER_MESSAGE_ARG]!!
        val name = data[KEY_NAME]!!
        val message = data[KEY_MESSAGE] ?: ""



        val notificationBuilder = NotificationCompat.Builder(
            this,
            MessagingConstants.MESSAGING_NOTIFICATION_CHANNEL).apply {
            setSmallIcon(R.drawable.ic_app_icon_cyclistance)
            priority = NotificationCompat.PRIORITY_HIGH
            setAutoCancel(true)
            setShowWhen(true)
            setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
        }

        val uri = "$MESSAGING_URI/$RECEIVER_MESSAGE_ARG=$userSenderMessage&$SENDER_MESSAGE_ARG=$userReceiverMessage".toUri()
        val clickIntent = Intent(
            Intent.ACTION_VIEW,
            uri,
            this,
            MainActivity::class.java
        )
        val clickPendingIntent: PendingIntent = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(clickIntent)
            getPendingIntent(1, PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val notificationStyle = NotificationCompat.BigTextStyle().bigText(message)
        val notificationCompat = notificationBuilder.apply {
            setContentIntent(clickPendingIntent)
            setContentTitle("Message from $name")
            setContentText(message)
            setStyle(notificationStyle)
        }


        val notificationManagerCompat = NotificationManagerCompat.from(this)
        notificationManagerCompat.notify(MESSAGING_NOTIFICATION_ID, notificationCompat.build())


    }
}


