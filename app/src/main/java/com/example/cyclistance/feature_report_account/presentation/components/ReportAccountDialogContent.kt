package com.example.cyclistance.feature_report_account.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.cyclistance.R
import com.example.cyclistance.feature_mapping.presentation.common.AdditionalMessage
import com.example.cyclistance.feature_mapping.presentation.common.ButtonNavigation
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ReportAccountDialogContent(modifier: Modifier = Modifier, onDismiss: () -> Unit) {

    val selectedOptions = remember { mutableStateListOf<String>() }
    val isReportMaxLimitReached = remember { derivedStateOf { selectedOptions.size == 3 } }
    val isReportButtonEnabled by remember { derivedStateOf { selectedOptions.isNotEmpty() } }

    var messageInput by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(
            TextFieldValue(""))
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = false,
            dismissOnBackPress = true
        )) {


        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = MaterialTheme.colors.background) {


            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally) {


                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.95f)
                        .background(MaterialTheme.colors.background)) {

                    val (image, text, divider, selections, message, buttonNavButtonSection) = createRefs()

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
                            .padding(vertical = 4.dp)
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
                            .fillMaxWidth()
                            .constrainAs(text) {
                                top.linkTo(divider.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)

                            }
                    )

                    Column(modifier = Modifier.constrainAs(selections) {
                        top.linkTo(text.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }) {

                        reasonsToReportAccount.forEach { reason ->
                            val isChecked by remember { derivedStateOf { reason in selectedOptions } }
                            ReportReasonItem(
                                label = reason,
                                isChecked = isChecked,
                                onReasonChecked = {

                                    if (selectedOptions.contains(reason)) {
                                        selectedOptions.remove(reason)
                                        return@ReportReasonItem
                                    }

                                    if (isReportMaxLimitReached.value) {
                                        return@ReportReasonItem
                                    }

                                    selectedOptions.add(reason)

                                })
                        }
                    }

                    AdditionalMessage(
                        modifier = Modifier.constrainAs(message) {
                            top.linkTo(selections.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            height = Dimension.percent(0.23f)
                            width = Dimension.matchParent
                        },
                        message = messageInput,
                        onChangeValueMessage = { messageInput = it },
                        enabled = true
                    )


                    ButtonNavigation(
                        modifier = Modifier.constrainAs(buttonNavButtonSection) {
                            top.linkTo(message.bottom, margin = 30.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom, margin = 30.dp)
                            height = Dimension.wrapContent
                            width = Dimension.percent(0.9f)
                        },

                        onClickCancelButton = onDismiss,
                        onClickConfirmButton = { },
                        positiveButtonEnabled = isReportButtonEnabled
                    )
                }


            }
        }

    }
}

@Preview(device = "id:Galaxy Nexus")
@Composable
private fun PreviewReportAccountDialogContent() {
    CyclistanceTheme(darkTheme = true) {
        ReportAccountDialogContent(onDismiss = {})
    }
}