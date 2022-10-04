package com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cyclistance.R

@Composable
fun TopAppBarCreator(
    icon: ImageVector,
    onClickIcon: () -> Unit,
    topAppBarTitle: @Composable () -> Unit) {


    TopAppBar(
        elevation = 10.dp,
        title = topAppBarTitle,
        backgroundColor = MaterialTheme.colors.background,
        navigationIcon = {
            IconButton(onClick = onClickIcon) {
                Icon(
                    icon,
                    contentDescription = "Top App Bar Icon",
                    tint = MaterialTheme.colors.onBackground)
            }
        })

}


@Composable
fun TitleTopAppBar(
    title: String,
    confirmationText: String? = null,
    onClickConfirmation: () -> Unit = {}) {

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = title,
            color = MaterialTheme.colors.onBackground,
            fontWeight = FontWeight.Medium,
            fontSize = 20.sp)

        Spacer(modifier = Modifier.weight(0.1f))
        confirmationText?.let {
            TextButton(onClick = onClickConfirmation, modifier = Modifier.padding(end = 4.dp)) {
                Text(
                    text = "SAVE",
                    color = MaterialTheme.colors.onBackground,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp)
            }
        }
    }
}

@Preview
@Composable
fun TopAppBarPreview() {
    TitleTopAppBar(title = "Title")
}


@Composable
fun DefaultTopBar(onClickIcon: () -> Unit) {
    TopAppBarCreator(
        icon = Icons.Filled.Menu,
        onClickIcon = onClickIcon,
        topAppBarTitle = {
            Row(
                modifier = Modifier.wrapContentSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)) {

                Image(
                    painter = painterResource(id = R.drawable.ic_app_icon_cyclistance),
                    contentDescription = "Image Icon",
                    alignment = Alignment.Center,
                    modifier = Modifier
                        .size(30.dp)
                        .align(alignment = Alignment.CenterVertically)
                        .padding(top = 5.dp)
                )
                Text(
                    "Cyclistance",
                    color = MaterialTheme.colors.onBackground,
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp)
            }
        })

}