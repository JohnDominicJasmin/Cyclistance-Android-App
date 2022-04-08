package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.activity.compose.BackHandler
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.viewinterop.AndroidView

import com.example.cyclistance.R
import com.mapbox.maps.MapView
import kotlinx.coroutines.launch


@Composable
fun MappingScreen(
    onBackPressed: () -> Unit,
    navigateTo: (destination: String) -> Unit) {

    val scaffoldState = rememberScaffoldState(rememberDrawerState(initialValue = DrawerValue.Closed))
    val coroutineScope = rememberCoroutineScope()

    BackHandler(enabled = true, onBack = onBackPressed)


    Scaffold(
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
        scaffoldState = scaffoldState,
        topBar = {


            TopAppBarCreator(
                icon = Icons.Filled.Menu,
                onClickIcon = {
                    coroutineScope.launch {
                        scaffoldState.drawerState.open()
                    }
                }) {

                DefaultTitleTopAppBar()
            }



        },
        drawerContent = { MappingDrawerContent() }) {

        SetupMapScreen()
    }
}

@Composable
fun SetupMapScreen() {

    AndroidView(
        factory = { context: Context ->
            val view = LayoutInflater.from(context).inflate(R.layout.activity_mapping, null, false)
            val mapView: MapView = view.findViewById(R.id.mapView)
            mapView.getMapboxMap().loadStyleUri(com.mapbox.maps.Style.DARK)
            view
        },
        update = { view ->
            view.visibility = View.VISIBLE

        }
    )

}







