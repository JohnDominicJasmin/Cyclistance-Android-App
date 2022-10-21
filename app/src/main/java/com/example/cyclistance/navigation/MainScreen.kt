package com.example.cyclistance.navigation

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.MappingEvent
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.MappingUiEvent
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.MappingViewModel
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.components.DefaultTopBar
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.components.TitleTopAppBar
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.components.MappingDrawerContent
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.components.TopAppBarCreator
import com.example.cyclistance.feature_settings.presentation.setting_edit_profile.EditProfileEvent
import com.example.cyclistance.feature_settings.presentation.setting_edit_profile.EditProfileViewModel
import com.example.cyclistance.feature_settings.presentation.setting_edit_profile.utils.isUserInformationChanges
import com.example.cyclistance.feature_settings.presentation.setting_main_screen.SettingEvent
import com.example.cyclistance.feature_settings.presentation.setting_main_screen.SettingViewModel
import com.example.cyclistance.theme.CyclistanceTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainScreen(
    settingViewModel: SettingViewModel = hiltViewModel(),
    mappingViewModel: MappingViewModel = hiltViewModel(),
    editProfileViewModel: EditProfileViewModel = hiltViewModel()
) {


    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val isDarkTheme by settingViewModel.isDarkTheme.collectAsState(initial = false)
    val scaffoldState = rememberScaffoldState(drawerState = rememberDrawerState(initialValue = DrawerValue.Closed))
    val coroutineScope = rememberCoroutineScope()
    val editProfileState by editProfileViewModel.state.collectAsState()
    val mappingState by mappingViewModel.state.collectAsState()

    LaunchedEffect(key1 = true) {
        mappingViewModel.onEvent(event = MappingEvent.LoadUserProfile)
        mappingViewModel.eventFlow.collectLatest { event ->
            when (event) {
                is MappingUiEvent.ShowSignInScreen -> {
                    navController.popBackStack()
                    navController.navigate(Screens.SignInScreen.route)
                }
                else -> {}
            }
        }
    }



    CyclistanceTheme(darkTheme = isDarkTheme) {

        Scaffold(
            drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
            scaffoldState = scaffoldState,
            topBar = {
                TopAppBar(
                    navController = navController,
                    scaffoldState = scaffoldState,
                    route = navBackStackEntry?.destination?.route,
                    coroutineScope = coroutineScope,
                    onClickSaveProfile = {
                        editProfileViewModel.onEvent(EditProfileEvent.Save)
                    },
                editProfileSaveButtonEnabled = editProfileState.isUserInformationChanges())
            },
            drawerContent = {
                MappingDrawerContent(
                    state = mappingState,
                    onClickSettings = {
                        coroutineScope.launch {
                            scaffoldState.drawerState.close()
                            navController.navigateScreen(
                                Screens.SettingScreen.route,
                                Screens.MappingScreen.route)
                        }
                    },
                    onClickRescueRequest = {
                        coroutineScope.launch {
                            scaffoldState.drawerState.close()
                        }
                    },
                    onClickChat = {
                        coroutineScope.launch {
                            scaffoldState.drawerState.close()
                        }
                    },
                    onClickSignOut = {
                        coroutineScope.launch {
                            scaffoldState.drawerState.close()
                        }
                        mappingViewModel.onEvent(event = MappingEvent.SignOut)

                    }
                )
            },
        ) { paddingValues ->
            NavGraph(
                navController = navController,
                paddingValues = paddingValues,
                isDarkTheme = isDarkTheme,
                editProfileViewModel = editProfileViewModel,
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
    onClickSaveProfile: () -> Unit,
    editProfileSaveButtonEnabled: Boolean = false,
    route: String?) {


    when (route) {
        Screens.MappingScreen.route + "?bottomSheetType={bottomSheetType}" -> {

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
                    TitleTopAppBar(
                        title = "Edit Profile",
                        confirmationText = "Save",
                        confirmationTextEnable = editProfileSaveButtonEnabled,
                        onClickConfirmation = onClickSaveProfile)
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