package com.example.cyclistance.feature_messaging.presentation.conversation.components


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cyclistance.core.presentation.dialogs.permissions_dialog.DialogNotificationPermission
import com.example.cyclistance.core.utils.composable_utils.Keyboard
import com.example.cyclistance.core.utils.composable_utils.keyboardAsState
import com.example.cyclistance.core.utils.composable_utils.noRippleClickable
import com.example.cyclistance.feature_messaging.domain.model.ui.conversation.ConversationItemModel
import com.example.cyclistance.feature_messaging.domain.model.ui.conversation.ConversationsModel
import com.example.cyclistance.feature_messaging.domain.model.ui.conversation.MessageDuration
import com.example.cyclistance.feature_messaging.presentation.conversation.event.ConversationUiEvent
import com.example.cyclistance.feature_messaging.presentation.conversation.state.ConversationState
import com.example.cyclistance.feature_messaging.presentation.conversation.state.ConversationUiState
import com.example.cyclistance.navigation.IsDarkTheme
import com.example.cyclistance.theme.CyclistanceTheme
import kotlinx.coroutines.launch
import java.util.Date





@Composable
fun ConversationContent(
    conversation: List<ConversationItemModel>,
    modifier: Modifier = Modifier,
    message: TextFieldValue,
    isInternetAvailable: Boolean,
    uiState: ConversationUiState,
    state: ConversationState,
    event: (ConversationUiEvent) -> Unit) {

    val conversationAvailable = remember(conversation.size) {
        conversation.isNotEmpty()
    }
    val listState = rememberLazyListState()
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

    val keyboardIsOpen by remember(keyboardState) {
        derivedStateOf { keyboardState == Keyboard.Opened }
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


        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

            if(uiState.notificationPermissionVisible){
                DialogNotificationPermission(
                    modifier = Modifier.align(Alignment.Center),
                    onDismiss = {event(ConversationUiEvent.DismissNotificationPermissionDialog)}
                )
            }

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = if (conversationAvailable) Alignment.BottomCenter else Alignment.Center) {


                Column(
                    modifier = Modifier.fillMaxSize()) {


                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        contentAlignment = Alignment.Center) {

                        if (state.isLoading) {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                        }

                        if (conversationAvailable) {
                            ConversationChatItems(
                                modifier = Modifier
                                    .fillMaxSize(),
                                keyboardIsOpen = keyboardIsOpen,
                                listState = listState,
                                conversation = conversation,
                                uiState = uiState,
                                state = state,
                                isInternetAvailable = isInternetAvailable,
                                event = event)

                        }

                        if (!conversationAvailable && !state.isLoading) {
                            PlaceholderEmptyConversation(
                                modifier = Modifier
                                    .fillMaxSize())
                        }
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
                            listState.animateScrollToItem(index = conversation.indices.last)
                        }
                    })
            }
        }


    }
}

private val fakeConversationsModel = ConversationsModel(
    messages = listOf(
        ConversationItemModel(
            messageId = "OsxvIecqWpbh32mjZPgx",
            senderId = "gfjltEWoLYZ5sQ80bSAL5zljiIS7",
            receiverId = "f80O4Y2BtrqIicuHTgjIYQ06AIPH",
            message = "Hello",
            timestamp = Date(),
            messageDuration = MessageDuration.OneDay,

            ),
        ConversationItemModel(
            messageId = "13",
            senderId = "2",
            receiverId = "1",
            message = "How are you?",
            timestamp = Date()
        ),
        ConversationItemModel(
            messageId = "14",
            senderId = "1",
            receiverId = "2",
            message = "I'm fine, thanks",
            timestamp = Date()
        ),
        ConversationItemModel(
            messageId = "15",
            senderId = "1",
            receiverId = "2",
            message = "How about you?",
            timestamp = Date(),
            messageDuration = MessageDuration.OneMonth
        ),
        ConversationItemModel(
            messageId = "16",
            senderId = "2",
            receiverId = "1",
            message = "I'm fine too",
            timestamp = Date(),
            messageDuration = MessageDuration.OneHour
        ),
        ConversationItemModel(
            messageId = "17",
            senderId = "1",
            receiverId = "2",
            message = "Good to hear that",
            timestamp = Date()
        ),
        ConversationItemModel(
            messageId = "18",
            senderId = "2",
            receiverId = "1",
            message = "orem ipsum dolor sit amet consectetur adipisicing elit. Maxime mollitia,\n" +
                      "molestiae quas vel sint commodi repudiandae consequuntur voluptatum laborum\n" +
                      "numquam blanditiis harum quisquam eius sed odit fugiat iusto fuga praesentium\n" +
                      "optio, eaque rerum! Provident similique accusantium nemo autem.",
            timestamp = Date()
        ),
        ConversationItemModel(
            messageId = "19357846457",
            senderId = "1",
            receiverId = "2",
            message = "Let's go for a ride?",
            timestamp = Date()
        ),
        ConversationItemModel(
            messageId = "23457570",
            senderId = "2",
            receiverId = "1",
            message = "Sure",
            timestamp = Date()
        ),
        ConversationItemModel(
            messageId = "19",
            senderId = "1",
            receiverId = "2",
            message = "Let's go for a ride?",
            timestamp = Date()
        ),
        ConversationItemModel(
            messageId = "23453450",
            senderId = "2",
            receiverId = "1",
            message = "Sure",
            timestamp = Date()
        ),
        ConversationItemModel(
            messageId = "194533467",
            senderId = "1",
            receiverId = "2",
            message = "Let's go for a ride?",
            timestamp = Date()
        ),
        ConversationItemModel(
            messageId = "203223",
            senderId = "2",
            receiverId = "1",
            message = "Sure asdasdasd",
            timestamp = Date()
        ),
        ConversationItemModel(
            messageId = "18553",
            senderId = "2",
            receiverId = "1",
            message = "orem ipsum dolor sit amet consectetur adipisicing elit. Maxime mollitia,\n" +
                      "molestiae quas vel sint commodi repudiandae consequuntur voluptatum laborum\n" +
                      "numquam blanditiis harum quisquam eius sed odit fugiat iusto fuga praesentium\n" +
                      "optio, eaque rerum! Provident similique accusantium nemo autem.",
            timestamp = Date()
        ),
        ConversationItemModel(
            messageId = "2546718",
            senderId = "2",
            receiverId = "1",
            message = "orem ipsum dolor sit amet consectetur adipisicing elit. Maxime mollitia,\n" +
                      "molestiae quas vel sint commodi repudiandae consequuntur voluptatum laborum\n" +
                      "numquam blanditiis harum quisquam eius sed odit fugiat iusto fuga praesentium\n" +
                      "optio, eaque rerum! Provident similique accusantium nemo autem.",
            timestamp = Date()
        ),
    )
)

@Preview
@Composable
fun PreviewMessagingConversationContentDark() {
    CompositionLocalProvider(IsDarkTheme provides true) {
        CyclistanceTheme(darkTheme = true) {
            ConversationContent(
                uiState = ConversationUiState(
                    messageAreaExpanded = true,
                ),
                conversation = fakeConversationsModel.messages,
                event = {}, state = ConversationState(
                ), message = TextFieldValue("Hello"), isInternetAvailable = false)
        }
    }
}

@Preview
@Composable
fun PreviewMessagingConversationContentLight() {
    CompositionLocalProvider(IsDarkTheme provides false) {
        CyclistanceTheme(darkTheme = false) {
            ConversationContent(
                conversation = fakeConversationsModel.messages,
                uiState = ConversationUiState(
                    messageAreaExpanded = true,
                    notificationPermissionVisible = true
                ),
                event = {}, state = ConversationState(

                ), message = TextFieldValue("Hello"), isInternetAvailable = true)
        }
    }
}

