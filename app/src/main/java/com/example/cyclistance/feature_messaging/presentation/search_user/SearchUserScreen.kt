package com.example.cyclistance.feature_messaging.presentation.search_user

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.input.TextFieldValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.cyclistance.feature_messaging.domain.model.ui.chats.MessagingUserItemModel
import com.example.cyclistance.feature_messaging.presentation.search_user.components.SearchUserContent
import com.example.cyclistance.feature_messaging.presentation.search_user.event.SearchUserUiEvent
import com.example.cyclistance.navigation.Screens
import com.example.cyclistance.navigation.nav_graph.navigateScreen
import com.google.gson.Gson
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun SearchUserScreen(
    viewModel: SearchUserViewModel = hiltViewModel(),
    navController: NavController,
    paddingValues: PaddingValues) {
    val focusRequester = remember { FocusRequester() }
    val state by viewModel.state.collectAsStateWithLifecycle()
    var searchQuery by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(
            TextFieldValue())
    }


    val closeMessagingScreen = remember {
        {
            navController.popBackStack()
        }
    }
    val onSelectConversation = remember {
        { user: MessagingUserItemModel ->
            val encodedUrl = URLEncoder.encode(user.userDetails.photo, StandardCharsets.UTF_8.toString())
            val jsonString = Gson().toJson(user.copy(userDetails = user.userDetails.copy(photo = encodedUrl)))

            navController.navigateScreen(
                route = Screens.MessagingNavigation.ConversationScreen.passArgument(
                    message =  jsonString
                )
            )

        }
    }


    val onSearchQueryChanged = remember {
        { input: TextFieldValue ->
            searchQuery = input
        }
    }

    val onClearSearchQuery = remember {
        {
            searchQuery = TextFieldValue()
        }
    }

    BackHandler {
        closeMessagingScreen()
    }


    SearchUserContent(
        modifier = Modifier.fillMaxSize().padding(paddingValues = paddingValues),
        searchQuery = searchQuery,
        focusRequester = focusRequester,
        state = state,
        event = { event ->
            when (event) {
                is SearchUserUiEvent.OnSelectConversation -> onSelectConversation(event.messageUser)
                is SearchUserUiEvent.CloseScreen -> closeMessagingScreen()
                is SearchUserUiEvent.OnSearchQueryChanged -> onSearchQueryChanged(event.searchQuery)
                is SearchUserUiEvent.ClearSearchQuery -> onClearSearchQuery()
            }
        }
    )
}