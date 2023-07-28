package com.example.cyclistance.navigation.components

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.cyclistance.R
import com.example.cyclistance.core.utils.constants.EmergencyCallConstants.SHOULD_OPEN_CONTACT_DIALOG
import com.example.cyclistance.core.utils.constants.MessagingConstants.CHAT_ID
import com.example.cyclistance.core.utils.constants.MessagingConstants.CHAT_NAME
import com.example.cyclistance.core.utils.constants.MessagingConstants.CHAT_PHOTO_URL
import com.example.cyclistance.core.utils.constants.NavigationConstants.CANCELLATION_TYPE
import com.example.cyclistance.core.utils.constants.NavigationConstants.CLIENT_ID
import com.example.cyclistance.core.utils.constants.NavigationConstants.LATITUDE
import com.example.cyclistance.core.utils.constants.NavigationConstants.LONGITUDE
import com.example.cyclistance.core.utils.constants.NavigationConstants.TRANSACTION_ID
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

        Screens.MappingNavigation.MappingScreen.screenRoute -> {
            AnimatedVisibility(
                visible = uiState.isNavigating.not(),
                enter = fadeIn(initialAlpha = 0.4f, animationSpec = tween(durationMillis = 10000)),
                exit = fadeOut(animationSpec = tween(durationMillis = 10000))) {

                DefaultTopBar(onClickIcon = onClickMenuIcon)
            }
        }

        "${Screens.MappingNavigation.CancellationScreen.screenRoute}/{${CANCELLATION_TYPE}}/{${TRANSACTION_ID}}/{${CLIENT_ID}}" -> {
            TopAppBarCreator(
                icon = Icons.Default.ArrowBack,
                onClickIcon = onClickArrowBackIcon,
                topAppBarTitle = {
                    TitleTopAppBar(title = "Cancellation Reason")
                })
        }

        Screens.MappingNavigation.ConfirmDetailsScreen.screenRoute + "?${LATITUDE}={${LATITUDE}}&${LONGITUDE}={${LONGITUDE}}" -> {
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

        Screens.MessagingNavigation.ConversationScreen.screenRoute + "/{${CHAT_ID}}/{${CHAT_PHOTO_URL}}/{${CHAT_NAME}}" -> {
            TopAppBarCreator(
                icon = Icons.Default.Close,
                onClickIcon = onClickArrowBackIcon,
                topAppBarTitle = {

                    Row(
                        modifier = Modifier.wrapContentSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)) {

                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(uiState.conversationPhotoUrl)
                                .crossfade(true)
                                .networkCachePolicy(CachePolicy.ENABLED)
                                .diskCachePolicy(CachePolicy.ENABLED)
                                .memoryCachePolicy(CachePolicy.ENABLED)
                                .build(),
                            alignment = Alignment.Center,
                            contentDescription = "User Profile Image",
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(45.dp),
                            contentScale = ContentScale.Crop,
                            placeholder = painterResource(id = R.drawable.ic_empty_profile_placeholder_large),
                            error = painterResource(id = R.drawable.ic_empty_profile_placeholder_large),
                            fallback = painterResource(id = R.drawable.ic_empty_profile_placeholder_large))

                        TitleTopAppBar(
                            title = uiState.conversationName,
                            modifier = Modifier.padding(start = 5.dp))
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
                route = Screens.SettingsNavigation.EditProfileScreen.screenRoute,
                uiState = NavUiState(isNavigating = false))
        }
    }
}

