package com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.myapp.cyclistance.MainViewModel
import com.myapp.cyclistance.R
import com.myapp.cyclistance.core.domain.model.AlertDialogState
import com.myapp.cyclistance.core.utils.app.AppUtils
import com.myapp.cyclistance.core.utils.connection.ConnectionStatus.checkLocationSetting
import com.myapp.cyclistance.core.utils.connection.ConnectionStatus.hasGPSConnection
import com.myapp.cyclistance.core.utils.connection.ConnectionStatus.hasInternetConnection
import com.myapp.cyclistance.core.utils.constants.MappingConstants
import com.myapp.cyclistance.core.utils.constants.MappingConstants.ACTION_START_FOREGROUND
import com.myapp.cyclistance.core.utils.constants.MappingConstants.ACTION_STOP_FOREGROUND
import com.myapp.cyclistance.core.utils.constants.MappingConstants.DEFAULT_ACTION
import com.myapp.cyclistance.core.utils.constants.MappingConstants.DEFAULT_CAMERA_ANIMATION_DURATION
import com.myapp.cyclistance.core.utils.constants.MappingConstants.DEFAULT_LATITUDE
import com.myapp.cyclistance.core.utils.constants.MappingConstants.DEFAULT_LONGITUDE
import com.myapp.cyclistance.core.utils.constants.MappingConstants.FAST_CAMERA_ANIMATION_DURATION
import com.myapp.cyclistance.core.utils.constants.MappingConstants.LOCATE_USER_ZOOM_LEVEL
import com.myapp.cyclistance.core.utils.constants.MappingConstants.ROUTE_SOURCE_ID
import com.myapp.cyclistance.core.utils.constants.MappingConstants.SELECTION_RESCUEE_TYPE
import com.myapp.cyclistance.core.utils.constants.MappingConstants.SELECTION_RESCUER_TYPE
import com.myapp.cyclistance.core.utils.contexts.callPhoneNumber
import com.myapp.cyclistance.core.utils.contexts.shareLocation
import com.myapp.cyclistance.core.utils.contexts.startLocationServiceIntentAction
import com.myapp.cyclistance.core.utils.json.JsonConverter.toJson
import com.myapp.cyclistance.core.utils.permissions.requestPermission
import com.myapp.cyclistance.core.utils.save_images.ImageUtils
import com.myapp.cyclistance.core.utils.save_images.ImageUtils.toImageUri
import com.myapp.cyclistance.feature_authentication.domain.util.findActivity
import com.myapp.cyclistance.feature_emergency_call.presentation.emergency_call_screen.EmergencyCallViewModel
import com.myapp.cyclistance.feature_emergency_call.presentation.emergency_call_screen.event.EmergencyCallVmEvent
import com.myapp.cyclistance.feature_mapping.domain.model.Role
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.hazardous_lane.HazardousLaneMarkerDetails
import com.myapp.cyclistance.feature_mapping.domain.model.ui.camera.CameraState
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.*
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.event.MappingEvent
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.event.MappingUiEvent
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.event.MappingVmEvent
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.state.MappingUiState
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.utils.BottomSheetType
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.utils.MappingUtils
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.utils.MappingUtils.animateCameraPosition
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.utils.MappingUtils.changeToNormalPuckIcon
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.utils.MappingUtils.openNavigationApp
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.utils.MappingUtils.showRoute
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.utils.MarkerSnippet
import com.myapp.cyclistance.navigation.Screens
import com.myapp.cyclistance.navigation.nav_graph.navigateScreen
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import timber.log.Timber


@SuppressLint("MissingPermission")
@OptIn(ExperimentalMaterialApi::class)
@ExperimentalPermissionsApi
@Composable
fun MappingScreen(
    hasInternetConnection: Boolean,
    mappingViewModel: MappingViewModel = hiltViewModel(),
    emergencyViewModel: EmergencyCallViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel(),
    paddingValues: PaddingValues,
    isNavigating: Boolean,
    onChangeNavigatingState: (isNavigating: Boolean) -> Unit,
    navController: NavController) {


    val context = LocalContext.current
    val state by mappingViewModel.state.collectAsStateWithLifecycle()
    val mainState by mainViewModel.state.collectAsStateWithLifecycle()
    val hazardousMarkers = mappingViewModel.hazardousLaneMarkers
    val emergencyState by emergencyViewModel.state.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    var uiState by rememberSaveable { mutableStateOf(MappingUiState()) }
    var cameraState by rememberSaveable { mutableStateOf(CameraState()) }
    val locationComponentOptions = MappingUtils.rememberLocationComponentOptions()
    var mapboxMap by remember<MutableState<MapboxMap?>> {
        mutableStateOf(null)
    }

    var incidentDescription by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(
            initialValue = BottomSheetValue.Collapsed,
            confirmStateChange = { false })
    )

    val collapseBottomSheet = remember {
        {
            coroutineScope.launch {
                if (bottomSheetScaffoldState.bottomSheetState.isExpanded) {
                    uiState = uiState.copy(bottomSheetType = null, incidentImageErrorMessage = "").also {
                        bottomSheetScaffoldState.bottomSheetState.collapse()
                    }
                }
            }
        }
    }

    val expandBottomSheet = remember {
        {
            coroutineScope.launch {
                if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                    bottomSheetScaffoldState.bottomSheetState.expand()
                }
            }
        }
    }

    val settingResultRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { activityResult ->
        if (activityResult.resultCode == RESULT_OK) {
            context.startLocationServiceIntentAction()
            Timber.d("GPS Setting Request Accepted")
            return@rememberLauncherForActivityResult
        }
        Timber.d("GPS Setting Request Denied")
    }

    val requestHelp = remember {
        {
            if (!context.hasGPSConnection()) {
                context.checkLocationSetting(
                    onDisabled = settingResultRequest::launch,
                    onEnabled = {
                        mappingViewModel.onEvent(
                            event = MappingVmEvent.RequestHelp)

                    })
            } else {
                mappingViewModel.onEvent(
                    event = MappingVmEvent.RequestHelp)

            }
        }
    }

    val foregroundLocationPermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION))


    val userLocationAvailable by remember(
        foregroundLocationPermissionsState.allPermissionsGranted,
        state.userLocation) {
        derivedStateOf {
            foregroundLocationPermissionsState.allPermissionsGranted.and(state.userLocation != null)
        }
    }


    val pulsingEnabled by remember(
        uiState.searchingAssistance,
        foregroundLocationPermissionsState.allPermissionsGranted
    ) {
        derivedStateOf { uiState.searchingAssistance.and(foregroundLocationPermissionsState.allPermissionsGranted) }
    }


    val showUserLocation = remember(mapboxMap, isNavigating, userLocationAvailable) {
        {
            mapboxMap?.style?.let { style ->

                val buildLocationComponentActivationOptions =
                    LocationComponentActivationOptions.builder(context, style)
                        .locationComponentOptions(
                            locationComponentOptions
                                .changeToNormalPuckIcon(context)
                                .pulseEnabled(pulsingEnabled)
                                .build())
                        .build()
                mapboxMap?.locationComponent?.apply {
                    activateLocationComponent(buildLocationComponentActivationOptions)
                    isLocationComponentEnabled = userLocationAvailable
                    cameraMode = CameraMode.NONE
                    renderMode = RenderMode.NORMAL
                }
            }
            Unit
        }
    }


    val locateUser =
        remember(userLocationAvailable, mapboxMap) {
            { zoomLevel: Double, latLng: LatLng, cameraAnimationDuration: Int ->

                val mapboxLoaded =
                    (mapboxMap?.locationComponent != null) && (mapboxMap?.style?.isFullyLoaded
                                                               ?: false)
                if (userLocationAvailable && mapboxLoaded) {
                    showUserLocation()
                    mapboxMap?.animateCameraPosition(
                        latLng = latLng,
                        zoomLevel = zoomLevel,
                        cameraAnimationDuration = cameraAnimationDuration)
                }
            }
        }


    val onInitializeMapboxMap = remember {
        { mbm: MapboxMap ->
            if (mapboxMap == null) {
                mapboxMap = mbm
            }
        }
    }


    val onRequestHelp = remember {
        {
            foregroundLocationPermissionsState.requestPermission(
                onGranted = {
                    context.startLocationServiceIntentAction()
                    requestHelp()
                }, onExplain = {
                    uiState = uiState.copy(locationPermissionDialogVisible = true)
                }, onDenied = {
                    uiState = uiState.copy(locationPermissionDialogVisible = true)
                })
        }
    }


    val notificationPermissionDialogVisibility = remember {
        { visible: Boolean ->
            uiState = uiState.copy(notificationPermissionVisible = visible)
        }
    }

    val respondToHelp = remember {
        {
            uiState.mapSelectedRescuee?.let {
                mappingViewModel.onEvent(
                    event = MappingVmEvent.RespondToHelp(
                        selectedRescuee = it
                    ))
            }
            Unit
        }
    }
    val notificationLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {
            if (uiState.mapSelectedRescuee == null) {
                onRequestHelp()
            } else {
                respondToHelp()
            }
        }
    )
    val notificationPermissionState = rememberPermissionState(
        permission = Manifest.permission.POST_NOTIFICATIONS
    ) { permissionGranted ->
        if (permissionGranted) {
            notificationLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

    }

    val startRequestingHelp = remember {
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                notificationPermissionState.requestPermission(onGranted = {
                    notificationLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }, onExplain = {
                    notificationPermissionDialogVisibility(true)
                }, onDenied = {
                    onRequestHelp()
                })
            } else {
                onRequestHelp()
            }
        }
    }


    val showRouteDirection = remember(mapboxMap) {
        { geometry: String ->

            mapboxMap?.getStyle { style ->

                if (style.isFullyLoaded.not()) {
                    return@getStyle
                }
                if (geometry.isEmpty()) {
                    return@getStyle
                }

                style.showRoute(geometry)

            }
            Unit
        }
    }

    val removeRouteDirection = remember(mapboxMap, uiState.routeDirection) {
        {
            mapboxMap?.getStyle { style ->

                if (style.isFullyLoaded.not()) {
                    return@getStyle
                }

                runCatching {
                    val routeLineSource = style.getSourceAs<GeoJsonSource>(ROUTE_SOURCE_ID)
                    routeLineSource?.setGeoJson(FeatureCollection.fromFeatures(arrayOf()))
                }.onFailure {
                    Timber.v("Mapbox style not loaded ${it.message}")
                }

            }
            Unit
        }
    }


    val onLocateUser = remember(uiState.routeDirection, mapboxMap) {
        {

            foregroundLocationPermissionsState.requestPermission(
                onGranted = {
                    if (!context.hasGPSConnection()) {
                        context.checkLocationSetting(
                            onDisabled = settingResultRequest::launch)
                    }


                    state.userLocation?.let {
                        it.latitude ?: return@let
                        it.longitude ?: return@let
                        val point = LatLng(it.latitude, it.longitude)
                        locateUser(
                            LOCATE_USER_ZOOM_LEVEL,
                            point,
                            DEFAULT_CAMERA_ANIMATION_DURATION)

                    }

                }, onExplain = {
                    uiState = uiState.copy(locationPermissionDialogVisible = true)
                }, onDenied = {
                    uiState = uiState.copy(locationPermissionDialogVisible = true)
                })
        }
    }

    val changeCameraMode = remember(mapboxMap) {
        { mode: Int ->
            mapboxMap?.locationComponent?.apply {
                if (isLocationComponentActivated) {
                    cameraMode = mode
                }else{
                    onLocateUser()
                }
            }
        }
    }

    val routeOverView = remember {
        {
            changeCameraMode(CameraMode.TRACKING)

        }
    }

    val onLocateUserButton = remember(uiState.routeDirection) {
        {
            if (uiState.routeDirection != null) {
                routeOverView()
            }
            onLocateUser()
        }
    }

    val recenterRoute = remember {
        {
            changeCameraMode(CameraMode.TRACKING_GPS)
        }
    }

    val openNavigationApp = remember(state.rescueTransaction?.route) {
        {
            val rescueTransaction = state.rescueTransaction
            rescueTransaction?.let {
                val latitude = it.getDestinationLatitude() ?: return@let
                val longitude = it.getDestinationLongitude() ?: return@let
                context.openNavigationApp(latitude = latitude, longitude = longitude)
            }

        }
    }


    val onClickOpenNavigationButton = remember {
        {
            openNavigationApp()
        }
    }

    val cancelSearchingAssistance = remember {
        {
            coroutineScope.launch {
                collapseBottomSheet()
            }.invokeOnCompletion {
                mappingViewModel.onEvent(event = MappingVmEvent.CancelSearchingAssistance)
                uiState = uiState.copy(searchingAssistance = false)
            }
            Unit
        }
    }
    val onChangeCameraPosition = remember {
        { _cameraState: CameraState ->
            cameraState = _cameraState
        }
    }


    val resetState = remember{{
        uiState = MappingUiState()
        collapseBottomSheet()
        onChangeNavigatingState(false)
    }}




    DisposableEffect(key1 = true) {
        onDispose {
            val camera = mapboxMap?.cameraPosition
            val cameraCenter = camera?.target ?: LatLng(
                DEFAULT_LATITUDE,
                DEFAULT_LONGITUDE,
            )
            val cameraZoom = camera?.zoom
            onChangeCameraPosition(
                CameraState(
                    position = cameraCenter,
                    zoom = cameraZoom ?: 0.0
                ))
        }
    }

    val cancelOnGoingRescue = remember(state.rescuer, state.rescueTransaction) {
        {

            val isRescuee = state.user.isRescuee()
            val transactionId = state.rescueTransaction?.id
            val selectionType = if (isRescuee) SELECTION_RESCUEE_TYPE else SELECTION_RESCUER_TYPE
            val clientId = state.rescuer?.id ?: state.rescuee?.id

            if(transactionId == null || clientId == null){
                resetState()
                Toast.makeText(context, "Rescue not found", Toast.LENGTH_SHORT).show()
            }else {
                navController.navigateScreen(
                    route = Screens.MappingNavigation.Cancellation.passArgument(
                        cancellationType = selectionType,
                        transactionId = transactionId,
                        clientId = clientId))

            }

        }
    }

    val noInternetDialogVisibility = remember {
        { visibility: Boolean ->
            uiState = uiState.copy(
                isNoInternetVisible = visibility
            )
        }
    }

    val hasTransaction = remember(key1 = state.rescueTransaction, key2 = state.user.transaction) {
        state.getTransactionId().isNotEmpty()
    }

    val isRescueCancelled =
        remember(state.rescueTransaction) {
            state.rescueTransaction?.isRescueCancelled() ?: false
        }

    fun getConversationSelectedId(): String? {
        val transaction = state.rescueTransaction
        val rescueeId = transaction?.rescueeId
        val rescuerId = transaction?.rescuerId
        val userId = state.userId
        val isUserRescuee = userId == rescueeId
        val id = if (isUserRescuee) rescuerId else rescueeId
        id ?: Toast.makeText(context, "No current transaction", Toast.LENGTH_SHORT).show()

        return id
    }

    val onClickChatButton = remember(state.rescueTransaction, state.user.getRole()) {
        {
            getConversationSelectedId()?.let { id ->
                navController.navigateScreen(
                    route = Screens.MessagingNavigation.Conversation.passArgument(
                        receiverMessageId = id)
                )
            }
        }
    }

    val onRequestNavigationCameraToOverview = remember(mapboxMap) {
        {
            val locationComponent = mapboxMap?.locationComponent
            locationComponent?.cameraMode = CameraMode.TRACKING
        }
    }

    val confirmedDestinationArrived = remember {
        {

            mappingViewModel.onEvent(event = MappingVmEvent.DestinationArrived)
        }
    }

    val onClickOkCancelledRescue = remember {
        {
            mappingViewModel.onEvent(event = MappingVmEvent.CancelRescueTransaction)
        }
    }

    val onDismissRescueeBanner = remember {
        {
            val isRescueeBannerVisible = uiState.mapSelectedRescuee != null
            if (isRescueeBannerVisible) {
                uiState = uiState.copy(
                    mapSelectedRescuee = null,
                    requestHelpButtonVisible = true
                )
            }
        }
    }


    val expandableFab = remember {
        { expanded: Boolean ->
            uiState = uiState.copy(
                isFabExpanded = expanded
            )
        }
    }


    fun checkIfHasEditingMarker(noMarkerCurrentlyEditing: () -> Unit) {
        val isCurrentlyEditing = uiState.currentlyEditingHazardousMarker != null
        if (isCurrentlyEditing) {
            uiState = uiState.copy(discardHazardousMarkerDialogVisible = true)
            return
        }
        if (uiState.bottomSheetType == BottomSheetType.SearchAssistance.type) {
            return
        }
        noMarkerCurrentlyEditing()
    }


    val onMapMarkerClick = remember {
        { snippet: String, id: String ->
            if (snippet == MarkerSnippet.HazardousLaneSnippet.type) {
                checkIfHasEditingMarker(noMarkerCurrentlyEditing = {
                    mappingViewModel.onEvent(event = MappingVmEvent.SelectHazardousLaneMarker(id))
                })
            } else {
                collapseBottomSheet()
                mappingViewModel.onEvent(event = MappingVmEvent.SelectRescueMapIcon(id))
            }
        }
    }

    val hazardousLaneMarkerDialogVisibility = remember {
        { visibility: Boolean ->
            uiState = uiState.copy(
                deleteHazardousMarkerDialogVisible = visibility
            )
        }
    }


    val onMapClick = remember {
        {

            if (uiState.reportIncidentDialogVisible) {
                 uiState = uiState.copy(reportIncidentDialogVisible = false)
            }

            if (uiState.incidentDescriptionDialogVisible) {
                checkIfHasEditingMarker(noMarkerCurrentlyEditing = {
                    uiState = uiState.copy(incidentDescriptionDialogVisible = false)
                })
            }

            onDismissRescueeBanner()
            expandableFab(false)


        }
    }

    val onMapLongClick = remember {
        { latLng: LatLng ->
            checkIfHasEditingMarker(noMarkerCurrentlyEditing = {
                if(uiState.bottomSheetType == BottomSheetType.OnGoingRescue.type){
                    return@checkIfHasEditingMarker
                }
                onDismissRescueeBanner()
                expandableFab(false)
                uiState = uiState.copy(
                    lastLongPressedLocation = latLng,
                    reportIncidentDialogVisible = true)
            })

        }
    }


    val startRespondingToHelp = remember {
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                notificationPermissionState.requestPermission(onGranted = {
                    notificationLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }, onExplain = {
                    notificationPermissionDialogVisibility(true)
                }, onDenied = {
                    respondToHelp()
                })
            } else {
                respondToHelp()
            }
        }
    }


    val onClickOkAcceptedRescue = remember {
        {
            onChangeNavigatingState(true)
            uiState = uiState.copy(
                rescueRequestAccepted = false,
                bottomSheetType = BottomSheetType.OnGoingRescue.type,
                requestAcceptedVisible = false
            )
            expandBottomSheet()
            onDismissRescueeBanner()


        }
    }

    val locationPermissionDialogVisibility = remember {
        { visibility: Boolean ->
            uiState = uiState.copy(locationPermissionDialogVisible = visibility)
        }
    }

    val banAccountDialogVisibility = remember{{ visibility: Boolean ->
        uiState = uiState.copy(banAccountDialogVisible = visibility)
    }}

    val onClickCancelButton = remember {
        { id: String ->
            mappingViewModel.onEvent(MappingVmEvent.DeclineRescueRequest(id))
        }
    }

    val onClickConfirmButton = remember {
        { id: String ->
            mappingViewModel.onEvent(MappingVmEvent.AcceptRescueRequest(id))
        }
    }

    val onClickReportIncident = remember {
        { incidentLabel: String ->
            val imageUri = uiState.incidentImageUri

            if(imageUri == null){
                uiState = uiState.copy(incidentImageErrorMessage = "Please select an image")
            }else{
                uiState.lastLongPressedLocation?.let { locationLatLng ->
                    mappingViewModel.onEvent(
                        event = MappingVmEvent.ReportIncident(
                            label = incidentLabel,
                            latLng = locationLatLng,
                            description = incidentDescription.text,
                            imageUri = imageUri
                        ))
                    uiState = uiState.copy(selectedIncidentLabel = "")
                    incidentDescription = TextFieldValue()
                    coroutineScope.launch {
                        bottomSheetScaffoldState.bottomSheetState.collapse()
                    }
                }

            }
        }
    }

    val emergencyCallDialogVisibility = remember {
        { visible: Boolean ->
            uiState = uiState.copy(
                isEmergencyCallDialogVisible = visible
            )
        }
    }

    val changeAlertDialogState = remember {
        { alertDialogState: AlertDialogState ->
            uiState = uiState.copy(
                alertDialogState = alertDialogState
            )
        }
    }

    val rescueRequestDialogVisibility = remember {
        { visibility: Boolean ->
            uiState = uiState.copy(
                isRescueRequestDialogVisible = visibility
            )
        }
    }

    val openSinoTrack = remember {
        {
            navController.navigateScreen(Screens.MappingNavigation.SinoTrack.screenRoute)
        }
    }





    val callPhoneNumber = remember {
        { phoneNumber: String ->
            context.callPhoneNumber(phoneNumber)
        }
    }

    val openPhoneCallPermissionState =
        rememberPermissionState(permission = Manifest.permission.CALL_PHONE) { permissionGranted ->
            if (permissionGranted) {
                uiState.selectedPhoneNumber.takeIf { it.isNotEmpty() }
                    ?.let { callPhoneNumber(it) }
            }
        }

    val onEmergencyCall = remember {
        { phoneNumber: String ->
            if (!openPhoneCallPermissionState.status.isGranted) {
                uiState = uiState.copy(selectedPhoneNumber = phoneNumber)
                openPhoneCallPermissionState.launchPermissionRequest()
            } else {
                callPhoneNumber(phoneNumber)
            }
        }
    }

    val onAddEmergencyContact = remember {
        {
            navController.navigateScreen(Screens.EmergencyCallNavigation.AddEditEmergencyContact.screenRoute)
        }
    }

    val shareLocation = remember(state.userLocation, state.user.location) {
        {

            val location = state.getCurrentLocation()

            if (location == null) {
                Toast.makeText(context, "Searching for GPS", Toast.LENGTH_SHORT).show()
            } else {
                context.shareLocation(
                    latitude = location.latitude!!,
                    longitude = location.longitude!!
                )
            }
        }
    }


    val openMapTypeBottomSheet = remember {
        {
            uiState = uiState.copy(
                bottomSheetType = BottomSheetType.MapType.type
            ).also {
                expandBottomSheet()
            }
        }
    }

    val closeMapTypeBottomSheet = remember {
        {

                collapseBottomSheet()

        }
    }


    val mapTypeBottomSheetVisibility = remember {
        { visibility: Boolean ->
            checkIfHasEditingMarker(noMarkerCurrentlyEditing = {
                if (visibility) {
                    openMapTypeBottomSheet()
                } else {
                    closeMapTypeBottomSheet()
                }
            })
        }
    }

    fun toggleMapType(event: MappingVmEvent){
        if(!context.hasInternetConnection()){
            noInternetDialogVisibility(true)
            return
        }
        if(state.userLocation == null){
            Toast.makeText(context, "Searching for GPS", Toast.LENGTH_SHORT).show()
            return
        }

        mappingViewModel.onEvent(event = event)

    }

    val toggleDefaultMapType = remember(state.userLocation) {
        {
            toggleMapType(MappingVmEvent.ToggleDefaultMapType)
        }
    }

    val toggleTrafficMapType = remember(state.userLocation) {
        {
            toggleMapType(MappingVmEvent.ToggleTrafficMapType)
        }
    }

    val toggleHazardousMapType = remember(state.userLocation) {
        {
            toggleMapType(MappingVmEvent.ToggleHazardousMapType)
        }

    }


    val onChangeIncidentLabel = remember {
        { incidentLabel: String ->
            uiState = uiState.copy(selectedIncidentLabel = incidentLabel)
        }
    }

    val onChangeIncidentDescription = remember {
        { input: TextFieldValue ->
            incidentDescription = input
        }
    }


    val onClickDeleteIncident = remember {
        {

            uiState = uiState.copy(
                deleteHazardousMarkerDialogVisible = true
            )

        }
    }

    val onClickEditIncidentDescription = remember {
        { marker: HazardousLaneMarkerDetails ->
            uiState = uiState.copy(currentlyEditingHazardousMarker = marker)
        }
    }

    val onConfirmDeleteIncident = remember(uiState.selectedHazardousMarker) {
        {
            mappingViewModel.onEvent(
                event = MappingVmEvent.DeleteHazardousLaneMarker(
                    id = uiState.selectedHazardousMarker!!.id
                ))
        }
    }

    val discardChangesMarkerDialogVisibility = remember {
        { visibility: Boolean ->
            uiState = uiState.copy(
                discardHazardousMarkerDialogVisible = visibility
            )
        }
    }


    val onDiscardMarkerChanges = remember {
        {
            uiState = uiState.copy(currentlyEditingHazardousMarker = null)
        }
    }

    val onDismissIncidentDescriptionBottomSheet = remember {
        {
            checkIfHasEditingMarker(noMarkerCurrentlyEditing = {
                collapseBottomSheet()
            })
        }
    }

    val onCancelEditIncidentDescription = remember {
        {
            checkIfHasEditingMarker(onDiscardMarkerChanges)
        }
    }

    val onUpdateReportedIncident = remember(uiState.currentlyEditingHazardousMarker) {
        { description: String, label: String ->
            mappingViewModel.onEvent(
                event = MappingVmEvent.UpdateReportedIncident(
                    marker = uiState.currentlyEditingHazardousMarker!!.copy(
                        description = description,
                        label = label)
                ))
        }
    }

    val onClickHazardousInfoGotIt = remember {
        {
            mappingViewModel.onEvent(event = MappingVmEvent.ShouldShowHazardousStartingInfo(false))
        }
    }

    fun getRouteDirections() {
        val rescueTransaction = state.rescueTransaction ?: return

        val startingLongitude = rescueTransaction.getStartingLongitude() ?: return
        val startingLatitude = rescueTransaction.getStartingLatitude() ?: return
        val destinationLongitude = rescueTransaction.getDestinationLongitude() ?: return
        val destinationLatitude = rescueTransaction.getDestinationLatitude() ?: return


        if(uiState.routeDirection != null){
            return
        }

        mappingViewModel.onEvent(
            event = MappingVmEvent.GetRouteDirections(
                origin = Point.fromLngLat(startingLongitude, startingLatitude),
                destination = Point.fromLngLat(
                    destinationLongitude,
                    destinationLatitude)))
    }


    val cancelSearchDialogVisibility = remember {
        { visibility: Boolean ->
            uiState = uiState.copy(cancelSearchDialogVisible = visibility)
        }
    }

    val cancelOnGoingRescueDialogVisibility = remember {
        { visibility: Boolean ->
            uiState = uiState.copy(cancelOnGoingRescueDialogVisible = visibility)
        }
    }

    fun notifyNewRescueRequest(message: String) {
        if (notificationPermissionState.status.isGranted && !AppUtils.isAppInForeground(context = context)) {
            mappingViewModel.onEvent(
                event = MappingVmEvent.NotifyNewRescueRequest(
                    message = message
                ))
        }
    }

    fun notifyRequestAccepted(message: String) {
        if (notificationPermissionState.status.isGranted && !AppUtils.isAppInForeground(context = context)) {
            mappingViewModel.onEvent(
                event = MappingVmEvent.NotifyRequestAccepted(
                    message = message
                ))
        }
    }

    val viewProfile = remember {
        { id: String ->
            navController.navigateScreen(
                Screens.UserProfileNavigation.UserProfile.passArgument(
                    userId = id))
        }
    }

    val cancelRespondToHelp = remember(uiState.mapSelectedRescuee) {
        {
            uiState.mapSelectedRescuee?.userId?.let { id ->
                mappingViewModel.onEvent(event = MappingVmEvent.CancelRespondHelp(id = id))
            }
        }
    }


    val arrivedAtLocation = remember{{
        mappingViewModel.onEvent(event = MappingVmEvent.ArrivedAtLocation)
    }}

    val startNavigation = remember(state.user.transaction?.transactionId) {
        {
            val role = state.user.getRole()
            val isRescuer = role == Role.Rescuer.name
            val userTransaction = state.user.transaction?.transactionId ?: ""
            if (userTransaction.isNotEmpty()) {
                uiState = uiState.copy(
                    requestHelpButtonVisible = false,
                    bottomSheetType = BottomSheetType.OnGoingRescue.type,
                    isRescueRequestDialogVisible = false,
                    isNavigating = isRescuer
                )
                onChangeNavigatingState(isRescuer)
                expandBottomSheet()
                getRouteDirections()
                showUserLocation()
            }

        }
    }

    val stopNavigation = remember() {
        {
            resetState()
            onDismissRescueeBanner()

        }
    }
    val accessPhotoDialog = remember{{ visibility: Boolean ->
        uiState = uiState.copy(
            accessPhotoDialogVisible = visibility
        )
    }}

    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }

    val openGalleryResultLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { selectedUri ->
                imageBitmap =
                    when {
                        Build.VERSION.SDK_INT < Build.VERSION_CODES.P -> {
                            MediaStore.Images.Media.getBitmap(
                                context.contentResolver,
                                selectedUri)
                        }

                        else -> {
                            val source =
                                ImageDecoder.createSource(
                                    context.contentResolver,
                                    selectedUri)
                            ImageDecoder.decodeBitmap(source)
                        }
                    }
            val imageUri = if (imageBitmap == null) uri.toString() else ImageUtils.encodeImage(
                imageBitmap!!)

            uiState = uiState.copy(incidentImageUri = imageUri)
            }

        }

    val openCameraResultLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap: Bitmap? ->
            val uri = bitmap?.toImageUri().toString()
            imageBitmap = bitmap
            val imageUri = if (imageBitmap == null) uri else ImageUtils.encodeImage(
                imageBitmap!!)

            imageUri.takeIf { it != "null" && it.isNotEmpty() }?.let{
                uiState = uiState.copy(incidentImageUri = imageUri)
            }

        }

    val filesAndMediaPermissionState =
        rememberMultiplePermissionsState(
            permissions = listOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) { permissionGranted ->
            if (permissionGranted.values.all { it }) {
                openGalleryResultLauncher.launch("image/*")
            }
        }


    val openCameraPermissionState =
        rememberPermissionState(permission = Manifest.permission.CAMERA) { permissionGranted ->

            if (permissionGranted) {
                openCameraResultLauncher.launch()
            }
        }

    val openGallery = remember {
        {
            filesAndMediaPermissionState.requestPermission(
                onGranted = {
                    accessPhotoDialog(false)
                    openGalleryResultLauncher.launch("image/*")
                }, onExplain = {
                    uiState = uiState.copy(filesAndMediaPermissionDialogVisible = true)
                }, onDenied = {
                    uiState = uiState.copy(filesAndMediaPermissionDialogVisible = true)
                })

        }
    }

    val openCamera = remember {
        {
            openCameraPermissionState.requestPermission(
                onGranted = {
                    accessPhotoDialog(false)
                    openCameraResultLauncher.launch()
                }, onExplain = {
                    uiState = uiState.copy(cameraPermissionDialogVisible = true)
                }, onDenied = {
                    uiState = uiState.copy(cameraPermissionDialogVisible = true)
                })
        }
    }


    val cameraPermissionDialogVisibility = remember {{ visibility: Boolean ->
        uiState = uiState.copy(cameraPermissionDialogVisible = visibility)
    }}

    val filesAndMediaPermissionDialogVisibility = remember{{ visibility : Boolean ->
        uiState = uiState.copy(filesAndMediaPermissionDialogVisible = visibility)
    }}


    val viewIncidentDetails = remember{{
        val selectedHazardousMarker = uiState.selectedHazardousMarker
        val markerDetails = selectedHazardousMarker?.copy(incidentImageUri = Uri.encode(selectedHazardousMarker.incidentImageUri))
        navController.navigateScreen(route = Screens.MappingNavigation.MarkerIncidentDetails.passArgument(markerDetails = markerDetails.toJson()!!))
    }}

    val viewIncidentImage = remember{{
        val uri = Uri.encode(uiState.incidentImageUri)
        navController.navigateScreen(route = Screens.MappingNavigation.IncidentImage.passArgument(imageUri = uri))
    }}

    val reportIncidentDialog = remember{{ visibility: Boolean ->
        uiState = uiState.copy(
            reportIncidentDialogVisible = visibility,
        )
    }}

    val incidentDescriptionDialog = remember{{ visibility: Boolean ->
        uiState = uiState.copy(
            incidentDescriptionDialogVisible = visibility,

        )
    }}

    val removeIncidentImage = remember{{
        uiState = uiState.copy(
            incidentImageUri = null,
            incidentImageErrorMessage = ""
        )
    }}


    DisposableEffect(key1 = Unit) {
        val window = context.findActivity()?.window
        window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        onDispose {
            window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    LaunchedEffect(
        key1 = state.rescueTransaction,
        key2 = uiState.isRescueCancelled,
        key3 = uiState.rescueRequestAccepted) {

        if (uiState.isRescueCancelled) {
            return@LaunchedEffect
        }

        if (state.rescueTransaction?.isRescueFinished() == true) {
            return@LaunchedEffect
        }

        if (!uiState.rescueRequestAccepted) {
            return@LaunchedEffect
        }


        uiState = uiState.copy(
            requestAcceptedVisible = true
        )
    }


    LaunchedEffect(
        key1 = uiState.isRescueCancelled,
        key2 = uiState.rescueRequestAccepted,
        key3 = state.rescueTransaction) {

        if(!uiState.isRescueCancelled){
            return@LaunchedEffect
        }
        if(uiState.rescueRequestAccepted){
            return@LaunchedEffect
        }

        uiState = uiState.copy(
            requestCancelledVisible = true
        )
    }

    BackHandler(enabled = bottomSheetScaffoldState.bottomSheetState.isExpanded) {
        checkIfHasEditingMarker(noMarkerCurrentlyEditing = {

            cancelOnGoingRescueDialogVisibility(hasTransaction)
            cancelSearchDialogVisibility(hasTransaction)

            if (hasTransaction) {
                return@checkIfHasEditingMarker
            }

            if (uiState.searchingAssistance) {
                return@checkIfHasEditingMarker
            }

            collapseBottomSheet()
        })
    }


    LaunchedEffect(state.user.userAssistance) {

        if (state.user.isUserNeedHelp() == true) {

            uiState = uiState.copy(
                bottomSheetType = BottomSheetType.SearchAssistance.type,
                searchingAssistance = true)
                .also {
                    expandBottomSheet()
                }

        }
    }

    LaunchedEffect(key1 = true) {
        emergencyViewModel.onEvent(event = EmergencyCallVmEvent.LoadDefaultContact)
    }

    LaunchedEffect(key1 = mainState.mappingIntentAction) {

        when (mainState.mappingIntentAction) {
            MappingConstants.ACTION_OPEN_CONVERSATION -> {
                onClickChatButton()
                mainViewModel.setIntentAction(DEFAULT_ACTION)
            }

            MappingConstants.ACTION_OPEN_RESCUE_REQUEST -> {
                rescueRequestDialogVisibility(true)
                mainViewModel.setIntentAction(DEFAULT_ACTION)
            }
        }
    }

    LaunchedEffect(key1 = userLocationAvailable, mapboxMap) {
        if (userLocationAvailable) {
            val camera = cameraState
            locateUser(camera.zoom, camera.position, FAST_CAMERA_ANIMATION_DURATION)
        }
    }


    LaunchedEffect(key1 = state.rescueTransaction?.status, key2 = hasTransaction) {
        val rescueTransaction = state.rescueTransaction
        val isRescueFinished = rescueTransaction?.isRescueFinished() ?: false
        val isRescueOnGoing = rescueTransaction?.isRescueOnGoing() ?: false

        if (rescueTransaction == null) {
            return@LaunchedEffect
        }

        if (isRescueOnGoing) {
            return@LaunchedEffect
        }

        if (!isRescueFinished) {
            return@LaunchedEffect
        }

        if(!hasTransaction){
            return@LaunchedEffect
        }

        val role = state.user.transaction?.role
        val type = if (role == Role.Rescuee.name) {
            BottomSheetType.RescuerArrived.type
        } else {
            BottomSheetType.DestinationReached.type
        }
        uiState = uiState.copy(bottomSheetType = type)

    }

    LaunchedEffect(key1 = hasTransaction) {
        uiState = uiState.copy(
            hasTransaction = hasTransaction
        )
    }
    LaunchedEffect(key1 = isRescueCancelled) {
        uiState = uiState.copy(
            isRescueCancelled = isRescueCancelled
        )
    }
    LaunchedEffect(key1 = isNavigating) {
        uiState = uiState.copy(
            isNavigating = isNavigating
        )
    }

    LaunchedEffect(key1 = true) {
        mappingViewModel.eventFlow.distinctUntilChanged().collectLatest{ event ->
            when (event) {

                is MappingEvent.NoInternetConnection -> {
                    noInternetDialogVisibility(true)
                }
                else -> {}
            }

        }
    }
    LaunchedEffect(key1 = true) {

        mappingViewModel.eventFlow.collectLatest { event ->
            when (event) {

                is MappingEvent.AccountBanned -> {
                    banAccountDialogVisibility(true)
                }

                is MappingEvent.RequestHelpSuccess -> {
                    val location = state.userLocation!!
                    navController.navigateScreen(
                        Screens.MappingNavigation.ConfirmDetails.passArgument(
                            latitude = location.latitude!!.toFloat(),
                            longitude = location.longitude!!.toFloat()
                        ))
                }

                is MappingEvent.InsufficientUserCredential -> {
                    navController.navigateScreen(
                        Screens.UserProfileNavigation.EditProfile.screenRoute)
                }

                is MappingEvent.LocationNotAvailable -> {
                    Toast.makeText(context, event.reason, Toast.LENGTH_SHORT).show()
                }

                is MappingEvent.RescuerLocationNotAvailable -> {
                    Toast.makeText(context, event.reason, Toast.LENGTH_SHORT).show()
                }

                is MappingEvent.UnexpectedError -> {
                    Toast.makeText(context, event.reason, Toast.LENGTH_SHORT).show()
                }

                is MappingEvent.UserFailed -> {
                    Toast.makeText(context, event.reason, Toast.LENGTH_SHORT).show()
                }

                is MappingEvent.RespondToHelpSuccess -> {
                    Toast.makeText(context, event.reason, Toast.LENGTH_SHORT).show()
                }

                is MappingEvent.AddressFailed -> {
                    Toast.makeText(context, event.reason, Toast.LENGTH_SHORT).show()
                }


                is MappingEvent.NewSelectedRescuee -> {
                    uiState = uiState.copy(
                        mapSelectedRescuee = event.selectedRescuee,
                        requestHelpButtonVisible = false
                    )
                }

                is MappingEvent.GenerateRouteNavigationSuccess -> {
                    uiState = uiState.copy(
                        routeDirection = event.routeDirection,
                        generateRouteFailed = false
                    )
                    context.startLocationServiceIntentAction(intentAction = ACTION_START_FOREGROUND)

                }

                is MappingEvent.CancelRescueTransactionSuccess -> {

                    stopNavigation()
                }


                is MappingEvent.RescueRequestAccepted -> {
                    uiState = uiState.copy(
                        rescueRequestAccepted = true
                    )
                    onDismissRescueeBanner()
                }

                is MappingEvent.CancelHelpRequestSuccess -> {
                    uiState = uiState.copy(
                        requestHelpButtonVisible = true
                    )
                }

                is MappingEvent.AcceptRescueRequestSuccess -> {
                    uiState = uiState.copy(
                        requestHelpButtonVisible = false,
                        bottomSheetType = BottomSheetType.OnGoingRescue.type,
                        isRescueRequestDialogVisible = false
                    ).also {
                        expandBottomSheet()
                        rescueRequestDialogVisibility(false)

                    }
                }

                is MappingEvent.FailedToCalculateDistance -> {
                    Toast.makeText(context, "Failed to Calculate Distance", Toast.LENGTH_SHORT)
                        .show()
                }


                is MappingEvent.RemoveRespondentFailed -> {
                    Toast.makeText(context, event.reason, Toast.LENGTH_SHORT).show()
                }

                is MappingEvent.RescueHasTransaction -> {
                    changeAlertDialogState(
                        AlertDialogState(
                            title = "Cannot Request",
                            description = "Unfortunately the Rescuer is currently in a Rescue.",
                            icon = R.raw.error
                        ))
                }

                is MappingEvent.UserHasCurrentTransaction -> {
                    changeAlertDialogState(
                        AlertDialogState(
                            title = "Cannot Request",
                            description = "You can only have one transaction at a time",
                            icon = R.raw.error
                        )
                    )
                }

                is MappingEvent.NewBottomSheetType -> {
                    uiState = uiState.copy(
                        bottomSheetType = event.bottomSheetType,
                        searchingAssistance = event.bottomSheetType == BottomSheetType.SearchAssistance.type)
                        .also {
                            expandBottomSheet()
                        }
                }

                is MappingEvent.ReportIncidentFailed -> {
                    Toast.makeText(context, event.reason, Toast.LENGTH_SHORT).show()
                }

                MappingEvent.ReportIncidentSuccess -> {
                    Toast.makeText(context, "Incident Reported", Toast.LENGTH_SHORT).show()
                    reportIncidentDialog(false)
                }

                is MappingEvent.IncidentDistanceTooFar -> {
                    changeAlertDialogState(
                        AlertDialogState(
                            title = "Exceeds Reachable Distance",
                            description = "The incident is taking place quite a distance away from your current location, making it challenging to directly engage or intervene.",
                            icon = R.raw.error
                        )
                    )
                }

                is MappingEvent.SelectHazardousLaneMarker -> {
                    uiState = uiState.copy(
                        selectedHazardousMarker = event.marker,
                        incidentDescriptionDialogVisible = true)
                }

                is MappingEvent.DeleteHazardousLaneMarkerFailed -> {
                    collapseBottomSheet()
                    Toast.makeText(context, event.reason, Toast.LENGTH_LONG).show()
                }

                MappingEvent.DeleteHazardousLaneMarkerSuccess -> {
                    collapseBottomSheet()
                    Toast.makeText(context, "Marker Deleted", Toast.LENGTH_LONG).show()
                }

                is MappingEvent.UpdateIncidentFailed -> {
                    Toast.makeText(context, event.reason, Toast.LENGTH_LONG).show()
                }

                MappingEvent.UpdateIncidentSuccess -> {
                    changeAlertDialogState(
                        AlertDialogState(
                            title = "Incident Updated",
                            description = "The incident has been updated successfully",
                            icon = R.raw.success
                        )
                    )
                    onDiscardMarkerChanges()
                    collapseBottomSheet()
                }

                is MappingEvent.GenerateRouteNavigationFailed -> {
                    changeAlertDialogState(
                        AlertDialogState(
                            title = "Failed to Generate Route",
                            description = "Failed to generate route to the destination due to a connection error.",
                        ))
                    uiState = uiState.copy(
                        generateRouteFailed = true
                    )
                }

                MappingEvent.CancelRespondSuccess -> {
                    Toast.makeText(context, "Respond Cancelled", Toast.LENGTH_SHORT).show()
                }

                MappingEvent.RescueArrivedSuccess -> {
                    val role = state.user.getRole()

                    val route = if(role == Role.Rescuee.name){
                        Screens.RescueRecordNavigation.RescueResults.screenRoute
                    }else{
                        Screens.RescueRecordNavigation.RescueDetails.screenRoute
                    }

                    resetState()
                    navController.navigateScreen(route)
                }

                is MappingEvent.RescueArrivedFailed -> {
                    Toast.makeText(context, event.reason, Toast.LENGTH_SHORT).show()
                }

                else -> {}
            }
        }
    }
    LaunchedEffect(key1 = uiState.routeDirection?.geometry, key2 = mapboxMap) {

        val route = uiState.routeDirection

        if (route == null) {
            removeRouteDirection()
            context.startLocationServiceIntentAction(intentAction = ACTION_STOP_FOREGROUND)
            return@LaunchedEffect
        }
        showRouteDirection(route.geometry)
    }




    LaunchedEffect(
        key1 = state.rescueTransaction?.route,
        key2 = hasTransaction,
        key3 = isRescueCancelled) {


        if(state.user.getTransactionId()?.isEmpty() == true){
            uiState = uiState.copy(routeDirection = null)
            return@LaunchedEffect
        }

        if (hasTransaction.not() || isRescueCancelled) {
            uiState = uiState.copy(routeDirection = null)
            return@LaunchedEffect
        }


        startNavigation()
    }

/*    LaunchedEffect(
        key1 = hasInternetConnection,
        key2 = uiState.generateRouteFailed,
        *//*key3 = state.rescueTransaction?.route*//*) {

        if (hasInternetConnection.not()) {
            return@LaunchedEffect
        }
        if (!uiState.generateRouteFailed) {
            return@LaunchedEffect
        }

        startNavigation()
    }*/


    LaunchedEffect(key1 = isNavigating, key2 = userLocationAvailable, key3 = pulsingEnabled) {
        showUserLocation()
    }
    LaunchedEffect(key1 = uiState.bottomSheetType) {
        coroutineScope.launch {
            if (uiState.bottomSheetType?.isNotEmpty() == true) {
                expandBottomSheet()
            }
        }
    }


    LaunchedEffect(key1 = foregroundLocationPermissionsState.allPermissionsGranted) {
        if (!foregroundLocationPermissionsState.allPermissionsGranted) {
            return@LaunchedEffect
        }

        if (!context.hasGPSConnection()) {
            context.checkLocationSetting(onDisabled = settingResultRequest::launch)
        }

        context.startLocationServiceIntentAction()

    }


    MappingScreenContent(
        modifier = Modifier.padding(paddingValues),
        state = state,
        locationPermissionState = foregroundLocationPermissionsState,
        bottomSheetScaffoldState = bottomSheetScaffoldState,
        hazardousLaneMarkers = hazardousMarkers,
        mapboxMap = mapboxMap,
        uiState = uiState,
        emergencyState = emergencyState,
        incidentDescription = incidentDescription,
        event = { event ->
            when (event) {
                is MappingUiEvent.RequestHelp -> startRequestingHelp()
                is MappingUiEvent.RespondToHelp -> startRespondingToHelp()
                is MappingUiEvent.CancelSearching -> cancelSearchDialogVisibility(true)
                is MappingUiEvent.ChatRescueTransaction -> onClickChatButton()
                is MappingUiEvent.CancelRescueTransaction -> cancelOnGoingRescueDialogVisibility(
                    true)

                is MappingUiEvent.CancelledRescueConfirmed -> onClickOkCancelledRescue()
                is MappingUiEvent.OnInitializeMap -> onInitializeMapboxMap(event.mapboxMap)
                is MappingUiEvent.RescueRequestAccepted -> onClickOkAcceptedRescue()
                is MappingUiEvent.OnChangeCameraState -> onChangeCameraPosition(event.cameraState)
                is MappingUiEvent.NoInternetDialog -> noInternetDialogVisibility(event.visibility)
                is MappingUiEvent.OnMapClick -> onMapClick()
                is MappingUiEvent.DismissBanner -> onDismissRescueeBanner()
                is MappingUiEvent.LocateUser -> onLocateUserButton()
                is MappingUiEvent.RouteOverview -> routeOverView()
                is MappingUiEvent.RecenterRoute -> recenterRoute()
                is MappingUiEvent.OpenNavigation -> onClickOpenNavigationButton()
                is MappingUiEvent.OnRequestNavigationCameraToOverview -> onRequestNavigationCameraToOverview()
                is MappingUiEvent.ConfirmedDestinationArrived -> confirmedDestinationArrived()
                is MappingUiEvent.LocationPermissionDialog -> locationPermissionDialogVisibility(event.visibility)
                is MappingUiEvent.ExpandableFab -> expandableFab(event.expanded)
                is MappingUiEvent.EmergencyCallDialog -> emergencyCallDialogVisibility(event.visibility)
                is MappingUiEvent.OpenFamilyTracker -> shareLocation()
                is MappingUiEvent.RescueRequestDialog -> rescueRequestDialogVisibility(event.visibility)
                is MappingUiEvent.DeclineRequestHelp -> onClickCancelButton(event.id)
                is MappingUiEvent.ConfirmRequestHelp -> onClickConfirmButton(event.id)
                is MappingUiEvent.AlertDialog -> changeAlertDialogState(event.alertDialogState)
                is MappingUiEvent.OnMapLongClick -> onMapLongClick(event.latLng)
                is MappingUiEvent.OnReportIncident -> onClickReportIncident(event.labelIncident)
                is MappingUiEvent.OnEmergencyCall -> onEmergencyCall(event.phoneNumber)
                is MappingUiEvent.OnAddEmergencyContact -> onAddEmergencyContact()
                is MappingUiEvent.MapTypeBottomSheet -> mapTypeBottomSheetVisibility(event.visibility)
                is MappingUiEvent.OnChangeIncidentDescription -> onChangeIncidentDescription(event.description)
                is MappingUiEvent.OnChangeIncidentLabel -> onChangeIncidentLabel(event.label)
                is MappingUiEvent.OnClickDeleteIncident -> onClickDeleteIncident()
                is MappingUiEvent.OnClickEditIncidentDescription -> onClickEditIncidentDescription(
                    event.marker)

                is MappingUiEvent.OnClickMapMarker -> onMapMarkerClick(
                    event.markerSnippet,
                    event.markerId)

                is MappingUiEvent.HazardousLaneMarkerDialog -> hazardousLaneMarkerDialogVisibility(
                    event.visibility)

                MappingUiEvent.OnConfirmDeleteIncident -> onConfirmDeleteIncident()
                is MappingUiEvent.DiscardChangesMarkerDialog -> discardChangesMarkerDialogVisibility(
                    event.visibility)

                MappingUiEvent.DiscardMarkerChanges -> onDiscardMarkerChanges()
                MappingUiEvent.DismissIncidentDescriptionBottomSheet -> onDismissIncidentDescriptionBottomSheet()
                MappingUiEvent.CancelEditIncidentDescription -> onCancelEditIncidentDescription()
                is MappingUiEvent.UpdateIncidentDescription -> onUpdateReportedIncident(
                    event.description,
                    event.label)

                MappingUiEvent.OnClickHazardousInfoGotIt -> onClickHazardousInfoGotIt()
                is MappingUiEvent.CancelSearchDialog -> cancelSearchDialogVisibility(event.visibility)
                MappingUiEvent.SearchCancelled -> cancelSearchingAssistance()
                MappingUiEvent.CancelOnGoingRescue -> cancelOnGoingRescue()
                is MappingUiEvent.CancelOnGoingRescueDialog -> cancelOnGoingRescueDialogVisibility(
                    event.visibility)

                is MappingUiEvent.NotificationPermissionDialog -> notificationPermissionDialogVisibility(
                    event.visibility)

                is MappingUiEvent.NotifyRequestAccepted -> notifyRequestAccepted(message = event.message)
                is MappingUiEvent.NotifyNewRescueRequest -> notifyNewRescueRequest(message = event.message)
                MappingUiEvent.OpenSinoTrack -> openSinoTrack()

                is MappingUiEvent.ViewProfile -> viewProfile(event.id)
                MappingUiEvent.CancelRespondHelp -> cancelRespondToHelp()
                MappingUiEvent.ArrivedAtLocation -> arrivedAtLocation()
                is MappingUiEvent.BannedAccountDialog -> banAccountDialogVisibility(event.visibility)
                MappingUiEvent.ToggleDefaultMapType -> toggleDefaultMapType()
                MappingUiEvent.ToggleHazardousMapType -> toggleHazardousMapType()
                MappingUiEvent.ToggleTrafficMapType -> toggleTrafficMapType()
                is MappingUiEvent.AccessPhotoDialog -> accessPhotoDialog(event.visibility)
                is MappingUiEvent.CameraPermissionDialog -> cameraPermissionDialogVisibility(event.visibility)
                is MappingUiEvent.FilesAndMediaPermissionDialog -> filesAndMediaPermissionDialogVisibility(event.visibility)
                MappingUiEvent.OpenCamera -> openCamera()
                MappingUiEvent.SelectImageFromGallery -> openGallery()
                MappingUiEvent.ViewImage -> viewIncidentImage()
                MappingUiEvent.ViewImageIncidentDetails -> viewIncidentDetails()
                is MappingUiEvent.IncidentDescriptionDialog -> incidentDescriptionDialog(event.visibility)
                is MappingUiEvent.ReportIncidentDialog -> reportIncidentDialog(event.visibility)
                MappingUiEvent.ResetIncidentReport -> removeIncidentImage()
            }
        }
    )

}

