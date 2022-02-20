package com.example.cyclistance.feature_authentication

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.cyclistance.R

@Composable
fun AppIcon(modifier : Modifier = Modifier, contentAlignment: Alignment ) {
    Box(contentAlignment = contentAlignment,
        modifier = Modifier.fillMaxSize()){
        Image(
            painter = painterResource(id = R.drawable.ic_cyclistance_app_icon),
            contentDescription = "App Icon", modifier = modifier)


}
}