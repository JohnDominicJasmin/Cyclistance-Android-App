package com.example.cyclistance.feature_messaging.presentation.messaging.components.messaging_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.example.cyclistance.R
import com.example.cyclistance.feature_messaging.domain.model.ui.MessageItemModel
import com.example.cyclistance.feature_messaging.domain.model.ui.MessagesModel
import com.example.cyclistance.feature_messaging.presentation.messaging.components.conversation.MessagingConversationContent
import com.example.cyclistance.feature_messaging.presentation.messaging.event.MessagingUiEvent
import com.example.cyclistance.feature_messaging.presentation.messaging.state.MessagingState
import com.example.cyclistance.feature_messaging.presentation.messaging.state.MessagingUiState
import com.example.cyclistance.theme.Black500
import com.example.cyclistance.theme.CyclistanceTheme

@Composable
fun MessagingScreenContent(
    modifier: Modifier = Modifier,
    uiState: MessagingUiState,
    state: MessagingState,
    event: (MessagingUiEvent) -> Unit) {


    val messageAvailable =
        remember(state.messagesModel.messages) { state.messagesModel.messages.isNotEmpty() }
    val shouldShowConversationDialog =
        remember(uiState.selectedConversationId) { uiState.selectedConversationId != null }
    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colors.background) {

        if (shouldShowConversationDialog) {
            MessagingConversationContent(
                modifier = Modifier.fillMaxSize(),
                uiState = uiState,
                state = state,
                event = event
            )
        }

        if (messageAvailable && !shouldShowConversationDialog) {

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

                    MessagesSection(messagesModel = state.messagesModel, onClick = {
                        event(MessagingUiEvent.OnSelectedConversation(it))
                    })
                }
            }
        }
        if (!messageAvailable && !shouldShowConversationDialog) {

            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

                Text(
                    text = "Start a conversation by sending a message. Connect with others and let the chat come alive!",
                    color = Black500,
                    style = MaterialTheme.typography.subtitle1,
                    modifier = Modifier.fillMaxWidth(0.7f),
                    textAlign = TextAlign.Center, lineHeight = TextUnit(20f, TextUnitType.Sp))

                AddMessageButton(onClick = {}, modifier = Modifier.align(Alignment.BottomEnd))

            }
        }

    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun BoxScope.AddMessageButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Surface(
        color = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.background,
        modifier = modifier
            .padding(all = 12.dp),
        shape = CircleShape,
        onClick = onClick) {

        Icon(
            painter = painterResource(id = R.drawable.baseline_edit_24),
            contentDescription = "Add Message",
            modifier = Modifier
                .padding(12.dp)
                .size(28.dp),
        )
    }
}

@Composable
private fun MessagesSection(
    modifier: Modifier = Modifier,
    messagesModel: MessagesModel,
    onClick: (String) -> Unit) {

    LazyColumn(
        modifier = modifier
            .fillMaxSize()) {
        items(messagesModel.messages, key = {
            it.messageId

        }) { item ->

            MessagingItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                messageItemModel = item,
                onClick = onClick
            )
        }
    }
}


val fakeMessages = MessagesModel(
    listOf(
        MessageItemModel(
            userPhotoUrl = "https://www.liquidsandsolids.com/wp-content/uploads/2022/09/talking-to-a-dead-person.jpg",
            name = "John Doe",
            message = "Hey there! How are you?",
            timeStamp = "12:00",
            unreadMessages = 4,
            isMessageSent = false,
            messageId = "1"
        ),
        MessageItemModel(
            userPhotoUrl = "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8cGVyc29ufGVufDB8fDB8fHww&w=1000&q=80",
            name = "Jane Doe",
            message = "Lorem ipsum dolor sit amet consectetur adipisicing elit. Maxime mollitia,\n" +
                      "molestiae quas vel sint commodi repudiandae consequuntur voluptatum laborum\n" +
                      "numquam blanditiis harum quisquam eius sed odit fugiat iusto fuga praesentium\n" +
                      "optio, eaque rerum! Provident similique accusantium nemo autem.",
            timeStamp = "12:00",
            unreadMessages = 10,
            isMessageSent = true,
            messageId = "2"
        ),
        MessageItemModel(
            userPhotoUrl = "https://www.diethelmtravel.com/wp-content/uploads/2016/04/bill-gates-wealthiest-person.jpg",
            name = "Jennifer",
            message = "Lorem ipsum dolor sit amet consectetur adipisicing elit. Maxime mollitia,\n" +
                      "molestiae quas vel sint commodi repudiandae consequuntur voluptatum laborum\n" +
                      "numquam blanditiis harum quisquam eius sed odit fugiat iusto fuga praesentium\n" +
                      "optio, eaque rerum! Provident similique accusantium nemo autem.",
            timeStamp = "12:00",
            unreadMessages = 0,
            isMessageSent = true,
            messageId = "3"
        ),
        MessageItemModel(
            userPhotoUrl = "https://www.harleytherapy.co.uk/counselling/wp-content/uploads/16297800391_5c6e812832.jpg",
            name = "John Doe",
            message = "Hello",
            timeStamp = "12:00",
            unreadMessages = 0,
            isMessageSent = true,
            messageId = "4"
        ),
        MessageItemModel(
            userPhotoUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcREz8aDOvFn1m2fCQ020dcrr-RCxey0NyF_XG6JOG1HzYoQRdBwB8U3fQJKEwG7t6Yr72Q",
            name = "John Doe",
            message = "Lorem ipsum dolor sit amet consectetur adipisicing elit. Maxime mollitia,\n" +
                      "molestiae quas vel sint commodi repudiandae consequuntur voluptatum laborum\n" +
                      "numquam blanditiis harum quisquam eius sed odit fugiat iusto fuga praesentium\n" +
                      "optio, eaque rerum! Provident similique accusantium nemo autem.",
            timeStamp = "12:00",
            unreadMessages = 0,
            isMessageSent = false,
            messageId = "5"
        ),

        )
)

@Preview
@Composable
fun PreviewMessagingScreenContentDark() {
    CyclistanceTheme(darkTheme = true) {
        MessagingScreenContent(
            uiState = MessagingUiState(),
            state = MessagingState(),
            event = {})
    }
}

@Preview
@Composable
fun PreviewMessagingScreenContentLight() {
    CyclistanceTheme(darkTheme = false) {
        MessagingScreenContent(
            uiState = MessagingUiState(),
            state = MessagingState(),
            event = {})
    }
}
