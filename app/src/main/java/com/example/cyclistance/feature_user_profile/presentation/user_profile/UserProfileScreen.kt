package com.example.cyclistance.feature_user_profile.presentation.user_profile

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.cyclistance.feature_user_profile.presentation.user_profile.components.UserProfileContent

@Composable
fun UserProfileScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    viewModel: UserProfileViewModel = viewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    UserProfileContent(state = state, modifier = Modifier.padding(paddingValues))
}