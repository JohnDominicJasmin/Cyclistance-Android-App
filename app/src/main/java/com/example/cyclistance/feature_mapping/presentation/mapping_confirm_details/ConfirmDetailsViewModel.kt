package com.example.cyclistance.feature_mapping.presentation.mapping_confirm_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.core.utils.constants.MappingConstants.CONFIRM_DETAILS_VM_STATE_KEY
import com.example.cyclistance.feature_authentication.domain.use_case.AuthenticationUseCase
import com.example.cyclistance.feature_mapping.data.remote.dto.user_dto.ConfirmationDetail
import com.example.cyclistance.feature_mapping.data.remote.dto.user_dto.UserAssistance
import com.example.cyclistance.feature_mapping.domain.exceptions.MappingExceptions
import com.example.cyclistance.feature_mapping.domain.model.UserItem
import com.example.cyclistance.feature_mapping.domain.use_case.MappingUseCase
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
                _state.update { it.copy(bikeType = bikeType) }
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
            is ConfirmDetailsEvent.ConfirmDetails -> {
                updateUser()
            }
            is ConfirmDetailsEvent.DismissNoInternetDialog -> {
                _state.update { it.copy(hasInternet = true) }
            }
            is ConfirmDetailsEvent.EnterAddress -> {
                _state.update { it.copy(address = event.address) }
            }
            is ConfirmDetailsEvent.SelectBikeType -> {
                _state.update { it.copy(bikeType = event.bikeType) }
            }
            is ConfirmDetailsEvent.ClearBikeTypeErrorMessage -> {
                _state.update { it.copy(bikeTypeErrorMessage = "") }
            }
            is ConfirmDetailsEvent.ClearDescriptionErrorMessage -> {
                _state.update { it.copy(descriptionErrorMessage = "") }
            }
            is ConfirmDetailsEvent.EnterMessage -> {
                _state.update { it.copy(message = event.message) }
            }
            is ConfirmDetailsEvent.SelectDescription -> {
                _state.update { it.copy(description = event.description) }
            }
        }
        savedStateHandle[CONFIRM_DETAILS_VM_STATE_KEY] = state.value
    }


    private fun updateUser() {

        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                with(state.value) {
                    _state.update { it.copy(isLoading = true) }

                    mappingUseCase.confirmDetailsUseCase(
                        user = UserItem(
                            id = getId(),
                            address = address.trim(),
                            userAssistance = UserAssistance(
                                confirmationDetail = ConfirmationDetail(
                                    bikeType = bikeType,
                                    description = description,
                                    message = message.trim()),
                                needHelp = true
                            ))).also {

                        mappingUseCase.setAddressUseCase(address = address)
                        mappingUseCase.setBikeTypeUseCase(bikeType = bikeType)
                    }
                }


            }.onSuccess {
                broadcastUser()
                broadcastRescueTransaction()
                _state.update { it.copy(isLoading = false) }
                _eventFlow.emit(value = ConfirmDetailsUiEvent.ShowMappingScreen)


            }.onFailure { exception ->
                _state.update { it.copy(isLoading = false) }
                exception.handleException()
            }.also {
                savedStateHandle[CONFIRM_DETAILS_VM_STATE_KEY] = state.value
            }
        }
    }

    private suspend fun broadcastUser() {
        runCatching {
            mappingUseCase.broadcastUserUseCase()
        }.onFailure {
            it.handleException()
        }
    }

    private suspend fun broadcastRescueTransaction() {
        runCatching {
            mappingUseCase.broadcastRescueTransactionUseCase()
        }.onFailure {
            it.handleException()
        }
    }

    private suspend fun Throwable.handleException() {
        when (this) {
            is MappingExceptions.UnexpectedErrorException, is MappingExceptions.UserException -> {
                _eventFlow.emit(
                    ConfirmDetailsUiEvent.ShowToastMessage(
                        message = this.message ?: "",
                    ))
            }
            is MappingExceptions.NetworkException -> {
                _state.update { it.copy(hasInternet = false) }
            }
            is MappingExceptions.BikeTypeException -> {
                _state.update { it.copy(bikeTypeErrorMessage = this.message!!) }
            }
            is MappingExceptions.DescriptionException -> {
                _state.update { it.copy(descriptionErrorMessage = this.message!!) }
            }
        }
        savedStateHandle[CONFIRM_DETAILS_VM_STATE_KEY] = state.value
    }

    private fun getId(): String = authUseCase.getIdUseCase()


}