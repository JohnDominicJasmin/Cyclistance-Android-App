package com.example.cyclistance.navigation.nav_graph

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.cyclistance.core.utils.constants.MessagingConstants.CHAT_ID
import com.example.cyclistance.core.utils.constants.MessagingConstants.CHAT_NAME
import com.example.cyclistance.core.utils.constants.MessagingConstants.CHAT_PHOTO_URL
import com.example.cyclistance.feature_messaging.presentation.chats.MessagingScreen
import com.example.cyclistance.feature_messaging.presentation.conversation.ConversationScreen
import com.example.cyclistance.navigation.Screens

fun NavGraphBuilder.messagingGraph(
    navController: NavController,
    paddingValues: PaddingValues) {
    navigation(
        startDestination = Screens.Messaging.MessagingScreen.screenRoute,
        route = Screens.Messaging.ROUTE
    ) {
        composable(Screens.Messaging.MessagingScreen.screenRoute) {
            MessagingScreen(
                navController = navController,
                paddingValues = paddingValues
            )
        }

        composable(route = Screens.Messaging.ConversationScreen.screenRoute + "/{$CHAT_ID}/{${CHAT_PHOTO_URL}}/{$CHAT_NAME]}",
            arguments =
            listOf(
                navArgument(CHAT_ID) { type = NavType.StringType },
                navArgument(CHAT_PHOTO_URL) { type = NavType.StringType },
                navArgument(CHAT_NAME) { type = NavType.StringType }
            )
        ) {
            ConversationScreen(
                navController = navController,
                paddingValues = paddingValues
            )
        }
    }
}

