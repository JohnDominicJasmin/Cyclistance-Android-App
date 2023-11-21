package com.myapp.cyclistance.feature_emergency_call.presentation.emergency_call_screen.event

sealed class EmergencyCallEvent {
    data object ContactDeleteSuccess : EmergencyCallEvent()
    data object ContactDeleteFailed : EmergencyCallEvent()


}