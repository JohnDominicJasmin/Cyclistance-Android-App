package com.myapp.cyclistance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapp.cyclistance.feature_mapping.domain.use_case.MappingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mappingUseCase: MappingUseCase,
    private val defaultDispatcher: CoroutineDispatcher,
): ViewModel() {


    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    init{
        subscribeToIntentActionUpdates()
    }

    private fun subscribeToIntentActionUpdates() {
        viewModelScope.launch(context = SupervisorJob() + defaultDispatcher) {
            mappingUseCase.intentActionUseCase().catch {
            }.distinctUntilChanged().onEach {intentAction ->
                if(intentAction.isEmpty()){
                    return@onEach
                }
                _state.update { it.copy(mappingIntentAction = intentAction) }
            }.launchIn(this)
        }
    }


    fun setIntentAction(intentAction: String){
        viewModelScope.launch(Dispatchers.IO) {
            mappingUseCase.intentActionUseCase(intentAction)
        }
    }

}