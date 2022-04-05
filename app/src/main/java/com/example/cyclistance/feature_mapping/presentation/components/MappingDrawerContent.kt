package com.example.cyclistance.feature_mapping.presentation.components

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
import com.example.cyclistance.feature_authentication.presentation.theme.BackgroundColor
import com.example.cyclistance.feature_authentication.presentation.theme.ThemeColor

@Composable
fun MappingDrawerContent() {

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor),
        constraintSet = drawerMappingConstraintSet) {

        Box(
            modifier = Modifier
                .layoutId(MappingConstraintItem.UpperSection.layoutId)
                .background(Color(0xFF3F3F3F))) {


            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally) {


                Image(
                    painter = painterResource(R.drawable.mike_tyson),
                    contentDescription = "User Picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .border(2.dp, ThemeColor, CircleShape)
                )

                Text(
                    text = "John Doe",
                    color = Color.White,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(top = 7.dp, bottom = 0.5.dp))


                Text(
                    text = "johndoe@gmail.com",
                    color = Color(0xFFA5A5A5),
                    style = MaterialTheme.typography.subtitle2)

                Spacer(modifier = Modifier.height(7.dp))

                Button(
                    onClick = { /*TODO*/ },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = ThemeColor,
                        contentColor = Color(0xFF424242)),
                    shape = RoundedCornerShape(17.dp),
                    modifier = Modifier.wrapContentSize()) {

                    Row(
                        modifier = Modifier
                            .wrapContentWidth()
                            .height(20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(
                            space = 10.dp, alignment = Alignment.CenterHorizontally)) {
                        Text(
                            text = "Edit Profile",
                            color = Color(0xFF424242),
                            style = MaterialTheme.typography.button,
                            textAlign = TextAlign.Center)

                        Icon(
                            modifier = Modifier.size(12.dp),
                            painter = painterResource(id = R.drawable.ic_right_arrow),
                            contentDescription = "Edit Profile",
                            tint = Color(0xFF424242))
                    }
                }
                Spacer(modifier = Modifier.height(7.dp))


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

@Preview
@Composable
fun EditProfileButtonPreview() {
    Button(
        onClick = { /*TODO*/ },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = ThemeColor,
            contentColor = Color(0xFF424242)),
        shape = RoundedCornerShape(17.dp),
        modifier = Modifier.wrapContentSize()) {
        Row(
            modifier = Modifier.wrapContentSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                space = 10.dp, alignment = Alignment.CenterHorizontally)) {
            Text(
                text = "Edit Profile",
                color = Color(0xFF424242),
                style = MaterialTheme.typography.caption,
                textAlign = TextAlign.Center)

            Icon(
                modifier = Modifier.size(10.dp),
                painter = painterResource(id = R.drawable.ic_right_arrow),
                contentDescription = "Edit Profile",
                tint = Color(0xFF424242))
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