package com.example.cyclistance.feature_mapping.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.layout.ContentScale.Companion.Crop

import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout

import com.example.cyclistance.feature_authentication.presentation.theme.TopAppBarBackgroundColor
import kotlinx.coroutines.launch
import com.example.cyclistance.R
import com.example.cyclistance.feature_authentication.presentation.theme.BackgroundColor
import com.example.cyclistance.feature_authentication.presentation.theme.ThemeColor
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.maps.MapView


@Preview
@Composable
fun MappingScreen() {
    val scaffoldState =
        rememberScaffoldState(rememberDrawerState(initialValue = DrawerValue.Closed))



    Scaffold(
        drawerGesturesEnabled = false,
        scaffoldState = scaffoldState,
        topBar = {
            SetupTopAppBar(scaffoldState)


        },
        drawerContent = { SetupDrawerContent() },
    ) {


        AndroidView(
            factory = { context: Context ->
                val view =
                    LayoutInflater.from(context).inflate(R.layout.activity_mapping, null, false)
                val mapView: MapView = view.findViewById(R.id.mapView)
                mapView.getMapboxMap().loadStyleUri(com.mapbox.maps.Style.DARK)
                view
            },
            update = { view ->
                view.visibility = View.VISIBLE

            }
        )

    }
}

@Preview
@Composable
fun SetupDrawerContent() {


    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor), constraintSet = drawerMappingConstraintSet) {

        Box(
            modifier = Modifier
                .layoutId(MappingDrawerConstraintItem.UpperSection.layoutId)
                .background(Color(0xFF3F3F3F))) {


            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {


                Image(
                    painter = painterResource(R.drawable.mike_tyson),
                    contentDescription = "User Picture",
                    contentScale = Crop,
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

            }
        }



        Column(
            modifier = Modifier
                .layoutId(MappingDrawerConstraintItem.BottomSection.layoutId),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(space = 1.5.dp)
        ) {

            DrawerContentButton(
                modifier = Modifier
                    .background(Transparent)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                iconId = R.drawable.ic_baseline_person_outline_24,
                buttonText = "Profile") { /* todo onClick here */ }

            DrawerContentButton(
                modifier = Modifier
                    .background(Transparent)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                iconId = R.drawable.ic_baseline_settings_24,
                buttonText = "Settings") { /* todo onClick here */ }

            DrawerContentButton(
                modifier = Modifier
                    .background(Transparent)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                iconId = R.drawable.ic_baseline_chat_bubble_outline_24,
                buttonText = "Chat") { /* todo onClick here */ }

            DrawerContentButton(
                modifier = Modifier
                    .background(Transparent)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                iconId = R.drawable.ic_baseline_notifications_none_24,
                buttonText = "Notifications") { /* todo onClick here */ }

            DrawerContentButton(
                modifier = Modifier
                    .background(Transparent)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                iconId = R.drawable.ic_about,
                buttonText = "About") { /* todo onClick here */ }




            DrawerContentButton(
                modifier = Modifier
                    .background(Transparent)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                iconId = R.drawable.ic_group_35,
                buttonText = "Sign out") { /* todo onClick here */ }
        }


    }


}


@Composable
fun DrawerContentButton(
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

@Composable
fun SetupTopAppBar(scaffoldState: ScaffoldState) {
    val coroutineScope = rememberCoroutineScope()
    TopAppBar(
        elevation = 10.dp,
        title = {
            Row(
                modifier = Modifier.wrapContentSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)) {

                Image(
                    painter = painterResource(id = R.drawable.ic_cyclistance_app_icon),
                    contentDescription = "Image Icon",
                    alignment = Alignment.Center,
                    modifier = Modifier
                        .size(30.dp)
                        .align(alignment = Alignment.CenterVertically)
                        .padding(top = 5.dp)
                )
                Text(
                    "Cyclistance",
                    color = Color.White,
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp)
            }
        },
        backgroundColor = TopAppBarBackgroundColor,
        navigationIcon = {
            IconButton(onClick = { coroutineScope.launch { scaffoldState.drawerState.open() } }
            ) {
                Icon(Icons.Filled.Menu, contentDescription = "Menu Icon", tint = Color.White)
            }
        })

}

