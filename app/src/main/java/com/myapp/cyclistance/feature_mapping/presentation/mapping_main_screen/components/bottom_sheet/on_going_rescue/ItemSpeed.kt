package com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.bottom_sheet.on_going_rescue

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


@Composable
internal fun RowScope.ItemSpeed(modifier: Modifier, title: String, content: String) {
    Column(
        modifier = modifier
            .padding(vertical = 4.dp)
            .weight(0.3f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Text(
            text = title,
            color = MaterialTheme.colors.onSurface,
            style = MaterialTheme.typography.caption.copy(
                fontWeight = FontWeight.Normal,
                fontSize = MaterialTheme.typography.caption.fontSize))

        Text(
            text = content,
            color = MaterialTheme.colors.onSurface,
            style = MaterialTheme.typography.subtitle1.copy(
                fontWeight = FontWeight.Medium,
                fontSize = MaterialTheme.typography.button.fontSize))
    }
}
