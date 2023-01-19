package com.example.cyclistance.feature_mapping.presentation

import androidx.lifecycle.SavedStateHandle
import com.example.cyclistance.di.TestAppModule
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.MappingEvent
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.MappingViewModel
import com.example.cyclistance.test_rule.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MappingViewModelTest {

    private lateinit var mappingViewModel: MappingViewModel


    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup(){
        mappingViewModel = MappingViewModel(
            savedStateHandle = SavedStateHandle(),
            authUseCase = TestAppModule.provideTestAuthUseCase(),
            mappingUseCase = TestAppModule.provideTestMappingUseCase())
    }


    @ExperimentalCoroutinesApi
    @Test
    fun `request_help_testing`() = runTest {
        mappingViewModel.onEvent(MappingEvent.SubscribeToDataChanges)
        mappingViewModel.onEvent(MappingEvent.LoadData)
        launch {
            delay(2000)
            Assert.assertEquals(true,mappingViewModel.state.value.nearbyCyclists?.users?.isNotEmpty() ?: false)
        }


    }

}