package com.example.cyclistance

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.CompositionLocalProvider
import com.example.cyclistance.core.utils.constants.LocationServiceConstants.LOCATION_CHANNEL_DESCRIPTION
import com.example.cyclistance.core.utils.constants.LocationServiceConstants.LOCATION_NOTIFICATION_NAME
import com.example.cyclistance.core.utils.constants.LocationServiceConstants.LOCATION_SERVICE_CHANNEL_ID
import com.example.cyclistance.core.utils.constants.MappingConstants
import com.example.cyclistance.core.utils.constants.MappingConstants.RESCUE_NOTIFICATION_CHANNEL_DESCRIPTION
import com.example.cyclistance.core.utils.constants.MappingConstants.RESCUE_NOTIFICATION_CHANNEL_ID
import com.example.cyclistance.core.utils.constants.MappingConstants.RESCUE_NOTIFICATION_CHANNEL_NAME
import com.example.cyclistance.core.utils.constants.MessagingConstants
import com.example.cyclistance.feature_authentication.domain.util.ActivityResultCallbackManager
import com.example.cyclistance.feature_authentication.domain.util.LocalActivityResultCallbackManager
import com.example.cyclistance.navigation.NavScreen
import com.facebook.FacebookSdk.sdkInitialize
import com.facebook.appevents.AppEventsLogger
import com.mapbox.mapboxsdk.Mapbox
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    @Inject
    lateinit var notificationManager: NotificationManager

    val mainViewModel: MainViewModel by viewModels()
    private var callbackManager = ActivityResultCallbackManager()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, getString(R.string.MapsDownloadToken))
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        setTheme(R.style.Theme_Cyclistance)
        sdkInitialize(applicationContext);
        AppEventsLogger.activateApp(application);
        addNotificationChannel()

        val action = intent.getStringExtra(MappingConstants.ACTION) ?: ""
        mainViewModel.setIntentAction(action)

        setContent {
            CompositionLocalProvider(LocalActivityResultCallbackManager provides callbackManager) {
                NavScreen()
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val conversationId = intent?.getStringExtra(MappingConstants.ACTION) ?: ""
        mainViewModel.setIntentAction(conversationId)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getTrackingNotificationChannel() = NotificationChannel(
        LOCATION_SERVICE_CHANNEL_ID,
        LOCATION_NOTIFICATION_NAME,
        NotificationManager.IMPORTANCE_LOW).apply {
        description = LOCATION_CHANNEL_DESCRIPTION
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getMessagingNotificationChannel() = NotificationChannel(
        MessagingConstants.CHANNEL_ID,
        MessagingConstants.CHANNEL_NAME,
        NotificationManager.IMPORTANCE_HIGH).apply {
        description = MessagingConstants.CHANNEL_DESCRIPTION

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getRescueNotificationChannel()=
         NotificationChannel(
            RESCUE_NOTIFICATION_CHANNEL_ID,
            RESCUE_NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH).apply {
            description = RESCUE_NOTIFICATION_CHANNEL_DESCRIPTION
        }



    private fun addNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannels(
                listOf(
                    getTrackingNotificationChannel(),
                    getMessagingNotificationChannel(),
                    getRescueNotificationChannel()

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






