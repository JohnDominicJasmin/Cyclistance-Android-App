package com.example.cyclistance.feature_mapping.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.cyclistance.feature_authentication.presentation.theme.ThemeColor


@Composable
fun ButtonDialogSection(
    modifier: Modifier,
    onClickCancelButton: () -> Unit,
    onClickConfirmButton: () -> Unit) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {

            Button(
                onClick = onClickCancelButton,
                modifier = Modifier
                    .wrapContentHeight()
                    .weight(0.5f)
                    .padding(all = 7.dp)
                    .shadow(7.dp, shape = RoundedCornerShape(12.dp), clip = true),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF8C8C8C)),
                shape = RoundedCornerShape(12.dp)) {
                Text(text = "Dismiss", color = Color.Black, style = MaterialTheme.typography.button)
            }


            Button(
                onClick = onClickConfirmButton,
                modifier = Modifier
                    .wrapContentHeight()
                    .weight(0.5f)
                    .padding(all = 5.dp)
                    .shadow(7.dp, shape = RoundedCornerShape(12.dp), clip = true),
                colors = ButtonDefaults.buttonColors(backgroundColor = ThemeColor),
                shape = RoundedCornerShape(12.dp)) {
                Text(text = "Confirm", color = Color.Black, style = MaterialTheme.typography.button)
            }



        }
    }
}