package com.example.cyclistance.feature_authentication.presentation.authentication_sign_up


sealed class SignUpEvent{
    object SignUp: SignUpEvent()
    object SignOut: SignUpEvent()
    object TogglePasswordVisibility: SignUpEvent()
    object DismissAlertDialog: SignUpEvent()
    data class EnterEmail(val email: String): SignUpEvent()
    data class EnterPassword(val password: String): SignUpEvent()
    data class EnterConfirmPassword(val confirmPassword: String): SignUpEvent()
    object DismissNoInternetDialog: SignUpEvent()

}
