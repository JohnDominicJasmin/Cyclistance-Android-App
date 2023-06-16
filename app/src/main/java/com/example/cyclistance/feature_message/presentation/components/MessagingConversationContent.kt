package com.example.cyclistance.feature_message.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cyclistance.feature_message.domain.model.ui.Duration
import com.example.cyclistance.feature_message.domain.model.ui.MessageContent
import com.example.cyclistance.feature_message.domain.model.ui.MessageConversation
import com.example.cyclistance.theme.CyclistanceTheme

private val USER_ID = "1"
private val conversation = MessageConversation(
    messages = listOf(
        MessageContent(
            senderId = "1",
            recipientId = "2",
            content = "Hello",
            dateSent = "10:30 AM",
            duration = Duration.OneDay,

            ),
        MessageContent(
            senderId = "2",
            recipientId = "1",
            content = "How are you?",
            dateSent = "11:32 AM"
        ),
        MessageContent(
            senderId = "1",
            recipientId = "2",
            content = "I'm fine, thanks",
            dateSent = "11:35 AM"
        ),
        MessageContent(
            senderId = "1",
            recipientId = "2",
            content = "How about you?",
            dateSent = "FEB 13 12:12 AM",
            duration = Duration.OneMonth
        ),
        MessageContent(
            senderId = "2",
            recipientId = "1",
            content = "I'm fine too",
            dateSent = "11:42 AM",
            duration = Duration.OneHour
        ),
        MessageContent(
            senderId = "1",
            recipientId = "2",
            content = "Good to hear that",
            dateSent = "11:43 AM"
        ),
        MessageContent(
            senderId = "2",
            recipientId = "1",
            content = "orem ipsum dolor sit amet consectetur adipisicing elit. Maxime mollitia,\n" +
                      "molestiae quas vel sint commodi repudiandae consequuntur voluptatum laborum\n" +
                      "numquam blanditiis harum quisquam eius sed odit fugiat iusto fuga praesentium\n" +
                      "optio, eaque rerum! Provident similique accusantium nemo autem.",
            dateSent = "11:45 AM"
        ),
        MessageContent(
            senderId = "1",
            recipientId = "2",
            content = "Let's go for a ride?",
            dateSent = "11:50 AM"
        ),
        MessageContent(
            senderId = "2",
            recipientId = "1",
            content = "Sure",
            dateSent = "11:55 AM"
        ),

        )
)

@Composable
fun MessagingConversationContent() {

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
        LazyColumn {
            items(items = conversation.messages) { message ->

                val isSender by remember { derivedStateOf { message.senderId != USER_ID } }

                AnimatedVisibility(visible = message.duration != null) {

                    MessagingTimeStamp(
                        value = message.dateSent,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp))
                }


                ChatItem(
                    message = message,
                    isSender = isSender,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 6.dp),
                    contentAlignment = if (isSender) Alignment.CenterStart else Alignment.CenterEnd,
                )
            }
        }
    }

}


@Preview
@Composable
fun PreviewMessagingConversationContentDark() {
    CyclistanceTheme(darkTheme = true) {
        MessagingConversationContent()
    }
}

@Preview
@Composable
fun PreviewMessagingConversationContentLight() {
    CyclistanceTheme(darkTheme = false) {
        MessagingConversationContent()
    }
}

