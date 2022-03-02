package com.example.cyclistance.feature_authentication.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.compose.rememberNavController
import com.example.cyclistance.navigation.Navigation
import com.example.cyclistance.feature_authentication.presentation.theme.CyclistanceTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalAnimationApi
@ExperimentalPagerApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            CyclistanceTheme(darkTheme = true) {
                val navController = rememberNavController()


                Navigation(
                    navController = navController)


                }
        }
    }
}






