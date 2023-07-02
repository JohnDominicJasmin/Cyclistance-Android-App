package com.example.cyclistance.feature_mapping.presentation.mapping_rescue_request

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.event.MappingUiEvent
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.state.MappingState
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.state.MappingUiState
import com.example.cyclistance.feature_mapping.presentation.mapping_rescue_request.components.RescueRequestScreenContent


@Composable
fun RescueRequestDialog(
    modifier: Modifier = Modifier,
    mappingState: MappingState = MappingState(),
    uiState: MappingUiState = MappingUiState(),
    event: (MappingUiEvent) -> Unit
) {


    Dialog(
        onDismissRequest = { event(MappingUiEvent.DismissRescueRequestDialog) },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = false,
            dismissOnBackPress = true
        )) {

        RescueRequestScreenContent(
            modifier = modifier,
            mappingState = mappingState,
            uiState = uiState,
            event = event
        )
    }

}





