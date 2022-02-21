package com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.components

import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.cyclistance.R

@Composable
fun Waves(topWaveLayoutId:String, bottomWaveLayoutId:String) {
    CustomImage(
        layoutId = topWaveLayoutId,
        contentDescription = "Top wave",
        imageId = R.drawable.ic_top_wave,
        modifier = Modifier.wrapContentSize()
    )

    CustomImage(
        layoutId = bottomWaveLayoutId,
        contentDescription = "Bottom wave",
        imageId = R.drawable.ic_bottom_wave,
        modifier = Modifier.wrapContentSize()
    )
}