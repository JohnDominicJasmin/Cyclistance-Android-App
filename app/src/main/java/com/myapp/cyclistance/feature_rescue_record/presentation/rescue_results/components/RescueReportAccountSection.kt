package com.myapp.cyclistance.feature_rescue_record.presentation.rescue_results.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Report
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.myapp.cyclistance.R
import com.myapp.cyclistance.theme.Black500
import com.myapp.cyclistance.theme.CyclistanceTheme

@Composable
fun RescueReportAccountSection(
    modifier: Modifier = Modifier,
    photoUrl: String,
    name: String,
    onClickReportAccount: () -> Unit,
    viewProfile:() -> Unit){

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally) {

        Card(
            modifier = modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colors.background,
                contentColor = MaterialTheme.colors.onBackground
            ),
            border = BorderStroke(width = 1.dp, color = Black500)) {

            Row(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .padding(start = 16.dp, end = 8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically) {

                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(photoUrl)
                        .crossfade(true)
                        .networkCachePolicy(CachePolicy.ENABLED)
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .memoryCachePolicy(CachePolicy.ENABLED)
                        .build(),
                    alignment = Alignment.Center,
                    contentDescription = "User Profile Image",
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .clip(CircleShape)
                        .clickable { viewProfile() }
                        .size(50.dp),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.ic_empty_profile_placeholder_large),
                    error = painterResource(id = R.drawable.ic_empty_profile_placeholder_large),
                    fallback = painterResource(id = R.drawable.ic_empty_profile_placeholder_large))

                Text(
                    text = name,
                    color = MaterialTheme.colors.onBackground,
                    style = MaterialTheme.typography.subtitle1,
                    modifier = Modifier.padding(all = 4.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                IconButton(onClick = onClickReportAccount) {
                    Icon(
                        imageVector = Icons.Default.Report,
                        contentDescription = "Report Account",
                        tint = Black500)
                }
            }
        }

        Text(
            text = "Would you like to rate your rescuer?",
            color = MaterialTheme.colors.onBackground,
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
fun PreviewRescueReportAccountSectionDark() {
    CyclistanceTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .wrapContentSize()
                .padding(all = 12.dp)
                .background(MaterialTheme.colors.background)) {

            RescueReportAccountSection(
                photoUrl = "https://www.diethelmtravel.com/wp-content/uploads/2016/04/bill-gates-wealthiest-person.jpg",
                name = "John Doe", onClickReportAccount = {}, viewProfile = {})
        }
    }
}


