package com.myapp.cyclistance.navigation

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.DrawerValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.myapp.cyclistance.core.network.ConnectivityObserver
import com.myapp.cyclistance.core.network.NetworkConnectivityObserver
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.drawer.NavigationDrawerContent
import com.myapp.cyclistance.feature_settings.presentation.setting_screen.SettingViewModel
import com.myapp.cyclistance.feature_settings.presentation.setting_screen.event.SettingEvent
import com.myapp.cyclistance.feature_settings.presentation.setting_screen.event.SettingVmEvent
import com.myapp.cyclistance.feature_user_profile.presentation.edit_profile.EditProfileViewModel
import com.myapp.cyclistance.feature_user_profile.presentation.edit_profile.event.EditProfileEvent
import com.myapp.cyclistance.feature_user_profile.presentation.edit_profile.event.EditProfileVmEvent
import com.myapp.cyclistance.navigation.components.NoInternetStatusBar
import com.myapp.cyclistance.navigation.components.TopAppBar
import com.myapp.cyclistance.navigation.event.NavEvent
import com.myapp.cyclistance.navigation.event.NavUiEvent
import com.myapp.cyclistance.navigation.event.NavVmEvent
import com.myapp.cyclistance.navigation.nav_graph.NavGraph
import com.myapp.cyclistance.navigation.nav_graph.navigateScreen
import com.myapp.cyclistance.navigation.nav_graph.navigateScreenInclusively
import com.myapp.cyclistance.navigation.state.NavUiState
import com.myapp.cyclistance.theme.CyclistanceTheme
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
    val scaffoldState =
        rememberScaffoldState(drawerState = rememberDrawerState(initialValue = DrawerValue.Closed))
    val coroutineScope = rememberCoroutineScope()
    val settingState by settingViewModel.state.collectAsStateWithLifecycle()
    val editProfileState by editProfileViewModel.state.collectAsStateWithLifecycle()
    val navState by navViewModel.state.collectAsStateWithLifecycle()
    val connectivityObserver = NetworkConnectivityObserver(context.applicationContext)
    val onChangeNavigatingState = remember {
        { navigating: Boolean ->
            navUiState = navUiState.copy(
                isNavigating = navigating
            )
        }
    }

    val status by connectivityObserver.observe().collectAsState(
        initial = ConnectivityObserver.Status.Unavailable
    )


    BackHandler(enabled = scaffoldState.drawerState.isOpen, onBack = {
        coroutineScope.launch {
            scaffoldState.drawerState.close()
        }
    })


    LaunchedEffect(key1 = status){
         navUiState = navUiState.copy(internetStatus = status)
    }

    LaunchedEffect(true) {
        navViewModel.event.collectLatest { event ->
            when (event) {
                NavEvent.DeleteMessagingTokenSuccess -> {
                    settingViewModel.onEvent(event = SettingVmEvent.SignOut)
                }

                is NavEvent.DeleteMessagingTokenFailure -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }



    LaunchedEffect(key1 = true) {

        editProfileViewModel.eventFlow.distinctUntilChanged().collect { event ->
            when (event) {
                is EditProfileEvent.GetNameSuccess -> {
                    navUiState = navUiState.copy(drawerDisplayName = event.name)
                }

                is EditProfileEvent.GetPhotoUrlSuccess -> {
                    navUiState = navUiState.copy(drawerPhotoUrl = event.photoUrl)
                }

                else -> {}
            }
        }
    }

    LaunchedEffect(key1 = true) {

        settingViewModel.eventFlow.collectLatest { event ->
            when (event) {

                is SettingEvent.SignOutSuccess -> {

                    navController.navigateScreenInclusively(
                        Screens.AuthenticationNavigation.ROUTE,
                        Screens.MappingNavigation.ROUTE)
                }

                is SettingEvent.SignOutFailed -> {
                    Toast.makeText(context, "Failed to Sign out account", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    val closeDrawer = remember {{
        coroutineScope.launch {
            scaffoldState.drawerState.close()
        }
    }}

    val openDrawer = remember{{
        coroutineScope.launch {
            scaffoldState.drawerState.open()
        }
    }}

    val onClickSettings = remember {
        {
            closeDrawer()
            navController.navigate(
                Screens.SettingsNavigation.ROUTE)
        }
    }

    val onClickSignOut = remember {
        {
            navViewModel.onEvent(NavVmEvent.DeleteMessagingToken)
            closeDrawer()
            Unit
        }
    }

    val onClickEmergencyCall = remember {
        {
            closeDrawer()
            navController.navigateScreen(Screens.EmergencyCallNavigation.ROUTE)
        }
    }

    val onClickRideHistory = remember {
        {
            if(editProfileState.userId.isEmpty()){
                Toast.makeText(context, "Ride History not available", Toast.LENGTH_SHORT).show()
            }else{
                closeDrawer()
                navController.navigateScreen(Screens.RescueRecordNavigation.RideHistory.passArgument(rideHistoryUid = editProfileState.userId))
            }

        }
    }

    val onToggleTheme = remember {
        {
            settingViewModel.onEvent(event = SettingVmEvent.ToggleTheme)
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
            openDrawer()
            editProfileViewModel.onEvent(event = EditProfileVmEvent.LoadProfile)
            Unit
        }
    }


    val openUserProfile = remember(editProfileState.userId){{
        if(editProfileState.userId.isEmpty()){
            Toast.makeText(context, "User Profile not available", Toast.LENGTH_SHORT).show()
        }else{
            closeDrawer()
            navController.navigate(Screens.UserProfileNavigation.UserProfile.passArgument(userId = editProfileState.userId))
        }
    }}






    CompositionLocalProvider(IsDarkTheme provides settingState.isDarkTheme) {

        CyclistanceTheme(darkTheme = settingState.isDarkTheme) {

            Surface(
                modifier = Modifier.fillMaxSize()) {

                Scaffold(
                    drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
                    scaffoldState = scaffoldState,
                    topBar = {

                        Surface(elevation = 10.dp, modifier = Modifier.fillMaxWidth()) {

                            Column {

                                TopAppBar(
                                    route = navBackStackEntry?.destination?.route,
                                    onClickMenuIcon = onClickMenuIcon,
                                    onClickArrowBackIcon = onClickArrowBackIcon,
                                    uiState = navUiState)

                                NoInternetStatusBar(
                                    internetStatus = status,
                                    route = navBackStackEntry?.destination?.route)

                            }
                        }
                    },
                    drawerContent = {
                        NavigationDrawerContent(
                            onClickSettings = onClickSettings,
                            onClickSignOut = onClickSignOut,
                            onClickEmergencyCall = onClickEmergencyCall,
                            onClickRideHistory = onClickRideHistory,
                            onClickUserProfile = openUserProfile,
                            uiState = navUiState
                        )
                    },
                ) { paddingValues ->
                    navState.navigationStartingDestination?.let {
                        NavGraph(
                            uiState = navUiState.copy(startingDestination = it),
                            navController = navController,
                            paddingValues = paddingValues,
                            event = { event ->
                                when (event) {
                                    is NavUiEvent.OnChangeNavigation -> onChangeNavigatingState(event.isNavigating)
                                    is NavUiEvent.OnToggleTheme -> onToggleTheme()
                                }
                            }
                        )
                    }
                }
            }
        }
    }

}

