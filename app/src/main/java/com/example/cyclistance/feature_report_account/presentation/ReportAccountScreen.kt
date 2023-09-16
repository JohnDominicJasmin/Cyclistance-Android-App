package com.example.cyclistance.feature_report_account.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.cyclistance.R
import com.example.cyclistance.core.domain.model.AlertDialogState
import com.example.cyclistance.core.utils.composable_utils.rememberMutableStateListOf
import com.example.cyclistance.feature_report_account.domain.model.ReportAccountDetails
import com.example.cyclistance.feature_report_account.domain.model.ui.OptionsReport
import com.example.cyclistance.feature_report_account.presentation.components.ReportAccountContent
import com.example.cyclistance.feature_report_account.presentation.event.ReportAccountEvent
import com.example.cyclistance.feature_report_account.presentation.event.ReportAccountUiEvent
import com.example.cyclistance.feature_report_account.presentation.event.ReportAccountVmEvent
import com.example.cyclistance.feature_report_account.presentation.state.ReportAccountUiState
import kotlinx.coroutines.flow.collectLatest
import java.util.Date

@Composable
fun ReportAccountScreen(
    paddingValues: PaddingValues,
    navController: NavController,
    viewModel: ReportAccountViewModel = hiltViewModel()) {

    var uiState by remember {
        mutableStateOf(ReportAccountUiState())
    }

    val state by viewModel.state.collectAsStateWithLifecycle()
    val selectedOptions = rememberMutableStateListOf<String>()
    var comment by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }

    val onChangeComment = remember {
        { _comment: TextFieldValue ->
            comment = _comment
        }
    }

    val reportAccount = remember{{
        viewModel.onEvent(event = ReportAccountVmEvent.ReportAccount(
            reportAccountDetails = ReportAccountDetails(
               reasons = OptionsReport(selectedOptions = selectedOptions),
                name = state.reportedName,
                date = Date(),
                comment = comment.text,
                userId = state.reportedId,
                idReportBy = state.userId
            )
        ))
    }}

    val onReasonChecked = remember {
        { reason: String ->

            selectedOptions.apply {
                if (contains(reason)) {
                    remove(reason)
                    return@apply
                }

                if (uiState.isReportMaxLimitReached) {
                    return@apply
                }
                selectedOptions.add(reason)

            }.also {
                uiState = uiState.copy(optionsReport = OptionsReport(selectedOptions = it))
            }

        }
    }


    val isReportMaxLimitReached by remember(uiState.optionsReport) { derivedStateOf { uiState.optionsReport.selectedOptions.size == 3 } }
    val isReportButtonEnabled by remember(uiState.optionsReport) { derivedStateOf { uiState.optionsReport.selectedOptions.isNotEmpty() } }

    val dismissAlertDialog = remember{{
        uiState = uiState.copy(
            alertDialogState = AlertDialogState()
        )
    }}

    val showReportFeedback = remember {
        {
            uiState = uiState.copy(shouldShowReportFeedback = true)
        }
    }










    LaunchedEffect(key1 = isReportButtonEnabled) {
        uiState = uiState.copy(
            isReportButtonEnabled = isReportButtonEnabled
        )
    }

    LaunchedEffect(key1 = isReportMaxLimitReached) {
        uiState = uiState.copy(
            isReportMaxLimitReached = isReportMaxLimitReached
        )
    }

    LaunchedEffect(key1 = true){
        viewModel.eventFlow.collectLatest { event ->
            when(event){
                ReportAccountEvent.ReportAccountSuccess -> showReportFeedback()
                is ReportAccountEvent.ReportAccountFailed -> {
                    uiState = uiState.copy(
                        alertDialogState = AlertDialogState(
                            title = "Failed to report account",
                            description = event.reason,
                            icon = R.raw.error
                        )
                    )
                }
            }
        }
    }


    ReportAccountContent(
        modifier = Modifier.padding(paddingValues),
        message = comment,
        uiState = uiState,
        event = { event ->
            when (event) {
                is ReportAccountUiEvent.CloseReportAccountScreen -> navController.popBackStack()
                is ReportAccountUiEvent.ReportAccount -> reportAccount()
                is ReportAccountUiEvent.OnReasonChecked -> onReasonChecked(event.reason)
                is ReportAccountUiEvent.OnChangeMessage -> onChangeComment(event.message)
                ReportAccountUiEvent.DismissAlertDialog -> dismissAlertDialog()
            }
        },
        state = state

    )

}

