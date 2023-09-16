package com.example.cyclistance.navigation

import com.example.cyclistance.core.utils.constants.EmergencyCallConstants.SHOULD_OPEN_CONTACT_DIALOG
import com.example.cyclistance.core.utils.constants.MessagingConstants.RECEIVER_MESSAGE_OBJ
import com.example.cyclistance.core.utils.constants.MessagingConstants.SENDER_MESSAGE_OBJ
import com.example.cyclistance.core.utils.constants.NavigationConstants.CANCELLATION_TYPE
import com.example.cyclistance.core.utils.constants.NavigationConstants.CLIENT_ID
import com.example.cyclistance.core.utils.constants.NavigationConstants.LATITUDE
import com.example.cyclistance.core.utils.constants.NavigationConstants.LONGITUDE
import com.example.cyclistance.core.utils.constants.NavigationConstants.TRANSACTION_ID
import com.example.cyclistance.core.utils.constants.UserProfileConstants.USER_ID

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

        object EmergencyCall :
            EmergencyCallNavigation(screenRoute = "emergency_call_screen?$SHOULD_OPEN_CONTACT_DIALOG={$SHOULD_OPEN_CONTACT_DIALOG}") {
            fun passArgument(shouldOpenContactDialog: Boolean) =
                "emergency_call_screen?$SHOULD_OPEN_CONTACT_DIALOG=$shouldOpenContactDialog"
        }
    }

    open class MessagingNavigation(val screenRoute: String = "") : Screens() {
        companion object {
            const val ROUTE = "messaging_navigation"
        }

        object Chats : MessagingNavigation(screenRoute = "chat_screen")
        object SearchUser : MessagingNavigation(screenRoute = "search_user_screen")
        object Conversation :
            MessagingNavigation(screenRoute = "conversation_screen/{$RECEIVER_MESSAGE_OBJ}/{$SENDER_MESSAGE_OBJ}") {
            fun passArgument(receiverMessageUser: String, senderMessageUser: String) =
                "conversation_screen/$receiverMessageUser/$senderMessageUser"
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

        object SinoTrack: MappingNavigation(screenRoute = "sino_track_screen")
        object RescueResults: MappingNavigation(screenRoute = "rescue_results_screen")
        object RescueDetails: MappingNavigation(screenRoute = "rescue_details_screen")
        object Mapping : MappingNavigation(screenRoute = "mapping_screen")
        object Cancellation :
            MappingNavigation(screenRoute = "cancellation_screen" + "/{${CANCELLATION_TYPE}}/{${TRANSACTION_ID}}/{${CLIENT_ID}}") {
            fun passArgument(cancellationType: String, transactionId: String, clientId: String) =
                "cancellation_screen/$cancellationType/$transactionId/$clientId"

        }
        object ConfirmDetails : MappingNavigation(screenRoute = "confirm_details_screen"+"?${LATITUDE}={${LATITUDE}}&${LONGITUDE}={${LONGITUDE}}"){
            fun passArgument(latitude: Float, longitude: Float) =
                "confirm_details_screen"+"?$LATITUDE=${latitude}&$LONGITUDE=${longitude}"
        }
    }

    open class SettingsNavigation(val screenRoute: String = "") : Screens() {

        companion object {
            const val ROUTE = "settings_navigation"
        }

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

    open class UserProfileNavigation(val screenRoute: String = "") : Screens() {
        companion object {
            const val ROUTE = "user_profile_navigation"
        }

        object UserProfile :
            UserProfileNavigation(screenRoute = "user_profile_screen/{$USER_ID}") {
            fun passArgument(userId: String) = "user_profile_screen/$userId"

        }

        object EditProfile : UserProfileNavigation(screenRoute = "edit_profile_screen")
    }

}
