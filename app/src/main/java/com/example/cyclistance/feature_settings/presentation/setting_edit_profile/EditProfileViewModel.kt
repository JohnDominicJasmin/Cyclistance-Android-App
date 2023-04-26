package com.example.cyclistance.feature_settings.presentation.setting_edit_profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.core.utils.constants.SettingConstants.EDIT_PROFILE_VM_STATE_KEY
import com.example.cyclistance.feature_authentication.domain.exceptions.AuthExceptions
import com.example.cyclistance.feature_authentication.domain.use_case.AuthenticationUseCase
import com.example.cyclistance.feature_mapping.domain.exceptions.MappingExceptions
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

    private val _state = MutableStateFlow(savedStateHandle[EDIT_PROFILE_VM_STATE_KEY] ?: EditProfileState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<EditProfileUiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()



    fun onEvent(event: EditProfileEvent) {

        when (event) {
            is EditProfileEvent.Save -> {
                updateUserProfile()
            }
            is EditProfileEvent.DismissNoInternetDialog -> {
                _state.update { it.copy(hasInternet = true) }
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
                _state.update { it.copy(imageUri = event.uri) }
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
        }
        savedStateHandle[EDIT_PROFILE_VM_STATE_KEY] = state.value


    }


    private fun loadPhoto() {
        viewModelScope.launch(context = defaultDispatcher) {
            runCatching {
                _state.update {
                    it.copy(photoUrl = getPhotoUrl())
                }
                startLoading()
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
        viewModelScope.launch(context = defaultDispatcher) {
            runCatching {
                _state.update {
                    val name = getName()
                    it.copy(
                        name = name,
                        nameSnapshot = name
                    )
                }
                startLoading()
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
        viewModelScope.launch(context = defaultDispatcher){
            runCatching {
                _state.update {
                    val phoneNumber = getPhoneNumber()
                    it.copy(
                        phoneNumber = phoneNumber,
                        phoneNumberSnapshot = phoneNumber)
                }
                startLoading()

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
        viewModelScope.launch(context = defaultDispatcher) {
            runCatching {
                startLoading()
                val photoUri = state.value.imageUri?.let { authUseCase.uploadImageUseCase(it) }
                val phoneNumberChanges = with(state.value) {
                    phoneNumber != phoneNumberSnapshot
                }

                if (phoneNumberChanges) {
                    authUseCase.updatePhoneNumberUseCase(state.value.phoneNumber.trim())
                }

                authUseCase.updateProfileUseCase(
                    photoUri = photoUri,
                    name = state.value.name.trim())

            }.onSuccess {
                _eventFlow.emit(EditProfileUiEvent.UpdateUserProfileSuccess(reason = "Successfully Updated!"))
            }.onFailure { exception ->
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
                       _state.update { it.copy(hasInternet = false) }
                    }
                }

            }.also {
                finishLoading()
            }
        }
        savedStateHandle[EDIT_PROFILE_VM_STATE_KEY] = state.value
    }


    private fun finishLoading(){
        _state.update { it.copy(isLoading = false) }
    }
    private fun startLoading(){
        _state.update { it.copy(isLoading = true) }
    }



    private fun getName() = authUseCase.getNameUseCase()
    private fun getPhotoUrl() = authUseCase.getPhotoUrlUseCase()
    private suspend fun getPhoneNumber() = authUseCase.getPhoneNumberUseCase()

}