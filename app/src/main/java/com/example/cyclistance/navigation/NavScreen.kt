package com.example.cyclistance.navigation

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cyclistance.core.utils.composable_utils.ComposableLifecycle
import com.example.cyclistance.feature_authentication.domain.util.findActivity
import com.example.cyclistance.feature_mapping.data.local.network_observer.NetworkConnectivityChecker
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components.drawer.MappingDrawerContent
import com.example.cyclistance.feature_settings.presentation.setting_edit_profile.EditProfileViewModel
import com.example.cyclistance.feature_settings.presentation.setting_edit_profile.event.EditProfileEvent
import com.example.cyclistance.feature_settings.presentation.setting_edit_profile.event.EditProfileVmEvent
import com.example.cyclistance.feature_settings.presentation.setting_screen.SettingEvent
import com.example.cyclistance.feature_settings.presentation.setting_screen.SettingUiEvent
import com.example.cyclistance.feature_settings.presentation.setting_screen.SettingViewModel
import com.example.cyclistance.navigation.components.NoInternetStatusBar
import com.example.cyclistance.navigation.components.TopAppBar
import com.example.cyclistance.navigation.state.NavUiState
import com.example.cyclistance.theme.CyclistanceTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch


val IsDarkTheme = compositionLocalOf { false }

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NavScreen(
    settingViewModel: SettingViewModel = hiltViewModel(),
    navViewModel: NavViewModel = hiltViewModel(),
    editProfileViewModel: EditProfileViewModel = hiltViewModel()
) {



    var navUiState by rememberSaveable { mutableStateOf(NavUiState()) }
    val navController = rememberNavController()
    val context = LocalContext.current
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val scaffoldState = rememberScaffoldState(drawerState = rememberDrawerState(initialValue = DrawerValue.Closed))
    val coroutineScope = rememberCoroutineScope()
    val settingState by settingViewModel.state.collectAsStateWithLifecycle()
    val navState by navViewModel.state.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current

    val onChangeNavigatingState = remember {
        { navigating: Boolean ->
            navUiState = navUiState.copy(
                isNavigating = navigating
            )
        }
    }


    BackHandler(enabled = true, onBack = {
        coroutineScope.launch {

            if (scaffoldState.drawerState.isOpen) {
                scaffoldState.drawerState.close()
                return@launch
            }

            context.findActivity()?.finish()
        }
    })

    ComposableLifecycle { _, event ->
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
                NetworkConnectivityChecker.observe(lifecycleOwner) { isConnected ->
                    isConnected?.let {
                        navUiState = navUiState.copy(internetAvailable = isConnected)
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

        editProfileViewModel.eventFlow.distinctUntilChanged().collect{ event ->
            when (event) {
                is EditProfileEvent.GetNameSuccess -> {
                    navUiState = navUiState.copy(name = event.name)
                }

                is EditProfileEvent.GetPhotoUrlSuccess -> {
                    navUiState = navUiState.copy(photoUrl = event.photoUrl)
                }

                else -> {}
            }
        }
    }

    LaunchedEffect(key1 = true) {

        settingViewModel.eventFlow.collectLatest { event ->
            when (event) {

                is SettingUiEvent.SignOutSuccess -> {
                    navController.popBackStack()
                    navController.navigate(Screens.SignInScreen.route)
                }

                is SettingUiEvent.SignOutFailed -> {
                    Toast.makeText(context, "Failed to Sign out account", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }


    val onClickSettings = remember {
        {
            coroutineScope.launch {
                scaffoldState.drawerState.close()
            }
            navController.navigate(
                Screens.SettingScreen.route)
        }
    }


    val onClickChat = remember {
        {
            coroutineScope.launch {
                scaffoldState.drawerState.close()
            }
            navController.navigateScreen(Screens.MessagingScreen.route)
        }
    }

    val onClickSignOut = remember {
        {
            coroutineScope.launch {
                scaffoldState.drawerState.close()
            }
            settingViewModel.onEvent(event = SettingEvent.SignOut)
        }
    }

    val onToggleTheme = remember {
        {
            settingViewModel.onEvent(event = SettingEvent.ToggleTheme)
        }
    }

    val onClickArrowBackIcon = remember {
        {
            navController.popBackStack()
            Unit
        }
    }

    val onClickMenuIcon = remember {
        {
            editProfileViewModel.onEvent(event = EditProfileVmEvent.LoadProfile)
            coroutineScope.launch {
                scaffoldState.drawerState.open()
            }
            Unit
        }
    }




    CompositionLocalProvider(IsDarkTheme provides settingState.isDarkTheme) {

        CyclistanceTheme(darkTheme = settingState.isDarkTheme) {

            Surface(
                modifier = Modifier.fillMaxSize()) {

                Scaffold(
                    drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
                    scaffoldState = scaffoldState,
                    topBar = {
                        Column {

                            TopAppBar(
                                route = navBackStackEntry?.destination?.route,
                                onClickMenuIcon = onClickMenuIcon,
                                onClickArrowBackIcon = onClickArrowBackIcon,
                                isNavigating = navUiState.isNavigating)

                            NoInternetStatusBar(
                                navUiState.internetAvailable,
                                navBackStackEntry?.destination?.route)

                        }
                    },
                    drawerContent = {
                        MappingDrawerContent(
                            onClickSettings = onClickSettings,
                            onClickChat = onClickChat,
                            onClickSignOut = onClickSignOut,
                            uiState = navUiState
                        )
                    },
                ) { paddingValues ->
                    navState.navigationStartingDestination?.let {
                        NavGraph(
                            hasInternetConnection = navUiState.internetAvailable,
                            navController = navController,
                            paddingValues = paddingValues,
                            onChangeNavigatingState = onChangeNavigatingState,
                            onToggleTheme = onToggleTheme,
                            isNavigating = navUiState.isNavigating,
                            startingDestination = it
                        )
                    }
                }
            }
        }
    }

}

