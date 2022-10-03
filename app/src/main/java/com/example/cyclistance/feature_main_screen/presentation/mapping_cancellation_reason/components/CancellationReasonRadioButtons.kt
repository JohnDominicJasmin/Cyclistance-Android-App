package com.example.cyclistance.feature_main_screen.presentation.mapping_cancellation.components.jb

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cyclistance.theme.Black450


val clientCancellationReasons = listOf(
    "Change of mind",
    "Rescue time is too long",
    "Modifying existing rescue details (description, message and address).",
    "Not responsive to my questions.",
    "Problem already fixed.",
    "Other")

val rescuerCancellationReasons = listOf(
    "Change of mind",
    "Decided to rescue someone",
    "Location is hard to reach",
    "Not responsive to my questions.",
    "I didn't receive enough information",
    "Problem already fixed.",
    "Other")



@Composable
fun RadioButtonsSection(modifier : Modifier) {

    val (selectedOption, onOptionSelected) = remember { mutableStateOf(rescuerCancellationReasons[0]) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally) {

        rescuerCancellationReasons.forEach { text ->
            Row(
                Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .selectable(
                        selected = (text == selectedOption),
                        onClick = { onOptionSelected(text) })
                , horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {


                RadioButton(
                    selected = (text == selectedOption),
                    onClick = { onOptionSelected(text) },
                    colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colors.primary, unselectedColor = Black450)
                )
                Text(
                    text = text,
                    modifier = Modifier.padding(start = 8.dp),
                    color = MaterialTheme.colors.onBackground
                )
            }
        }
    }

}
