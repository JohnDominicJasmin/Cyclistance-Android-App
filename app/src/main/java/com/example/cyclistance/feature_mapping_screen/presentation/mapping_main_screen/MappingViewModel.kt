package com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen

import android.graphics.drawable.Drawable
import android.location.Address
import android.location.Geocoder
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.core.utils.constants.MappingConstants.CYCLIST_MAP_ICON_HEIGHT
import com.example.cyclistance.core.utils.constants.MappingConstants.CYCLIST_MAP_ICON_WIDTH
import com.example.cyclistance.core.utils.constants.MappingConstants.DEFAULT_LATITUDE
import com.example.cyclistance.core.utils.constants.MappingConstants.IMAGE_PLACEHOLDER_URL
import com.example.cyclistance.core.utils.constants.MappingConstants.MAPPING_VM_STATE_KEY
import com.example.cyclistance.feature_authentication.domain.use_case.AuthenticationUseCase
import com.example.cyclistance.feature_mapping_screen.data.mapper.UserMapper.toCardModel
import com.example.cyclistance.feature_mapping_screen.data.mapper.UserMapper.toRespondent
import com.example.cyclistance.feature_mapping_screen.data.remote.dto.user_dto.ConfirmationDetail
import com.example.cyclistance.feature_mapping_screen.data.remote.dto.user_dto.Location
import com.example.cyclistance.feature_mapping_screen.data.remote.dto.user_dto.RescueRequest
import com.example.cyclistance.feature_mapping_screen.data.remote.dto.user_dto.UserAssistance
import com.example.cyclistance.feature_mapping_screen.domain.exceptions.MappingExceptions
import com.example.cyclistance.feature_mapping_screen.domain.model.CardModel
import com.example.cyclistance.feature_mapping_screen.domain.model.RescueTransactionItem
import com.example.cyclistance.feature_mapping_screen.domain.model.UserItem
import com.example.cyclistance.feature_mapping_screen.domain.use_case.MappingUseCase
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
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

    private val _userDrawableImage: MutableState<Drawable?> = mutableStateOf(null)
    val userDrawableImage: State<Drawable?> = _userDrawableImage


    init {
        // TODO: Remove this when the backend is ready
        createMockUpUsers()
    }

    fun onEvent(event: MappingEvent) {
        when (event) {

            is MappingEvent.SearchAssistance -> {
                uploadUserProfile()
            }

            is MappingEvent.PostLocation -> {
//                postLocation() todo: remove this later
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


            is MappingEvent.LoadUserImageLocationPuck -> {
                getUserDrawableImage()
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
            runCatching {
                mappingUseCase.getRescueTransactionUpdatesUseCase().distinctUntilChanged()
                    .collect {
//                    todo: filter what rescue transaction needed
                    }
                mappingUseCase.broadcastRescueTransactionUseCase()
            }.onFailure {
                Timber.e("ERROR GETTING USERS: ${it.message}")
            }
        }
    }

    private fun unSubscribeToNearbyRescueTransaction() {
        getRescueTransactionJob?.cancel()
    }

    private fun unSubscribeToNearbyUsersChanges() {
        getUsersJob?.cancel()
    }

    private fun acceptRescueRequest(cardModel: CardModel){
        viewModelScope.launch {
            runCatching {
             mappingUseCase.getUserByIdUseCase(id = cardModel.id!!)
            }.onSuccess { user ->
                val transactionId = user.transaction?.transactionId
                if(transactionId.isNullOrEmpty()){
                    Timber.v("Transaction id is null or empty")
                    return@onSuccess
                }
                Timber.v("Transaction is on process.")


            }.onFailure {
                it.handleException()
            }

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
                mappingUseCase.createUserUseCase(
                    user = UserItem(
                        id = getId(),
                        rescueRequest = RescueRequest(respondents = updatedState.userRescueRequestRespondents.respondents.map { it.toRespondent() })
                    )
                )
            }.onSuccess {
                Timber.v("Successfully updated user")
                mappingUseCase.broadcastUserUseCase()
            }.onFailure {
                Timber.e("Failed to update user: ${it.message}")
                it.handleDeclineRescueRequest()
            }
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

    private fun postLocation() {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                mappingUseCase.createUserUseCase(
                    user = UserItem(
                        id = getId(),
                        location = Location(
                            latitude = state.value.latitude,
                            longitude = state.value.longitude,
                        ),
                        profilePictureUrl = state.value.photoUrl))
            }.onSuccess {
                Timber.v("Successfully posted location")
                mappingUseCase.broadcastUserUseCase()
            }.onFailure {
                Timber.e("Failed to post location: ${it.message}")
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
                _state.update { it.copy(isLoading = true) }
                mappingUseCase.createUserUseCase(
                    user = UserItem(
                        id = getId(),
                        userAssistance = UserAssistance(
                            needHelp = false,
                            confirmationDetail = ConfirmationDetail()))
                )

            }.onSuccess {
                _state.update {
                    it.copy(
                        isLoading = false,
                        searchAssistanceButtonVisible = true)
                }
                mappingUseCase.broadcastUserUseCase()
            }.onFailure { exception ->
                Timber.e("Failed to cancel search assistance: ${exception.message}")
                _state.update { it.copy(isLoading = false) }
                exception.handleException()
            }
        }.invokeOnCompletion {
            savedStateHandle[MAPPING_VM_STATE_KEY] = state.value
        }
    }

    private fun getUserDrawableImage() {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                mappingUseCase.imageUrlToDrawableUseCase(getPhotoUrl())
            }.onSuccess { drawableImage ->
                _userDrawableImage.value = drawableImage
            }.onFailure {
                Timber.e("GET USER DRAWABLE IMAGE: ${it.message}")
            }
        }.invokeOnCompletion {
            savedStateHandle[MAPPING_VM_STATE_KEY] = state.value
        }
    }


    private fun subscribeToNearbyUsersChanges() {
        getUsersJob?.cancel()
        getUsersJob = viewModelScope.launch(Dispatchers.IO + SupervisorJob()) {
            runCatching {
                mappingUseCase.getUserUpdatesUseCase().distinctUntilChanged()
                    .collect {
                        it.users.getUser()
                        it.users.getUsers()
                        savedStateHandle[MAPPING_VM_STATE_KEY] = state.value
                    }
                mappingUseCase.broadcastUserUseCase()

            }.onFailure {
                Timber.e("ERROR GETTING USERS: ${it.message}")
            }
        }
    }

    private fun NearbyCyclists.findUser(id: String): UserItem {
        return this.activeUsers.find {
            it.user.id == id
        }?.user ?: UserItem()

    }

    private fun List<UserItem>.findUser(id: String): UserItem {
        return this.find { it.id == id } ?: UserItem()
    }

    private fun List<UserItem>.getUser() {
        val user = findUser(id = getId())
        _state.update { it.copy(user = user) }
        getUserRescueRespondents()
    }

    private fun List<UserItem>.getUserRescueRespondents() {
        val stateUser = state.value.user
        val rescueRespondentsSnapShot: MutableList<CardModel> = mutableListOf()
        stateUser.rescueRequest?.respondents?.forEachIndexed { index, respondent ->
            val user = findUser(id = respondent.clientId)
            rescueRespondentsSnapShot.add(index = index, element = user.toCardModel())
        }
        _state.update {
            it.copy(
                userRescueRequestRespondents = RescueRequestRespondents(
                    respondents = rescueRespondentsSnapShot.toSet().toList().toImmutableList()))
        }
        rescueRespondentsSnapShot.clear()
    }

    private suspend fun List<UserItem>.getUsers() {
        val nearbyCyclistSnapShot: MutableList<Cyclist> = mutableListOf()
        this.forEachIndexed { index, user ->
            val bitmapProfile = mappingUseCase.imageUrlToDrawableUseCase(
                user.profilePictureUrl ?: IMAGE_PLACEHOLDER_URL)
            nearbyCyclistSnapShot.add(
                index = index,
                element = Cyclist(
                    user, bitmapProfile.toBitmap(
                        width = CYCLIST_MAP_ICON_HEIGHT,
                        height = CYCLIST_MAP_ICON_WIDTH)))
        }
        _state.update {
            it.copy(
                nearbyCyclists = NearbyCyclists(
                    activeUsers = nearbyCyclistSnapShot.toSet().toList().toImmutableList()))
        }
        nearbyCyclistSnapShot.clear()
    }


    private fun subscribeToLocationUpdates() {
        locationUpdatesJob?.cancel()
        locationUpdatesJob = viewModelScope.launch(Dispatchers.IO) {
            runCatching {

                mappingUseCase.getUserLocationUseCase().collect { location ->
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
            }.onFailure {
                Timber.e("Error Location Updates: ${it.message}")
            }
        }
    }

    private fun unSubscribeToLocationUpdates() {
        locationUpdatesJob?.cancel()
    }

    private fun signOutAccount() {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                authUseCase.signOutUseCase()
            }.onSuccess {
                _eventFlow.emit(value = MappingUiEvent.ShowSignInScreen)
            }.onFailure {
                Timber.e("Error Sign out account: ${it.message}")
            }
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
                _state.update { it.copy(currentAddress = currentAddress, isLoading = true) }
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
                _state.update { it.copy(isLoading = false, searchAssistanceButtonVisible = false) }
                mappingUseCase.broadcastUserUseCase()
                _eventFlow.emit(MappingUiEvent.ShowConfirmDetailsScreen)

            }.onFailure { exception ->
                Timber.e("Error uploading profile: ${exception.message}")
                _state.update { it.copy(isLoading = false) }
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

        }
        savedStateHandle[MAPPING_VM_STATE_KEY] = state.value
    }



    private fun createMockUpUsers() {
        viewModelScope.launch {
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
