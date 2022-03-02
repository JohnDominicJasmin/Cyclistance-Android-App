package com.example.cyclistance.feature_mapping.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cyclistance.R
import com.example.cyclistance.feature_authentication.presentation.theme.TopAppBarBackgroundColor
import kotlinx.coroutines.launch


@Composable
fun SetupTopAppBar(scaffoldState: ScaffoldState) {
    val coroutineScope = rememberCoroutineScope()
    TopAppBar(
        elevation = 10.dp,
        title = {
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
        },
        backgroundColor = TopAppBarBackgroundColor,
        navigationIcon = {
            IconButton(onClick = { coroutineScope.launch { scaffoldState.drawerState.open() } }
            ) {
                Icon(Icons.Filled.Menu, contentDescription = "Menu Icon", tint = Color.White)
            }
        })

}
