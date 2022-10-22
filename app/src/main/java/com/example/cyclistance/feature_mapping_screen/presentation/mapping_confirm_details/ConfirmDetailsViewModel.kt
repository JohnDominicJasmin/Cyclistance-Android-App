package com.example.cyclistance.feature_mapping_screen.presentation.mapping_confirm_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.core.utils.constants.MappingConstants.CONFIRM_DETAILS_VM_STATE_KEY
import com.example.cyclistance.feature_authentication.domain.use_case.AuthenticationUseCase
import com.example.cyclistance.feature_mapping_screen.data.remote.dto.ConfirmationDetails
import com.example.cyclistance.feature_mapping_screen.data.remote.dto.Status
import com.example.cyclistance.feature_mapping_screen.data.remote.dto.UserAssistance
import com.example.cyclistance.feature_mapping_screen.domain.exceptions.MappingExceptions
import com.example.cyclistance.feature_mapping_screen.domain.model.User
import com.example.cyclistance.feature_mapping_screen.domain.use_case.MappingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class ConfirmDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val authUseCase: AuthenticationUseCase,
    private val mappingUseCase: MappingUseCase) : ViewModel() {

    private val _state: MutableStateFlow<ConfirmDetailsState> = MutableStateFlow(savedStateHandle[CONFIRM_DETAILS_VM_STATE_KEY] ?: ConfirmDetailsState())
    val state = _state.asStateFlow()

    private val _eventFlow: MutableSharedFlow<ConfirmDetailsUiEvent> = MutableSharedFlow()
    val eventFlow = _eventFlow.asSharedFlow()



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
                savedStateHandle[CONFIRM_DETAILS_VM_STATE_KEY] = state.value
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
                _state.update { it.copy(address = address) }
                savedStateHandle[CONFIRM_DETAILS_VM_STATE_KEY] = state.value
            }.onFailure { exception ->
                Timber.e(exception.message)
            }
        }
    }

    fun onEvent(event: ConfirmDetailsEvent) {
        when (event) {
            is ConfirmDetailsEvent.ConfirmUpdate -> {
                viewModelScope.launch {
                    updateUser(state.value)
                }
            }
            is ConfirmDetailsEvent.DismissNoInternetScreen -> {
                _state.update { it.copy(hasInternet = true) }
            }
            is ConfirmDetailsEvent.EnterAddress -> {
                _state.update { it.copy(address = event.address) }
            }
            is ConfirmDetailsEvent.SelectBikeType -> {
                _state.update { it.copy(bikeType = event.bikeType, bikeTypeErrorMessage = "") }
            }
            is ConfirmDetailsEvent.EnterMessage -> {
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
        savedStateHandle[CONFIRM_DETAILS_VM_STATE_KEY] = state.value
    }


    private suspend fun updateUser(confirmDetailsState: ConfirmDetailsState) {
        runCatching {
            with(confirmDetailsState) {
                _state.update { it.copy(isLoading = true) }
                if (bikeType.isEmpty()){
                    throw MappingExceptions.BikeTypeException()
                }
                if(description.isEmpty()){
                    throw MappingExceptions.DescriptionException()
                }
                mappingUseCase.updateUserUseCase(
                    itemId = getId() ?: return,
                    user = User(
                        userNeededHelp = true,
                        address = address.trim(),
                        userAssistance = UserAssistance(
                            confirmationDetails = ConfirmationDetails(
                                bikeType = bikeType,
                                description = description,
                                message = message.trim()),
                            status = Status(started = true, searching = true)
                        )
                    )).also {

                    mappingUseCase.updateAddressUseCase(address = confirmDetailsState.address)
                    mappingUseCase.updateBikeTypeUseCase(bikeType = confirmDetailsState.bikeType)
                }

            }

        }.onSuccess {
            _state.update { it.copy(isLoading = false) }
            _eventFlow.emit(value = ConfirmDetailsUiEvent.ShowMappingScreen)

        }.onFailure { exception ->
            _state.update { it.copy(isLoading = false) }
            handleException(exception)
        }.also {
            savedStateHandle[CONFIRM_DETAILS_VM_STATE_KEY] = state.value
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
            is MappingExceptions.NetworkExceptions -> {
                _state.update { it.copy(hasInternet = false) }
            }
            is MappingExceptions.BikeTypeException -> {
                _state.update { it.copy(bikeTypeErrorMessage = exception.message!!) }
            }
            is MappingExceptions.DescriptionException -> {
                _state.update { it.copy(descriptionErrorMessage = exception.message!!) }
            }
        }
        savedStateHandle[CONFIRM_DETAILS_VM_STATE_KEY] = state.value
    }

    private fun getId(): String? = authUseCase.getIdUseCase()


}