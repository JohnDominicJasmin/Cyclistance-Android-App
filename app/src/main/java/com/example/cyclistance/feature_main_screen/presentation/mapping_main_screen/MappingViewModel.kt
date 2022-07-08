package com.example.cyclistance.feature_main_screen.presentation.mapping_main_screen

import android.location.Address
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.core.utils.MappingConstants.IMAGE_PLACEHOLDER_URL
import com.example.cyclistance.feature_authentication.domain.use_case.AuthenticationUseCase
import com.example.cyclistance.feature_main_screen.data.remote.dto.Location
import com.example.cyclistance.feature_main_screen.domain.exceptions.MappingExceptions
import com.example.cyclistance.feature_main_screen.domain.model.User
import com.example.cyclistance.feature_main_screen.domain.use_case.MappingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

import javax.inject.Inject

@HiltViewModel
class MappingViewModel @Inject constructor(
    private val authUseCase: AuthenticationUseCase,
    private val mappingUseCase: MappingUseCase):ViewModel() {

    private val _eventFlow: MutableSharedFlow<MappingUiEvent> = MutableSharedFlow()
    val eventFlow: SharedFlow<MappingUiEvent> = _eventFlow.asSharedFlow()

    private val _state: MutableState<MappingState> = mutableStateOf(MappingState())
    val state by _state

    private lateinit var locationUpdatesFlow: Job





    fun getEmail():String = authUseCase.getEmailUseCase() ?: ""
    private fun getId():String? = authUseCase.getIdUseCase()

    private fun getName():String = authUseCase.getNameUseCase() ?: getEmail().apply{
        val index = this.indexOf('@')
        return this.substring(0, index)
    }


    private suspend fun getPhoneNumber(): String = authUseCase.getPhoneNumberUseCase()
    private fun getPhotoUrl(): String{
        return authUseCase.getPhotoUrlUseCase()?.toString()
               ?: IMAGE_PLACEHOLDER_URL
    }





    fun onEvent(event: MappingEvent){
        when(event){

            is MappingEvent.UploadProfile -> {
                viewModelScope.launch {
                    postUser()
                }
            }

            is MappingEvent.SignOut -> {
                viewModelScope.launch {
                    signOutAccount()
                }
            }

            is MappingEvent.SubscribeToLocationUpdates -> {
                subscribeToLocationUpdates()
            }

            is MappingEvent.UnsubscribeToLocationUpdates -> {
                unSubscribeToLocationUpdates()
            }
        }
    }

    private fun subscribeToLocationUpdates(){

        runCatching {
            mappingUseCase.getUserLocationUseCase()
        }.onSuccess { locationFlow ->
            locationUpdatesFlow = locationFlow.onEach { userLocation ->
                _state.value = state.copy(addresses = userLocation.addresses, currentLatLng = userLocation.latLng)
            }.launchIn(viewModelScope)

        }.onFailure {
            Timber.e("Error Location Updates: ${it.message}")
        }
    }

    private fun unSubscribeToLocationUpdates(){
        locationUpdatesFlow.cancel()
    }

    private suspend fun signOutAccount(){
        runCatching {
            authUseCase.signOutUseCase()
        }.onSuccess {
            _eventFlow.emit(value = MappingUiEvent.ShowSignInScreen)
        }.onFailure {
            Timber.e("Error Sign out account: ${it.message}")
        }
    }

    private suspend fun postUser() {

            if (state.addresses.isNotEmpty()) {
                state.addresses.forEach { address ->
                    runCatching {
                         _state.value = state.copy(isLoading = true)
                         createUser(address)
                    }.onSuccess {
                        _state.value = state.copy(isLoading = false, findAssistanceButtonVisible = false)
                        _eventFlow.emit(MappingUiEvent.ShowConfirmDetailsScreen)
                    }.onFailure { exception ->
                        _state.value = state.copy(isLoading = false)
                        when (exception) {
                            is MappingExceptions.NoInternetException -> {
                                _eventFlow.emit(MappingUiEvent.ShowNoInternetScreen)
                            }
                            is MappingExceptions.UnexpectedErrorException -> {
                                _eventFlow.emit(
                                    MappingUiEvent.ShowToastMessage(
                                        message = exception.message ?: "",
                                    ))
                            }
                            is MappingExceptions.PhoneNumberException, is MappingExceptions.NameException -> {
                                _eventFlow.emit(MappingUiEvent.ShowSettingScreen)
                            }

                        }
                    }
                }
            } else {
                _eventFlow.emit(MappingUiEvent.ShowToastMessage(message = "Searching for GPS"))
            }

    }

    private suspend fun createUser(address: Address){
        with(address) {
            mappingUseCase.createUserUseCase(
                user = User(
                    address = "$subThoroughfare $thoroughfare., $locality, $subAdminArea",
                    id = getId() ?: return,
                    location = Location(
                        lat = latitude.toString(),
                        lng = longitude.toString()),
                    name = getName().ifEmpty { throw MappingExceptions.NameException() },
                    profilePictureUrl = getPhotoUrl(),
                    contactNumber = getPhoneNumber()
                ))
        }
    }

    override fun onCleared() {
        unSubscribeToLocationUpdates()
        super.onCleared()
    }
}