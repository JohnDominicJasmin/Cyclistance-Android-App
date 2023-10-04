package com.example.cyclistance.service

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import com.example.cyclistance.MainActivity
import com.example.cyclistance.core.utils.constants.MessagingConstants.CONVERSATION_ID
import com.example.cyclistance.core.utils.constants.MessagingConstants.KEY_MESSAGE
import com.example.cyclistance.core.utils.constants.MessagingConstants.MESSAGING_URI
import com.example.cyclistance.core.utils.constants.MessagingConstants.NOTIFICATION_ID
import com.example.cyclistance.core.utils.constants.UtilConstants.KEY_NAME
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class MessagingService : FirebaseMessagingService() {

    @Inject lateinit var notificationManager: NotificationManagerCompat
    @Inject @Named("messagingNotification") lateinit var notificationBuilder: NotificationCompat.Builder

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.v("FirebaseMessagingService New token: $token")
    }


    @SuppressLint("MissingPermission")
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val data = message.data
        val conversationId = data[CONVERSATION_ID]!!
        val name = data[KEY_NAME]!!
        val receivedMessage = data[KEY_MESSAGE] ?: ""


        val uri =  "$MESSAGING_URI/$CONVERSATION_ID=$conversationId".toUri()
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

        val notificationStyle = NotificationCompat.BigTextStyle().bigText(receivedMessage)
        val notificationCompat = notificationBuilder.apply {
            setContentIntent(clickPendingIntent)
            setContentTitle("Message from $name")
            setContentText(receivedMessage)
            setStyle(notificationStyle)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                return
            }
        }

        notificationManager.notify(NOTIFICATION_ID, notificationCompat.build())


    }



}


