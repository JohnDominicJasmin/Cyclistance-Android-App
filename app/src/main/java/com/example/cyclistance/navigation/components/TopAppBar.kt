package com.example.cyclistance.navigation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.cyclistance.core.utils.constants.EmergencyCallConstants
import com.example.cyclistance.core.utils.constants.NavigationConstants
import com.example.cyclistance.navigation.Screens
import com.example.cyclistance.theme.CyclistanceTheme
import com.example.cyclistance.top_bars.DefaultTopBar
import com.example.cyclistance.top_bars.TitleTopAppBar
import com.example.cyclistance.top_bars.TopAppBarCreator

@Composable
fun TopAppBar(
    onClickArrowBackIcon: () -> Unit = {},
    onClickMenuIcon: () -> Unit = {},
    isNavigating: Boolean,
    route: String?) {

    when (route) {
        Screens.MappingNavigation.MappingScreen.screenRoute -> {
            AnimatedVisibility(
                visible = isNavigating.not(),
                enter = fadeIn(initialAlpha = 0.4f),
                exit = fadeOut(animationSpec = tween(durationMillis = 100))) {

                DefaultTopBar(onClickIcon = onClickMenuIcon)
            }
        }

        "${Screens.MappingNavigation.CancellationScreen.screenRoute}/{${NavigationConstants.CANCELLATION_TYPE}}/{${NavigationConstants.TRANSACTION_ID}}/{${NavigationConstants.CLIENT_ID}}" -> {
            TopAppBarCreator(
                icon = Icons.Default.ArrowBack,
                onClickIcon = onClickArrowBackIcon,
                topAppBarTitle = {
                    TitleTopAppBar(title = "Cancellation Reason")
                })
        }

        Screens.MappingNavigation.ConfirmDetailsScreen.screenRoute + "?${NavigationConstants.LATITUDE}={${NavigationConstants.LATITUDE}}&${NavigationConstants.LONGITUDE}={${NavigationConstants.LONGITUDE}}" -> {
            TopAppBarCreator(
                icon = Icons.Default.ArrowBack,
                onClickIcon = onClickArrowBackIcon,
                topAppBarTitle = {
                    TitleTopAppBar(
                        title = "Confirmation Details")
                })
        }


        Screens.SettingsNavigation.ChangePasswordScreen.screenRoute -> {
            TopAppBarCreator(
                icon = Icons.Default.ArrowBack,
                onClickIcon = onClickArrowBackIcon,
                topAppBarTitle = {
                    TitleTopAppBar(title = "Change Password")
                })
        }

        Screens.SettingsNavigation.EditProfileScreen.screenRoute -> {
            TopAppBarCreator(
                icon = Icons.Default.ArrowBack,
                onClickIcon = onClickArrowBackIcon,
                topAppBarTitle = {
                    TitleTopAppBar(title = "Edit Profile")
                })
        }

        Screens.SettingsNavigation.SettingScreen.screenRoute -> {
            TopAppBarCreator(
                icon = Icons.Default.ArrowBack,
                onClickIcon = onClickArrowBackIcon,
                topAppBarTitle = {
                    TitleTopAppBar(title = "Settings")
                })
        }

        Screens.EmergencyCallNavigation.EmergencyCallScreen.screenRoute + "?${EmergencyCallConstants.SHOULD_OPEN_CONTACT_DIALOG}={${EmergencyCallConstants.SHOULD_OPEN_CONTACT_DIALOG}}" -> {

            TopAppBarCreator(
                icon = Icons.Default.ArrowBack,
                onClickIcon = onClickArrowBackIcon,
                topAppBarTitle = {
                    TitleTopAppBar(title = "Emergency Call")
                })
        }


        Screens.RideHistoryNavigation.RideHistoryScreen.screenRoute -> {
            TopAppBarCreator(
                icon = Icons.Default.ArrowBack,
                onClickIcon = onClickArrowBackIcon,
                topAppBarTitle = {
                    TitleTopAppBar(title = "Ride History")
                })
        }

        Screens.RideHistoryNavigation.RideHistoryDetailsScreen.screenRoute -> {
            TopAppBarCreator(
                icon = Icons.Default.Close,
                onClickIcon = onClickArrowBackIcon,
                topAppBarTitle = {
                    TitleTopAppBar(title = "Rescue Details")
                })
        }


    }

}


@Preview
@Composable
fun PreviewTopAppBarDark() {
    CyclistanceTheme(true) {
        Surface(color = MaterialTheme.colors.background) {
            TopAppBar(
                route = Screens.SettingsNavigation.EditProfileScreen.screenRoute,
                isNavigating = false)
        }
    }
}


@Preview
@Composable
fun PreviewTopAppBarLight() {
    CyclistanceTheme(false) {
        Surface(color = MaterialTheme.colors.background) {
            TopAppBar(
                route = Screens.SettingsNavigation.EditProfileScreen.screenRoute,
                isNavigating = false)
        }
    }
}

