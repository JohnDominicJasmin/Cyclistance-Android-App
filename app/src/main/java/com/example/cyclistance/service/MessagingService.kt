package com.example.cyclistance.service

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import com.example.cyclistance.core.utils.app.AppUtils.isAppInForeground
import com.example.cyclistance.core.utils.constants.MappingConstants
import com.example.cyclistance.core.utils.constants.MappingConstants.ACTION_OPEN_CONVERSATION
import com.example.cyclistance.core.utils.constants.MappingConstants.MAPPING_URI
import com.example.cyclistance.core.utils.constants.MessagingConstants.KEY_MESSAGE
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

        var messageData = message.data
//        val conversationId = messageData[CONVERSATION_ID]!!
        val name = messageData[KEY_NAME]!!
        val receivedMessage = messageData[KEY_MESSAGE] ?: ""


        val uri = "$MAPPING_URI/${MappingConstants.ACTION}=$ACTION_OPEN_CONVERSATION".toUri()
        val clickIntent = Intent(
            Intent.ACTION_VIEW,
            uri
        ).apply {
            putExtra(MappingConstants.ACTION, ACTION_OPEN_CONVERSATION)
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val clickPendingIntent: PendingIntent = PendingIntent.getActivity(
            this,
            0,
            clickIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)


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

        if (!isAppInForeground(this)) {
            notificationManager.notify(NOTIFICATION_ID, notificationCompat.build())
        }

    }


}


