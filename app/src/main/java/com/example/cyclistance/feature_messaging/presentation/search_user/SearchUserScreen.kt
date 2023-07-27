package com.example.cyclistance.feature_messaging.presentation.search_user

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
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
import com.example.cyclistance.feature_messaging.presentation.search_user.components.SearchUserSection
import com.example.cyclistance.feature_messaging.presentation.search_user.event.SearchUserUiEvent
import com.example.cyclistance.navigation.Screens
import com.example.cyclistance.navigation.nav_graph.navigateScreen
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
        { chatItem: MessagingUserItemModel ->
            val encodedUrl =
                URLEncoder.encode(chatItem.userDetails.photo, StandardCharsets.UTF_8.toString())
            navController.navigateScreen(
                destination = "${Screens.MessagingNavigation.ConversationScreen.screenRoute}/${chatItem.userDetails.uid}/$encodedUrl/${chatItem.userDetails.name}",
                popUpToDestination = Screens.MessagingNavigation.ChatScreen.screenRoute
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



    SearchUserSection(
        modifier = Modifier.animateContentSize(
            animationSpec = tween(
                durationMillis = 1,
                easing = FastOutLinearInEasing
            )
        ).padding(paddingValues),
        searchQuery = searchQuery,
        focusRequester = focusRequester,
        state = state,
        event =  {event ->
            when(event){
                is SearchUserUiEvent.OnSelectConversation -> onSelectConversation(event.messageUser)
                is SearchUserUiEvent.CloseScreen -> closeMessagingScreen()
                is SearchUserUiEvent.OnSearchQueryChanged -> onSearchQueryChanged(event.searchQuery)
                is SearchUserUiEvent.ClearSearchQuery -> onClearSearchQuery()
            }
        }
    )
}