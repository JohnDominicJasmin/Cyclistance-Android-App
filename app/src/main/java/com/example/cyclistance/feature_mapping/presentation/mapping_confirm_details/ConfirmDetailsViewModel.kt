package com.example.cyclistance.feature_mapping.presentation.mapping_confirm_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.core.utils.constants.MappingConstants.CONFIRM_DETAILS_VM_STATE_KEY
import com.example.cyclistance.feature_authentication.domain.use_case.AuthenticationUseCase
import com.example.cyclistance.feature_mapping.data.remote.dto.user_dto.ConfirmationDetail
import com.example.cyclistance.feature_mapping.data.remote.dto.user_dto.UserAssistance
import com.example.cyclistance.feature_mapping.domain.exceptions.MappingExceptions
import com.example.cyclistance.feature_mapping.domain.model.ConfirmationDetailsModel
import com.example.cyclistance.feature_mapping.domain.model.LiveLocationWSModel
import com.example.cyclistance.feature_mapping.domain.model.UserItem
import com.example.cyclistance.feature_mapping.domain.use_case.MappingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class ConfirmDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val authUseCase: AuthenticationUseCase,
    private val mappingUseCase: MappingUseCase) : ViewModel() {

    private val _latitude: Float? = savedStateHandle["latitude"]
    private val _longitude: Float? = savedStateHandle["longitude"]
    private val _state: MutableStateFlow<ConfirmDetailsState> =
        MutableStateFlow(savedStateHandle[CONFIRM_DETAILS_VM_STATE_KEY] ?: ConfirmDetailsState())
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
                _eventFlow.emit(value = ConfirmDetailsUiEvent.GetSavedBikeType(bikeType))
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
                _eventFlow.emit(value = ConfirmDetailsUiEvent.GetSavedAddress(address))
                savedStateHandle[CONFIRM_DETAILS_VM_STATE_KEY] = state.value
            }.onFailure { exception ->
                Timber.e(exception.message)
            }
        }
    }

    fun onEvent(event: ConfirmDetailsEvent) {
        when (event) {
            is ConfirmDetailsEvent.ConfirmDetails -> {
                confirmDetails(confirmationDetailsModel = event.confirmDetailsModel)
            }
        }
        savedStateHandle[CONFIRM_DETAILS_VM_STATE_KEY] = state.value
    }


    private fun confirmDetails(confirmationDetailsModel: ConfirmationDetailsModel) {

        viewModelScope.launch(Dispatchers.IO) {
            runCatching {

                _state.update { it.copy(isLoading = true) }

                with(confirmationDetailsModel) {
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
                _eventFlow.emit(value = ConfirmDetailsUiEvent.ConfirmDetailsSuccess)


            }.onFailure { exception ->
                _state.update { it.copy(isLoading = false) }
                exception.handleException()
            }.also {
                savedStateHandle[CONFIRM_DETAILS_VM_STATE_KEY] = state.value
            }
        }
    }

    private suspend fun broadcastUser() {
        _latitude ?: return
        _longitude ?: return

        val latitude = _latitude.toDouble()
        val longitude = _longitude.toDouble()

        runCatching {
            mappingUseCase.broadcastToNearbyCyclists(
                locationModel = LiveLocationWSModel(
                    latitude = latitude,
                    longitude = longitude
                ))
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
            is MappingExceptions.UnexpectedErrorException -> {
                _eventFlow.emit(value = ConfirmDetailsUiEvent.UnexpectedError(this.message!!))
            }

            is MappingExceptions.UserException -> {
                _eventFlow.emit(value = ConfirmDetailsUiEvent.UserError(this.message!!))
            }

            is MappingExceptions.NetworkException -> {
                _eventFlow.emit(value = ConfirmDetailsUiEvent.NoInternetConnection)
            }

            is MappingExceptions.BikeTypeException -> {
                _eventFlow.emit(value = ConfirmDetailsUiEvent.InvalidBikeType(this.message!!))
            }

            is MappingExceptions.DescriptionException -> {
                _eventFlow.emit(value = ConfirmDetailsUiEvent.InvalidDescription(this.message!!))
            }

            is MappingExceptions.AddressException -> {
                _eventFlow.emit(value = ConfirmDetailsUiEvent.InvalidAddress(this.message!!))
            }
        }
        savedStateHandle[CONFIRM_DETAILS_VM_STATE_KEY] = state.value
    }

    private fun getId(): String = authUseCase.getIdUseCase()


}