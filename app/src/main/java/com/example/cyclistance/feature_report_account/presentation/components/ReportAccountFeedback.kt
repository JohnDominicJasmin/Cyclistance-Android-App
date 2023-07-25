package com.example.cyclistance.feature_report_account.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.cyclistance.R
import com.example.cyclistance.theme.Black500
import com.example.cyclistance.theme.CyclistanceTheme

@Composable
fun ReportAccountFeedback(
    modifier: Modifier = Modifier,
    photo: String,
    name: String,
    onClickOkayButton: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier.fillMaxWidth()
    ) {


        Image(
            painter = painterResource(id = R.drawable.ic_report_account_feedback),
            contentDescription = "Report Account Feedback",
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Text(
            text = "Thank you for feedback",
            color = MaterialTheme.colors.onBackground,
            style = MaterialTheme.typography.body1,
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                8.dp,
                alignment = Alignment.CenterHorizontally
            ),
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth(0.9f)
        ) {


            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(photo)
                    .crossfade(true)
                    .networkCachePolicy(CachePolicy.ENABLED)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .build(),
                alignment = Alignment.Center,
                contentDescription = "User Profile Image",
                modifier = Modifier
                    .clip(CircleShape)
                    .size(45.dp),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.ic_empty_profile_placeholder_large),
                error = painterResource(id = R.drawable.ic_empty_profile_placeholder_large),
                fallback = painterResource(id = R.drawable.ic_empty_profile_placeholder_large)
            )

            Text(
                text = name,
                color = MaterialTheme.colors.onBackground,
                style = MaterialTheme.typography.body1,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier.weight(0.3f)

            )


        }

        Text(
            text = "The reported account will be validated. The penalty will be implied once the action has been confirmed.",
            color = Black500,
            style = MaterialTheme.typography.body1.copy(textAlign = TextAlign.Center),
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(vertical = 16.dp),
            )

        Button(onClick = onClickOkayButton, shape = RoundedCornerShape(8.dp)) {
            Text(
                text = "Okay",
                color = MaterialTheme.colors.onPrimary,
                style = MaterialTheme.typography.button,
                modifier = Modifier.padding(horizontal = 16.dp),

                )
        }
    }
}

@Preview
@Composable
private fun PreviewReportAccountFeedback() {
    CyclistanceTheme(darkTheme = true) {
        Box(modifier = Modifier.background(MaterialTheme.colors.background)) {
            Box(modifier = Modifier.fillMaxSize()) {
                ReportAccountFeedback(
                    photo = "",
                    name = "John Doe John DoeJohn DoeJohn DoeJohn DoeJohn DoeJohn DoeJohn DoeJohn Doe",
                    onClickOkayButton = {})
            }
        }
    }
}