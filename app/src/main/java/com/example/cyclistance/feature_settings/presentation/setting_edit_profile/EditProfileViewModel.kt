package com.example.cyclistance.feature_settings.presentation.setting_edit_profile

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.core.utils.constants.SettingConstants.EDIT_PROFILE_VM_IMAGE_URI_KEY
import com.example.cyclistance.core.utils.constants.SettingConstants.EDIT_PROFILE_VM_STATE_KEY
import com.example.cyclistance.feature_authentication.domain.exceptions.AuthExceptions
import com.example.cyclistance.feature_authentication.domain.use_case.AuthenticationUseCase
import com.example.cyclistance.feature_mapping_screen.domain.exceptions.MappingExceptions
import com.example.cyclistance.feature_settings.domain.use_case.SettingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val settingUseCase: SettingUseCase,
    private val authUseCase: AuthenticationUseCase) : ViewModel() {

    private val _state = MutableStateFlow(savedStateHandle[EDIT_PROFILE_VM_STATE_KEY] ?: EditProfileState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<EditProfileUiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var imageUri: Uri? = savedStateHandle[EDIT_PROFILE_VM_IMAGE_URI_KEY]


    fun onEvent(event: EditProfileEvent) {

        when (event) {
            is EditProfileEvent.Save -> {
                updateUserProfile()
            }
            is EditProfileEvent.OnClickGalleryButton -> {
                _state.update { it.copy(galleryButtonClick = !state.value.galleryButtonClick) }
            }

            is EditProfileEvent.EnterPhoneNumber -> {
                _state.update { it.copy(phoneNumber = event.phoneNumber, phoneNumberErrorMessage = "") }

            }
            is EditProfileEvent.EnterName -> {
                _state.update { it.copy(name = event.name, nameErrorMessage = "") }
            }
            is EditProfileEvent.SelectImageUri -> {
                imageUri = event.uri
                savedStateHandle[EDIT_PROFILE_VM_IMAGE_URI_KEY] = event.uri
            }
            is EditProfileEvent.SelectBitmapPicture -> {
                _state.update { it.copy(imageBitmap = ImageBitmap(event.bitmap)) }
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
        savedStateHandle[EDIT_PROFILE_VM_STATE_KEY] = state.value


    }

    private fun saveImageToGallery() {
        viewModelScope.launch(context = Dispatchers.IO) {
            state.value.imageBitmap.bitmap?.let { bitmap ->
                runCatching {
                    settingUseCase.saveImageToGalleryUseCase(bitmap)
                }.onSuccess { uri ->
                    imageUri = uri
                    savedStateHandle[EDIT_PROFILE_VM_IMAGE_URI_KEY] = uri
                }.onFailure {
                    Timber.e("Saving Image to Gallery: ${it.message}")
                }
            }
        }

    }

    private fun loadPhoto() {
        viewModelScope.launch {
            runCatching {
                _state.update {
                    it.copy(
                        photoUrl = getPhotoUrl(),
                        isLoading = true)
                }
            }.onSuccess {
                _state.update {
                    it.copy(
                        isLoading = false
                    )
                }
            }
        }
        savedStateHandle[EDIT_PROFILE_VM_STATE_KEY] = state.value
    }

    private fun loadName() {
        viewModelScope.launch {
            runCatching {
                _state.update {
                    val name = getName()
                    it.copy(
                        name = name,
                        nameSnapshot = name,
                        isLoading = true
                    )
                }
            }.onSuccess {
                _state.update { it.copy(isLoading = false) }
            }.onFailure { exception ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        nameErrorMessage = exception.message!!)
                }

            }
        }
        savedStateHandle[EDIT_PROFILE_VM_STATE_KEY] = state.value
    }

    private fun loadPhoneNumber() {
        viewModelScope.launch {
            runCatching {
                _state.update {
                    val phoneNumber = getPhoneNumber()
                    it.copy(
                        phoneNumber = phoneNumber,
                        phoneNumberSnapshot = phoneNumber,
                        isLoading = true)
                }

            }.onSuccess {
                _state.update { it.copy(isLoading = false) }
            }.onFailure { exception ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        phoneNumberErrorMessage = exception.message!!)
                }
            }
        }

        savedStateHandle[EDIT_PROFILE_VM_STATE_KEY] = state.value
    }

    private fun updateUserProfile() {
        viewModelScope.launch {
            runCatching {
                _state.update { it.copy(isLoading = true) }
                val phoneNumberChanges = with(state.value) {
                    phoneNumber != phoneNumberSnapshot
                }

                if (phoneNumberChanges) {
                    authUseCase.updatePhoneNumberUseCase(state.value.phoneNumber.trim())
                }

                authUseCase.updateProfileUseCase(
                    photoUri = run {
                        imageUri?.let { localImageUri ->
                            authUseCase.uploadImageUseCase(localImageUri)
                        } },
                    name = state.value.name.trim())

            }.onSuccess {
                _state.update { it.copy(isLoading = false) }
                _eventFlow.emit(EditProfileUiEvent.ShowToastMessage(message = "Successfully Updated!"))
                _eventFlow.emit(EditProfileUiEvent.CloseScreen)
            }.onFailure { exception ->
                _state.update { it.copy(isLoading = false) }
                when (exception) {
                    is MappingExceptions.PhoneNumberException -> {
                        _state.update {
                            it.copy(
                                phoneNumberErrorMessage = exception.message!!)
                        }
                    }
                    is MappingExceptions.NameException -> {
                        _state.update {
                            it.copy(
                                nameErrorMessage = exception.message!!)
                        }
                    }
                    is AuthExceptions.NetworkException -> {
                        _eventFlow.emit(
                            value = EditProfileUiEvent.ShowToastMessage(
                                message = exception.message ?:"No Internet Connection."))
                    }
                }

            }
        }
        savedStateHandle[EDIT_PROFILE_VM_STATE_KEY] = state.value
    }




    private fun getName() = authUseCase.getNameUseCase()
    private fun getPhotoUrl() = authUseCase.getPhotoUrlUseCase()
    private suspend fun getPhoneNumber() = authUseCase.getPhoneNumberUseCase()

}