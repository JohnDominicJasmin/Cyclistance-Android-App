package com.example.cyclistance.navigation

import android.content.Intent
import android.content.Intent.ACTION_MAIN
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cyclistance.core.utils.composable_utils.ComposableLifecycle
import com.example.cyclistance.core.utils.network_observer.NetworkConnectivityChecker
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.MappingEvent
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.MappingUiEvent
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.MappingViewModel
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.components.DefaultTopBar
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.components.MappingDrawerContent
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.components.TitleTopAppBar
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

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainScreen(
    settingViewModel: SettingViewModel = hiltViewModel(),
    mappingViewModel: MappingViewModel = hiltViewModel(),
    editProfileViewModel: EditProfileViewModel = hiltViewModel()
) {


    val navController = rememberNavController()
    val context = LocalContext.current
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val scaffoldState = rememberScaffoldState(drawerState = rememberDrawerState(initialValue = DrawerValue.Closed))
    var internetAvailable by rememberSaveable { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()
    val editProfileState by editProfileViewModel.state.collectAsState()
    val mappingState by mappingViewModel.state.collectAsState()
    val settingState by settingViewModel.state.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    ComposableLifecycle{ _, event ->
        when(event){
            Lifecycle.Event.ON_CREATE -> {
                NetworkConnectivityChecker.observe(lifecycleOwner){ isConnected ->
                    isConnected?.let{
                        internetAvailable = isConnected
                    }
                }
            }

            Lifecycle.Event.ON_RESUME -> {
                NetworkConnectivityChecker.checkForConnection()
            }

            else -> {}
        }

    }
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

    val onClickSaveProfile = remember(scaffoldState.drawerState){{
        editProfileViewModel.onEvent(EditProfileEvent.Save)
    }}
    val onClickSettings = remember{{
        coroutineScope.launch {
            scaffoldState.drawerState.close()
        }
        navController.navigateScreen(
            Screens.SettingScreen.route)
    }}
    val onClickRescueRequest = remember(scaffoldState.drawerState){{
        coroutineScope.launch {
            scaffoldState.drawerState.close()
        }
        navController.navigateScreen(
            Screens.RescueRequestScreen.route)
    }}
    val onClickChat = remember{{
        coroutineScope.launch {
            scaffoldState.drawerState.close()
        }
        Intent(ACTION_MAIN).apply {
            flags =
                Intent.FLAG_ACTIVITY_RETAIN_IN_RECENTS or Intent.FLAG_ACTIVITY_NEW_TASK
            addCategory(Intent.CATEGORY_APP_MESSAGING)
        }.also {
            context.startActivity(it)
        }
        Unit
    }}
    val onClickSignOut = remember{{
        coroutineScope.launch {
            scaffoldState.drawerState.close()
        }
        mappingViewModel.onEvent(event = MappingEvent.SignOut)

    }}
    val onToggleTheme = remember{{
        settingViewModel.onEvent(event = SettingEvent.ToggleTheme)
    }}
    val onClickTopBarIcon = remember{{
        navController.navigateScreen(Screens.MappingScreen.route)
    }}

    CyclistanceTheme(darkTheme = settingState.isDarkTheme) {

        Scaffold(
            drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
            scaffoldState = scaffoldState,
            topBar = {
                Column {
                    TopAppBar(
                        scaffoldState = scaffoldState,
                        route = navBackStackEntry?.destination?.route,
                        coroutineScope = coroutineScope,
                        onClickSaveProfile = onClickSaveProfile,
                        editProfileSaveButtonEnabled = editProfileState.isUserInformationChanges(),
                        onClickTopBarIcon = onClickTopBarIcon)
                        NoInternetStatusBar(internetAvailable)
                }
            },
            drawerContent = {
                MappingDrawerContent(
                    state = mappingState,
                    onClickSettings = onClickSettings,
                    onClickRescueRequest = onClickRescueRequest,
                    onClickChat = onClickChat,
                    onClickSignOut = onClickSignOut
                )
            },
        ) { paddingValues ->
            NavGraph(
                navController = navController,
                paddingValues = paddingValues,
                isDarkTheme = settingState.isDarkTheme,
                editProfileViewModel = editProfileViewModel,
                mappingViewModel = mappingViewModel,
                scaffoldState = scaffoldState,
                onToggleTheme = onToggleTheme
            )
        }
    }

}

@Composable
fun TopAppBar(
    onClickTopBarIcon: () -> Unit = {},
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    onClickSaveProfile: () -> Unit = {},
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
                TopAppBarCreator(
                    icon = Icons.Default.ArrowBack,
                    onClickIcon = onClickTopBarIcon,
                    topAppBarTitle = {
                        TitleTopAppBar(title = "Cancellation Reason")
                    })
            }

            Screens.ConfirmDetailsScreen.route -> {
                TopAppBarCreator(
                    icon = Icons.Default.ArrowBack,
                    onClickIcon = onClickTopBarIcon,
                    topAppBarTitle = {
                        TitleTopAppBar(
                            title = "Confirmation Details")
                    })
            }


            Screens.RescueRequestScreen.route -> {
                TopAppBarCreator(
                    icon = Icons.Default.ArrowBack,
                    onClickIcon = onClickTopBarIcon,
                    topAppBarTitle = {
                        TitleTopAppBar(title = "Rescue Requests")
                    })
            }
            Screens.ChangePasswordScreen.route -> {
                TopAppBarCreator(
                    icon = Icons.Default.ArrowBack,
                    onClickIcon = onClickTopBarIcon,
                    topAppBarTitle = {
                        TitleTopAppBar(title = "Change Password")
                    })
            }
            Screens.EditProfileScreen.route -> {
                TopAppBarCreator(
                    icon = Icons.Default.Close, onClickIcon = onClickTopBarIcon, topAppBarTitle = {
                        TitleTopAppBar(
                            title = "Edit Profile",
                            confirmationText = "Save",
                            confirmationTextEnable = editProfileSaveButtonEnabled,
                            onClickConfirmation = onClickSaveProfile)
                    })
            }
            Screens.SettingScreen.route -> {
                TopAppBarCreator(
                    icon = Icons.Default.ArrowBack,
                    onClickIcon = onClickTopBarIcon,
                    topAppBarTitle = {
                        TitleTopAppBar(title = "Setting Screen")
                    })
            }
        }

}


@Composable
fun NoInternetStatusBar(internetAvailable: Boolean) {
    AnimatedVisibility(
        visible = internetAvailable.not(),
        enter = slideInVertically(),
        exit = slideOutVertically()) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(MaterialTheme.colors.background)
                .fillMaxWidth()) {

            Text(
                text = "No Connection",
                color = Color.White,
                fontSize = 14.sp,
                modifier = Modifier.padding(vertical = 2.5.dp))
        }
    }

}



@Preview
@Composable
fun TopAppBarPreview() {
    CyclistanceTheme(true) {
        TopAppBar(route = Screens.MappingScreen.route + "?bottomSheetType={bottomSheetType}")
    }
}

