package com.example.cyclistance.feature_rescue_outcome.presentation.rescue_details

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.cyclistance.feature_rescue_outcome.presentation.rescue_details.components.RescueDetailsScreenContent

@Composable
fun RescueDetailsScreen(
    paddingValues: PaddingValues,
    navController: NavController
) {

    RescueDetailsScreenContent(
        modifier = Modifier.padding(paddingValues)
    )
}