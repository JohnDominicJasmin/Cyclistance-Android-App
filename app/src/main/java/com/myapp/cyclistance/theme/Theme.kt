package com.myapp.cyclistance.theme

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
    secondary = Black850, // often used sa text fields
    secondaryVariant = Black445,
    background = Black900, // often used sa screen color
    surface = Black800, // often used sa dialogs, fabs and cards
    error = Red600, // error color
    onPrimary = Black910, // often used sa text
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
    background = White900,
    surface = White800,
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
            color = LightColorPalette.onBackground
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