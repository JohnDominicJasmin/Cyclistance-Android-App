package com.example.cyclistance

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LiveData
import androidx.navigation.compose.rememberNavController
import com.example.cyclistance.feature_authentication.domain.util.ActivityResultCallbackManager
import com.example.cyclistance.feature_authentication.domain.util.LocalActivityResultCallbackManager
import com.example.cyclistance.feature_settings.presentation.setting_main_screen.SettingViewModel
import com.example.cyclistance.navigation.Navigation
import com.example.cyclistance.theme.CyclistanceTheme
import com.facebook.FacebookSdk.sdkInitialize
import com.facebook.appevents.AppEventsLogger
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.mapbox.mapboxsdk.Mapbox
import dagger.hilt.android.AndroidEntryPoint

const val CLICK_TIME_INTERVAL = 1800

@ExperimentalPermissionsApi
@ExperimentalAnimationApi
@ExperimentalPagerApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var callbackManager = ActivityResultCallbackManager()


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        sdkInitialize(applicationContext);
        AppEventsLogger.activateApp(application);


        Mapbox.getInstance(
            this,
            getString(R.string.MapsToken))

        var backPressedTime = 0L
        val onBackPressed = {
            val isDoubleClicked = backPressedTime + CLICK_TIME_INTERVAL > System.currentTimeMillis()

            if (isDoubleClicked) {
                this.finish()
            } else {
                Toast.makeText(this, "Tap again to exit.", Toast.LENGTH_SHORT).show()
                backPressedTime = System.currentTimeMillis()
            }
        }


        setContent {
            val settingViewModel: SettingViewModel = hiltViewModel()
            val isDarkThemeLiveData:LiveData<Boolean> = settingViewModel.isDarkTheme()
            val isDarkThemeState by isDarkThemeLiveData.observeAsState(initial = false)

            CyclistanceTheme(darkTheme = isDarkThemeState) {

                CompositionLocalProvider(LocalActivityResultCallbackManager provides callbackManager) {
                    Navigation(
                        navController = rememberNavController(),
                        onToggleTheme = { settingViewModel.toggleTheme() },
                        isDarkTheme = isDarkThemeState,
                        isDarkThemeLiveData = isDarkThemeLiveData,
                        onBackPressed = { settingViewModel.toggleTheme() })

                }
            }
        }
    }

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






