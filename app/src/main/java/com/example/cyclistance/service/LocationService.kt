package com.example.cyclistance.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.cyclistance.core.utils.constants.MappingConstants
import com.example.cyclistance.core.utils.constants.MappingConstants.ACTION_START
import com.example.cyclistance.core.utils.constants.MappingConstants.ACTION_STOP
import com.example.cyclistance.core.utils.constants.MappingConstants.LOCATION_SERVICE_CHANNEL_ID
import com.example.cyclistance.feature_mapping.domain.location.LocationClient
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class LocationService(
): Service() {

    @Inject lateinit var locationClient: LocationClient
    @Inject lateinit var notification: NotificationCompat.Builder

    private lateinit var notificationManager: NotificationManager
    private var isServiceRunning: Boolean = false
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    companion object {
        val address: MutableStateFlow<Location> = MutableStateFlow(Location(""))
    }

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                LOCATION_SERVICE_CHANNEL_ID,
                MappingConstants.LOCATION_NAME,
                NotificationManager.IMPORTANCE_LOW)
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)


        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> {
                if(!isServiceRunning){
                    startService()
                    isServiceRunning = true
                }
            }
            ACTION_STOP -> {
                stopService()
                isServiceRunning = false
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }


    private fun startService() {
        locationClient.getLocationUpdates()
            .distinctUntilChanged()
            .catch {
                Timber.e("Start Service: ${it.message}")
            }.onEach { location ->
                address.emit(location)
            }.launchIn(serviceScope)
    }

    private fun stopService() {
        stopForeground(true)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    override fun onBind(intent: Intent?): android.os.IBinder? {
        return null
    }
}