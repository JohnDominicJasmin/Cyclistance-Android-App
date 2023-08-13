package com.example.cyclistance.navigation

import com.example.cyclistance.core.utils.constants.MessagingConstants.RECEIVER_MESSAGE_ARG
import com.example.cyclistance.core.utils.constants.MessagingConstants.SENDER_MESSAGE_ARG

sealed class Screens {


    open class AuthenticationNavigation(val screenRoute: String = "") : Screens() {
        companion object {
            const val ROUTE = "authentication_navigation"
        }

        object SignIn : AuthenticationNavigation(screenRoute = "sign_in_screen")
        object ForgotPassword : AuthenticationNavigation(screenRoute = "forgot_password_screen")
        object ResetPassword : AuthenticationNavigation(screenRoute = "change_password_screen")
        object SignUp : AuthenticationNavigation(screenRoute = "sign_up_screen")
        object EmailAuth : AuthenticationNavigation(screenRoute = "email_auth_screen")
    }

    open class EmergencyCallNavigation(val screenRoute: String = "") : Screens() {
        companion object {
            const val ROUTE = "emergency_call_navigation"
        }

        object EmergencyCall : EmergencyCallNavigation(screenRoute = "emergency_call_screen")
    }

    open class MessagingNavigation(val screenRoute: String = "") : Screens() {
        companion object {
            const val ROUTE = "messaging_navigation"
        }

        object Chat : MessagingNavigation(screenRoute = "chat_screen")
        object SearchUser : MessagingNavigation(screenRoute = "search_user_screen")
        object Conversation : MessagingNavigation(screenRoute = "conversation_screen/{$RECEIVER_MESSAGE_ARG}/{$SENDER_MESSAGE_ARG}") {
            fun passArgument(receiverMessageUser: String, senderMessageUser: String) = "conversation_screen/$receiverMessageUser/$senderMessageUser"
        }
    }

    open class OnBoardingNavigation(val screenRoute: String = "") : Screens() {
        companion object {
            const val ROUTE = "on_boarding_navigation"
        }

        object IntroSlider : OnBoardingNavigation(screenRoute = "intro_slider_screen")
    }

    open class MappingNavigation(val screenRoute: String = "") : Screens() {
        companion object {
            const val ROUTE = "mapping_navigation"
        }

        object Mapping : MappingNavigation(screenRoute = "mapping_screen")
        object Cancellation : MappingNavigation(screenRoute = "cancellation_screen")
        object ConfirmDetails : MappingNavigation(screenRoute = "confirm_details_screen")
    }

    open class SettingsNavigation(val screenRoute: String = "") : Screens() {

        companion object {
            const val ROUTE = "settings_navigation"
        }

        object EditProfile : SettingsNavigation(screenRoute = "edit_profile_screen")
        object Setting : SettingsNavigation(screenRoute = "setting_screen")
    }


    open class RideHistoryNavigation(val screenRoute: String = "") : Screens() {
        companion object {
            const val ROUTE = "ride_history_navigation"
        }

        object RideHistory : RideHistoryNavigation(screenRoute = "ride_history_screen")
        object RideHistoryDetails :
            RideHistoryNavigation(screenRoute = "ride_history_details_screen")
    }

}
