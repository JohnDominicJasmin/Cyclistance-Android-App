package com.example.cyclistance.common

import androidx.datastore.preferences.core.stringPreferencesKey

object AuthConstants {
    const val REGEX_NUMBER_VALUE="[0-9]"
    const val REGEX_SPECIAL_CHARACTERS_VALUE = "[!#$%&*()_+=|<>?{}\\[\\]~]"
    const val PASSWORD_MINIMUM_NUMBER_OF_CHARACTERS = 8
    const val TIMER_COUNTS: Long = 90000
    const val ONE_SECOND_TO_MILLIS: Long = 1000
    const val REFRESH_EMAIL_INTERVAL: Long = 1800
    const val GOOGLE_SIGN_IN_REQUEST_CODE: Int = 1
    const val FACEBOOK_CONNECTION_FAILURE = "CONNECTION_FAILURE: CONNECTION_FAILURE"
    const val USER_NOT_FOUND = "ERROR_USER_NOT_FOUND"
    val DATA_STORE_PHONE_NUMBER_KEY = stringPreferencesKey("phone_number_key")
    const val MINIMUM_NUMBER_OF_CHARACTERS = 3

}