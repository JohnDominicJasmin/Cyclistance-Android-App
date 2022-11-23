package com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen

import android.location.Address
import android.location.Geocoder
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.core.utils.constants.MappingConstants.DEFAULT_BIKE_AVERAGE_SPEED_KM
import com.example.cyclistance.core.utils.constants.MappingConstants.DEFAULT_LATITUDE
import com.example.cyclistance.core.utils.constants.MappingConstants.MAPPING_VM_STATE_KEY
import com.example.cyclistance.feature_alert_dialog.domain.model.AlertDialogModel
import com.example.cyclistance.feature_authentication.domain.use_case.AuthenticationUseCase
import com.example.cyclistance.feature_mapping_screen.data.mapper.UserMapper.toCardModel
import com.example.cyclistance.feature_mapping_screen.data.mapper.UserMapper.toRespondent
import com.example.cyclistance.feature_mapping_screen.data.remote.dto.rescue_transaction.Route
import com.example.cyclistance.feature_mapping_screen.data.remote.dto.rescue_transaction.Status
import com.example.cyclistance.feature_mapping_screen.data.remote.dto.user_dto.*
import com.example.cyclistance.feature_mapping_screen.domain.exceptions.MappingExceptions
import com.example.cyclistance.feature_mapping_screen.domain.model.*
import com.example.cyclistance.feature_mapping_screen.domain.use_case.MappingUseCase
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.utils.*
import com.mapbox.geojson.Point
import dagger.hilt.android.lifecycle.HiltViewModel
import im.delight.android.location.SimpleLocation
import io.github.farhanroy.composeawesomedialog.R
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import okhttp3.internal.toImmutableList
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MappingViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val authUseCase: AuthenticationUseCase,
    private val geocoder: Geocoder,
    private val mappingUseCase: MappingUseCase) : ViewModel() {

    private var getUsersJob: Job? = null
    private var locationUpdatesJob: Job? = null
    private var getRescueTransactionJob: Job? = null

    private val _state: MutableStateFlow<MappingState> = MutableStateFlow(savedStateHandle[MAPPING_VM_STATE_KEY] ?: MappingState())
    val state = _state.asStateFlow()

    private val _eventFlow: MutableSharedFlow<MappingUiEvent> = MutableSharedFlow()
    val eventFlow: SharedFlow<MappingUiEvent> = _eventFlow.asSharedFlow()



    init {
        // TODO: Remove this when the backend is ready
        createMockUpUsers()
        getUsers()
    }

    private fun getUsers(){
        viewModelScope.launch {
            mappingUseCase.getUsersUseCase().distinctUntilChanged()
                .catch {
                    it.handleException()
                }.collect {
                    it.getUser()
                    it.getUsers()
                    savedStateHandle[MAPPING_VM_STATE_KEY] = state.value
                }
        }
    }


    fun onEvent(event: MappingEvent) {
        when (event) {

            is MappingEvent.SearchAssistance -> {
                uploadUserProfile()
            }

            is MappingEvent.ChangeCameraState -> {
                _state.update {
                    it.copy(
                        cameraState = CameraState(
                            cameraPosition = event.cameraPosition,
                            cameraZoom = event.cameraZoomLevel))
                }
            }

            is MappingEvent.DeclineRescueRequest -> {
                declineRescueRequest(event.cardModel)
            }

            is MappingEvent.AcceptRescueRequest -> {
                acceptRescueRequest(event.cardModel)
            }

            is MappingEvent.CancelSearchAssistance -> {
                cancelSearchAssistance()
            }
            is MappingEvent.LoadUserProfile -> {
                loadUserProfile()
            }

            is MappingEvent.StartPinging -> {
                _state.update { it.copy(isSearchingForAssistance = true) }
            }

            is MappingEvent.StopPinging -> {
                _state.update { it.copy(isSearchingForAssistance = false) }
            }

            is MappingEvent.SubscribeToRescueTransactionChanges -> {
                subscribeToNearbyRescueTransaction()
            }

            is MappingEvent.UnsubscribeToRescueTransactionChanges -> {
                unSubscribeToNearbyRescueTransaction()
            }

            is MappingEvent.SubscribeToLocationUpdates -> {
                subscribeToLocationUpdates()
            }

            is MappingEvent.UnsubscribeToLocationUpdates -> {
                unSubscribeToLocationUpdates()
            }

            is MappingEvent.SubscribeToNearbyUsersChanges -> {
                subscribeToNearbyUsersChanges()
            }
            is MappingEvent.UnsubscribeToNearbyUsersChanges -> {
                unSubscribeToNearbyUsersChanges()
            }

            is MappingEvent.DismissNoInternetScreen -> {
                _state.update { it.copy(hasInternet = true) }
            }

            is MappingEvent.DismissAlertDialog -> {
                _state.update { it.copy(alertDialogModel = AlertDialogModel()) }
            }

            is MappingEvent.SignOut -> {
                signOutAccount()
            }


            is MappingEvent.ChangeBottomSheet -> {
                _state.update { it.copy(bottomSheetType = event.bottomSheetType) }
            }
        }
        savedStateHandle[MAPPING_VM_STATE_KEY] = state.value
    }

    private fun subscribeToNearbyRescueTransaction() {
        getRescueTransactionJob?.cancel()
        getRescueTransactionJob = viewModelScope.launch(Dispatchers.IO + SupervisorJob()) {
            mappingUseCase.getRescueTransactionUpdatesUseCase().distinctUntilChanged()
                .catch {
                    Timber.e("ERROR GETTING RESCUE TRANSACTION: ${it.message}")
                }.collect {
//                    todo: filter what rescue transaction needed
                    savedStateHandle[MAPPING_VM_STATE_KEY] = state.value
                    Timber.v("COLLECTING RESCUE TRANSACTIONS")
                }
        }
    }

    private fun unSubscribeToNearbyRescueTransaction() {
        getRescueTransactionJob?.cancel()
    }

    private fun unSubscribeToNearbyUsersChanges() {
        getUsersJob?.cancel()
    }

    private fun acceptRescueRequest(cardModel: CardModel) {
        viewModelScope.launch(Dispatchers.IO + SupervisorJob()) {

            val user = state.value.user
            val userHasCurrentTransaction = (user.transaction ?: Transaction()).transactionId.isNotEmpty()

            val rescuer = state.value.nearbyCyclists.findUser(cardModel.id!!)
            val rescuerHasCurrentTransaction = (rescuer.transaction ?: Transaction()).transactionId.isNotEmpty()

            val transactionId = user.id + rescuer.id + System.currentTimeMillis().toString().takeLast(5)

            if(rescuer.location == null){
                _eventFlow.emit(value = MappingUiEvent.ShowToastMessage("Can't reach Rescuer"))
                return@launch
            }

            if(user.location == null){
                _eventFlow.emit(value = MappingUiEvent.ShowToastMessage("Location not found"))
                return@launch
            }

            if (userHasCurrentTransaction) {
                rescueeCannotRequest()
                return@launch
            }

            if (rescuerHasCurrentTransaction) {
                rescuerCannotRequest()
                return@launch
            }

            runCatching {
                startLoading()

                RescueTransactionItem(
                    id = transactionId,
                    rescuerId = rescuer.id,
                    rescueeId = user.id,
                    status = Status(started = true, ongoing = true),
                    route = Route(
                        startingLocation = Location(
                            latitude = rescuer.location.latitude,
                            longitude = rescuer.location.longitude
                        ),
                        destinationLocation = Location(
                            latitude = user.location.latitude,
                            longitude = user.location.longitude)
                    )).apply {
                    mappingUseCase.createRescueTransactionUseCase(rescueTransaction = this)
                }

            }.onSuccess { rescueTransaction ->
                mappingUseCase.broadcastRescueTransactionUseCase()
                assignTransaction(rescueTransaction,user, rescuer, transactionId)
            }.onFailure { exception ->
                finishLoading()
                exception.handleException()
            }

            savedStateHandle[MAPPING_VM_STATE_KEY] = state.value

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
            _state.update { it.copy(rescuer = rescuer, searchAssistanceButtonVisible = false) }
            _state.update { it.copy(rescueTransaction = rescueTransaction) }
            mappingUseCase.broadcastUserUseCase()
            _eventFlow.emit(value = MappingUiEvent.ShowMappingScreen)
            finishLoading()
        }.onFailure { exception ->
            finishLoading()
            exception.handleException()
        }

    }

    private fun startLoading() {
        _state.update { it.copy(isLoading = true) }
    }

    private fun finishLoading() {
        _state.update { it.copy(isLoading = false) }
    }


    private fun rescueeCannotRequest(){
        _state.update { it.copy(alertDialogModel = AlertDialogModel(
            title = "Cannot Request",
            description = "You can only have one transaction at a time",
            icon = R.raw.error
        )) }
    }
    private fun rescuerCannotRequest(){
        _state.update { it.copy(alertDialogModel = AlertDialogModel(
            title = "Cannot Request",
            description = "Unfortunately the Rescuer is currently in a transaction.",
            icon = R.raw.error
        )) }
    }

    private suspend fun String.assignTransaction(role: String, id: String?) {
        mappingUseCase.createUserUseCase(
            user = UserItem(
                id = id, userAssistance = UserAssistance(needHelp = false),
                transaction = Transaction(role = role, transactionId = this),
                rescueRequest = RescueRequest()
            )
        )
    }

    private fun declineRescueRequest(cardModel: CardModel) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                val rescueRespondents = state.value.userRescueRequestRespondents.respondents
                val updatedState = _state.updateAndGet {
                    it.copy(userRescueRequestRespondents = RescueRequestRespondents(
                        rescueRespondents.toMutableList().apply {
                            remove(element = cardModel)
                        }))
                }
                startLoading()
                mappingUseCase.createUserUseCase(
                    user = UserItem(
                        id = getId(),
                        rescueRequest = RescueRequest(respondents = updatedState.userRescueRequestRespondents.respondents.map { it.toRespondent() })
                    )
                )
            }.onSuccess {
                mappingUseCase.broadcastUserUseCase()
            }.onFailure {
                it.handleDeclineRescueRequest()
            }
            finishLoading()
            savedStateHandle[MAPPING_VM_STATE_KEY] = state.value

        }
    }

    private suspend fun Throwable.handleDeclineRescueRequest() {
        when (this) {
            is MappingExceptions.NetworkException -> {
                _eventFlow.emit(MappingUiEvent.ShowToastMessage("No internet connection"))
            }
            else -> {
                Timber.d("Failed to update user")
            }
        }
    }



    private fun loadUserProfile() {
        viewModelScope.launch(Dispatchers.IO) {
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


    private fun cancelSearchAssistance() {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                startLoading()
                mappingUseCase.createUserUseCase(
                    user = UserItem(
                        id = getId(),
                        userAssistance = UserAssistance(
                            needHelp = false,
                            confirmationDetail = ConfirmationDetail()))
                )

            }.onSuccess {
                _state.update { it.copy(searchAssistanceButtonVisible = true) }
                mappingUseCase.broadcastUserUseCase()
            }.onFailure { exception ->
                Timber.e("Failed to cancel search assistance: ${exception.message}")
                exception.handleException()
            }
            finishLoading()
            savedStateHandle[MAPPING_VM_STATE_KEY] = state.value
        }
    }



    private fun subscribeToNearbyUsersChanges() {
        getUsersJob?.cancel()
        getUsersJob = viewModelScope.launch(Dispatchers.IO + SupervisorJob()) {
            mappingUseCase.getUserUpdatesUseCase().distinctUntilChanged()
                .catch {
                    Timber.e("ERROR GETTING USERS: ${it.message}")
                    it.handleException()
                }.collect {
                    it.getUser()
                    it.getUsers()
                    savedStateHandle[MAPPING_VM_STATE_KEY] = state.value
                }
        }
    }
    private fun User.findUser(id: String): UserItem {
        return this.users.find {
            it.id == id
        } ?: UserItem()

    }

    private fun User.getUser() {
        val user = findUser(id = getId())
        _state.update { it.copy(user = user) }
        getUserRescueRespondents()
    }

    private fun User.getUserRescueRespondents() {
        val user = state.value.user
        val rescueRespondentsSnapShot: MutableList<CardModel> = mutableListOf()
        user.rescueRequest?.respondents?.forEachIndexed { index, respondent ->
            val userRespondent = findUser(id = respondent.clientId)
            val distance = SimpleLocation.calculateDistance(
                SimpleLocation.Point(user.location!!.latitude, user.location.longitude),
                SimpleLocation.Point(userRespondent.location!!.latitude, userRespondent.location.longitude))
            val formattedETA = getETA(distanceMeters = distance, averageSpeedKm = DEFAULT_BIKE_AVERAGE_SPEED_KM)
            rescueRespondentsSnapShot.add(index = index, element = userRespondent.toCardModel(distance = distance.distanceFormat(), eta = formattedETA))
        }
        _state.update {
            it.copy(userRescueRequestRespondents = RescueRequestRespondents(
                    respondents = rescueRespondentsSnapShot.distinct().toImmutableList()))
        }
        rescueRespondentsSnapShot.clear()
    }

    private fun User.getUsers() {
        _state.update { it.copy(nearbyCyclists = this) }
    }

    private fun subscribeToLocationUpdates() {
        locationUpdatesJob?.cancel()
        locationUpdatesJob = viewModelScope.launch(Dispatchers.IO) {
            mappingUseCase.getUserLocationUseCase()
                .catch {
                    Timber.e("Error Location Updates: ${it.message}")
                }.collect { location ->
                    geocoder.getAddress(location.latitude, location.longitude) { addresses ->
                        _state.update { it.copy(userAddress = UserAddress(addresses.lastOrNull())) }
                    }

                    _state.update { state ->
                        state.copy(
                            latitude = location.latitude.takeIf { it != 0.0 } ?: DEFAULT_LATITUDE,
                            longitude = location.longitude.takeIf { it != 0.0 } ?: DEFAULT_LATITUDE,
                        )
                    }
                    savedStateHandle[MAPPING_VM_STATE_KEY] = state.value
                }
            }
    }

    private fun unSubscribeToLocationUpdates() {
        locationUpdatesJob?.cancel()
    }

    private fun signOutAccount() {
        viewModelScope.launch(Dispatchers.IO) {
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

    private fun uploadUserProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            val address = state.value.userAddress.address
            if (address != null) {
                uploadProfile(address)
                return@launch
            }
            _eventFlow.emit(MappingUiEvent.ShowToastMessage(message = "Searching for GPS"))

        }.invokeOnCompletion {
            savedStateHandle[MAPPING_VM_STATE_KEY] = state.value
        }
    }

    private suspend fun uploadProfile(address: Address) {
        coroutineScope {
            runCatching {
                val currentAddress = address.getFullAddress()
                _state.update { it.copy(currentAddress = currentAddress) }
                startLoading()
                mappingUseCase.createUserUseCase(
                    user = UserItem(
                        id = getId(),
                        name = getName(),
                        address = currentAddress,
                        profilePictureUrl = getPhotoUrl(),
                        contactNumber = getPhoneNumber(),
                        location = Location(
                            latitude = address.latitude,
                            longitude = address.longitude)))
                mappingUseCase.updateAddressUseCase(currentAddress)

            }.onSuccess {
                finishLoading()
                _state.update { it.copy(searchAssistanceButtonVisible = false) }
                mappingUseCase.broadcastUserUseCase()
                _eventFlow.emit(MappingUiEvent.ShowConfirmDetailsScreen)

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

    private fun createMockUpUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                mappingUseCase.createMockUsers()
            }.onSuccess {
                Timber.v("CREATED MOCK USERS!")
                mappingUseCase.broadcastUserUseCase()
            }.onFailure {
                Timber.e("FAILED TO CREATE MOCK USERS: ${it.message}")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        onEvent(event = MappingEvent.UnsubscribeToLocationUpdates)
        onEvent(event = MappingEvent.UnsubscribeToNearbyUsersChanges)
        onEvent(event = MappingEvent.UnsubscribeToRescueTransactionChanges)
        onEvent(event = MappingEvent.StopPinging)
    }

    private fun getId(): String = authUseCase.getIdUseCase()

    private fun getName(): String = authUseCase.getNameUseCase()

    private suspend fun getPhoneNumber(): String =
        authUseCase.getPhoneNumberUseCase()

    private fun getPhotoUrl() = authUseCase.getPhotoUrlUseCase()


}
