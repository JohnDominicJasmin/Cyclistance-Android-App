package com.example.cyclistance.di.mapping

import android.app.PendingIntent
import android.content.Context
import androidx.core.app.NotificationCompat
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
        @ApplicationContext context: Context,
        @Named("notificationContentIntentSingleTop") contentIntent: PendingIntent
    ): NotificationCompat.Builder{

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