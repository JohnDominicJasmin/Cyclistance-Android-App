package com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.bottom_sheet.on_going_rescue

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.myapp.cyclistance.R
import com.myapp.cyclistance.theme.Black440
import com.myapp.cyclistance.theme.Red900

@Composable
fun RoundButtonSection(
    modifier: Modifier = Modifier,
    onClickCallButton: () -> Unit,
    onClickChatButton: () -> Unit,
    onClickCancelButton: () -> Unit) {


    Row(
        modifier = modifier
            .background(Color.Transparent)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center) {

        RoundedButtonItem(
            modifier = Modifier.weight(1f),
            backgroundColor = MaterialTheme.colors.secondary,
            contentColor = MaterialTheme.colors.onSecondary,
            imageId = R.drawable.ic_call,
            buttonSubtitle = "Emergency Call", onClick = onClickCallButton)


        RoundedButtonItem(
            modifier = Modifier.weight(1f),
            backgroundColor = MaterialTheme.colors.secondary,
            contentColor = MaterialTheme.colors.onSecondary,
            imageId = R.drawable.ic_chat,
            buttonSubtitle = "Chat", onClick = onClickChatButton)


        RoundedButtonItem(
            modifier = Modifier.weight(1f),
            backgroundColor = Red900,
            contentColor = Color.White,
            imageId = R.drawable.ic_cancel_1,
            buttonSubtitle = "Cancel", onClick = onClickCancelButton)
    }
}

@Composable
private fun RoundedButtonItem(
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    contentColor: Color,
    imageId: Int,
    buttonSubtitle: String,
    onClick: () -> Unit) {

    Column(
        modifier = modifier
            .wrapContentSize()
            .padding(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(
            space = 7.dp,
            alignment = Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally) {

        Button(
            modifier = Modifier
                .size(48.dp)
                .shadow(elevation = 2.dp, shape = CircleShape),
            onClick = onClick,
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = backgroundColor,
                contentColor = contentColor)) {

            Icon(
                painter = painterResource(id = imageId),
                contentDescription = null, modifier = Modifier.fillMaxSize())
        }

        Text(
            text = buttonSubtitle,
            color = Black440,
            style = MaterialTheme.typography.caption,
            textAlign = TextAlign.Center)
    }
}