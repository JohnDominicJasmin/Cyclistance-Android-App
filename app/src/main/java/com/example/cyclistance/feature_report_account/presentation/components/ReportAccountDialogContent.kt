package com.example.cyclistance.feature_report_account.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.cyclistance.R
import com.example.cyclistance.feature_mapping.presentation.common.AdditionalMessage
import com.example.cyclistance.feature_mapping.presentation.common.ButtonNavigation
import com.example.cyclistance.feature_report_account.presentation.event.ReportAccountUiEvent
import com.example.cyclistance.feature_report_account.presentation.state.ReportAccountUiState
import com.example.cyclistance.theme.Black500
import com.example.cyclistance.theme.CyclistanceTheme
import com.example.cyclistance.top_bars.TitleTopAppBar
import com.example.cyclistance.top_bars.TopAppBarCreator


private val reasonsToReportAccount = listOf(
    "Misleading or Scam",
    "Verbal Abuse",
    "Sexualizing",
    "Negative behavior",
    "Pretending to be someone else",
    "Violence",
)

@Composable
fun ReportAccountDialogContent(
    modifier: Modifier = Modifier,
    message: TextFieldValue,
    uiState: ReportAccountUiState,
    event: (ReportAccountUiEvent) -> Unit
) {

    Scaffold(modifier = modifier, topBar = {
        TopAppBarCreator(
            icon = Icons.Default.Close,
            onClickIcon = { },
            topAppBarTitle = {
                TitleTopAppBar(title = "Report Account")
            })

    }) { paddingValues ->

        Surface(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {


            AnimatedVisibility(
                visible = !uiState.shouldShowReportFeedback,
                enter = fadeIn(
                    animationSpec = tween(
                        durationMillis = 1200,
                        delayMillis = 250,
                        easing = FastOutSlowInEasing
                    )
                ),
                exit = fadeOut(),
            ) {
                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState())
                        .fillMaxWidth(0.95f)
                        .background(MaterialTheme.colors.background)
                ) {

                    val (image, text, divider, selections, comment, buttonNavButtonSection) = createRefs()

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
                            val isChecked by remember(uiState.accountReport) { derivedStateOf { reason in uiState.accountReport.selectedOptions } }
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

                        onClickNegativeButton = { event(ReportAccountUiEvent.DismissReportAccountDialog) },
                        onClickPositiveButton = { event(ReportAccountUiEvent.ShowReportFeedback) },
                        positiveButtonEnabled = uiState.isReportButtonEnabled
                    )
                }
            }


            AnimatedVisibility(
                visible = uiState.shouldShowReportFeedback,
                enter = fadeIn(
                    animationSpec = tween(
                        durationMillis = 1200,
                        delayMillis = 250,
                        easing = FastOutSlowInEasing
                    )
                ),
                exit = fadeOut(),
            ) {
                ReportAccountFeedback(
                    modifier = Modifier
                        .fillMaxSize(),
                    photo = uiState.reportedPhoto, name = uiState.reportedName,
                    onClickOkayButton = { event(ReportAccountUiEvent.DismissReportAccountDialog) }
                )

            }

        }

    }
}


@Preview(device = "id:Galaxy Nexus")
@Composable
private fun PreviewReportAccountDialogContent() {
    CyclistanceTheme(darkTheme = true) {
        ReportAccountDialogContent(
            message = TextFieldValue(),
            uiState = ReportAccountUiState(),
            event = {})
    }
}