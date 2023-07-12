package com.example.cyclistance.feature_report_account.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ReportReasonItem(
    modifier: Modifier = Modifier,
    label: String,
    isChecked: Boolean,
    onReasonChecked: (String) -> Unit
) {


    Surface(
        onClick = { onReasonChecked(label) },
        modifier = modifier,
        color = Color.Transparent,
        shape = RoundedCornerShape(4.dp),
        elevation = 0.dp) {


        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start) {

            Checkbox(
                modifier = Modifier.offset(x = (-12).dp),
                checked = isChecked,
                onCheckedChange = {
                    onReasonChecked(label)
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colors.primary,
                    checkmarkColor = MaterialTheme.colors.onPrimary,
                    uncheckedColor = MaterialTheme.colors.onSurface,
                ))

            Text(
                text = label,
                color = MaterialTheme.colors.onSurface,
                style = MaterialTheme.typography.body1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)
            )
        }
    }
}

