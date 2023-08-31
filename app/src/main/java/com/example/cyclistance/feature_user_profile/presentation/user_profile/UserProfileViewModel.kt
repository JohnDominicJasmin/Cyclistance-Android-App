package com.example.cyclistance.feature_user_profile.presentation.user_profile

import androidx.lifecycle.ViewModel
import com.example.cyclistance.feature_user_profile.presentation.user_profile.state.UserProfileState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserProfileViewModel:ViewModel() {

    private val _state = MutableStateFlow(UserProfileState())
    val state = _state.asStateFlow()

}