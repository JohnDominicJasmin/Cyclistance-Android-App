package com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen

import android.location.Address
import android.location.Geocoder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.core.utils.constants.MappingConstants.DEFAULT_LATITUDE
import com.example.cyclistance.core.utils.constants.MappingConstants.IMAGE_PLACEHOLDER_URL
import com.example.cyclistance.core.utils.constants.MappingConstants.INTERVAL_UPDATE_USERS
import com.example.cyclistance.feature_authentication.domain.use_case.AuthenticationUseCase
import com.example.cyclistance.feature_mapping_screen.data.remote.dto.Location
import com.example.cyclistance.feature_mapping_screen.data.remote.dto.Status
import com.example.cyclistance.feature_mapping_screen.data.remote.dto.UserAssistance
import com.example.cyclistance.feature_mapping_screen.domain.exceptions.MappingExceptions
import com.example.cyclistance.feature_mapping_screen.domain.model.User
import com.example.cyclistance.feature_mapping_screen.domain.use_case.MappingUseCase
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.utils.getAddress
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.utils.getFullAddress
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MappingViewModel @Inject constructor(
    private val authUseCase: AuthenticationUseCase,
    private val geocoder: Geocoder,
    private val mappingUseCase: MappingUseCase) : ViewModel() {

    private var getUsersJob: Job? = null
    private var locationUpdatesFlow: Job? = null

    private val _state: MutableStateFlow<MappingState> = MutableStateFlow(MappingState())
    val state = _state.asStateFlow()

    private val _eventFlow: MutableSharedFlow<MappingUiEvent> = MutableSharedFlow()
    val eventFlow: SharedFlow<MappingUiEvent> = _eventFlow.asSharedFlow()

    private var address: List<Address> = emptyList()


    fun onEvent(event: MappingEvent) {
        when (event) {

            is MappingEvent.UploadProfile -> {
                uploadUserProfile()
            }
            is MappingEvent.GetUsers -> {
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
    }

    private fun getUserDrawableImage(){
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                mappingUseCase.imageUrlToDrawableUseCase(getPhotoUrl())
            }.onSuccess { drawableImage ->
                _state.update { it.copy(drawableImages = DrawableImages(userDrawableImage = drawableImage)) }
            }.onFailure {
                Timber.v("GET USER DRAWABLE IMAGE: ${it.message}")
            }
        }
    }


    private fun getNearbyUsers() {
        getUsersJob?.cancel()
        getUsersJob = viewModelScope.launch(Dispatchers.IO) {
            while (this.isActive) {
                runCatching {
                    mappingUseCase.getUsersUseCase().collect { users ->
                        _state.update { it.copy(nearbyCyclists = NearbyCyclists(activeUsers = users)) }
                    }
                }.onFailure {
                    Timber.e("ERROR GETTING USERS: ${it.message}")
                }
                delay(INTERVAL_UPDATE_USERS)
            }
        }
    }

    private fun subscribeToLocationUpdates() {
        locationUpdatesFlow?.cancel()
        locationUpdatesFlow = viewModelScope.launch(Dispatchers.IO) {
            runCatching {

                mappingUseCase.getUserLocationUseCase().collect { location ->
                    withContext(Dispatchers.Default){
                        geocoder.getAddress(location.latitude, location.longitude){ addresses ->
                            address = addresses
                        }
                    }

                    _state.update { state ->
                        state.copy(
                            latitude = location.latitude.takeIf { it != 0.0 } ?: DEFAULT_LATITUDE,
                            longitude = location.longitude.takeIf { it != 0.0 } ?: DEFAULT_LATITUDE,
                        )
                    }
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
        }
    }

    private fun uploadUserProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            if (address.isNotEmpty()) {

                address.forEach { address ->
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
                }
                return@launch
            }
            _eventFlow.emit(MappingUiEvent.ShowToastMessage(message = "Searching for GPS"))

        }

    }

    private suspend inline fun handleException(exception: Throwable) {
        when (exception) {
            is MappingExceptions.NoInternetException -> {
                _state.update { it.copy(hasInternet = false) }
            }
            is MappingExceptions.UnexpectedErrorException -> {
                _eventFlow.emit(
                    MappingUiEvent.ShowToastMessage(
                        message = exception.message ?: "",
                    ))
            }
            is MappingExceptions.PhoneNumberException, is MappingExceptions.NameException -> {
                _eventFlow.emit(MappingUiEvent.ShowEditProfileScreen)
            }

        }
    }


    private suspend inline fun createUser(address: Address) {
        with(address) {
            val currentAddress = this.getFullAddress()
            _state.update { it.copy(currentAddress = currentAddress) }
            mappingUseCase.createUserUseCase(
                user = User(
                    id = getId() ?: return,
                    name = getName(),
                    address = currentAddress,
                    profilePictureUrl = getPhotoUrl(),
                    contactNumber = getPhoneNumber(),
                    location = Location(
                        lat = latitude.toString(),
                        lng = longitude.toString()),
                    userAssistance = UserAssistance(
                        status = Status(started = true)
                    ),
                    userNeededHelp = false,
                ))

            mappingUseCase.updateAddressUseCase(currentAddress)
        }
    }

    override fun onCleared() {
        super.onCleared()
        onEvent(event = MappingEvent.UnsubscribeToLocationUpdates)
        onEvent(event = MappingEvent.StopPinging)
    }



    private fun getId(): String? = authUseCase.getIdUseCase()

    private fun getName(): String = authUseCase.getNameUseCase().takeIf { !it.isNullOrEmpty() }
                                    ?: throw MappingExceptions.NameException()

    private suspend fun getPhoneNumber(): String =
        authUseCase.getPhoneNumberUseCase().takeIf { !it.isNullOrEmpty() }
        ?: throw MappingExceptions.PhoneNumberException()

    private fun getPhotoUrl(): String {
        return authUseCase.getPhotoUrlUseCase() ?: IMAGE_PLACEHOLDER_URL
    }





}

