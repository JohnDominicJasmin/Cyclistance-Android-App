package com.example.cyclistance.feature_mapping.presentation.components

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView

import com.example.cyclistance.R
import com.example.cyclistance.feature_authentication.presentation.theme.BackgroundColor
import com.example.cyclistance.feature_authentication.presentation.theme.TextFieldBackgroundColor
import com.example.cyclistance.feature_authentication.presentation.theme.TextFieldTextColor
import com.mapbox.maps.MapView


@Preview
@Composable
fun MappingScreen() {
    val scaffoldState =
        rememberScaffoldState(rememberDrawerState(initialValue = DrawerValue.Closed))



    Scaffold(
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
        scaffoldState = scaffoldState,
        topBar = {


            TopAppBarCreator(
                icon = Icons.Filled.Close,
                scaffoldState = scaffoldState) {

                DetailsTitleTopAppBar()
            }


        },
        drawerContent = { MappingDrawerContent() },
    ) {
        SetupMapScreen()
    }
}

@Composable
fun SetupMapScreen() {
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








