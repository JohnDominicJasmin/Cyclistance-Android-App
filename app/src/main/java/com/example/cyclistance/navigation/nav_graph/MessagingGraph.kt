package com.example.cyclistance.navigation.nav_graph

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.cyclistance.core.utils.constants.MessagingConstants.CONVERSATION_ID
import com.example.cyclistance.feature_messaging.presentation.conversation.ConversationScreen
import com.example.cyclistance.navigation.Screens


fun NavGraphBuilder.messagingGraph(
    navController: NavController,
    paddingValues: PaddingValues,
    isInternetAvailable: Boolean) {

    navigation(
        startDestination = Screens.MessagingNavigation.Conversation.screenRoute,
        route = Screens.MessagingNavigation.ROUTE
    ) {

        composable(route = Screens.MessagingNavigation.Conversation.screenRoute, arguments = listOf(
            navArgument(name = CONVERSATION_ID) {
                defaultValue = ""
                type = NavType.StringType
            }
        )) { backStackEntry ->

            val receiverId = backStackEntry.arguments?.getString(CONVERSATION_ID)!!

            ConversationScreen(
                navController = navController,
                paddingValues = paddingValues,
                userReceiverId = receiverId,
                isInternetAvailable = isInternetAvailable
            )
        }


    }
}

