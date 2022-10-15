package com.example.cyclistance

import com.example.cyclistance.address.MockAddress
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.utils.getFullAddress
import org.junit.Assert
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun resizeImage() {
        val sample =
            "https://lh3.googleusercontent.com/a-/AOh14GhTD_mkobyJg2FxApjwLul1sgaEfuKHlPr-rVsV=s96-c"
        val res = sample.replace(oldValue = "=s96-c", newValue = "=s220-c");
        print("$res \n \n")
        assert(res == "https://lh3.googleusercontent.com/a-/AOh14GhTD_mkobyJg2FxApjwLul1sgaEfuKHlPr-rVsV=s220-c")
    }




    private fun getNameUseCase(): String? {
        return ""
    }

    private fun getEmailUseCase(): String? {
        return "johndoe394@gmail.com"
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