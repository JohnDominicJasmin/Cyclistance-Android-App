package com.example.cyclistance.navigation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.cyclistance.core.utils.constants.NavigationConstants
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components.topAppBar.DefaultTopBar
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components.topAppBar.TitleTopAppBar
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components.topAppBar.TopAppBarCreator
import com.example.cyclistance.navigation.Screens
import com.example.cyclistance.theme.CyclistanceTheme

@Composable
fun TopAppBar(
    onClickArrowBackIcon: () -> Unit = {},
    onClickMenuIcon: () -> Unit = {},
    isNavigating: Boolean,
    route: String?) {

    when (route) {
        Screens.MappingScreen.route -> {
            AnimatedVisibility(
                visible = isNavigating.not(),
                enter = fadeIn(initialAlpha = 0.4f),
                exit = fadeOut(animationSpec = tween(durationMillis = 100))) {

                DefaultTopBar(onClickIcon = onClickMenuIcon)
            }
        }

        "${Screens.CancellationScreen.route}/{${NavigationConstants.CANCELLATION_TYPE}}/{${NavigationConstants.TRANSACTION_ID}}/{${NavigationConstants.CLIENT_ID}}" -> {
            TopAppBarCreator(
                icon = Icons.Default.ArrowBack,
                onClickIcon = onClickArrowBackIcon,
                topAppBarTitle = {
                    TitleTopAppBar(title = "Cancellation Reason")
                })
        }

        Screens.ConfirmDetailsScreen.route + "?${NavigationConstants.LATITUDE}={${NavigationConstants.LATITUDE}}&${NavigationConstants.LONGITUDE}={${NavigationConstants.LONGITUDE}}" -> {
            TopAppBarCreator(
                icon = Icons.Default.ArrowBack,
                onClickIcon = onClickArrowBackIcon,
                topAppBarTitle = {
                    TitleTopAppBar(
                        title = "Confirmation Details")
                })
        }


        Screens.ChangePasswordScreen.route -> {
            TopAppBarCreator(
                icon = Icons.Default.ArrowBack,
                onClickIcon = onClickArrowBackIcon,
                topAppBarTitle = {
                    TitleTopAppBar(title = "Change Password")
                })
        }

        Screens.EditProfileScreen.route -> {
            TopAppBarCreator(
                icon = Icons.Default.ArrowBack,
                onClickIcon = onClickArrowBackIcon,
                topAppBarTitle = {
                    TitleTopAppBar(title = "Edit Profile")
                })
        }

        Screens.SettingScreen.route -> {
            TopAppBarCreator(
                icon = Icons.Default.ArrowBack,
                onClickIcon = onClickArrowBackIcon,
                topAppBarTitle = {
                    TitleTopAppBar(title = "Settings")
                })
        }

        Screens.EmergencyCallScreen.route -> {
            TopAppBarCreator(
                icon = Icons.Default.ArrowBack,
                onClickIcon = onClickArrowBackIcon,
                topAppBarTitle = {
                    TitleTopAppBar(title = "Emergency Call")
                })
        }

        Screens.MessagingScreen.route -> {
            TopAppBarCreator(
                icon = Icons.Default.ArrowBack,
                onClickIcon = onClickArrowBackIcon,
                topAppBarTitle = {
                    TitleTopAppBar(title = "Chats")
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
                route = Screens.EditProfileScreen.route,
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
                route = Screens.EditProfileScreen.route,
                isNavigating = false)
        }
    }
}

