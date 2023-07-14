package com.example.cyclistance.core.utils.validation

import com.example.cyclistance.core.utils.validation.InputValidate.containsNumeric
import com.example.cyclistance.core.utils.validation.InputValidate.containsSpecialCharacters
import com.example.cyclistance.core.utils.validation.InputValidate.isDigit
import com.example.cyclistance.core.utils.validation.InputValidate.isEmailValid
import com.example.cyclistance.core.utils.validation.InputValidate.isPasswordStrong
import com.example.cyclistance.core.utils.validation.InputValidate.isPhoneNumberEnough
import com.example.cyclistance.core.utils.validation.InputValidate.numberOfCharactersEnough
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class InputValidateTest {

    @Test
    fun inputEmailValue_returnsTrue() {
        val input = "johndoe@gmail.com"
        val result = input.isEmailValid()
        assertTrue(result)
    }

    @Test
    fun inputEmailValue_returnsFalse(){
        val input = "johndoe"
        val result = input.isEmailValid()
        assertFalse(result)
    }
    @Test
    fun `input contains numeric returns true`(){
        val input = "aslkdnalsdknalksd29"
        val result = input.containsNumeric()
        assertTrue(result)
    }

    @Test
    fun `input contains numeric returns false`(){
        val input = "aslkdnalsdknalksd"
        val result = input.containsNumeric()
        assertFalse(result)
    }


    @Test
    fun `input contains special characters returns true`(){
        val input = "aslkdnalsdknalks@"
        val result = input.containsSpecialCharacters()
        assertTrue(result)
    }

    @Test
    fun `input contains special characters returns false`(){
        val input = "aslkdnalsdknalksd"
        val result = input.containsSpecialCharacters()
        assertFalse(result)
    }


    @Test
    fun `input number of characters enough returns true`(){
        val input = "aslkdnalsdknalksd"
        val result = input.numberOfCharactersEnough()
        assertTrue(result)
    }

    @Test
    fun `input number of characters enough returns false`(){
        val input = "dn"
        val result = input.numberOfCharactersEnough()
        assertFalse(result)
    }

    @Test
    fun `input isDigit returns true`(){
        val input = "123456789"
        val result = input.isDigit()
        assertTrue(result)
    }

    @Test
    fun `input isDigit returns false`(){
        val input = "aasdas23123"
        val result = input.isDigit()
        assertFalse(result)
    }

    @Test
    fun `input isPhoneNumberLongEnough returns true`(){
        val input = "1234567890"
        val result = input.isPhoneNumberEnough()
        assertTrue(result)
    }

    @Test
    fun `input isPhoneNumberLongEnough returns false`(){
        val input = "123456789"
        val result = input.isPhoneNumberEnough()
        assertFalse(result)
    }


    @Test
    fun `input isPasswordStrong returns true`(){
        val input = "johndoe_"
        val result = input.isPasswordStrong()
        assertTrue(result)
    }

    @Test
    fun `input isPasswordStrong returns false`(){
        val input = "johndoeeee"
        val result = input.isPasswordStrong()
        assertFalse(result)
    }
}