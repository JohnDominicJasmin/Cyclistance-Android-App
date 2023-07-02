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
        Screens.Mapping.MappingScreen.screenRoute -> {
            AnimatedVisibility(
                visible = isNavigating.not(),
                enter = fadeIn(initialAlpha = 0.4f),
                exit = fadeOut(animationSpec = tween(durationMillis = 100))) {

                DefaultTopBar(onClickIcon = onClickMenuIcon)
            }
        }

        "${Screens.Mapping.CancellationScreen.screenRoute}/{${NavigationConstants.CANCELLATION_TYPE}}/{${NavigationConstants.TRANSACTION_ID}}/{${NavigationConstants.CLIENT_ID}}" -> {
            TopAppBarCreator(
                icon = Icons.Default.ArrowBack,
                onClickIcon = onClickArrowBackIcon,
                topAppBarTitle = {
                    TitleTopAppBar(title = "Cancellation Reason")
                })
        }

        Screens.Mapping.ConfirmDetailsScreen.screenRoute + "?${NavigationConstants.LATITUDE}={${NavigationConstants.LATITUDE}}&${NavigationConstants.LONGITUDE}={${NavigationConstants.LONGITUDE}}" -> {
            TopAppBarCreator(
                icon = Icons.Default.ArrowBack,
                onClickIcon = onClickArrowBackIcon,
                topAppBarTitle = {
                    TitleTopAppBar(
                        title = "Confirmation Details")
                })
        }


        Screens.Settings.ChangePasswordScreen.screenRoute -> {
            TopAppBarCreator(
                icon = Icons.Default.ArrowBack,
                onClickIcon = onClickArrowBackIcon,
                topAppBarTitle = {
                    TitleTopAppBar(title = "Change Password")
                })
        }

        Screens.Settings.EditProfileScreen.screenRoute -> {
            TopAppBarCreator(
                icon = Icons.Default.ArrowBack,
                onClickIcon = onClickArrowBackIcon,
                topAppBarTitle = {
                    TitleTopAppBar(title = "Edit Profile")
                })
        }

        Screens.Settings.SettingScreen.screenRoute -> {
            TopAppBarCreator(
                icon = Icons.Default.ArrowBack,
                onClickIcon = onClickArrowBackIcon,
                topAppBarTitle = {
                    TitleTopAppBar(title = "Settings")
                })
        }

        Screens.EmergencyCall.EmergencyCallScreen.screenRoute -> {
            TopAppBarCreator(
                icon = Icons.Default.ArrowBack,
                onClickIcon = onClickArrowBackIcon,
                topAppBarTitle = {
                    TitleTopAppBar(title = "Emergency Call")
                })
        }

        Screens.Messaging.MessagingScreen.screenRoute -> {
            TopAppBarCreator(
                icon = Icons.Default.ArrowBack,
                onClickIcon = onClickArrowBackIcon,
                topAppBarTitle = {
                    TitleTopAppBar(title = "Chats")
                })
        }

        Screens.RideHistory.RideHistoryScreen.screenRoute -> {
            TopAppBarCreator(
                icon = Icons.Default.ArrowBack,
                onClickIcon = onClickArrowBackIcon,
                topAppBarTitle = {
                    TitleTopAppBar(title = "Ride History")
                })
        }

        Screens.RideHistory.RideHistoryDetailsScreen.screenRoute -> {
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
                route = Screens.Settings.EditProfileScreen.screenRoute,
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
                route = Screens.Settings.EditProfileScreen.screenRoute,
                isNavigating = false)
        }
    }
}

