package com.example.cyclistance

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val sample = "https://lh3.googleusercontent.com/a-/AOh14GhTD_mkobyJg2FxApjwLul1sgaEfuKHlPr-rVsV=s96-c"
        val res = sample.replace(oldValue = "=s96-c", newValue = "=s220-c");
        print("$res \n \n")
    }
}