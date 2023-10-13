package com.example.cyclistance.di.location_service

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.cyclistance.MainActivity
import com.example.cyclistance.R
import com.example.cyclistance.core.utils.constants.LocationServiceConstants.LOCATION_SERVICE_CHANNEL_ID
import com.example.cyclistance.core.utils.constants.MappingConstants
import com.example.cyclistance.feature_mapping.data.data_source.local.location.DefaultLocationClient
import com.example.cyclistance.feature_mapping.domain.location.LocationClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.Priority
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Named


@Module
@InstallIn(ServiceComponent::class)

object LocationServiceModule {

    @Provides
    @ServiceScoped
    fun provideLocationRequest(): LocationRequest {
        return lazy {
            LocationRequest.create()
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .setInterval(MappingConstants.LOCATION_UPDATES_INTERVAL)
                .setFastestInterval(MappingConstants.FASTEST_LOCATION_UPDATES_INTERVAL)
//                .setSmallestDisplacement(1.5f)
        }.value
    }


    @Provides
    @ServiceScoped
    fun provideLocationClient(
        locationRequest: LocationRequest,
        @ApplicationContext context: Context,

        ): LocationClient {
        return DefaultLocationClient(
            locationRequest = locationRequest,
            context = context,
        )
    }

    @Provides
    @ServiceScoped
    @Named("trackingNotification")
    fun provideNotificationBuilder(
        @ApplicationContext context: Context,
    ): NotificationCompat.Builder {

        val intent = Intent(context, MainActivity::class.java).apply {
            flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        val contentIntent = PendingIntent.getActivity(
            context,
            120,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        return lazy {
            NotificationCompat.Builder(context, LOCATION_SERVICE_CHANNEL_ID)
                .setContentIntent(contentIntent)
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_app_notification_icon)
                .setContentTitle("Cyclistance")
                .setContentText("Tracking your ride")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setVibrate(longArrayOf(200, 200, 200, 200))
                .setAutoCancel(false)
        }.value

    }


}