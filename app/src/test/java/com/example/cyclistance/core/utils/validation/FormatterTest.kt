package com.example.cyclistance.core.utils.validation

import com.example.cyclistance.core.utils.validation.FormatterUtils.distanceFormat
import com.example.cyclistance.core.utils.validation.FormatterUtils.getCalculatedETA
import org.junit.Assert.assertEquals
import org.junit.Test

class FormatterTest {

    @Test
    fun `distance format to meters`(){
        val input = 10.0
        val result = input.distanceFormat()
        assertEquals("10.00 m", result)
    }

    @Test
    fun `distance format to kilometers`(){
        val input = 1000.0
        val result = input.distanceFormat()
        assertEquals("1.00 km", result)
    }


    //make a test getCalculatedETA
    @Test
    fun `get calculated ETA`(){
        val result = getCalculatedETA(distanceMeters = 5000.0)
        assertEquals("12 mins", result)
    }

    @Test
    fun `get calculated ETA distance 0 meters`(){
        val result = getCalculatedETA(distanceMeters = 0.00)
        assertEquals("0 min", result)
    }



}