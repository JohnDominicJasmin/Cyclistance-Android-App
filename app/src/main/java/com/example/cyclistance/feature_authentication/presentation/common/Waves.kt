package com.example.cyclistance.feature_authentication.presentation.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import com.example.cyclistance.R

@Composable
fun Waves(topWaveLayoutId:String, bottomWaveLayoutId:String) {
    Image(
        contentDescription = "Top wave",
        painter = painterResource(id = R.drawable.ic_top_wave),
        modifier = Modifier
            .wrapContentSize()
            .layoutId(topWaveLayoutId)

    )

    Image(
        contentDescription = "Bottom wave",
        painter = painterResource(id = R.drawable.ic_bottom_wave),
        modifier = Modifier
            .wrapContentSize()
            .layoutId(bottomWaveLayoutId)
    )
}