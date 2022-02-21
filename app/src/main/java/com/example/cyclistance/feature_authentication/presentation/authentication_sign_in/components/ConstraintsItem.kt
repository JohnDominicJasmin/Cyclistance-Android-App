package com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.components

sealed class ConstraintsItem(val layoutId:String){
    object AppIcon:ConstraintsItem(layoutId = "app_icon")
    object TopWave:ConstraintsItem(layoutId = "top_wave")
    object BottomWave:ConstraintsItem(layoutId = "bottom_wave")
    object WelcomeTextArea:ConstraintsItem(layoutId = "welcome_text_area")
    object TextFields:ConstraintsItem(layoutId = "text_fields")
    object OtherSignIns:ConstraintsItem(layoutId = "fb_and_google")
    object SignInButton:ConstraintsItem(layoutId = "sign_in_button")
    object DontHaveAccountText:ConstraintsItem(layoutId = "dont_have_account")
}
