package com.example.cyclistance.feature_authentication.presentation.authentication_sign_up


sealed class SignUpEvent{
    data class SignUp(val email: String = "", val password: String = "", val confirmPassword: String = ""): SignUpEvent()
    object SignOut: SignUpEvent()
    object TogglePasswordVisibility: SignUpEvent()
    object DismissAlertDialog: SignUpEvent()


}
