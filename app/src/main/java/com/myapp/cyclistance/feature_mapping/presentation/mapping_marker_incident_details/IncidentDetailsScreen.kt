package com.myapp.cyclistance.feature_mapping.presentation.mapping_marker_incident_details

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.hazardous_lane.HazardousLaneMarkerDetails
import com.myapp.cyclistance.feature_mapping.presentation.mapping_marker_incident_details.components.IncidentDetailsContent

@Composable
fun IncidentDetailsScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    markerDetails: HazardousLaneMarkerDetails) {


    IncidentDetailsContent(modifier = Modifier.padding(paddingValues), marker = markerDetails)
}