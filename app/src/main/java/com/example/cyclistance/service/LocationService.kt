package com.example.cyclistance.service

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.cyclistance.core.utils.constants.LocationServiceConstants
import com.example.cyclistance.core.utils.constants.MappingConstants.ACTION_START
import com.example.cyclistance.core.utils.constants.MappingConstants.ACTION_START_FOREGROUND
import com.example.cyclistance.core.utils.constants.MappingConstants.ACTION_STOP
import com.example.cyclistance.core.utils.constants.MappingConstants.ACTION_STOP_FOREGROUND
import com.example.cyclistance.feature_mapping.domain.location.LocationClient
import com.example.cyclistance.feature_mapping.domain.model.remote_models.user.LocationModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class LocationService(
): Service() {

    @Inject lateinit var locationClient: LocationClient
    @Inject @Named("trackingNotification") lateinit var notification: NotificationCompat.Builder

    @Inject lateinit var notificationManager: NotificationManager

    private var isServiceRunning: Boolean = false
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    companion object {
        val address: MutableStateFlow<LocationModel> = MutableStateFlow(LocationModel())
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
            ACTION_START_FOREGROUND -> {
                startForeground(LocationServiceConstants.LOCATION_NOTIFICATION_ID, notification.build())
            }
            ACTION_STOP_FOREGROUND -> {
                stopForeground(true)
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
                val speed = location.speed * 3600 / 1000
                address.emit(LocationModel(latitude = location.latitude, longitude = location.longitude, speed = speed.toDouble()))
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