package com.example.cyclistance.feature_mapping.presentation.mapping_cancellation_reason

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.core.utils.constants.MappingConstants.CANCELLATION_VM_STATE_KEY
import com.example.cyclistance.feature_authentication.domain.use_case.AuthenticationUseCase
import com.example.cyclistance.feature_mapping.data.remote.dto.rescue_transaction.Cancellation
import com.example.cyclistance.feature_mapping.data.remote.dto.rescue_transaction.CancellationReason
import com.example.cyclistance.feature_mapping.data.remote.dto.rescue_transaction.Route
import com.example.cyclistance.feature_mapping.data.remote.dto.rescue_transaction.Status
import com.example.cyclistance.feature_mapping.domain.exceptions.MappingExceptions
import com.example.cyclistance.feature_mapping.domain.model.RescueTransactionItem
import com.example.cyclistance.feature_mapping.domain.use_case.MappingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CancellationReasonViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val mappingUseCase: MappingUseCase,
    private val authUseCase: AuthenticationUseCase,
): ViewModel() {


    private val _transactionId: String = savedStateHandle["transactionId"] ?: ""
    private val _clientId: String = savedStateHandle["clientId"] ?: ""

    private val _state = MutableStateFlow(savedStateHandle[CANCELLATION_VM_STATE_KEY] ?: CancellationReasonState())
    val state = _state.asStateFlow()
    
    private val _eventFlow: MutableSharedFlow<CancellationReasonUiEvent> = MutableSharedFlow()
    val eventFlow = _eventFlow.asSharedFlow()


    fun onEvent(event: CancellationReasonEvent) {
        when (event) {
            is CancellationReasonEvent.DismissNoInternetDialog -> {
                _state.update { it.copy(hasInternet = true) }
            }
            is CancellationReasonEvent.SelectReason -> {
                _state.update { it.copy(selectedReason = event.reason) }
            }
            is CancellationReasonEvent.ClearReasonErrorMessage -> {
                _state.update { it.copy(reasonErrorMessage = "") }
            }
            is CancellationReasonEvent.ConfirmCancellationReason -> {
                confirmCancellationReason()
            }
            is CancellationReasonEvent.EnterMessage -> {
                _state.update { it.copy(message = event.message) }
            }
        }
        savedStateHandle[CANCELLATION_VM_STATE_KEY] = state.value
    }

    private fun confirmCancellationReason() {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                startLoading()
                mappingUseCase.confirmCancellationUseCase(
                    rescueTransaction = RescueTransactionItem(
                        id = _transactionId,
                        cancellation = Cancellation(
                            cancellationReason = CancellationReason(
                                reason = state.value.selectedReason,
                                message = state.value.message
                            ),
                            idCancelledBy = getId(),
                            nameCancelledBy = getName(),
                            rescueCancelled = true,
                        ),
                        status = Status(),
                        route = Route()
                    )
                )

            }.onSuccess {
                broadcastRescueTransaction()
                delay(500)
                finishLoading()
                _eventFlow.emit(value = CancellationReasonUiEvent.ShowMappingScreen)
            }.onFailure { exception ->
                finishLoading()
                exception.handleException()
            }.also {
                savedStateHandle[CANCELLATION_VM_STATE_KEY] = state.value
            }
        }
    }

    private fun startLoading() {
        _state.update { it.copy(isLoading = true) }
    }

    private fun finishLoading() {
        _state.update { it.copy(isLoading = false) }
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
            is MappingExceptions.UnexpectedErrorException, is MappingExceptions.UserException, is MappingExceptions.RescueTransactionException -> {
                _eventFlow.emit(
                    CancellationReasonUiEvent.ShowToastMessage(
                        message = this.message!!
                    ))
            }
            is MappingExceptions.NetworkException -> {
                _state.update { it.copy(hasInternet = false) }
            }

            is MappingExceptions.RescueTransactionReasonException -> {
                _state.update { it.copy(reasonErrorMessage = this.message!!) }
            }

        }
        savedStateHandle[CANCELLATION_VM_STATE_KEY] = state.value
    }
    private fun getId() = authUseCase.getIdUseCase()
    private fun getName() = authUseCase.getNameUseCase()

}