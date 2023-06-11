package com.example.cyclistance.feature_mapping.presentation.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.cyclistance.theme.Black500


@Composable
fun MappingButtonNavigation(
    modifier: Modifier,
    negativeButtonEnabled: Boolean = true,
    positiveButtonEnabled: Boolean = true,
    negativeButtonText: String = "Cancel",
    positiveButtonText: String = "Confirm",
    onClickCancelButton: () -> Unit,
    onClickConfirmButton: () -> Unit) {

    Column(modifier = modifier) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {

            OutlinedButton(
                enabled = negativeButtonEnabled,
                onClick = onClickCancelButton,
                border = BorderStroke(1.dp, Black500),
                modifier = Modifier
                    .wrapContentHeight()
                    .weight(0.5f)
                    .align(Alignment.CenterVertically)
                    .padding(all = 7.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                shape = RoundedCornerShape(12.dp)) {

                Text(
                    text = negativeButtonText,
                    color = MaterialTheme.colors.onBackground,
                    style = MaterialTheme.typography.button,
                    textAlign = TextAlign.Center)
            }


            Button(
                enabled = positiveButtonEnabled,
                onClick = onClickConfirmButton,
                modifier = Modifier
                    .wrapContentHeight()
                    .weight(0.5f)
                    .align(Alignment.CenterVertically)
                    .padding(all = 5.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
                shape = RoundedCornerShape(12.dp)) {

                Text(
                    text = positiveButtonText,
                    color = MaterialTheme.colors.onPrimary,
                    style = MaterialTheme.typography.button,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )

            }


        }
    }
}