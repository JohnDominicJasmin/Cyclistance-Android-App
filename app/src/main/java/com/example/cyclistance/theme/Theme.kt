package com.example.cyclistance.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(
    primary = Orange300,
    primaryVariant = Orange30,
    secondary = Black850,
    background = Black900,
    surface = Black800,
    error = _Red600,
    onPrimary = Color.Black,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
    onError = Color.White,
)

private val LightColorPalette = lightColors(

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
        LightColorPalette
    }


        MaterialTheme(
            colors = colors,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
}