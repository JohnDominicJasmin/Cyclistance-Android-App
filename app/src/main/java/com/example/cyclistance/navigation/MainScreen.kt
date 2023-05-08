package com.example.cyclistance.navigation

import android.content.Intent
import android.content.Intent.ACTION_MAIN
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
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
import com.example.cyclistance.core.utils.constants.NavigationConstants.BOTTOM_SHEET_TYPE
import com.example.cyclistance.core.utils.constants.NavigationConstants.CANCELLATION_TYPE
import com.example.cyclistance.core.utils.constants.NavigationConstants.CLIENT_ID
import com.example.cyclistance.core.utils.constants.NavigationConstants.LATITUDE
import com.example.cyclistance.core.utils.constants.NavigationConstants.LONGITUDE
import com.example.cyclistance.core.utils.constants.NavigationConstants.TRANSACTION_ID
import com.example.cyclistance.feature_mapping.data.local.network_observer.NetworkConnectivityChecker
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.MappingEvent
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.MappingUiEvent
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.MappingViewModel
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components.DefaultTopBar
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components.MappingDrawerContent
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components.TitleTopAppBar
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components.TopAppBarCreator
import com.example.cyclistance.feature_settings.presentation.setting_edit_profile.EditProfileEvent
import com.example.cyclistance.feature_settings.presentation.setting_edit_profile.EditProfileViewModel
import com.example.cyclistance.feature_settings.presentation.setting_edit_profile.utils.isUserInformationChanges
import com.example.cyclistance.feature_settings.presentation.setting_screen.SettingEvent
import com.example.cyclistance.feature_settings.presentation.setting_screen.SettingViewModel
import com.example.cyclistance.theme.Black900
import com.example.cyclistance.theme.CyclistanceTheme
import com.example.cyclistance.theme.White50
import com.google.accompanist.permissions.ExperimentalPermissionsApi
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
    var isNavigating by rememberSaveable{ mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val editProfileState by editProfileViewModel.state.collectAsState()
    val mappingState by mappingViewModel.state.collectAsState()
    val settingState by settingViewModel.state.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    val onChangeNavigatingState = remember{{ navigating: Boolean ->
        isNavigating = navigating
    }}


    ComposableLifecycle { _, event ->
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
                NetworkConnectivityChecker.observe(lifecycleOwner) { isConnected ->
                    isConnected?.let {
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
                is MappingUiEvent.SignOutSuccess -> {
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
    val onClickArrowBackIcon = remember{{
        navController.navigateScreen(Screens.MappingScreen.route)
    }}
    val onClickMenuIcon = remember{{
        coroutineScope.launch {
            scaffoldState.drawerState.open()
        }
        Unit
    }}

    CyclistanceTheme(darkTheme = settingState.isDarkTheme) {

        Scaffold(
            drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
            scaffoldState = scaffoldState,
            topBar = {

                Column {
                    TopAppBar(
                        route = navBackStackEntry?.destination?.route,
                        onClickMenuIcon = onClickMenuIcon,
                        onClickSaveProfile = onClickSaveProfile,
                        editProfileSaveButtonEnabled = editProfileState.isUserInformationChanges(),
                        onClickArrowBackIcon = onClickArrowBackIcon,
                        isNavigating = isNavigating)

                    NoInternetStatusBar(internetAvailable, navBackStackEntry?.destination?.route)
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
                hasInternetConnection = internetAvailable,
                navController = navController,
                paddingValues = paddingValues,
                isDarkTheme = settingState.isDarkTheme,
                editProfileViewModel = editProfileViewModel,
                mappingViewModel = mappingViewModel,
                scaffoldState = scaffoldState,
                onToggleTheme = onToggleTheme,
                isNavigating = isNavigating,
                onChangeNavigatingState = onChangeNavigatingState
            )
        }
    }

}

@Composable
private fun TopAppBar(
    onClickArrowBackIcon: () -> Unit = {},
    onClickMenuIcon: () -> Unit = {},
    onClickSaveProfile: () -> Unit = {},
    editProfileSaveButtonEnabled: Boolean = false,
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
                icon = Icons.Default.Close, onClickIcon = onClickArrowBackIcon, topAppBarTitle = {
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
                fontSize = 13.sp,
                modifier = Modifier.padding(vertical = 1.5.dp))
        }
    }

}


@Preview
@Composable
fun TopAppBarPreview() {
    CyclistanceTheme(true) {
        TopAppBar(
            route = Screens.MappingScreen.route + "?$BOTTOM_SHEET_TYPE={$BOTTOM_SHEET_TYPE}",
            isNavigating = false)
    }
}

