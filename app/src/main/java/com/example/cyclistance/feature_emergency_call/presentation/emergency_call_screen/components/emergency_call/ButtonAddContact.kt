package com.example.cyclistance.feature_emergency_call.presentation.emergency_call_screen.components.emergency_call

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cyclistance.R
import com.example.cyclistance.theme.Black500
import com.example.cyclistance.theme.CyclistanceTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ButtonAddContact(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        color = Color.Transparent,
        modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp)) {


        Row(
            modifier = modifier.padding(vertical = 8.dp),
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
                    text = "Max 7 contacts",
                    style = MaterialTheme.typography.caption,
                    color = Black500
                )
            }


        }
    }
}


@Preview
@Composable
fun PreviewButtonAddContact() {
    CyclistanceTheme(darkTheme = true) {
        ButtonAddContact(onClick = {})
    }
}