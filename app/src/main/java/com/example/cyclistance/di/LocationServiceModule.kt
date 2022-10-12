package com.example.cyclistance.di

import android.content.Context
import androidx.core.app.NotificationCompat
import com.example.cyclistance.BaseApplication
import com.example.cyclistance.R
import com.example.cyclistance.core.utils.constants.MappingConstants
import com.example.cyclistance.core.utils.location.DefaultLocationClient
import com.example.cyclistance.core.utils.location.LocationClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.Priority
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

object LocationServiceModule {

    @Provides
    @Singleton
    fun provideLocationRequest(): LocationRequest {
        return LocationRequest.create()
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .setInterval(MappingConstants.LOCATION_UPDATES_INTERVAL)
            .setFastestInterval(MappingConstants.FASTEST_LOCATION_UPDATES_INTERVAL)
            .setSmallestDisplacement(2f)
    }

    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(
        @ApplicationContext context: Context
    ): FusedLocationProviderClient {
        return FusedLocationProviderClient(context)
    }

    @Provides
    @Singleton
    fun provideLocationClient(
        locationRequest: LocationRequest,
        @ApplicationContext context: Context,
        fusedLocationProviderClient: FusedLocationProviderClient

    ): LocationClient {
        return DefaultLocationClient(
            locationRequest = locationRequest,
            context = context,
            fusedLocationProviderClient = fusedLocationProviderClient,
            externalScope = (context.applicationContext as BaseApplication).applicationScope
        )
    }





    @Provides
    @Singleton
    fun provideNotificationBuilder(
        @ApplicationContext context: Context
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, MappingConstants.LOCATION_SERVICE_CHANNEL_ID)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("Cyclistance")
            .setContentText("Tracking your ride")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true);

    }
}