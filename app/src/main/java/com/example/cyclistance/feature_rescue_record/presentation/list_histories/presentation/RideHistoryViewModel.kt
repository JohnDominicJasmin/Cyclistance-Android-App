package com.example.cyclistance.feature_rescue_record.presentation.list_histories.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.core.utils.constants.RescueRecordConstants.RIDE_HISTORY_VM_STATE_KEY
import com.example.cyclistance.feature_rescue_record.domain.use_case.RescueRecordUseCase
import com.example.cyclistance.feature_rescue_record.presentation.list_histories.presentation.event.RideHistoryEvent
import com.example.cyclistance.feature_rescue_record.presentation.list_histories.presentation.state.RideHistoryState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RideHistoryViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val rescueRecordUseCase: RescueRecordUseCase
) : ViewModel() {


    private val _state =
        MutableStateFlow(savedStateHandle[RIDE_HISTORY_VM_STATE_KEY] ?: RideHistoryState())
    val state = _state.asStateFlow()


    private val _eventFlow = MutableSharedFlow<RideHistoryEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        getRideHistory()
    }

    private fun getRideHistory() {
        viewModelScope.launch {
            runCatching {
                isLoading(true)
                rescueRecordUseCase.getRideHistoryUseCase()
            }.onSuccess { history ->
                isLoading(false)
                _eventFlow.emit(value = RideHistoryEvent.GetRideHistorySuccess(rideHistory = history))
            }.onFailure {
                isLoading(false)
                _eventFlow.emit(value = RideHistoryEvent.GetRideHistoryFailed(it.message ?: "Failed to fetch ride history"))
            }.also {

                savedStateHandle[RIDE_HISTORY_VM_STATE_KEY] = state.value
            }
        }
    }

    private fun isLoading(loading: Boolean) {
        _state.update { it.copy(isLoading = loading) }
    }
}