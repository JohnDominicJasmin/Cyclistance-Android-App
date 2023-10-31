package com.example.cyclistance.feature_rescue_record.presentation.rescue_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.core.utils.constants.RescueRecordConstants
import com.example.cyclistance.core.utils.constants.RescueRecordConstants.RESCUE_DETAILS_VM_STATE_KEY
import com.example.cyclistance.feature_rescue_record.domain.use_case.RescueRecordUseCase
import com.example.cyclistance.feature_rescue_record.presentation.rescue_details.event.RescueDetailsEvent
import com.example.cyclistance.feature_rescue_record.presentation.rescue_details.state.RescueDetailsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RescueDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val rescueRecordUseCase: RescueRecordUseCase
): ViewModel() {

    private val _state = MutableStateFlow(
        savedStateHandle[RESCUE_DETAILS_VM_STATE_KEY]
        ?: RescueDetailsState(transactionId = savedStateHandle[RescueRecordConstants.TRANSACTION_ID]))
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<RescueDetailsEvent>()
    val eventFlow = _eventFlow.asSharedFlow()



    init {
        val transactionId = state.value.transactionId

        if(transactionId != null){
            loadRescueDetails(transactionId)
        }else{
            loadRescueDetails()
        }
    }


    private fun saveState(){
        savedStateHandle[RESCUE_DETAILS_VM_STATE_KEY] = state.value
    }

    private fun loadRescueDetails(){
        viewModelScope.launch(SupervisorJob()) {
            rescueRecordUseCase.rescueDetailsUseCase().collectLatest { rideDetails ->
                _eventFlow.emit(value = RescueDetailsEvent.GetRideSummarySuccess(rideSummary =  rideDetails.rideSummary, ))
            }
            rescueRecordUseCase.rideMetricsUseCase().collectLatest { metrics ->
                _eventFlow.emit(value = RescueDetailsEvent.GetRideMetricsSuccess(rideMetrics = metrics))
            }
            saveState()
        }
    }
    private fun loadRescueDetails(transactionId: String) {
        viewModelScope.launch(SupervisorJob()) {
            runCatching {
                isLoading(true)
                rescueRecordUseCase.getRescueRecordUseCase(transactionId = transactionId)
            }.onSuccess { rideRescue ->
                _eventFlow.emit(value = RescueDetailsEvent.GetRideSummarySuccess(rideRescue.rideSummary))
            }.onFailure {
                _eventFlow.emit(value = RescueDetailsEvent.GetRescueRecordFailed(it.message.toString()))
            }.also {
                isLoading(false)
                saveState()
            }
        }
    }

    private fun isLoading(loading: Boolean){
        _state.update { it.copy(isLoading = loading) }
    }
}
