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
import com.example.cyclistance.feature_messaging.domain.model.ui.chats.MessagingUserItemModel
import com.example.cyclistance.feature_messaging.presentation.chat.chats.event.ChatUiEvent
import com.example.cyclistance.feature_messaging.presentation.chat.chats.state.ChatState
import com.example.cyclistance.theme.Black500
import com.example.cyclistance.theme.CyclistanceTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun ChatScreenContent(
    modifier: Modifier = Modifier,
    chatState: List<Pair<MessagingUserItemModel,ChatItemModel>>,
    isInternetAvailable: Boolean,
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

                        ChatsSection(
                            isInternetAvailable = isInternetAvailable,
                            chatState = chatState,
                            state = state,
                            onClick = {
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



@Preview
@Composable
fun PreviewChatScreenContentDark() {
    CyclistanceTheme(darkTheme = true) {
        ChatScreenContent(
            chatState = emptyList(),
            state = ChatState(isLoading = false),
            event = {},
            isInternetAvailable = false

        )
    }
}

@Preview
@Composable
fun PreviewChatScreenContentLight() {
    CyclistanceTheme(darkTheme = false) {
        ChatScreenContent(
            chatState = emptyList(),
            state = ChatState(),
            event = {},
            isInternetAvailable = true
        )
    }
}

