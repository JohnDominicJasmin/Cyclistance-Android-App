package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cyclistance.R
import com.example.cyclistance.theme.TopAppBarBackgroundColor

@Composable
fun TopAppBarCreator(
    icon: ImageVector,
    onClickIcon: () -> Unit,
    topAppBarTitle: @Composable () -> Unit) {


    TopAppBar(
        elevation = 10.dp,
        title = { topAppBarTitle() },
        backgroundColor = TopAppBarBackgroundColor,
        navigationIcon = {
            IconButton(onClick = onClickIcon) {
                Icon(icon, contentDescription = "", tint = Color.White)
            }
        })

}


@Composable
fun DefaultTitleTopAppBar() {

    Row(
        modifier = Modifier.wrapContentSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)) {

        Image(
            painter = painterResource(id = R.drawable.ic_cyclistance_app_icon),
            contentDescription = "Image Icon",
            alignment = Alignment.Center,
            modifier = Modifier
                .size(30.dp)
                .align(alignment = Alignment.CenterVertically)
                .padding(top = 5.dp)
        )
        Text(
            "Cyclistance",
            color = Color.White,
            fontWeight = FontWeight.Medium,
            fontSize = 20.sp)
    }

}

@Composable
fun DetailsTitleTopAppBar(text: String) {
    Row(
        modifier = Modifier.wrapContentSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)) {

        Text(
            text = text,
            color = Color.White,
            fontWeight = FontWeight.Medium,
            fontSize = 20.sp)
    }
}