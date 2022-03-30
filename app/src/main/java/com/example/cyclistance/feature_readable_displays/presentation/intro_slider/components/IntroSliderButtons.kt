package com.example.cyclistance.feature_readable_displays.presentation.intro_slider.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
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
import com.example.cyclistance.feature_authentication.presentation.theme.ThemeColor
import com.google.accompanist.pager.ExperimentalPagerApi





@OptIn(ExperimentalPagerApi::class)
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
                .shadow(1.dp, shape = RoundedCornerShape(12.dp), clip = true),
            colors = ButtonDefaults.buttonColors(backgroundColor = ThemeColor),
            shape = RoundedCornerShape(12.dp)) {
            Text(
                text = text,
                color = Color.Black,
                fontSize = 16.sp,
                textAlign = TextAlign.Center

            )
        }



        TextButton(
            onClick = { onClickSkipButton() },
            modifier = Modifier
                .wrapContentSize()) {
            Text(
                text = "Skip", color = Color.White, textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium)
        }


    }
}




