package com.example.cyclistance.feature_rescue_record.presentation.rescue_results

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.core.utils.constants.RescueRecordConstants.RESCUE_RESULT_VM_STATE_KEY
import com.example.cyclistance.feature_rescue_record.domain.use_case.RescueRecordUseCase
import com.example.cyclistance.feature_rescue_record.presentation.rescue_results.event.RescueResultEvent
import com.example.cyclistance.feature_rescue_record.presentation.rescue_results.event.RescueResultVmEvent
import com.example.cyclistance.feature_rescue_record.presentation.rescue_results.state.RescueResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RescueResultViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    rescueRecordUseCase: RescueRecordUseCase

): ViewModel() {

    private val _state = MutableStateFlow(
        savedStateHandle[RESCUE_RESULT_VM_STATE_KEY] ?: RescueResultState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<RescueResultEvent>()
    val event = _eventFlow.asSharedFlow()



    init {
        viewModelScope.launch {
            rescueRecordUseCase.rescueDetailsUseCase().collect{ rideDetails ->
                _state.update { it.copy(rideDetails = rideDetails) }
            }
        }
    }

    fun onEvent(event: RescueResultVmEvent){

    }


}