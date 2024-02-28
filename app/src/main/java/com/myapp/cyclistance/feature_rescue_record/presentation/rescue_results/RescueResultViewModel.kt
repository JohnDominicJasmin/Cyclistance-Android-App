package com.myapp.cyclistance.feature_rescue_record.presentation.rescue_results

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapp.cyclistance.core.utils.constants.RescueRecordConstants.RESCUE_RESULT_VM_STATE_KEY
import com.myapp.cyclistance.feature_rescue_record.domain.model.ui.RideDetails
import com.myapp.cyclistance.feature_rescue_record.domain.use_case.RescueRecordUseCase
import com.myapp.cyclistance.feature_rescue_record.presentation.rescue_results.event.RescueResultEvent
import com.myapp.cyclistance.feature_rescue_record.presentation.rescue_results.event.RescueResultVmEvent
import com.myapp.cyclistance.feature_rescue_record.presentation.rescue_results.state.RescueResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
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
class RescueResultViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val rescueRecordUseCase: RescueRecordUseCase

): ViewModel() {

    private val _state = MutableStateFlow(
        savedStateHandle[RESCUE_RESULT_VM_STATE_KEY] ?: RescueResultState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<RescueResultEvent>()
    val eventFlow = _eventFlow.asSharedFlow()



    init {
        loadRideDetails()
    }

    private fun loadRideDetails(){
        rescueRecordUseCase.rideDetailsUseCase().catch {
            Timber.e( "Failed to load ride details: ${it.message}")
        }.onEach {rideDetails ->
            if(rideDetails.isNotEmpty()){
                _state.update { it.copy(rideDetails = rideDetails.last()) }
            }
        }.launchIn(viewModelScope)

    }

    private fun saveState(){
        savedStateHandle[RESCUE_RESULT_VM_STATE_KEY] = state.value
    }
    fun onEvent(event: RescueResultVmEvent){
        when(event){
            is RescueResultVmEvent.RateRescue -> {
                rateRescue(event.rating)
            }


        }
        saveState()
    }




    private fun rateRescue(rating: Float) {
        val validRating = rating.coerceIn(1f, 5f)
        val rideDetails = state.value.rideDetails

        viewModelScope.launch(SupervisorJob() + Dispatchers.IO) {
            runCatching {
                val rateRescue = rateRescueUseCase(rideDetails, validRating)
                val rateRescuer = rateRescuerUseCase(rideDetails, validRating)

                awaitAll(rateRescue, rateRescuer)
            }.onSuccess {
                _eventFlow.emit(value = RescueResultEvent.RatingSuccess)
            }.onFailure {
                _eventFlow.emit(value = RescueResultEvent.RatingFailed(message = it.message!!))
            }
            saveState()
        }
    }

    private suspend fun rateRescueUseCase(rideDetails: RideDetails, rating: Float) =
        coroutineScope {
            async {
                rescueRecordUseCase.rateRescueUseCase(
                    rescueId = rideDetails.rideId,
                    rating = rating.toDouble(),
                    ratingText = ratingToDescription(rating)
                )
            }
        }

    private suspend fun rateRescuerUseCase(rideDetails: RideDetails, rating: Float) =
        coroutineScope {
            async {
                rescueRecordUseCase.rateRescuerUseCase(
                    rescuerId = rideDetails.rescuerId,
                    rating = rating.toDouble()
                )
            }
        }





    private fun ratingToDescription(rating: Float): String {
        return when (rating) {
            in 0.0f..1.0f -> "Poor"
            in 1.1f..2.0f -> "Bad"
            in 2.1f..3.0f -> "Fair"
            in 3.1f..4.0f -> "Good"
            in 4.1f..5.0f -> "Excellent"
            else -> "Good"
        }
    }

}