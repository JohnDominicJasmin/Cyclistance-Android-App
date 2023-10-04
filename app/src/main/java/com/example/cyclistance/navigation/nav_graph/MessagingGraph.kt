package com.example.cyclistance.navigation.nav_graph

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.example.cyclistance.core.utils.constants.MessagingConstants.CONVERSATION_ID
import com.example.cyclistance.core.utils.constants.MessagingConstants.MESSAGING_URI
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
        startDestination = Screens.MessagingNavigation.Chats.screenRoute,
        route = Screens.MessagingNavigation.ROUTE
    ) {


        composable(Screens.MessagingNavigation.Chats.screenRoute) {
            ChatsScreen(
                navController = navController,
                paddingValues = paddingValues,
                isInternetAvailable = isInternetAvailable
            )
        }

        composable(Screens.MessagingNavigation.SearchUser.screenRoute) {
            SearchUserScreen(
                navController = navController,
                paddingValues = paddingValues
            )
        }

        composable(route = Screens.MessagingNavigation.Conversation.screenRoute,
            deepLinks = listOf(
                navDeepLink {
                    uriPattern =
                        "$MESSAGING_URI/${CONVERSATION_ID}={${CONVERSATION_ID}}"
                })) {

            val arguments = it.arguments!!
            val receiverId = arguments.getString(CONVERSATION_ID)!!

            ConversationScreen(
                navController = navController,
                paddingValues = paddingValues,
                userReceiverId = receiverId,
                newConversationDetails = newConversationDetails,
                isInternetAvailable = isInternetAvailable
            )
        }


    }
}

