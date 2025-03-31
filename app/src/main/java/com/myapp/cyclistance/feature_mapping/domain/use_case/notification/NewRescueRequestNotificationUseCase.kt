package com.myapp.cyclistance.feature_mapping.domain.use_case.notification

import android.annotation.SuppressLint
import android.app.Notification
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.myapp.cyclistance.core.utils.constants.MappingConstants

class NewRescueRequestNotificationUseCase(
    private val notificationManagerCompat: NotificationManagerCompat,
    private val notificationBuilder: NotificationCompat.Builder
) {
    @SuppressLint("MissingPermission")
    operator fun invoke(message: String){


        val notificationStyle = NotificationCompat.BigTextStyle().bigText(message)
        val notificationCompat = notificationBuilder.apply {

            setContentTitle("New Rescue Request")
            setContentText(message)
            setStyle(notificationStyle)
        }

        notificationManagerCompat.notify(
            MappingConstants.RESCUE_NOTIFICATION_ID,
            notificationCompat.build().apply { flags =
                Notification.FLAG_AUTO_CANCEL } )
    }
}