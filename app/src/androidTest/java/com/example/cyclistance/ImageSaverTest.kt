package com.example.cyclistance

import android.app.Application
import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.Before

class ImageSaverTest: AndroidJUnitRunner() {
    private lateinit var context: Context

    @Before
    fun setup() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
    }


    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?): Application {
        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
    }
}