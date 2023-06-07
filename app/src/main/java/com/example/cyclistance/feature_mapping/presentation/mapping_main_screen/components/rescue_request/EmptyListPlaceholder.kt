package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components.rescue_request

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.cyclistance.theme.Black440

@Preview
@Composable
fun EmptyListPlaceholder() {
    Text(
        text = "No Rescue Request",
        color = Black440,
        fontWeight = FontWeight.Normal,
        style = MaterialTheme.typography.subtitle1,
        fontSize = 18.sp
    )
}
