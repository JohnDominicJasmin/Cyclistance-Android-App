package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import com.example.cyclistance.R
import com.example.cyclistance.core.domain.model.AlertDialogState
import com.example.cyclistance.core.utils.connection.ConnectionStatus.checkLocationSetting
import com.example.cyclistance.core.utils.connection.ConnectionStatus.hasGPSConnection
import com.example.cyclistance.core.utils.constants.EmergencyCallConstants.SHOULD_OPEN_CONTACT_DIALOG
import com.example.cyclistance.core.utils.constants.MappingConstants.DEFAULT_CAMERA_ANIMATION_DURATION
import com.example.cyclistance.core.utils.constants.MappingConstants.DEFAULT_LATITUDE
import com.example.cyclistance.core.utils.constants.MappingConstants.DEFAULT_LONGITUDE
import com.example.cyclistance.core.utils.constants.MappingConstants.FAST_CAMERA_ANIMATION_DURATION
import com.example.cyclistance.core.utils.constants.MappingConstants.LOCATE_USER_ZOOM_LEVEL
import com.example.cyclistance.core.utils.constants.MappingConstants.ROUTE_SOURCE_ID
import com.example.cyclistance.core.utils.constants.MappingConstants.SELECTION_RESCUEE_TYPE
import com.example.cyclistance.core.utils.constants.MappingConstants.SELECTION_RESCUER_TYPE
import com.example.cyclistance.core.utils.constants.NavigationConstants.LATITUDE
import com.example.cyclistance.core.utils.constants.NavigationConstants.LONGITUDE
import com.example.cyclistance.core.utils.contexts.callPhoneNumber
import com.example.cyclistance.core.utils.contexts.shareLocation
import com.example.cyclistance.core.utils.contexts.startLocationServiceIntentAction
import com.example.cyclistance.core.utils.permissions.requestPermission
import com.example.cyclistance.feature_emergency_call.presentation.emergency_call_screen.EmergencyCallViewModel
import com.example.cyclistance.feature_mapping.domain.model.Role
import com.example.cyclistance.feature_mapping.domain.model.ui.camera.CameraState
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components.*
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.event.MappingEvent
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.event.MappingUiEvent
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.event.MappingVmEvent
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.state.MappingUiState
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.utils.BottomSheetType
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.utils.MappingUtils
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.utils.MappingUtils.animateCameraPosition
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.utils.MappingUtils.changeToNormalPuckIcon
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.utils.MappingUtils.openNavigationApp
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.utils.MarkerSnippet
import com.example.cyclistance.navigation.Screens
import com.example.cyclistance.navigation.nav_graph.navigateScreen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.mapbox.core.constants.Constants.PRECISION_6
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
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
    paddingValues: PaddingValues,
    isNavigating: Boolean,
    onChangeNavigatingState: (isNavigating: Boolean) -> Unit,
    navController: NavController) {


    val context = LocalContext.current
    val state by mappingViewModel.state.collectAsStateWithLifecycle()
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
            confirmStateChange = {false})
    )

    val collapseBottomSheet = remember {
        {
            coroutineScope.launch {
                if (bottomSheetScaffoldState.bottomSheetState.isExpanded) {
                    bottomSheetScaffoldState.bottomSheetState.collapse()
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
                if (isNavigating) {

                    val buildLocationComponentActivationOptions =
                        LocationComponentActivationOptions.builder(context, style)
                            .locationComponentOptions(locationComponentOptions.build())
                            .build()
                    mapboxMap?.locationComponent?.apply {
                        activateLocationComponent(buildLocationComponentActivationOptions)
                        isLocationComponentEnabled = userLocationAvailable
                        cameraMode = CameraMode.NONE
                        renderMode = RenderMode.GPS

                    }

                } else {
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


    val onInitializeMapboxMap = remember(userLocationAvailable) {
        { mbm: MapboxMap ->
            if (mapboxMap == null) {
                mapboxMap = mbm
            }

            if (userLocationAvailable) {
                val camera = cameraState
                locateUser(camera.zoom, camera.position, FAST_CAMERA_ANIMATION_DURATION)

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

    val onClickRequestHelpButton = remember {
        {
            foregroundLocationPermissionsState.requestPermission(
                onGranted = {
                    context.startLocationServiceIntentAction()
                    requestHelp()
                }, onExplain = {
                    uiState = uiState.copy(locationPermissionDialogVisible = true)
                })

        }
    }


    val showRouteDirection = remember(uiState.routeDirection, mapboxMap) {
        {

            uiState.routeDirection?.geometry?.let { geometry ->

                mapboxMap?.getStyle { style ->
                    if (style.isFullyLoaded.not() || geometry.isEmpty()) {
                        return@getStyle
                    }

                    val routeLineSource = style.getSourceAs<GeoJsonSource>(ROUTE_SOURCE_ID)
                    routeLineSource?.setGeoJson(
                        FeatureCollection.fromFeature(
                            Feature.fromGeometry(
                                LineString.fromPolyline(geometry, PRECISION_6))))
                }
            }
            Unit
        }
    }

    val removeRouteDirection = remember(mapboxMap) {
        {
            mapboxMap?.getStyle { style ->

                if (style.isFullyLoaded.not()) {
                    return@getStyle
                }

                val routeLineSource = style.getSourceAs<GeoJsonSource>(ROUTE_SOURCE_ID)
                routeLineSource?.setGeoJson(FeatureCollection.fromFeatures(arrayOf()))
            }
            Unit
        }
    }


    val onClickLocateUserButton = remember {
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
                        locateUser(LOCATE_USER_ZOOM_LEVEL, point, DEFAULT_CAMERA_ANIMATION_DURATION)
                    }

                }, onExplain = {
                    uiState = uiState.copy(locationPermissionDialogVisible = true)
                })
        }
    }

    val openNavigationApp = remember(state.rescueTransaction?.route) {
        {
            val rescueTransaction = state.rescueTransaction
            rescueTransaction?.let{
                val latitude =  it.getDestinationLatitude() ?: return@let
                val longitude = it.getDestinationLongitude() ?: return@let
                context.openNavigationApp(latitude = latitude, longitude = longitude)
            }

        }
    }

    val onClickRouteOverViewButton = remember(mapboxMap) {
        {
            mapboxMap?.locationComponent?.cameraMode = CameraMode.TRACKING
        }
    }

    val onClickRecenterButton = remember(mapboxMap) {
        {
            mapboxMap?.locationComponent?.cameraMode = CameraMode.TRACKING_GPS
        }
    }

    val onClickOpenNavigationButton = remember {
        {
            openNavigationApp()
        }
    }

    val onClickCancelSearchButton = remember {
        {
            coroutineScope.launch {
                collapseBottomSheet()
            }.invokeOnCompletion {
                mappingViewModel.onEvent(event = MappingVmEvent.CancelRequestHelp)
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


    DisposableEffect(key1 = true){
        onDispose {
            val camera = mapboxMap?.cameraPosition
            val cameraCenter = camera?.target ?: LatLng(
                 DEFAULT_LATITUDE,
                 DEFAULT_LONGITUDE,
            )
            val cameraZoom = camera?.zoom
            onChangeCameraPosition(CameraState(
                position = cameraCenter,
                zoom = cameraZoom ?: 0.0
            ))
        }
    }

    val onClickCancelRescueButton = remember(state.rescuer, state.rescueTransaction) {
        {
            val role = state.user.transaction?.role
            val isRescuee = role == Role.RESCUEE.name.lowercase()
            val transactionId = state.rescueTransaction?.id
            val selectionType = if (isRescuee) SELECTION_RESCUEE_TYPE else SELECTION_RESCUER_TYPE
            val clientId = state.rescuer?.id ?: state.rescuee?.id

            navController.navigateScreen(route = "${Screens.MappingNavigation.Cancellation.screenRoute}/$selectionType/$transactionId/$clientId")

        }
    }

    val onDismissNoInternetDialog = remember {
        {
            uiState = uiState.copy(
                isNoInternetVisible = false
            )
        }
    }

    val hasTransaction = remember(key1 = state.rescueTransaction, key2 = state.user.transaction) {
        val transaction = state.rescueTransaction
        val rescueTransactionId = state.rescueTransaction?.id ?: ""
        val userTransactionId = state.user.transaction?.transactionId ?: ""
        transaction != null && rescueTransactionId.isNotEmpty() && userTransactionId.isNotEmpty()
    }

    val isRescueCancelled =
        remember(state.rescueTransaction) {
            (state.rescueTransaction?.cancellation)?.rescueCancelled == true
        }

    val clientPhoneNumber = remember(state.rescuee, state.rescuer) {
        val client = state.rescuee ?: state.rescuer
        client?.contactNumber
    }

    val callClient = remember(clientPhoneNumber) {
        {
            clientPhoneNumber?.let(context::callPhoneNumber)
        }
    }
    val phonePermissionState =
        rememberPermissionState(permission = Manifest.permission.CALL_PHONE) { permissionGranted ->
            if (permissionGranted) {
                callClient()
            }
        }
    val onClickChatButton = remember(clientPhoneNumber) {
        {


        }
    }

    val onClickCallButton = remember(clientPhoneNumber) {
        {
            phonePermissionState.requestPermission(
                onGranted = {
                    callClient()
                },
                onExplain = {
                    uiState = uiState.copy(phonePermissionDialogVisible = true)
                })
        }
    }

    val onRequestNavigationCameraToOverview = remember(mapboxMap) {
        {
            val locationComponent = mapboxMap?.locationComponent
            locationComponent?.cameraMode = CameraMode.TRACKING
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


    val onCollapseExpandableFAB = remember {
        {
            uiState = uiState.copy(
                isFabExpanded = false
            )
        }
    }


    val onToggleExpandedFAB = remember {
        {
            uiState = uiState.copy(
                isFabExpanded = !uiState.isFabExpanded
            )
        }
    }


    val onMapMarkerClick = remember {
        { snippet: String, id: String ->
            if (snippet == MarkerSnippet.HazardousLaneSnippet.type) {
                mappingViewModel.onEvent(event = MappingVmEvent.SelectHazardousLaneMarker(id))
            } else {
                mappingViewModel.onEvent(event = MappingVmEvent.SelectRescueMapIcon(id))
            }
        }
    }

    val onDismissHazardousLaneMarkerDialog = remember{{
        uiState = uiState.copy(
            deleteHazardousMarkerVisible = false
        )
    }}

    val onMapClick = remember {
        {
            if (uiState.bottomSheetType == BottomSheetType.ReportIncident.type) {
                collapseBottomSheet()
            }

            if(uiState.bottomSheetType == BottomSheetType.IncidentDescription.type){
                collapseBottomSheet()
            }
            onDismissRescueeBanner()
            onCollapseExpandableFAB()

        }
    }

    val onMapLongClick = remember {
        { latLng: LatLng ->
            onDismissRescueeBanner()
            onCollapseExpandableFAB()
            uiState = uiState.copy(
                lastLongPressedLocation = latLng,
                bottomSheetType = BottomSheetType.ReportIncident.type).also {
                expandBottomSheet()
            }
        }
    }

    val onClickRespondToHelpButton = remember {
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
    val onClickOkAcceptedRescue = remember {
        {
            onChangeNavigatingState(true)
            uiState = uiState.copy(
                rescueRequestAccepted = false,
                bottomSheetType = BottomSheetType.OnGoingRescue.type
            ).also {
                expandBottomSheet()
            }
        }
    }

    val onDismissLocationPermissionDialog = remember {
        {
            uiState = uiState.copy(locationPermissionDialogVisible = false)
        }
    }

    val onDismissPhonePermissionDialog = remember {
        {
            uiState = uiState.copy(phonePermissionDialogVisible = false)
        }
    }
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
            uiState.lastLongPressedLocation?.let { locationLatLng ->
                mappingViewModel.onEvent(
                    event = MappingVmEvent.ReportIncident(
                        label = incidentLabel,
                        latLng = locationLatLng,
                        description = incidentDescription.text
                    ))
                uiState = uiState.copy(selectedIncidentLabel = "")
                incidentDescription = TextFieldValue()
            }
        }
    }

    val showEmergencyCallDialog = remember {
        {
            uiState = uiState.copy(
                isEmergencyCallDialogVisible = true
            )
        }
    }

    val dismissEmergencyCallDialog = remember {
        {
            uiState = uiState.copy(
                isEmergencyCallDialogVisible = false
            )
        }
    }

    val onDismissAlertDialog = remember {
        {
            uiState = uiState.copy(
                alertDialogState = AlertDialogState()
            )
        }
    }

    val onShowRescueRequestDialog = remember {
        {
            uiState = uiState.copy(
                isRescueRequestDialogVisible = true
            )
        }
    }

    val onDismissRescueRequestDialog = remember {
        {
            uiState = uiState.copy(
                isRescueRequestDialogVisible = false
            )
        }
    }

    val onDismissSinoTrackWebView = remember {
        {
            uiState = uiState.copy(
                isSinoTrackWebViewVisible = false
            )
        }
    }

    val onShowSinoTrackWebView = remember {
        {
            uiState = uiState.copy(
                isSinoTrackWebViewVisible = true
            )
        }
    }

    val onDismissRescueResultsDialog = remember {
        {
            uiState = uiState.copy(
                isRescueResultsDialogVisible = false
            )
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
            navController.navigateScreen(Screens.EmergencyCallNavigation.EmergencyCall.screenRoute + "?$SHOULD_OPEN_CONTACT_DIALOG=${true}")
        }
    }

    val shareLocation = remember(state.userLocation, state.user.location){{

        val location = state.getCurrentLocation()

        if(location == null){
            Toast.makeText(context, "Searching for GPS", Toast.LENGTH_SHORT).show()
        }else{
            context.shareLocation(
                latitude = location.latitude!!,
                longitude = location.longitude!!
            )
        }
    }}

    val onOpenHazardousLaneBottomSheet = remember{{

        uiState = if(uiState.bottomSheetType == BottomSheetType.HazardousLane.type){
            collapseBottomSheet()
            uiState.copy(bottomSheetType = null)
        }else{
            uiState.copy(
                bottomSheetType = BottomSheetType.HazardousLane.type
            ).also {
                expandBottomSheet()
            }
        }
    }}

    val onSelectMapType = remember(key1 = state.userLocation){{ mapType: String ->
        if(state.userLocation == null){
            Toast.makeText(context, "Searching for GPS", Toast.LENGTH_SHORT).show()
        }else{
            mappingViewModel.onEvent(event = MappingVmEvent.SetMapType(mapType))
        }
    }}


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


    val onClickDeleteIncident = remember{{

        uiState = uiState.copy(
            deleteHazardousMarkerVisible = true
        )

    }}

    val onConfirmDeleteIncident = remember(uiState.selectedHazardousMarker){{
        mappingViewModel.onEvent(event = MappingVmEvent.DeleteHazardousLaneMarker(
            id = uiState.selectedHazardousMarker!!.id
        ))
    }}





















    BackHandler(enabled = bottomSheetScaffoldState.bottomSheetState.isExpanded) {
        collapseBottomSheet()
    }


    LaunchedEffect(key1 = true) {

        mappingViewModel.eventFlow.collect { event ->
            when (event) {

                is MappingEvent.RequestHelpSuccess -> {
                    navController.navigateScreen(
                        Screens.MappingNavigation.ConfirmDetails.screenRoute + "?$LATITUDE=${state.userLocation?.latitude}&$LONGITUDE=${state.userLocation?.longitude}")
                }

                is MappingEvent.InsufficientUserCredential -> {
                    navController.navigateScreen(
                        Screens.SettingsNavigation.EditProfile.screenRoute)
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

                is MappingEvent.NoInternetConnection -> {
                    uiState = uiState.copy(
                        isNoInternetVisible = true
                    )
                }

                is MappingEvent.NewSelectedRescuee -> {
                    uiState = uiState.copy(
                        mapSelectedRescuee = event.selectedRescuee,
                        requestHelpButtonVisible = false
                    )
                }

                is MappingEvent.NewRouteDirection -> {
                    uiState = uiState.copy(
                        routeDirection = event.routeDirection
                    )
                }

                is MappingEvent.RemoveAssignedTransactionSuccess -> {
                    uiState = uiState.copy(
                        rescueRequestAccepted = false,
                        requestHelpButtonVisible = true,
                        searchingAssistance = false,
                        routeDirection = null,
                        mapSelectedRescuee = null,
                    ).also {
                        collapseBottomSheet()
                    }
                    onChangeNavigatingState(false)

                }

                is MappingEvent.RescueRequestAccepted -> {
                    uiState = uiState.copy(
                        rescueRequestAccepted = true
                    )
                }

                is MappingEvent.CancelHelpRequestSuccess -> {
                    uiState = uiState.copy(
                        requestHelpButtonVisible = true
                    )
                }

                is MappingEvent.AcceptRescueRequestSuccess -> {
                    uiState = uiState.copy(
                        requestHelpButtonVisible = false,
                        bottomSheetType = BottomSheetType.OnGoingRescue.type
                    ).also {
                        expandBottomSheet()
                        onDismissRescueRequestDialog()
                    }
                }

                is MappingEvent.FailedToCalculateDistance -> {
                    Toast.makeText(context, "Failed to Calculate Distance", Toast.LENGTH_SHORT)
                        .show()
                }

                is MappingEvent.DestinationReached -> {
                    val role = state.user.transaction?.role
                    val type = if (role == Role.RESCUEE.name.lowercase()) {
                        BottomSheetType.RescuerArrived.type
                    } else {
                        BottomSheetType.DestinationReached.type
                    }
                    uiState = uiState.copy(bottomSheetType = type).also {
                        expandBottomSheet()
                    }

                }

                is MappingEvent.RemoveRespondentFailed -> {
                    Toast.makeText(context, event.reason, Toast.LENGTH_SHORT).show()
                }

                is MappingEvent.RescueHasTransaction -> {
                    uiState = uiState.copy(
                        alertDialogState = AlertDialogState(
                            title = "Cannot Request",
                            description = "Unfortunately the Rescuer is currently in a Rescue.",
                            icon = R.raw.error
                        )
                    )
                }

                is MappingEvent.UserHasCurrentTransaction -> {
                    uiState = uiState.copy(
                        alertDialogState = AlertDialogState(
                            title = "Cannot Request",
                            description = "You can only have one transaction at a time",
                            icon = R.raw.error
                        )
                    )
                }

                is MappingEvent.NewBottomSheetType -> {
                    uiState = uiState.copy(bottomSheetType = event.bottomSheetType).also {
                        expandBottomSheet()
                    }
                }

                is MappingEvent.ReportIncidentFailed -> {
                    Toast.makeText(context, event.reason, Toast.LENGTH_SHORT).show()
                }

                MappingEvent.ReportIncidentSuccess -> {
                    Toast.makeText(context, "Incident Reported", Toast.LENGTH_SHORT).show()
                }

                is MappingEvent.IncidentDistanceTooFar -> {
                    uiState = uiState.copy(
                    alertDialogState = AlertDialogState(
                        title = "Exceeds Reachable Distance",
                        description = "The incident is taking place quite a distance away from your current location, making it challenging to directly engage or intervene.",
                        icon = R.raw.error
                    ))
                }

                is MappingEvent.SelectHazardousLaneMarker -> {
                    uiState = uiState.copy(
                        selectedHazardousMarker = event.marker,
                        bottomSheetType = BottomSheetType.IncidentDescription.type).also {
                        expandBottomSheet()
                    }
                }

                is MappingEvent.DeleteHazardousLaneMarkerFailed -> {
                    collapseBottomSheet()
                    Toast.makeText(context, event.reason, Toast.LENGTH_LONG).show()
                }

                MappingEvent.DeleteHazardousLaneMarkerSuccess -> {
                    collapseBottomSheet()
                    Toast.makeText(context, "Marker Deleted", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    LaunchedEffect(key1 = uiState.routeDirection, key2 = mapboxMap) {

        val route = uiState.routeDirection ?: return@LaunchedEffect

        if (route.geometry.isEmpty()) {
            removeRouteDirection()
            return@LaunchedEffect
        }
        showRouteDirection()
    }
    LaunchedEffect(
        key1 = state.rescueTransaction?.route,
        key2 = hasTransaction,
        key3 = isRescueCancelled) {

        val rescueTransaction = state.rescueTransaction


        if (hasTransaction.not() || isRescueCancelled) {
            uiState = uiState.copy(routeDirection = null)
            return@LaunchedEffect
        }


        val startingLongitude = rescueTransaction?.getStartingLongitude() ?: return@LaunchedEffect
        val startingLatitude = rescueTransaction.getStartingLatitude() ?: return@LaunchedEffect
        val destinationLongitude = rescueTransaction.getDestinationLongitude() ?: return@LaunchedEffect
        val destinationLatitude = rescueTransaction.getDestinationLatitude() ?: return@LaunchedEffect

        mappingViewModel.onEvent(
            event = MappingVmEvent.GetRouteDirections(
                origin = Point.fromLngLat(startingLongitude, startingLatitude),
                destination = Point.fromLngLat(
                    destinationLongitude,
                    destinationLatitude)))


    }
    LaunchedEffect(key1 = hasInternetConnection) {
        val nearbyCyclistLoaded = state.nearbyCyclist?.users?.isNotEmpty() ?: false
        val userLoaded = state.user.id != null
        val dataHaveBeenLoaded = userLoaded && nearbyCyclistLoaded

        if (hasInternetConnection.not()) {
            return@LaunchedEffect
        }

        if (dataHaveBeenLoaded.not()) {
            mappingViewModel.onEvent(MappingVmEvent.LoadData)
        }
        mappingViewModel.onEvent(MappingVmEvent.SubscribeToDataChanges)
    }
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
    LaunchedEffect(key1 = hasTransaction, key2 = isRescueCancelled) {

        if (hasTransaction.not()) {
            return@LaunchedEffect
        }

        if (isRescueCancelled) {
            return@LaunchedEffect
        }

        onChangeNavigatingState(false)

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
        hasTransaction = hasTransaction,
        isRescueCancelled = isRescueCancelled,
        hazardousLaneMarkers = hazardousMarkers,
        isNavigating = isNavigating,
        mapboxMap = mapboxMap,
        uiState = uiState,
        emergencyState = emergencyState,
        incidentDescription = incidentDescription,
        event = { event ->
            when (event) {
                is MappingUiEvent.RequestHelp -> onClickRequestHelpButton()
                is MappingUiEvent.RespondToHelp -> onClickRespondToHelpButton()
                is MappingUiEvent.CancelSearchConfirmed -> onClickCancelSearchButton()
                is MappingUiEvent.CallRescueTransaction -> onClickCallButton()
                is MappingUiEvent.ChatRescueTransaction -> onClickChatButton()
                is MappingUiEvent.CancelRescueTransaction -> onClickCancelRescueButton()
                is MappingUiEvent.CancelledRescueConfirmed -> onClickOkCancelledRescue()
                is MappingUiEvent.OnInitializeMap -> onInitializeMapboxMap(event.mapboxMap)
                is MappingUiEvent.RescueRequestAccepted -> onClickOkAcceptedRescue()
                is MappingUiEvent.OnChangeCameraState -> onChangeCameraPosition(event.cameraState)
                is MappingUiEvent.DismissNoInternetDialog -> onDismissNoInternetDialog()
                is MappingUiEvent.OnMapClick -> onMapClick()
                is MappingUiEvent.DismissBanner -> onDismissRescueeBanner()
                is MappingUiEvent.LocateUser -> onClickLocateUserButton()
                is MappingUiEvent.RouteOverview -> onClickRouteOverViewButton()
                is MappingUiEvent.RecenterRoute -> onClickRecenterButton()
                is MappingUiEvent.OpenNavigation -> onClickOpenNavigationButton()
                is MappingUiEvent.OnRequestNavigationCameraToOverview -> onRequestNavigationCameraToOverview()
                is MappingUiEvent.RescueArrivedConfirmed -> {}
                is MappingUiEvent.DestinationReachedConfirmed -> {}
                is MappingUiEvent.DismissLocationPermission -> onDismissLocationPermissionDialog()
                is MappingUiEvent.DismissPhonePermission -> onDismissPhonePermissionDialog()
                is MappingUiEvent.OnToggleExpandableFAB -> onToggleExpandedFAB()
                is MappingUiEvent.ShowEmergencyCallDialog -> showEmergencyCallDialog()
                is MappingUiEvent.DismissEmergencyCallDialog -> dismissEmergencyCallDialog()
                is MappingUiEvent.OpenFamilyTracker -> shareLocation()
                is MappingUiEvent.ShowRescueRequestDialog -> onShowRescueRequestDialog()
                is MappingUiEvent.DismissRescueRequestDialog -> onDismissRescueRequestDialog()
                is MappingUiEvent.CancelRequestHelp -> onClickCancelButton(event.id)
                is MappingUiEvent.ConfirmRequestHelp -> onClickConfirmButton(event.id)
                is MappingUiEvent.DismissAlertDialog -> onDismissAlertDialog()
                is MappingUiEvent.OnCollapseExpandableFAB -> onCollapseExpandableFAB()
                is MappingUiEvent.OnMapLongClick -> onMapLongClick(event.latLng)
                is MappingUiEvent.OnReportIncident -> onClickReportIncident(event.labelIncident)
                is MappingUiEvent.DismissSinoTrackWebView -> onDismissSinoTrackWebView()
                is MappingUiEvent.ShowSinoTrackWebView -> onShowSinoTrackWebView()
                is MappingUiEvent.DismissRescueResultsDialog -> onDismissRescueResultsDialog()
                is MappingUiEvent.OnEmergencyCall -> onEmergencyCall(event.phoneNumber)
                is MappingUiEvent.OnAddEmergencyContact -> onAddEmergencyContact()
                is MappingUiEvent.OpenHazardousLaneBottomSheet -> onOpenHazardousLaneBottomSheet()
                is MappingUiEvent.OnSelectMapType -> onSelectMapType(event.mapType)
                is MappingUiEvent.OnChangeIncidentDescription -> onChangeIncidentDescription(event.description)
                is MappingUiEvent.OnChangeIncidentLabel -> onChangeIncidentLabel(event.label)
                is MappingUiEvent.OnClickDeleteIncident -> onClickDeleteIncident()
                is MappingUiEvent.OnClickEditIncidentDescription -> TODO()
                is MappingUiEvent.OnClickOkayIncidentDescription -> TODO()
                is MappingUiEvent.OnClickMapMarker -> onMapMarkerClick(event.markerSnippet, event.markerId)
                MappingUiEvent.DismissHazardousLaneMarkerDialog -> onDismissHazardousLaneMarkerDialog()
                MappingUiEvent.OnConfirmDeleteIncident -> onConfirmDeleteIncident()
            }
        }


    )

}


