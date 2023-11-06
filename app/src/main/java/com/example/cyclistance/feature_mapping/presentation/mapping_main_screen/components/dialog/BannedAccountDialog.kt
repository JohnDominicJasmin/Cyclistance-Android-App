package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.cyclistance.R
import com.example.cyclistance.core.utils.date.DateUtils.toReadableDateTime
import com.example.cyclistance.theme.CyclistanceTheme
import java.util.Date

@Composable
fun BannedAccountDialog(
    modifier: Modifier = Modifier,
    period: String,
    reason: String,
    onDismissRequest: () -> Unit) {

    val (isDialogOpen, onDialogVisibilityToggle) = rememberSaveable { mutableStateOf(true) }

    if (isDialogOpen) {

        Dialog(
            onDismissRequest = {
                onDismissRequest()
                onDialogVisibilityToggle(false)
            }, properties = DialogProperties(
                usePlatformDefaultWidth = true,
            )) {

            Column(
                modifier = modifier
                    .clip(RoundedCornerShape(12.dp))
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.surface)
                    ,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    modifier = Modifier.padding(top = 16.dp),
                    painter = painterResource(id = R.drawable.ic_warning),
                    contentDescription = "Warning icon",
                )

                Column(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .padding(top = 12.dp), verticalArrangement = Arrangement.SpaceBetween) {

                    Text(
                        text = "Your account has been suspended due to violations of our terms of service. Contact support if you believe this is an error.",
                        modifier = Modifier
                            .fillMaxWidth(),
                        color = MaterialTheme.colors.onSurface,
                        style = MaterialTheme.typography.body1.copy(fontSize = MaterialTheme.typography.body2.fontSize),
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        text = "Ban period: $period",
                        color = MaterialTheme.colors.onSurface,
                        style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.SemiBold, fontSize = MaterialTheme.typography.body2.fontSize))

                    Text(
                        text = "Reason for Ban: $reason",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        color = MaterialTheme.colors.onSurface,
                        style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.SemiBold, fontSize = MaterialTheme.typography.body2.fontSize))

                }

                Button(
                    onClick = onDismissRequest,
                    colors = ButtonDefaults.buttonColors(),
                    modifier = Modifier.fillMaxWidth(0.6f).padding(vertical = 24.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Confirm",
                        color = MaterialTheme.colors.onPrimary,
                        style = MaterialTheme.typography.button,
                        modifier = Modifier.padding(vertical = 2.dp))
                }

            }
        }
    }
}

@Preview
@Composable
fun PreviewBannedAccountDialog() {
    CyclistanceTheme(darkTheme = true) {
        BannedAccountDialog(
            period = Date().toReadableDateTime(pattern = "yyyy-MM-dd"),
            reason = "Lorem ipsum dolor sit amet consectetur adipisicing elit. ",
            onDismissRequest = { /*TODO*/ })
    }
}