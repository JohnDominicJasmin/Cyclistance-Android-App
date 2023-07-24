package com.example.cyclistance.feature_messaging.presentation.chat.chats.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.cyclistance.R
import com.example.cyclistance.feature_messaging.domain.model.ui.chats.ChatItemModel
import com.example.cyclistance.theme.Black500
import com.example.cyclistance.theme.CyclistanceTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ChatSearchItem(modifier: Modifier = Modifier, chatItem: ChatItemModel, onClick: () -> Unit) {

    Surface(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().padding(vertical = 8.dp),
        color = MaterialTheme.colors.background, shape = RoundedCornerShape(4.dp)) {

        Row(
            modifier = Modifier
                .height(IntrinsicSize.Max)
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                4.dp,
                alignment = Alignment.CenterHorizontally)) {


            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(chatItem.userPhotoUrl)
                    .crossfade(true)
                    .networkCachePolicy(CachePolicy.ENABLED)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .build(),
                alignment = Alignment.Center,
                contentDescription = "User Profile Image",
                modifier = Modifier
                    .clip(CircleShape)
                    .size(55.dp),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.ic_empty_profile_placeholder_large),
                error = painterResource(id = R.drawable.ic_empty_profile_placeholder_large),
                fallback = painterResource(id = R.drawable.ic_empty_profile_placeholder_large))

            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start) {


                Text(
                    text = chatItem.name,
                    color = MaterialTheme.colors.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.body1.copy(
                        fontWeight = FontWeight.SemiBold,
                    ),
                    modifier = Modifier.padding(top = 4.dp).padding(start = 12.dp)
                )

                Divider(
                    modifier = Modifier.fillMaxWidth().padding(start = 12.dp),
                    thickness = 0.8.dp,
                    color = Black500
                )
            }


        }

    }
}

@Preview
@Composable
private fun PreviewChatSearchItem() {
    CyclistanceTheme(darkTheme = true) {
        ChatSearchItem(
            chatItem = ChatItemModel(
                messageId = "1",
                name = "John Doe",
                userPhotoUrl = "https://i.pravatar.cc/150?img=3",
                userId = "1",

                ),
            onClick = {}
        )
    }
}