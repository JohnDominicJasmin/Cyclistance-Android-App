package com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource

@Composable
fun CustomImage(
    layoutId: String,
    contentDescription: String? = null,
    imageId: Int,
    modifier: Modifier = Modifier
) {

    Box(modifier = Modifier
        .layoutId(layoutId)
        ) {
        Image(
            painter = painterResource(id = imageId),
            contentDescription = contentDescription,
            alignment = Alignment.Center,
            modifier = modifier,
        )
    }
}