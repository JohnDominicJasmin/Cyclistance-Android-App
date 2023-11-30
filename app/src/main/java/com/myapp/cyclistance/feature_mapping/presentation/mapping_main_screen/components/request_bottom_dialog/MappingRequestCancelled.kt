package com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.request_bottom_dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.myapp.cyclistance.R
import com.myapp.cyclistance.feature_mapping.domain.model.ui.rescue.CancelledRescueModel
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.bottom_sheet.OutlinedActionButton
import com.myapp.cyclistance.theme.Black440
import com.myapp.cyclistance.theme.CyclistanceTheme
import com.myapp.cyclistance.theme.Red610


@Composable
fun MappingRequestCancelled(
    modifier: Modifier = Modifier,
    cancelledRescueModel: CancelledRescueModel = CancelledRescueModel(),
    onClickOkButton: () -> Unit,
    onDismiss: () -> Unit,

    ) {

    var dialogOpen by rememberSaveable { mutableStateOf(true) }
    if (dialogOpen) {
        Dialog(
            onDismissRequest = {
                onDismiss()
                dialogOpen = false
            },
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
                MappingRequestCancelledContent(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth(),
                    cancelledRescueModel = cancelledRescueModel,
                    onClickOkButton = {
                        onClickOkButton()
                        onDismiss()
                    })

            }
        }
    }
}

@Composable
private fun MappingRequestCancelledContent(
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
            modifier = Modifier
                .padding(bottom = 10.dp)
                .fillMaxWidth(0.94f),
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
            style = MaterialTheme.typography.subtitle1)

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
                style = MaterialTheme.typography.h6,
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
        horizontalArrangement = Arrangement.spacedBy(
            18.dp,
            alignment = Alignment.CenterHorizontally)) {

        Text(
            text = itemTitle,
            style = MaterialTheme.typography.subtitle2.copy(
                fontWeight = FontWeight.Normal,
                color = Black440,
                textAlign = TextAlign.Start
            ),

            )

        Spacer(modifier = Modifier.weight(0.1f))

        Text(
            text = itemValue,
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.subtitle2.copy(
                color = MaterialTheme.colors.onSurface,
            )
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

@Preview(name = "MappingCancelledRescue", device = "id:Galaxy Nexus", widthDp = 400)
@Composable
private fun PreviewMappingCancelledRescue() {
    CyclistanceTheme(true) {
        MappingRequestCancelled(
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