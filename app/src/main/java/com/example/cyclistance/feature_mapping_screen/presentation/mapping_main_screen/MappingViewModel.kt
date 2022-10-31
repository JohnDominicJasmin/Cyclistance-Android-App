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
import com.example.cyclistance.core.utils.constants.MappingConstants.INTERVAL_UPDATE_USERS
import com.example.cyclistance.core.utils.constants.MappingConstants.MAPPING_VM_STATE_KEY
import com.example.cyclistance.feature_authentication.domain.use_case.AuthenticationUseCase
import com.example.cyclistance.feature_mapping_screen.data.mapper.UserMapper.toCardModel
import com.example.cyclistance.feature_mapping_screen.data.remote.dto.user_dto.ConfirmationDetail
import com.example.cyclistance.feature_mapping_screen.data.remote.dto.user_dto.Location
import com.example.cyclistance.feature_mapping_screen.data.remote.dto.user_dto.UserAssistance
import com.example.cyclistance.feature_mapping_screen.domain.exceptions.MappingExceptions
import com.example.cyclistance.feature_mapping_screen.domain.model.CardModel
import com.example.cyclistance.feature_mapping_screen.domain.model.User
import com.example.cyclistance.feature_mapping_screen.domain.use_case.MappingUseCase
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.utils.createMockUsers
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.utils.getAddress
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.utils.getFullAddress
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
    private var locationUpdatesFlow: Job? = null

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
                postLocation()
            }

            is MappingEvent.DeclineRescueRequest -> {
                /*todo*/
            }

            is MappingEvent.AcceptRescueRequest -> {
                /*todo*/
            }

            is MappingEvent.CancelSearchAssistance -> {
                cancelSearchAssistance()
            }
            is MappingEvent.LoadUserProfile -> {
                loadUserProfile()
            }

            is MappingEvent.LoadUsers -> {
                getNearbyUsers()
                getUserDrawableImage()
            }
            is MappingEvent.StartPinging -> {
                _state.update { it.copy(isSearchingForAssistance = true) }
            }

            is MappingEvent.StopPinging -> {
                _state.update { it.copy(isSearchingForAssistance = false) }
            }

            is MappingEvent.DismissNoInternetScreen -> {
                _state.update { it.copy(hasInternet = true) }
            }

            is MappingEvent.SignOut -> {
                signOutAccount()
            }
            is MappingEvent.SubscribeToLocationUpdates -> {
                subscribeToLocationUpdates()
            }

            is MappingEvent.UnsubscribeToLocationUpdates -> {
                unSubscribeToLocationUpdates()
            }

            is MappingEvent.ChangeBottomSheet -> {
                _state.update { it.copy(bottomSheetType = event.bottomSheetType) }
            }
        }
        savedStateHandle[MAPPING_VM_STATE_KEY] = state.value
    }

    private fun postLocation(){
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                mappingUseCase.createUserUseCase(
                    user = User(
                        id = getId(),
                        location = Location(
                            latitude = state.value.latitude,
                            longitude = state.value.longitude,
                        ), profilePictureUrl = state.value.photoUrl))
            }.onSuccess {
                Timber.v("Successfully posted location")
            }.onFailure {
                Timber.v("Failed to post location")
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
                mappingUseCase.updateUserUseCase(
                    itemId = getId(),
                    user = User(
                        userAssistance = UserAssistance(
                            needHelp = false,
                            confirmationDetail = ConfirmationDetail()))
                )

            }.onSuccess {
                _state.update {
                    it.copy(
                        isSearchingForAssistance = false,
                        isLoading = false,
                        findAssistanceButtonVisible = true)
                }
            }.onFailure { exception ->
                _state.update { it.copy(isLoading = false) }
                handleException(exception)
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
                Timber.v("GET USER DRAWABLE IMAGE: ${it.message}")
            }
        }.invokeOnCompletion {
            savedStateHandle[MAPPING_VM_STATE_KEY] = state.value
        }
    }


    private fun getNearbyUsers() {
        getUsersJob?.cancel()
        getUsersJob = viewModelScope.launch(Dispatchers.IO + SupervisorJob()) {
            while (this.isActive) {
                runCatching {
                    mappingUseCase.getUsersUseCase().distinctUntilChanged()
                        .collect { users: List<User> ->
                            users.getUser()
                            users.getUsers()
                            users.getUserRescueRespondents()
                            savedStateHandle[MAPPING_VM_STATE_KEY] = state.value
                        }
                }.onFailure {
                    Timber.e("ERROR GETTING USERS: ${it.message}")
                }
                delay(INTERVAL_UPDATE_USERS)
                savedStateHandle[MAPPING_VM_STATE_KEY] = state.value
            }
        }
    }

    private fun List<User>.getUser() {

        val user = this.find {
            // TODO: Change id later
            it.id == "1"
        } ?: User()

        _state.update { it.copy(user = user) }

    }

    private fun List<User>.getUserRescueRespondents() {
        val user = state.value.user
        val rescueRespondentsSnapShot: MutableList<CardModel> = mutableListOf()
        user.rescueRequest?.respondents?.forEachIndexed { index, respondent ->
            this.find { usersOnMap ->
                respondent.clientId == usersOnMap.id
            }?.let { user ->
                rescueRespondentsSnapShot.add(index = index, element = user.toCardModel())
            }
        }
        _state.update {
            it.copy(
                rescueRequestRespondents = RescueRequestRespondents(
                    respondents = rescueRespondentsSnapShot.toSet().toList().toImmutableList()))
        }
        rescueRespondentsSnapShot.clear()
    }

    private suspend fun List<User>.getUsers() {
        val nearbyCyclistSnapShot: MutableList<Cyclist> = mutableListOf()
        this.forEachIndexed { index, user ->
            val bitmapProfile = mappingUseCase.imageUrlToDrawableUseCase(
                user.profilePictureUrl ?: IMAGE_PLACEHOLDER_URL)
            nearbyCyclistSnapShot.add(
                index = index,
                element = Cyclist(
                    user,
                    bitmapProfile.toBitmap(
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
        locationUpdatesFlow?.cancel()
        locationUpdatesFlow = viewModelScope.launch(Dispatchers.IO) {
            runCatching {

                mappingUseCase.getUserLocationUseCase().collect { location ->
                    withContext(Dispatchers.Default) {
                        geocoder.getAddress(location.latitude, location.longitude) { addresses ->
                            _state.update { it.copy(userAddress = UserAddress(addresses.lastOrNull())) }
                        }
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
        locationUpdatesFlow?.cancel()
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
                runCatching {
                    _state.update { it.copy(isLoading = true) }
                    createUser(address)
                }.onSuccess {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            findAssistanceButtonVisible = false)
                    }
                    _eventFlow.emit(MappingUiEvent.ShowConfirmDetailsScreen)
                }.onFailure { exception ->
                    _state.update { it.copy(isLoading = false) }
                    handleException(exception)
                }
                return@launch
            }
            _eventFlow.emit(MappingUiEvent.ShowToastMessage(message = "Searching for GPS"))

        }.invokeOnCompletion {
            savedStateHandle[MAPPING_VM_STATE_KEY] = state.value
        }

    }

    private suspend inline fun handleException(exception: Throwable) {
        when (exception) {

            is MappingExceptions.NetworkExceptions -> {
                _state.update { it.copy(hasInternet = false) }
            }

            is MappingExceptions.UnexpectedErrorException, is MappingExceptions.UserException -> {
                _eventFlow.emit(
                    MappingUiEvent.ShowToastMessage(
                        message = exception.message ?: "Unexpected error occurred."
                    ))
            }
            is MappingExceptions.PhoneNumberException, is MappingExceptions.NameException -> {
                _eventFlow.emit(MappingUiEvent.ShowEditProfileScreen)
            }

        }
        savedStateHandle[MAPPING_VM_STATE_KEY] = state.value
    }

    private suspend fun createUser(address: Address) {
        with(address) {
            val currentAddress = this.getFullAddress()
            _state.update { it.copy(currentAddress = currentAddress) }
            mappingUseCase.updateUserUseCase(
                itemId = getId(),
                user = User(
                    name = getName(),
                    address = currentAddress,
                    profilePictureUrl = getPhotoUrl(),
                    contactNumber = getPhoneNumber(),
                    location = Location(
                        latitude = latitude,
                        longitude = longitude),
                ))

            mappingUseCase.updateAddressUseCase(currentAddress)
        }
    }

    private fun createMockUpUsers() {
        viewModelScope.launch {
            runCatching {
                mappingUseCase.createMockUsers()
            }.onSuccess {
                Timber.v("CREATED MOCK USERS!")
            }.onFailure {
                Timber.e("FAILED TO CREATE MOCK USERS: ${it.message}")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        onEvent(event = MappingEvent.UnsubscribeToLocationUpdates)
        onEvent(event = MappingEvent.StopPinging)
    }


    private fun getId(): String = authUseCase.getIdUseCase()

    private fun getName(): String = authUseCase.getNameUseCase()

    private suspend fun getPhoneNumber(): String =
        authUseCase.getPhoneNumberUseCase()

    private fun getPhotoUrl() = authUseCase.getPhotoUrlUseCase()


}
