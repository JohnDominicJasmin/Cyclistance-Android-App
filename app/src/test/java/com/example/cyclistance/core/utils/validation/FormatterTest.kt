package com.myapp.cyclistance.core.utils.validation

import com.myapp.cyclistance.core.utils.formatter.FormatterUtils.formatToDistanceKm
import com.myapp.cyclistance.core.utils.formatter.FormatterUtils.getCalculatedETA
import org.junit.Assert.assertEquals
import org.junit.Test

class FormatterTest {

    @Test
    fun `distance format to meters`(){
        val input = 10.0
        val result = input.formatToDistanceKm()
        assertEquals("10.00 m", result)
    }

    @Test
    fun `distance format to kilometers`(){
        val input = 1000.0
        val result = input.formatToDistanceKm()
        assertEquals("1.00 km", result)
    }



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