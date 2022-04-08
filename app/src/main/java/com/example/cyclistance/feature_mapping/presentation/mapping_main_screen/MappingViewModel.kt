package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen

import androidx.lifecycle.ViewModel
import com.example.cyclistance.feature_authentication.domain.use_case.AuthenticationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel

import javax.inject.Inject

@HiltViewModel
class MappingViewModel @Inject constructor(
    private val authUseCase: AuthenticationUseCase):ViewModel() {


    fun getEmail():String = authUseCase.getEmailUseCase() ?: "--------"

    fun getName():String = authUseCase.getNameUseCase() ?: "--------"



    fun signOutAccount() = authUseCase.signOutUseCase()



}