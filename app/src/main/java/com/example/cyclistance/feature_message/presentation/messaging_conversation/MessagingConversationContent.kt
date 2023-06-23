package com.example.cyclistance.feature_message.presentation.messaging_conversation


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cyclistance.core.utils.composable_utils.Keyboard
import com.example.cyclistance.core.utils.composable_utils.keyboardAsState
import com.example.cyclistance.core.utils.composable_utils.noRippleClickable
import com.example.cyclistance.feature_message.domain.model.ui.Duration
import com.example.cyclistance.feature_message.domain.model.ui.MessageContent
import com.example.cyclistance.feature_message.domain.model.ui.MessageConversation
import com.example.cyclistance.feature_message.presentation.messaging_conversation.components.ChatItem
import com.example.cyclistance.feature_message.presentation.messaging_conversation.components.MessagingTextArea
import com.example.cyclistance.feature_message.presentation.messaging_conversation.components.MessagingTimeStamp
import com.example.cyclistance.feature_message.presentation.messaging_conversation.components.ScrollToBottomButton
import com.example.cyclistance.feature_message.presentation.messaging_conversation.event.MessagingConversationUiEvent
import com.example.cyclistance.feature_message.presentation.messaging_conversation.state.MessagingConversationUiState
import com.example.cyclistance.navigation.IsDarkTheme
import com.example.cyclistance.theme.CyclistanceTheme
import kotlinx.coroutines.launch

private val USER_ID = "1"
private val conversation = MessageConversation(
    messages = listOf(
        MessageContent(
            messageId = "12",
            senderId = "1",
            recipientId = "2",
            content = "Hello",
            dateSent = "10:30 AM",
            duration = Duration.OneDay,

            ),
        MessageContent(
            messageId = "13",
            senderId = "2",
            recipientId = "1",
            content = "How are you?",
            dateSent = "11:32 AM"
        ),
        MessageContent(
            messageId = "14",
            senderId = "1",
            recipientId = "2",
            content = "I'm fine, thanks",
            dateSent = "11:35 AM"
        ),
        MessageContent(
            messageId = "15",
            senderId = "1",
            recipientId = "2",
            content = "How about you?",
            dateSent = "FEB 13 12:12 AM",
            duration = Duration.OneMonth
        ),
        MessageContent(
            messageId = "16",
            senderId = "2",
            recipientId = "1",
            content = "I'm fine too",
            dateSent = "11:42 AM",
            duration = Duration.OneHour
        ),
        MessageContent(
            messageId = "17",
            senderId = "1",
            recipientId = "2",
            content = "Good to hear that",
            dateSent = "11:43 AM"
        ),
        MessageContent(
            messageId = "18",
            senderId = "2",
            recipientId = "1",
            content = "orem ipsum dolor sit amet consectetur adipisicing elit. Maxime mollitia,\n" +
                      "molestiae quas vel sint commodi repudiandae consequuntur voluptatum laborum\n" +
                      "numquam blanditiis harum quisquam eius sed odit fugiat iusto fuga praesentium\n" +
                      "optio, eaque rerum! Provident similique accusantium nemo autem.",
            dateSent = "11:45 AM"
        ),
        MessageContent(
            messageId = "19357846457",
            senderId = "1",
            recipientId = "2",
            content = "Let's go for a ride?",
            dateSent = "11:50 AM"
        ),
        MessageContent(
            messageId = "23457570",
            senderId = "2",
            recipientId = "1",
            content = "Sure",
            dateSent = "11:55 AM"
        ),
        MessageContent(
            messageId = "19",
            senderId = "1",
            recipientId = "2",
            content = "Let's go for a ride?",
            dateSent = "11:50 AM"
        ),
        MessageContent(
            messageId = "23453450",
            senderId = "2",
            recipientId = "1",
            content = "Sure",
            dateSent = "11:55 AM"
        ),
        MessageContent(
            messageId = "194533467",
            senderId = "1",
            recipientId = "2",
            content = "Let's go for a ride?",
            dateSent = "11:50 AM"
        ),
        MessageContent(
            messageId = "203223",
            senderId = "2",
            recipientId = "1",
            content = "Sure asdasdasd",
            dateSent = "11:55 AM"
        ),
        MessageContent(
            messageId = "18553",
            senderId = "2",
            recipientId = "1",
            content = "orem ipsum dolor sit amet consectetur adipisicing elit. Maxime mollitia,\n" +
                      "molestiae quas vel sint commodi repudiandae consequuntur voluptatum laborum\n" +
                      "numquam blanditiis harum quisquam eius sed odit fugiat iusto fuga praesentium\n" +
                      "optio, eaque rerum! Provident similique accusantium nemo autem.",
            dateSent = "11:45 AM"
        ),
        MessageContent(
            messageId = "2546718",
            senderId = "2",
            recipientId = "1",
            content = "orem ipsum dolor sit amet consectetur adipisicing elit. Maxime mollitia,\n" +
                      "molestiae quas vel sint commodi repudiandae consequuntur voluptatum laborum\n" +
                      "numquam blanditiis harum quisquam eius sed odit fugiat iusto fuga praesentium\n" +
                      "optio, eaque rerum! Provident similique accusantium nemo autem.",
            dateSent = "11:45 AM"
        ),
    )
)


@Composable
fun MessagingConversationContent(
    uiState: MessagingConversationUiState,
    event: (MessagingConversationUiEvent) -> Unit) {


    val listState =
        rememberLazyListState(initialFirstVisibleItemIndex = conversation.messages.indices.last)
    val focusManager = LocalFocusManager.current
    val keyboardState by keyboardAsState()


    val scope = rememberCoroutineScope()


    val stateFirstVisibleItemIndex by remember { derivedStateOf { listState.firstVisibleItemIndex } }
    var farthestVisibleItemIndex by rememberSaveable { mutableIntStateOf(0) }
    val isScrollingUp by remember { derivedStateOf { farthestVisibleItemIndex > stateFirstVisibleItemIndex + 4 } }

    LaunchedEffect(key1 = listState.isScrollInProgress) {
        if (stateFirstVisibleItemIndex > farthestVisibleItemIndex) {
            farthestVisibleItemIndex = stateFirstVisibleItemIndex
        }
    }

    LaunchedEffect(key1 = keyboardState) {
        if (keyboardState == Keyboard.Opened) {
            return@LaunchedEffect
        }
        if (!uiState.messageAreaExpanded) {
            return@LaunchedEffect
        }
        event(MessagingConversationUiEvent.ToggleMessageArea)

    }




    Surface(
        modifier = Modifier
            .fillMaxSize()
            .noRippleClickable {

                event(MessagingConversationUiEvent.ResetSelectedIndex)
                if (uiState.messageAreaExpanded) {
                    event(MessagingConversationUiEvent.ToggleMessageArea)
                }
                focusManager.clearFocus()
            },
        color = MaterialTheme.colors.background) {

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {

            Column(
                modifier = Modifier.fillMaxSize()) {

                LazyColumn(
                    state = listState,
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp)
                ) {

                    itemsIndexed(
                        items = conversation.messages,
                        key = { _, item -> item.messageId }) { index, message ->

                        val isSender by remember { derivedStateOf { message.senderId != USER_ID } }
                        val timeStampAvailable by remember { derivedStateOf { message.duration != null && message.dateSent != null } }

                        AnimatedVisibility(visible = timeStampAvailable) {

                            MessagingTimeStamp(
                                value = message.dateSent!!,
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
                            currentIndex = index,
                            selectedIndex = uiState.chatItemSelectedIndex,
                            onClick = { event(MessagingConversationUiEvent.SelectChatItem(index = it)) }
                        )
                    }

                }

                MessagingTextArea(
                    message = uiState.message,
                    onValueChange = { event(MessagingConversationUiEvent.ChangeMessage(it)) },
                    modifier = Modifier.wrapContentHeight(),
                    onClickSend = {},
                    onToggleExpand = { event(MessagingConversationUiEvent.ToggleMessageArea) },
                    isExpanded = uiState.messageAreaExpanded)

            }

            ScrollToBottomButton(
                modifier = Modifier.absoluteOffset(y = (-85).dp),
                isVisible = isScrollingUp,
                onClick = {
                    scope.launch {
                        listState.animateScrollToItem(index = conversation.messages.indices.last)
                    }
                })

        }
    }

}


@Preview
@Composable
fun PreviewMessagingConversationContentDark() {
    CompositionLocalProvider(IsDarkTheme provides true) {
        CyclistanceTheme(darkTheme = true) {
            MessagingConversationContent(
                uiState = MessagingConversationUiState(messageAreaExpanded = true),
                event = {})
        }
    }
}

@Preview
@Composable
fun PreviewMessagingConversationContentLight() {
    CompositionLocalProvider(IsDarkTheme provides false) {
        CyclistanceTheme(darkTheme = false) {
            MessagingConversationContent(
                uiState = MessagingConversationUiState(messageAreaExpanded = true),
                event = {})
        }
    }
}

