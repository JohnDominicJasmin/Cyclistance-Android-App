package com.example.cyclistance.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(
    primary = Orange300,
    primaryVariant = Orange30,
    secondary = Black850,
    secondaryVariant = Black445,
    background = Black900,
    surface = Black800,
    error = Red600,
    onPrimary = Black910,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
    onError = Color.White,
)

private val LightColorPalette = lightColors(
    primary = Orange300,
    primaryVariant = Orange30,
    secondary = White100,
    secondaryVariant = White200,
    background = White50,
    surface = White50,
    error = Red600,
    onPrimary = Black910,
    onSecondary = Black910,
    onBackground = Black910,
    onSurface = Black910,
    onError = Color.White

)

@Composable
fun CyclistanceTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val systemUiController = rememberSystemUiController()


    val colors = if (darkTheme) {
        systemUiController.setSystemBarsColor(
            color = DarkColorPalette.background
        )
        DarkColorPalette
    } else {
        systemUiController.setSystemBarsColor(
            color = DarkColorPalette.onBackground
        )
        LightColorPalette
    }


        MaterialTheme(
            colors = colors,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
}