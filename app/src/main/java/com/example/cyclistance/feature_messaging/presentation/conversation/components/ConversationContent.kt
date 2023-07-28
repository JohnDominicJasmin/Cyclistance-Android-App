package com.example.cyclistance.feature_messaging.presentation.conversation.components


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cyclistance.R
import com.example.cyclistance.core.utils.composable_utils.Keyboard
import com.example.cyclistance.core.utils.composable_utils.keyboardAsState
import com.example.cyclistance.core.utils.composable_utils.noRippleClickable
import com.example.cyclistance.core.utils.validation.FormatterUtils.toReadableDateTime
import com.example.cyclistance.feature_messaging.domain.model.ui.conversation.ConversationItemModel
import com.example.cyclistance.feature_messaging.domain.model.ui.conversation.ConversationsModel
import com.example.cyclistance.feature_messaging.domain.model.ui.conversation.MessageDuration
import com.example.cyclistance.feature_messaging.presentation.conversation.event.ConversationUiEvent
import com.example.cyclistance.feature_messaging.presentation.conversation.state.ConversationState
import com.example.cyclistance.feature_messaging.presentation.conversation.state.ConversationUiState
import com.example.cyclistance.navigation.IsDarkTheme
import com.example.cyclistance.theme.Black500
import com.example.cyclistance.theme.CyclistanceTheme
import kotlinx.coroutines.launch
import java.util.Date

private val USER_ID = "1"
private val conversationsModel = ConversationsModel(
    messages = listOf(
        ConversationItemModel(
            messageId = "12",
            senderId = "1",
            receiverId = "2",
            message = "Hello",
            timeStamp = Date(),
            messageDuration = MessageDuration.OneDay,

            ),
        ConversationItemModel(
            messageId = "13",
            senderId = "2",
            receiverId = "1",
            message = "How are you?",
            timeStamp = Date()
        ),
        ConversationItemModel(
            messageId = "14",
            senderId = "1",
            receiverId = "2",
            message = "I'm fine, thanks",
            timeStamp = Date()
        ),
        ConversationItemModel(
            messageId = "15",
            senderId = "1",
            receiverId = "2",
            message = "How about you?",
            timeStamp = Date(),
            messageDuration = MessageDuration.OneMonth
        ),
        ConversationItemModel(
            messageId = "16",
            senderId = "2",
            receiverId = "1",
            message = "I'm fine too",
            timeStamp = Date(),
            messageDuration = MessageDuration.OneHour
        ),
        ConversationItemModel(
            messageId = "17",
            senderId = "1",
            receiverId = "2",
            message = "Good to hear that",
            timeStamp = Date()
        ),
        ConversationItemModel(
            messageId = "18",
            senderId = "2",
            receiverId = "1",
            message = "orem ipsum dolor sit amet consectetur adipisicing elit. Maxime mollitia,\n" +
                      "molestiae quas vel sint commodi repudiandae consequuntur voluptatum laborum\n" +
                      "numquam blanditiis harum quisquam eius sed odit fugiat iusto fuga praesentium\n" +
                      "optio, eaque rerum! Provident similique accusantium nemo autem.",
            timeStamp = Date()
        ),
        ConversationItemModel(
            messageId = "19357846457",
            senderId = "1",
            receiverId = "2",
            message = "Let's go for a ride?",
            timeStamp = Date()
        ),
        ConversationItemModel(
            messageId = "23457570",
            senderId = "2",
            receiverId = "1",
            message = "Sure",
            timeStamp = Date()
        ),
        ConversationItemModel(
            messageId = "19",
            senderId = "1",
            receiverId = "2",
            message = "Let's go for a ride?",
            timeStamp = Date()
        ),
        ConversationItemModel(
            messageId = "23453450",
            senderId = "2",
            receiverId = "1",
            message = "Sure",
            timeStamp = Date()
        ),
        ConversationItemModel(
            messageId = "194533467",
            senderId = "1",
            receiverId = "2",
            message = "Let's go for a ride?",
            timeStamp = Date()
        ),
        ConversationItemModel(
            messageId = "203223",
            senderId = "2",
            receiverId = "1",
            message = "Sure asdasdasd",
            timeStamp = Date()
        ),
        ConversationItemModel(
            messageId = "18553",
            senderId = "2",
            receiverId = "1",
            message = "orem ipsum dolor sit amet consectetur adipisicing elit. Maxime mollitia,\n" +
                      "molestiae quas vel sint commodi repudiandae consequuntur voluptatum laborum\n" +
                      "numquam blanditiis harum quisquam eius sed odit fugiat iusto fuga praesentium\n" +
                      "optio, eaque rerum! Provident similique accusantium nemo autem.",
            timeStamp = Date()
        ),
        ConversationItemModel(
            messageId = "2546718",
            senderId = "2",
            receiverId = "1",
            message = "orem ipsum dolor sit amet consectetur adipisicing elit. Maxime mollitia,\n" +
                      "molestiae quas vel sint commodi repudiandae consequuntur voluptatum laborum\n" +
                      "numquam blanditiis harum quisquam eius sed odit fugiat iusto fuga praesentium\n" +
                      "optio, eaque rerum! Provident similique accusantium nemo autem.",
            timeStamp = Date()
        ),
    )
)


@Composable
fun ConversationContent(
    modifier: Modifier = Modifier,
    message: TextFieldValue,
    uiState: ConversationUiState,
    state: ConversationState,
    event: (ConversationUiEvent) -> Unit) {

    val conversationAvailable by remember(state.conversationsModel) {
        derivedStateOf { state.conversationsModel.messages.isNotEmpty() }
    }
    val listState =
        rememberLazyListState(initialFirstVisibleItemIndex = state.conversationsModel.messages.indices.last)
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
        event(ConversationUiEvent.ToggleMessageArea)

    }



    Surface(
        modifier = modifier
            .fillMaxSize()
            .noRippleClickable {

                event(ConversationUiEvent.ResetSelectedIndex)

                if (uiState.messageAreaExpanded) {
                    event(ConversationUiEvent.ToggleMessageArea)
                }
                focusManager.clearFocus()
            },
        color = MaterialTheme.colors.background) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = if (conversationAvailable) Alignment.BottomCenter else Alignment.Center) {


            Column(
                modifier = Modifier.fillMaxSize()) {


                if (conversationAvailable) {

                    LazyColumn(
                        state = listState,
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp)
                    ) {

                        itemsIndexed(
                            items = state.conversationsModel.messages,
                            key = { _, item -> item.messageId }) { index, message ->

                            val isSender by remember { derivedStateOf { message.senderId != USER_ID } }
                            val timeStampAvailable by remember { derivedStateOf { message.messageDuration != null && message.timeStamp != null } }

                            AnimatedVisibility(visible = timeStampAvailable) {

                                MessagingTimeStamp(
                                    value = message.timeStamp!!.toReadableDateTime(),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp))
                            }


                            ChatItem(
                                conversation = message,
                                isSender = isSender,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp, horizontal = 6.dp),
                                contentAlignment = if (isSender) Alignment.CenterStart else Alignment.CenterEnd,
                                currentIndex = index,
                                selectedIndex = uiState.chatItemSelectedIndex,
                                onSelectChatMessage = {
                                    event(
                                        ConversationUiEvent.SelectChatItem(
                                            index = it))
                                }
                            )
                        }

                    }

                } else {
                    PlaceholderEmptyConversation(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize())
                }




                MessagingTextArea(
                    message = message,
                    onValueChange = { event(ConversationUiEvent.OnChangeValueMessage(it)) },
                    modifier = Modifier.wrapContentHeight(),
                    onClickSend = { event(ConversationUiEvent.OnSendMessage) },
                    onToggleExpand = { event(ConversationUiEvent.ToggleMessageArea) },
                    isExpanded = uiState.messageAreaExpanded)

            }

            ScrollToBottomButton(
                modifier = Modifier.absoluteOffset(y = (-85).dp),
                isVisible = isScrollingUp,
                onClick = {
                    scope.launch {
                        listState.animateScrollToItem(index = state.conversationsModel.messages.indices.last)
                    }
                })
        }
    }
}


@Composable
private fun PlaceholderEmptyConversation(modifier: Modifier = Modifier) {
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


@Preview
@Composable
fun PreviewMessagingConversationContentDark() {
    CompositionLocalProvider(IsDarkTheme provides true) {
        CyclistanceTheme(darkTheme = true) {
            ConversationContent(
                uiState = ConversationUiState(
                    messageAreaExpanded = true,
                ),
                event = {}, state = ConversationState(
                    conversationsModel = conversationsModel
                ), message = TextFieldValue("Hello"))
        }
    }
}

@Preview
@Composable
fun PreviewMessagingConversationContentLight() {
    CompositionLocalProvider(IsDarkTheme provides false) {
        CyclistanceTheme(darkTheme = false) {
            ConversationContent(
                uiState = ConversationUiState(
                    messageAreaExpanded = true,
                ),
                event = {}, state = ConversationState(
                    conversationsModel = conversationsModel
                ), message = TextFieldValue("Hello"))
        }
    }
}

