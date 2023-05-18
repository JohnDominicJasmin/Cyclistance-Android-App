package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.core.utils.constants.MappingConstants.IMAGE_PLACEHOLDER_URL
import com.example.cyclistance.core.utils.constants.MappingConstants.MAPPING_VM_STATE_KEY
import com.example.cyclistance.core.utils.constants.MappingConstants.NEAREST_METERS
import com.example.cyclistance.core.utils.validation.FormatterUtils.findRescueTransaction
import com.example.cyclistance.core.utils.validation.FormatterUtils.findUser
import com.example.cyclistance.core.utils.validation.FormatterUtils.formatToDistanceKm
import com.example.cyclistance.core.utils.validation.FormatterUtils.getCalculatedETA
import com.example.cyclistance.core.utils.validation.FormatterUtils.isLocationAvailable
import com.example.cyclistance.feature_authentication.domain.use_case.AuthenticationUseCase
import com.example.cyclistance.feature_mapping.data.mapper.UserMapper.toCardModel
import com.example.cyclistance.feature_mapping.data.remote.dto.rescue_transaction.Route
import com.example.cyclistance.feature_mapping.data.remote.dto.rescue_transaction.Status
import com.example.cyclistance.feature_mapping.data.remote.dto.user_dto.ConfirmationDetail
import com.example.cyclistance.feature_mapping.data.remote.dto.user_dto.Location
import com.example.cyclistance.feature_mapping.data.remote.dto.user_dto.RescueRequest
import com.example.cyclistance.feature_mapping.data.remote.dto.user_dto.Transaction
import com.example.cyclistance.feature_mapping.data.remote.dto.user_dto.UserAssistance
import com.example.cyclistance.feature_mapping.domain.exceptions.MappingExceptions
import com.example.cyclistance.feature_mapping.domain.model.LiveLocationWSModel
import com.example.cyclistance.feature_mapping.domain.model.MapSelectedRescuee
import com.example.cyclistance.feature_mapping.domain.model.NearbyCyclist
import com.example.cyclistance.feature_mapping.domain.model.NewRescueRequestsModel
import com.example.cyclistance.feature_mapping.domain.model.RescueRequestModel
import com.example.cyclistance.feature_mapping.domain.model.RescueTransaction
import com.example.cyclistance.feature_mapping.domain.model.RescueTransactionItem
import com.example.cyclistance.feature_mapping.domain.model.Role
import com.example.cyclistance.feature_mapping.domain.model.UserItem
import com.example.cyclistance.feature_mapping.domain.use_case.MappingUseCase
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.event.MappingEvent
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.event.MappingVmEvent
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.state.MappingState
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.utils.createMockUsers
import com.mapbox.geojson.Point
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.internal.toImmutableList
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MappingViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val authUseCase: AuthenticationUseCase,
    private val mappingUseCase: MappingUseCase,
    private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {


    private var loadDataJob: Job? = null
    private var getUsersUpdatesJob: Job? = null
    private var locationUpdatesJob: Job? = null
    private var getRescueTransactionUpdatesJob: Job? = null
    private var getTransactionLocationUpdatesJob: Job? = null

    private val _state: MutableStateFlow<MappingState> = MutableStateFlow(
        savedStateHandle[MAPPING_VM_STATE_KEY] ?: MappingState())
    val state = _state.asStateFlow()

    private val _eventFlow: MutableSharedFlow<MappingEvent> = MutableSharedFlow()
    val eventFlow: SharedFlow<MappingEvent> = _eventFlow.asSharedFlow()


    init {
        loadData()
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
        loadDataJob = viewModelScope.launch(SupervisorJob() + defaultDispatcher) {
            // TODO: Remove when the backend is ready
            createMockUpUsers()
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

        val rescueTransaction = rescueTransactionItem ?: state.value.rescueTransaction
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
                _state.update { it.copy(rescueTransaction = rescueTransaction) }
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
        viewModelScope.launch(context = defaultDispatcher) {
            runCatching {
                mappingUseCase.getRouteDirectionsUseCase(origin = origin, destination = destination)
            }.onSuccess { routeDirection ->
                _eventFlow.emit(value = MappingEvent.NewRouteDirection(routeDirection))
            }.onFailure {
                Timber.v("Failure: ${it.message}")
                it.handleException()
            }
        }
    }


    fun onEvent(event: MappingVmEvent) {
        when (event) {

            is MappingVmEvent.SubscribeToDataChanges -> {
                observeDataChanges()
            }


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

            is MappingVmEvent.CancelRescueTransaction -> {
                removeAssignedTransaction()
            }


            is MappingVmEvent.LoadData -> {
                loadData()
            }


            is MappingVmEvent.DeclineRescueRequest -> {
                declineRescueRequest(event.id)
            }

            is MappingVmEvent.AcceptRescueRequest -> {
                acceptRescueRequest(event.id)
            }

            is MappingVmEvent.CancelRequestHelp -> {
                cancelHelpRequest()
            }

        }
        savedStateHandle[MAPPING_VM_STATE_KEY] = state.value
    }


    private fun clearTransactionRoles() =
        _state.update {
            it.copy(
                respondedToHelp = true,
                rescueTransaction = RescueTransactionItem(),
                rescuee = null,
                rescuer = null,
                newRescueRequest = NewRescueRequestsModel()
            )
        }


    private fun respondToHelp(selectedRescuee: MapSelectedRescuee) {
        viewModelScope.launch(context = defaultDispatcher) {
            runCatching {

                uploadUserProfile(onSuccess = {
                    mappingUseCase.addRescueRespondentUseCase(
                        userId = selectedRescuee.userId,
                        respondentId = getId())
                })
            }.onSuccess {
                _eventFlow.emit(value = MappingEvent.RespondToHelpSuccess())
                broadcastToNearbyCyclists()
                broadcastRescueTransaction()
                _state.update { it.copy(respondedToHelp = true) }
            }.onFailure {
                it.handleException()
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


    private suspend fun calculateSelectedRescueeDistance(userLocation: Location?, id: String) {
        val selectedRescuee = state.value.nearbyCyclists?.findUser(id) ?: return
        val selectedRescueeLocation = selectedRescuee.location
        val confirmationDetail = selectedRescuee.userAssistance?.confirmationDetail

        runCatching {
            startLoading()
            mappingUseCase.getCalculatedDistanceUseCase(
                startingLocation = Location(
                    latitude = userLocation?.latitude,
                    longitude = userLocation?.longitude
                ), destinationLocation = Location(
                    latitude = selectedRescueeLocation!!.latitude,
                    longitude = selectedRescueeLocation.longitude
                )
            )
        }.onSuccess { distance ->
            val timeRemaining = getCalculatedETA(distance)
            _eventFlow.emit(
                value = MappingEvent.NewSelectedRescuee(
                    selectedRescuee = MapSelectedRescuee(
                        userId = selectedRescuee.id!!,
                        userProfileImage = selectedRescuee.profilePictureUrl
                                           ?: IMAGE_PLACEHOLDER_URL,
                        name = selectedRescuee.name ?: "name unavailable",
                        issue = confirmationDetail?.description ?: "",
                        bikeType = confirmationDetail?.bikeType ?: "",
                        address = selectedRescuee.address ?: "",
                        message = confirmationDetail?.message ?: "",
                        distanceRemaining = distance.formatToDistanceKm(),
                        timeRemaining = timeRemaining)
                ))
        }.onFailure {
            _eventFlow.emit(value = MappingEvent.FailedToCalculateDistance)
        }.also {
            finishLoading()
        }
    }


    private suspend fun String.removeAssignedTransaction() {
        //todo: create api breakpoint
        mappingUseCase.createUserUseCase(
            user = UserItem(
                id = this,
                transaction = Transaction(),
                userAssistance = UserAssistance(needHelp = false),
                rescueRequest = RescueRequest()))
    }


    private fun removeAssignedTransaction() {
        viewModelScope.launch(context = defaultDispatcher) {
            runCatching {
                getId().removeAssignedTransaction()
            }.onSuccess {
                broadcastToNearbyCyclists()
                broadcastRescueTransaction()
                finishLoading()
                _eventFlow.emit(value = MappingEvent.RemoveAssignedTransactionSuccess)
                clearTransactionRoles()
            }.onFailure { exception ->
                finishLoading()
                exception.handleException()
            }
        }

    }


    private fun subscribeToTransactionLocationUpdates() {
        if (getRescueTransactionUpdatesJob?.isActive == true) {
            return
        }
        getTransactionLocationUpdatesJob = viewModelScope.launch(context = SupervisorJob() + defaultDispatcher) {

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
            latitude ?: return@coroutineScope
            longitude ?: return@coroutineScope

            rescueTransaction?.let { transaction ->

                val distance = mappingUseCase.getCalculatedDistanceUseCase(
                    startingLocation = Location(latitude, longitude),
                    destinationLocation = Location(transaction.latitude, transaction.longitude)
                ).toInt()


                if (distance <= NEAREST_METERS) {
                    _eventFlow.emit(value = MappingEvent.DestinationReached)
                    removeAssignedTransaction()
                }

            }
        }
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

    private fun getETABetweenTwoPoints(startingLocation: Location, endLocation: Location): String {
        val distance = mappingUseCase.getCalculatedDistanceUseCase(
            startingLocation = startingLocation,
            destinationLocation = endLocation
        )

        return getCalculatedETA(distanceMeters = distance)
    }


    private suspend inline fun checkCurrentTransactions(
        user: UserItem,
        rescuer: UserItem,
        crossinline noCurrentTransaction: suspend () -> Unit) {

        val userHasCurrentTransaction =
            (user.transaction ?: Transaction()).transactionId.isNotEmpty()

        val rescuerHasCurrentTransaction =
            (rescuer.transaction ?: Transaction()).transactionId.isNotEmpty()

        val rescuerLocationAvailable = rescuer.location.isLocationAvailable()
        val userLocationAvailable = user.location.isLocationAvailable()

        if (!rescuerLocationAvailable) {
            _eventFlow.emit(value = MappingEvent.RescuerLocationNotAvailable())
            return
        }

        if (!userLocationAvailable) {
            _eventFlow.emit(value = MappingEvent.LocationNotAvailable("Location not found"))
            return
        }

        if (rescuerHasCurrentTransaction) {
            _eventFlow.emit(value = MappingEvent.RescueHasTransaction)
            return
        }

        if (userHasCurrentTransaction) {
            _eventFlow.emit(value = MappingEvent.UserHasCurrentTransaction)
            return
        }


        noCurrentTransaction()
    }

    private fun acceptRescueRequest(id: String) {
        viewModelScope.launch(context = SupervisorJob() + defaultDispatcher) {

            val rescuer = state.value.nearbyCyclists?.findUser(id) ?: return@launch
            _state.update { it.copy(rescueRequestAcceptedUser = rescuer) }
            val transactionId = getTransactionId(rescuer)
            val user = state.value.user

            checkCurrentTransactions(user = user, rescuer = rescuer) {

                coroutineScope {
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
                                    longitude = rescuer.location.longitude),
                                destinationLocation = Location(
                                    latitude = user.location!!.latitude,
                                    longitude = user.location.longitude)
                            )).apply {
                            mappingUseCase.createRescueTransactionUseCase(rescueTransaction = this)
                        }

                    }.onSuccess { rescueTransaction ->
                        broadcastRescueTransaction()
                        assignRequestTransaction(
                            rescueTransaction = rescueTransaction,
                            user = user,
                            rescuer = rescuer,
                            transactionId = transactionId)

                    }.onFailure { exception ->
                        finishLoading()
                        exception.handleException()
                    }
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

    private suspend fun broadcastToNearbyCyclists() {
        val location = state.value.userLocation ?: return
        runCatching {
            mappingUseCase.broadcastToNearbyCyclists(
                locationModel = LiveLocationWSModel(
                    latitude = location.latitude,
                    longitude = location.longitude
                ))
        }.onFailure {
            it.handleException()
        }
    }

    private suspend fun assignRequestTransaction(
        rescueTransaction: RescueTransactionItem,
        user: UserItem,
        rescuer: UserItem,
        transactionId: String) {

        runCatching {

            transactionId.assignRequestTransaction(
                role = Role.RESCUEE.name.lowercase(),
                id = user.id)

            transactionId.assignRequestTransaction(
                role = Role.RESCUER.name.lowercase(),
                id = rescuer.id)

        }.onSuccess {
            broadcastToNearbyCyclists()
            _eventFlow.emit(value = MappingEvent.AcceptRescueRequestSuccess)
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
        val userLocation = state.value.userLocation ?: return

        val estimatedTimeArrival = rescuer.location?.let {
            getETABetweenTwoPoints(
                startingLocation = it,
                endLocation = userLocation)
        }
        _state.update {
            it.copy(
                rescueTransaction = rescueTransaction,
                rescuerETA = estimatedTimeArrival ?: "",
                rescuer = rescuer)
        }
    }

    private fun startLoading() {
        _state.update { it.copy(isLoading = true) }
    }

    private fun finishLoading() {
        _state.update { it.copy(isLoading = false) }
    }


    private suspend fun String.assignRequestTransaction(role: String, id: String?) {
        mappingUseCase.createUserUseCase(
            user = UserItem.empty(id = id, transactionId = this, role = role)
        )
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


    private fun cancelHelpRequest() {
        viewModelScope.launch(context = defaultDispatcher) {
            runCatching {
                startLoading()
                cancelUserHelpRequest()
            }.onSuccess {
                _eventFlow.emit(value = MappingEvent.CancelHelpRequestSuccess)
                broadcastToNearbyCyclists()
                _state.update { it.copy(rescueRequestAcceptedUser = null) }
            }.onFailure { exception ->
                Timber.e("Failed to cancel search assistance: ${exception.message}")
                exception.handleException()
            }
            finishLoading()
            savedStateHandle[MAPPING_VM_STATE_KEY] = state.value
        }
    }

    private suspend fun cancelUserHelpRequest() {
        // TODO: Create api breakpoint
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


    private suspend fun NearbyCyclist.getUser() {
        runCatching {
            getId()
        }.onSuccess { id ->
            val user = findUser(id = id)
            _state.update { it.copy(user = user) }
            val respondents = user.getUserRescueRespondents(this)
            _state.update {
                it.copy(
                    newRescueRequest = NewRescueRequestsModel(
                        request = respondents))
            }
        }.onFailure {
            _eventFlow.emit(value = MappingEvent.GetUserIdFailed)
        }

    }


    private fun UserItem.getUserRescueRespondents(nearbyCyclist: NearbyCyclist): List<RescueRequestModel> {
        val rescueRespondentsSnapShot: MutableList<RescueRequestModel> = mutableListOf()

        rescueRequest?.respondents?.forEach { respondent ->
            val userRespondent = nearbyCyclist.findUser(id = respondent.clientId)
            val distance = location?.let { start ->
                start.latitude ?: return@forEach
                start.longitude ?: return@forEach
                userRespondent.location?.let { end ->
                    end.latitude ?: return@forEach
                    end.longitude ?: return@forEach
                    mappingUseCase.getCalculatedDistanceUseCase(
                        startingLocation = Location(
                            latitude = start.latitude,
                            longitude = start.longitude
                        ),
                        destinationLocation = Location(
                            latitude = end.latitude,
                            longitude = end.longitude
                        )
                    )
                }
            }

            distance?.let {
                val formattedETA = getCalculatedETA(distanceMeters = distance)
                rescueRespondentsSnapShot.add(
                    element = userRespondent.toCardModel(
                        distance = distance.formatToDistanceKm(),
                        eta = formattedETA))
            }
        }

        return rescueRespondentsSnapShot.distinct().toImmutableList().also {
            rescueRespondentsSnapShot.clear()
        }
    }

    private fun NearbyCyclist.getNearbyCyclist() {
        _state.update { it.copy(nearbyCyclists = this.apply { users.distinct() }) }
    }

    private suspend fun broadcastRescueTransactionToRespondent(location: Location) {
        val rescueTransaction = state.value.rescueTransaction ?: return
        runCatching {

            val user = state.value.user
            mappingUseCase.broadcastRescueTransactionToRespondent(
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
        getRescueTransactionUpdatesJob =
            viewModelScope.launch(context = SupervisorJob() + defaultDispatcher) {

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


    private suspend fun RescueTransaction.checkRescueRequestAccepted() {
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

                _eventFlow.emit(value = MappingEvent.RescueRequestAccepted)
            }
        }

    }

    private fun RescueTransaction.updateRescueTransaction() {
        getUserRescueTransaction(this) { rescueTransaction ->
            _state.update { it.copy(rescueTransaction = rescueTransaction) }
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
        locationUpdatesJob = viewModelScope.launch(context = defaultDispatcher) {

            runCatching {
                mappingUseCase.getUserLocationUseCase().collect { location ->
                    broadcastRescueTransactionToRespondent(location)
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

        getUsersUpdatesJob = viewModelScope.launch(context = SupervisorJob() + defaultDispatcher) {
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
        viewModelScope.launch(context = defaultDispatcher) {
            runCatching {
                startLoading()
                mappingUseCase.deleteRescueRespondentUseCase(userId = getId(), respondentId = id)
            }.onSuccess {
                removeRescueRespondent(id)
                broadcastToNearbyCyclists()
            }.onFailure {
                it.handleDeclineRescueRequest()
            }
            finishLoading()
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
        viewModelScope.launch(context = defaultDispatcher) {
            runCatching {
                uploadUserProfile(onSuccess = {
                    _eventFlow.emit(MappingEvent.RequestHelpSuccess)
                })
            }.onFailure {
                it.handleException()
            }
        }.invokeOnCompletion {
            savedStateHandle[MAPPING_VM_STATE_KEY] = state.value
        }
    }


    private suspend inline fun uploadUserProfile(crossinline onSuccess: suspend () -> Unit) {
        coroutineScope {
            val userLocation = state.value.userLocation

            if (userLocation == null) {
                _eventFlow.emit(MappingEvent.LocationNotAvailable(reason = "Searching for GPS"))
                return@coroutineScope
            }

            getFullAddress(location = userLocation, onSuccess = onSuccess)

        }
    }
    private suspend inline fun getFullAddress(
        location: Location,
        crossinline onSuccess: suspend () -> Unit) {

        location.latitude ?: return
        location.longitude ?: return

        runCatching {
            mappingUseCase.getFullAddressUseCase(
                latitude = location.latitude,
                longitude = location.longitude)
        }.onSuccess { fullAddress ->
            uploadProfile(location = location, fullAddress = fullAddress, onSuccess = onSuccess)
        }.onFailure { exception ->
            throw exception
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
                broadcastToNearbyCyclists()
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
                _eventFlow.emit(value = MappingEvent.NoInternetConnection)
            }

            is MappingExceptions.UnexpectedErrorException -> {
                _eventFlow.emit(
                    MappingEvent.UnexpectedError(
                        reason = this.message
                    ))
            }

            is MappingExceptions.UserException -> {
                _eventFlow.emit(
                    MappingEvent.UserFailed(
                        reason = this.message
                    ))
            }

            is MappingExceptions.AddressException -> {
                _eventFlow.emit(
                    MappingEvent.AddressFailed(
                        reason = this.message ?: "Searching for GPS"
                    ))
            }

            is MappingExceptions.PhoneNumberException, is MappingExceptions.NameException -> {
                _eventFlow.emit(MappingEvent.InsufficientUserCredential)
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
            broadcastToNearbyCyclists()
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
//        onEvent(event = MappingEvent.StopPinging)
    }


    private fun getId(): String = authUseCase.getIdUseCase()

    private fun getName(): String = authUseCase.getNameUseCase()

    private suspend fun getPhoneNumber(): String =
        authUseCase.getPhoneNumberUseCase()

    private fun getPhotoUrl() = authUseCase.getPhotoUrlUseCase()


}


