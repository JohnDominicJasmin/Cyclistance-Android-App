package com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.bottom_sheet.incident_description

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.myapp.cyclistance.feature_mapping.presentation.common.AdditionalMessage
import com.myapp.cyclistance.feature_mapping.presentation.common.ButtonNavigation
import com.myapp.cyclistance.theme.Black500

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IncidentDescriptionEditMode(
    modifier: Modifier = Modifier,
    markerLabel: String,
    markerDescription: String,
    onClickCancelButton: () -> Unit,
    onClickConfirmButton: (description: String, label: String) -> Unit) {

    val pagerState = rememberPagerState(pageCount = {
        6
    })

    var description by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }

    val descriptionChanges = remember(description) {
        description.text != markerDescription
    }
    val labelChanges = remember(pagerState.currentPage) {
        incidentMarkers[pagerState.currentPage].first != markerLabel
    }

    val inputChanges = remember(descriptionChanges, labelChanges) {
        descriptionChanges || labelChanges
    }


    LaunchedEffect(key1 = markerDescription) {
        description = TextFieldValue(markerDescription)
    }


    LaunchedEffect(key1 = true) {
        pagerState.scrollToPage(page = incidentMarkers.indexOfFirst { it.first == markerLabel })
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {

        Text(
            text = "Edit Hazardous Marker",
            color = MaterialTheme.colors.onSurface,
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.padding(top = 12.dp, bottom = 4.dp)
        )

        Divider(
            modifier = Modifier
                .fillMaxWidth(0.91f)
                .padding(top = 12.dp, bottom = 4.dp),
            thickness = 1.2.dp,
            color = Black500,
        )

        ChooseMarkerSection(
            pagerState = pagerState
        )

        HazardousIncidentIndicator(
            pagerState = pagerState
        )


        AdditionalMessage(
            text = "Description",
            placeholderText = null,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(120.dp),
            message = description,
            enabled = true,
            onChangeValueMessage = {
                description = it
            })

        ButtonNavigation(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(vertical = 16.dp),
            positiveButtonEnabled = inputChanges,
            onClickNegativeButton = onClickCancelButton,
            onClickPositiveButton = {
                onClickConfirmButton(
                    description.text,
                    incidentMarkers[pagerState.currentPage].first)
            },
        )


    }

}
