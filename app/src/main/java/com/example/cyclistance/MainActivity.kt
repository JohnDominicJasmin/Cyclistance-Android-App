package com.example.cyclistance

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.compose.rememberNavController
import com.example.cyclistance.navigation.Navigation
import com.example.cyclistance.theme.CyclistanceTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import dagger.hilt.android.AndroidEntryPoint

const val CLICK_TIME_INTERVAL = 1800

@ExperimentalAnimationApi
@ExperimentalPagerApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        FacebookSdk.sdkInitialize(applicationContext);
//        AppEventsLogger.activateApp(application);
        var backPressedTime = 0L


        val onBackPressed = {
            val isDoubleClicked = backPressedTime + CLICK_TIME_INTERVAL > System.currentTimeMillis()

            if(isDoubleClicked){
                this.finish()
            }else{
                Toast.makeText(this, "Tap again to exit.", Toast.LENGTH_SHORT).show()
                backPressedTime = System.currentTimeMillis()
            }
        }


        setContent {
            CyclistanceTheme(darkTheme = true) {

                Navigation(
                    navController = rememberNavController(), onBackPressed = onBackPressed)




                }
        }
    }
}






