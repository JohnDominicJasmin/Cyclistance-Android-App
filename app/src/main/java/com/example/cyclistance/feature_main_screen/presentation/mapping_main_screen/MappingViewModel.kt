package com.example.cyclistance.feature_main_screen.presentation.mapping_main_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.feature_authentication.domain.use_case.AuthenticationUseCase
import com.example.cyclistance.feature_main_screen.domain.use_case.MappingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

import javax.inject.Inject

@HiltViewModel
class MappingViewModel @Inject constructor(
    private val authUseCase: AuthenticationUseCase,
    private val mappingUseCase: MappingUseCase):ViewModel() {


    fun getEmail():String = authUseCase.getEmailUseCase() ?: "--------"

    fun getName():String = authUseCase.getNameUseCase() ?: "--------"



    fun signOutAccount() = authUseCase.signOutUseCase()



}