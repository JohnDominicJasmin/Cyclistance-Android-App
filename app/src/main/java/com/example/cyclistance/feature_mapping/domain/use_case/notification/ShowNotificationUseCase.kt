package com.example.cyclistance.feature_mapping.domain.use_case.notification

import android.annotation.SuppressLint
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.cyclistance.core.utils.constants.MappingConstants

class ShowNotificationUseCase(
    private val notificationManagerCompat: NotificationManagerCompat,
    private val notificationBuilder: NotificationCompat.Builder
) {
    @SuppressLint("MissingPermission")
    operator fun invoke(title: String, message: String){
        val notificationStyle = NotificationCompat.BigTextStyle().bigText(message)
        val notificationCompat = notificationBuilder.apply {
            setContentTitle(title)
            setContentText(message)
            setStyle(notificationStyle)
        }
        notificationManagerCompat.notify(
            MappingConstants.RESCUE_NOTIFICATION_ID,
            notificationCompat.build())
    }
}