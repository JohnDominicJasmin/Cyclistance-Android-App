package com.example.cyclistance.feature_mapping.presentation.mapping_rescue_details

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.cyclistance.feature_mapping.presentation.mapping_rescue_details.components.RescueDetailsScreenContent

@Composable
fun RescueDetailsDialog(
    modifier: Modifier = Modifier,
) {
    Dialog(
        onDismissRequest = { /*event(MappingUiEvent.DismissRescueRequestDialog)*/ },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = false,
        )) {

        RescueDetailsScreenContent(
            modifier = modifier
        )
    }
}