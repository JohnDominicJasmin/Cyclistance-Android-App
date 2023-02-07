package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.R
import com.example.cyclistance.core.utils.constants.MappingConstants.IMAGE_PLACEHOLDER_URL
import com.example.cyclistance.core.utils.constants.MappingConstants.MAPPING_VM_STATE_KEY
import com.example.cyclistance.core.utils.constants.MappingConstants.NEAREST_METERS
import com.example.cyclistance.core.utils.validation.FormatterUtils.findRescueTransaction
import com.example.cyclistance.core.utils.validation.FormatterUtils.findUser
import com.example.cyclistance.core.utils.validation.FormatterUtils.formatToDistanceKm
import com.example.cyclistance.core.utils.validation.FormatterUtils.getCalculatedDistance
import com.example.cyclistance.core.utils.validation.FormatterUtils.getCalculatedETA
import com.example.cyclistance.core.utils.validation.FormatterUtils.getETABetweenTwoPoints
import com.example.cyclistance.core.utils.validation.FormatterUtils.getUserRescueRespondents
import com.example.cyclistance.feature_alert_dialog.domain.model.AlertDialogModel
import com.example.cyclistance.feature_authentication.domain.use_case.AuthenticationUseCase
import com.example.cyclistance.feature_mapping.data.remote.dto.rescue_transaction.Route
import com.example.cyclistance.feature_mapping.data.remote.dto.rescue_transaction.Status
import com.example.cyclistance.feature_mapping.data.remote.dto.user_dto.*
import com.example.cyclistance.feature_mapping.domain.exceptions.MappingExceptions
import com.example.cyclistance.feature_mapping.domain.model.*
import com.example.cyclistance.feature_mapping.domain.use_case.MappingUseCase
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.utils.BottomSheetType
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.utils.createMockUsers
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.geometry.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MappingViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val authUseCase: AuthenticationUseCase,
    private val mappingUseCase: MappingUseCase) : ViewModel() {

    private var loadDataJob: Job? = null
    private var getUsersUpdatesJob: Job? = null
    private var locationUpdatesJob: Job? = null
    private var getRescueTransactionUpdatesJob: Job? = null
    private var getTransactionLocationUpdatesJob: Job? = null

    private val _state: MutableStateFlow<MappingState> = MutableStateFlow(
        savedStateHandle[MAPPING_VM_STATE_KEY] ?: MappingState())
    val state = _state.asStateFlow()

    private val _eventFlow: MutableSharedFlow<MappingUiEvent> = MutableSharedFlow()
    val eventFlow: SharedFlow<MappingUiEvent> = _eventFlow.asSharedFlow()


    init {
        observeDataChanges()
    }

    private fun observeDataChanges() {
        subscribeToLocationUpdates()
        subscribeToNearbyUsersChanges()
        subscribeToRescueTransactionUpdates()
        subscribeToTransactionLocationUpdates()
    }

    private fun loadData() {
        if (loadDataJob?.isActive == true) return
        loadDataJob = viewModelScope.launch(SupervisorJob()) {
            // TODO: Remove when the backend is ready
//            createMockUpUsers()
            getNearbyCyclist()
            loadRescueTransaction()
            loadClient()
        }

    }

    private suspend fun loadClient() {
        coroutineScope {
            val user = state.value.nearbyCyclists
            user?.updateClient()
        }
    }

    private fun NearbyCyclist.updateClient(rescueTransactionItem: RescueTransactionItem? = null) {

        val rescueTransaction = rescueTransactionItem ?: state.value.userRescueTransaction
        val role = state.value.user.transaction?.role

        if (role == Role.RESCUEE.name.lowercase()) {
            rescueTransaction?.rescuerId?.let { rescuerId ->
                _state.update { it.copy(rescuer = findUser(rescuerId), rescuee = null) }
            }
            return
        }

        rescueTransaction?.rescueeId?.let { rescueeId ->
            _state.update { it.copy(rescuee = findUser(rescueeId), rescuer = null) }
        }

    }

    private fun updateClient(rescueTransactionItem: RescueTransactionItem) {
        val nearbyCyclist = state.value.nearbyCyclists
        nearbyCyclist?.updateClient(rescueTransactionItem)
    }

    private suspend fun getNearbyCyclist() {
        if (state.value.nearbyCyclists != null) {
            return
        }

        coroutineScope {
            runCatching {
                mappingUseCase.getUsersUseCase().distinctUntilChanged().collect {
                    it.getUser()
                    it.getNearbyCyclist()
                    savedStateHandle[MAPPING_VM_STATE_KEY] = state.value
                }
            }.onFailure {
                it.handleException()
            }
        }

    }


    private suspend fun loadRescueTransaction() {
        coroutineScope {
            val transactionId = state.value.user.transaction?.transactionId
            if (transactionId.isNullOrEmpty()) {
                return@coroutineScope
            }
            runCatching {
                mappingUseCase.getRescueTransactionByIdUseCase(transactionId)
            }.onSuccess { rescueTransaction ->
                _state.update { it.copy(userRescueTransaction = rescueTransaction) }
                savedStateHandle[MAPPING_VM_STATE_KEY] = state.value
            }.onFailure {
                it.handleException()
            }
        }
    }


    private inline fun getUserRescueTransaction(
        rescueTransaction: RescueTransaction,
        onFoundRescueTransaction: (RescueTransactionItem?) -> Unit) {

        val transactionId = state.value.user.transaction?.transactionId
        val rescueTransactionResult =
            transactionId?.let { rescueTransaction.findRescueTransaction(it) }
        onFoundRescueTransaction(rescueTransactionResult)

    }

    private fun showRouteDirection(origin: Point, destination: Point) {
        viewModelScope.launch {
            runCatching {
                mappingUseCase.getRouteDirectionsUseCase(origin = origin, destination = destination)
            }.onSuccess { routeDirection ->
                _state.update { it.copy(routeDirection = routeDirection) }
            }.onFailure {
                Timber.v("Failure: ${it.message}")
            }
        }
    }

    private fun removeRouteDirections() {
        _state.update { it.copy(routeDirection = RouteDirection()) }
    }

    fun onEvent(event: MappingEvent) {
        when (event) {

            is MappingEvent.SubscribeToDataChanges -> {
                observeDataChanges()
            }

            is MappingEvent.RemoveRouteDirections -> {
                removeRouteDirections()
            }
            is MappingEvent.ShowRouteDirections -> {
                showRouteDirection(origin = event.origin, destination = event.destination)
            }

            is MappingEvent.RespondToHelp -> {
                respondToHelp()
            }

            is MappingEvent.DismissRescueeBanner -> {
                dismissRescueeBanner()
            }
            is MappingEvent.SelectRescueMapIcon -> {
                selectRescueeMapIcon(event.id)
            }
            is MappingEvent.DismissRequestAccepted -> {
                dismissRequestAccepted()
            }

            is MappingEvent.RequestHelp -> {
                requestHelp()
            }

            is MappingEvent.CancelRescueTransaction -> {
                removeAssignedTransaction()
            }

            is MappingEvent.DismissNoInternetDialog -> {
                dismissNoInternetDialog()
            }

            is MappingEvent.LoadData -> {
                loadData()
            }

            is MappingEvent.ChangeCameraState -> {
                updateCameraState(
                    cameraPosition = event.cameraPosition,
                    cameraZoomLevel = event.cameraZoomLevel)
            }

            is MappingEvent.StartNavigation -> {
                startNavigation()
            }

            is MappingEvent.StopNavigation -> {
                stopNavigation()
            }

            is MappingEvent.DeclineRescueRequest -> {
                declineRescueRequest(event.id)
            }

            is MappingEvent.AcceptRescueRequest -> {
                acceptRescueRequest(event.id)
            }

            is MappingEvent.CancelRequestHelp -> {
                cancelRequestHelp()
            }
            is MappingEvent.LoadUserProfile -> {
                loadUserProfile()
            }

            is MappingEvent.StartPinging -> {
                startPinging()
            }

            is MappingEvent.StopPinging -> {
                stopPinging()
            }

            is MappingEvent.DismissAlertDialog -> {
                dismissAlertDialog()
            }

            is MappingEvent.SignOut -> {
                signOutAccount()
            }


            is MappingEvent.ChangeBottomSheet -> {
                changeBottomSheetType(event.bottomSheetType)
            }
        }
        savedStateHandle[MAPPING_VM_STATE_KEY] = state.value
    }

    private fun dismissNoInternetDialog() {
        _state.update { it.copy(hasInternet = true) }
    }

    private fun updateCameraState(cameraPosition: LatLng, cameraZoomLevel: Double) {
        _state.update {
            it.copy(
                cameraState = CameraState(
                    cameraPosition = cameraPosition,
                    cameraZoom = cameraZoomLevel))
        }
    }

    private fun stopPinging() {
        _state.update { it.copy(searchingAssistance = false) }
    }

    private fun startPinging() {
        _state.update { it.copy(searchingAssistance = true) }
    }

    private fun startNavigation() {
        _state.update { it.copy(isNavigating = true) }
    }

    private fun stopNavigation() {
        _state.update { it.copy(isNavigating = false) }
    }

    private fun dismissAlertDialog() {
        _state.update { it.copy(alertDialogModel = AlertDialogModel()) }
    }

    private fun changeBottomSheetType(type: String) {
        _state.update { it.copy(bottomSheetType = type) }
    }

    private fun respondToHelp() {
        viewModelScope.launch {
            val selectedRescuee = state.value.selectedRescueeMapIcon
            uploadUserProfile {
                runCatching {
                    mappingUseCase.addRescueRespondentUseCase(
                        userId = selectedRescuee!!.userId,
                        respondentId = getId())
                }.onSuccess {
                    broadcastUser()
                    broadcastRescueTransaction()
                    _eventFlow.emit(value = MappingUiEvent.ShowToastMessage("Rescue request sent"))
                    _state.update { it.copy(respondedToHelp = true) }
                }.onFailure {
                    it.handleException()
                }
            }
        }
    }

    private fun selectRescueeMapIcon(id: String) {
        viewModelScope.launch {
            val selectedRescuee = state.value.nearbyCyclists?.findUser(id) ?: return@launch
            val selectedRescueeLocation = selectedRescuee.location
            val confirmationDetail = selectedRescuee.userAssistance?.confirmationDetail

            val userLocation = state.value.user.location ?: state.value.userLocation

            if (userLocation == null) {
                _eventFlow.emit(value = MappingUiEvent.ShowToastMessage("Tracking your location"))
                return@launch
            }

            val distance = getCalculatedDistance(
                startingLocation = Location(
                    latitude = userLocation.latitude,
                    longitude = userLocation.longitude
                ), endLocation = Location(
                    latitude = selectedRescueeLocation!!.latitude,
                    longitude = selectedRescueeLocation.longitude
                ))

            val timeRemaining = getCalculatedETA(distance)

            _state.update {
                it.copy(
                    selectedRescueeMapIcon = MappingBannerModel(
                        userId = selectedRescuee.id!!,
                        userProfileImage = selectedRescuee.profilePictureUrl
                                           ?: IMAGE_PLACEHOLDER_URL,
                        name = selectedRescuee.name ?: "name unavailable",
                        issue = confirmationDetail?.description ?: "",
                        bikeType = confirmationDetail?.bikeType ?: "",
                        address = selectedRescuee.address ?: "",
                        message = confirmationDetail?.message ?: "",
                        distanceRemaining = distance.formatToDistanceKm(),
                        timeRemaining = timeRemaining))
            }
            hideRequestHelpButton()
            showRespondToHelpButton()

        }
    }


    private fun showRespondToHelpButton() {
        _state.update { it.copy(respondToHelpButtonVisible = true) }
    }

    private fun hideRespondToHelpButton() {
        _state.update { it.copy(respondToHelpButtonVisible = false) }
    }


    private fun showRequestHelpButton() {
        _state.update { it.copy(requestHelpButtonVisible = true) }
    }

    private fun hideRequestHelpButton() {
        _state.update { it.copy(requestHelpButtonVisible = false) }
    }


    private fun dismissRescueeBanner() {
        if (state.value.selectedRescueeMapIcon != null) {
            _state.update { it.copy(selectedRescueeMapIcon = null) }
            hideRespondToHelpButton()
            showRequestHelpButton()
        }
    }

    private fun dismissRequestAccepted() {
        _state.update { it.copy(isRescueRequestAccepted = false) }
    }

    private fun showRequestAccepted() {
        _state.update { it.copy(isRescueRequestAccepted = true) }
    }

    private suspend fun String.removeAssignedTransaction() {
        mappingUseCase.createUserUseCase(
            user = UserItem(
                id = this,
                transaction = Transaction(),
                userAssistance = UserAssistance(needHelp = false),
                rescueRequest = RescueRequest()))
    }


    private fun removeAssignedTransaction() {
        viewModelScope.launch {
            runCatching {
                getId().removeAssignedTransaction()
            }.onSuccess {
                broadcastUser()
                broadcastRescueTransaction()
                finishLoading()
                dismissRequestAccepted()
                resetUiState()
            }.onFailure { exception ->
                finishLoading()
                exception.handleException()
            }
        }

    }

    private fun resetUiState() =
        _state.update {
            it.copy(
                respondedToHelp = true,
                searchingAssistance = false,
                bottomSheetType = "",
                requestHelpButtonVisible = true,
                isNavigating = false,
                isRescueRequestAccepted = false,
                userActiveRescueRequests = ActiveRescueRequests(),
                userRescueTransaction = RescueTransactionItem(),
                rescuee = null,
                rescuer = null

            )
        }

    private fun subscribeToTransactionLocationUpdates() {
        if (getRescueTransactionUpdatesJob?.isActive == true) {
            return
        }
        getTransactionLocationUpdatesJob = viewModelScope.launch(SupervisorJob()) {

            runCatching {
                mappingUseCase.getTransactionLocationUpdatesUseCase().distinctUntilChanged()
                    .collect { liveLocation ->
                        liveLocation.updateTransactionLocation()
                        liveLocation.updateTransactionETA()
                        liveLocation.updateTransactionDistance()
                    }
            }.onSuccess {
                savedStateHandle[MAPPING_VM_STATE_KEY] = state.value
            }.onFailure {
                Timber.e("ERROR GETTING TRANSACTION LOCATION: ${it.message}")
            }

        }
    }

    private suspend fun LiveLocationWSModel.updateTransactionDistance() {
        coroutineScope {
            val rescueTransaction = state.value.userLocation
            val role = state.value.user.transaction?.role
            latitude ?: return@coroutineScope
            longitude ?: return@coroutineScope

            rescueTransaction?.let { transaction ->

                val distance = getCalculatedDistance(
                    startingLocation = Location(latitude, longitude),
                    endLocation = Location(transaction.latitude, transaction.longitude)).toInt()

                if (distance <= NEAREST_METERS) {
                    updateRoleBottomSheet(role)
                    removeAssignedTransaction()
                }

            }
        }
    }

    private fun updateRoleBottomSheet(role: String?) {

        val bottomSheetType = if (role == Role.RESCUEE.name.lowercase()) {
            BottomSheetType.RescuerArrived.type
        } else {
            BottomSheetType.DestinationReached.type
        }

        _state.update { it.copy(bottomSheetType = bottomSheetType) }
    }

    private fun LiveLocationWSModel.updateTransactionLocation() {
        this.longitude ?: return
        this.latitude ?: return
        _state.update {
            it.copy(
                transactionLocation = Location(
                    latitude = this.latitude,
                    longitude = this.longitude))
        }
    }

    private fun LiveLocationWSModel.updateTransactionETA() {
        val userLocation = state.value.userLocation
        userLocation ?: return
        this.latitude ?: return
        this.longitude ?: return

        val eta = getETABetweenTwoPoints(
            startingLocation = Location(
                latitude = this.latitude,
                longitude = this.longitude), endLocation = userLocation)
        _state.update { it.copy(rescuerETA = eta) }
    }


    private suspend inline fun checkCurrentTransactions(
        user: UserItem,
        rescuer: UserItem,
        crossinline noCurrentTransaction: suspend () -> Unit) {

        val userHasCurrentTransaction =
            (user.transaction ?: Transaction()).transactionId.isNotEmpty()

        val rescuerHasCurrentTransaction =
            (rescuer.transaction ?: Transaction()).transactionId.isNotEmpty()


        if (rescuer.location == null) {
            _eventFlow.emit(value = MappingUiEvent.ShowToastMessage("Can't reach Rescuer"))
            return
        }

        if (user.location == null) {
            _eventFlow.emit(value = MappingUiEvent.ShowToastMessage("Location not found"))
            return
        }

        if (userHasCurrentTransaction) {
            rescueeCannotRequest()
            return
        }

        if (rescuerHasCurrentTransaction) {
            rescuerCannotRequest()
            return
        }
        noCurrentTransaction()
    }

    private fun acceptRescueRequest(id: String) {
        viewModelScope.launch(SupervisorJob()) {

            val rescuer = state.value.nearbyCyclists?.findUser(id) ?: return@launch
            val transactionId = getTransactionId(rescuer)
            val user = state.value.user

            checkCurrentTransactions(user = user, rescuer = rescuer) {

                runCatching {
                    startLoading()
                    RescueTransactionItem(
                        id = transactionId,
                        rescuerId = rescuer.id,
                        rescueeId = user.id,
                        status = Status(started = true, ongoing = true),
                        route = Route(
                            startingLocation = Location(
                                latitude = rescuer.location!!.latitude,
                                longitude = rescuer.location.longitude
                            ),
                            destinationLocation = Location(
                                latitude = user.location!!.latitude,
                                longitude = user.location.longitude)
                        )).apply {
                        mappingUseCase.createRescueTransactionUseCase(rescueTransaction = this)
                    }

                }.onSuccess { rescueTransaction ->
                    broadcastRescueTransaction()
                    assignTransaction(
                        rescueTransaction = rescueTransaction,
                        user = user,
                        rescuer = rescuer,
                        transactionId = transactionId)

                }.onFailure { exception ->
                    finishLoading()
                    exception.handleException()
                }

                savedStateHandle[MAPPING_VM_STATE_KEY] = state.value

            }
        }
    }

    private fun getTransactionId(rescuer: UserItem): String {
        val user = state.value.user
        return user.id?.take(3) + rescuer.id?.take(3) + System.currentTimeMillis().toString()
            .takeLast(6)
    }

    private suspend fun broadcastRescueTransaction() {
        runCatching {
            mappingUseCase.broadcastRescueTransactionUseCase()
        }.onFailure {
            it.handleException()
        }
    }

    private suspend fun broadcastUser() {
        val location = state.value.userLocation ?: return
        runCatching {
            mappingUseCase.broadcastUserUseCase(
                locationModel = LiveLocationWSModel(
                    latitude = location.latitude,
                    longitude = location.longitude
                ))
        }.onFailure {
            it.handleException()
        }
    }

    private suspend fun assignTransaction(
        rescueTransaction: RescueTransactionItem,
        user: UserItem,
        rescuer: UserItem,
        transactionId: String) {

        runCatching {

            transactionId.assignTransaction(role = Role.RESCUEE.name.lowercase(), id = user.id)
            transactionId.assignTransaction(role = Role.RESCUER.name.lowercase(), id = rescuer.id)

        }.onSuccess {
            broadcastUser()
            _eventFlow.emit(value = MappingUiEvent.ShowMappingScreen)
            delay(500)
            updateTransactionETA(rescuer, rescueTransaction)
            finishLoading()
            broadcastRescueTransaction()
        }.onFailure { exception ->
            finishLoading()
            exception.handleException()
        }

    }

    private fun updateTransactionETA(rescuer: UserItem, rescueTransaction: RescueTransactionItem) {
        val userLocation = state.value.userLocation
        userLocation ?: return

        val estimatedTimeArrival = rescuer.location?.let {
            getETABetweenTwoPoints(
                startingLocation = it,
                endLocation = userLocation)
        }
        _state.update {
            it.copy(
                userRescueTransaction = rescueTransaction,
                rescuerETA = estimatedTimeArrival ?: "",
                rescuer = rescuer)
        }
        hideRequestHelpButton()
    }

    private fun startLoading() {
        _state.update { it.copy(isLoading = true) }
    }

    private fun finishLoading() {
        _state.update { it.copy(isLoading = false) }
    }


    private fun rescueeCannotRequest() {
        _state.update {
            it.copy(
                alertDialogModel = AlertDialogModel(
                    title = "Cannot Request",
                    description = "You can only have one transaction at a time",
                    icon = R.raw.error
                ))
        }
    }

    private fun rescuerCannotRequest() {
        _state.update {
            it.copy(
                alertDialogModel = AlertDialogModel(
                    title = "Cannot Request",
                    description = "Unfortunately the Rescuer is currently in a transaction.",
                    icon = R.raw.error
                ))
        }
    }

    private suspend fun String.assignTransaction(role: String, id: String?) {
        mappingUseCase.createUserUseCase(
            user = UserItem.empty(id = id, transactionId = this, role = role)
        )
    }


    private suspend fun Throwable.handleDeclineRescueRequest() {
        when (this) {
            is MappingExceptions.NetworkException -> {
                _eventFlow.emit(MappingUiEvent.ShowToastMessage("No Internet Connection"))
            }
            else -> {
                Timber.d("Failed to update user")
            }
        }
    }


    private fun loadUserProfile() {
        viewModelScope.launch(SupervisorJob()) {
            loadName()
            loadPhoto()
        }.invokeOnCompletion {
            savedStateHandle[MAPPING_VM_STATE_KEY] = state.value
        }
    }

    private suspend fun loadName() {
        coroutineScope {
            runCatching {
                getName()
            }.onSuccess { name ->
                _state.update {
                    it.copy(name = name)
                }
            }.onFailure {
                Timber.e("Load Name ${it.message}")
            }
        }

    }

    private suspend fun loadPhoto() {
        coroutineScope {
            runCatching {
                getPhotoUrl()
            }.onSuccess { photoUrl ->
                _state.update {
                    it.copy(photoUrl = photoUrl)
                }
            }.onFailure {
                Timber.e("Load Photo ${it.message}")
            }
        }
    }

    private fun cancelRequestHelp() {
        viewModelScope.launch {
            runCatching {
                startLoading()
                createUser()
            }.onSuccess {
                showRequestHelpButton()
                broadcastUser()
            }.onFailure { exception ->
                Timber.e("Failed to cancel search assistance: ${exception.message}")
                exception.handleException()
            }
            finishLoading()
            savedStateHandle[MAPPING_VM_STATE_KEY] = state.value
        }
    }

    private suspend fun createUser() {
        mappingUseCase.createUserUseCase(
            user = UserItem(
                id = getId(),
                userAssistance = UserAssistance(
                    needHelp = false,
                    confirmationDetail = ConfirmationDetail()))
        )
    }

    private fun updateLocation(location: Location) {
        val latitude = location.latitude.takeIf { it != 0.0 } ?: return
        val longitude = location.longitude.takeIf { it != 0.0 } ?: return
        _state.update { state ->
            state.copy(
                userLocation = Location(
                    latitude = latitude,
                    longitude = longitude))
        }

    }


    private fun NearbyCyclist.getUser() {
        runCatching {
            getId()
        }.onSuccess { id ->
            val user = findUser(id = id)
            _state.update { it.copy(user = user) }
            val respondents = user.getUserRescueRespondents(this)
            _state.update {
                it.copy(
                    userActiveRescueRequests = ActiveRescueRequests(
                        respondents = respondents))
            }
        }.onFailure {
            Timber.e("Failed to get User ID")
        }

    }


    private fun NearbyCyclist.getNearbyCyclist() {
        _state.update { it.copy(nearbyCyclists = this) }
    }

    private suspend fun broadCastLocationToTransaction(location: Location) {
        val rescueTransaction = state.value.userRescueTransaction ?: return
        runCatching {

            val user = state.value.user
            mappingUseCase.broadcastTransactionLocationUseCase(
                LiveLocationWSModel(
                    latitude = location.latitude,
                    longitude = location.longitude,
                    room = rescueTransaction.id),
                user = user,
                rescueTransactionItem = rescueTransaction)

        }.onFailure {
            Timber.v("Broadcasting location to transaction failed: ${it.message}")
        }
    }


    private fun unSubscribeToTransactionLocationUpdates() {
        getTransactionLocationUpdatesJob?.cancel()
    }

    private fun subscribeToRescueTransactionUpdates() {
        if (getRescueTransactionUpdatesJob?.isActive == true) {
            return
        }
        getRescueTransactionUpdatesJob = viewModelScope.launch(SupervisorJob()) {

            runCatching {
                mappingUseCase.getRescueTransactionUpdatesUseCase().collect {
                    it.updateRescueTransaction()
                    it.updateRescueClient()
                    it.checkRescueRequestAccepted()
                }
            }.onSuccess {
                savedStateHandle[MAPPING_VM_STATE_KEY] = state.value
            }.onFailure {
                Timber.e("ERROR GETTING RESCUE TRANSACTION: ${it.message}")
            }
        }
    }


    private fun RescueTransaction.checkRescueRequestAccepted() {
        val respondedToHelp = state.value.respondedToHelp
        val user = state.value.user
        val role = user.transaction?.role
        val userId = state.value.user.id ?: getId()

        if (respondedToHelp.not()) {
            return
        }

        getUserRescueTransaction(this) { rescueTransaction ->
            rescueTransaction?.let { transaction ->

                if (transaction.cancellation?.rescueCancelled == true) {
                    return@let
                }

                if (transaction.rescuerId != userId) {
                    return@let
                }

                if (role == Role.RESCUEE.name.lowercase()) {
                    return@let
                }

                if (transaction.rescueeId.isNullOrEmpty()) {
                    return@let
                }


                showRequestAccepted()
            }
        }

    }

    private fun RescueTransaction.updateRescueTransaction() {
        getUserRescueTransaction(this) { rescueTransaction ->
            _state.update { it.copy(userRescueTransaction = rescueTransaction) }
        }
    }

    private fun RescueTransaction.updateRescueClient() {
        getUserRescueTransaction(this) { rescueTransaction ->
            rescueTransaction?.let { transaction ->
                updateClient(transaction)
            }
        }
    }

    private fun unSubscribeToRescueTransactionUpdates() {
        getRescueTransactionUpdatesJob?.cancel()
    }

    private fun unSubscribeToNearbyUsersChanges() {
        getUsersUpdatesJob?.cancel()
    }

    private fun subscribeToLocationUpdates() {
        if (locationUpdatesJob?.isActive == true) {
            return
        }
        locationUpdatesJob = viewModelScope.launch {

            runCatching {
                mappingUseCase.getUserLocationUseCase().collect { location ->
                    broadCastLocationToTransaction(location)
                    updateLocation(location)
                    getNearbyCyclist()
                }

            }.onSuccess {
                savedStateHandle[MAPPING_VM_STATE_KEY] = state.value

            }.onFailure {
                Timber.e("Error Location Updates: ${it.message}")
            }
        }
    }


    private fun subscribeToNearbyUsersChanges() {
        if (getUsersUpdatesJob?.isActive == true) {
            return
        }

        getUsersUpdatesJob = viewModelScope.launch(SupervisorJob()) {
            runCatching {
                mappingUseCase.getUserUpdatesUseCase().collect {
                    it.getUser()
                    it.getNearbyCyclist()
                    it.updateClient()
                }
            }.onSuccess {
                savedStateHandle[MAPPING_VM_STATE_KEY] = state.value
            }.onFailure {
                Timber.e("ERROR GETTING USERS: ${it.message}")
            }
        }
    }

    private fun unSubscribeToLocationUpdates() {
        locationUpdatesJob?.cancel()
    }


    private fun declineRescueRequest(id: String) {
        viewModelScope.launch {
            runCatching {
                startLoading()
                removeRescueRespondent(id)
                mappingUseCase.deleteRescueRespondentUseCase(userId = getId(), respondentId = id)
            }.onSuccess {
                broadcastUser()
            }.onFailure {
                it.handleDeclineRescueRequest()
            }
            finishLoading()
            savedStateHandle[MAPPING_VM_STATE_KEY] = state.value

        }
    }

    private fun removeRescueRespondent(id: String) {
        val rescueRespondents = state.value.userActiveRescueRequests.respondents.apply {
            toMutableList().apply {
                removeAll { it.id == id }
            }
        }
        _state.update {
            it.copy(userActiveRescueRequests = ActiveRescueRequests(rescueRespondents))
        }
    }

    private fun signOutAccount() {
        viewModelScope.launch {
            runCatching {
                startLoading()
                authUseCase.signOutUseCase()
            }.onSuccess {
                _eventFlow.emit(value = MappingUiEvent.ShowSignInScreen)
            }.onFailure {
                Timber.e("Error Sign out account: ${it.message}")
            }
            finishLoading()
        }.invokeOnCompletion {
            savedStateHandle[MAPPING_VM_STATE_KEY] = state.value
        }
    }


    private fun requestHelp() {
        viewModelScope.launch {
            uploadUserProfile(onSuccess = {
                _eventFlow.emit(MappingUiEvent.ShowConfirmDetailsScreen)
            })

        }.invokeOnCompletion {
            savedStateHandle[MAPPING_VM_STATE_KEY] = state.value
        }
    }


    private suspend inline fun uploadUserProfile(crossinline onSuccess: suspend () -> Unit) {
        coroutineScope {
            val userLocation = state.value.userLocation

            if (userLocation == null) {
                _eventFlow.emit(MappingUiEvent.ShowToastMessage(message = "Searching for GPS"))
                return@coroutineScope
            }

            getFullAddress(location = userLocation, onSuccess = onSuccess)

        }
    }


    private suspend inline fun getFullAddress(
        location: Location,
        crossinline onSuccess: suspend () -> Unit) {

        runCatching {
            mappingUseCase.getFullAddressUseCase(
                latitude = location.latitude,
                longitude = location.longitude)
        }.onSuccess { fullAddress ->
            uploadProfile(location = location, fullAddress = fullAddress, onSuccess = onSuccess)
        }.onFailure { exception ->
            if (exception is MappingExceptions.NoAddressFound) {
                _eventFlow.emit(MappingUiEvent.ShowToastMessage(message = exception.message!!))
            }
        }
    }

    private suspend inline fun uploadProfile(
        location: Location,
        fullAddress: String,
        crossinline onSuccess: suspend () -> Unit
    ) {

        val isProfileUploaded = state.value.profileUploaded

        if (isProfileUploaded) {
            onSuccess()
            return
        }

        coroutineScope {
            runCatching {
                startLoading()
                mappingUseCase.createUserUseCase(
                    user = UserItem(
                        id = getId(),
                        name = getName(),
                        address = fullAddress,
                        profilePictureUrl = getPhotoUrl(),
                        contactNumber = getPhoneNumber(),
                        location = Location(
                            latitude = location.latitude,
                            longitude = location.longitude),
                        rescueRequest = RescueRequest(), userAssistance = UserAssistance()))
                mappingUseCase.setAddressUseCase(fullAddress)

            }.onSuccess {
                finishLoading()
                broadcastUser()
                onSuccess()
                _state.update { it.copy(profileUploaded = true) }

            }.onFailure { exception ->
                Timber.e("Error uploading profile: ${exception.message}")
                finishLoading()
                exception.handleException()
            }
        }
    }


    private suspend fun Throwable.handleException() {
        when (this) {

            is MappingExceptions.NetworkException -> {
                _state.update { it.copy(hasInternet = false) }
            }

            is MappingExceptions.UnexpectedErrorException, is MappingExceptions.UserException -> {
                _eventFlow.emit(
                    MappingUiEvent.ShowToastMessage(
                        message = this.message ?: "Unexpected error occurred."
                    ))
            }
            is MappingExceptions.PhoneNumberException, is MappingExceptions.NameException -> {
                _eventFlow.emit(MappingUiEvent.ShowEditProfileScreen)
            }

            else -> {
                Timber.e("Error HandleException: ${this.message}")
            }

        }
        savedStateHandle[MAPPING_VM_STATE_KEY] = state.value
    }


    private suspend fun createMockUpUsers() {
        runCatching {
            mappingUseCase.createMockUsers()
        }.onSuccess {
            Timber.v("CREATED MOCK USERS!")
            broadcastUser()
        }.onFailure {
            Timber.e("FAILED TO CREATE MOCK USERS: ${it.message}")
        }
    }


    override fun onCleared() {
        super.onCleared()
        unSubscribeToLocationUpdates()
        unSubscribeToNearbyUsersChanges()
        unSubscribeToRescueTransactionUpdates()
        unSubscribeToTransactionLocationUpdates()
        onEvent(event = MappingEvent.StopPinging)
    }


    private fun getId(): String = authUseCase.getIdUseCase()

    private fun getName(): String = authUseCase.getNameUseCase()

    private suspend fun getPhoneNumber(): String =
        authUseCase.getPhoneNumberUseCase()

    private fun getPhotoUrl() = authUseCase.getPhotoUrlUseCase()


}


