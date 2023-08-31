package com.example.cyclistance.navigation.nav_graph

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.cyclistance.feature_user_profile.presentation.edit_profile.EditProfileScreen
import com.example.cyclistance.feature_user_profile.presentation.user_profile.UserProfileScreen
import com.example.cyclistance.navigation.Screens

fun NavGraphBuilder.userProfileGraph(
    navController: NavController,
    paddingValues: PaddingValues,
) {

    navigation(
        startDestination = Screens.UserProfileNavigation.UserProfile.screenRoute,
        route = Screens.UserProfileNavigation.ROUTE) {


        composable(Screens.UserProfileNavigation.UserProfile.screenRoute) {
            UserProfileScreen(
                navController = navController,
                paddingValues = paddingValues)
        }

        composable(Screens.UserProfileNavigation.EditProfile.screenRoute){
            EditProfileScreen(
                navController = navController,
                paddingValues = paddingValues)
        }

    }

}