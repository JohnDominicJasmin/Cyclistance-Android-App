package com.example.cyclistance.navigation.nav_graph

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.cyclistance.core.utils.constants.MessagingConstants.CONVERSATION_USER
import com.example.cyclistance.feature_messaging.domain.model.ui.chats.MessagingUserItemModel
import com.example.cyclistance.feature_messaging.presentation.chat.chats.ChatsScreen
import com.example.cyclistance.feature_messaging.presentation.conversation.ConversationScreen
import com.example.cyclistance.feature_messaging.presentation.search_user.SearchUserScreen
import com.example.cyclistance.navigation.Screens

fun NavGraphBuilder.messagingGraph(
    navController: NavController,
    paddingValues: PaddingValues,
    isInternetAvailable: Boolean,
    newConversationDetails: (MessagingUserItemModel) -> Unit) {

    navigation(
        startDestination = Screens.MessagingNavigation.ChatScreen.screenRoute,
        route = Screens.MessagingNavigation.ROUTE
    ) {


        composable(Screens.MessagingNavigation.ChatScreen.screenRoute, ) {
            ChatsScreen(
                navController = navController,
                paddingValues = paddingValues,
                isInternetAvailable = isInternetAvailable
            )
        }

        composable(Screens.MessagingNavigation.SearchUserScreen.screenRoute,         ) {
            SearchUserScreen(
                navController = navController,
                paddingValues = paddingValues
            )
        }

        composable(route = Screens.MessagingNavigation.ConversationScreen.screenRoute + "/{$CONVERSATION_USER}",
            arguments = listOf(navArgument(CONVERSATION_USER) { type = NavType.StringType })
        ) {

            ConversationScreen(
                navController = navController,
                paddingValues = paddingValues,
                newConversationDetails = newConversationDetails
            )
        }


    }
}

