package com.myapp.cyclistance.feature_messaging.presentation.conversation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.myapp.cyclistance.R
import com.myapp.cyclistance.core.utils.composable_utils.noRippleClickable
import com.myapp.cyclistance.theme.CyclistanceTheme

@Composable
fun NotSentIndicator(modifier: Modifier = Modifier, resendMessage : () -> Unit ) {

    Row(
        modifier = modifier.wrapContentSize().noRippleClickable { resendMessage()  },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center) {

        Icon(
            modifier = Modifier.size(18.dp),
            painter = painterResource(id = R.drawable.ic_not_sent),
            contentDescription = "Sent",
            tint = MaterialTheme.colors.error.copy(alpha = 0.7f)
        )

        Text(
            text = "Not Sent",
            color = MaterialTheme.colors.error.copy(alpha = 0.7f),
            style = MaterialTheme.typography.caption,
            modifier = Modifier.padding(start = 4.dp))
    }
}

@Preview
@Composable
fun PreviewNotSentIndicator() {
    CyclistanceTheme(darkTheme = true) {
        Box(modifier = Modifier.background(MaterialTheme.colors.background)){
            NotSentIndicator(resendMessage = {})
        }
    }
}
