package com.example.cyclistance

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnitRunner
import com.example.cyclistance.core.utils.save_images.ImageUtils.saveImageToGallery
import dagger.hilt.android.testing.HiltTestApplication
import junit.framework.TestCase.assertNotNull
import org.junit.Before
import org.junit.Test

class ImageSaverTest: AndroidJUnitRunner() {
    private lateinit var context: Context

    @Before
    fun setup() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun testSaveImage() {
        val bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
        val uri = context.saveImageToGallery(bitmap)
        assertNotNull(uri)
    }
    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?): Application {
        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
    }
}