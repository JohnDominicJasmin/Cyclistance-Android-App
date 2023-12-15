package com.myapp.cyclistance.feature_rescue_record.presentation.rescue_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapp.cyclistance.core.utils.constants.RescueRecordConstants
import com.myapp.cyclistance.core.utils.constants.RescueRecordConstants.RESCUE_DETAILS_VM_STATE_KEY
import com.myapp.cyclistance.feature_rescue_record.domain.use_case.RescueRecordUseCase
import com.myapp.cyclistance.feature_rescue_record.presentation.rescue_details.event.RescueDetailsEvent
import com.myapp.cyclistance.feature_rescue_record.presentation.rescue_details.state.RescueDetailsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
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

        rescueRecordUseCase.rideDetailsUseCase().catch {
            Timber.v( "Failed to load ride details: ${it.message}")
        }.onEach {
            if(it.isNotEmpty()){
                _eventFlow.emit(value = RescueDetailsEvent.GetRideSummarySuccess(rideSummary = it.last().rideSummary))
            }
        }.launchIn(viewModelScope)


        rescueRecordUseCase.rideMetricsUseCase().catch {
            Timber.v( "Failed to load ride details: ${it.message}")
        }.onEach { metrics ->
            if(metrics.isNotEmpty()){
                _eventFlow.emit(value = RescueDetailsEvent.GetRideMetricsSuccess(rideMetrics = metrics.last()))
            }
        }.launchIn(viewModelScope)

        saveState()
    }
    private fun loadRescueDetails(transactionId: String) {
        viewModelScope.launch(SupervisorJob()) {
            runCatching {
                isLoading(true)
                rescueRecordUseCase.getRescueRecordUseCase(transactionId = transactionId)
            }.onSuccess { rideRescue ->
                _eventFlow.emit(value = RescueDetailsEvent.GetRideSummarySuccess(rideRescue.rideSummary))
                _eventFlow.emit(value = RescueDetailsEvent.GetRideMetricsSuccess(rideRescue.rideMetrics))
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
