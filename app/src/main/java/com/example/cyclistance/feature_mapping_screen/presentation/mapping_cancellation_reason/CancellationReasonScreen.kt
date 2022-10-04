package com.example.cyclistance.feature_mapping_screen.presentation.mapping_cancellation.components.jb

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.cyclistance.feature_mapping_screen.presentation.common.AdditionalMessage
import com.example.cyclistance.feature_mapping_screen.presentation.common.MappingButtonNavigation
import com.example.cyclistance.theme.CyclistanceTheme


@Composable
fun CancellationReasonScreen(paddingValues: PaddingValues) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colors.background)) {

        val (radioButtonsSection, additionalMessageSection, buttonNavButtonSection) = createRefs()

        RadioButtonsSection(modifier = Modifier
            .fillMaxSize()
            .constrainAs(radioButtonsSection) {
                top.linkTo(parent.top, margin = 15.dp)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
                width = Dimension.fillToConstraints
                height = Dimension.wrapContent
            })

        AdditionalMessage(
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
                //todo: implement later
            },
            enabled = true // todo: change this later
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
                //todo: implement later
            },
            onClickConfirmButton = {
                //todo: implement later
            },
            negativeButtonEnabled = true,
            positiveButtonEnabled = true)


    }
}

@Preview
@Composable
fun CancellationReasonPreview() {
    CyclistanceTheme(true) {
        CancellationReasonScreen(paddingValues = PaddingValues())
    }
}



