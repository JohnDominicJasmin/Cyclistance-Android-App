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
import com.example.cyclistance.feature_mapping.presentation.mapping_cancellation_reason.event.CancellationReasonEvent
import com.example.cyclistance.feature_mapping.presentation.mapping_cancellation_reason.event.CancellationReasonVmEvent
import com.example.cyclistance.feature_mapping.presentation.mapping_cancellation_reason.state.CancellationReasonState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CancellationReasonViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val mappingUseCase: MappingUseCase,
    private val authUseCase: AuthenticationUseCase,
): ViewModel() {


    private val _transactionId: String = savedStateHandle["transactionId"] ?: ""

    private val _state = MutableStateFlow(savedStateHandle[CANCELLATION_VM_STATE_KEY] ?: CancellationReasonState())
    val state = _state.asStateFlow()
    
    private val _eventFlow: MutableSharedFlow<CancellationReasonEvent> = MutableSharedFlow()
    val eventFlow = _eventFlow.asSharedFlow()


    fun onEvent(event: CancellationReasonVmEvent) {
        when (event) {

            is CancellationReasonVmEvent.ConfirmCancellationReason -> {

                confirmCancellationReason(
                    reason = event.reason,
                    message = event.message)

            }

        }
        savedStateHandle[CANCELLATION_VM_STATE_KEY] = state.value
    }

    private fun confirmCancellationReason(reason: String, message: String) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                startLoading()
                mappingUseCase.confirmCancellationUseCase(
                    rescueTransaction = RescueTransactionItem(
                        id = _transactionId,
                        cancellation = Cancellation(
                            cancellationReason = CancellationReason(
                                reason = reason,
                                message = message
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
                _eventFlow.emit(value = CancellationReasonEvent.ConfirmCancellationReasonSuccess)
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

            is MappingExceptions.UnexpectedErrorException -> {
                _eventFlow.emit(value = CancellationReasonEvent.UnexpectedError(this.message!!))
            }

            is MappingExceptions.UserException -> {
                _eventFlow.emit(value = CancellationReasonEvent.UserFailed(this.message!!))
            }

            is MappingExceptions.RescueTransactionNotFoundException -> {
                _eventFlow.emit(value = CancellationReasonEvent.RescueTransactionFailed(this.message!!))
            }

            is MappingExceptions.NetworkException -> {
                _eventFlow.emit(value = CancellationReasonEvent.NoInternetConnection)
            }

            is MappingExceptions.RescueTransactionReasonException -> {
                _eventFlow.emit(value = CancellationReasonEvent.InvalidCancellationReason(this.message!!))
            }

        }
        savedStateHandle[CANCELLATION_VM_STATE_KEY] = state.value
    }
    private fun getId() = authUseCase.getIdUseCase()
    private suspend fun getName() = authUseCase.getNameUseCase()

}