package com.example.cyclistance.di.location_service

import android.content.Context
import androidx.core.app.NotificationCompat
import com.example.cyclistance.R
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
                .setSmallestDisplacement(1.5f)
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
    fun provideNotificationBuilder(
        @ApplicationContext context: Context
    ): NotificationCompat.Builder {
        return lazy {
            NotificationCompat.Builder(context, MappingConstants.LOCATION_SERVICE_CHANNEL_ID)
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Cyclistance")
                .setContentText("Tracking your ride")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
        }.value

    }
}