package com.example.cyclistance.navigation.nav_graph

import MessagingConversationScreen
import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.cyclistance.feature_messaging.presentation.messaging_main_screen.MessagingScreen
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

        composable(Screens.Messaging.ConversationScreen.screenRoute) {
            MessagingConversationScreen(
                navController = navController,
                paddingValues = paddingValues)
        }
    }
}
