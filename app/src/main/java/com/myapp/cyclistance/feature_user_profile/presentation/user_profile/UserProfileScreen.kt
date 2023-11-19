package com.myapp.cyclistance.feature_user_profile.presentation.user_profile

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
import com.myapp.cyclistance.feature_user_profile.presentation.user_profile.components.UserProfileContent
import com.myapp.cyclistance.feature_user_profile.presentation.user_profile.event.UserProfileUiEvent
import com.myapp.cyclistance.feature_user_profile.presentation.user_profile.event.UserProfileVmEvent
import com.myapp.cyclistance.navigation.Screens

@Composable
fun UserProfileScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    userId: String,
    viewModel: UserProfileViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    val navigateToEditProfile = remember {{
        navController.navigate(Screens.UserProfileNavigation.EditProfile.screenRoute)
    }}


    val navigateToUserProfile = remember{{
        navController.navigate(Screens.RescueRecordNavigation.RideHistory.passArgument(
          rideHistoryUid = userId
        ))
    }}


    LaunchedEffect(key1 = userId) {
        viewModel.onEvent(event = UserProfileVmEvent.LoadProfile(userId = userId))
    }



    UserProfileContent(
        state = state,
        modifier = Modifier.padding(paddingValues),
        event = { event ->
            when (event) {
                UserProfileUiEvent.OnClickEditProfile -> navigateToEditProfile()
                UserProfileUiEvent.OnClickRideHistory -> navigateToUserProfile()
            }
        }
    )

}