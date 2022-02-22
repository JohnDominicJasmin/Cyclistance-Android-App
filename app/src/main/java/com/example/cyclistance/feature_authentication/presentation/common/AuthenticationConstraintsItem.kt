package com.example.cyclistance.feature_authentication.presentation.common

sealed class AuthenticationConstraintsItem(val layoutId:String){
    object AppIcon: AuthenticationConstraintsItem(layoutId = "app_icon")
    object TopWave: AuthenticationConstraintsItem(layoutId = "top_wave")
    object BottomWave: AuthenticationConstraintsItem(layoutId = "bottom_wave")
    object WelcomeTextArea: AuthenticationConstraintsItem(layoutId = "welcome_text_area")
    object TextFields: AuthenticationConstraintsItem(layoutId = "text_fields")
    object OtherSignIns: AuthenticationConstraintsItem(layoutId = "fb_and_google")
    object SignInButton: AuthenticationConstraintsItem(layoutId = "sign_in_button")
    object ClickableTexts: AuthenticationConstraintsItem(layoutId = "dont_have_account")
    object SignUpButton: AuthenticationConstraintsItem(layoutId = "sign_up_button")
}
