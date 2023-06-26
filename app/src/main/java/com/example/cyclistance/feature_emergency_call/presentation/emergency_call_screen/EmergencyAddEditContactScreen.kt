package com.example.cyclistance.feature_emergency_call.presentation.emergency_call_screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.cyclistance.feature_emergency_call.domain.model.ui.EmergencyCallModel
import com.example.cyclistance.feature_emergency_call.presentation.emergency_call_screen.components.EmergencyCallScreenContent
import com.example.cyclistance.feature_emergency_call.presentation.emergency_call_screen.event.EmergencyCallUiEvent
import com.example.cyclistance.navigation.Screens

@Composable
fun EmergencyCallScreen(navController: NavController, paddingValues: PaddingValues) {

    EmergencyCallScreenContent(
        modifier = Modifier.padding(paddingValues),
        emergencyCallModel = EmergencyCallModel(),
        event = { event ->
            when (event) {
                is EmergencyCallUiEvent.OnClickContact -> {

                }

                is EmergencyCallUiEvent.OnClickAddContact -> {
                    navController.navigate(Screens.EmergencyAddNewContact.route)
                }

                is EmergencyCallUiEvent.OnClickCancel -> {
                    navController.popBackStack()
                }

                is EmergencyCallUiEvent.OnClickDeleteContact -> {

                }

                is EmergencyCallUiEvent.OnClickEditContact -> {

                }

            }
        })
}