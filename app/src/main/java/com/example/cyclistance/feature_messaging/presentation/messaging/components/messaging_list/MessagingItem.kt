package com.example.cyclistance.feature_messaging.presentation.messaging.components.messaging_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.cyclistance.R
import com.example.cyclistance.core.utils.constants.MappingConstants
import com.example.cyclistance.core.utils.validation.FormatterUtils.toReadableDateTime
import com.example.cyclistance.feature_messaging.domain.model.ui.list_messages.ChatItemModel
import com.example.cyclistance.theme.CyclistanceTheme
import java.util.Date

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MessagingItem(
    modifier: Modifier = Modifier,
    chatItemModel: ChatItemModel = ChatItemModel(),
    onClick: (ChatItemModel) -> Unit) {


    val hasUnreadMessage by remember {
        derivedStateOf {
            chatItemModel.unreadMessages > 0
        }
    }

    Surface(
        onClick = { onClick(chatItemModel) },
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colors.background, shape = RoundedCornerShape(4.dp)) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                4.dp,
                alignment = Alignment.CenterHorizontally)) {

            Box(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .padding(end = 6.dp),
                contentAlignment = Alignment.CenterStart) {

                AsyncImage(
                    model = chatItemModel.userPhotoUrl,
                    contentDescription = "User Profile Image",
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(54.dp),
                    contentScale = ContentScale.Crop,
                    placeholder = rememberAsyncImagePainter(model = R.drawable.ic_empty_profile_placeholder_large),
                    error = painterResource(id = R.drawable.ic_empty_profile_placeholder_large),
                    fallback = painterResource(id = R.drawable.ic_empty_profile_placeholder_large)
                )
            }

            Column(
                modifier = Modifier.weight(0.8f),
                verticalArrangement = Arrangement.spacedBy(3.dp),
                horizontalAlignment = Alignment.Start) {

                Text(
                    text = chatItemModel.name,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colors.onBackground, maxLines = 1,
                )

                Text(
                    text = chatItemModel.message,
                    overflow = TextOverflow.Ellipsis,
                    color = if (hasUnreadMessage) MaterialTheme.colors.primary.copy(
                        alpha = 0.7f) else MaterialTheme.colors.onBackground,
                    maxLines = 1,
                )

            }

            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement.spacedBy(
                    3.dp,
                    alignment = Alignment.CenterVertically),
                horizontalAlignment = Alignment.End) {

                Text(
                    text = chatItemModel.timeStamp!!.toReadableDateTime(pattern = "hh:mm a"),
                    color = MaterialTheme.colors.onBackground,
                    style = MaterialTheme.typography.body2.copy(
                        fontWeight = FontWeight.Light,
                        letterSpacing = TextUnit(0.7f, type = TextUnitType.Sp)))

                if (hasUnreadMessage) {

                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(color = MaterialTheme.colors.primary)
                            .wrapContentSize(),
                        contentAlignment = Alignment.Center
                    ) {

                        val unreadMessageCount =
                            if (chatItemModel.unreadMessages > 9) "9+" else chatItemModel.unreadMessages.toString()

                        Text(
                            text = unreadMessageCount,
                            color = MaterialTheme.colors.onPrimary,
                            modifier = Modifier
                                .wrapContentSize(),
                            style = MaterialTheme.typography.caption
                        )
                    }

                } else {

                    val icon =
                        if (chatItemModel.isMessageSent) Icons.Default.Check else Icons.Default.Info
                    val iconTint =
                        if (chatItemModel.isMessageSent) MaterialTheme.colors.onBackground else MaterialTheme.colors.error

                    Icon(
                        imageVector = icon,
                        contentDescription = "Message Read",
                        tint = iconTint,
                    )
                }
            }
        }
    }
}

@Preview(name = "Messaging Item ", showBackground = false)
@Composable
fun PreviewMessagingItemDark() {
    CyclistanceTheme(darkTheme = true) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colors.background),
            contentAlignment = Alignment.TopCenter) {

            MessagingItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                chatItemModel = ChatItemModel(
                    userPhotoUrl = MappingConstants.IMAGE_PLACEHOLDER_URL,
                    name = "John Doe",
                    message = "Lorem ipsum dolor sit amet consectetur adipisicing elit. Maxime mollitia,\n" +
                              "molestiae quas vel sint commodi repudiandae consequuntur voluptatum laborum\n" +
                              "numquam blanditiis harum quisquam eius sed odit fugiat iusto fuga praesentium\n" +
                              "optio, eaque rerum! Provident similique accusantium nemo autem.",
                    timeStamp = Date(),
                    unreadMessages = 4,
                ),
                onClick = {}

            )

        }
    }
}

@Preview(showBackground = false)
@Composable
fun PreviewMessagingItemLight() {
    CyclistanceTheme(darkTheme = false) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colors.background),
            contentAlignment = Alignment.TopCenter) {

            MessagingItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                chatItemModel = ChatItemModel(
                    userPhotoUrl = MappingConstants.IMAGE_PLACEHOLDER_URL,
                    name = "John Doe",
                    message = "Lorem ipsum dolor sit amet consectetur adipisicing elit. Maxime mollitia,\n" +
                              "molestiae quas vel sint commodi repudiandae consequuntur voluptatum laborum\n" +
                              "numquam blanditiis harum quisquam eius sed odit fugiat iusto fuga praesentium\n" +
                              "optio, eaque rerum! Provident similique accusantium nemo autem.",
                    timeStamp = Date(),
                    unreadMessages = 0,
                    isMessageSent = false
                ),
                onClick = {}
            )
        }
    }

}

