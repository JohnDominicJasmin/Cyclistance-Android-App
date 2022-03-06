package com.example.cyclistance.feature_mapping.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import  androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.cyclistance.feature_authentication.presentation.theme.BackgroundColor
import com.example.cyclistance.feature_authentication.presentation.theme.ThemeColor



@Preview
@Composable
fun MappingCancellationReasonScreen() {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(BackgroundColor)) {

        val (radioButtonsSection, additionalMessageSection,buttonNavButtonSection) = createRefs()

    RadioButtonsSection(modifier = Modifier
        .fillMaxSize()
        .constrainAs(radioButtonsSection) {
            top.linkTo(parent.top, margin = 15.dp)
            end.linkTo(parent.end)
            start.linkTo(parent.start)
            width = Dimension.fillToConstraints
            height = Dimension.wrapContent
        })

        SetupAdditionalMessageSection(
            modifier = Modifier
                .constrainAs(additionalMessageSection) {
                    top.linkTo(radioButtonsSection.bottom, margin = 15.dp)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)

                    height = Dimension.percent(0.25f)
                    width = Dimension.percent(0.9f)

                }
        )


        ButtonDialogSection(
            modifier = Modifier
                .constrainAs(buttonNavButtonSection) {
                    top.linkTo(additionalMessageSection.bottom, margin = 15.dp)
                    bottom.linkTo(parent.bottom,margin = 5.dp)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                    height = Dimension.wrapContent
                    width = Dimension.percent(0.9f)
                },
            onClickCancelButton = {

            },
            onClickConfirmButton = {

            })



    }
}

@Composable
fun RadioButtonsSection(modifier : Modifier) {

    val (selectedOption, onOptionSelected) = remember { mutableStateOf(rescuerCancellationReasons[0]) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally) {

        rescuerCancellationReasons.forEach { text ->
                Row(
                    Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .selectable(
                            selected = (text == selectedOption),
                            onClick = { onOptionSelected(text) })
                        , horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {


                        RadioButton(
                            selected = (text == selectedOption),
                            onClick = { onOptionSelected(text) },
                        colors = RadioButtonDefaults.colors(selectedColor = ThemeColor, unselectedColor = Color(0xFFAEAEAE))
                        )
                        Text(
                            text = text,
                            modifier = Modifier.padding(start = 8.dp),
                            color = Color.White
                        )
                }
            }
        }

}


