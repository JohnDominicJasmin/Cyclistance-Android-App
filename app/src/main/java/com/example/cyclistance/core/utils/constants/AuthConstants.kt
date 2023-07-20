package com.example.cyclistance.core.utils.constants

import androidx.datastore.preferences.core.stringPreferencesKey

object AuthConstants {

    const val PHONE_NUMBER_NUMBER_OF_CHARACTERS: Int = 10
    const val REGEX_NUMBER_VALUE="[0-9]"
    const val REGEX_SPECIAL_CHARACTERS_VALUE = "[@!#$%&*()_+=|<>?{}\\[\\]~]"
    const val REGEX_EMAIL_VALUE =
        "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@" +
        "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?" +
        "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\." +
        "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?" +
        "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|" +
        "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"

    const val PASSWORD_MINIMUM_NUMBER_OF_CHARACTERS = 8
    const val TIMER_COUNTS: Long = 90000
    const val ONE_SECOND_TO_MILLIS: Long = 1000
    const val REFRESH_EMAIL_INTERVAL: Long = 2000
    const val GOOGLE_SIGN_IN_REQUEST_CODE: Int = 1
    const val FACEBOOK_CONNECTION_FAILURE = "CONNECTION_FAILURE: CONNECTION_FAILURE"
    const val USER_NOT_FOUND = "ERROR_USER_NOT_FOUND"
    val DATA_STORE_PHONE_NUMBER_KEY = stringPreferencesKey("phone_number_key")
    const val MINIMUM_NUMBER_OF_CHARACTERS = 3
    const val IMAGE_SMALL_SIZE = "=s96-c"
    const val IMAGE_LARGE_SIZE = "=s400-c"


    const val EMAIL_AUTH_VM_STATE_KEY = "email_auth_vm_state_key"
    const val SIGN_IN_VM_STATE_KEY = "sign_in_vm_state_key"
    const val SIGN_UP_VM_STATE_KEY = "sign_up_vm_state_key"
    const val USER_DOCUMENT = "Users"
}