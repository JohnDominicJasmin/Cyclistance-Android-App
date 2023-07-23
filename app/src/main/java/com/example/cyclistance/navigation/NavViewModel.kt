package com.example.cyclistance.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.core.utils.constants.NavConstants.NAV_VM_STATE_KEY
import com.example.cyclistance.feature_authentication.domain.use_case.AuthenticationUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.MessagingUseCase
import com.example.cyclistance.feature_on_boarding.domain.use_case.IntroSliderUseCase
import com.example.cyclistance.navigation.event.NavEvent
import com.example.cyclistance.navigation.event.NavVmEvent
import com.example.cyclistance.navigation.state.NavState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class NavViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val introSliderUseCase: IntroSliderUseCase,
    private val authUseCase: AuthenticationUseCase,
    private val messagingUseCase: MessagingUseCase
) : ViewModel() {


    private val _state = MutableStateFlow(savedStateHandle[NAV_VM_STATE_KEY] ?: NavState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<NavEvent>()
    val event = _event.asSharedFlow()

    init {
        getStartingDestination()
    }

    private fun getStartingDestination() {


        introSliderUseCase.readIntroSliderUseCase().catch {
            Timber.e("IntroSlider DataStore Reading Failed: ${it.localizedMessage}")
        }.onEach { userCompletedWalkThrough ->
            if (!userCompletedWalkThrough) {
                _state.update { it.copy(navigationStartingDestination = Screens.OnBoarding.ROUTE) }
                return@onEach
            }

            if (isUserSignedIn()) {
                _state.update { it.copy(navigationStartingDestination = Screens.Mapping.ROUTE) }
                return@onEach
            }

            _state.update { it.copy(navigationStartingDestination = Screens.Authentication.ROUTE) }
        }.launchIn(viewModelScope).invokeOnCompletion {
            savedStateHandle[NAV_VM_STATE_KEY] = state.value
        }

    }

    fun onEvent(event: NavVmEvent) {
        when (event) {
            is NavVmEvent.DeleteMessagingToken -> deleteMessagingToken()
        }
    }

    private fun deleteMessagingToken() {
        viewModelScope.launch {
            runCatching {
                messagingUseCase.deleteTokenUseCase()
            }.onSuccess {
                _event.emit(value = NavEvent.DeleteMessagingTokenSuccess)
            }.onFailure {
                _event.emit(
                    value = NavEvent.DeleteMessagingTokenFailure(
                        it.message ?: "Unknown Error"))
            }
        }
    }

    private fun isUserSignedIn(): Boolean {
        return (authUseCase.isSignedInWithProviderUseCase() == true || authUseCase.isEmailVerifiedUseCase() == true) &&
               authUseCase.hasAccountSignedInUseCase()
    }


}