package com.example.cyclistance

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import com.example.cyclistance.core.utils.constants.MessagingConstants.CHANNEL_DESCRIPTION
import com.example.cyclistance.core.utils.constants.MessagingConstants.CHANNEL_IMPORTANCE
import com.example.cyclistance.core.utils.constants.MessagingConstants.CHANNEL_NAME
import com.example.cyclistance.core.utils.constants.MessagingConstants.MESSAGING_NOTIFICATION_CHANNEL
import com.example.cyclistance.feature_authentication.domain.util.ActivityResultCallbackManager
import com.example.cyclistance.feature_authentication.domain.util.LocalActivityResultCallbackManager
import com.example.cyclistance.navigation.NavScreen
import com.facebook.FacebookSdk.sdkInitialize
import com.facebook.appevents.AppEventsLogger
import com.mapbox.mapboxsdk.Mapbox
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity @Inject constructor(

) : ComponentActivity() {

    private var callbackManager = ActivityResultCallbackManager()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, getString(R.string.MapsDownloadToken))

        setTheme(R.style.Theme_Cyclistance)
        sdkInitialize(applicationContext);
        AppEventsLogger.activateApp(application);
        addMessagingNotificationChannel()

        setContent {
            CompositionLocalProvider(LocalActivityResultCallbackManager provides callbackManager) {
                NavScreen()
            }
        }
    }


    private fun addMessagingNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                MESSAGING_NOTIFICATION_CHANNEL,
                CHANNEL_NAME,
                CHANNEL_IMPORTANCE).apply {
                description = CHANNEL_DESCRIPTION
            }
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        if (!callbackManager.onActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

}






