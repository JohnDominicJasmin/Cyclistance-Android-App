package com.example.cyclistance.navigation

import com.example.cyclistance.core.utils.constants.MessagingConstants.RECEIVER_MESSAGE_ARG
import com.example.cyclistance.core.utils.constants.MessagingConstants.SENDER_MESSAGE_ARG

sealed class Screens {


    open class AuthenticationNavigation(val screenRoute: String = "") : Screens() {
        companion object {
            const val ROUTE = "authentication_navigation"
        }

        object SignInScreen : AuthenticationNavigation(screenRoute = "sign_in_screen")
        object ForgotPasswordScreen : AuthenticationNavigation(screenRoute = "forgot_password_screen")
        object SignUpScreen : AuthenticationNavigation(screenRoute = "sign_up_screen")
        object EmailAuthScreen : AuthenticationNavigation(screenRoute = "email_auth_screen")
    }

    open class EmergencyCallNavigation(val screenRoute: String = "") : Screens() {
        companion object {
            const val ROUTE = "emergency_call_navigation"
        }

        object EmergencyCallScreen : EmergencyCallNavigation(screenRoute = "emergency_call_screen")
    }

    open class MessagingNavigation(val screenRoute: String = "") : Screens() {
        companion object {
            const val ROUTE = "messaging_navigation"
        }

        object ChatScreen : MessagingNavigation(screenRoute = "chat_screen")
        object SearchUserScreen : MessagingNavigation(screenRoute = "search_user_screen")
        object ConversationScreen : MessagingNavigation(screenRoute = "conversation_screen/{$RECEIVER_MESSAGE_ARG}/{$SENDER_MESSAGE_ARG}") {
            fun passArgument(receiverMessageUser: String, senderMessageUser: String) = "conversation_screen/$receiverMessageUser/$senderMessageUser"
        }
    }

    open class OnBoardingNavigation(val screenRoute: String = "") : Screens() {
        companion object {
            const val ROUTE = "on_boarding_navigation"
        }

        object IntroSliderScreen : OnBoardingNavigation(screenRoute = "intro_slider_screen")
    }

    open class MappingNavigation(val screenRoute: String = "") : Screens() {
        companion object {
            const val ROUTE = "mapping_navigation"
        }

        object MappingScreen : MappingNavigation(screenRoute = "mapping_screen")
        object CancellationScreen : MappingNavigation(screenRoute = "cancellation_screen")
        object ConfirmDetailsScreen : MappingNavigation(screenRoute = "confirm_details_screen")
    }

    open class SettingsNavigation(val screenRoute: String = "") : Screens() {

        companion object {
            const val ROUTE = "settings_navigation"
        }

        object ChangePasswordScreen : SettingsNavigation(screenRoute = "change_password_screen")
        object EditProfileScreen : SettingsNavigation(screenRoute = "edit_profile_screen")
        object SettingScreen : SettingsNavigation(screenRoute = "setting_screen")
    }


    open class RideHistoryNavigation(val screenRoute: String = "") : Screens() {
        companion object {
            const val ROUTE = "ride_history_navigation"
        }

        object RideHistoryScreen : RideHistoryNavigation(screenRoute = "ride_history_screen")
        object RideHistoryDetailsScreen :
            RideHistoryNavigation(screenRoute = "ride_history_details_screen")
    }

}
