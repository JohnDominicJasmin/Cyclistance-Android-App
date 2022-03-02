package com.example.cyclistance.feature_mapping.presentation.components

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.compose.material.*
import androidx.compose.runtime.Composable

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView

import com.example.cyclistance.R
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
            SetupTopAppBar(scaffoldState)


        },
        drawerContent = { MappingDrawerContent() },
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






