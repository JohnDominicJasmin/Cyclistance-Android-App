package com.myapp.cyclistance.feature_emergency_call.presentation.emergency_call_screen.components.emergency_call

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.myapp.cyclistance.core.presentation.dialogs.common.DropDownMenu
import com.myapp.cyclistance.feature_emergency_call.domain.model.EmergencyContactModel
import com.myapp.cyclistance.feature_emergency_call.presentation.add_edit_contact.components.AddEditContactImage
import com.myapp.cyclistance.feature_emergency_call.presentation.emergency_call_screen.event.EmergencyCallUiEvent
import com.myapp.cyclistance.theme.Black500

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ContactItem(
    modifier: Modifier = Modifier,
    event: (EmergencyCallUiEvent) -> Unit,
    emergencyContact: EmergencyContactModel) {


    Surface(
        onClick = {event(EmergencyCallUiEvent.OnClickContact(emergencyContact.phoneNumber))},
        color = MaterialTheme.colors.background,
        modifier = modifier.fillMaxWidth()) {

        Row(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp, alignment = Alignment.Start)) {


            AddEditContactImage(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape),
                photoUrl = emergencyContact.photo,

            )


            Column(verticalArrangement = Arrangement.spacedBy(2.dp), modifier =  Modifier.weight(0.99f)) {
                Text(
                    text = emergencyContact.name,
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onBackground,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Text(
                    text = emergencyContact.phoneNumber,
                    style = MaterialTheme.typography.caption,
                    color = Black500,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }

            DropDownMenu(
                modifier = Modifier.wrapContentSize().weight(0.1f),
                onClickEdit = { event(
                    EmergencyCallUiEvent.OnClickEditContact(
                        emergencyContact))},
                onClickDelete = { event(EmergencyCallUiEvent.OnClickDeleteContact(emergencyContact))}
            )
        }
    }
}

