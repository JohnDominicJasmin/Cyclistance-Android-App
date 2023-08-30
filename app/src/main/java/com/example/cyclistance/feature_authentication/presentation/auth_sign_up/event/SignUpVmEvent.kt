package com.example.cyclistance.feature_authentication.presentation.auth_sign_up.event


sealed class SignUpVmEvent{
    data class SignUp(
        val email: String,
        val password: String,
        val confirmPassword: String,
    ): SignUpVmEvent()

    object SignOut: SignUpVmEvent()
    object AgreedToPrivacyPolicy : SignUpVmEvent()
}
