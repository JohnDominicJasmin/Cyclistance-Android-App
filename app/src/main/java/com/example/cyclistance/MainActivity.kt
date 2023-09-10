package com.example.cyclistance

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import com.example.cyclistance.feature_authentication.domain.util.ActivityResultCallbackManager
import com.example.cyclistance.feature_authentication.domain.util.LocalActivityResultCallbackManager
import com.example.cyclistance.navigation.NavScreen
import com.facebook.FacebookSdk.sdkInitialize
import com.facebook.appevents.AppEventsLogger
import com.mapbox.mapboxsdk.Mapbox
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    @Named("messagingNotificationChannel") lateinit var notificationChannel: NotificationChannel

    @Inject
    @Named("rescueNotificationChannel") lateinit var rescueNotificationChannel: NotificationChannel

    @Inject lateinit var notificationManager: NotificationManager


    private var callbackManager = ActivityResultCallbackManager()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, getString(R.string.MapsDownloadToken))
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        setTheme(R.style.Theme_Cyclistance)
        sdkInitialize(applicationContext);
        AppEventsLogger.activateApp(application);
        addNotificationChannel()

        setContent {
            CompositionLocalProvider(LocalActivityResultCallbackManager provides callbackManager) {
                NavScreen()
            }
        }
    }


    private fun addNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannels(
                   listOf(
                      notificationChannel,
                      rescueNotificationChannel,
                 )
            )

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






