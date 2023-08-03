package com.example.cyclistance.navigation.nav_graph

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.cyclistance.core.utils.constants.MessagingConstants.CHAT_AVAILABILITY
import com.example.cyclistance.core.utils.constants.MessagingConstants.CHAT_ID
import com.example.cyclistance.core.utils.constants.MessagingConstants.CHAT_NAME
import com.example.cyclistance.core.utils.constants.MessagingConstants.CHAT_PHOTO_URL
import com.example.cyclistance.feature_messaging.presentation.chat.chats.ChatsScreen
import com.example.cyclistance.feature_messaging.presentation.conversation.ConversationScreen
import com.example.cyclistance.feature_messaging.presentation.search_user.SearchUserScreen
import com.example.cyclistance.navigation.Screens

fun NavGraphBuilder.messagingGraph(
    navController: NavController,
    paddingValues: PaddingValues,
    newConversationDetails: (name: String, photoUrl: String, availability: Boolean) -> Unit) {

    navigation(
        startDestination = Screens.MessagingNavigation.ChatScreen.screenRoute,
        route = Screens.MessagingNavigation.ROUTE
    ) {


        composable(Screens.MessagingNavigation.ChatScreen.screenRoute, ) {
            ChatsScreen(
                navController = navController,
                paddingValues = paddingValues
            )
        }

        composable(Screens.MessagingNavigation.SearchUserScreen.screenRoute,         ) {
            SearchUserScreen(
                navController = navController,
                paddingValues = paddingValues
            )
        }

        composable(route = Screens.MessagingNavigation.ConversationScreen.screenRoute + "/{$CHAT_ID}/{${CHAT_PHOTO_URL}}/{$CHAT_NAME}/{$CHAT_AVAILABILITY}",
            arguments = listOf(
                navArgument(CHAT_ID) { type = NavType.StringType },
                navArgument(CHAT_PHOTO_URL) { type = NavType.StringType },
                navArgument(CHAT_NAME) { type = NavType.StringType },
                navArgument(CHAT_AVAILABILITY) { type = NavType.BoolType }),
        ) {

            ConversationScreen(
                navController = navController,
                paddingValues = paddingValues,
                newConversationDetails = newConversationDetails
            )
        }


    }
}

