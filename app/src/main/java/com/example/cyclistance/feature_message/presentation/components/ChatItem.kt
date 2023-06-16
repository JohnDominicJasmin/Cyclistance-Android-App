package com.example.cyclistance.feature_message.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cyclistance.feature_message.domain.model.ui.MessageContent
import com.example.cyclistance.navigation.IsDarkTheme
import com.example.cyclistance.theme.CyclistanceTheme

@Composable
fun ChatItem(
    modifier: Modifier = Modifier,
    message: MessageContent,
    isSender: Boolean, contentAlignment: Alignment = Alignment.Center) {

    val isDarkTheme = IsDarkTheme.current
    val shouldShowElevation by remember { derivedStateOf { isDarkTheme && isSender } }

    Box(modifier = modifier.fillMaxWidth(), contentAlignment = contentAlignment) {

        Column {

            Card(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(horizontal = 8.dp),
                shape = RoundedCornerShape(12.dp),
                contentColor = if (isSender) MaterialTheme.colors.onSurface else MaterialTheme.colors.onPrimary,
                backgroundColor = if (isSender) MaterialTheme.colors.surface else MaterialTheme.colors.primaryVariant,
                elevation = if (shouldShowElevation) 0.dp else 2.dp,
            ) {

                Text(
                    text = message.content,
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(all = 12.dp),
                    style = MaterialTheme.typography.body1.copy(
                        textAlign = TextAlign.Start))
            }
        }
    }
}

@Preview
@Composable
fun PreviewChatItemSenderDark() {

    CyclistanceTheme(darkTheme = true) {
        ChatItem(
            isSender = true,
            message = MessageContent(
                senderId = "1",
                content = "Lorem ipsum dolor sit amet consectetur adipisicing elit. Maxime mollitia,\n" +
                          "molestiae quas vel sint commodi repudiandae consequuntur",
                recipientId = "2",
                dateSent = "11:40 am",
            ))
    }
}


@Preview
@Composable
fun PreviewChatItemSenderLight() {

    CyclistanceTheme(darkTheme = false) {
        ChatItem(
            isSender = true,
            message = MessageContent(
                senderId = "1",
                content = "Lorem ipsum dolor sit amet consectetur adipisicing elit. Maxime mollitia,\n" +
                          "molestiae quas vel sint commodi repudiandae consequuntur",
                recipientId = "2",
                dateSent = "11:40 am",
            ))
    }
}


@Preview
@Composable
fun PreviewChatItemRecipientDark() {

    CyclistanceTheme(darkTheme = true) {
        ChatItem(
            isSender = false,
            message = MessageContent(
                senderId = "1",
                content = "Lorem ipsum dolor sit amet consectetur adipisicing elit. Maxime mollitia,\n" +
                          "molestiae quas vel sint commodi repudiandae consequuntur",
                recipientId = "2",
                dateSent = "11:40 am",
            ))
    }
}

@Preview
@Composable
fun PreviewChatItemRecipientLight() {

    CyclistanceTheme(darkTheme = false) {
        ChatItem(
            isSender = false,
            message = MessageContent(
                senderId = "1",
                content = "Lorem ipsum dolor sit amet consectetur adipisicing elit. Maxime mollitia,\n" +
                          "molestiae quas vel sint commodi repudiandae consequuntur",
                recipientId = "2",
                dateSent = "11:40 am",
            ))
    }
}


