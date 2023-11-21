package com.myapp.cyclistance.navigation.components

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
import com.myapp.cyclistance.navigation.Screens
import com.myapp.cyclistance.navigation.state.NavUiState
import com.myapp.cyclistance.theme.CyclistanceTheme
import com.myapp.cyclistance.top_bars.DefaultTopBar
import com.myapp.cyclistance.top_bars.TitleTopAppBar
import com.myapp.cyclistance.top_bars.TopAppBarCreator

@Composable
fun TopAppBar(
    onClickArrowBackIcon: () -> Unit = {},
    onClickMenuIcon: () -> Unit = {},
    uiState: NavUiState,
    route: String?) {

    when (route) {

        Screens.MappingNavigation.Mapping.screenRoute -> {
            AnimatedVisibility(
                visible = uiState.isNavigating.not(),
                enter = fadeIn(initialAlpha = 0.4f, animationSpec = tween(durationMillis = 1000)),
                exit = fadeOut(animationSpec = tween(durationMillis = 1000))) {

                DefaultTopBar(onClickIcon = onClickMenuIcon)
            }
        }

        Screens.MappingNavigation.Cancellation.screenRoute -> {
            TopAppBarCreator(
                icon = Icons.Filled.ArrowBack,
                onClickIcon = onClickArrowBackIcon,
                topAppBarTitle = {
                    TitleTopAppBar(title = "Cancellation Reason")
                })
        }

        Screens.MappingNavigation.ConfirmDetails.screenRoute -> {
            TopAppBarCreator(
                icon = Icons.Filled.ArrowBack,
                onClickIcon = onClickArrowBackIcon,
                topAppBarTitle = {
                    TitleTopAppBar(
                        title = "Confirmation Details")
                })
        }


        Screens.AuthenticationNavigation.ResetPassword.screenRoute -> {
            TopAppBarCreator(
                icon = Icons.Filled.ArrowBack,
                onClickIcon = onClickArrowBackIcon,
                topAppBarTitle = {
                    TitleTopAppBar(title = "Reset Password")
                })
        }

        Screens.UserProfileNavigation.EditProfile.screenRoute -> {
            TopAppBarCreator(
                icon = Icons.Filled.ArrowBack,
                onClickIcon = onClickArrowBackIcon,
                topAppBarTitle = {
                    TitleTopAppBar(title = "Edit Profile")
                })
        }

        Screens.SettingsNavigation.Setting.screenRoute -> {
            TopAppBarCreator(
                icon = Icons.Filled.ArrowBack,
                onClickIcon = onClickArrowBackIcon,
                topAppBarTitle = {
                    TitleTopAppBar(title = "Settings")
                })
        }

        Screens.EmergencyCallNavigation.EmergencyCall.screenRoute -> {

            TopAppBarCreator(
                icon = Icons.Filled.ArrowBack,
                onClickIcon = onClickArrowBackIcon,
                topAppBarTitle = {
                    TitleTopAppBar(title = "Emergency Call")
                })
        }

        Screens.EmergencyCallNavigation.AddEditEmergencyContact.screenRoute -> {
            TopAppBarCreator(
                icon = Icons.Default.Close,
                onClickIcon = onClickArrowBackIcon,
                topAppBarTitle = {
                    TitleTopAppBar(title = "Manage Emergency Contacts")
                })
        }


        Screens.MessagingNavigation.Conversation.screenRoute -> {

            TopAppBarCreator(
                icon = Icons.Filled.ArrowBack,
                onClickIcon = onClickArrowBackIcon,
                topAppBarTitle = {
                    TitleTopAppBar(title = "Contact your rescuer")
                })
        }

        Screens.RescueRecordNavigation.RideHistory.screenRoute -> {
            TopAppBarCreator(
                icon = Icons.Filled.ArrowBack,
                onClickIcon = onClickArrowBackIcon,
                topAppBarTitle = {
                    TitleTopAppBar(title = "Ride History")
                })
        }

        Screens.RescueRecordNavigation.RideHistoryDetails.screenRoute -> {
            TopAppBarCreator(
                icon = Icons.Default.Close,
                onClickIcon = onClickArrowBackIcon,
                topAppBarTitle = {
                    TitleTopAppBar(title = "Rescue Details")
                })
        }

        Screens.UserProfileNavigation.UserProfile.screenRoute -> {
            TopAppBarCreator(
                icon = Icons.Default.Close,
                onClickIcon = onClickArrowBackIcon,
                topAppBarTitle = {
                    TitleTopAppBar(title = "User Profile")
                })
        }

       Screens.RescueRecordNavigation.RescueDetails.screenRoute -> {
            TopAppBarCreator(
                icon = Icons.Default.Close,
                onClickIcon = onClickArrowBackIcon,
                topAppBarTitle = {
                    TitleTopAppBar(title = "Rescue Details")
                })

        }

        Screens.RescueRecordNavigation.RescueResults.screenRoute -> {
            TopAppBarCreator(
                icon = Icons.Default.Close,
                onClickIcon = onClickArrowBackIcon,
                topAppBarTitle = {
                    TitleTopAppBar(title = "Rescue Result")
                })

        }

        Screens.ReportAccountNavigation.ReportAccount.screenRoute -> {
            TopAppBarCreator(
                icon = Icons.Default.Close,
                onClickIcon = onClickArrowBackIcon,
                topAppBarTitle = {
                    TitleTopAppBar(title = "Report Account")
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
                route = Screens.UserProfileNavigation.EditProfile.screenRoute,
                uiState = NavUiState(isNavigating = false))
        }
    }
}


@Preview
@Composable
fun PreviewTopAppBarLight() {
    CyclistanceTheme(false) {
        Surface(color = MaterialTheme.colors.background) {
            TopAppBar(
                route = Screens.UserProfileNavigation.EditProfile.screenRoute,
                uiState = NavUiState(isNavigating = false))
        }
    }
}

