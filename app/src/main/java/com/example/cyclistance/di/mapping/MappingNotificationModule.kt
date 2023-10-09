package com.example.cyclistance.di.mapping

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.cyclistance.MainActivity
import com.example.cyclistance.R
import com.example.cyclistance.core.utils.constants.MappingConstants.RESCUE_NOTIFICATION_CHANNEL_ID
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object MappingNotificationModule {

    @Provides
    @Singleton
    @Named("rescueNotification")
    fun provideRescueNotification(
        @ApplicationContext context: Context
    ): NotificationCompat.Builder{

        val clickIntent = Intent(context, MainActivity::class.java).apply {
            flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        val contentIntent = PendingIntent.getActivity(
            context, 0,
            clickIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        return lazy {
            NotificationCompat.Builder(context, RESCUE_NOTIFICATION_CHANNEL_ID)
                .setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.ic_app_notification_icon)
                .setOngoing(false)
                .setVibrate(longArrayOf(200,200,200))
                .setAutoCancel(true)
                .setShowWhen(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        }.value
    }



}