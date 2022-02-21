package com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.components

sealed class SignInConstraintsItem(val layoutId:String){
    object AppIcon:SignInConstraintsItem(layoutId = "app_icon")
    object TopWave:SignInConstraintsItem(layoutId = "top_wave")
    object BottomWave:SignInConstraintsItem(layoutId = "bottom_wave")
    object WelcomeTextArea:SignInConstraintsItem(layoutId = "welcome_text_area")
    object TextFields:SignInConstraintsItem(layoutId = "text_fields")
    object OtherSignIns:SignInConstraintsItem(layoutId = "fb_and_google")
    object SignInButton:SignInConstraintsItem(layoutId = "sign_in_button")
    object DontHaveAccountText:SignInConstraintsItem(layoutId = "dont_have_account")
}
