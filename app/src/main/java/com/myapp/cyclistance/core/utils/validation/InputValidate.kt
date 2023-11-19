package com.myapp.cyclistance.core.utils.validation

import com.myapp.cyclistance.core.utils.constants.AuthConstants
import com.myapp.cyclistance.core.utils.constants.AuthConstants.MINIMUM_NUMBER_OF_CHARACTERS
import com.myapp.cyclistance.core.utils.constants.AuthConstants.REGEX_EMAIL_VALUE
import com.myapp.cyclistance.core.utils.constants.AuthConstants.REGEX_NUMBER_VALUE
import com.myapp.cyclistance.core.utils.constants.AuthConstants.REGEX_SPECIAL_CHARACTERS_VALUE
import java.util.regex.Pattern

object InputValidate {

    fun String.containsNumeric(): Boolean {
        return Pattern.compile(REGEX_NUMBER_VALUE).matcher(this).find()
    }
     fun String.containsSpecialCharacters(): Boolean {
        return Pattern.compile(REGEX_SPECIAL_CHARACTERS_VALUE).matcher(this).find()
    }
    fun String.numberOfCharactersEnough(): Boolean {
        return this.toCharArray().size >= MINIMUM_NUMBER_OF_CHARACTERS
    }
    fun String.isDigit() = all { it.isDigit() }

    fun String.isPhoneNumberEnough(): Boolean {
        return toCharArray().size == AuthConstants.PHONE_NUMBER_NUMBER_OF_CHARACTERS
    }


    fun String.isEmailValid(): Boolean {
        return Pattern.compile(REGEX_EMAIL_VALUE).matcher(this).matches()
    }


    fun String.isPasswordStrong(): Boolean {
        return isPasswordLongEnough() &&
               (containsNumeric() ||
                containsSpecialCharacters())
    }

     private fun String.isPasswordLongEnough() =
         this.toCharArray().size >= AuthConstants.PASSWORD_MINIMUM_NUMBER_OF_CHARACTERS
}