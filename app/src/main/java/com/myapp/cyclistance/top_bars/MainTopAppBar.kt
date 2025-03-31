package com.myapp.cyclistance.top_bars

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.myapp.cyclistance.R
import com.myapp.cyclistance.theme.CyclistanceTheme

@Composable
fun TopAppBarCreator(
    modifier: Modifier = Modifier,
    elevation: Dp = 10.dp,
    icon: ImageVector,
    onClickIcon: () -> Unit,
    topAppBarTitle: @Composable () -> Unit) {

    TopAppBar(
        modifier = modifier,
        elevation = elevation,
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
fun TitleTopAppBar(modifier: Modifier = Modifier, title: String) {
    Text(
        text = title,
        color = MaterialTheme.colors.onBackground,
        style = MaterialTheme.typography.h6,
        modifier = modifier,
        overflow = TextOverflow.Ellipsis)

}


@Composable
fun DefaultTopBar(onClickIcon: () -> Unit = {}) {
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
                TitleTopAppBar(title = "Cyclistance")
            }
        })

}


@Preview
@Composable
fun PreviewDefaultTopBar() {
    CyclistanceTheme {
        DefaultTopBar()
    }
}


@Preview
@Composable
fun TopAppBarPreview() {
    TitleTopAppBar(title = "Title")
}

