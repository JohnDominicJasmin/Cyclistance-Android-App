package com.example.cyclistance.feature_emergency_call.presentation.emergency_call_screen.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.cyclistance.R
import com.example.cyclistance.core.utils.constants.EmergencyCallConstants.NATIONAL_EMERGENCY
import com.example.cyclistance.core.utils.constants.EmergencyCallConstants.NATIONAL_EMERGENCY_PHOTO
import com.example.cyclistance.core.utils.constants.EmergencyCallConstants.PHILIPPINE_RED_CROSS
import com.example.cyclistance.core.utils.constants.EmergencyCallConstants.PHILIPPINE_RED_CROSS_PHOTO
import com.example.cyclistance.feature_emergency_call.domain.model.ui.EmergencyCallModel
import com.example.cyclistance.feature_emergency_call.domain.model.ui.EmergencyContactModel
import com.example.cyclistance.theme.Black500
import com.example.cyclistance.theme.CyclistanceTheme


private val emergencyContactModel = EmergencyCallModel(
    contacts = listOf(
        EmergencyContactModel(
            id = "1",
            name = "John Doe",
            photo = "",
            number = "1388"),

        EmergencyContactModel(
            id = "12",
            name = "Victoria Matthews",
            photo = "",
            number = "1388"),

        EmergencyContactModel(
            id = "12",
            name = "Jasmine Spencer",
            photo = "",
            number = "1388"),

        EmergencyContactModel(
            id = "12",
            name = "Jasmine Spencer",
            photo = "",
            number = "1388"),

        EmergencyContactModel(
            id = "12",
            name = "Jasmine Spencer",
            photo = "",
            number = "1388"),


        )


)


@Composable
fun EmergencyCallDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
) {
    var dialogOpen by rememberSaveable { mutableStateOf(true) }

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
                            .padding(vertical = 8.dp, horizontal = 4.dp), color = Black500)

                    ContactSection(modifier = Modifier, emergencyCallModel = emergencyContactModel)


                    OutlinedButton(
                        onClick = { /*TODO*/ },
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
    onClick: (String) -> Unit = {}) {
    Column(
        modifier = modifier.padding(vertical = 4.dp, horizontal = 2.dp),
        verticalArrangement = Arrangement.spacedBy(7.dp)) {

        Row {

            DialogEmergencyItem(
                modifier = Modifier.weight(0.5f),
                imageUrl = PHILIPPINE_RED_CROSS_PHOTO,
                name = PHILIPPINE_RED_CROSS,
                id = "1",
                onClick = onClick)


            DialogEmergencyItem(
                modifier = Modifier.weight(0.5f),
                imageUrl = NATIONAL_EMERGENCY_PHOTO,
                name = NATIONAL_EMERGENCY,
                id = "2",
                onClick = onClick)


        }
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            maxItemsInEachRow = 3,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            val contacts = emergencyCallModel.contacts

            repeat(contacts.indices.count()) {
                DialogEmergencyItem(
                    modifier = Modifier.weight(0.3f),
                    imageUrl = contacts[it].photo,
                    name = contacts[it].name,
                    id = contacts[it].id,
                    onClick = onClick)
            }


        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun DialogEmergencyItem(
    modifier: Modifier, imageUrl: String, name: String, id: String, onClick: (String) -> Unit) {


    Column(
        modifier = modifier.background(color = MaterialTheme.colors.surface),
        verticalArrangement = Arrangement.spacedBy(
            4.dp, alignment = Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally) {

        Surface(
            onClick = { onClick(id) },
            shape = CircleShape,
        ) {
            AsyncImage(
                model = imageUrl,
                alignment = Alignment.Center,
                contentDescription = "User Profile Image",
                modifier = Modifier
                    .clip(CircleShape)
                    .size(54.dp),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.ic_empty_profile_placeholder_large),
                error = painterResource(id = R.drawable.ic_empty_profile_placeholder_large),
                fallback = painterResource(id = R.drawable.ic_empty_profile_placeholder_large))
        }
        Text(
            text = name,
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
            DialogEmergencyItem(modifier = Modifier.width(70.dp),
                imageUrl = PHILIPPINE_RED_CROSS_PHOTO,
                name = PHILIPPINE_RED_CROSS,
                id = "1",
                onClick = {})
        }
    }
}


@Preview(device = "id:Galaxy Nexus")
@Composable
fun PreviewEmergencyCallDialog() {
    CyclistanceTheme(darkTheme = true) {
        EmergencyCallDialog(onDismiss = {})
    }
}