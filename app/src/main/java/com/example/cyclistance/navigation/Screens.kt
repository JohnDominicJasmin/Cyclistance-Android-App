package com.example.cyclistance.navigation

sealed class Screens {


    open class Authentication(val screenRoute: String = "") : Screens() {
        companion object {
            const val ROUTE = "authentication"
        }

        object SignInScreen : Authentication(screenRoute = "sign_in_screen")
        object SignUpScreen : Authentication(screenRoute = "sign_up_screen")
        object EmailAuthScreen : Authentication(screenRoute = "email_auth_screen")
    }

    open class EmergencyCall(val screenRoute: String = "") : Screens() {
        companion object {
            const val ROUTE = "emergency_call"
        }

        object EmergencyCallScreen : EmergencyCall(screenRoute = "emergency_call_screen")
        object AddNewContact : EmergencyCall(screenRoute = "emergency_add_new_contact")
    }

    open class Messaging(val screenRoute: String = "") : Screens() {
        companion object {
            const val ROUTE = "messaging"
        }

        object MessagingScreen : Messaging(screenRoute = "messaging_screen")
    }

    open class OnBoarding(val screenRoute: String = "") : Screens() {
        companion object {
            const val ROUTE = "on_boarding"
        }

        object IntroSliderScreen : OnBoarding(screenRoute = "intro_slider_screen")
    }

    open class Mapping(val screenRoute: String = "") : Screens() {
        companion object {
            const val ROUTE = "mapping"
        }

        object MappingScreen : Mapping(screenRoute = "mapping_screen")
        object CancellationScreen : Mapping(screenRoute = "cancellation_screen")
        object ConfirmDetailsScreen : Mapping(screenRoute = "confirm_details_screen")
    }

    open class Settings(val screenRoute: String = "") : Screens() {

        companion object {
            const val ROUTE = "settings"
        }

        object ChangePasswordScreen : Settings(screenRoute = "change_password_screen")
        object EditProfileScreen : Settings(screenRoute = "edit_profile_screen")
        object SettingScreen : Settings(screenRoute = "setting_screen")
    }


    open class RideHistory(val screenRoute: String = "") : Screens() {
        companion object {
            const val ROUTE = "ride_history"
        }

        object RideHistoryScreen : RideHistory(screenRoute = "ride_history_screen")
        object RideHistoryDetailsScreen : RideHistory(screenRoute = "ride_history_details_screen")
    }

}
