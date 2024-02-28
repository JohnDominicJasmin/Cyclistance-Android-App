package com.myapp.cyclistance.navigation.nav_graph

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.myapp.cyclistance.core.utils.constants.UserProfileConstants
import com.myapp.cyclistance.feature_user_profile.presentation.edit_profile.EditProfileScreen
import com.myapp.cyclistance.feature_user_profile.presentation.user_profile.UserProfileScreen
import com.myapp.cyclistance.navigation.Screens

fun NavGraphBuilder.userProfileGraph(
    navController: NavController,
    paddingValues: PaddingValues,
) {

    navigation(
        startDestination = Screens.UserProfileNavigation.UserProfile.screenRoute,
        route = Screens.UserProfileNavigation.ROUTE) {


        composable(Screens.UserProfileNavigation.UserProfile.screenRoute, arguments = listOf(
            navArgument(
                name = UserProfileConstants.USER_ID
            ){
                type = NavType.StringType
            })) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString(UserProfileConstants.USER_ID)!!
            UserProfileScreen(
                navController = navController,
                paddingValues = paddingValues,
                userId = userId)
        }

        composable(Screens.UserProfileNavigation.EditProfile.screenRoute){
            EditProfileScreen(
                navController = navController,
                paddingValues = paddingValues)
        }

    }

}