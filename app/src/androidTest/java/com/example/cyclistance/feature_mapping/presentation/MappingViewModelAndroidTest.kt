package com.example.cyclistance.feature_mapping.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.platform.app.InstrumentationRegistry
import com.example.cyclistance.feature_authentication.domain.use_case.AuthenticationUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.MappingUseCase
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named


@HiltAndroidTest
class MappingViewModelAndroidTest{


    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_auth_use_case")
    lateinit var authUseCase: AuthenticationUseCase

    @Inject
    @Named("test_mapping_use_case")
    lateinit var mappingUseCase: MappingUseCase

    @Before
    fun setUp() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        hiltRule.inject()

    }

    @Test
    fun assertTrue(){
        Assert.assertTrue(true)

    }
}