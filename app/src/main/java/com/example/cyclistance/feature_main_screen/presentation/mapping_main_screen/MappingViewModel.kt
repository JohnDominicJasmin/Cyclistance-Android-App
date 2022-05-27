package com.example.cyclistance.feature_main_screen.presentation.mapping_main_screen

import android.location.Address
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.feature_authentication.domain.use_case.AuthenticationUseCase
import com.example.cyclistance.feature_main_screen.data.remote.dto.Location
import com.example.cyclistance.feature_main_screen.data.remote.dto.UserAssistance
import com.example.cyclistance.feature_main_screen.domain.exceptions.MappingExceptions
import com.example.cyclistance.feature_main_screen.domain.model.User
import com.example.cyclistance.feature_main_screen.domain.use_case.MappingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
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


    fun getEmail():String = authUseCase.getEmailUseCase() ?: ""

    private fun getName():String = authUseCase.getNameUseCase() ?: ""

    private fun getId():String = authUseCase.getIdUseCase() ?: ""


    fun signOutAccount() = authUseCase.signOutUseCase()


    fun onEvent(event: MappingEvent){
        when(event){
            is MappingEvent.UploadProfile -> {
                viewModelScope.launch {
                    event.addresses.also{ addresses ->
                        if(addresses.isNotEmpty()){
                            addresses.forEach { postUser(it) }
                            return@also
                        }
                        _eventFlow.emit(MappingUiEvent.ShowToastMessage(
                            message = "Searching for GPS"
                        ))


                    }
                }
            }
        }
    }
    
    private suspend fun postUser(address: Address){

        kotlin.runCatching {
            with(address) {
                    mappingUseCase.createUserUseCase(
                    user = User(
                        address = "$subThoroughfare $thoroughfare., $locality, $subAdminArea",
                        id = getId(),
                        location = Location(
                            lat = latitude.toString(),
                            lng = longitude.toString()),
                        name = getName(),
                        userAssistance = null
                    ))
            }
        }.onSuccess {
            Timber.d("Successfully created user.")
            _eventFlow.emit(MappingUiEvent.ShowConfirmDetailsScreen)
        }.onFailure { exception ->
            when(exception) {
                is MappingExceptions.NoInternetException -> {
                    _eventFlow.emit(MappingUiEvent.ShowNoInternetScreen)
                }
                is MappingExceptions.UnexpectedErrorException -> {
                    _eventFlow.emit(MappingUiEvent.ShowToastMessage(
                        message = exception.message ?: "",
                    ))
                }


            }

        }
    }

}