package com.example.cyclistance.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.cyclistance.core.utils.constants.MappingConstants.SELECTION_RESCUEE_TYPE
import com.example.cyclistance.core.utils.constants.NavigationConstants.BOTTOM_SHEET_TYPE
import com.example.cyclistance.core.utils.constants.NavigationConstants.CANCELLATION_TYPE
import com.example.cyclistance.core.utils.constants.NavigationConstants.CLIENT_ID
import com.example.cyclistance.core.utils.constants.NavigationConstants.LATITUDE
import com.example.cyclistance.core.utils.constants.NavigationConstants.LONGITUDE
import com.example.cyclistance.core.utils.constants.NavigationConstants.TRANSACTION_ID
import com.example.cyclistance.feature_authentication.presentation.authentication_email.EmailAuthScreen
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.SignInScreen
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.SignUpScreen
import com.example.cyclistance.feature_mapping.presentation.mapping_cancellation_reason.CancellationReasonScreen
import com.example.cyclistance.feature_mapping.presentation.mapping_confirm_details.ConfirmDetailsScreen
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.MappingScreen
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.MappingViewModel
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.utils.BottomSheetType
import com.example.cyclistance.feature_mapping.presentation.mapping_rescue_request.RescueRequestScreen
import com.example.cyclistance.feature_readable_displays.presentation.intro_slider.IntroSliderScreen
import com.example.cyclistance.feature_settings.presentation.setting_change_password.ChangePasswordScreen
import com.example.cyclistance.feature_settings.presentation.setting_edit_profile.EditProfileScreen
import com.example.cyclistance.feature_settings.presentation.setting_edit_profile.EditProfileViewModel
import com.example.cyclistance.feature_settings.presentation.setting_screen.SettingScreen
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
    isNavigating: Boolean,
    onChangeNavigatingState: (isNavigating: Boolean) -> Unit,
    onToggleTheme: () -> Unit) {

    NavHost(navController = navController, startDestination = Screens.IntroSliderScreen.route) {

        composable(Screens.IntroSliderScreen.route) {
            IntroSliderScreen(
                navController = navController,
                paddingValues = paddingValues)
        }

        composable(Screens.SignInScreen.route) {
            SignInScreen(
                navController = navController,
                paddingValues = paddingValues)
        }

        composable(Screens.SignUpScreen.route) {
            SignUpScreen(
                navController = navController,
                paddingValues = paddingValues)
        }

        composable(Screens.EmailAuthScreen.route) {
            EmailAuthScreen(
                isDarkTheme = isDarkTheme,
                navController = navController,
                paddingValues = paddingValues)
        }




        composable(
            route = Screens.MappingScreen.route + "?$BOTTOM_SHEET_TYPE={$BOTTOM_SHEET_TYPE}",
            arguments = listOf(navArgument(BOTTOM_SHEET_TYPE) {
                defaultValue = BottomSheetType.Collapsed.type
            })
        ) {

            it.arguments?.getString(BOTTOM_SHEET_TYPE)?.let { bottomSheetType ->
                MappingScreen(
                    hasInternetConnection = hasInternetConnection,
                    typeBottomSheet = bottomSheetType,
                    isDarkTheme = isDarkTheme,
                    navController = navController,
                    paddingValues = paddingValues,
                    mappingViewModel = mappingViewModel,
                    isNavigating = isNavigating,
                    onChangeNavigatingState = onChangeNavigatingState
                    )
            }
        }


        composable(
            route = "${Screens.CancellationScreen.route}/{$CANCELLATION_TYPE}/{$TRANSACTION_ID}/{$CLIENT_ID}",
            arguments = listOf(
                navArgument(CANCELLATION_TYPE) { defaultValue = SELECTION_RESCUEE_TYPE },
                navArgument(TRANSACTION_ID) {},
                navArgument(CLIENT_ID) {})) {

            it.arguments?.getString(CANCELLATION_TYPE)?.let { cancellationType ->
                CancellationReasonScreen(
                    navController = navController,
                    paddingValues = paddingValues,
                    cancellationType = cancellationType)
            }
        }


        composable(route = Screens.ConfirmDetailsScreen.route + "?$LATITUDE={$LATITUDE}&$LONGITUDE={$LONGITUDE}",
            arguments = listOf(
                navArgument(LATITUDE) { type = NavType.FloatType; defaultValue = -1f },
                navArgument(LONGITUDE) { type = NavType.FloatType; defaultValue = -1f }
            )) {

            ConfirmDetailsScreen(
                navController = navController,
                paddingValues = paddingValues)

        }

        composable(Screens.RescueRequestScreen.route) {
            RescueRequestScreen(
                navController = navController,
                paddingValues = paddingValues,
                mappingViewModel = mappingViewModel)
        }

        composable(Screens.ChangePasswordScreen.route) {
            ChangePasswordScreen(paddingValues = paddingValues)
        }

        composable(Screens.EditProfileScreen.route) {
            EditProfileScreen(
                navController = navController,
                paddingValues = paddingValues,
                editProfileViewModel = editProfileViewModel)
        }

        composable(Screens.SettingScreen.route) {
            SettingScreen(
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
