package com.example.cyclistance.feature_user_profile.presentation.edit_profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.core.utils.constants.UserProfileConstants.EDIT_PROFILE_VM_STATE_KEY
import com.example.cyclistance.feature_authentication.domain.use_case.AuthenticationUseCase
import com.example.cyclistance.feature_user_profile.domain.exceptions.UserProfileExceptions
import com.example.cyclistance.feature_user_profile.domain.model.UserProfileInfoModel
import com.example.cyclistance.feature_user_profile.domain.use_case.UserProfileUseCase
import com.example.cyclistance.feature_user_profile.presentation.edit_profile.event.EditProfileEvent
import com.example.cyclistance.feature_user_profile.presentation.edit_profile.event.EditProfileVmEvent
import com.example.cyclistance.feature_user_profile.presentation.edit_profile.state.EditProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val defaultDispatcher: CoroutineDispatcher,
    private val userProfileUseCase: UserProfileUseCase,
    private val authUseCase: AuthenticationUseCase
) : ViewModel() {

    private val _state =
        MutableStateFlow(savedStateHandle[EDIT_PROFILE_VM_STATE_KEY] ?: EditProfileState())
    val state = _state.asStateFlow()

    private val _eventFlow =
        MutableSharedFlow<EditProfileEvent>(replay = 5, extraBufferCapacity = 5)
    val eventFlow = _eventFlow.asSharedFlow()


    private fun loadProfile() {
        viewModelScope.launch(context = defaultDispatcher + SupervisorJob()) {
            startLoading()
            loadName()
            loadPhoto()
            loadUserId()
            finishLoading()
        }
    }

    fun onEvent(event: EditProfileVmEvent) {

        when (event) {
            is EditProfileVmEvent.Save -> updateUserProfile(event.userProfile)
            is EditProfileVmEvent.LoadProfile -> loadProfile()
            is EditProfileVmEvent.LoadUserProfileInfo -> loadUserProfileInfo()
        }

        savedStateHandle[EDIT_PROFILE_VM_STATE_KEY] = state.value
    }

    private fun loadUserProfileInfo() {
        viewModelScope.launch(context = defaultDispatcher) {
            runCatching {
                userProfileUseCase.getUserProfileInfoUseCase(id = getId())
            }.onSuccess { profile ->

                val cyclingGroup = profile.getBikeGroup() ?: ""
                val address = profile.getAddress() ?: ""

                _eventFlow.emit(
                    value = EditProfileEvent.GetBikeGroupSuccess(
                        cyclingGroup = cyclingGroup))

                _eventFlow.emit(
                    value = EditProfileEvent.GetAddressSuccess(
                        address = address))

                _state.update {
                    it.copy(
                        cyclingGroupSnapshot = cyclingGroup,
                        addressSnapshot = address)
                }
            }.onFailure {
                Timber.v("Error ${it.message}")
            }
        }
    }

    private fun loadUserId(){
        runCatching {
            getId()
        }.onSuccess { id ->
            _state.update { it.copy(userId = id) }
        }.onFailure {
            Timber.e("Load user id ${it.message}")
        }
    }
    private suspend fun loadPhoto() {
        runCatching {
            getPhotoUrl()
        }.onSuccess { photoUrl ->
            _eventFlow.emit(value = EditProfileEvent.GetPhotoUrlSuccess(photoUrl))
        }.onFailure {
            Timber.e(it.message)
        }
        savedStateHandle[EDIT_PROFILE_VM_STATE_KEY] = state.value
    }

    private suspend fun loadName() {
        runCatching {
            getName()
        }.onSuccess { name ->
            _eventFlow.emit(value = EditProfileEvent.GetNameSuccess(name))
            _state.update { it.copy(nameSnapshot = name) }
        }.onFailure { exception ->
            _eventFlow.emit(value = EditProfileEvent.NameInputFailed(exception.message!!))
        }
        savedStateHandle[EDIT_PROFILE_VM_STATE_KEY] = state.value
    }


    private fun updateUserProfile(useProfile: UserProfileInfoModel) {
        viewModelScope.launch(context = defaultDispatcher) {
            runCatching {
                startLoading()
                val uri = useProfile.photoUrl.takeIf { it.isNotEmpty() }
                val photoUri: String? = uri?.let { userProfileUseCase.uploadImageUseCase(it) }

                userProfileUseCase.updateAuthProfileUseCase(
                    photoUri = photoUri,
                    name = useProfile.name.trim())

                userProfileUseCase.updateProfileInfoUseCase(
                    id = getId(),
                    userProfile = useProfile.copy(photoUrl = photoUri ?: getPhotoUrl())
                )

            }.onSuccess {
                finishLoading()
                _eventFlow.emit(EditProfileEvent.UpdateUserProfileSuccess)
            }.onFailure { exception ->
                finishLoading()
                when (exception) {

                    is UserProfileExceptions.UpdateProfileException -> {
                        _eventFlow.emit(value = EditProfileEvent.UpdateUserProfileFailed(reason = exception.message!!))
                    }

                    is UserProfileExceptions.NameException -> {
                        _eventFlow.emit(value = EditProfileEvent.NameInputFailed(reason = exception.message!!))
                    }

                    is UserProfileExceptions.AddressException -> {
                        _eventFlow.emit(value = EditProfileEvent.AddressInputFailed(reason = exception.message!!))
                    }

                    is UserProfileExceptions.NetworkException -> {
                        _eventFlow.emit(value = EditProfileEvent.NoInternetConnection)
                    }

                    is UserProfileExceptions.InternalServerException -> {
                        _eventFlow.emit(value = EditProfileEvent.InternalServerError(reason = exception.message!!))
                    }
                }
            }
        }
        savedStateHandle[EDIT_PROFILE_VM_STATE_KEY] = state.value
    }


    private fun finishLoading() {
        _state.update { it.copy(isLoading = false) }
    }

    private fun startLoading() {
        _state.update { it.copy(isLoading = true) }
    }


    private fun getId() = authUseCase.getIdUseCase()
    private suspend fun getName() = userProfileUseCase.getNameUseCase()
    private suspend fun getPhotoUrl() = userProfileUseCase.getPhotoUrlUseCase()

}