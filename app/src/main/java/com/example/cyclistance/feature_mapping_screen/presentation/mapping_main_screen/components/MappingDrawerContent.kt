package com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.cyclistance.R
import com.example.cyclistance.theme.Black440
import com.example.cyclistance.theme.CyclistanceTheme


@Composable
fun MappingDrawerContent(
    onClickSettings: () -> Unit = {},
    onClickChat: () -> Unit = {},
    onClickRescueRequest: () -> Unit = {},
    onClickSignOut: () -> Unit = {},
) {

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


                Image(
                    painter = painterResource(R.drawable.person_image),
                    contentDescription = "Profile_Screen",
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(105.dp)
                        .border(
                            width = 3.6.dp,
                            color = MaterialTheme.colors.primary,
                            shape = CircleShape),
                    contentScale = ContentScale.Crop,
                )

                Text(
                    text = "John Doe",
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
                    .background(Color.Transparent)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                iconId = R.drawable.ic_baseline_settings_24,
                buttonText = "Settings",
                onClick = onClickSettings)

            DrawerContentButtonItem(
                modifier = Modifier
                    .background(Color.Transparent)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                iconId = R.drawable.ic_baseline_chat_bubble_outline_24,
                buttonText = "Chat",
                onClick = onClickChat)

            DrawerContentButtonItem(
                modifier = Modifier
                    .background(Color.Transparent)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                iconId = R.drawable.rescue_request,
                buttonText = "Rescue Request",
                onClick = onClickRescueRequest)

            DrawerContentButtonItem(
                modifier = Modifier
                    .background(Color.Transparent)
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
            onClickSignOut = {}
        )
    }
}

@Composable
private fun DrawerContentButtonItem(
    modifier: Modifier,
    iconId: Int,
    buttonText: String,
    onClick: () -> Unit) {


    TextButton(
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
    }
}


