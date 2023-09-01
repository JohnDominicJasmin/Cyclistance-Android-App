package com.example.cyclistance.navigation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cyclistance.core.utils.constants.EmergencyCallConstants.SHOULD_OPEN_CONTACT_DIALOG
import com.example.cyclistance.core.utils.constants.NavigationConstants.CANCELLATION_TYPE
import com.example.cyclistance.core.utils.constants.NavigationConstants.CLIENT_ID
import com.example.cyclistance.core.utils.constants.NavigationConstants.LATITUDE
import com.example.cyclistance.core.utils.constants.NavigationConstants.LONGITUDE
import com.example.cyclistance.core.utils.constants.NavigationConstants.TRANSACTION_ID
import com.example.cyclistance.core.utils.constants.UserProfileConstants.USER_ID
import com.example.cyclistance.feature_messaging.presentation.common.MessageUserImage
import com.example.cyclistance.navigation.Screens
import com.example.cyclistance.navigation.state.NavUiState
import com.example.cyclistance.theme.CyclistanceTheme
import com.example.cyclistance.top_bars.DefaultTopBar
import com.example.cyclistance.top_bars.TitleTopAppBar
import com.example.cyclistance.top_bars.TopAppBarCreator

@Composable
fun TopAppBar(
    onClickArrowBackIcon: () -> Unit = {},
    onClickMenuIcon: () -> Unit = {},
    onClickSearchMessagingUser: () -> Unit = {},
    uiState: NavUiState,
    route: String?) {

    when (route) {

        Screens.MappingNavigation.Mapping.screenRoute -> {
            AnimatedVisibility(
                visible = uiState.isNavigating.not(),
                enter = fadeIn(initialAlpha = 0.4f, animationSpec = tween(durationMillis = 10000)),
                exit = fadeOut(animationSpec = tween(durationMillis = 10000))) {

                DefaultTopBar(onClickIcon = onClickMenuIcon)
            }
        }

        "${Screens.MappingNavigation.Cancellation.screenRoute}/{${CANCELLATION_TYPE}}/{${TRANSACTION_ID}}/{${CLIENT_ID}}" -> {
            TopAppBarCreator(
                icon = Icons.Default.ArrowBack,
                onClickIcon = onClickArrowBackIcon,
                topAppBarTitle = {
                    TitleTopAppBar(title = "Cancellation Reason")
                })
        }

        Screens.MappingNavigation.ConfirmDetails.screenRoute + "?${LATITUDE}={${LATITUDE}}&${LONGITUDE}={${LONGITUDE}}" -> {
            TopAppBarCreator(
                icon = Icons.Default.ArrowBack,
                onClickIcon = onClickArrowBackIcon,
                topAppBarTitle = {
                    TitleTopAppBar(
                        title = "Confirmation Details")
                })
        }


        Screens.AuthenticationNavigation.ResetPassword.screenRoute -> {
            TopAppBarCreator(
                icon = Icons.Default.ArrowBack,
                onClickIcon = onClickArrowBackIcon,
                topAppBarTitle = {
                    TitleTopAppBar(title = "Reset Password")
                })
        }

        Screens.UserProfileNavigation.EditProfile.screenRoute -> {
            TopAppBarCreator(
                icon = Icons.Default.ArrowBack,
                onClickIcon = onClickArrowBackIcon,
                topAppBarTitle = {
                    TitleTopAppBar(title = "Edit Profile")
                })
        }

        Screens.SettingsNavigation.Setting.screenRoute -> {
            TopAppBarCreator(
                icon = Icons.Default.ArrowBack,
                onClickIcon = onClickArrowBackIcon,
                topAppBarTitle = {
                    TitleTopAppBar(title = "Settings")
                })
        }

        Screens.EmergencyCallNavigation.EmergencyCall.screenRoute + "?${SHOULD_OPEN_CONTACT_DIALOG}={${SHOULD_OPEN_CONTACT_DIALOG}}" -> {

            TopAppBarCreator(
                icon = Icons.Default.ArrowBack,
                onClickIcon = onClickArrowBackIcon,
                topAppBarTitle = {
                    TitleTopAppBar(title = "Emergency Call")
                })
        }

        Screens.MessagingNavigation.Chat.screenRoute -> {

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


        Screens.MessagingNavigation.Conversation.screenRoute -> {
            TopAppBarCreator(
                icon = Icons.Default.Close,
                onClickIcon = onClickArrowBackIcon,
                topAppBarTitle = {

                    Row(
                        modifier = Modifier.wrapContentSize().padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)) {

                        MessageUserImage(
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(48.dp),
                            isOnline = if(!uiState.internetAvailable) null else uiState.conversationAvailability,
                            photoUrl = uiState.conversationPhotoUrl)

                        TitleTopAppBar(
                            title = uiState.conversationName,
                            modifier = Modifier
                                .padding(start = 5.dp)
                                .animateContentSize(animationSpec = spring(stiffness = Spring.StiffnessLow)))

                    }
                })
        }

        Screens.RideHistoryNavigation.RideHistory.screenRoute -> {
            TopAppBarCreator(
                icon = Icons.Default.ArrowBack,
                onClickIcon = onClickArrowBackIcon,
                topAppBarTitle = {
                    TitleTopAppBar(title = "Ride History")
                })
        }

        Screens.RideHistoryNavigation.RideHistoryDetails.screenRoute -> {
            TopAppBarCreator(
                icon = Icons.Default.Close,
                onClickIcon = onClickArrowBackIcon,
                topAppBarTitle = {
                    TitleTopAppBar(title = "Rescue Details")
                })
        }

        Screens.UserProfileNavigation.UserProfile.screenRoute + "/{${USER_ID}}" -> {
            TopAppBarCreator(
                icon = Icons.Default.Close,
                onClickIcon = onClickArrowBackIcon,
                topAppBarTitle = {
                    TitleTopAppBar(title = "User Profile")
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

