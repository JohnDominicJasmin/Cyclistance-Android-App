package com.example.cyclistance.feature_emergency_call.presentation.add_edit_contact.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import com.example.cyclistance.R
import com.example.cyclistance.feature_emergency_call.presentation.add_edit_contact.event.AddEditContactUiEvent


@Composable
fun AddEditPhotoSection(
    isOnEditMode: Boolean,
    selectedImage: String,
    event: (AddEditContactUiEvent) -> Unit) {


    val shouldShowAddEditImage = remember(isOnEditMode, selectedImage) {
        isOnEditMode.or(selectedImage.isNotEmpty())
    }


    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally) {
        if (shouldShowAddEditImage) {

            AddEditContactImage(
                photoUrl = selectedImage,

                modifier = Modifier
                    .size(125.dp)
                    .clip(CircleShape)
                    .clickable { event(AddEditContactUiEvent.ToggleBottomSheet) })

        } else {

            Icon(
                painter = painterResource(id = R.drawable.ic_add_to_contact),
                contentDescription = "Add new contact",
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .clickable { event(AddEditContactUiEvent.ToggleBottomSheet) }
            )

        }


        ClickableText(
            text = buildAnnotatedString {
                append(if (isOnEditMode) "Change Photo" else "Add Photo")
            },
            onClick = {
                event(AddEditContactUiEvent.ToggleBottomSheet)
            },
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.body2.copy(color = MaterialTheme.colors.onBackground))


    }


}

