package com.example.cyclistance.feature_main_screen.presentation.mapping_confirm_details.components.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.cyclistance.feature_main_screen.presentation.common.MappingAdditionalMessage
import com.example.cyclistance.feature_main_screen.presentation.common.MappingButtonNavigation

import com.example.cyclistance.theme.*





@Preview
@Composable
fun ConfirmationDetailsScreen() {

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(BackgroundColor)) {

        val (addressTextField, bikeTypeDropDownList, buttonDescriptionSection, additionalMessageSection, buttonNavButtonSection) = createRefs()



        AddressTextField(
            modifier = Modifier
                .shadow(2.dp, shape = RoundedCornerShape(12.dp), clip = true)
                .constrainAs(addressTextField) {
                    top.linkTo(parent.top, margin = 15.dp)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                    width = Dimension.percent(0.9f)
                    height = Dimension.wrapContent
                })


        DropDownBikeList(modifier = Modifier
            .shadow(2.dp, shape = RoundedCornerShape(12.dp), clip = true)
            .constrainAs(bikeTypeDropDownList) {
                top.linkTo(addressTextField.bottom, margin = 10.dp)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
                width = Dimension.percent(0.9f)
                height = Dimension.wrapContent
            })

        ButtonDescriptionDetails(
            modifier = Modifier
                .wrapContentHeight()
                .constrainAs(buttonDescriptionSection) {
                    top.linkTo(bikeTypeDropDownList.bottom, margin = 5.dp)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                    height = Dimension.wrapContent
                    width = Dimension.percent(0.9f)
                })

        MappingAdditionalMessage(
            modifier = Modifier
                .constrainAs(additionalMessageSection) {
                    top.linkTo(buttonDescriptionSection.bottom, margin = 15.dp)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                    height = Dimension.percent(0.25f)
                    width = Dimension.percent(0.9f)

                }
        )

        MappingButtonNavigation(
            modifier = Modifier
                .constrainAs(buttonNavButtonSection) {
                    top.linkTo(additionalMessageSection.bottom, margin = 50.dp)
                    bottom.linkTo(parent.bottom, margin = 12.dp)
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






