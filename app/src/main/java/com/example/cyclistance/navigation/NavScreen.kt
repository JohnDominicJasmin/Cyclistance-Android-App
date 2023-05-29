package com.example.cyclistance.navigation

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cyclistance.core.utils.composable_utils.ComposableLifecycle
import com.example.cyclistance.core.utils.constants.NavigationConstants.BOTTOM_SHEET_TYPE
import com.example.cyclistance.core.utils.constants.NavigationConstants.CANCELLATION_TYPE
import com.example.cyclistance.core.utils.constants.NavigationConstants.CLIENT_ID
import com.example.cyclistance.core.utils.constants.NavigationConstants.LATITUDE
import com.example.cyclistance.core.utils.constants.NavigationConstants.LONGITUDE
import com.example.cyclistance.core.utils.constants.NavigationConstants.TRANSACTION_ID
import com.example.cyclistance.feature_authentication.domain.util.findActivity
import com.example.cyclistance.feature_mapping.data.local.network_observer.NetworkConnectivityChecker
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.MappingViewModel
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components.DefaultTopBar
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components.MappingDrawerContent
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components.TitleTopAppBar
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components.TopAppBarCreator
import com.example.cyclistance.feature_settings.presentation.setting_edit_profile.EditProfileViewModel
import com.example.cyclistance.feature_settings.presentation.setting_edit_profile.event.EditProfileEvent
import com.example.cyclistance.feature_settings.presentation.setting_edit_profile.event.EditProfileVmEvent
import com.example.cyclistance.feature_settings.presentation.setting_screen.SettingEvent
import com.example.cyclistance.feature_settings.presentation.setting_screen.SettingUiEvent
import com.example.cyclistance.feature_settings.presentation.setting_screen.SettingViewModel
import com.example.cyclistance.navigation.state.NavUiState
import com.example.cyclistance.theme.Black900
import com.example.cyclistance.theme.CyclistanceTheme
import com.example.cyclistance.theme.White50
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NavScreen(
    settingViewModel: SettingViewModel = hiltViewModel(),
    mappingViewModel: MappingViewModel = hiltViewModel(),
    navViewModel: NavViewModel = hiltViewModel(),
    editProfileViewModel: EditProfileViewModel = hiltViewModel()
) {


    var navUiState by rememberSaveable { mutableStateOf(NavUiState()) }
    val navController = rememberNavController()
    val context = LocalContext.current
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val scaffoldState = rememberScaffoldState(drawerState = rememberDrawerState(initialValue = DrawerValue.Closed))
    val coroutineScope = rememberCoroutineScope()
    val mappingState by mappingViewModel.state.collectAsStateWithLifecycle()
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
            navController.navigateScreen(
                Screens.SettingScreen.route)
        }
    }

    val onClickRescueRequest = remember(scaffoldState.drawerState) {
        {
            coroutineScope.launch {
                scaffoldState.drawerState.close()
            }
            navController.navigateScreen(
                Screens.RescueRequestScreen.route)
        }
    }

    val onClickChat = remember {
        {
            coroutineScope.launch {
                scaffoldState.drawerState.close()
            }
            Unit
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

    val respondentCount by remember(mappingState.newRescueRequest?.request?.size) {
        derivedStateOf { (mappingState.newRescueRequest?.request ?: emptyList()).size }
    }


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
                        respondentCount = respondentCount,
                        onClickSettings = onClickSettings,
                        onClickRescueRequest = onClickRescueRequest,
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
                        isDarkTheme = settingState.isDarkTheme,
                        mappingViewModel = mappingViewModel,
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

@Composable
private fun TopAppBar(
    onClickArrowBackIcon: () -> Unit = {},
    onClickMenuIcon: () -> Unit = {},
    isNavigating: Boolean,
    route: String?) {

    when (route) {
        Screens.MappingScreen.route + "?$BOTTOM_SHEET_TYPE={$BOTTOM_SHEET_TYPE}" -> {
            AnimatedVisibility(
                visible = isNavigating.not(),
                enter = fadeIn(initialAlpha = 0.4f),
                exit = fadeOut(animationSpec = tween(durationMillis = 100))) {

                DefaultTopBar(onClickIcon = onClickMenuIcon)
            }
        }

        "${Screens.CancellationScreen.route}/{$CANCELLATION_TYPE}/{$TRANSACTION_ID}/{$CLIENT_ID}" -> {
            TopAppBarCreator(
                icon = Icons.Default.ArrowBack,
                onClickIcon = onClickArrowBackIcon,
                topAppBarTitle = {
                    TitleTopAppBar(title = "Cancellation Reason")
                })
        }

        Screens.ConfirmDetailsScreen.route + "?$LATITUDE={$LATITUDE}&$LONGITUDE={$LONGITUDE}" -> {
            TopAppBarCreator(
                icon = Icons.Default.ArrowBack,
                onClickIcon = onClickArrowBackIcon,
                topAppBarTitle = {
                    TitleTopAppBar(
                        title = "Confirmation Details")
                })
        }


        Screens.RescueRequestScreen.route -> {
            TopAppBarCreator(
                icon = Icons.Default.ArrowBack,
                onClickIcon = onClickArrowBackIcon,
                topAppBarTitle = {
                    TitleTopAppBar(title = "Rescue Requests")
                })
        }

        Screens.ChangePasswordScreen.route -> {
            TopAppBarCreator(
                icon = Icons.Default.ArrowBack,
                onClickIcon = onClickArrowBackIcon,
                topAppBarTitle = {
                    TitleTopAppBar(title = "Change Password")
                })
        }

        Screens.EditProfileScreen.route -> {
            TopAppBarCreator(
                icon = Icons.Default.ArrowBack,
                onClickIcon = onClickArrowBackIcon,
                topAppBarTitle = {
                    TitleTopAppBar(title = "Edit Profile")
                })
        }

        Screens.SettingScreen.route -> {
            TopAppBarCreator(
                icon = Icons.Default.ArrowBack,
                onClickIcon = onClickArrowBackIcon,
                topAppBarTitle = {
                    TitleTopAppBar(title = "Setting Screen")
                })
        }
    }

}


@Composable
private fun NoInternetStatusBar(internetAvailable: Boolean, route: String?) {

    val inShowableScreens =
        route != Screens.SettingScreen.route && route != Screens.IntroSliderScreen.route

    AnimatedVisibility(
        visible = internetAvailable.not() && inShowableScreens,
        enter = slideInVertically(),
        exit = slideOutVertically()) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(Black900)
                .fillMaxWidth()) {

            Text(
                text = "No Connection",
                color = White50,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(vertical = 1.5.dp))
        }
    }

}


@Preview
@Composable
fun TopAppBarPreview() {
    CyclistanceTheme(true) {
        TopAppBar(
            route = Screens.EditProfileScreen.route,
            isNavigating = false)
    }
}

