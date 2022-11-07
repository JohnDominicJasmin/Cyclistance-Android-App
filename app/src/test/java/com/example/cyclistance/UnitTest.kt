package com.example.cyclistance

import com.example.cyclistance.address.MockAddress
import com.example.cyclistance.core.utils.validation.InputValidate.containsSpecialCharacters
import com.example.cyclistance.core.utils.validation.InputValidate.isDigit
import com.example.cyclistance.core.utils.validation.InputValidate.isPhoneNumberLongEnough
import com.example.cyclistance.feature_mapping_screen.domain.exceptions.MappingExceptions
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.utils.getFullAddress
import org.junit.Assert
import org.junit.Test
import timber.log.Timber

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class UnitTest {
    @Test
    fun resizeImage() {
        val sample =
            "https://lh3.googleusercontent.com/a-/AOh14GhTD_mkobyJg2FxApjwLul1sgaEfuKHlPr-rVsV=s96-c"
        val res = sample.replace(oldValue = "=s96-c", newValue = "=s220-c");
        assert(res == "https://lh3.googleusercontent.com/a-/AOh14GhTD_mkobyJg2FxApjwLul1sgaEfuKHlPr-rVsV=s220-c")
    }

    @Test
    fun phoneNumberValidate(){
        val phoneNumber = "9123456789"
        val userPhoneNumber = phoneNumber.trim()

        if(userPhoneNumber.isEmpty()){
            throw MappingExceptions.PhoneNumberException()
        }

        if(!userPhoneNumber.isDigit()){
            throw MappingExceptions.PhoneNumberException("Phone number must contain only numbers.")
        }

        if(userPhoneNumber.containsSpecialCharacters()){
            throw MappingExceptions.PhoneNumberException("Phone number must not contain special characters.")
        }

        if(!userPhoneNumber.isPhoneNumberLongEnough()){
            throw MappingExceptions.PhoneNumberException("Phone number is invalid")
        }

        if(!userPhoneNumber.startsWith(prefix = "9")){
            throw MappingExceptions.PhoneNumberException("Phone number must start with 9")
        }

        assert(true){
            Timber.v("Phone number is valid")
        }
    }






    private fun getNameUseCase(): String? {
        return ""
    }


    @Test
    fun testAddress() {
        val testAddress = MockAddress()
        val expectedResult = "subAdminArea"
        Assert.assertEquals(expectedResult, testAddress.getFullAddress())
    }



    @Test
    fun getNameTest() {
        val name = getNameUseCase().takeIf{ !it.isNullOrEmpty()} ?: throw RuntimeException("Name is null")
        println(name)

    }
}