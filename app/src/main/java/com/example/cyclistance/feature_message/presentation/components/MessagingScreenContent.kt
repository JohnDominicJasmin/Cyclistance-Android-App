package com.example.cyclistance.feature_message.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.example.cyclistance.feature_message.domain.model.ui.MessageItemModel
import com.example.cyclistance.feature_message.domain.model.ui.MessagesModel
import com.example.cyclistance.theme.CyclistanceTheme

@Composable
fun MessagingScreenContent(modifier: Modifier = Modifier, messagesModel: MessagesModel) {


    val messageAvailable by remember { derivedStateOf { messagesModel.messages.isNotEmpty() } }

    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colors.background) {

        if (messageAvailable) {
            Box(contentAlignment = Alignment.TopCenter, modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.SpaceEvenly) {

                    MessagingSearchBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                            .padding(horizontal = 8.dp),

                        value = "",
                        onValueChange = {},
                        onClickClearSearch = {})

                    Text(
                        text = "Recent Messages",
                        style = MaterialTheme.typography.body2.copy(
                            letterSpacing = TextUnit(
                                2f,
                                type = TextUnitType.Sp)),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 12.dp),
                    )

                    MessagesSection(messagesModel = messagesModel)
                }
            }
        } else {

            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

                Text(
                    text = "No messages found",
                    color = MaterialTheme.colors.onBackground.copy(alpha = 0.75f),
                    style = MaterialTheme.typography.subtitle1)
            }
        }

    }
}

@Composable
private fun MessagesSection(modifier: Modifier = Modifier, messagesModel: MessagesModel) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()) {

        items(messagesModel.messages) { item ->
            MessagingItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                messageItemModel = item
            )
        }
    }
}


private val messages = MessagesModel(
    listOf(
        MessageItemModel(
            userImage = "https://www.liquidsandsolids.com/wp-content/uploads/2022/09/talking-to-a-dead-person.jpg",
            name = "John Doe",
            message = "Hey there! How are you?",
            time = "12:00",
            unreadMessages = 4,
            isMessageSent = false
        ),
        MessageItemModel(
            userImage = "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8cGVyc29ufGVufDB8fDB8fHww&w=1000&q=80",
            name = "Jane Doe",
            message = "Lorem ipsum dolor sit amet consectetur adipisicing elit. Maxime mollitia,\n" +
                      "molestiae quas vel sint commodi repudiandae consequuntur voluptatum laborum\n" +
                      "numquam blanditiis harum quisquam eius sed odit fugiat iusto fuga praesentium\n" +
                      "optio, eaque rerum! Provident similique accusantium nemo autem.",
            time = "12:00",
            unreadMessages = 10,
            isMessageSent = true
        ),
        MessageItemModel(
            userImage = "https://www.diethelmtravel.com/wp-content/uploads/2016/04/bill-gates-wealthiest-person.jpg",
            name = "Jennifer",
            message = "Lorem ipsum dolor sit amet consectetur adipisicing elit. Maxime mollitia,\n" +
                      "molestiae quas vel sint commodi repudiandae consequuntur voluptatum laborum\n" +
                      "numquam blanditiis harum quisquam eius sed odit fugiat iusto fuga praesentium\n" +
                      "optio, eaque rerum! Provident similique accusantium nemo autem.",
            time = "12:00",
            unreadMessages = 0,
            isMessageSent = true
        ),
        MessageItemModel(
            userImage = "https://www.harleytherapy.co.uk/counselling/wp-content/uploads/16297800391_5c6e812832.jpg",
            name = "John Doe",
            message = "Hello",
            time = "12:00",
            unreadMessages = 0,
            isMessageSent = true
        ),
        MessageItemModel(
            userImage = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcREz8aDOvFn1m2fCQ020dcrr-RCxey0NyF_XG6JOG1HzYoQRdBwB8U3fQJKEwG7t6Yr72Q",
            name = "John Doe",
            message = "Lorem ipsum dolor sit amet consectetur adipisicing elit. Maxime mollitia,\n" +
                      "molestiae quas vel sint commodi repudiandae consequuntur voluptatum laborum\n" +
                      "numquam blanditiis harum quisquam eius sed odit fugiat iusto fuga praesentium\n" +
                      "optio, eaque rerum! Provident similique accusantium nemo autem.",
            time = "12:00",
            unreadMessages = 0,
            isMessageSent = false
        ),

        )
)

@Preview
@Composable
fun PreviewMessagingScreenContentDark() {
    CyclistanceTheme(darkTheme = true) {
        MessagingScreenContent(messagesModel = MessagesModel())
    }
}

@Preview
@Composable
fun PreviewMessagingScreenContentLight() {
    CyclistanceTheme(darkTheme = false) {
        MessagingScreenContent(
            messagesModel = messages)
    }
}

