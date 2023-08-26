package com.example.cyclistance.feature_messaging.presentation.chat.chats.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.example.cyclistance.core.domain.model.UserDetails
import com.example.cyclistance.core.utils.constants.MappingConstants
import com.example.cyclistance.core.utils.formatter.FormatterUtils.toReadableDateTime
import com.example.cyclistance.feature_messaging.domain.model.ui.chats.ChatItemModel
import com.example.cyclistance.feature_messaging.domain.model.ui.chats.MessagingUserItemModel
import com.example.cyclistance.feature_messaging.presentation.common.MessageUserImage
import com.example.cyclistance.theme.CyclistanceTheme
import java.util.Date

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ChatItem(
    isInternetAvailable: Boolean,
    modifier: Modifier = Modifier,
    user: MessagingUserItemModel,
    chatItem: ChatItemModel,
    onClick: (MessagingUserItemModel) -> Unit) {


    Surface(
        onClick = { onClick(user) },
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

                MessageUserImage(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(48.dp),
                    isOnline = if (!isInternetAvailable) null else user.isUserAvailable,
                    photoUrl = user.userDetails.photo
                )
            }

            Column(
                modifier = Modifier.weight(0.8f),
                verticalArrangement = Arrangement.spacedBy(3.dp),
                horizontalAlignment = Alignment.Start) {

                Text(
                    text = user.userDetails.name,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colors.onBackground, maxLines = 1,
                )

                Text(
                    text = chatItem.lastMessage,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colors.onBackground,
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
                    text = chatItem.timeStamp!!.toReadableDateTime(pattern = "hh:mm a"),
                    color = MaterialTheme.colors.onBackground,
                    style = MaterialTheme.typography.body2.copy(
                        fontWeight = FontWeight.Light,
                        letterSpacing = TextUnit(0.7f, type = TextUnitType.Sp)))


            }
        }
    }
}

@Preview(name = "Messaging Item ", showBackground = false)
@Composable
fun PreviewChatItemDark() {
    CyclistanceTheme(darkTheme = true) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colors.background),
            contentAlignment = Alignment.TopCenter) {

            ChatItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                isInternetAvailable = false,
                user = MessagingUserItemModel(
                    userDetails = UserDetails(
                        name = "John Doe",
                        photo = MappingConstants.IMAGE_PLACEHOLDER_URL
                    )
                ),
                chatItem = ChatItemModel(

                    lastMessage = "Lorem ipsum dolor sit amet consectetur adipisicing elit. Maxime mollitia,\n" +
                                  "molestiae quas vel sint commodi repudiandae consequuntur voluptatum laborum\n" +
                                  "numquam blanditiis harum quisquam eius sed odit fugiat iusto fuga praesentium\n" +
                                  "optio, eaque rerum! Provident similique accusantium nemo autem.",
                    timeStamp = Date(),

                    ),
                onClick = {}

            )

        }
    }
}

@Preview(showBackground = false)
@Composable
fun PreviewChatItemLight() {
    CyclistanceTheme(darkTheme = false) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colors.background),
            contentAlignment = Alignment.TopCenter) {

            ChatItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                isInternetAvailable = true,
                user = MessagingUserItemModel(
                    userDetails = UserDetails(
                        name = "John Doe",
                        photo = MappingConstants.IMAGE_PLACEHOLDER_URL
                    )
                ),
                chatItem = ChatItemModel(
                    lastMessage = "Lorem ipsum dolor sit amet consectetur adipisicing elit. Maxime mollitia,\n" +
                                  "molestiae quas vel sint commodi repudiandae consequuntur voluptatum laborum\n" +
                                  "numquam blanditiis harum quisquam eius sed odit fugiat iusto fuga praesentium\n" +
                                  "optio, eaque rerum! Provident similique accusantium nemo autem.",
                    timeStamp = Date(),

                    ),
                onClick = {}
            )
        }
    }

}

