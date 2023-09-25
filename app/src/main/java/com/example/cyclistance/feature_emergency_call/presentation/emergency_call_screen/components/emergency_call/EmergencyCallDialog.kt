package com.example.cyclistance.feature_emergency_call.presentation.emergency_call_screen.components.emergency_call

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.cyclistance.core.utils.constants.EmergencyCallConstants.NATIONAL_EMERGENCY
import com.example.cyclistance.core.utils.constants.EmergencyCallConstants.NATIONAL_EMERGENCY_NUMBER
import com.example.cyclistance.core.utils.constants.EmergencyCallConstants.NATIONAL_EMERGENCY_PHOTO
import com.example.cyclistance.core.utils.constants.EmergencyCallConstants.PHILIPPINE_RED_CROSS
import com.example.cyclistance.core.utils.constants.EmergencyCallConstants.PHILIPPINE_RED_CROSS_NUMBER
import com.example.cyclistance.core.utils.constants.EmergencyCallConstants.PHILIPPINE_RED_CROSS_PHOTO
import com.example.cyclistance.feature_emergency_call.domain.model.EmergencyCallModel
import com.example.cyclistance.feature_emergency_call.domain.model.EmergencyContactModel
import com.example.cyclistance.feature_emergency_call.presentation.emergency_call_screen.components.add_edit_contact.AddEditContactImage
import com.example.cyclistance.theme.Black500
import com.example.cyclistance.theme.CyclistanceTheme


val emergencyContactModel1 = EmergencyCallModel(
    contacts = listOf(
        EmergencyContactModel(
            name = PHILIPPINE_RED_CROSS,
            photo = PHILIPPINE_RED_CROSS_PHOTO,
            phoneNumber = PHILIPPINE_RED_CROSS_NUMBER),
        EmergencyContactModel(
            name = NATIONAL_EMERGENCY,
            photo = NATIONAL_EMERGENCY_PHOTO,
            phoneNumber = NATIONAL_EMERGENCY_NUMBER),
        EmergencyContactModel(
            id = 1,
            name = "John Doe",
            photo = "",
            phoneNumber = "1388"),

        EmergencyContactModel(
            id = 12,
            name = "Victoria Matthews",
            photo = "",
            phoneNumber = "1388"),

        EmergencyContactModel(
            id = 12,
            name = "Jasmine Spencer",
            photo = "",
            phoneNumber = "1388"),

        EmergencyContactModel(
            id = 123,
            name = "Jasmine Spencer",
            photo = "",
            phoneNumber = "1388"),

        EmergencyContactModel(
            id = 33,
            name = "Jasmine Spencer",
            photo = "",
            phoneNumber = "1388"),


        )


)


@Composable
fun EmergencyCallDialog(
    modifier: Modifier = Modifier,
    emergencyCallModel: EmergencyCallModel,
    onDismiss: () -> Unit,
    onClick: (EmergencyContactModel) -> Unit,
    onAddContact: () -> Unit,
) {
    var dialogOpen by rememberSaveable { mutableStateOf(true) }

    val emergencyContactAvailable = remember(emergencyCallModel.contacts) {
        emergencyCallModel.contacts.isNotEmpty()
    }

    if (dialogOpen) {
        Dialog(
            onDismissRequest = {
                onDismiss()
                dialogOpen = false
            }, properties = DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnBackPress = true,
                dismissOnClickOutside = true)) {

            Surface(
                modifier = modifier
                    .fillMaxWidth(0.9f)
                    .wrapContentHeight(),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colors.surface) {

                Column(
                    modifier = Modifier.padding(all = 8.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {

                    Text(
                        text = "Emergency Call",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        style = MaterialTheme.typography.subtitle2,
                        color = MaterialTheme.colors.onSurface,
                        textAlign = TextAlign.Center)

                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 8.dp), color = Black500)


                    if (emergencyContactAvailable) {
                        ContactSection(
                            modifier = Modifier,
                            emergencyCallModel = emergencyCallModel,
                            onClick = {
                                onDismiss()
                                onClick(it)
                            })
                    } else {
                        ButtonAddContact(
                            onClick = onAddContact,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp)
                                .padding(horizontal = 4.dp))

                        Text(
                            text = "No contacts added",
                            style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Medium),
                            color = Black500,
                            modifier = Modifier.padding(vertical = 24.dp)
                        )
                    }




                    OutlinedButton(
                        onClick = onDismiss,
                        border = BorderStroke(1.dp, Black500),
                        modifier = Modifier
                            .wrapContentHeight()
                            .padding(all = 7.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Transparent,
                            contentColor = MaterialTheme.colors.onSurface),
                        shape = RoundedCornerShape(12.dp),
                    ) {

                        Text(
                            text = "Cancel",
                            style = MaterialTheme.typography.button,
                            modifier = Modifier.padding(horizontal = 12.dp))
                    }

                }
            }
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ContactSection(
    modifier: Modifier = Modifier,
    emergencyCallModel: EmergencyCallModel,
    onClick: (EmergencyContactModel) -> Unit) {
    Column(
        modifier = modifier.padding(vertical = 4.dp, horizontal = 2.dp),
        verticalArrangement = Arrangement.spacedBy(7.dp)) {


        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            maxItemsInEachRow = 3,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            val contacts = emergencyCallModel.contacts

            repeat(contacts.indices.count()) {
                DialogEmergencyItem(
                    modifier = Modifier.weight(0.3f),
                    emergencyContact = contacts[it],
                    onClick = onClick)
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun DialogEmergencyItem(
    modifier: Modifier,
    emergencyContact: EmergencyContactModel,
    onClick: (EmergencyContactModel) -> Unit) {



    Column(
        modifier = modifier.background(color = MaterialTheme.colors.surface),
        verticalArrangement = Arrangement.spacedBy(
            4.dp, alignment = Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally) {

        Surface(
            onClick = { onClick(emergencyContact) },
            shape = CircleShape,
        ) {

            AddEditContactImage(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(54.dp),
                photoUrl = emergencyContact.photo,
            )
        }
        Text(
            text = emergencyContact.name,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.caption,
            modifier = Modifier.padding(all = 2.dp),
            color = MaterialTheme.colors.onSurface)
    }
}

@Preview
@Composable
fun PreviewDialogEmergencyItem() {
    CyclistanceTheme(darkTheme = true) {
        Box(modifier = Modifier.background(MaterialTheme.colors.onSurface)) {
            DialogEmergencyItem(
                modifier = Modifier.width(70.dp),
                emergencyContact = EmergencyContactModel(
                    photo = PHILIPPINE_RED_CROSS_PHOTO,
                    name = PHILIPPINE_RED_CROSS,
                    id = 11,
                    phoneNumber = "1234",

                    ),
                onClick = {},
            )
        }
    }
}


@Preview(device = "id:Galaxy Nexus")
@Composable
fun PreviewEmergencyCallDialog() {
    CyclistanceTheme(darkTheme = true) {
        EmergencyCallDialog(
            onDismiss = {},
            emergencyCallModel = EmergencyCallModel(),
            onClick = {},
            onAddContact = {})
    }
}