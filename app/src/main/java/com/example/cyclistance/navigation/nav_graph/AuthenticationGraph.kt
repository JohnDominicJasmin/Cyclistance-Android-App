package com.example.cyclistance.navigation.nav_graph

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.cyclistance.feature_authentication.presentation.authentication_email.EmailAuthScreen
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.SignInScreen
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.SignUpScreen
import com.example.cyclistance.navigation.Screens

fun NavGraphBuilder.authenticationGraph(
    navController: NavHostController,
    paddingValues: PaddingValues) {
    navigation(
        startDestination = Screens.AuthenticationNavigation.SignInScreen.screenRoute,
        route = Screens.AuthenticationNavigation.ROUTE) {


        composable(route = Screens.AuthenticationNavigation.SignInScreen.screenRoute) {
            SignInScreen(
                navController = navController,
                paddingValues = paddingValues)
        }
        composable(route = Screens.AuthenticationNavigation.SignUpScreen.screenRoute) {
            SignUpScreen(
                navController = navController,
                paddingValues = paddingValues)
        }
        composable(route = Screens.AuthenticationNavigation.EmailAuthScreen.screenRoute) {
            EmailAuthScreen(
                navController = navController,
                paddingValues = paddingValues)
        }
    }

}