package com.example.cyclistance.feature_report_account.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.cyclistance.feature_report_account.domain.model.ui.AccountReport
import com.example.cyclistance.feature_report_account.presentation.components.ReportAccountDialogContent
import com.example.cyclistance.feature_report_account.presentation.event.ReportAccountUiEvent
import com.example.cyclistance.feature_report_account.presentation.state.ReportAccountUiState
import com.example.cyclistance.theme.CyclistanceTheme

@Composable
fun ReportAccountDialog(onDismiss: () -> Unit) {

    var uiState by rememberSaveable {
        mutableStateOf(ReportAccountUiState())
    }
    val selectedOptions = remember { mutableStateListOf<String>() }
    var message by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }

    val onChangeMessage = remember {
        { _message: TextFieldValue ->
            message = _message
        }
    }
    val showReportFeedback = remember {
        {
            uiState = uiState.copy(shouldShowReportFeedback = true)
        }
    }
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
                uiState = uiState.copy(accountReport = AccountReport(selectedOptions = it))
            }

        }
    }


    val isReportMaxLimitReached by remember(uiState.accountReport) { derivedStateOf { uiState.accountReport.selectedOptions.size == 3 } }
    val isReportButtonEnabled by remember(uiState.accountReport) { derivedStateOf { uiState.accountReport.selectedOptions.isNotEmpty() } }

    LaunchedEffect(key1 = isReportButtonEnabled) {
        uiState = uiState.copy(
            isReportButtonEnabled = isReportButtonEnabled
        )
    }

    LaunchedEffect(key1 = isReportMaxLimitReached){
        uiState = uiState.copy(
            isReportMaxLimitReached = isReportMaxLimitReached
        )
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = false,
            dismissOnBackPress = true
        )
    ) {

        ReportAccountDialogContent(
            modifier = Modifier,
            message = message,
            uiState = uiState,
            event = { event ->
                when (event) {
                    is ReportAccountUiEvent.DismissReportAccountDialog -> onDismiss()
                    is ReportAccountUiEvent.ShowReportFeedback -> showReportFeedback()
                    is ReportAccountUiEvent.OnReasonChecked -> onReasonChecked(event.reason)
                    is ReportAccountUiEvent.OnChangeMessage -> onChangeMessage(event.message)
                }

            }

        )
    }

}


@Preview
@Composable
fun PreviewReportAccountDialog() {
    CyclistanceTheme(darkTheme = true) {
        ReportAccountDialog(onDismiss = {})
    }
}