package com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.maps.android.SphericalUtil
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.geometry.LatLng
import com.myapp.cyclistance.core.utils.constants.MappingConstants.DEFAULT_RADIUS
import com.myapp.cyclistance.core.utils.constants.MappingConstants.MAPPING_VM_STATE_KEY
import com.myapp.cyclistance.core.utils.formatter.FormatterUtils
import com.myapp.cyclistance.core.utils.formatter.FormatterUtils.formatToDistanceKm
import com.myapp.cyclistance.core.utils.formatter.FormatterUtils.isLocationAvailable
import com.myapp.cyclistance.core.utils.formatter.FormatterUtils.toRescueDescription
import com.myapp.cyclistance.feature_authentication.domain.use_case.AuthenticationUseCase
import com.myapp.cyclistance.feature_mapping.data.mapper.UserMapper.toRescueRequest
import com.myapp.cyclistance.feature_mapping.domain.exceptions.MappingExceptions
import com.myapp.cyclistance.feature_mapping.domain.helper.TrackingStateHandler
import com.myapp.cyclistance.feature_mapping.domain.model.Role
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.hazardous_lane.HazardousLaneMarkerDetails
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.live_location.LiveLocationSocketModel
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.rescue.RescueRequestItemModel
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.rescue_transaction.RescueTransaction
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.user.LocationModel
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.user.NearbyCyclist
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.user.RescuePending
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.user.RescueRequest
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.user.UserAssistanceModel
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.user.UserItem
import com.myapp.cyclistance.feature_mapping.domain.model.ui.rescue.MapSelectedRescuee
import com.myapp.cyclistance.feature_mapping.domain.model.ui.rescue.NewRescueRequestsModel
import com.myapp.cyclistance.feature_mapping.domain.use_case.MappingUseCase
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.event.MappingEvent
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.event.MappingVmEvent
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.state.MappingState
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.utils.createMockUsers
import com.myapp.cyclistance.feature_messaging.domain.use_case.MessagingUseCase
import com.myapp.cyclistance.feature_report_account.domain.model.BannedAccountDetails
import com.myapp.cyclistance.feature_report_account.domain.use_case.ReportAccountUseCase
import com.myapp.cyclistance.feature_rescue_record.domain.model.ui.RideDetails
import com.myapp.cyclistance.feature_rescue_record.domain.model.ui.RideMetrics
import com.myapp.cyclistance.feature_rescue_record.domain.use_case.RescueRecordUseCase
import com.myapp.cyclistance.feature_user_profile.domain.model.UserStats
import com.myapp.cyclistance.feature_user_profile.domain.use_case.UserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import com.google.android.gms.maps.model.LatLng as GoogleLatLng


@HiltViewModel
class MappingViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val authUseCase: AuthenticationUseCase,
    private val mappingUseCase: MappingUseCase,
    private val userProfileUseCase: UserProfileUseCase,
    private val defaultDispatcher: CoroutineDispatcher,
    private val messagingUseCase: MessagingUseCase,
    private val rescueRecordUseCase: RescueRecordUseCase,
    private val reportAccountUseCase: ReportAccountUseCase
) : ViewModel() {


    private var locationUpdatesJob: Job? = null
    private var trackingHandler: TrackingStateHandler

    private val _state: MutableStateFlow<MappingState> = MutableStateFlow(
        savedStateHandle[MAPPING_VM_STATE_KEY] ?: MappingState(userId = getId())
    )
    val state = _state.asStateFlow()

    private val _eventFlow: MutableSharedFlow<MappingEvent> = MutableSharedFlow()
    val eventFlow: SharedFlow<MappingEvent> = _eventFlow.asSharedFlow()
    private var travelledPath: MutableList<GoogleLatLng> = mutableStateListOf()

    private val _hazardousLaneMarkers = mutableStateListOf<HazardousLaneMarkerDetails>()
    val hazardousLaneMarkers: List<HazardousLaneMarkerDetails> = _hazardousLaneMarkers

    init {
        trackingHandler = TrackingStateHandler(state = _state, eventFlow = _eventFlow)
        getBannedAccountDetails()
        loadData()
        observeDataChanges()
        getDefaultMapTypeSelected()
        getHazardousMapTypeSelected()
        getTrafficMapTypeSelected()
        getShouldShowHazardousStartingInfo()
        refreshToken()
    }
    private fun getBannedAccountDetails(){
        viewModelScope.launch {
            val bannedAccountDetails = reportAccountUseCase.getBannedAccountDetailsUseCase(userId = getId())
            _state.update { it.copy(bannedAccountDetails = bannedAccountDetails) }
        }
    }

    private fun setShouldShowHazardousStartingInfo(shouldShow: Boolean) {
        viewModelScope.launch {
            mappingUseCase.shouldHazardousStartingInfoUseCase(shouldShow = shouldShow)
        }
    }

    private fun getShouldShowHazardousStartingInfo() {
        viewModelScope.launch {
            mappingUseCase.shouldHazardousStartingInfoUseCase().catch {
                it.handleException()
            }.onEach { shouldShow ->
                _state.update { it.copy(shouldShowHazardousStartingInfo = shouldShow) }
            }.launchIn(this)
        }
    }


    private fun observeDataChanges() {
        subscribeToLocationUpdates()
        subscribeToNearbyUsersUpdates()
        subscribeToRescueTransactionUpdates()
        subscribeToTransactionLocationUpdates()
        subscribeToBottomSheetTypeUpdates()
        subscribeToHazardousLaneUpdates()
    }

    private fun getDefaultMapTypeSelected() {
        viewModelScope.launch {
            mappingUseCase.defaultMapTypeUseCase()
                .distinctUntilChanged()
                .catch {
                    Timber.v("Error getting map type: ${it.message}")
                }.onEach { mapTypeSelected ->
                    _state.update { it.copy(defaultMapTypeSelected = mapTypeSelected) }
                }.launchIn(this)

        }
    }

    private fun getHazardousMapTypeSelected(){
        viewModelScope.launch {
            mappingUseCase.hazardousMapTypeUseCase()
                .distinctUntilChanged()
                .catch {
                    Timber.v("Error getting map type: ${it.message}")
                }.onEach {hazardousMapSelected ->
                    _state.update { it.copy(hazardousMapTypeSelected = hazardousMapSelected) }
                }.launchIn(this)
        }
    }

    private fun getTrafficMapTypeSelected(){
        viewModelScope.launch {
            mappingUseCase.trafficMapTypeUseCase()
                .distinctUntilChanged()
                .catch {
                    Timber.v("Error getting map type: ${it.message}")
                }.onEach { mapTypeSelected ->
                    _state.update { it.copy(trafficMapTypeSelected = mapTypeSelected) }
                }.launchIn(this)
        }
    }

    private fun toggleDefaultMapType() {
        viewModelScope.launch {
            mappingUseCase.defaultMapTypeUseCase(isSelected = !state.value.defaultMapTypeSelected)
        }
    }

    private fun toggleHazardousMapType(){
        viewModelScope.launch {
            mappingUseCase.hazardousMapTypeUseCase(isSelected = !state.value.hazardousMapTypeSelected)
        }
    }

    private fun toggleTrafficMapType(){
        viewModelScope.launch {
            mappingUseCase.trafficMapTypeUseCase(isSelected = !state.value.trafficMapTypeSelected)
        }
    }


    private fun subscribeToHazardousLaneUpdates() {
        viewModelScope.launch(SupervisorJob() + defaultDispatcher) {
            mappingUseCase.newHazardousLaneUseCase(
                onAddedHazardousMarker = ::handleAddedHazardousMarker,
                onModifiedHazardousMarker = ::handleModifiedHazardousMarker,
                onRemovedHazardousMarker = ::handleRemovedHazardousMarker
            )
        }
    }

    private fun handleAddedHazardousMarker(marker: HazardousLaneMarkerDetails) {
        _hazardousLaneMarkers.removeAll { it.id == marker.id }
        _hazardousLaneMarkers.add(marker)
    }

    private fun handleModifiedHazardousMarker(modifiedMarker: HazardousLaneMarkerDetails) {
        _hazardousLaneMarkers.removeAll { it.id == modifiedMarker.id }
        _hazardousLaneMarkers.add(modifiedMarker)
    }

    private fun handleRemovedHazardousMarker(markerId: String) {
        _hazardousLaneMarkers.removeAll { it.id == markerId }
    }


    private fun subscribeToBottomSheetTypeUpdates() {
        viewModelScope.launch(context = SupervisorJob() + defaultDispatcher) {
            mappingUseCase.bottomSheetTypeUseCase().catch {
                it.handleException()
            }.onEach {
                if (it.isEmpty()) {
                    return@onEach
                }
                _eventFlow.emit(value = MappingEvent.NewBottomSheetType(it))
            }.launchIn(this)
        }
    }

    private fun loadData() {
        viewModelScope.launch(SupervisorJob() + defaultDispatcher) {
//            createMockUpUsers()
            trackingHandler.updateClient()

        }
    }




    private fun acceptRescueRequest(id: String) {
        viewModelScope.launch(context = SupervisorJob() + defaultDispatcher) {

            val bannedAccountDetails = state.value.bannedAccountDetails ?: BannedAccountDetails()
            val isAccountBanned = bannedAccountDetails.isAccountStillBanned
            if(isAccountBanned){
                _eventFlow.emit(value = MappingEvent.AccountBanned(bannedAccountDetails = bannedAccountDetails))
                return@launch
            }

            val rescuer = state.value.nearbyCyclist?.findUser(id) ?: return@launch
            val user = state.value.user


            trackingHandler.checkCurrentTransactions(user = user, rescuer = rescuer) {

                coroutineScope {
                    runCatching {
                        isLoading(true)
                        mappingUseCase.acceptRescueRequestUseCase(
                            userId = getId(),
                            rescuerId = id
                        )
                    }.onSuccess {
                        broadcastToNearbyCyclists()
                        _eventFlow.emit(value = MappingEvent.AcceptRescueRequestSuccess)
                        delay(500)
                        updateTransactionETA(rescuer)
                        isLoading(false)
                        broadcastRescueTransaction()
                        user.location?.let { broadcastRescueTransactionToRespondent(it) }
                    }.onFailure { exception ->
                        isLoading(false)
                        exception.handleException()
                    }
                }

                savedStateHandle[MAPPING_VM_STATE_KEY] = state.value

            }
        }
    }

    private fun cancelHelpRequest() {
        viewModelScope.launch(context = defaultDispatcher) {
            runCatching {
                isLoading(true)
                cancelUserHelpRequest()
            }.onSuccess {
                _eventFlow.emit(value = MappingEvent.CancelHelpRequestSuccess)
                broadcastToNearbyCyclists()
            }.onFailure { exception ->
                Timber.e("Failed to cancel search assistance: ${exception.message}")
                exception.handleException()
            }
            isLoading(false)
            savedStateHandle[MAPPING_VM_STATE_KEY] = state.value
        }
    }

    private fun destinationArrived() {
        viewModelScope.launch(context = defaultDispatcher) {
            runCatching {
                rescuerArrived()
                removeUserTransaction(id = getId())
            }.onSuccess {
                broadcastToNearbyCyclists()
                isLoading(false)
                _eventFlow.emit(value = MappingEvent.RescueArrivedSuccess)
            }.onFailure { exception ->
                isLoading(false)
                exception.handleException()
            }
        }
    }

    private fun cancelRescueTransaction() {
        viewModelScope.launch(context = defaultDispatcher + SupervisorJob()) {
            runCatching {
                removeUserTransaction(id = getId())
            }.onSuccess {
                _eventFlow.emit(value = MappingEvent.CancelRescueTransactionSuccess)
                broadcastToNearbyCyclists()
                isLoading(false)
                trackingHandler.clearTransactionRoles()
            }.onFailure { exception ->
                isLoading(false)
                exception.handleException()
            }
        }
    }

    private fun selectRescueeMapIcon(id: String) {
        viewModelScope.launch(context = defaultDispatcher) {

            val userLocation = state.value.user.location ?: state.value.userLocation

            if (!userLocation.isLocationAvailable()) {
                viewModelScope.launch(context = defaultDispatcher) {
                    _eventFlow.emit(value = MappingEvent.LocationNotAvailable("Tracking your Location"))
                }
                return@launch
            }

            calculateSelectedRescueeDistance(userLocation, id)

        }
    }

    private fun respondToHelp(selectedRescuee: MapSelectedRescuee) {
        viewModelScope.launch(context = defaultDispatcher + SupervisorJob()) {


            val bannedAccountDetails = state.value.bannedAccountDetails ?: BannedAccountDetails()
            val isAccountBanned = bannedAccountDetails.isAccountStillBanned
            if(isAccountBanned){
                _eventFlow.emit(value = MappingEvent.AccountBanned(bannedAccountDetails = bannedAccountDetails))
                return@launch
            }
            uploadUserProfile(onSuccess = {
                viewModelScope.launch(this.coroutineContext) {
                    runCatching {
                        mappingUseCase.addRescueRespondentUseCase(
                            userId = selectedRescuee.userId,
                            respondentId = getId()
                        )
                    }.onSuccess {
                        broadcastToNearbyCyclists()
                        broadcastRescueTransaction()
                        _state.update { it.copy(respondedToHelp = true) }
                        _eventFlow.emit(value = MappingEvent.RespondToHelpSuccess())

                    }.onFailure {
                        it.handleException()
                    }
                }
            })
        }
    }

    private fun showRouteDirection(origin: Point, destination: Point) {
        viewModelScope.launch(context = defaultDispatcher) {
            runCatching {
                mappingUseCase.getRouteDirectionsUseCase(origin = origin, destination = destination)
            }.onSuccess { routeDirection ->
                _eventFlow.emit(value = MappingEvent.GenerateRouteNavigationSuccess(routeDirection))
            }.onFailure {
                it.handleException()
            }
        }
    }


    private suspend fun broadcastRescueTransaction() {
        runCatching {
            mappingUseCase.broadcastRescueTransactionUseCase()
        }.onFailure {
            it.handleException()
        }
    }

    private suspend fun broadcastToNearbyCyclists() {
        val location = state.value.userLocation ?: return
        location.longitude ?: return
        location.latitude ?: return

        runCatching {
            mappingUseCase.nearbyCyclistsUseCase(
                locationModel = LiveLocationSocketModel(
                    latitude = location.latitude,
                    longitude = location.longitude
                )
            )
        }.onFailure {
            it.handleException()
        }
    }

    private fun refreshToken() {
        viewModelScope.launch(SupervisorJob()) {
            runCatching {
                isLoading(true)
                messagingUseCase.refreshTokenUseCase()
            }.onSuccess {
                Timber.v("Successfully refreshed token")
            }.onFailure {
                Timber.e("Failed to refresh token ${it.message}")
            }.also {
                isLoading(false)
            }
        }
    }

    private fun declineRescueRequest(id: String) {
        viewModelScope.launch(context = defaultDispatcher) {
            runCatching {
                isLoading(true)
                mappingUseCase.deleteRescueRespondentUseCase(userId = getId(), respondentId = id)
            }.onSuccess {
                removeRescueRespondent(id)
                broadcastToNearbyCyclists()
            }.onFailure {
                it.handleDeclineRescueRequest()
            }
            isLoading(false)
            savedStateHandle[MAPPING_VM_STATE_KEY] = state.value

        }
    }

    private suspend fun removeRescueRespondent(id: String) {
        state.value.newRescueRequest?.request?.toMutableList()?.apply {
            val respondentRemoved = removeAll { it.id == id }
            if (!respondentRemoved) {
                _eventFlow.emit(value = MappingEvent.RemoveRespondentFailed())
                return@apply
            }
            _state.update {
                it.copy(
                    newRescueRequest = NewRescueRequestsModel(this)
                )
            }
        }
    }



    private fun requestHelp() {
        viewModelScope.launch(context = defaultDispatcher + SupervisorJob()) {

            val bannedAccountDetails = state.value.bannedAccountDetails ?: BannedAccountDetails()
            val isAccountBanned = bannedAccountDetails.isAccountStillBanned
            if(isAccountBanned){
                _eventFlow.emit(value = MappingEvent.AccountBanned(bannedAccountDetails = bannedAccountDetails))
                return@launch
            }

            runCatching {
                uploadUserProfile(onSuccess = {
                    viewModelScope.launch(context = defaultDispatcher) {
                        _eventFlow.emit(MappingEvent.RequestHelpSuccess)
                        subscribeToNearbyUsersUpdates()
                        subscribeToRescueTransactionUpdates()
                        subscribeToTransactionLocationUpdates()
                    }
                })
            }.onFailure {
                it.handleException()
            }
        }.invokeOnCompletion {
            savedStateHandle[MAPPING_VM_STATE_KEY] = state.value
        }
    }

    fun onEvent(event: MappingVmEvent) {
        when (event) {


            is MappingVmEvent.GetRouteDirections -> {
                showRouteDirection(origin = event.origin, destination = event.destination)
            }

            is MappingVmEvent.RespondToHelp -> {
                respondToHelp(event.selectedRescuee)
            }

            is MappingVmEvent.SelectRescueMapIcon -> {
                selectRescueeMapIcon(event.id)
            }

            is MappingVmEvent.RequestHelp -> {
                requestHelp()
            }


            is MappingVmEvent.DeclineRescueRequest -> {
                declineRescueRequest(event.id)
                clearTravelledPath()
            }

            is MappingVmEvent.AcceptRescueRequest -> {
                acceptRescueRequest(event.id)
            }

            is MappingVmEvent.CancelSearchingAssistance -> {
                cancelHelpRequest()
                clearTravelledPath()
            }

            is MappingVmEvent.ReportIncident -> {
                reportHazardousIncident(
                    latLng = event.latLng,
                    label = event.label,
                    incidentDescription = event.description,
                    imageUri = event.imageUri)
            }



            is MappingVmEvent.SelectHazardousLaneMarker -> {
                selectHazardousLaneMarker(id = event.id)
            }

            is MappingVmEvent.DeleteHazardousLaneMarker -> {
                deleteHazardousLaneMarker(id = event.id)
            }

            is MappingVmEvent.UpdateReportedIncident -> {
                updateReportedIncident(marker = event.marker)
            }

            is MappingVmEvent.ShouldShowHazardousStartingInfo ->
                setShouldShowHazardousStartingInfo(event.shouldShow)

            is MappingVmEvent.NotifyNewRescueRequest -> {
                mappingUseCase.newRescueRequestNotificationUseCase(
                    message = event.message
                )
            }

            is MappingVmEvent.NotifyRequestAccepted -> {
                mappingUseCase.requestAcceptedNotificationUseCase(
                    message = event.message
                )
            }

            is MappingVmEvent.CancelRespondHelp -> cancelRespondToHelp(respondentId = event.id)


            is MappingVmEvent.CancelRescueTransaction -> {
                cancelRescueTransaction()
                clearTravelledPath()
            }

            is MappingVmEvent.DestinationArrived -> {
                destinationArrived()
                clearTravelledPath()
            }

            MappingVmEvent.ArrivedAtLocation -> {
                arrivedAtLocation()
            }

            MappingVmEvent.ToggleDefaultMapType -> toggleDefaultMapType()
            MappingVmEvent.ToggleHazardousMapType -> toggleHazardousMapType()
            MappingVmEvent.ToggleTrafficMapType -> toggleTrafficMapType()
        }
        savedStateHandle[MAPPING_VM_STATE_KEY] = state.value
    }


    private fun arrivedAtLocation(){
        viewModelScope.launch(Dispatchers.IO){
            runCatching {
                val transactionId = state.value.getTransactionId()
                mappingUseCase.rescueFinishUseCase(transactionId = transactionId)
            }.onSuccess {
                Timber.v("Successfully finished rescue transaction")
                broadcastRescueTransaction()
            }.onFailure {
                Timber.e("Failed to finish rescue transaction ${it.message}")
                it.handleException()
            }
        }
    }

    private suspend fun addRideDetails(rideDetails: RideDetails){
        runCatching {
            rescueRecordUseCase.rideDetailsUseCase(rideDetails = rideDetails)
        }.onSuccess {
            Timber.v("AddRideDetails Successfully added ride details")
        }.onFailure {
            Timber.v("AddRideDetails Failed to add ride details")
        }
    }

    private suspend fun addRideMetrics(rideMetrics: RideMetrics){
        runCatching {
            rescueRecordUseCase.rideMetricsUseCase(rideMetrics = rideMetrics)
        }.onSuccess {
            Timber.v("AddRideMetrics Successfully added ride metrics")
        }.onFailure {
            Timber.v("AddRideMetrics Failed to add ride metrics")
        }
    }

    private fun rescuerArrived() {
        val role = state.value.user.getRole()
        val rideDetails = trackingHandler.getRideDetails()
        viewModelScope.launch(SupervisorJob() + defaultDispatcher) {
            runCatching {
                addRideDetails(rideDetails = rideDetails)
                if (role == Role.Rescuee.name) {
                    rescueRecordUseCase.addRescueRecordUseCase(rideDetails = rideDetails)
                }else{
                    val rideMetrics = trackingHandler.getRideMetrics()
                    addRideMetrics(rideMetrics = rideMetrics)
                    rescueRecordUseCase.addRideMetricsUseCase(rideId = rideDetails.rideId, rideMetrics = rideMetrics)
                    rescueRecordUseCase.updateStatsUseCase(userStats = UserStats(
                        rescuerId = rideDetails.rescuerId,
                        rescueeId = rideDetails.rescueeId,
                        rescueOverallDistanceInMeters = rideMetrics.distanceInMeters,
                        rescueAverageSpeedMps = rideMetrics.averageSpeedMps,
                        rescueDescription = toRescueDescription(rideDetails.rideSummary.iconDescription) ?: ""
                    ))
                }
            }.onSuccess {
                trackingHandler.clearTransactionRoles()
            }.onFailure {
                _eventFlow.emit(value = MappingEvent.RescueArrivedFailed(it.message ?: "Rescuer Arrived"))
            }
        }
    }

    private fun cancelRespondToHelp(respondentId: String) {
        viewModelScope.launch {
            runCatching {
                isLoading(true)
                mappingUseCase.cancelHelpRespondUseCase(
                    userId = getId(),
                    respondentId = respondentId)
            }.onSuccess {
                _eventFlow.emit(value = MappingEvent.CancelRespondSuccess)
                broadcastToNearbyCyclists()
                _state.update { it.copy(respondedToHelp = false) }
            }.onFailure {
                it.handleException()
            }.also {
                isLoading(false)
            }
        }
    }


    private fun updateReportedIncident(marker: HazardousLaneMarkerDetails) {
        viewModelScope.launch {
            runCatching {
                mappingUseCase.updateHazardousLaneUseCase(
                    hazardousLaneMarker = marker
                )
            }.onSuccess {
                _eventFlow.emit(value = MappingEvent.UpdateIncidentSuccess)
            }.onFailure {
                _eventFlow.emit(
                    value = MappingEvent.UpdateIncidentFailed(
                        it.message ?: "Failed to update incident"))
            }
        }
    }


    private fun deleteHazardousLaneMarker(id: String) {
        viewModelScope.launch {
            runCatching {
                mappingUseCase.deleteHazardousLaneUseCase(id)
            }.onSuccess {
                _eventFlow.emit(value = MappingEvent.DeleteHazardousLaneMarkerSuccess)
            }.onFailure {
                _eventFlow.emit(
                    value = MappingEvent.DeleteHazardousLaneMarkerFailed(
                        it.message ?: "Failed to delete incident marker"))
            }
        }
    }

    private fun selectHazardousLaneMarker(id: String) {
        viewModelScope.launch {
            hazardousLaneMarkers.find { it.id == id }?.let { marker ->
                _eventFlow.emit(value = MappingEvent.SelectHazardousLaneMarker(marker))
            }
        }
    }


    private fun reportHazardousIncident(
        latLng: LatLng,
        label: String,
        incidentDescription: String,
        imageUri: String,) {

        viewModelScope.launch {
            val userLocation = state.value.getCurrentLocation()
            val bannedAccountDetails = state.value.bannedAccountDetails ?: BannedAccountDetails()
            val isAccountBanned = bannedAccountDetails.isAccountStillBanned

            if (userLocation == null) {
                _eventFlow.emit(MappingEvent.LocationNotAvailable(reason = "Searching for GPS"))
                return@launch
            }

            val distance = mappingUseCase.getCalculatedDistanceUseCase(
                startingLocation = userLocation,
                destinationLocation = LocationModel(
                    latitude = latLng.latitude,
                    longitude = latLng.longitude
                )
            )

            if (distance > DEFAULT_RADIUS) {
                _eventFlow.emit(MappingEvent.IncidentDistanceTooFar)
                return@launch
            }

            if(isAccountBanned){
                _eventFlow.emit(value = MappingEvent.AccountBanned(bannedAccountDetails = bannedAccountDetails))
                return@launch
            }


            reportIncident(
                label = label,
                latLng = latLng,
                incidentDescription = incidentDescription,
                imageUri = imageUri)


        }

    }



    private suspend fun reportIncident(
        label: String,
        latLng: LatLng,
        incidentDescription: String,
        imageUri: String) {


        coroutineScope {
            runCatching {
                mappingUseCase.newHazardousLaneUseCase(
                    hazardousLaneMarker = HazardousLaneMarkerDetails(
                        id = getId() + System.currentTimeMillis(),
                        idCreator = getId(),
                        latitude = latLng.latitude,
                        longitude = latLng.longitude,
                        label = label,
                        description = incidentDescription,
                        datePosted = java.util.Date(),
                        address = "",
                        incidentImageUri = imageUri))
            }.onSuccess {
                _eventFlow.emit(value = MappingEvent.ReportIncidentSuccess)
            }.onFailure {
                _eventFlow.emit(
                    value = MappingEvent.ReportIncidentFailed(
                        reason = it.message ?: "Failed to report incident"))
            }
        }
    }

    private suspend fun calculateSelectedRescueeDistance(userLocation: LocationModel?, id: String) {
        val selectedRescuee = state.value.nearbyCyclist?.findUser(id) ?: return
        val selectedRescueeLocation = selectedRescuee.location


        runCatching {
            isLoading(true)
            mappingUseCase.getCalculatedDistanceUseCase(
                startingLocation = LocationModel(
                    latitude = userLocation?.latitude,
                    longitude = userLocation?.longitude
                ), destinationLocation = LocationModel(
                    latitude = selectedRescueeLocation!!.latitude,
                    longitude = selectedRescueeLocation.longitude
                )
            )
        }.onSuccess { distance ->
            val timeRemaining = FormatterUtils.getCalculatedETA(distance)
            trackingHandler.showSelectedRescuee(
                selectedRescuee = selectedRescuee,
                distance = distance,
                timeRemaining = timeRemaining)
        }.onFailure {
            _eventFlow.emit(value = MappingEvent.FailedToCalculateDistance)
        }.also {
            isLoading(false)
        }
    }


    private suspend fun removeUserTransaction(id: String) {
        mappingUseCase.removeUserTransactionUseCase(id = id)
    }


    private fun subscribeToTransactionLocationUpdates() {

        viewModelScope.launch(context = SupervisorJob() + defaultDispatcher) {
            mappingUseCase.transactionLocationUseCase().distinctUntilChanged().catch {
                Timber.e("ERROR GETTING TRANSACTION LOCATION: ${it.message}")
            }.onEach { liveLocation ->
                trackingHandler.updateTransactionLocation(location = liveLocation)
                liveLocation.updateTransactionETA()
            }.launchIn(this@launch)

        }
    }



    private fun LiveLocationSocketModel.updateTransactionETA() {
        val userLocation = state.value.userLocation
        userLocation ?: return
        this.latitude ?: return
        this.longitude ?: return

        val eta = getETABetweenTwoPoints(
            startingLocation = LocationModel(
                latitude = this.latitude,
                longitude = this.longitude
            ), endLocation = userLocation
        )

        val distance = mappingUseCase.getCalculatedDistanceUseCase(
            startingLocation = LocationModel(
                latitude = this.latitude,
                longitude = this.longitude
            ),
            destinationLocation = userLocation)
        _state.update { it.copy(rescueETA = eta, rescueDistance = distance) }
    }

    private fun getETABetweenTwoPoints(
        startingLocation: LocationModel,
        endLocation: LocationModel
    ): String {
        val distance = mappingUseCase.getCalculatedDistanceUseCase(
            startingLocation = startingLocation,
            destinationLocation = endLocation
        )

        return FormatterUtils.getCalculatedETA(distanceMeters = distance)
    }


    private fun updateTransactionETA(rescuer: UserItem) {
        val userLocation = state.value.userLocation ?: return

        val estimatedTimeArrival = rescuer.location?.let {
            getETABetweenTwoPoints(
                startingLocation = it,
                endLocation = userLocation
            )
        }

        val distance = rescuer.location?.let {
            mappingUseCase.getCalculatedDistanceUseCase(
                startingLocation = it,
                destinationLocation = userLocation)
        }
        _state.update {
            it.copy(
                rescueETA = estimatedTimeArrival ?: "",
                rescueDistance = distance ?: 0.0,
                rescuer = rescuer
            )
        }
    }

    private fun isLoading(loading: Boolean) {
        _state.update { it.copy(isLoading = loading) }
    }



    private suspend fun Throwable.handleDeclineRescueRequest() {
        when (this) {
            is MappingExceptions.NetworkException -> {
                _eventFlow.emit(value = MappingEvent.NoInternetConnection)
            }

            else -> {
                Timber.d("Failed to update user")
            }
        }
    }


    private suspend fun cancelUserHelpRequest() {
        mappingUseCase.cancelHelpRequestUseCase(id = getId())
    }


    private fun NearbyCyclist.filterUser() {

        runCatching {
            getId()
        }.onSuccess { id ->
            findUser(id = id)?.let { user ->
                val respondents = user.getUserRescueRespondents(this).distinctBy { it.id }
                _state.update {
                    it.copy(
                        newRescueRequest = NewRescueRequestsModel(request = respondents),
                        user = user)
                }
            }
        }.onFailure {
            Timber.e("Failed to get user: ${it.message}")
        }

    }


    private fun UserItem.getUserRescueRespondents(nearbyCyclist: NearbyCyclist): List<RescueRequestItemModel> {
        val rescueRespondentsSnapShot: MutableList<RescueRequestItemModel> = mutableListOf()

        rescueRequest?.respondents?.forEach { respondent ->
            val userRespondent = nearbyCyclist.findUser(id = respondent.clientId) ?: UserItem()
            val distance =
                calculateDistance(startLocation = location, endLocation = userRespondent.location)

            distance?.let {
                val formattedETA = FormatterUtils.getCalculatedETA(distanceMeters = it)
                rescueRespondentsSnapShot.add(
                    element = userRespondent.toRescueRequest(
                        distance = it.formatToDistanceKm(),
                        eta = formattedETA,
                        timestamp = respondent.timeStamp
                    )
                )
            }
        }

        return rescueRespondentsSnapShot.distinct()
    }

    private fun calculateDistance(
        startLocation: LocationModel?,
        endLocation: LocationModel?): Double? {
        val startLatitude = startLocation?.latitude ?: return null
        val startLongitude = startLocation.longitude ?: return null
        val endLatitude = endLocation?.latitude ?: return null
        val endLongitude = endLocation.longitude ?: return null

        return mappingUseCase.getCalculatedDistanceUseCase(
            startingLocation = LocationModel(latitude = startLatitude, longitude = startLongitude),
            destinationLocation = LocationModel(latitude = endLatitude, longitude = endLongitude)
        )
    }

    private fun NearbyCyclist.updateNearbyCyclists() {

        _state.update {
            it.copy(nearbyCyclist = NearbyCyclist())
        }
        _state.update {
            it.copy(nearbyCyclist = this)
        }
    }

    private suspend fun broadcastRescueTransactionToRespondent(location: LocationModel) {
        val rescueTransaction = state.value.rescueTransaction ?: return
        runCatching {

            val transaction = state.value.user.transaction
            val user = state.value.user

            if (transaction?.transactionId?.isEmpty() == true) {
                return@runCatching
            }

            mappingUseCase.transactionLocationUseCase(
                LiveLocationSocketModel(
                    latitude = location.latitude,
                    longitude = location.longitude,
                    room = rescueTransaction.id
                ),
                user = user,
                rescueTransactionItem = rescueTransaction
            )

        }.onSuccess {
            Timber.v("Broadcasting location to transaction success")
        }.onFailure {
            Timber.v("Broadcasting location to transaction failed: ${it.message}")
        }
    }




    private fun removeHazardousLaneListener() {
        mappingUseCase.removeHazardousListenerUseCase()
    }


    private fun subscribeToRescueTransactionUpdates() {

        viewModelScope.launch(context = SupervisorJob() + defaultDispatcher) {
            mappingUseCase.getRescueTransactionUpdatesUseCase().catch {
                Timber.e("ERROR GETTING RESCUE TRANSACTION: ${it.message}")

            }.onEach { rescueTransactions ->
                rescueTransactions.updateCurrentRescueTransaction()
                trackingHandler.filterRescueRequestAccepted(
                    rescueTransaction = rescueTransactions,
                    id = getId()
                )
                trackingHandler.updateClient()
            }.launchIn(this@launch).invokeOnCompletion {
                savedStateHandle[MAPPING_VM_STATE_KEY] = state.value
            }

        }
    }


    private fun RescueTransaction.updateCurrentRescueTransaction() {
        val rescueTransaction = trackingHandler.filterUserRescueTransaction(this)
        _state.update { it.copy(rescueTransaction = rescueTransaction) }
    }


    private fun subscribeToLocationUpdates() {
        if (locationUpdatesJob?.isActive == true) {
            return
        }
        locationUpdatesJob = viewModelScope.launch(context = SupervisorJob() + defaultDispatcher) {

            mappingUseCase.getUserLocationUseCase().catch {
                Timber.e("Error Location Updates: ${it.message}")
            }.onEach { location ->
                trackingHandler.updateLocation(location)
                broadcastRescueTransactionToRespondent(location)
                updateSpeedometer(location)
                if (state.value.nearbyCyclist == null) {
                    broadcastToNearbyCyclists()
                }

            }.launchIn(this@launch).invokeOnCompletion {
                savedStateHandle[MAPPING_VM_STATE_KEY] = state.value
            }
        }
    }

    private fun updateSpeedometer(location: LocationModel) {
        val isUserRescuer = state.value.user.isRescuer()
        if (!isUserRescuer) {
            return
        }

        val distance = SphericalUtil.computeLength(travelledPath)


        trackingHandler.setSpeed(location.speed)
        trackingHandler.getTopSpeed(location.speed)
        travelledPath.add(element = GoogleLatLng(location.latitude!!, location.longitude!!))

        trackingHandler.setTravelledDistance(distance)
    }


    private fun subscribeToNearbyUsersUpdates() {
        viewModelScope.launch(context = SupervisorJob() + defaultDispatcher) {
            mappingUseCase.nearbyCyclistsUseCase().catch {
                Timber.e("ERROR GETTING USERS: ${it.message}")
            }.onEach {
                it.filterUser()
                it.updateNearbyCyclists()
                Timber.v("Receiving from subscribeToNearbyUsersChanges")
                trackingHandler.updateClient()
            }.launchIn(this).invokeOnCompletion {
                savedStateHandle[MAPPING_VM_STATE_KEY] = state.value
            }
        }
    }

    private fun unSubscribeToLocationUpdates() {
        locationUpdatesJob?.cancel()
    }


    private suspend inline fun uploadUserProfile(crossinline onSuccess: () -> Unit) {
        coroutineScope {
            val userLocation = state.value.userLocation


            if (userLocation == null) {
                _eventFlow.emit(MappingEvent.LocationNotAvailable(reason = "Searching for GPS"))
                return@coroutineScope
            }

            if(userLocation.longitude == null && userLocation.latitude == null){
                _eventFlow.emit(MappingEvent.LocationNotAvailable(reason = "Searching for GPS"))
                return@coroutineScope
            }



            uploadProfile(location = userLocation, onSuccess = onSuccess)

        }
    }


    private suspend inline fun uploadProfile(
        location: LocationModel,
        crossinline onSuccess: () -> Unit
    ) {

        val isProfileUploaded = state.value.profileUploaded

        if (isProfileUploaded) {
            onSuccess()
            return
        }

        coroutineScope {

            runCatching {
                isLoading(true)

                val fullAddress = mappingUseCase.getFullAddressUseCase(
                    latitude = location.latitude,
                    longitude = location.longitude
                )

                fullAddress?.let { mappingUseCase.addressUseCase(it) }
                mappingUseCase.createUserUseCase(
                    user = UserItem(
                        id = getId(),
                        name = getName(),
                        address = fullAddress,
                        profilePictureUrl = getPhotoUrl(),
                        location = LocationModel(
                            latitude = location.latitude,
                            longitude = location.longitude
                        ),
                        rescueRequest = RescueRequest(),
                        userAssistance = UserAssistanceModel(),
                        rescuePending = RescuePending()
                    )
                )


            }.onSuccess {
                isLoading(false)
                broadcastToNearbyCyclists()
                onSuccess()
                _state.update { it.copy(profileUploaded = true) }

            }.onFailure { exception ->
                Timber.e("Error uploading profile: ${exception.message}")
                isLoading(false)
                exception.handleException()
            }
        }
    }


    private suspend fun Throwable.handleException() {
        trackingHandler.handleException(this)
        savedStateHandle[MAPPING_VM_STATE_KEY] = state.value
    }


    private suspend fun createMockUpUsers() {
        runCatching {
            mappingUseCase.createMockUsers()
        }.onSuccess {
            Timber.v("CREATED MOCK USERS!")
            broadcastToNearbyCyclists()
        }.onFailure {
            Timber.e("FAILED TO CREATE MOCK USERS: ${it.message}")
        }
    }

    private fun removeBottomSheet() {
        viewModelScope.launch(SupervisorJob()) {
            mappingUseCase.bottomSheetTypeUseCase(bottomSheet = "")
        }
    }


    override fun onCleared() {
        super.onCleared()
        removeBottomSheet()
        unSubscribeToLocationUpdates()
        removeHazardousLaneListener()


    }


    private fun getId(): String {
        return try {
            authUseCase.getIdUseCase()
        }catch (exception: Exception) {
           ""
        }
    }

    private suspend fun getName(): String = userProfileUseCase.getNameUseCase()

    private suspend fun getPhotoUrl() = userProfileUseCase.getPhotoUrlUseCase()

    private fun clearTravelledPath() {
        travelledPath = mutableListOf()
    }


}


