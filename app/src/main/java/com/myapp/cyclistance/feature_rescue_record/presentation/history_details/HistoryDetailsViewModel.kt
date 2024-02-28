package com.myapp.cyclistance.feature_rescue_record.presentation.history_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapp.cyclistance.core.utils.constants.RescueRecordConstants.RIDE_HISTORY_DETAILS_VM_STATE_KEY
import com.myapp.cyclistance.feature_rescue_record.domain.use_case.RescueRecordUseCase
import com.myapp.cyclistance.feature_rescue_record.presentation.history_details.event.HistoryDetailsEvent
import com.myapp.cyclistance.feature_rescue_record.presentation.history_details.event.HistoryDetailsVmEvent
import com.myapp.cyclistance.feature_rescue_record.presentation.history_details.state.HistoryDetailsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val rescueRecordUseCase: RescueRecordUseCase
) : ViewModel(){
    
    
    private val _state = MutableStateFlow(savedStateHandle[RIDE_HISTORY_DETAILS_VM_STATE_KEY] ?: HistoryDetailsState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<HistoryDetailsEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: HistoryDetailsVmEvent){
        when(event){
            is HistoryDetailsVmEvent.LoadRideDetails -> loadRescueDetails(transactionId = event.transactionId)
        }
    }

    private fun loadRescueDetails(transactionId: String){
        viewModelScope.launch {
            runCatching {
                isLoading(true)
                rescueRecordUseCase.getRescueRecordUseCase(transactionId = transactionId)
            }.onSuccess { rescueRide ->
                _eventFlow.emit(value = HistoryDetailsEvent.GetRideDetailsSuccess(rescueRide))
            }.onFailure {
                _eventFlow.emit(value = HistoryDetailsEvent.GetRideDetailsFailed(it.message.toString()))
            }.also {
                isLoading(false)
            }
        }
    }

    private fun isLoading(loading: Boolean){
        _state.update { it.copy(isLoading = loading) }
    }
}