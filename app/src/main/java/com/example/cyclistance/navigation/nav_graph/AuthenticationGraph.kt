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
        startDestination = Screens.Authentication.SignInScreen.screenRoute,
        route = Screens.Authentication.ROUTE) {


        composable(route = Screens.Authentication.SignInScreen.screenRoute) {
            SignInScreen(
                navController = navController,
                paddingValues = paddingValues)
        }
        composable(route = Screens.Authentication.SignUpScreen.screenRoute) {
            SignUpScreen(
                navController = navController,
                paddingValues = paddingValues)
        }
        composable(route = Screens.Authentication.EmailAuthScreen.screenRoute) {
            EmailAuthScreen(
                navController = navController,
                paddingValues = paddingValues)
        }
    }

}