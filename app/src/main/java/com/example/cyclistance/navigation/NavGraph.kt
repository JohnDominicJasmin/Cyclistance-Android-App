package com.example.cyclistance.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.cyclistance.feature_authentication.presentation.authentication_email.EmailAuthScreen
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.SignInScreen
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.SignUpScreen
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_cancellation.components.jb.CancellationReasonScreen
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_confirm_details.ConfirmDetailsScreen
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.MappingScreen
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.MappingViewModel
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_rescue_request.RescueRequestScreen
import com.example.cyclistance.feature_readable_displays.presentation.intro_slider.IntroSliderScreen
import com.example.cyclistance.feature_settings.presentation.setting_change_password.ChangePasswordScreen
import com.example.cyclistance.feature_settings.presentation.setting_edit_profile.EditProfileScreen
import com.example.cyclistance.feature_settings.presentation.setting_edit_profile.EditProfileViewModel
import com.example.cyclistance.feature_settings.presentation.setting_main_screen.SettingScreen
import com.google.accompanist.permissions.ExperimentalPermissionsApi


@ExperimentalPermissionsApi
@Composable
fun NavGraph(
    hasInternetConnection: Boolean,
    navController: NavHostController,
    paddingValues: PaddingValues,
    editProfileViewModel: EditProfileViewModel,
    mappingViewModel: MappingViewModel,
    isDarkTheme: Boolean,
    scaffoldState: ScaffoldState,
    onToggleTheme: () -> Unit) {

    NavHost(navController = navController, startDestination = Screens.IntroSliderScreen.route) {

        composable(Screens.IntroSliderScreen.route) {
            IntroSliderScreen(navController = navController, paddingValues = paddingValues, hasInternetConnection = hasInternetConnection)
        }

        composable(Screens.SignInScreen.route) {
            SignInScreen(navController = navController, paddingValues = paddingValues, hasInternetConnection = hasInternetConnection)
        }

        composable(Screens.SignUpScreen.route) {
            SignUpScreen(navController = navController, paddingValues = paddingValues, hasInternetConnection = hasInternetConnection)
        }

        composable(Screens.EmailAuthScreen.route) {
            EmailAuthScreen(
                isDarkTheme = isDarkTheme,
                navController = navController,
                paddingValues = paddingValues,
                hasInternetConnection = hasInternetConnection)
        }




        composable(
            Screens.MappingScreen.route + "?bottomSheetType={bottomSheetType}",
            arguments = listOf(navArgument("bottomSheetType") {
                defaultValue = ""
            })
        ) {

            it.arguments?.getString("bottomSheetType")?.let { bottomSheetType ->
                MappingScreen(
                    hasInternetConnection = hasInternetConnection,
                    typeBottomSheet = bottomSheetType,
                    isDarkTheme = isDarkTheme,
                    navController = navController,
                    scaffoldState = scaffoldState,
                    paddingValues = paddingValues,
                    mappingViewModel = mappingViewModel)
            }
        }


        composable(Screens.CancellationScreen.route) {
            CancellationReasonScreen(paddingValues = paddingValues)
        }


        composable(Screens.ConfirmDetailsScreen.route) {

            ConfirmDetailsScreen(
                hasInternetConnection = hasInternetConnection,
                navController = navController,
                paddingValues = paddingValues)
        }

        composable(Screens.RescueRequestScreen.route) {
            RescueRequestScreen(
                hasInternetConnection = hasInternetConnection,
                navController = navController,
                paddingValues = paddingValues,
                mappingViewModel = mappingViewModel)
        }

        composable(Screens.ChangePasswordScreen.route) {
            ChangePasswordScreen(paddingValues = paddingValues)
        }

        composable(Screens.EditProfileScreen.route) {
            EditProfileScreen(
                hasInternetConnection = hasInternetConnection,
                navController = navController,
                paddingValues = paddingValues,
                editProfileViewModel = editProfileViewModel)
        }

        composable(Screens.SettingScreen.route) {
            SettingScreen(
                hasInternetConnection = hasInternetConnection,
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme,
                navController = navController,
                paddingValues = paddingValues)

        }

    }
}

fun NavController.navigateScreenInclusively(
    destination: String,
    popUpToDestination: String) {
    navigate(destination) {
        popUpTo(popUpToDestination) {
            inclusive = true
        }
        launchSingleTop = true
    }


}

fun NavController.navigateScreen(destination: String) {
    navigate(destination) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
