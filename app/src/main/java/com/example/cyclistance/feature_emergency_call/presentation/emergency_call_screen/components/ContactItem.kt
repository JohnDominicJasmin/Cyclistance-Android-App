package com.example.cyclistance.feature_emergency_call.presentation.emergency_call_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.cyclistance.R
import com.example.cyclistance.feature_emergency_call.domain.model.ui.EmergencyContactModel
import com.example.cyclistance.theme.Black500

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ContactItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onClickEdit: (id: String) -> Unit,
    onClickDelete: (emergencyContact: EmergencyContactModel) -> Unit,
    emergencyContact: EmergencyContactModel) {

    Surface(
        onClick = onClick,
        color = MaterialTheme.colors.background,
        modifier = modifier.fillMaxWidth()) {

        Row(
            modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp, alignment = Alignment.Start)
        ) {

            AsyncImage(
                model = emergencyContact.photo,
                alignment = Alignment.Center,
                contentDescription = "User Profile Image",
                modifier = Modifier
                    .clip(CircleShape)
                    .size(54.dp),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.ic_empty_profile_placeholder_large),
                error = painterResource(id = R.drawable.ic_empty_profile_placeholder_large),
                fallback = painterResource(id = R.drawable.ic_empty_profile_placeholder_large))


            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = emergencyContact.name,
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onBackground
                )
                Text(
                    text = emergencyContact.number,
                    style = MaterialTheme.typography.caption,
                    color = Black500
                )
            }

            DropDownMenu(
                modifier = Modifier.wrapContentSize(),
                onClickEdit = { onClickEdit(emergencyContact.id) },
                onClickDelete = { onClickDelete(emergencyContact) }
            )
        }
    }
}

@Composable
private fun DropDownMenu(
    modifier: Modifier = Modifier,
    onClickEdit: () -> Unit,
    onClickDelete: () -> Unit) {

    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopEnd)
    ) {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "More"
            )
        }

        DropdownMenu(
            modifier = Modifier
                .background(MaterialTheme.colors.secondary)
                .wrapContentSize(),
            expanded = expanded,
            onDismissRequest = { expanded = false }) {

            DropdownMenuItem(
                text = { Text("Edit") },
                onClick = { onClickEdit(); expanded = false }
            )

            DropdownMenuItem(
                text = { Text("Delete") },
                onClick = { onClickDelete(); expanded = false }
            )
        }
    }
}