package com.example.cyclistance.feature_mapping.presentation.mapping_rescue_results

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.event.MappingUiEvent
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.state.MappingState
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.state.MappingUiState
import com.example.cyclistance.feature_mapping.presentation.mapping_rescue_results.components.RescueResultsScreenContent

@Composable
fun RescueResultsDialog(
    modifier: Modifier = Modifier,
    mappingState: MappingState = MappingState(),
    uiState: MappingUiState = MappingUiState(),
    event: (MappingUiEvent) -> Unit
) {


    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = false,
            dismissOnBackPress = true
        )
    ) {
        RescueResultsScreenContent(
            modifier = modifier,
            mappingState = mappingState,
            uiState = uiState,
            event = event
        )
    }

}