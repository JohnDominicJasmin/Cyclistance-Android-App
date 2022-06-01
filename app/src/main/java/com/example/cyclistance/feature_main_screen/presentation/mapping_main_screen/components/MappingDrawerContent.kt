package com.example.cyclistance.feature_main_screen.presentation.mapping_main_screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
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
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.cyclistance.R
import com.example.cyclistance.feature_settings.presentation.setting_edit_profile.components.ProfilePictureArea


@Preview(showSystemUi = true)
@Composable
fun MappingDrawerContent() {

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        constraintSet = drawerMappingConstraintSet) {

        Box(
            modifier = Modifier
                .layoutId(MappingConstraintItem.UpperSection.layoutId)
                .background(Color(0xFF3F3F3F))) {


            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally) {


                ProfilePictureArea(modifier = Modifier.size(105.dp))

                Text(
                    text = "John Doe",
                    color = Color.White,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(top = 7.dp, bottom = 0.5.dp))


                Text(
                    text = "johndoe@gmail.com",
                    color = Color(0xFFA5A5A5),
                    style = MaterialTheme.typography.subtitle2)

                Spacer(modifier = Modifier.height(15.dp))



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
                buttonText = "Settings") {  }

            DrawerContentButtonItem(
                modifier = Modifier
                    .background(Color.Transparent)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                iconId = R.drawable.ic_baseline_chat_bubble_outline_24,
                buttonText = "Chat") {  }

            DrawerContentButtonItem(
                modifier = Modifier
                    .background(Color.Transparent)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                iconId = R.drawable.ic_baseline_notifications_none_24,
                buttonText = "Notifications") {   }


            DrawerContentButtonItem(
                modifier = Modifier
                    .background(Color.Transparent)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                iconId = R.drawable.ic_group_35,
                buttonText = "Sign out") {   }
        }


    }

}


@Composable
fun DrawerContentButtonItem(
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
                .size(26.dp), tint = Color(0xFFA6A6A6))


        Text(
            text = buttonText,
            modifier = Modifier
                .weight(10f)
                .padding(start = 20.dp),
            style = MaterialTheme.typography.subtitle1,
            color = Color.White

        )


    }


}