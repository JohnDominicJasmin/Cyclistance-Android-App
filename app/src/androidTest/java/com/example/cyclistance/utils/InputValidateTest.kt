package com.example.cyclistance.utils

import android.location.Geocoder
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.cyclistance.R
import com.example.cyclistance.core.utils.constants.MappingConstants
import com.example.cyclistance.core.utils.validation.FormatterUtils
import com.example.cyclistance.core.utils.validation.FormatterUtils.getAddress
import com.example.cyclistance.core.utils.validation.FormatterUtils.getFullAddress
import com.example.cyclistance.core.utils.validation.FormatterUtils.getMapIconImage
import com.example.cyclistance.core.utils.validation.InputValidate.isEmailValid
import com.example.cyclistance.feature_mapping.data.remote.dto.user_dto.Location
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class InputValidateTest {

    @Test
    fun inputEmailValue_returnsTrue() {
        val input = "johndoe@gmail.com"
        val result = input.isEmailValid()
        Assert.assertTrue(result)
    }

    @Test
    fun inputEmailValue_returnsFalse(){
        val input = "johndoe"
        val result = input.isEmailValid()
        Assert.assertFalse(result)
    }


    @Test
    fun getAddressFromGeocoder(){
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val geocoder = Geocoder(appContext)
        val manilaLatitude = 14.5995
        val manilaLongitude = 120.9842

        geocoder.getAddress(
            latitude = manilaLatitude,
            longitude = manilaLongitude
        ){
            Assert.assertTrue(it != null)
            Assert.assertEquals("1001 Paterno Street., Manila", it!!.getFullAddress().trim())


        }

    }



    @Test
    fun getETABetweenTwoPointsTest(){
        val result = FormatterUtils.getETABetweenTwoPoints(
            startingLocation = Location(
                latitude = 40.74030573169066,
                longitude = -73.98681511981998
            ),
            endLocation = Location(
                latitude = 40.72358717941432,
                longitude = -73.99637648058844
            )
        )
        Assert.assertEquals("4 mins", result)
    }

    @Test
    fun getMapIconImageTest_returnsInjuryIcon(){
        val description = MappingConstants.INJURY_TEXT
        val result = description.getMapIconImage()
        Assert.assertEquals(R.drawable.ic_injury_em, result)
    }


    @Test
    fun getMapIconImageTest_returnsBrokenFrameIcon(){
        val description = MappingConstants.BROKEN_FRAME_TEXT
        val result = description.getMapIconImage()
        Assert.assertEquals(R.drawable.ic_broken_frame_em, result)
    }

    @Test
    fun getMapIconImageTest_returnsIncidentIcon(){
        val description = MappingConstants.INCIDENT_TEXT
        val result = description.getMapIconImage()
        Assert.assertEquals(R.drawable.ic_incident_em, result)
    }

    @Test
    fun getMapIconImageTest_returnsBrokenChainIcon(){
        val description = MappingConstants.BROKEN_CHAIN_TEXT
        val result = description.getMapIconImage()
        Assert.assertEquals(R.drawable.ic_broken_chain_em, result)
    }

    @Test
    fun getMapIconImageTest_returnsFlatTireIcon(){
        val description = MappingConstants.FLAT_TIRES_TEXT
        val result = description.getMapIconImage()
        Assert.assertEquals(R.drawable.ic_flat_tire_em, result)
    }


    @Test
    fun getMapIconImageTest_returnsFaultyBrakesIcon(){
        val description = MappingConstants.FAULTY_BRAKES_TEXT
        val result = description.getMapIconImage()
        Assert.assertEquals(R.drawable.ic_faulty_brakes_em, result)
    }

    @Test
    fun getMapIconImageTest_returnsNull(){
        val description = "Other"
        val result = description.getMapIconImage()
        Assert.assertEquals(null, result)
    }
}