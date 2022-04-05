package com.example.cyclistance.feature_authentication.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.rememberNavController
import com.example.cyclistance.navigation.Navigation
import com.example.cyclistance.feature_authentication.presentation.theme.CyclistanceTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@ExperimentalAnimationApi
@ExperimentalPagerApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var backPressedTime = 0L

        val onBackPressed = {
            val isDoubleClicked = backPressedTime + 1800 > System.currentTimeMillis()

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






