package com.example.cyclistance.navigation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.cyclistance.core.utils.constants.EmergencyCallConstants.SHOULD_OPEN_CONTACT_DIALOG
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
    onClickSearchMessagingUser: () -> Unit = {},
    isNavigating: Boolean,
    route: String?) {

    when (route) {

        Screens.MappingNavigation.MappingScreen.screenRoute -> {
            AnimatedVisibility(
                visible = isNavigating.not(),
                enter = fadeIn(initialAlpha = 0.4f, animationSpec = tween(durationMillis = 10000)),
                exit = fadeOut(animationSpec = tween(durationMillis = 10000))) {

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

        Screens.EmergencyCallNavigation.EmergencyCallScreen.screenRoute + "?${SHOULD_OPEN_CONTACT_DIALOG}={${SHOULD_OPEN_CONTACT_DIALOG}}" -> {

            TopAppBarCreator(
                icon = Icons.Default.ArrowBack,
                onClickIcon = onClickArrowBackIcon,
                topAppBarTitle = {
                    TitleTopAppBar(title = "Emergency Call")
                })
        }

        Screens.MessagingNavigation.ChatScreen.screenRoute -> {

            TopAppBarCreator(
                icon = Icons.Default.ArrowBack,
                onClickIcon = onClickArrowBackIcon,
                topAppBarTitle = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween) {

                        TitleTopAppBar(title = "Chats")
                        IconButton(onClick = onClickSearchMessagingUser) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = MaterialTheme.colors.onBackground,
                            )
                        }
                    }
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

