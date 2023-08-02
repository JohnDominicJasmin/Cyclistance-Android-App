package com.example.cyclistance.feature_messaging.presentation.chat.chats.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.example.cyclistance.feature_messaging.domain.model.ui.chats.ChatItemModel
import com.example.cyclistance.feature_messaging.domain.model.ui.chats.ChatsModel
import com.example.cyclistance.feature_messaging.presentation.chat.chats.event.ChatUiEvent
import com.example.cyclistance.feature_messaging.presentation.chat.chats.state.ChatState
import com.example.cyclistance.theme.Black500
import com.example.cyclistance.theme.CyclistanceTheme
import java.util.Date

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun ChatScreenContent(
    modifier: Modifier = Modifier,
    chatState: List<ChatItemModel>,
    state: ChatState,
    event: (ChatUiEvent) -> Unit) {


    val messageAvailable =
        remember(chatState.size) { chatState.isNotEmpty() }
    val pullRefreshState = rememberPullRefreshState(state.isRefreshing, {
        event(ChatUiEvent.OnRefreshChat)
    })
    Surface(
        modifier = modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState),
        color = MaterialTheme.colors.background) {

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = if (messageAvailable) Alignment.TopCenter else Alignment.Center) {

                if (messageAvailable) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.SpaceEvenly) {

                        Text(
                            text = "Recent Messages",
                            style = MaterialTheme.typography.body2.copy(
                                letterSpacing = TextUnit(
                                    2f,
                                    type = TextUnitType.Sp)),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 12.dp),
                        )

                        ChatsSection(chatState = chatState, onClick = {
                            event(ChatUiEvent.OnSelectConversation(it))
                        })
                    }
                }

                if (!messageAvailable && !state.isLoading) {

                    Text(
                        text = "Start a conversation by sending a message. Connect with others and let the chat come alive!",
                        color = Black500,
                        style = MaterialTheme.typography.subtitle1,
                        modifier = Modifier.fillMaxWidth(0.7f),
                        textAlign = TextAlign.Center,
                        lineHeight = TextUnit(20f, TextUnitType.Sp))
                }

            }
            PullRefreshIndicator(
                state.isRefreshing,
                pullRefreshState,
                Modifier.align(Alignment.TopCenter),
            )

        }
    }
}


val fakeMessages = ChatsModel(
    listOf(
        ChatItemModel(
            conversionPhoto = "https://www.liquidsandsolids.com/wp-content/uploads/2022/09/talking-to-a-dead-person.jpg",
            conversionName = "John Doe",
            lastMessage = "Hey there! How are you?",
            timeStamp = Date(),
            messageId = "1",
            conversionId = "1gaosidnuio2b",

        ),
        ChatItemModel(
            conversionPhoto = "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8cGVyc29ufGVufDB8fDB8fHww&w=1000&q=80",
            conversionName = "Jane Doe",
            lastMessage = "Lorem ipsum dolor sit amet consectetur adipisicing elit. Maxime mollitia,\n" +
                          "molestiae quas vel sint commodi repudiandae consequuntur voluptatum laborum\n" +
                          "numquam blanditiis harum quisquam eius sed odit fugiat iusto fuga praesentium\n" +
                          "optio, eaque rerum! Provident similique accusantium nemo autem.",
            timeStamp = Date(),
            messageId = "2",
            conversionId = "ksnksksk29u4091u2"
        ),
        ChatItemModel(
            conversionPhoto = "https://www.diethelmtravel.com/wp-content/uploads/2016/04/bill-gates-wealthiest-person.jpg",
            conversionName = "Jennifer",
            lastMessage = "Lorem ipsum dolor sit amet consectetur adipisicing elit. Maxime mollitia,\n" +
                          "molestiae quas vel sint commodi repudiandae consequuntur voluptatum laborum\n" +
                          "numquam blanditiis harum quisquam eius sed odit fugiat iusto fuga praesentium\n" +
                          "optio, eaque rerum! Provident similique accusantium nemo autem.",
            timeStamp = Date(),
            messageId = "3",
            conversionId = "asgknasoidn29h"
        ),
        ChatItemModel(
            conversionPhoto = "https://www.harleytherapy.co.uk/counselling/wp-content/uploads/16297800391_5c6e812832.jpg",
            conversionName = "John Doe",
            lastMessage = "Hello",
            timeStamp = Date(),

            messageId = "4",
            conversionId = "asidbnaoiusdb982bh"
        ),
        ChatItemModel(
            conversionPhoto = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcREz8aDOvFn1m2fCQ020dcrr-RCxey0NyF_XG6JOG1HzYoQRdBwB8U3fQJKEwG7t6Yr72Q",
            conversionName = "John Doe",
            lastMessage = "Lorem ipsum dolor sit amet consectetur adipisicing elit. Maxime mollitia,\n" +
                          "molestiae quas vel sint commodi repudiandae consequuntur voluptatum laborum\n" +
                          "numquam blanditiis harum quisquam eius sed odit fugiat iusto fuga praesentium\n" +
                          "optio, eaque rerum! Provident similique accusantium nemo autem.",
            timeStamp = Date(),
            messageId = "5",
            conversionId = "q0iweht08"
        ),

        )
)

@Preview
@Composable
fun PreviewChatScreenContentDark() {
    CyclistanceTheme(darkTheme = true) {
        ChatScreenContent(
            chatState = emptyList(),
            state = ChatState(isLoading = true),
            event = {}
        )
    }
}

@Preview
@Composable
fun PreviewChatScreenContentLight() {
    CyclistanceTheme(darkTheme = false) {
        ChatScreenContent(
            chatState = fakeMessages.chats,
            state = ChatState(),
            event = {}
        )
    }
}

