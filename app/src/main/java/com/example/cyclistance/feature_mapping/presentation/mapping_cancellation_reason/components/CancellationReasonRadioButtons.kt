package com.example.cyclistance.feature_mapping.presentation.mapping_cancellation_reason.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cyclistance.core.utils.constants.MappingConstants.SELECTION_RESCUEE_TYPE
import com.example.cyclistance.feature_authentication.presentation.common.ErrorMessage
import com.example.cyclistance.theme.Black450
import com.example.cyclistance.theme.CyclistanceTheme


val rescueeCancellationReasons = listOf(
    "Change of mind",
    "Rescue time is too long",
    "Need to modify rescue details (description, message and address).",
    "Not responsive to my questions.",
    "Problem already fixed.",
    "Other")

val rescuerCancellationReasons = listOf(
    "Change of mind",
    "Decided to rescue someone",
    "Location is hard to reach",
    "Not responsive to my questions",
    "I didn't receive enough information",
    "Job wasn't in the scope of the original request",
    "Problem already fixed",
    "Other")


@Composable
fun RadioButtonsSection(
    modifier: Modifier,
    cancellationType: String = SELECTION_RESCUEE_TYPE,
    selectedOption: String,
    errorMessage: String,
    onSelectReason: (String) -> Unit) {

    val hasError = remember(errorMessage){ errorMessage.isNotEmpty() }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start) {

        val selectionType =
            remember(cancellationType) { (if (cancellationType == SELECTION_RESCUEE_TYPE) rescueeCancellationReasons else rescuerCancellationReasons) }
        selectionType.forEach { text ->
            SelectionButton(
                selectionText = text,
                selectedOption = selectedOption,
                onOptionSelected = onSelectReason)
        }

        if (hasError) {
            ErrorMessage(
                errorMessage = errorMessage,
                modifier = Modifier
                    .padding(vertical = 10.dp, horizontal = 1.2.dp)
                    .padding(start = 14.dp))
        }

    }

}

@Preview
@Composable
fun SelectionButtonPreview() {
    CyclistanceTheme(true) {
        SelectionButton(
            selectionText = "Need to modify rescue details (description, message and address).",
            selectedOption = "Change of mind",
            onOptionSelected = {})
    }
}

@Composable
private fun SelectionButton(
    selectionText: String,
    selectedOption: String,
    onOptionSelected: (String) -> Unit) {

    Row(
        Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .selectable(
                selected = (selectionText == selectedOption),
                onClick = { onOptionSelected(selectionText) }),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically) {


        RadioButton(
            selected = (selectionText == selectedOption),
            onClick = { onOptionSelected(selectionText) },
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colors.primary,
                unselectedColor = Black450)
        )
        Text(
            text = selectionText,
            modifier = Modifier.padding(start = 8.dp, end = 4.dp),
            color = MaterialTheme.colors.onBackground,
        )
    }
}
