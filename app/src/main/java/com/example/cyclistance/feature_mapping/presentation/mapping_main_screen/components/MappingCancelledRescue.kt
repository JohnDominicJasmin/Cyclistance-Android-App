package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.cyclistance.R
import com.example.cyclistance.feature_mapping.domain.model.CancelledRescueModel
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components.bottomSheet.OutlinedActionButton
import com.example.cyclistance.theme.Black440
import com.example.cyclistance.theme.CyclistanceTheme
import com.example.cyclistance.theme.Red610

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MappingCancelledRescue(
    modifier: Modifier = Modifier,
    cancelledRescueModel: CancelledRescueModel = CancelledRescueModel(),
    onClickOkButton: () -> Unit,
    onDismiss: () -> Unit = {},

    ) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = false,
            dismissOnClickOutside = false),
    ) {
        Box(
            modifier = modifier
                .background(
                    color = Color.Transparent,
                )
        ) {
            MappingCancelledRescueContent(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
                cancelledRescueModel = cancelledRescueModel,
                onClickOkButton = onClickOkButton)

        }
    }
}

@Composable
private fun MappingCancelledRescueContent(
    modifier: Modifier = Modifier,
    cancelledRescueModel: CancelledRescueModel = CancelledRescueModel(),
    onClickOkButton: () -> Unit) {

    Box(
        modifier = modifier
            .background(
                color = MaterialTheme.colors.surface,
                shape = RoundedCornerShape(12.dp, 12.dp, 0.dp, 0.dp)),
        contentAlignment = Alignment.TopCenter) {

        Column(
            modifier = Modifier.padding(bottom = 10.dp).fillMaxWidth(0.94f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)) {

            TopBanner(
                modifier = Modifier.padding(top = 10.dp),
                color = Red610,
                iconId = R.drawable.ic_cancel,
                title = "Rescue Cancelled"
            )



                CancellationDetails(cancelledRescueModel)

                OutlinedActionButton(
                    text = "Ok",
                    onClick = onClickOkButton,
                    modifier = Modifier.width(100.dp))

        }
    }
}

@Composable
private fun CancellationDetails(cancelledRescueModel: CancelledRescueModel) {

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        Text(
            modifier = Modifier.padding(top = 12.dp, bottom = 6.dp),
            text = "Cancellation Details",
            color = MaterialTheme.colors.onSurface,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium)

        cancelledRescueModel.transactionID?.let {
            DetailsItem(
                itemTitle = "Transaction ID: ",
                itemValue = cancelledRescueModel.transactionID)
        }

        cancelledRescueModel.rescueCancelledBy?.let {
            DetailsItem(
                itemTitle = "Cancelled by: ",
                itemValue = cancelledRescueModel.rescueCancelledBy)
        }

        cancelledRescueModel.reason?.let {
            DetailsItem(itemTitle = "Reason: ", itemValue = cancelledRescueModel.reason)
        }

        cancelledRescueModel.message?.takeIf { it.isNotEmpty() }?.let {
            DetailsItem(itemTitle = "Message: ", itemValue = cancelledRescueModel.message)
        }


    }
}

@Composable
fun TopBanner(modifier: Modifier, color: Color, iconId: Int, title: String) {
    Surface(

        color = color,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent)
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                10.dp,
                alignment = Alignment.Start),

            ) {


            Spacer(modifier = Modifier.weight(0.01f))

            Icon(
                painter = painterResource(id = iconId),
                contentDescription = "cancel_display",
                tint = Color.White,
                modifier = Modifier
                    .padding(all = 4.dp)
                    .requiredWidthIn(max = 300.dp)
                    .weight(0.23f))

            Spacer(modifier = Modifier.weight(0.1f))

            Text(
                text = title,
                color = Color.White,
                fontSize = 20.sp,
                style = MaterialTheme.typography.subtitle1,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.weight(0.3f))

        }
    }

}

@Composable
private fun DetailsItem(modifier: Modifier = Modifier, itemTitle: String, itemValue: String) {
    Row(
        modifier = modifier.wrapContentHeight(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(18.dp,
            alignment = Alignment.CenterHorizontally)) {

        Text(
            text = itemTitle,
            color = Black440,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            textAlign = TextAlign.Start
        )

        Spacer(modifier = Modifier.weight(0.1f))

        Text(
            text = itemValue,
            color = MaterialTheme.colors.onSurface,
            textAlign = TextAlign.End,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,

            )

    }
}

@Preview
@Composable
private fun PreviewDetailsItem() {
    CyclistanceTheme(true) {
        DetailsItem(
            itemTitle = "Transaction ID:",
            itemValue = "Value Sample",
            modifier = Modifier.fillMaxWidth())
    }
}

@Preview(name = "MappingCancelledRescue", device = Devices.PIXEL_3A, widthDp = 400)
@Composable
private fun PreviewMappingCancelledRescue() {
    CyclistanceTheme(true) {
        MappingCancelledRescue(
            modifier = Modifier.fillMaxSize(),
            cancelledRescueModel = CancelledRescueModel(
                transactionID = "02i4n93j09",
                rescueCancelledBy = "John Doe",
                reason = "Magic Reason",
                "Nothing Gonna changsssssssssssssssssssssssssssssssssssssssssssssse my mind"),
            onDismiss = {},
            onClickOkButton = {})
    }
}