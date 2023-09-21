package com.example.cyclistance.feature_rescue_record.presentation.rescue_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.core.utils.constants.RescueRecordConstants.RESCUE_DETAILS_VM_STATE_KEY
import com.example.cyclistance.feature_rescue_record.domain.use_case.RescueRecordUseCase
import com.example.cyclistance.feature_rescue_record.presentation.rescue_details.event.RescueDetailsEvent
import com.example.cyclistance.feature_rescue_record.presentation.rescue_details.event.RescueDetailsVmEvent
import com.example.cyclistance.feature_rescue_record.presentation.rescue_details.state.RescueDetailsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RescueDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val rescueRecordUseCase: RescueRecordUseCase
): ViewModel() {
    
    private val _state = MutableStateFlow(savedStateHandle[RESCUE_DETAILS_VM_STATE_KEY] ?: RescueDetailsState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<RescueDetailsEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


    fun onEvent(event: RescueDetailsVmEvent) {
        when(event){
            is RescueDetailsVmEvent.LoadRescueDetails -> {
                loadRescueDetails(event.transactionId)
            }
        }
        savedStateHandle[RESCUE_DETAILS_VM_STATE_KEY] = state.value
    }


    private fun loadRescueDetails(transactionId: String){
        viewModelScope.launch {
            runCatching {
                isLoading(true)
                rescueRecordUseCase.getRescueRecordUseCase(transactionId = transactionId)
            }.onSuccess { rideDetails ->
                _eventFlow.emit(value = RescueDetailsEvent.GetRescueRecordSuccess(rideDetails.rideSummary))
            }.onFailure {
                _eventFlow.emit(value = RescueDetailsEvent.GetRescueRecordFailed(it.message.toString()))
            }.also {
                isLoading(false)
            }
        }
    }

    private fun isLoading(loading: Boolean){
        _state.update { it.copy(isLoading = loading) }
    }
}
