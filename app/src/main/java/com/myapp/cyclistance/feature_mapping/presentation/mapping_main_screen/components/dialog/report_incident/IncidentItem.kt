package com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.dialog.report_incident

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.myapp.cyclistance.theme.Black440
import com.myapp.cyclistance.theme.Orange800

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun IncidentItem(
    modifier: Modifier,
    @DrawableRes icon: Int,
    incidentLabel: String,
    buttonColor: Color = Orange800,
    selectedLabel: String,
    onClick: (label: String) -> Unit
) {

    val isSelected by remember(incidentLabel, selectedLabel) {
        derivedStateOf { incidentLabel == selectedLabel }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp, alignment = Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally) {

        Surface(
            shape = CircleShape,
            modifier = Modifier.size(54.dp),
            color = buttonColor,
            onClick = { onClick(incidentLabel) }) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = "$incidentLabel Icon",
                modifier = Modifier.padding(all = 12.dp)
            )
        }

        Text(
            text = incidentLabel,
            color = if (isSelected) MaterialTheme.colors.onSurface else Black440,
            style = MaterialTheme.typography.caption.copy(fontWeight = FontWeight.SemiBold),
            overflow = TextOverflow.Clip, textAlign = TextAlign.Center,
        )

    }
}
