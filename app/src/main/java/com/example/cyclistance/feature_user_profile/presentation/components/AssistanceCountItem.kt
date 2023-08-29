package com.example.cyclistance.feature_user_profile.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cyclistance.R
import com.example.cyclistance.theme.CyclistanceTheme

@Composable
fun AssistanceCountItem(modifier: Modifier = Modifier, @DrawableRes iconId: Int, count: Int) {

    Surface(
        modifier = modifier
            .padding(all = 4.dp)
            .wrapContentHeight(align = Alignment.CenterVertically),
        color = MaterialTheme.colors.secondary,
        shape = RoundedCornerShape(12.dp)) {

        Row(
            modifier = Modifier.padding(end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.Start)) {

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .background(MaterialTheme.colors.secondaryVariant, shape = CircleShape)
                    .clip(CircleShape).size(40.dp)) {

                Icon(
                    modifier = Modifier
                        .padding(all = 6.dp)
                        .align(Alignment.Center),
                    painter = painterResource(id = iconId),
                    contentDescription = "Reason display icon",
                    tint = MaterialTheme.colors.onSecondary
                )
            }
            Text(
                modifier = Modifier,
                text = count.toString(),
                color = MaterialTheme.colors.onBackground,
                style = MaterialTheme.typography.subtitle2,

                )
        }
    }

}

@Preview
@Composable
fun PreviewAssistanceCountItem() {
    CyclistanceTheme(darkTheme = true) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            AssistanceCountItem(iconId = R.drawable.ic_broken_chain, count = 5000)
        }
    }
}