package com.example.cyclistance.feature_emergency_call.presentation.emergency_call_screen.components.emergency_call

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.cyclistance.R
import com.example.cyclistance.theme.Black500

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ButtonAddContact(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        color = MaterialTheme.colors.background,
        modifier = modifier
            .fillMaxWidth()) {


        Row(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp, alignment = Alignment.Start)) {

            Icon(
                painter = painterResource(id = R.drawable.ic_add_to_contact),
                contentDescription = "Add to contact",
                tint = MaterialTheme.colors.primary,
                modifier = Modifier.size(54.dp)
            )
            Column {
                Text(
                    text = "Add new",
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onBackground
                )
                Text(
                    text = "Max 5 contacts",
                    style = MaterialTheme.typography.caption,
                    color = Black500
                )
            }


        }
    }
}
