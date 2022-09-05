package com.example.cyclistance.feature_main_screen.presentation.mapping_confirm_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.feature_authentication.domain.use_case.AuthenticationUseCase
import com.example.cyclistance.feature_main_screen.data.remote.dto.ConfirmationDetails
import com.example.cyclistance.feature_main_screen.data.remote.dto.Status
import com.example.cyclistance.feature_main_screen.data.remote.dto.UserAssistance
import com.example.cyclistance.feature_main_screen.domain.exceptions.MappingExceptions
import com.example.cyclistance.feature_main_screen.domain.model.User
import com.example.cyclistance.feature_main_screen.domain.use_case.MappingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class ConfirmDetailsViewModel @Inject constructor(
    private val authUseCase: AuthenticationUseCase,
    private val mappingUseCase: MappingUseCase) : ViewModel() {

    private val _state: MutableStateFlow<ConfirmDetailsState> =
        MutableStateFlow(ConfirmDetailsState())
    val state = _state.asStateFlow()

    private val _eventFlow: MutableSharedFlow<ConfirmDetailsUiEvent> = MutableSharedFlow()
    val eventFlow = _eventFlow.asSharedFlow()

    private fun getId(): String? = authUseCase.getIdUseCase()


    init {
        getAddress()
        getBikeType()
    }


    private fun getBikeType() {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                mappingUseCase.getBikeTypeUseCase().first()
            }.onSuccess { bikeType ->
                _state.update { it.copy(bikeType = bikeType ?: "") }
            }.onFailure { exception ->
                Timber.e(exception.message)
            }
        }
    }

    private fun getAddress() {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                mappingUseCase.getAddressUseCase().first()
            }.onSuccess { address ->
                _state.update { it.copy(address = address ?: "") }
            }.onFailure { exception ->
                Timber.e(exception.message)
            }
        }
    }

    fun onEvent(event: ConfirmDetailsEvent) {
        when (event) {
            is ConfirmDetailsEvent.Save -> {
                viewModelScope.launch {
                    updateUser(state.value)
                }
            }
            is ConfirmDetailsEvent.EnteredAddress -> {
                _state.update { it.copy(address = event.address) }
            }
            is ConfirmDetailsEvent.SelectBikeType -> {
                _state.update { it.copy(bikeType = event.bikeType, bikeTypeErrorMessage = "") }
            }
            is ConfirmDetailsEvent.EnteredMessage -> {
                _state.update { it.copy(message = event.message) }
            }
            is ConfirmDetailsEvent.SelectDescription -> {
                _state.update {
                    it.copy(
                        description = event.description,
                        descriptionErrorMessage = "")
                }
            }

        }
    }


    private suspend fun updateUser(confirmDetailsState: ConfirmDetailsState) {
        runCatching {
            _state.update { it.copy(isLoading = true) }
            mappingUseCase.updateUserUseCase(
                itemId = getId() ?: return,
                user = User(
                    userNeededHelp = true,
                    address = confirmDetailsState.address,
                    userAssistance = UserAssistance(
                        confirmationDetails = ConfirmationDetails(
                            bikeType = confirmDetailsState.bikeType,
                            description = confirmDetailsState.description,
                            message = confirmDetailsState.message.text),
                        status = Status(started = true)
                    )
                )).also {

                mappingUseCase.updateAddressUseCase(address = confirmDetailsState.address)
                mappingUseCase.updateBikeTypeUseCase(bikeType = confirmDetailsState.bikeType)
            }


        }.onSuccess {
            _state.update { it.copy(isLoading = false) }
            _eventFlow.emit(value = ConfirmDetailsUiEvent.ShowMappingScreen)
            //todo: show mapping screen and bottomSheet
        }.onFailure { exception ->
            _state.update { it.copy(isLoading = false) }
            handleException(exception)
        }
    }

    private suspend fun handleException(exception: Throwable) {
        when (exception) {
            is MappingExceptions.UnexpectedErrorException -> {
                _eventFlow.emit(
                    ConfirmDetailsUiEvent.ShowToastMessage(
                        message = exception.message ?: "",
                    ))
            }
            is MappingExceptions.NoInternetException -> {
                _eventFlow.emit(ConfirmDetailsUiEvent.ShowNoInternetScreen)
            }
            is MappingExceptions.BikeTypeException -> {
                _state.update { it.copy(bikeTypeErrorMessage = exception.message!!) }
            }
            is MappingExceptions.DescriptionException -> {
                _state.update { it.copy(descriptionErrorMessage = exception.message!!) }
            }


        }
    }
}