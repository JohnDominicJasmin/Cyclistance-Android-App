package com.example.cyclistance.feature_readable_displays.presentation.intro_slider.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp





@Composable
fun IntroSliderButtons(text:String,
                       onClickSkipButton: () -> Unit,
                       onClickNextButton: () -> Unit) {


    Column(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .layoutId(IntroSliderConstraintsItem.ButtonSection.layoutId),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {




        Button(
            onClick = onClickNextButton,
            modifier = Modifier
                .height(50.dp)
                .width(220.dp)
                .shadow(7.dp, shape = RoundedCornerShape(12.dp), clip = true),
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
            shape = RoundedCornerShape(12.dp)) {
            Text(
                text = text,
                color = MaterialTheme.colors.onPrimary,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold

            )
        }


        TextButton(
            onClick = { onClickSkipButton() },
            modifier = Modifier
                .wrapContentSize()) {
            Text(
                text = "Skip",
                color = MaterialTheme.colors.onBackground,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium)
        }


    }
}




