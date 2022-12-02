package com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen

import android.location.Address
import android.location.Geocoder
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.core.utils.constants.MappingConstants.DEFAULT_LATITUDE
import com.example.cyclistance.core.utils.constants.MappingConstants.DEFAULT_LONGITUDE
import com.example.cyclistance.core.utils.constants.MappingConstants.MAPPING_VM_STATE_KEY
import com.example.cyclistance.feature_alert_dialog.domain.model.AlertDialogModel
import com.example.cyclistance.feature_authentication.domain.use_case.AuthenticationUseCase
import com.example.cyclistance.feature_mapping_screen.data.mapper.UserMapper.toCardModel
import com.example.cyclistance.feature_mapping_screen.data.remote.dto.rescue_transaction.Route
import com.example.cyclistance.feature_mapping_screen.data.remote.dto.rescue_transaction.Status
import com.example.cyclistance.feature_mapping_screen.data.remote.dto.user_dto.*
import com.example.cyclistance.feature_mapping_screen.domain.exceptions.MappingExceptions
import com.example.cyclistance.feature_mapping_screen.domain.model.*
import com.example.cyclistance.feature_mapping_screen.domain.use_case.MappingUseCase
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.utils.*
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

    private var getUsersUpdatesJob: Job? = null
    private var locationUpdatesJob: Job? = null
    private var getRescueTransactionUpdatesJob: Job? = null
    private var getTransactionLocationUpdatesJob: Job? = null

    private val _state: MutableStateFlow<MappingState> = MutableStateFlow(savedStateHandle[MAPPING_VM_STATE_KEY] ?: MappingState())
    val state = _state.asStateFlow()

    private val _eventFlow: MutableSharedFlow<MappingUiEvent> = MutableSharedFlow()
    val eventFlow: SharedFlow<MappingUiEvent> = _eventFlow.asSharedFlow()



    init {
        observeDataChanges()
    }

    private fun observeDataChanges(){
        subscribeToLocationUpdates()
        subscribeToNearbyUsersChanges()
        subscribeToRescueTransactionUpdates()
        subscribeToTransactionLocationUpdates()
    }

    private fun loadData() {
        viewModelScope.launch(Dispatchers.IO + SupervisorJob()) {
            // TODO: Remove this when the backend is ready
            createMockUpUsers()
            loadUsers()
            loadRescueTransaction()
            loadRescuer()
        }

    }

    private suspend fun loadRescuer(){
        coroutineScope {
           val user = state.value.nearbyCyclists
           user.getRescuer()
        }
    }

    private fun User.getRescuer(rescueTransactionItem: RescueTransactionItem? = null){

        val rescueTransaction = rescueTransactionItem ?: state.value.userRescueTransaction

        rescueTransaction?.rescuerId?.let { rescuerId ->
            val rescuer = findUser(rescuerId)
            _state.update { it.copy(rescuer = rescuer) }
        }
    }

    private fun updateRescuer(rescueTransactionItem: RescueTransactionItem){
        val nearbyCyclist = state.value.nearbyCyclists
        nearbyCyclist.getRescuer(rescueTransactionItem)
    }

    private suspend fun loadUsers() {
        coroutineScope {
            mappingUseCase.getUsersUseCase().distinctUntilChanged()
                .catch {
                    it.handleException()
                }.collect {
                    it.getUser()
                    it.loadUsers()
                    savedStateHandle[MAPPING_VM_STATE_KEY] = state.value
                }
        }
    }


    private suspend fun loadRescueTransaction() {
        coroutineScope {
            runCatching {
                val transactionId = state.value.user.transaction?.transactionId
                transactionId?.let { mappingUseCase.getRescueTransactionByIdUseCase(it) }
            }.onSuccess { rescueTransaction ->
                _state.update { it.copy(userRescueTransaction = rescueTransaction) }
                savedStateHandle[MAPPING_VM_STATE_KEY] = state.value
            }.onFailure {
                it.handleException()
            }
        }
    }


    private fun RescueTransaction.getUserRescueTransaction(){
        val transactionId = state.value.user.transaction?.transactionId
        val rescueTransaction = transactionId?.let { findRescueTransaction(it) }
        rescueTransaction?.let { transaction ->
            _state.update { it.copy(userRescueTransaction = transaction) }
            updateRescuer(transaction)
        }
    }

    fun onEvent(event: MappingEvent) {
        when (event) {

            is MappingEvent.SearchAssistance -> {
                uploadUserProfile()
            }

            is MappingEvent.DismissNoInternetDialog -> {
                _state.update { it.copy(hasInternet = true) }
            }

            is MappingEvent.LoadData -> {
                loadData()
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

    private fun subscribeToTransactionLocationUpdates(){
        getTransactionLocationUpdatesJob?.cancel()
        getTransactionLocationUpdatesJob = viewModelScope.launch(Dispatchers.IO + SupervisorJob()) {
            mappingUseCase.getTransactionLocationUpdatesUseCase().distinctUntilChanged()
                .catch {
                    it.handleException()
                }.collect { liveLocation ->
                    updateTransactionLocation(liveLocation)
                    savedStateHandle[MAPPING_VM_STATE_KEY] = state.value
                }
        }
    }
    private fun updateTransactionLocation(liveLocation: LiveLocationWSModel){

        val userLocation = state.value.userLocation
        val eta = getEstimatedTimeArrival(startingLocation = Location(
            latitude = liveLocation.latitude,
            longitude = liveLocation.longitude), endLocation = userLocation)

        _state.update {
            it.copy(
                rescuerETA = eta,
                transactionLocation = Location(
                    latitude = liveLocation.latitude,
                    longitude = liveLocation.longitude))
        }
    }

    private fun getEstimatedTimeArrival(startingLocation: Location, endLocation: Location):String{
        val distance = getCalculatedDistance(startingLocation, endLocation)
        return getCalculatedETA(distance)
    }

    private fun getCalculatedDistance(startingLocation: Location, endLocation: Location): Double{
        val start = SimpleLocation.Point(startingLocation.latitude, startingLocation.longitude)
        val end = SimpleLocation.Point(endLocation.latitude, endLocation.longitude)
        return SimpleLocation.calculateDistance(start, end)
    }


    private fun acceptRescueRequest(cardModel: CardModel) {
        viewModelScope.launch(Dispatchers.IO + SupervisorJob()) {

            val user = state.value.user
            val userHasCurrentTransaction = (user.transaction ?: Transaction()).transactionId.isNotEmpty()

            val rescuer = state.value.nearbyCyclists.findUser(cardModel.id!!)
            val rescuerHasCurrentTransaction = (rescuer.transaction ?: Transaction()).transactionId.isNotEmpty()

            val transactionId =
                user.id + rescuer.id + System.currentTimeMillis().toString().takeLast(5)

            if (rescuer.location == null) {
                _eventFlow.emit(value = MappingUiEvent.ShowToastMessage("Can't reach Rescuer"))
                return@launch
            }

            if (user.location == null) {
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
                broadcastRescueTransaction()
                assignTransaction(rescueTransaction,user, rescuer, transactionId)
            }.onFailure { exception ->
                finishLoading()
                exception.handleException()
            }

            savedStateHandle[MAPPING_VM_STATE_KEY] = state.value

        }
    }

    private suspend fun broadcastRescueTransaction(){
        runCatching {
            mappingUseCase.broadcastRescueTransactionUseCase()
        }.onFailure {
           it.handleException()
        }
    }

    private suspend fun broadcastUser(){
        runCatching {
            mappingUseCase.broadcastUserUseCase()
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
        }.onFailure { exception ->
            finishLoading()
            exception.handleException()
        }

    }

    private fun updateTransactionETA(rescuer: UserItem,rescueTransaction: RescueTransactionItem ){
        val userLocation = state.value.userLocation
        val estimatedTimeArrival = rescuer.location?.let { getEstimatedTimeArrival(startingLocation = it, endLocation = userLocation) }
        _state.update {
            it.copy(userRescueTransaction = rescueTransaction,
                rescuerETA = estimatedTimeArrival ?: "",
                rescuer = rescuer,
                searchAssistanceButtonVisible = false)
        }
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
            user = UserItem(
                id = id, userAssistance = UserAssistance(needHelp = false),
                transaction = Transaction(role = role, transactionId = this),
                rescueRequest = RescueRequest()
            )
        )
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
        viewModelScope.launch(Dispatchers.IO + SupervisorJob()) {
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
                broadcastUser()
            }.onFailure { exception ->
                Timber.e("Failed to cancel search assistance: ${exception.message}")
                exception.handleException()
            }
            finishLoading()
            savedStateHandle[MAPPING_VM_STATE_KEY] = state.value
        }
    }

    private fun updateLocation(location: android.location.Location){
        _state.update { state ->
            state.copy(
                userLocation = Location(
                latitude = location.latitude.takeIf { it != 0.0 } ?: DEFAULT_LATITUDE,
                longitude = location.longitude.takeIf { it != 0.0 } ?: DEFAULT_LONGITUDE,
                )
            )
        }
    }





    private fun User.findUser(id: String): UserItem {
        return this.users.find {
            it.id == id
        } ?: UserItem()

    }

    private fun RescueTransaction.findRescueTransaction(id: String): RescueTransactionItem {
        return this.rescueTransactions.find {
            it.id == id
        } ?: RescueTransactionItem()
    }

    private fun User.getUser() {
        runCatching {
            getId()
        }.onSuccess { id ->
            val user = findUser(id = id)
            _state.update { it.copy(user = user) }
            user.getUserRescueRespondents(this)
        }.onFailure {
            Timber.e("Failed to get User ID")
        }

    }

    private fun UserItem.getUserRescueRespondents(user: User) {
        val rescueRespondentsSnapShot: MutableList<CardModel> = mutableListOf()
        rescueRequest?.respondents?.forEachIndexed { index, respondent ->
            val userRespondent = user.findUser(id = respondent.clientId)
            val distance = location?.let { start ->
                userRespondent.location?.let { end ->
                    SimpleLocation.calculateDistance(start.latitude, start.longitude, end.latitude, end.longitude)
                }
            }

            distance?.let{
                val formattedETA = getCalculatedETA(distanceMeters = distance)
                rescueRespondentsSnapShot.add(index = index, element = userRespondent.toCardModel(distance = distance.distanceFormat(), eta = formattedETA))
            }

        }
        _state.update {
            it.copy(userRescueRequestRespondents = RescueRequestRespondents(
                respondents = rescueRespondentsSnapShot.distinct().toImmutableList()))
        }
        rescueRespondentsSnapShot.clear()
    }

    private fun User.loadUsers() {
        _state.update { it.copy(nearbyCyclists = this) }
    }

    private fun RescueTransactionItem.broadCastLocationToTransaction(location: android.location.Location){
        runCatching {

            if (this.id.isNullOrEmpty()) {
                return
            }

            if(this.cancellation?.rescueCancelled == true){
                return
            }

            mappingUseCase.broadcastTransactionLocationUseCase(
                LiveLocationWSModel(
                    latitude = location.latitude,
                    longitude = location.longitude,
                    room = this.id))

        }.onFailure {
            Timber.v("Broadcasting location to transaction failed: ${it.message}")
        }
    }







    private fun unSubscribeToTransactionLocationUpdates(){
        getTransactionLocationUpdatesJob?.cancel()
    }
    private fun subscribeToRescueTransactionUpdates() {
        getRescueTransactionUpdatesJob?.cancel()
        getRescueTransactionUpdatesJob = viewModelScope.launch(Dispatchers.IO + SupervisorJob()) {
            mappingUseCase.getRescueTransactionUpdatesUseCase().distinctUntilChanged()
                .catch {
                    Timber.e("ERROR GETTING RESCUE TRANSACTION: ${it.message}")
                }.collect {
                    it.getUserRescueTransaction()
                    savedStateHandle[MAPPING_VM_STATE_KEY] = state.value
                    Timber.v("COLLECTING RESCUE TRANSACTIONS")
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
        locationUpdatesJob?.cancel()
        locationUpdatesJob = viewModelScope.launch(Dispatchers.IO) {
            mappingUseCase.getUserLocationUseCase()
                .catch {
                    Timber.e("Error Location Updates: ${it.message}")
                }.collect { location ->
                    val rescueTransaction = state.value.userRescueTransaction
                    rescueTransaction?.broadCastLocationToTransaction(location)
                    geocoder.getAddress(location.latitude, location.longitude) { addresses ->
                        _state.update { it.copy(userAddress = UserAddress(addresses.lastOrNull())) }
                    }
                    updateLocation(location)
                    savedStateHandle[MAPPING_VM_STATE_KEY] = state.value
                }
        }
    }
    private fun subscribeToNearbyUsersChanges() {
        getUsersUpdatesJob?.cancel()
        getUsersUpdatesJob = viewModelScope.launch(Dispatchers.IO + SupervisorJob()) {
            mappingUseCase.getUserUpdatesUseCase().distinctUntilChanged()
                .catch {
                    Timber.e("ERROR GETTING USERS: ${it.message}")
                    it.handleException()
                }.collect {
                    it.getUser()
                    it.loadUsers()
                    it.getRescuer()
                    savedStateHandle[MAPPING_VM_STATE_KEY] = state.value
                }
        }
    }
    private fun unSubscribeToLocationUpdates() {
        locationUpdatesJob?.cancel()
    }













    private fun declineRescueRequest(cardModel: CardModel) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                startLoading()
                val rescueRespondents = state.value.userRescueRequestRespondents.respondents
                _state.update {
                    it.copy(userRescueRequestRespondents = RescueRequestRespondents(
                        rescueRespondents.toMutableList().apply {
                            remove(element = cardModel)
                        }))
                }

                mappingUseCase.deleteRescueRespondentUseCase(
                    userId = getId(),
                    respondentId = cardModel.id!!
                )
            }.onSuccess {
                broadcastUser()
            }.onFailure {
                it.handleDeclineRescueRequest()
            }
            finishLoading()
            savedStateHandle[MAPPING_VM_STATE_KEY] = state.value

        }
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
                broadcastUser()
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
