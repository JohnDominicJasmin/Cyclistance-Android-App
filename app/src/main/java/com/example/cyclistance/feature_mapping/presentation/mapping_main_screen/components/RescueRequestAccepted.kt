package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.cyclistance.R
import com.example.cyclistance.theme.Black500
import com.example.cyclistance.theme.CyclistanceTheme
import com.example.cyclistance.theme.Green678

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RescueRequestAccepted(
    modifier: Modifier = Modifier,
    onClickOkButton: () -> Unit = {},
    acceptedName:String = "placeholder",
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
            MappingRequestAcceptedContent(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
                onClickOkButton = onClickOkButton,
                acceptedName = acceptedName)
        }
    }
}

@Composable
private fun MappingRequestAcceptedContent(
    modifier: Modifier = Modifier,
    acceptedName:String = "placeholder",
    onClickOkButton: () -> Unit
) {

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
                color = Green678,
                iconId = R.drawable.ic_succcess_circle,
                title = "Request Accepted"
            )

            Text(
                text = "Your request has been accepted by $acceptedName",
                fontWeight = FontWeight.Normal,
                color = Black500,
                fontSize = 14.sp,
            )

            OutlinedActionButton(
                text = "Ok",
                onClick = onClickOkButton,
                modifier = Modifier.width(100.dp))

        }

    }


}

@Preview(name = "RescueRequestAccepted")
@Composable
private fun PreviewRescueRequestAccepted() {
    CyclistanceTheme(true) {
        RescueRequestAccepted(modifier = Modifier.fillMaxSize())
    }
}