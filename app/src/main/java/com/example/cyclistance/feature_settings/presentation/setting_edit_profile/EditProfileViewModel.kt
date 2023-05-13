package com.example.cyclistance.feature_settings.presentation.setting_edit_profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.core.utils.constants.SettingConstants.EDIT_PROFILE_VM_STATE_KEY
import com.example.cyclistance.feature_authentication.domain.exceptions.AuthExceptions
import com.example.cyclistance.feature_authentication.domain.use_case.AuthenticationUseCase
import com.example.cyclistance.feature_mapping.domain.exceptions.MappingExceptions
import com.example.cyclistance.feature_settings.presentation.setting_edit_profile.event.EditProfileVmEvent
import com.example.cyclistance.feature_settings.presentation.setting_edit_profile.event.EditProfileEvent
import com.example.cyclistance.feature_settings.presentation.setting_edit_profile.state.EditProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val authUseCase: AuthenticationUseCase,
    private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _state =
        MutableStateFlow(savedStateHandle[EDIT_PROFILE_VM_STATE_KEY] ?: EditProfileState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<EditProfileEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


    init {
        loadName()
        loadPhoneNumber()
        loadPhoto()
    }

    fun onEvent(event: EditProfileVmEvent) {

        when (event) {
            is EditProfileVmEvent.Save -> {
                updateUserProfile(
                    imageUri = event.imageUri,
                    phoneNumber = event.phoneNumber,
                    name = event.name)
            }
        }
        savedStateHandle[EDIT_PROFILE_VM_STATE_KEY] = state.value
    }


    private fun loadPhoto() {
        viewModelScope.launch(context = defaultDispatcher) {
            runCatching {
                startLoading()
                getPhotoUrl()
            }.onSuccess { photoUrl ->
                _eventFlow.emit(value = EditProfileEvent.GetPhotoUrlSuccess(photoUrl))
                finishLoading()
            }.onFailure {
                finishLoading()
            }
        }
        savedStateHandle[EDIT_PROFILE_VM_STATE_KEY] = state.value
    }

    private fun loadName() {
        viewModelScope.launch(context = defaultDispatcher) {
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
        }
        savedStateHandle[EDIT_PROFILE_VM_STATE_KEY] = state.value
    }

    private fun loadPhoneNumber() {
        viewModelScope.launch(context = defaultDispatcher) {
            runCatching {
                startLoading()
                getPhoneNumber()
            }.onSuccess { phoneNumber ->
                finishLoading()
                _eventFlow.emit(value = EditProfileEvent.GetPhoneNumberSuccess(phoneNumber))
                _state.update { it.copy(phoneNumberSnapshot = phoneNumber) }
            }.onFailure { exception ->
                finishLoading()
                _eventFlow.emit(value = EditProfileEvent.GetPhoneNumberFailed(reason = exception.message!!))
            }
        }

        savedStateHandle[EDIT_PROFILE_VM_STATE_KEY] = state.value
    }

    private fun updateUserProfile(imageUri: String, phoneNumber: String, name: String) {
        viewModelScope.launch(context = defaultDispatcher) {
            runCatching {
                startLoading()
                val photoUri = imageUri?.let { authUseCase.uploadImageUseCase(it) }
                val phoneNumberChanges = phoneNumber != state.value.phoneNumberSnapshot


                if (phoneNumberChanges) {
                    authUseCase.updatePhoneNumberUseCase(phoneNumber.trim())
                }

                authUseCase.updateProfileUseCase(
                    photoUri = photoUri,
                    name = name.trim())

            }.onSuccess {
                finishLoading()
                _eventFlow.emit(EditProfileEvent.UpdateUserProfileSuccess(reason = "Successfully Updated!"))
            }.onFailure { exception ->
                finishLoading()
                when (exception) {
                    is MappingExceptions.PhoneNumberException -> {
                        _eventFlow.emit(value = EditProfileEvent.GetPhoneNumberFailed(reason = exception.message!!))
                    }

                    is MappingExceptions.NameException -> {
                        _eventFlow.emit(value = EditProfileEvent.GetNameFailed(reason = exception.message!!))
                    }

                    is AuthExceptions.NetworkException -> {
                        _eventFlow.emit(value = EditProfileEvent.NoInternetConnection)
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


    private fun getName() = authUseCase.getNameUseCase()
    private fun getPhotoUrl() = authUseCase.getPhotoUrlUseCase()
    private suspend fun getPhoneNumber() = authUseCase.getPhoneNumberUseCase()

}