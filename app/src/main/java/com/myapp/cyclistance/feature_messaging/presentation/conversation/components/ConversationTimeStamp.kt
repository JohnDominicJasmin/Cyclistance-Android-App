package com.myapp.cyclistance.feature_messaging.presentation.conversation.components

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign


@Composable
fun MessagingTimeStamp(value: String, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = value,
        color = MaterialTheme.colors.onBackground,
        style = MaterialTheme.typography.caption,
        textAlign = TextAlign.Center)
}

