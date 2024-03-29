package com.myapp.cyclistance.navigation.nav_graph

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.myapp.cyclistance.feature_authentication.presentation.auth_email.EmailAuthScreen
import com.myapp.cyclistance.feature_authentication.presentation.auth_forgot_password.ForgotPasswordScreen
import com.myapp.cyclistance.feature_authentication.presentation.auth_reset_password.ResetPasswordScreen
import com.myapp.cyclistance.feature_authentication.presentation.auth_sign_in.SignInScreen
import com.myapp.cyclistance.feature_authentication.presentation.auth_sign_up.SignUpScreen
import com.myapp.cyclistance.navigation.Screens

fun NavGraphBuilder.authenticationGraph(
    navController: NavHostController,
    paddingValues: PaddingValues) {
    navigation(
        startDestination = Screens.AuthenticationNavigation.SignIn.screenRoute,
        route = Screens.AuthenticationNavigation.ROUTE) {


        composable(route = Screens.AuthenticationNavigation.SignIn.screenRoute) {
            SignInScreen(
                navController = navController,
                paddingValues = paddingValues)
        }
        composable(route = Screens.AuthenticationNavigation.SignUp.screenRoute) {
            SignUpScreen(
                navController = navController,
                paddingValues = paddingValues)
        }
        composable(route = Screens.AuthenticationNavigation.EmailAuth.screenRoute) {
            EmailAuthScreen(
                navController = navController,
                paddingValues = paddingValues)
        }
        composable(route = Screens.AuthenticationNavigation.ForgotPassword.screenRoute){
            ForgotPasswordScreen(
                navController = navController,
                paddingValues = paddingValues
            )
        }
        composable(route = Screens.AuthenticationNavigation.ResetPassword.screenRoute){
            ResetPasswordScreen(
                navController = navController,
                paddingValues = paddingValues
            )
        }

    }

}