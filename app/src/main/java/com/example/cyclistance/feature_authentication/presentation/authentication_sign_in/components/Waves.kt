package com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.components

import androidx.compose.runtime.Composable
import com.example.cyclistance.R

@Composable
fun Waves() {
    CustomImage(
        layoutId = ConstraintsItem.TopWave.layoutId,
        contentDescription = "Top wave",
        imageId = R.drawable.ic_top_wave
    )

    CustomImage(
        layoutId = ConstraintsItem.BottomWave.layoutId,
        contentDescription = "Bottom wave",
        imageId = R.drawable.ic_bottom_wave
    )
}