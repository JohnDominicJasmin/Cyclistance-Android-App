package com.example.cyclistance.navigation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cyclistance.feature_main_screen.presentation.mapping_main_screen.MappingEvent
import com.example.cyclistance.feature_main_screen.presentation.mapping_main_screen.MappingUiEvent
import com.example.cyclistance.feature_main_screen.presentation.mapping_main_screen.MappingViewModel
import com.example.cyclistance.feature_main_screen.presentation.mapping_main_screen.components.DefaultTopBar
import com.example.cyclistance.feature_main_screen.presentation.mapping_main_screen.components.TitleTopAppBar
import com.example.cyclistance.feature_main_screen.presentation.mapping_main_screen.components.MappingDrawerContent
import com.example.cyclistance.feature_main_screen.presentation.mapping_main_screen.components.TopAppBarCreator
import com.example.cyclistance.feature_settings.presentation.setting_main_screen.SettingEvent
import com.example.cyclistance.feature_settings.presentation.setting_main_screen.SettingViewModel
import com.example.cyclistance.theme.CyclistanceTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainScreen(
    settingViewModel: SettingViewModel = hiltViewModel(),
    mappingViewModel: MappingViewModel = hiltViewModel(),
) {


    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val isDarkThemeLiveData: LiveData<Boolean?> = settingViewModel.isDarkTheme
    val isDarkThemeState = isDarkThemeLiveData.observeAsState(initial = isSystemInDarkTheme())
    val scaffoldState = rememberScaffoldState(rememberDrawerState(initialValue = DrawerValue.Closed))
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        mappingViewModel.eventFlow.collectLatest { event ->
            when (event) {
                is MappingUiEvent.ShowSignInScreen -> {
                    navController.navigateScreenInclusively(
                        Screens.SignInScreen.route,
                        Screens.MappingScreen.route)
                }
                else -> {}
            }
        }
    }


    CyclistanceTheme(darkTheme = isDarkThemeState.value == true) {

        Scaffold(
            drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
            scaffoldState = scaffoldState,
            topBar = {
                TopAppBar(
                    navController = navController,
                    scaffoldState = scaffoldState,
                    route = navBackStackEntry?.destination?.route,
                    coroutineScope = coroutineScope)
            },
            drawerContent = {
                MappingDrawerContent(
                    onSettingsItemClick = {

                        navController.navigateScreen(
                            Screens.SettingScreen.route,
                            Screens.MappingScreen.route).also {

                            coroutineScope.launch {
                                scaffoldState.drawerState.close()
                            }
                        }
                    },
                    onChatItemClick = {
                        /*  todo: open chat screen here   */
                        coroutineScope.launch {
                            scaffoldState.drawerState.close()
                        }
                    },
                    onSignOutItemClick = {
                        mappingViewModel.onEvent(event = MappingEvent.SignOut).also {
                            coroutineScope.launch {
                                scaffoldState.drawerState.close()
                            }
                        }
                    }
                )
            },
        ) { paddingValues ->
            NavGraph(
                navController = navController,
                paddingValues = paddingValues,
                isDarkThemeLiveData = isDarkThemeLiveData,
                isDarkThemeState = isDarkThemeState,
                scaffoldState = scaffoldState,
                onToggleTheme = {
                    settingViewModel.onEvent(event = SettingEvent.ToggleTheme)
                }
            )
        }
    }
}

@Composable
fun TopAppBar(
    navController: NavController,
    scaffoldState: ScaffoldState,
    coroutineScope: CoroutineScope,
    route: String?) {


    when (route) {
        Screens.MappingScreen.route -> {
            DefaultTopBar(onClickIcon = {
                coroutineScope.launch {
                    scaffoldState.drawerState.open()
                }
            })
        }

        Screens.CancellationScreen.route -> {
            TopAppBarCreator(icon = Icons.Default.ArrowBack, onClickIcon = {
                navController.popBackStack()
            }, topAppBarTitle = {
                TitleTopAppBar(title = "Cancellation Reason")
            })
        }

        Screens.ConfirmDetailsScreen.route -> {
            TopAppBarCreator(icon = Icons.Default.ArrowBack, onClickIcon = {
                navController.popBackStack()
            }, topAppBarTitle = {
                TitleTopAppBar(
                    title = "Confirmation Details")
            })
        }


        Screens.RescueRequestScreen.route -> {
            TopAppBarCreator(icon = Icons.Default.ArrowBack, onClickIcon = {
                navController.popBackStack()
            }, topAppBarTitle = {
                TitleTopAppBar(title = "Rescue Requests")
            })
        }
        Screens.ChangePasswordScreen.route -> {
            TopAppBarCreator(icon = Icons.Default.ArrowBack, onClickIcon = {
                navController.popBackStack()
            }, topAppBarTitle = {
                TitleTopAppBar(title = "Change Password")
            })
        }
        Screens.EditProfileScreen.route -> {
            TopAppBarCreator(
                icon = Icons.Default.Close, onClickIcon = {
                    navController.popBackStack()
                }, topAppBarTitle = {
                    TitleTopAppBar(title = "Edit Profile",
                        confirmationText = "Save",
                        onConfirmationClick = {
                            /*TODO*/
                        })
                })
        }
        Screens.SettingScreen.route -> {
            TopAppBarCreator(icon = Icons.Default.ArrowBack, onClickIcon = {
                navController.popBackStack()
            }, topAppBarTitle = {
                TitleTopAppBar(title = "Setting Screen")
            })
        }


    }
}