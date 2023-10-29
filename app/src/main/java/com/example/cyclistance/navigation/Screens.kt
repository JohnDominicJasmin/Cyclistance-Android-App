package com.example.cyclistance.navigation

import com.example.cyclistance.core.utils.constants.EmergencyCallConstants.EDIT_CONTACT_ID
import com.example.cyclistance.core.utils.constants.MessagingConstants.CONVERSATION_ID
import com.example.cyclistance.core.utils.constants.NavigationConstants.CANCELLATION_TYPE
import com.example.cyclistance.core.utils.constants.NavigationConstants.CLIENT_ID
import com.example.cyclistance.core.utils.constants.NavigationConstants.LATITUDE
import com.example.cyclistance.core.utils.constants.NavigationConstants.LONGITUDE
import com.example.cyclistance.core.utils.constants.NavigationConstants.TRANSACTION_ID
import com.example.cyclistance.core.utils.constants.RescueRecordConstants
import com.example.cyclistance.core.utils.constants.UserProfileConstants
import com.example.cyclistance.core.utils.constants.UserProfileConstants.USER_ID
import com.example.cyclistance.core.utils.constants.UserProfileConstants.USER_NAME
import com.example.cyclistance.core.utils.constants.UserProfileConstants.USER_PHOTO
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

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
        object AddEditEmergencyContact :EmergencyCallNavigation(screenRoute = "add_edit_emergency_contact_screen?$EDIT_CONTACT_ID={$EDIT_CONTACT_ID}"){
            fun passArgument(contactId: Int) = "add_edit_emergency_contact_screen?$EDIT_CONTACT_ID=$contactId"
        }
    }

    open class MessagingNavigation(val screenRoute: String = "") : Screens() {
        companion object {
            const val ROUTE = "messaging_navigation"
        }

        object Conversation :
            MessagingNavigation(screenRoute = "conversation_screen/{${CONVERSATION_ID}}") {
            fun passArgument(receiverMessageId: String) =
                "conversation_screen/$receiverMessageId"
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


    open class UserProfileNavigation(val screenRoute: String = "") : Screens() {
        companion object {
            const val ROUTE = "user_profile_navigation"
        }

        object UserProfile :
            UserProfileNavigation(screenRoute = "user_profile_screen/{${UserProfileConstants.USER_ID}}") {
            fun passArgument(userId: String) = "user_profile_screen/$userId"

        }

        object EditProfile : UserProfileNavigation(screenRoute = "edit_profile_screen")
    }

    open class ReportAccountNavigation(val screenRoute: String = ""): Screens(){
        companion object{
            const val ROUTE = "report_account_navigation"
        }

        object ReportAccount: ReportAccountNavigation(screenRoute = "report_account_screen/{$USER_ID}/{$USER_NAME}/{$USER_PHOTO}"){
            fun passArgument(userId: String, name: String, userPhoto: String): String {
                val photo = URLEncoder.encode(userPhoto, StandardCharsets.UTF_8.toString())
                return "report_account_screen/$userId/$name/$photo"
            }
        }
    }


    open class RescueRecordNavigation(val screenRoute: String = ""): Screens(){
        companion object{
            const val ROUTE = "rescue_record_navigation"
        }

        object RescueResults: RescueRecordNavigation(screenRoute = "rescue_results_screen")

        object RescueDetails: RescueRecordNavigation(screenRoute = "rescue_details_screen?${RescueRecordConstants.TRANSACTION_ID}={${RescueRecordConstants.TRANSACTION_ID}}"){
            fun passArgument(transactionId: String) = "rescue_details_screen?${RescueRecordConstants.TRANSACTION_ID}=$transactionId"
        }

        object RideHistory : RescueRecordNavigation(screenRoute = "ride_history_screen/{${RescueRecordConstants.RIDE_HISTORY_UID}}"){
            fun passArgument(rideHistoryUid: String) = "ride_history_screen/$rideHistoryUid"
        }
        object RideHistoryDetails :
            RescueRecordNavigation(screenRoute = "ride_history_details_screen/{${RescueRecordConstants.TRANSACTION_ID}}"){
                fun passArgument(transactionId: String) = "ride_history_details_screen/$transactionId"
            }

    }




}
