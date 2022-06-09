package com.example.cyclistance

import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun resizeImage() {
        val sample = "https://lh3.googleusercontent.com/a-/AOh14GhTD_mkobyJg2FxApjwLul1sgaEfuKHlPr-rVsV=s96-c"
        val res = sample.replace(oldValue = "=s96-c", newValue = "=s220-c");
        print("$res \n \n")
    }

    @Test
    fun filterName(){
        assert(value = getName()=="johndoe394").also{
            print("Result is ${getName()}")
        }
    }
    private fun getNameUseCase():String?{
        return null
    }
    private fun getName()=
        getNameUseCase()?:getEmail().apply{
            val index = this.indexOf('@')
            return this.substring(0, index)
        }

    private fun getEmail():String {
        return "johndoe394@gmail.com"
    }
}