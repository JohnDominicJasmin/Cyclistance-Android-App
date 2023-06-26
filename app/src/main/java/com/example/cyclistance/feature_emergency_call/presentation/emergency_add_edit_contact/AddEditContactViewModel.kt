package com.example.cyclistance.feature_emergency_call.presentation.emergency_add_edit_contact

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.cyclistance.core.utils.constants.EmergencyCallConstants.EMERGENCY_CALL_VM_STATE_KEY
import com.example.cyclistance.feature_emergency_call.presentation.emergency_add_edit_contact.state.AddEditContactState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AddEditContactViewModel(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _state =
        MutableStateFlow(savedStateHandle[EMERGENCY_CALL_VM_STATE_KEY] ?: AddEditContactState())
    val state = _state.asStateFlow()

}