package com.example.cyclistance.feature_messaging.presentation.chats.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.example.cyclistance.feature_messaging.domain.model.ui.list_messages.ChatItemModel
import com.example.cyclistance.feature_messaging.domain.model.ui.list_messages.ChatsModel
import com.example.cyclistance.feature_messaging.presentation.chats.event.MessagingUiEvent
import com.example.cyclistance.feature_messaging.presentation.chats.state.MessagingState
import com.example.cyclistance.feature_messaging.presentation.chats.state.MessagingUiState
import com.example.cyclistance.theme.Black500
import com.example.cyclistance.theme.CyclistanceTheme
import com.example.cyclistance.top_bars.TitleTopAppBar
import java.util.Date

@Composable
internal fun MessagingScreenContent(
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester,
    searchQuery: TextFieldValue,
    state: MessagingState,
    uiState: MessagingUiState,
    event: (MessagingUiEvent) -> Unit) {


    val messageAvailable =
        remember(state.chatsModel.messages) { state.chatsModel.messages.isNotEmpty() }

    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colors.background) {


        Column(modifier = Modifier.fillMaxSize()) {

            TopAppBar(
                elevation = 10.dp,
                title = {
                    TopAppBarSection(
                        modifier = Modifier,
                        uiState = uiState,
                        searchQuery = searchQuery,
                        event = event,
                        focusRequester = focusRequester
                    )
                },
                backgroundColor = MaterialTheme.colors.background,
                navigationIcon = {
                    IconButton(onClick = { event(MessagingUiEvent.CloseMessagingScreen) }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Top App Bar Icon",
                            tint = MaterialTheme.colors.onBackground)
                    }
                })

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

                        MessagesSection(chatsModel = state.chatsModel, onClick = {
                            event(MessagingUiEvent.OnSelectConversation(it))
                        })
                    }
                }

                if (!messageAvailable) {

                    Text(
                        text = "Start a conversation by sending a message. Connect with others and let the chat come alive!",
                        color = Black500,
                        style = MaterialTheme.typography.subtitle1,
                        modifier = Modifier.fillMaxWidth(0.7f),
                        textAlign = TextAlign.Center, lineHeight = TextUnit(20f, TextUnitType.Sp))
                }

                AddMessageButton(
                    modifier = Modifier.padding(all = 4.dp)
                        .align(Alignment.BottomEnd),
                    onClick = {})
            }
        }
    }
}


@Composable
private fun TopAppBarSection(
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester,
    uiState: MessagingUiState,
    searchQuery: TextFieldValue,
    event: (MessagingUiEvent) -> Unit) {


    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween) {

        if (uiState.isSearching) {


            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }

            MessagingSearchBar(
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .fillMaxWidth()
                    .weight(0.65f),
                value = searchQuery,
                onValueChange = {
                    event(MessagingUiEvent.OnSearchQueryChanged(it))
                },
            )

            IconButton(onClick = { event(MessagingUiEvent.ClearSearchQuery) }) {
                Icon(
                    imageVector = Icons.Default.Cancel,
                    contentDescription = "Clear Search",
                    tint = MaterialTheme.colors.onBackground,
                    modifier = Modifier.weight(0.35f)
                )
            }

        } else {

            TitleTopAppBar(title = "Chats")
            IconButton(onClick = {
                event(MessagingUiEvent.OnClickSearch)
            }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = MaterialTheme.colors.onBackground,
                )
            }
        }

    }
}

val fakeMessages = ChatsModel(
    listOf(
        ChatItemModel(
            userPhotoUrl = "https://www.liquidsandsolids.com/wp-content/uploads/2022/09/talking-to-a-dead-person.jpg",
            name = "John Doe",
            message = "Hey there! How are you?",
            timeStamp = Date(),
            messageId = "1",
            userId = "1gaosidnuio2b"
        ),
        ChatItemModel(
            userPhotoUrl = "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8cGVyc29ufGVufDB8fDB8fHww&w=1000&q=80",
            name = "Jane Doe",
            message = "Lorem ipsum dolor sit amet consectetur adipisicing elit. Maxime mollitia,\n" +
                      "molestiae quas vel sint commodi repudiandae consequuntur voluptatum laborum\n" +
                      "numquam blanditiis harum quisquam eius sed odit fugiat iusto fuga praesentium\n" +
                      "optio, eaque rerum! Provident similique accusantium nemo autem.",
            timeStamp = Date(),
            messageId = "2",
            userId = "ksnksksk29u4091u2"
        ),
        ChatItemModel(
            userPhotoUrl = "https://www.diethelmtravel.com/wp-content/uploads/2016/04/bill-gates-wealthiest-person.jpg",
            name = "Jennifer",
            message = "Lorem ipsum dolor sit amet consectetur adipisicing elit. Maxime mollitia,\n" +
                      "molestiae quas vel sint commodi repudiandae consequuntur voluptatum laborum\n" +
                      "numquam blanditiis harum quisquam eius sed odit fugiat iusto fuga praesentium\n" +
                      "optio, eaque rerum! Provident similique accusantium nemo autem.",
            timeStamp = Date(),
            messageId = "3",
            userId = "asgknasoidn29h"
        ),
        ChatItemModel(
            userPhotoUrl = "https://www.harleytherapy.co.uk/counselling/wp-content/uploads/16297800391_5c6e812832.jpg",
            name = "John Doe",
            message = "Hello",
            timeStamp = Date(),

            messageId = "4",
            userId = "asidbnaoiusdb982bh"
        ),
        ChatItemModel(
            userPhotoUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcREz8aDOvFn1m2fCQ020dcrr-RCxey0NyF_XG6JOG1HzYoQRdBwB8U3fQJKEwG7t6Yr72Q",
            name = "John Doe",
            message = "Lorem ipsum dolor sit amet consectetur adipisicing elit. Maxime mollitia,\n" +
                      "molestiae quas vel sint commodi repudiandae consequuntur voluptatum laborum\n" +
                      "numquam blanditiis harum quisquam eius sed odit fugiat iusto fuga praesentium\n" +
                      "optio, eaque rerum! Provident similique accusantium nemo autem.",
            timeStamp = Date(),
            messageId = "5",
            userId = "q0iweht08"
        ),

        )
)

@Preview
@Composable
fun PreviewMessagingScreenContentDark() {
    CyclistanceTheme(darkTheme = true) {
        MessagingScreenContent(
            state = MessagingState(),
            uiState = MessagingUiState(),
            searchQuery = TextFieldValue("apiosdmnaisnd"),
            focusRequester = FocusRequester(),
            event = {})
    }
}

@Preview
@Composable
fun PreviewMessagingScreenContentLight() {
    CyclistanceTheme(darkTheme = false) {
        MessagingScreenContent(
            state = MessagingState(),
            uiState = MessagingUiState(),
            searchQuery = TextFieldValue("apiosdmnaisnd"),
            focusRequester = FocusRequester(),
            event = {})
    }
}

