package com.example.cyclistance.feature_settings.presentation.setting_edit_profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.core.utils.MappingConstants.IMAGE_PLACEHOLDER_URL
import com.example.cyclistance.feature_authentication.domain.use_case.AuthenticationUseCase
import com.example.cyclistance.feature_main_screen.domain.exceptions.MappingExceptions
import com.example.cyclistance.feature_settings.domain.use_case.SettingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val settingUseCase: SettingUseCase,
    private val authUseCase: AuthenticationUseCase) : ViewModel() {

    private val _state = mutableStateOf(EditProfileState())
    val state by _state

    private val _eventFlow = MutableSharedFlow<EditProfileUiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


    private fun getName(): String {
        return authUseCase.getNameUseCase().takeIf { !it.isNullOrEmpty() }
               ?: throw MappingExceptions.NameException()

    }

    private fun getPhotoUrl(): String {
        return authUseCase.getPhotoUrlUseCase()?.toString()
               ?: IMAGE_PLACEHOLDER_URL
    }

    private suspend fun getPhoneNumber(): String = authUseCase.getPhoneNumberUseCase() ?: ""


    init {
        onEvent(event = EditProfileEvent.LoadName) //todo: fix this not working
        onEvent(event = EditProfileEvent.LoadPhoto)
        onEvent(event = EditProfileEvent.LoadPhoneNumber)
    }


    fun onEvent(event: EditProfileEvent) {

        when (event) {
            is EditProfileEvent.Save -> {
                updateUserProfile()
            }
            is EditProfileEvent.EnteredPhoneNumber -> {
                _state.value = state.copy(
                    phoneNumber = event.phoneNumber,
                    phoneNumberErrorMessage = "")
            }
            is EditProfileEvent.EnteredName -> {
                _state.value = state.copy(name = event.name, nameErrorMessage = "")
            }
            is EditProfileEvent.SelectImageUri -> {
                _state.value = state.copy(imageUri = event.uri)
            }
            is EditProfileEvent.SelectBitmapPicture -> {
                _state.value = state.copy(bitmap = event.bitmap)
            }
            is EditProfileEvent.LoadPhoto -> {
                loadPhoto()
            }

            is EditProfileEvent.LoadPhoneNumber -> {
                loadPhoneNumber()
            }
            is EditProfileEvent.LoadName -> {
                loadName()
            }
            is EditProfileEvent.SaveImageToGallery -> {
                saveImageToGallery()
            }

        }
    }

    private fun saveImageToGallery() {
        viewModelScope.launch(context = Dispatchers.IO) {
            state.bitmap?.let { bitmap ->
                runCatching {
                    settingUseCase.saveImageToGalleryUseCase(bitmap)
                }.onSuccess { uri ->
                    _state.value = state.copy(imageUri = uri)
                }.onFailure {
                    Timber.e("Saving Image to Gallery: ${it.message}")
                }
            }
        }
    }

    private fun loadPhoto() {
        viewModelScope.launch {
            runCatching {
                _state.value = state.copy(
                    photoUrl = getPhotoUrl(),
                    isLoading = true
                )
            }.onSuccess {
                _state.value = state.copy(isLoading = false)
            }
        }
    }

    private fun loadName() {
        viewModelScope.launch {
            runCatching {
                _state.value = state.copy(
                    name = TextFieldValue(text = getName()),
                    isLoading = true
                )
            }.onSuccess {
                _state.value = state.copy(isLoading = false)
            }.onFailure { exception ->
                _state.value = state.copy(
                    isLoading = false,
                    nameErrorMessage = exception.message!!
                )
            }
        }
    }

    private fun loadPhoneNumber() {
        viewModelScope.launch {
            runCatching {
                _state.value = state.copy(
                    phoneNumber = TextFieldValue(text = getPhoneNumber()),
                    isLoading = true
                )
            }.onSuccess {
                _state.value = state.copy(isLoading = false)
            }.onFailure {
                _state.value = state.copy(
                    isLoading = false,
                    phoneNumberErrorMessage = "Field cannot be blank."
                )
            }
        }
    }

    private fun updateUserProfile() {
        viewModelScope.launch {
            runCatching {
                _state.value = state.copy(isLoading = true)
                authUseCase.updateProfileUseCase(
                    photoUri = run {
                        state.imageUri?.let { localImageUri ->
                            authUseCase.uploadImageUseCase(localImageUri)
                        }
                    },
                    name = state.name.text)

                authUseCase.updatePhoneNumberUseCase(state.phoneNumber.text)
            }.onSuccess {
                _state.value = state.copy(isLoading = false)
                _eventFlow.emit(EditProfileUiEvent.ShowToastMessage(message = "Successfully Updated!"))
                _eventFlow.emit(EditProfileUiEvent.ShowMappingScreen)
            }.onFailure { exception ->
                _state.value = state.copy(isLoading = false)
                when (exception) {
                    is MappingExceptions.PhoneNumberException -> {
                        _state.value = state.copy(phoneNumberErrorMessage = exception.message!!)
                    }
                    is MappingExceptions.NameException -> {
                        _state.value = state.copy(nameErrorMessage = exception.message!!)
                    }
                    else -> {
                        Timber.e("Update Profile: ${exception.message}")
                    }
                }

            }
        }
    }
}