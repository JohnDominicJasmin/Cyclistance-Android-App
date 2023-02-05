package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.cyclistance.R
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.MappingState
import com.example.cyclistance.theme.Black440
import com.example.cyclistance.theme.Black450
import com.example.cyclistance.theme.CyclistanceTheme
import com.example.cyclistance.theme.Red710


@Composable
fun MappingDrawerContent(
    state: MappingState = MappingState(),
    onClickSettings: () -> Unit = {},
    onClickChat: () -> Unit = {},
    onClickRescueRequest: () -> Unit = {},
    onClickSignOut: () -> Unit = {},
) {

    val respondents = remember(state.userActiveRescueRequests.respondents.size) {
        state.userActiveRescueRequests.respondents
    }


    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        constraintSet = drawerMappingConstraintSet) {

        Box(
            modifier = Modifier
                .layoutId(MappingConstraintItem.UpperSection.layoutId)
                .background(MaterialTheme.colors.secondaryVariant)) {


            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally) {


                AsyncImage(
                    model = state.photoUrl, contentDescription = "User Profile Image",
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(105.dp)
                        .border(
                            width = 3.6.dp,
                            color = MaterialTheme.colors.primary,
                            shape = CircleShape),
                    contentScale = ContentScale.Crop,
                    placeholder = rememberAsyncImagePainter(model = R.drawable.ic_empty_profile_placeholder_large),
                    error = painterResource(id = R.drawable.ic_empty_profile_placeholder_large),
                    fallback = painterResource(id = R.drawable.ic_empty_profile_placeholder_large)
                )

                Text(
                    text = state.name,
                    color = MaterialTheme.colors.onSecondary,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(top = 7.dp, bottom = 10.dp))


            }
        }



        Column(
            modifier = Modifier
                .layoutId(MappingConstraintItem.BottomSection.layoutId),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(space = 1.5.dp, alignment = Alignment.Top)) {


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
                iconId = R.drawable.ic_baseline_chat_bubble_outline_24,
                buttonText = "Chat",
                onClick = onClickChat)

            DrawerContentButtonItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                iconId = R.drawable.ic_rescue_request,
                buttonText = "Rescue Request",
                onClick = onClickRescueRequest,
                badgeCountEnabled = true,
                badgeCount = respondents.size
                )

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
        MappingDrawerContent(
            onClickSettings = {},
            onClickChat = {},
            onClickSignOut = {},
        )
    }
}

@Composable
private fun DrawerContentButtonItem(
    modifier: Modifier,
    iconId: Int,
    badgeCountEnabled: Boolean = false,
    buttonText: String,
    onClick: () -> Unit,
    badgeCount: Int = -1) {

    TextButton(
        colors = ButtonDefaults.textButtonColors(contentColor = Black450),
        onClick = onClick,
        modifier = modifier) {

        Icon(
            painter = painterResource(id = iconId),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(1f)
                .size(26.dp), tint = Black440)


        Text(
            text = buttonText,
            modifier = Modifier
                .weight(10f)
                .padding(start = 20.dp),
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.onBackground

        )

        AnimatedVisibility(visible = badgeCountEnabled, enter = fadeIn(), exit = fadeOut()) {
            BadgeCount(
                modifier = Modifier.padding(end = 5.dp),
                count  = badgeCount.toString()
            )
        }
    }
}

@Composable
fun BadgeCount(modifier: Modifier = Modifier, count: String) {

    Surface(
        shape = CircleShape,
        color = Red710,
        contentColor = Color.White,
        modifier = modifier.size(22.dp)) {

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = count,
                textAlign = TextAlign.Center,
                fontSize = 14.sp)
        }
    }

}

