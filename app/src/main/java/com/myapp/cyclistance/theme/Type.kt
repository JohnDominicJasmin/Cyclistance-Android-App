package com.myapp.cyclistance.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType

// Set of Material typography styles to start with
val Typography = Typography(
    h1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Light,
        fontSize = TextUnit(96f, TextUnitType.Sp),
        letterSpacing = TextUnit(-1.5f, TextUnitType.Sp)
    ),
    h2 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Light,
        fontSize = TextUnit(60f, TextUnitType.Sp),
        letterSpacing = TextUnit(-0.5f, TextUnitType.Sp)
    ),
    h3 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = TextUnit(48f, TextUnitType.Sp),
        letterSpacing = TextUnit(0f, TextUnitType.Sp)
    ),
    h4 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = TextUnit(34f, TextUnitType.Sp),
        letterSpacing = TextUnit(0.25f, TextUnitType.Sp)
    ),
    h5 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = TextUnit(24f, TextUnitType.Sp),
        letterSpacing = TextUnit(0f, TextUnitType.Sp)
    ),
    h6 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = TextUnit(20f, TextUnitType.Sp),
        letterSpacing = TextUnit(0.15f, TextUnitType.Sp)
    ),
    subtitle1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = TextUnit(16f, TextUnitType.Sp),
        letterSpacing = TextUnit(0.15f, TextUnitType.Sp)
    ),
    subtitle2 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = TextUnit(14f, TextUnitType.Sp),
        letterSpacing = TextUnit(0.1f, TextUnitType.Sp)
    ),
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = TextUnit(16f, TextUnitType.Sp),
        letterSpacing = TextUnit(0.5f, TextUnitType.Sp)
    ),
    body2 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = TextUnit(14f, TextUnitType.Sp),
        letterSpacing = TextUnit(0.25f, TextUnitType.Sp)
    ),
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = TextUnit(14f, TextUnitType.Sp),
        letterSpacing = TextUnit(1.25f, TextUnitType.Sp)
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = TextUnit(12f, TextUnitType.Sp),
        letterSpacing = TextUnit(0.4f, TextUnitType.Sp)
    ),
    overline = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = TextUnit(10f, TextUnitType.Sp),
        letterSpacing = TextUnit(1.5f, TextUnitType.Sp)
    )


)