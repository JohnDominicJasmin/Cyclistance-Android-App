package com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SearchAssistanceButton(onClickSearchButton: () -> Unit, modifier: Modifier, enabled: Boolean) {


    Button(
        enabled = enabled,
        onClick = onClickSearchButton,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
        modifier = modifier) {
        Text(
            text = "Search Assistance",
            color = MaterialTheme.colors.onPrimary,
            style = MaterialTheme.typography.button,
            modifier = Modifier.padding(
                top = 4.dp,
                bottom = 4.dp,
                start = 12.dp,
                end = 12.dp)
        )
    }

}