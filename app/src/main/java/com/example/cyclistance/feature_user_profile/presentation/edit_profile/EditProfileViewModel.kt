package com.example.cyclistance.feature_user_profile.presentation.edit_profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.core.utils.constants.SettingConstants.EDIT_PROFILE_VM_STATE_KEY
import com.example.cyclistance.feature_user_profile.domain.exceptions.UserProfileExceptions
import com.example.cyclistance.feature_user_profile.domain.use_case.UserProfileUseCase
import com.example.cyclistance.feature_user_profile.presentation.edit_profile.event.EditProfileEvent
import com.example.cyclistance.feature_user_profile.presentation.edit_profile.event.EditProfileVmEvent
import com.example.cyclistance.feature_user_profile.presentation.edit_profile.state.EditProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
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
    private val userProfileUseCase: UserProfileUseCase
) : ViewModel() {

    private val _state =
        MutableStateFlow(savedStateHandle[EDIT_PROFILE_VM_STATE_KEY] ?: EditProfileState())
    val state = _state.asStateFlow()

    private val _eventFlow =
        MutableSharedFlow<EditProfileEvent>(replay = 5, extraBufferCapacity = 5)
    val eventFlow = _eventFlow.asSharedFlow()


    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch(context = defaultDispatcher) {
            loadName()
            loadPhoto()
        }
    }

    fun onEvent(event: EditProfileVmEvent) {

        when (event) {

            is EditProfileVmEvent.Save -> {
                updateUserProfile(
                    imageUri = event.imageUri,
                    name = event.name)
            }

            is EditProfileVmEvent.LoadProfile -> {
                loadProfile()
            }
        }

        savedStateHandle[EDIT_PROFILE_VM_STATE_KEY] = state.value
    }

    private suspend fun loadPhoto() {
        runCatching {
            startLoading()
            getPhotoUrl()
        }.onSuccess { photoUrl ->
            _eventFlow.emit(value = EditProfileEvent.GetPhotoUrlSuccess(photoUrl))
            finishLoading()
        }.onFailure {
            Timber.e(it.message)
            finishLoading()
        }
        savedStateHandle[EDIT_PROFILE_VM_STATE_KEY] = state.value
    }

    private suspend fun loadName() {
        runCatching {
            startLoading()
            getName()
        }.onSuccess { name ->
            _eventFlow.emit(value = EditProfileEvent.GetNameSuccess(name))
            _state.update { it.copy(nameSnapshot = name) }
            finishLoading()
        }.onFailure { exception ->
            finishLoading()
            _eventFlow.emit(value = EditProfileEvent.GetNameFailed(exception.message!!))
        }
        savedStateHandle[EDIT_PROFILE_VM_STATE_KEY] = state.value
    }


    private fun updateUserProfile(imageUri: String, name: String) {
        viewModelScope.launch(context = defaultDispatcher) {
            runCatching {
                startLoading()
                val uri = imageUri.takeIf { it.isNotEmpty() }
                val photoUri: String? = uri?.let { userProfileUseCase.uploadImageUseCase(it) }

                userProfileUseCase.updateProfileUseCase(
                    photoUri = photoUri,
                    name = name.trim())

            }.onSuccess {
                finishLoading()
                _eventFlow.emit(EditProfileEvent.UpdateUserProfileSuccess(reason = "Successfully Updated!"))
            }.onFailure { exception ->
                finishLoading()
                when (exception) {

                    is UserProfileExceptions.NameException -> {
                        _eventFlow.emit(value = EditProfileEvent.GetNameFailed(reason = exception.message!!))
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


    private suspend fun getName() = userProfileUseCase.getNameUseCase()
    private suspend fun getPhotoUrl() = userProfileUseCase.getPhotoUrlUseCase()

}