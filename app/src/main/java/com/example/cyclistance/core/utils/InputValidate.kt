package com.example.cyclistance.core.utils

import android.util.Patterns
import com.example.cyclistance.core.utils.AuthConstants.MINIMUM_NUMBER_OF_CHARACTERS
import com.example.cyclistance.core.utils.AuthConstants.REGEX_NUMBER_VALUE
import com.example.cyclistance.core.utils.AuthConstants.REGEX_SPECIAL_CHARACTERS_VALUE
import java.util.regex.Pattern

object InputValidate {

    fun containsNumeric(input: String): Boolean {
        return Pattern.compile(REGEX_NUMBER_VALUE).matcher(input).find()
    }
     fun containsSpecialCharacters(input: String): Boolean {
        return Pattern.compile(REGEX_SPECIAL_CHARACTERS_VALUE).matcher(input).find()
    }
    fun numberOfCharactersEnough(input: String): Boolean {
        return input.toCharArray().size >= MINIMUM_NUMBER_OF_CHARACTERS
    }

    fun isEmailValid(email: String) = Patterns.EMAIL_ADDRESS.matcher(email).matches()
    fun isPasswordStrong(password: String): Boolean {
        return isNumberOfCharactersLongEnough(password) &&
               (containsNumeric(password) ||
                containsSpecialCharacters(password))
    }

     private fun isNumberOfCharactersLongEnough(password:String) =
         password.toCharArray().size >= AuthConstants.PASSWORD_MINIMUM_NUMBER_OF_CHARACTERS
}