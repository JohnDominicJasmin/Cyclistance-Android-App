package com.example.cyclistance.feature_messaging.presentation.conversation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cyclistance.R
import com.example.cyclistance.navigation.IsDarkTheme
import com.example.cyclistance.theme.Black500
import com.example.cyclistance.theme.CyclistanceTheme

@Composable
fun PlaceholderEmptyConversation(modifier: Modifier = Modifier) {
    val isDarkTheme = IsDarkTheme.current
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp, alignment = Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "No messages here yet...",
            color = Black500,
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.padding(all = 4.dp)
        )

        Icon(
            painter = painterResource(id = if (isDarkTheme) R.drawable.ic_mailbox_dark else R.drawable.ic_mailbox_light),
            contentDescription = "No Conversation Available",
            tint = Color.Unspecified
        )


    }
}

@Preview
@Composable
fun PreviewPlaceholderEmptyConversation() {
    CompositionLocalProvider(IsDarkTheme provides false) {
        CyclistanceTheme(darkTheme = false) {
            Box(modifier = Modifier.background(MaterialTheme.colors.background)) {
                PlaceholderEmptyConversation()
            }
        }
    }
}

