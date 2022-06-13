package com.example.cyclistance.navigation

import androidx.compose.runtime.*
import androidx.lifecycle.LiveData
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.cyclistance.common.MappingConstants
import com.example.cyclistance.common.MappingConstants.MISSING_PHONE_NUMBER
import com.example.cyclistance.feature_no_internet.presentation.components.NoInternetScreen
import com.example.cyclistance.feature_authentication.presentation.authentication_email.components.EmailAuthScreen
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.components.SignInScreen
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.components.SignUpScreen
import com.example.cyclistance.feature_main_screen.presentation.mapping_cancellation.components.components.CancellationReasonScreen
import com.example.cyclistance.feature_main_screen.presentation.mapping_confirm_details.components.components.ConfirmationDetailsScreen
import com.example.cyclistance.feature_main_screen.presentation.mapping_main_screen.components.MappingScreen
import com.example.cyclistance.feature_main_screen.presentation.mapping_rescue_request.components.RescueRequestScreen
import com.example.cyclistance.feature_readable_displays.presentation.intro_slider.components.IntroSliderScreen
import com.example.cyclistance.feature_readable_displays.presentation.splash_screen.components.SplashScreen
import com.example.cyclistance.feature_settings.presentation.setting_change_password.components.ChangePasswordScreen
import com.example.cyclistance.feature_settings.presentation.setting_edit_profile.components.EditProfileScreen
import com.example.cyclistance.feature_settings.presentation.setting_main_screen.components.SettingScreen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import timber.log.Timber

@ExperimentalPermissionsApi
@Composable
fun Navigation(
    navController: NavHostController,
    onToggleTheme: () -> Unit,
    isDarkTheme: Boolean,
    isDarkThemeLiveData: LiveData<Boolean>,
    onBackPressed: () -> Unit) {

    NavHost(navController = navController, startDestination = Screens.SplashScreen.route) {
        composable(Screens.SplashScreen.route) {
            SplashScreen { destination, popUpToDestination ->
                navigateScreen(navController, destination, popUpToDestination)
            }
        }


        composable(Screens.IntroSliderScreen.route) {
            IntroSliderScreen(onBackPressed = onBackPressed) { destination, popUpToDestination ->
                navigateScreen(navController, destination, popUpToDestination)
            }
        }

        composable(Screens.SignInScreen.route) {
            SignInScreen(onBackPressed = onBackPressed) { destination, popUpToDestination ->
                navigateScreen(navController, destination, popUpToDestination)
            }
        }

        composable(Screens.SignUpScreen.route) {
            SignUpScreen { destination, popUpToDestination ->
                navigateScreen(navController, destination, popUpToDestination)
            }
        }

        composable(Screens.EmailAuthScreen.route) {
            EmailAuthScreen(
                onBackPressed = onBackPressed,
                isDarkTheme = isDarkTheme) { destination, popUpToDestination ->
                navigateScreen(navController, destination, popUpToDestination)
            }
        }
        composable(Screens.MappingScreen.route) {
            MappingScreen(
                isDarkTheme = isDarkThemeLiveData,
                onBackPressed = onBackPressed) { destination, popUpToDestination ->
                navigateScreen(navController, destination, popUpToDestination)
            }
        }
        composable(Screens.NoInternetScreen.route) {
            NoInternetScreen(onBackPressed = onBackPressed) {
                navController.popBackStack()
            }
        }

        composable(Screens.CancellationScreen.route) {
            CancellationReasonScreen()
        }

        composable(Screens.ConfirmDetailsScreen.route) {
            ConfirmationDetailsScreen()
        }

        composable(Screens.RescueRequestScreen.route) {
            RescueRequestScreen()
        }

        composable(Screens.ChangePasswordScreen.route) {
            ChangePasswordScreen()
        }

        composable(
            route = Screens.EditProfileScreen.route + "/{${MISSING_PHONE_NUMBER}}",
            arguments = listOf(navArgument(MISSING_PHONE_NUMBER){ type = NavType.BoolType })) { backStackEntry ->

            EditProfileScreen(hasMissingPhoneNumber = backStackEntry.arguments?.getBoolean(MISSING_PHONE_NUMBER)) { destination, popUpToDestination ->
                navigateScreen(navController, destination, popUpToDestination)
            }

        }

        composable(Screens.SettingScreen.route){
            SettingScreen()
        }

    }
}

private fun navigateScreen(
    navController: NavHostController,
    destination: String,
    popUpToDestination: String? = null) {


    navController.navigate(destination) {
        popUpToDestination?.let {
            popUpTo(it) {
                inclusive = true
            }
        }
        launchSingleTop = true
    }
}