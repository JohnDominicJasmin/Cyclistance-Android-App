package com.example.cyclistance.feature_messaging.presentation.search_user.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.cyclistance.feature_messaging.presentation.chat.chats.components.ChatSearchItem
import com.example.cyclistance.feature_messaging.presentation.search_user.event.SearchUserUiEvent
import com.example.cyclistance.feature_messaging.presentation.search_user.state.SearchUserState

@Composable
fun SearchUserSection(
    modifier: Modifier = Modifier,
    searchQuery: TextFieldValue,
    focusRequester: FocusRequester,
    state: SearchUserState,
    event: (SearchUserUiEvent) -> Unit) {

    val filteredQuery = remember(searchQuery.text, state.messagingUsers.users) {
        state.messagingUsers.users.filter {
            it.userDetails.name.contains(searchQuery.text, ignoreCase = true)
        }
    }

    val resultFound = remember(filteredQuery) { filteredQuery.isNotEmpty() }
    val shouldShowNotFound = remember(resultFound, searchQuery.text) {
        !resultFound && searchQuery.text.isNotEmpty()
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Surface(color = MaterialTheme.colors.background, modifier = modifier.fillMaxSize()) {


        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp)
            .padding(horizontal = 8.dp)) {
            IconButton(onClick = { event(SearchUserUiEvent.CloseScreen) }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Arrow Back",
                    tint = MaterialTheme.colors.onBackground
                )
            }

            MessagingSearchBar(
                modifier = Modifier.focusRequester(focusRequester = focusRequester),
                value = searchQuery,
                onValueChange = { event(SearchUserUiEvent.OnSearchQueryChanged(it)) },
            )
        }

        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center) {

            if (shouldShowNotFound) {
                SearchMessageNotFound()
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(items = filteredQuery, key = { it.userDetails.uid }) { model ->
                        ChatSearchItem(
                            modifier = Modifier.fillMaxWidth(),
                            messageUser = model,
                            onClick = {
                                event(SearchUserUiEvent.OnSelectConversation(model))
                            }
                        )
                    }
                }
            }

        }
    }
}