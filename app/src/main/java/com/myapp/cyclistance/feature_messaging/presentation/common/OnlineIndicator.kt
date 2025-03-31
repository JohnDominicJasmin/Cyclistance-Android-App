package com.myapp.cyclistance.feature_messaging.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.myapp.cyclistance.theme.Atlantis
import com.myapp.cyclistance.theme.CyclistanceTheme

@Composable
fun OnlineIndicator(modifier: Modifier = Modifier, isOnline: Boolean) {

    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(MaterialTheme.colors.background)
            .padding(all = 3.dp)) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .background(color = if(isOnline) Atlantis else Color.Gray))

    }

}

@Preview
@Composable
fun PreviewOnlineIndicatorDark() {
    CyclistanceTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center) {

            OnlineIndicator(modifier = Modifier.size(30.dp), isOnline = false)

        }
    }
}

@Preview
@Composable
fun PreviewOnlineIndicatorLight() {
    CyclistanceTheme(darkTheme = false) {

        Box(modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background), contentAlignment = Alignment.Center) {

            OnlineIndicator(modifier = Modifier.size(30.dp), isOnline = false)
        }
    }
}

