package com.example.cyclistance.feature_readable_displays.presentation.splash_screen.components

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cyclistance.R
import com.example.cyclistance.feature_readable_displays.presentation.splash_screen.SplashScreenViewModel
import com.example.cyclistance.navigation.Screens
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    splashScreenViewModel: SplashScreenViewModel = hiltViewModel(),
    navigateTo: (destination: String, popUpToDestination: String?) -> Unit,) {

    val screen by remember{ splashScreenViewModel.splashScreenState}

    val scale = remember { Animatable(initialValue = 0f) }
    LaunchedEffect(key1 = true) {
        scale.animateTo(0.9f,
            animationSpec = tween(
                durationMillis = 500,
                easing = { OvershootInterpolator(1.2f).getInterpolation(it) }
            ))
        delay(1200L)
            navigateTo(screen.navigationStartingDestination, Screens.SplashScreen.route)
        }


    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)) {
        Image(
            painter = painterResource(id = R.drawable.ic_cyclistance_app_icon),
            contentDescription = "App Icon", modifier = Modifier.scale(scale.value))

    }

}