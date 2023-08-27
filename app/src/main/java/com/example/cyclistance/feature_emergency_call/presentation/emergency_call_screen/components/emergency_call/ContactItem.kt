package com.example.cyclistance.feature_emergency_call.presentation.emergency_call_screen.components.emergency_call

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.cyclistance.R
import com.example.cyclistance.core.presentation.dialogs.common.DropDownMenu
import com.example.cyclistance.core.utils.constants.EmergencyCallConstants.DICE_BEAR_URL
import com.example.cyclistance.core.utils.constants.EmergencyCallConstants.NATIONAL_EMERGENCY_PHOTO
import com.example.cyclistance.core.utils.constants.EmergencyCallConstants.PHILIPPINE_RED_CROSS_PHOTO
import com.example.cyclistance.feature_emergency_call.domain.model.EmergencyContactModel
import com.example.cyclistance.theme.Black500

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ContactItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onClickEdit: () -> Unit,
    onClickDelete: () -> Unit,
    emergencyContact: EmergencyContactModel) {


    val imageModel = remember(emergencyContact.name, emergencyContact.photo) {
        if (emergencyContact.photo != NATIONAL_EMERGENCY_PHOTO &&
            emergencyContact.photo != PHILIPPINE_RED_CROSS_PHOTO) {
            "$DICE_BEAR_URL${emergencyContact.name}"
        } else {
            emergencyContact.photo
        }
    }



    Surface(
        onClick = onClick,
        color = MaterialTheme.colors.background,
        modifier = modifier.fillMaxWidth()) {

        Row(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp, alignment = Alignment.Start)) {


            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageModel)
                    .crossfade(true)
                    .networkCachePolicy(CachePolicy.ENABLED)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .build(),
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
                    text = emergencyContact.phoneNumber,
                    style = MaterialTheme.typography.caption,
                    color = Black500
                )
            }

            DropDownMenu(
                modifier = Modifier.wrapContentSize(),
                onClickEdit = onClickEdit,
                onClickDelete = onClickDelete
            )
        }
    }
}

