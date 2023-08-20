package com.example.cyclistance.feature_mapping.presentation.mapping_confirm_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.core.utils.constants.MappingConstants.CONFIRM_DETAILS_VM_STATE_KEY
import com.example.cyclistance.feature_authentication.domain.exceptions.AuthExceptions
import com.example.cyclistance.feature_authentication.domain.use_case.AuthenticationUseCase
import com.example.cyclistance.feature_mapping.domain.exceptions.MappingExceptions
import com.example.cyclistance.feature_mapping.domain.model.ConfirmationDetails
import com.example.cyclistance.feature_mapping.domain.model.remote_models.live_location.LiveLocationSocketModel
import com.example.cyclistance.feature_mapping.domain.model.remote_models.user.ConfirmationDetailModel
import com.example.cyclistance.feature_mapping.domain.model.remote_models.user.UserAssistanceModel
import com.example.cyclistance.feature_mapping.domain.model.remote_models.user.UserItem
import com.example.cyclistance.feature_mapping.domain.use_case.MappingUseCase
import com.example.cyclistance.feature_mapping.presentation.mapping_confirm_details.event.ConfirmDetailsEvent
import com.example.cyclistance.feature_mapping.presentation.mapping_confirm_details.event.ConfirmDetailsVmEvent
import com.example.cyclistance.feature_mapping.presentation.mapping_confirm_details.state.ConfirmDetailsState
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.utils.BottomSheetType
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

    private val _eventFlow: MutableSharedFlow<ConfirmDetailsEvent> = MutableSharedFlow()
    val eventFlow = _eventFlow.asSharedFlow()


    init {

        getAddress()
        getBikeType()
    }


    private fun getBikeType() {
        viewModelScope.launch {
            runCatching {
                mappingUseCase.bikeTypeUseCase().first()
            }.onSuccess { bikeType ->
                _eventFlow.emit(value = ConfirmDetailsEvent.GetSavedBikeType(bikeType))
                savedStateHandle[CONFIRM_DETAILS_VM_STATE_KEY] = state.value
            }.onFailure { exception ->
                Timber.e(exception.message)
            }
        }
    }

    private fun getAddress() {
        viewModelScope.launch {
            runCatching {
                mappingUseCase.addressUseCase().first()
            }.onSuccess { address ->
                _eventFlow.emit(value = ConfirmDetailsEvent.GetSavedAddress(address))
                savedStateHandle[CONFIRM_DETAILS_VM_STATE_KEY] = state.value
            }.onFailure { exception ->
                Timber.e(exception.message)
            }
        }
    }

    fun onEvent(event: ConfirmDetailsVmEvent) {
        when (event) {
            is ConfirmDetailsVmEvent.ConfirmDetails -> {
                confirmDetails(confirmationDetail = event.confirmDetailsModel)
            }
        }
        savedStateHandle[CONFIRM_DETAILS_VM_STATE_KEY] = state.value
    }


    private fun confirmDetails(confirmationDetail: ConfirmationDetails) {

        viewModelScope.launch(Dispatchers.IO) {
            runCatching {

                val confirmedDetails = ConfirmationDetailModel(
                    bikeType = confirmationDetail.bikeType,
                    description = confirmationDetail.description,
                    message = confirmationDetail.message
                )
                _state.update { it.copy(isLoading = true) }

                    mappingUseCase.addressUseCase(address = confirmationDetail.address)
                    mappingUseCase.bikeTypeUseCase(bikeType = confirmationDetail.bikeType)
                    mappingUseCase.bottomSheetTypeUseCase(bottomSheet = BottomSheetType.SearchAssistance.type)

                    mappingUseCase.confirmDetailsUseCase(
                        user = UserItem(
                            id = getId(),
                            address = confirmationDetail.address.trim(),
                            userAssistance = UserAssistanceModel(
                                confirmationDetail = confirmedDetails,
                                needHelp = true
                            )))


            }.onSuccess {
                broadcastUser()
                broadcastRescueTransaction()
                _state.update { it.copy(isLoading = false) }
                _eventFlow.emit(value = ConfirmDetailsEvent.ConfirmDetailsSuccess)


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
            mappingUseCase.nearbyCyclistsUseCase(
                locationModel = LiveLocationSocketModel(
                    latitude = latitude,
                    longitude = longitude
                ))
        }.onSuccess {
            Timber.v("BROADCASTING CONFIRMATION DETAILS: broadcastUser")
        }.onFailure {
            it.handleException()
        }
    }

    private suspend fun broadcastRescueTransaction() {
        runCatching {
            mappingUseCase.broadcastRescueTransactionUseCase()
        }.onSuccess {
            Timber.v("BROADCASTING CONFIRMATION DETAILS: broadcastRescueTransaction")
        }.onFailure {
            it.handleException()
        }
    }

    private suspend fun Throwable.handleException() {
        when (this) {
            is MappingExceptions.UnexpectedErrorException -> {
                _eventFlow.emit(value = ConfirmDetailsEvent.UnexpectedError(this.message!!))
            }

            is AuthExceptions.UserException -> {
                _eventFlow.emit(value = ConfirmDetailsEvent.UserError(this.message!!))
            }

            is MappingExceptions.NetworkException -> {
                _eventFlow.emit(value = ConfirmDetailsEvent.NoInternetConnection)
            }

            is MappingExceptions.BikeTypeException -> {
                _eventFlow.emit(value = ConfirmDetailsEvent.InvalidBikeType(this.message!!))
            }

            is MappingExceptions.DescriptionException -> {
                _eventFlow.emit(value = ConfirmDetailsEvent.InvalidDescription(this.message!!))
            }

            is MappingExceptions.AddressException -> {
                _eventFlow.emit(value = ConfirmDetailsEvent.InvalidAddress(this.message!!))
            }
        }
        savedStateHandle[CONFIRM_DETAILS_VM_STATE_KEY] = state.value
    }

    private fun getId(): String = authUseCase.getIdUseCase()


}