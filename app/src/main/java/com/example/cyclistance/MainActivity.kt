package com.example.cyclistance

import android.os.Bundle
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cyclistance.feature_authentication.AppIcon
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.components.SignInScreen
import com.example.cyclistance.feature_authentication.presentation.theme.BackgroundColor
import com.example.cyclistance.feature_authentication.presentation.theme.CyclistanceTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            CyclistanceTheme { // todo add theming(dark mode and light mode

                    Navigation()

                }

        }
    }
}

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.SplashScreen.route){

        composable(Screen.SplashScreen.route){
            SplashScreen(navController = navController)

        }

        composable(Screen.MainScreen.route){
            SignInScreen()
        }
        
    }
}




@Composable
fun SplashScreen(navController: NavController) {
    val scale = remember{ Animatable(initialValue = 0f) }
    LaunchedEffect(key1 = true){
        scale.animateTo(0.9f,
            animationSpec = tween(
                durationMillis = 500,
                easing =  {  OvershootInterpolator(1.2f).getInterpolation(it) }
        ))
        delay(1200L)

        navController.navigate(Screen.MainScreen.route){
            popUpTo(Screen.SplashScreen.route){ inclusive = true }
            launchSingleTop = true
        }
    }

    Box(contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize().background(BackgroundColor)){
        Image(
            painter = painterResource(id = R.drawable.ic_cyclistance_app_icon),
            contentDescription = "App Icon", modifier = Modifier.scale(scale.value))

    }

}
