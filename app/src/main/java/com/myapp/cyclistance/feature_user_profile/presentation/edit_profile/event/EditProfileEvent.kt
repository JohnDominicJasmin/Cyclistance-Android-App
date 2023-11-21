package com.myapp.cyclistance.feature_user_profile.presentation.edit_profile.event

sealed class EditProfileEvent {
    object UpdateUserProfileSuccess : EditProfileEvent()
    data class UpdateUserProfileFailed(val reason: String) : EditProfileEvent()

    data class GetPhotoUrlSuccess(val photoUrl: String) : EditProfileEvent()
    data class GetNameSuccess(val name: String) : EditProfileEvent()
    data class GetBikeGroupSuccess(val cyclingGroup: String) : EditProfileEvent()
    data class GetAddressSuccess(val address: String) : EditProfileEvent()

    data class NameInputFailed(val reason: String) : EditProfileEvent()
    data class AddressInputFailed(val reason: String) : EditProfileEvent()
    object NoInternetConnection: EditProfileEvent()

    data class InternalServerError(val reason: String) : EditProfileEvent()


}