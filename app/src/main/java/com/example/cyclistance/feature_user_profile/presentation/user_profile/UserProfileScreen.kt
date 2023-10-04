package com.example.cyclistance.feature_user_profile.presentation.user_profile

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.cyclistance.feature_user_profile.presentation.user_profile.components.UserProfileContent
import com.example.cyclistance.feature_user_profile.presentation.user_profile.event.UserProfileUiEvent
import com.example.cyclistance.feature_user_profile.presentation.user_profile.event.UserProfileVmEvent
import com.example.cyclistance.navigation.Screens
import com.example.cyclistance.navigation.nav_graph.navigateScreen

@Composable
fun UserProfileScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    userId: String,
    viewModel: UserProfileViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    val navigateToEditProfile = remember {
        {
            navController.navigate(Screens.UserProfileNavigation.EditProfile.screenRoute)
        }
    }


    val onClickMessagingProfile = remember {
        {
            navController.navigateScreen(
                route = Screens.MessagingNavigation.Conversation.passArgument(
                    receiverMessageId = userId,
                )
            )

        }
    }











    LaunchedEffect(key1 = userId) {
        viewModel.onEvent(event = UserProfileVmEvent.LoadProfile(userId = userId))
    }



    UserProfileContent(
        state = state,
        modifier = Modifier.padding(paddingValues),
        event = { event ->
            when (event) {
                UserProfileUiEvent.OnClickEditProfile -> navigateToEditProfile()
                is UserProfileUiEvent.OnClickMessageProfile -> onClickMessagingProfile()
            }
        }
    )

}