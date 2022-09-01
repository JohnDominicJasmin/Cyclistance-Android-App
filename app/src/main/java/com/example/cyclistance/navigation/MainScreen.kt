package com.example.cyclistance.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.DrawerValue
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LiveData
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cyclistance.feature_main_screen.presentation.mapping_main_screen.components.DefaultTopBar
import com.example.cyclistance.feature_main_screen.presentation.mapping_main_screen.components.DetailsTitleTopAppBar
import com.example.cyclistance.feature_main_screen.presentation.mapping_main_screen.components.TopAppBarCreator
import com.example.cyclistance.feature_settings.presentation.setting_main_screen.SettingEvent
import com.example.cyclistance.feature_settings.presentation.setting_main_screen.SettingViewModel
import com.example.cyclistance.theme.CyclistanceTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainScreen(
    settingViewModel: SettingViewModel = hiltViewModel(),
) {


    val navController = rememberNavController()
    val topBarState = rememberSaveable { (mutableStateOf(true)) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val isDarkThemeLiveData: LiveData<Boolean> = settingViewModel.isDarkTheme
    val isDarkThemeState = isDarkThemeLiveData.observeAsState(initial = isSystemInDarkTheme())

    val scaffoldState =
        rememberScaffoldState(rememberDrawerState(initialValue = DrawerValue.Closed))
    val coroutineScope = rememberCoroutineScope()


    CyclistanceTheme(darkTheme = isDarkThemeState.value) {

        Scaffold(
            drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
            scaffoldState = scaffoldState,
            topBar = {
                when (navBackStackEntry?.destination?.route) {

                    Screens.MappingScreen.route -> {
                        DefaultTopBar(onClickIcon = {
                            coroutineScope.launch {
                                scaffoldState.drawerState.open()
                            }
                        })
                    }

                    Screens.CancellationScreen.route -> {
                        TopAppBarCreator(icon = Icons.Default.Close, onClickIcon = {
                            navController.popBackStack()
                        }, topAppBarTitle = {
                            DetailsTitleTopAppBar(title = "Cancellation Reason")
                        })
                    }

                    Screens.ConfirmDetailsScreen.route -> {

                    }
                    Screens.RescueRequestScreen.route -> {

                    }
                    Screens.ChangePasswordScreen.route -> {

                    }
                    Screens.EditProfileScreen.route -> {

                    }
                    Screens.SettingScreen.route -> {

                    }


                }
            }, drawerContent = {

            }) { paddingValues ->
            NavGraph(
                navController = navController,
                paddingValues = paddingValues,
                isDarkThemeLiveData = isDarkThemeLiveData,
                isDarkThemeState = isDarkThemeState,
                onToggleTheme = {
                    settingViewModel.onEvent(event = SettingEvent.ToggleTheme)
                }
            )
        }
    }
}