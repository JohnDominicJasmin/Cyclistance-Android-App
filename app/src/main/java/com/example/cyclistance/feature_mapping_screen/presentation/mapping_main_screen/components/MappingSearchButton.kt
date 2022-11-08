package com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.MappingState

@Composable
fun SearchAssistanceButton(onClickSearchButton: () -> Unit, modifier: Modifier, enabled: Boolean, state: MappingState) {

    if(state.searchAssistanceButtonVisible) {

        Button(
            enabled = enabled,
            onClick = onClickSearchButton,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
            modifier = modifier) {

            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Assistance",
                tint = MaterialTheme.colors.onPrimary
            )

            Text(
                text = "Search Assistance",
                color = MaterialTheme.colors.onPrimary,
                style = MaterialTheme.typography.button,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(
                    top = 4.dp,
                    bottom = 4.dp,
                    start = 12.dp,
                    end = 12.dp)
            )
        }
    }
}