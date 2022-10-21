package com.example.cyclistance.feature_readable_displays.presentation.splash_screen

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.cyclistance.R
import com.example.cyclistance.navigation.Screens
import com.example.cyclistance.navigation.navigateScreenInclusively
import com.example.cyclistance.theme.CyclistanceTheme
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    splashScreenViewModel: SplashScreenViewModel = hiltViewModel(),
    paddingValues: PaddingValues,
    navController: NavController) {

    val state by splashScreenViewModel.state.collectAsState()
    SplashScreen(
        modifier = Modifier
            .padding(paddingValues)

    ) {
        navController.navigateScreenInclusively(
            state.navigationStartingDestination,
            Screens.SplashScreen.route)
    }


}

@Composable
private fun SplashScreen(modifier: Modifier, onNavigateScreen: () -> Unit) {
    val scale = remember { Animatable(initialValue = 0f) }
    LaunchedEffect(key1 = true) {
        scale.animateTo(0.9f,
            animationSpec = tween(
                durationMillis = 500,
                easing = { OvershootInterpolator(1.2f).getInterpolation(it) }
            ))

        delay(1200L)
        onNavigateScreen()

    }


    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)) {
        Image(
            painter = painterResource(id = R.drawable.ic_app_icon_cyclistance),
            contentDescription = "App Icon", modifier = Modifier.scale(scale.value))

    }
}

@Preview
@Composable
fun SplashScreenPreview() {
    CyclistanceTheme(darkTheme = true) {
        SplashScreen(modifier = Modifier, onNavigateScreen = {})
    }
}

