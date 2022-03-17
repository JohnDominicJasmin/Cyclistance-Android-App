package com.example.cyclistance.common

import android.util.Patterns
import com.example.cyclistance.common.AuthConstants.REGEX_NUMBER_VALUE
import com.example.cyclistance.common.AuthConstants.REGEX_SPECIAL_CHARACTERS_VALUE
import com.example.cyclistance.feature_authentication.domain.model.AuthModel
import java.util.regex.Pattern

object InputValidate {

    private fun containsNumeric(input: String): Boolean {
        return Pattern.compile(REGEX_NUMBER_VALUE).matcher(input).find()
    }
    private fun containSpecialCharacters(input: String): Boolean {
        return Pattern.compile(REGEX_SPECIAL_CHARACTERS_VALUE).matcher(input).find()
    }
     fun validEmail(email: String) = Patterns.EMAIL_ADDRESS.matcher(email).matches()
     fun strongPassword(password:String): Boolean {
        return isNumberOfCharactersLongEnough(password) &&
               (containsNumeric(password) ||
                containSpecialCharacters(password))
    }

     private fun isNumberOfCharactersLongEnough(password:String) =
         password.toCharArray().size >= AuthConstants.USER_INPUT_MINIMUM_NUMBER_OF_CHARACTERS
}