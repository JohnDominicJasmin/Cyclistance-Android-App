package com.example.cyclistance.feature_report_account.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.cyclistance.R
import com.example.cyclistance.core.domain.model.AlertDialogState
import com.example.cyclistance.core.presentation.dialogs.alert_dialog.AlertDialog
import com.example.cyclistance.feature_authentication.presentation.common.visible
import com.example.cyclistance.feature_mapping.presentation.common.AdditionalMessage
import com.example.cyclistance.feature_mapping.presentation.common.ButtonNavigation
import com.example.cyclistance.feature_report_account.domain.model.ui.OptionsReport
import com.example.cyclistance.feature_report_account.presentation.event.ReportAccountUiEvent
import com.example.cyclistance.feature_report_account.presentation.state.ReportAccountState
import com.example.cyclistance.feature_report_account.presentation.state.ReportAccountUiState
import com.example.cyclistance.theme.Black500
import com.example.cyclistance.theme.CyclistanceTheme


private val reasonsToReportAccount = listOf(
    "Misleading or Scam",
    "Verbal Abuse",
    "Sexualizing",
    "Negative behavior",
    "Pretending to be someone else",
    "Violence",
)

@Composable
fun ReportAccountContent(
    modifier: Modifier = Modifier,
    message: TextFieldValue,
    uiState: ReportAccountUiState,
    state: ReportAccountState,
    event: (ReportAccountUiEvent) -> Unit
) {


    Surface(
        modifier = modifier
            .fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {

        val shouldShowReportFeedback = uiState.shouldShowReportFeedback || state.reportedId == state.lastReportedId
        val shouldShowReportAccount = !uiState.shouldShowReportFeedback && state.reportedId != state.lastReportedId

        if(shouldShowReportAccount) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth(0.95f)
                    .background(MaterialTheme.colors.background)
            ) {

                val (image, text, divider, selections, comment, buttonNavButtonSection, dialog) = createRefs()

                Image(
                    painter = painterResource(id = R.drawable.ic_report_account_dark),
                    contentDescription = "Report Account",
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .constrainAs(image) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            height = Dimension.wrapContent
                            width = Dimension.percent(0.6f)
                        },
                    contentScale = ContentScale.Fit
                )

                Divider(
                    color = Black500,
                    thickness = (1).dp,
                    modifier = Modifier
                        .padding(vertical = 4.dp, horizontal = 16.dp)
                        .constrainAs(divider) {
                            top.linkTo(image.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                )

                Text(
                    text = "Select the reason for reporting this account",
                    color = Black500,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .constrainAs(text) {
                            top.linkTo(divider.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)

                        }
                )

                Column(modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .constrainAs(selections) {
                        top.linkTo(text.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }) {

                    reasonsToReportAccount.forEach { reason ->
                        val isChecked by remember(uiState.optionsReport) { derivedStateOf { reason in uiState.optionsReport.selectedOptions } }
                        ReportReasonItem(
                            label = reason,
                            isChecked = isChecked,
                            onReasonChecked = {
                                event(ReportAccountUiEvent.OnReasonChecked(reason = reason))
                            })
                    }
                }

                AdditionalMessage(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .constrainAs(comment) {
                            top.linkTo(selections.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            height = Dimension.percent(0.23f)
                            width = Dimension.matchParent
                        },
                    message = message,
                    onChangeValueMessage = { event(ReportAccountUiEvent.OnChangeMessage(it)) },
                    enabled = true,
                    text = "Comment",
                    placeholderText = "Comment (Optional)"
                )


                ButtonNavigation(
                    modifier = Modifier.constrainAs(buttonNavButtonSection) {
                        top.linkTo(comment.bottom, margin = 30.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom, margin = 30.dp)
                        height = Dimension.wrapContent
                        width = Dimension.percent(0.9f)
                    },

                    onClickNegativeButton = { event(ReportAccountUiEvent.CloseReportAccountScreen) },
                    onClickPositiveButton = { event(ReportAccountUiEvent.ReportAccount) },
                    positiveButtonEnabled = uiState.isReportButtonEnabled
                )

                if(uiState.alertDialogState.visible()){
                    AlertDialog(alertDialog = uiState.alertDialogState, modifier = Modifier.constrainAs(dialog){
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        centerTo(parent)
                    }, onDismissRequest = {
                        event(ReportAccountUiEvent.DismissAlertDialog)
                    })
                }
            }
        }


        if(shouldShowReportFeedback) {
            ReportAccountFeedback(
                modifier = Modifier
                    .fillMaxSize(),
                photo = state.reportedPhoto,
                name = state.reportedName,
                onClickOkayButton = { event(ReportAccountUiEvent.CloseReportAccountScreen) }
            )

        }

    }
}


@Preview(device = "id:Galaxy Nexus")
@Composable
private fun PreviewReportAccountDialogContent() {

    var uiState by remember {
        mutableStateOf(ReportAccountUiState())
    }

    val selectedOptions = remember { mutableStateListOf<String>() }
    var comment by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }

    val onChangeComment = remember {
        { _comment: TextFieldValue ->
            comment = _comment
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
                uiState = uiState.copy(optionsReport = OptionsReport(selectedOptions = it))
            }

        }
    }

    val dismissAlertDialog = remember{{
        uiState = uiState.copy(
            alertDialogState = AlertDialogState()
        )
    }}


    val isReportMaxLimitReached by remember(uiState.optionsReport) { derivedStateOf { uiState.optionsReport.selectedOptions.size == 3 } }
    val isReportButtonEnabled by remember(uiState.optionsReport) { derivedStateOf { uiState.optionsReport.selectedOptions.isNotEmpty() } }

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
    CyclistanceTheme(darkTheme = true) {
        ReportAccountContent(
            message = TextFieldValue(),
            uiState = ReportAccountUiState(),
            event = { event ->
                when (event) {
                    is ReportAccountUiEvent.CloseReportAccountScreen -> {}
                    is ReportAccountUiEvent.ReportAccount -> {}
                    is ReportAccountUiEvent.OnReasonChecked -> onReasonChecked(event.reason)
                    is ReportAccountUiEvent.OnChangeMessage -> onChangeComment(event.message)
                    ReportAccountUiEvent.DismissAlertDialog -> dismissAlertDialog()
                }
            },
            state = ReportAccountState(),

            )
    }
}