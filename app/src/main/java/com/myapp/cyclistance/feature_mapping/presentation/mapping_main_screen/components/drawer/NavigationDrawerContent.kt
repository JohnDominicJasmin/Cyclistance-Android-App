package com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.drawer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.myapp.cyclistance.R
import com.myapp.cyclistance.navigation.state.NavUiState
import com.myapp.cyclistance.theme.Black440
import com.myapp.cyclistance.theme.Black450
import com.myapp.cyclistance.theme.CyclistanceTheme


@Composable
fun NavigationDrawerContent(
    onClickEmergencyCall: () -> Unit = {},
    onClickSettings: () -> Unit = {},
    onClickRideHistory: () -> Unit = {},
    onClickSignOut: () -> Unit = {},
    onClickUserProfile: () -> Unit = {},
    uiState: NavUiState = NavUiState()
) {

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        constraintSet = drawerMappingConstraintSet) {

        Box(
            modifier = Modifier
                .layoutId(MappingDrawerConstraintItem.UpperSection.layoutId)
                .background(MaterialTheme.colors.secondaryVariant)) {


            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally) {


                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(uiState.drawerPhotoUrl)
                        .crossfade(true)
                        .networkCachePolicy(CachePolicy.ENABLED)
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .memoryCachePolicy(CachePolicy.ENABLED)
                        .build(), contentDescription = "User Profile Image",
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(105.dp)
                        .border(
                            width = 3.6.dp,
                            color = MaterialTheme.colors.primary,
                            shape = CircleShape)
                        .clickable { onClickUserProfile() },
                    contentScale = ContentScale.Crop,
                    placeholder = rememberAsyncImagePainter(model = R.drawable.ic_empty_profile_placeholder_large),
                    error = painterResource(id = R.drawable.ic_empty_profile_placeholder_large),
                    fallback = painterResource(id = R.drawable.ic_empty_profile_placeholder_large)
                )

                Text(
                    text = uiState.drawerDisplayName ?: "",
                    color = MaterialTheme.colors.onSecondary,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(top = 7.dp, bottom = 10.dp))
            }
        }



        Column(
            modifier = Modifier
                .layoutId(MappingDrawerConstraintItem.BottomSection.layoutId),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(space = 1.5.dp, alignment = Alignment.Top)) {




            DrawerContentButtonItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                iconId = R.drawable.ic_emergency_call,
                buttonText = "Emergency Call",
                onClick = onClickEmergencyCall)

            DrawerContentButtonItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                iconId = R.drawable.ic_setting,
                buttonText = "Settings",
                onClick = onClickSettings)

            DrawerContentButtonItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                iconId = R.drawable.ic_ride_history,
                buttonText = "Ride History",
                onClick = onClickRideHistory)

            DrawerContentButtonItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                iconId = R.drawable.ic_group_35,
                buttonText = "Sign out",
                onClick = onClickSignOut)



        }


    }

}

@Preview
@Composable
fun MappingDrawerContentPreview() {
    CyclistanceTheme(true) {
        NavigationDrawerContent(
            onClickSettings = {},

            onClickSignOut = {},
            uiState = NavUiState(
                drawerDisplayName = "John Doe",
                drawerPhotoUrl = "https://www.w3schools.com/howto/img_avatar.png"
            )
        )
    }
}



@Composable
private fun DrawerContentButtonItem(
    modifier: Modifier,
    iconId: Int,
    buttonText: String,
    onClick: () -> Unit,
) {

    TextButton(
        colors = ButtonDefaults.textButtonColors(contentColor = Black450),
        onClick = onClick,
        modifier = modifier) {

        Icon(
            painter = painterResource(id = iconId),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(2f, fill = true), tint = Black440)

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = buttonText,
            modifier = Modifier
                .weight(10f),
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.onBackground

        )


    }
}


