package com.example.cyclistance.feature_report_account.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.core.utils.constants.ReportAccountConstants.REPORT_ACCOUNT_VM_STATE_KEY
import com.example.cyclistance.core.utils.constants.UserProfileConstants.USER_ID
import com.example.cyclistance.core.utils.constants.UserProfileConstants.USER_NAME
import com.example.cyclistance.core.utils.constants.UserProfileConstants.USER_PHOTO
import com.example.cyclistance.feature_authentication.domain.use_case.AuthenticationUseCase
import com.example.cyclistance.feature_report_account.domain.model.ReportAccountDetails
import com.example.cyclistance.feature_report_account.domain.use_case.ReportAccountUseCase
import com.example.cyclistance.feature_report_account.presentation.event.ReportAccountEvent
import com.example.cyclistance.feature_report_account.presentation.event.ReportAccountVmEvent
import com.example.cyclistance.feature_report_account.presentation.state.ReportAccountState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ReportAccountViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val reportAccountUseCase: ReportAccountUseCase,
    private val authUseCase: AuthenticationUseCase
): ViewModel() {

    private val _state = MutableStateFlow(savedStateHandle[REPORT_ACCOUNT_VM_STATE_KEY] ?: ReportAccountState(
        reportedName = savedStateHandle[USER_NAME]!!,
        reportedPhoto = savedStateHandle[USER_PHOTO]!!,
        reportedId = savedStateHandle[USER_ID]!!,
        userId = getId()
    ))

    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<ReportAccountEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        getLastReportedId()
    }

    private fun getLastReportedId() {
        viewModelScope.launch(SupervisorJob() + Dispatchers.IO) {
            reportAccountUseCase.lastReportIdUseCase().catch {
                Timber.v("Failed to get last reported id ${it.message}")
            }.onEach { lastReportedId ->
                _state.update { it.copy(lastReportedId = lastReportedId) }
            }.launchIn(this)
        }
    }



    fun onEvent(event: ReportAccountVmEvent){
        when(event){
            is ReportAccountVmEvent.ReportAccount -> reportAccount(event.reportAccountDetails)
        }
        savedStateHandle[REPORT_ACCOUNT_VM_STATE_KEY] = state.value
    }

    private fun reportAccount(reportAccountDetails: ReportAccountDetails){
        viewModelScope.launch(SupervisorJob() + Dispatchers.IO){
            runCatching {
                reportAccountUseCase.reportUseCase(reportAccountDetails)
            }.onSuccess {
                _eventFlow.emit(value = ReportAccountEvent.ReportAccountSuccess)
                reportAccountUseCase.lastReportIdUseCase(lastReportedId = reportAccountDetails.userId)
            }.onFailure {
                _eventFlow.emit(value = ReportAccountEvent.ReportAccountFailed(reason = it.message ?: "Failed to report account"))
            }
        }
    }

    private fun getId(): String = authUseCase.getIdUseCase()
}