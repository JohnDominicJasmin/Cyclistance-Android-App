package com.myapp.cyclistance.di.mapping

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.myapp.cyclistance.R
import com.myapp.cyclistance.core.utils.constants.MappingConstants.ACTION
import com.myapp.cyclistance.core.utils.constants.MappingConstants.ACTION_OPEN_RESCUE_REQUEST
import com.myapp.cyclistance.core.utils.constants.MappingConstants.RESCUE_NOTIFICATION_CHANNEL_ID
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
    @Named("mappingNotification")
    fun provideMessagingNotification(@ApplicationContext context: Context):NotificationCompat.Builder{
        return lazy {
            NotificationCompat.Builder(context, RESCUE_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_app_notification_icon)
                .setOngoing(false)
                .setVibrate(longArrayOf(200,200,200))
                .setAutoCancel(true)
                .setShowWhen(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        }.value
    }



    @Provides
    @Singleton
    @Named("newRescueNotification")
    fun provideRescueNotification(
        @ApplicationContext context: Context,
        @Named("notificationContentIntent") intent: Intent,
        @Named("mappingNotification") notificationBuilder: NotificationCompat.Builder
    ): NotificationCompat.Builder{

        val contentIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        return lazy {
            notificationBuilder.apply {
                setContentIntent(contentIntent)
            }
        }.value
    }



    @Provides
    @Singleton
    @Named("requestAcceptedNotification")
    fun provideRequestAcceptedNotification(
        @ApplicationContext context: Context,
        @Named("notificationContentIntent") intent: Intent,
        @Named("mappingNotification") notificationBuilder: NotificationCompat.Builder
    ): NotificationCompat.Builder{

        val contentIntent = PendingIntent.getActivity(
            context, 0, intent.apply {
                putExtra(ACTION, ACTION_OPEN_RESCUE_REQUEST)
            }, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        return lazy {
            notificationBuilder.apply {
                setContentIntent(contentIntent)
            }
        }.value
    }






}