package com.example.cyclistance.feature_settings.presentation.setting_edit_profile

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.common.MappingConstants.IMAGE_PLACEHOLDER_URL
import com.example.cyclistance.feature_authentication.domain.use_case.AuthenticationUseCase
import com.example.cyclistance.feature_main_screen.domain.exceptions.MappingExceptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class EditProfileViewModel @Inject constructor(

    private val authUseCase: AuthenticationUseCase) : ViewModel() {

    private val _state = mutableStateOf(EditProfileState())
    val state = _state

    private val _eventFlow = MutableSharedFlow<EditProfileUiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


    private fun getName(): String {
        val email = authUseCase.getEmailUseCase()
        return authUseCase.getNameUseCase() ?: email?.run {
            val index = this.indexOf('@')
            this.substring(0, index)
        } ?: throw MappingExceptions.UnavailableName()
    }

    private fun getPhotoUrl(): String {
        return authUseCase.getPhotoUrlUseCase()?.toString()
               ?: IMAGE_PLACEHOLDER_URL
    }

    private suspend fun getPhoneNumber() = authUseCase.getPhoneNumberUseCase()


    init {
        onEvent(event = EditProfileEvent.LoadName)
        onEvent(event = EditProfileEvent.LoadPhoto)
        onEvent(event = EditProfileEvent.LoadPhoneNumber)
    }


    fun onEvent(event: EditProfileEvent) {
        when (event) {
            is EditProfileEvent.SaveProfile -> {
                viewModelScope.launch {
//             todo: update photo url, name and phone number using authUseCase, add validation
                }
            }
            is EditProfileEvent.EnteredPhoneNumber -> {
                _state.value =
                    state.value.copy(phoneNumber = event.phoneNumber, phoneNumberErrorMessage = "")
            }
            is EditProfileEvent.EnteredName -> {
                _state.value = state.value.copy(name = event.name, nameErrorMessage = "")
            }
            is EditProfileEvent.NewImageUri -> {
                _state.value = state.value.copy(imageUri = event.uri)
            }
            is EditProfileEvent.NewBitmapPicture -> {
                _state.value = state.value.copy(bitmap = event.bitmap)
            }
            is EditProfileEvent.LoadPhoto -> {
                viewModelScope.launch {
                    kotlin.runCatching {
                        _state.value = state.value.copy(
                            photoUrl = getPhotoUrl(),
                            isLoading = true
                        )
                    }.onSuccess {
                        _state.value = state.value.copy(isLoading = false)
                    }
                }
            }
            is EditProfileEvent.LoadPhoneNumber -> {
                viewModelScope.launch {
                    kotlin.runCatching {
                        _state.value = state.value.copy(
                            phoneNumber = TextFieldValue(text = getPhoneNumber()),
                            isLoading = true
                        )
                    }.onSuccess {
                        _state.value = state.value.copy(isLoading = false)
                    }.onFailure { exception ->
                        _state.value = state.value.copy(
                            isLoading = false,
                            phoneNumberErrorMessage = exception.message ?: "Field cannot be blank."
                        )
                    }
                }
            }
            is EditProfileEvent.LoadName -> {
                viewModelScope.launch {
                    kotlin.runCatching {
                        _state.value = state.value.copy(
                            name = TextFieldValue(text = getName()),
                            isLoading = true
                        )
                    }.onSuccess {
                        _state.value = state.value.copy(isLoading = false)
                    }.onFailure { exception ->
                        _state.value = state.value.copy(
                            isLoading = false,
                            nameErrorMessage = exception.message ?: "Field cannot be blank."
                        )
                    }
                }
            }

        }
    }


}