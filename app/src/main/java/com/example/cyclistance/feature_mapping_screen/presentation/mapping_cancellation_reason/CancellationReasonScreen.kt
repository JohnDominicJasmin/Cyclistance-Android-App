package com.example.cyclistance.feature_mapping_screen.presentation.mapping_cancellation_reason

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.cyclistance.core.utils.constants.MappingConstants
import com.example.cyclistance.core.utils.constants.MappingConstants.SELECTION_RESCUER_TYPE
import com.example.cyclistance.feature_mapping_screen.presentation.common.AdditionalMessage
import com.example.cyclistance.feature_mapping_screen.presentation.common.MappingButtonNavigation
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_cancellation_reason.components.RadioButtonsSection
import com.example.cyclistance.theme.CyclistanceTheme


@Composable
fun CancellationReasonScreen(
    cancellationType: String = MappingConstants.SELECTION_RESCUEE_TYPE,
    paddingValues: PaddingValues) {

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colors.background)) {

        val (radioButtonsSection, additionalMessageSection, buttonNavButtonSection) = createRefs()

        RadioButtonsSection(modifier = Modifier
            .constrainAs(radioButtonsSection) {
                top.linkTo(parent.top, margin = 15.dp)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
                width = Dimension.fillToConstraints
                height = Dimension.wrapContent
            }, cancellationType = cancellationType)

        AdditionalMessage(
            text = "Additional comments:",
            modifier = Modifier
                .constrainAs(additionalMessageSection) {
                    top.linkTo(radioButtonsSection.bottom, margin = 15.dp)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                    height = Dimension.percent(0.30f)
                    width = Dimension.percent(0.9f)

                },
            message = "My bike is already fixed.",
            onValueChange = {
            },
            enabled = true
        )


        MappingButtonNavigation(
            modifier = Modifier
                .constrainAs(buttonNavButtonSection) {
                    top.linkTo(additionalMessageSection.bottom, margin = 50.dp)
                    bottom.linkTo(parent.bottom, margin = 5.dp)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                    height = Dimension.percent(0.1f)
                    width = Dimension.percent(0.9f)
                },
            onClickCancelButton = {
            },
            onClickConfirmButton = {
            },
            negativeButtonEnabled = true,
            positiveButtonEnabled = true)


    }
}

@Preview
@Composable
fun CancellationReasonPreview() {
    CyclistanceTheme(true) {
        CancellationReasonScreen(
            paddingValues = PaddingValues(),
            cancellationType = SELECTION_RESCUER_TYPE)
    }
}



