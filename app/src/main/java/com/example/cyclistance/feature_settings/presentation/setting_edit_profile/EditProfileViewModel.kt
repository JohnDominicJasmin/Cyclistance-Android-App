package com.example.cyclistance.feature_settings.presentation.setting_edit_profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.core.utils.constants.MappingConstants.IMAGE_PLACEHOLDER_URL
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
    private val settingUseCase: SettingUseCase,
    private val authUseCase: AuthenticationUseCase) : ViewModel() {

    private val _state = MutableStateFlow(EditProfileState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<EditProfileUiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var imageUri: Uri? = null

    private fun getName(): String {
        return authUseCase.getNameUseCase().takeIf { !it.isNullOrEmpty() }
               ?: throw MappingExceptions.NameException()

    }

    private fun getPhotoUrl(): String {
        return authUseCase.getPhotoUrlUseCase()
               ?: IMAGE_PLACEHOLDER_URL
    }

    private suspend fun getPhoneNumber(): String =
        authUseCase.getPhoneNumberUseCase().ifEmpty { throw MappingExceptions.PhoneNumberException() }


    fun onEvent(event: EditProfileEvent) {

        when (event) {
            is EditProfileEvent.Save -> {
                updateUserProfile()
            }
            is EditProfileEvent.EnterPhoneNumber -> {
                _state.update {
                    it.copy(
                        phoneNumber = event.phoneNumber,
                        phoneNumberErrorMessage = "")
                }

            }
            is EditProfileEvent.EnterName -> {
                _state.update { it.copy(name = event.name, nameErrorMessage = "") }
            }
            is EditProfileEvent.SelectImageUri -> {
                imageUri = event.uri
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
    }

    private fun saveImageToGallery() {
        viewModelScope.launch(context = Dispatchers.IO) {
            state.value.imageBitmap.bitmap?.let { bitmap ->
                runCatching {
                    settingUseCase.saveImageToGalleryUseCase(bitmap)
                }.onSuccess { uri ->
                    imageUri = uri
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
                _eventFlow.emit(EditProfileUiEvent.ShowMappingScreen)
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
    }
}