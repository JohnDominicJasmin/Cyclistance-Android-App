package com.example.cyclistance.navigation.nav_graph

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.example.cyclistance.core.utils.constants.MessagingConstants.MESSAGING_URI
import com.example.cyclistance.core.utils.constants.MessagingConstants.RECEIVER_MESSAGE_ARG
import com.example.cyclistance.core.utils.constants.MessagingConstants.SENDER_MESSAGE_ARG
import com.example.cyclistance.feature_messaging.domain.model.ui.chats.MessagingUserItemModel
import com.example.cyclistance.feature_messaging.presentation.chat.chats.ChatsScreen
import com.example.cyclistance.feature_messaging.presentation.conversation.ConversationScreen
import com.example.cyclistance.feature_messaging.presentation.search_user.SearchUserScreen
import com.example.cyclistance.navigation.Screens
import com.google.gson.Gson


fun NavGraphBuilder.messagingGraph(
    navController: NavController,
    paddingValues: PaddingValues,
    isInternetAvailable: Boolean,
    newConversationDetails: (MessagingUserItemModel) -> Unit) {

    navigation(
        startDestination = Screens.MessagingNavigation.ChatScreen.screenRoute,
        route = Screens.MessagingNavigation.ROUTE
    ) {


        composable(Screens.MessagingNavigation.ChatScreen.screenRoute) {
            ChatsScreen(
                navController = navController,
                paddingValues = paddingValues,
                isInternetAvailable = isInternetAvailable
            )
        }

        composable(Screens.MessagingNavigation.SearchUserScreen.screenRoute) {
            SearchUserScreen(
                navController = navController,
                paddingValues = paddingValues
            )
        }

        composable(route = Screens.MessagingNavigation.ConversationScreen.screenRoute,
            deepLinks = listOf(
                navDeepLink {
                    uriPattern =
                        "$MESSAGING_URI/$RECEIVER_MESSAGE_ARG={$RECEIVER_MESSAGE_ARG}&$SENDER_MESSAGE_ARG={$SENDER_MESSAGE_ARG}"
                }
            )
        ) {

            val arguments = it.arguments!!
            val userReceiverObject = arguments.getString(RECEIVER_MESSAGE_ARG)
            val userSenderObject = arguments.getString(SENDER_MESSAGE_ARG)
            val userReceiverMessage = Gson().fromJson(userReceiverObject, MessagingUserItemModel::class.java)
            val userSenderMessage = Gson().fromJson(userSenderObject, MessagingUserItemModel::class.java)

            ConversationScreen(
                navController = navController,
                paddingValues = paddingValues,
                userReceiverMessage = userReceiverMessage,
                userSenderMessage = userSenderMessage,
                newConversationDetails = newConversationDetails
            )
        }


    }
}

