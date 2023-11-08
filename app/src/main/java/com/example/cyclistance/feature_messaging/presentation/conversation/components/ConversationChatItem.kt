package com.example.cyclistance.feature_messaging.presentation.conversation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cyclistance.R
import com.example.cyclistance.core.domain.model.UserDetails
import com.example.cyclistance.core.utils.date.DateUtils.toReadableDateTime
import com.example.cyclistance.feature_messaging.domain.model.ui.chats.MessagingUserItemModel
import com.example.cyclistance.feature_messaging.domain.model.ui.conversation.ConversationItemModel
import com.example.cyclistance.feature_messaging.presentation.common.MessageUserImage
import com.example.cyclistance.feature_messaging.presentation.conversation.state.ConversationState
import com.example.cyclistance.theme.CyclistanceTheme
import java.util.Date

@Composable
fun ChatItem(
    modifier: Modifier = Modifier,
    isInternetAvailable: Boolean,
    conversation: ConversationItemModel,
    isSender: Boolean,
    state: ConversationState,
    currentIndex: Int? = null,
    selectedIndex: Int? = null,
    onSelectChatMessage: (Int) -> Unit,
    resendMessage: () -> Unit,
    markAsSeen: (messageId: String) -> Unit,
    contentAlignment: Alignment = Alignment.Center,
) {

    val timeStampAvailable by remember {
        derivedStateOf {
            conversation.messageDuration == null
        }
    }


    val shouldShowNotSentIndicator = remember(conversation.isSent, isSender) {
        conversation.isSent.not().and(!isSender)
    }

    val isSelected = remember(selectedIndex, currentIndex) { selectedIndex == currentIndex }
    val contentColor =
        if (isSender) MaterialTheme.colors.onSurface else MaterialTheme.colors.onPrimary
    val backgroundColor =
        if (isSender) MaterialTheme.colors.surface else MaterialTheme.colors.primaryVariant


    LaunchedEffect(key1 = conversation) {
        if (!conversation.isSeen && isSender) {
            markAsSeen(conversation.messageId)
        }
    }

    Box(modifier = modifier.fillMaxWidth(), contentAlignment = contentAlignment) {


        Column(
            horizontalAlignment = if (isSender) Alignment.Start else Alignment.End,
            modifier = Modifier.fillMaxWidth()) {


            AnimatedVisibility(
                visible = timeStampAvailable.and(isSelected),
                enter = fadeIn() + expandVertically(animationSpec = tween(durationMillis = 320)),
                exit = fadeOut() + shrinkVertically(animationSpec = tween(durationMillis = 300)),
                modifier = Modifier.fillMaxWidth()) {


                Text(
                    text = conversation.timestamp!!,
                    color = MaterialTheme.colors.onBackground,
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(vertical = 6.dp),
                    style = MaterialTheme.typography.caption.copy(
                        textAlign = TextAlign.Start))
            }



            Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center) {

            if (isSender) {
                MessageUserImage(
                    modifier = Modifier
                        .padding(bottom = 4.dp, end = 4.dp)
                        .clip(CircleShape)
                        .size(36.dp),
                    photoUrl = state.userReceiverMessage?.getPhoto(),
                )
            }





                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center) {


                    Box(
                        modifier = Modifier
                            .wrapContentHeight()
                            .widthIn(max = 270.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                currentIndex?.let { onSelectChatMessage(it) }
                            }
                            .then(
                                if (isSelected) Modifier.background(backgroundColor) else Modifier.background(
                                    backgroundColor.copy(alpha = 0.8f)))) {

                        Column(
                            modifier = Modifier.wrapContentSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.End) {

                            Text(
                                text = conversation.message,
                                modifier = Modifier
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                style = MaterialTheme.typography.body1.copy(
                                    color = contentColor,
                                    textAlign = TextAlign.Start),
                            )


                            AnimatedVisibility(
                                visible = !isSender,
                                enter = fadeIn(),
                                exit = fadeOut()) {

                                Icon(
                                    painter = painterResource(if (conversation.isSeen) R.drawable.ic_seen else R.drawable.ic_not_seen),
                                    contentDescription = "Arrow Forward",
                                    tint = contentColor,
                                    modifier = Modifier
                                        .padding(end = 4.dp, bottom = 8.dp)
                                        .size(16.dp))
                            }



                        }
                    }

                    AnimatedVisibility(
                        visible = shouldShowNotSentIndicator,
                        modifier = Modifier.padding(top = 8.dp)) {
                        NotSentIndicator(resendMessage = {
                            resendMessage()
                        })
                    }
                }

            }

            }
        }
    }


@Preview
@Composable
fun PreviewChatItemSenderDark() {

    CyclistanceTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colors.background)
                .fillMaxSize()) {
            ChatItem(
                isSender = true,
                state = ConversationState(
                    userReceiverMessage = MessagingUserItemModel(userDetails = UserDetails(), fcmToken = "")
                ),
                isInternetAvailable = true,
                conversation = ConversationItemModel(
                    senderId = "1",
                    message = "Lorem ipsum dolor sit amet consectetur adipisicing elit. Maxime mollitia,\n" +
                              "molestiae quas vel sint commodi repudiandae consequuntur",
                    receiverId = "2",
                    timestamp = Date().toReadableDateTime(pattern = "MMM dd hh:mm a"),
                    messageId = "1",
                    messageDuration = null,
                    isSeen = false,
                    isSent = false

                ), resendMessage = {}, onSelectChatMessage = {}, markAsSeen = {})
        }
    }
}


@Preview
@Composable
fun PreviewChatItemSenderLight() {

    CyclistanceTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colors.background)
                .fillMaxSize()) {
            ChatItem(
                isSender = true,
                state = ConversationState(),
                isInternetAvailable = true,
                conversation = ConversationItemModel(
                    senderId = "1",
                    message = "i",
                    receiverId = "2",
                    timestamp = Date().toReadableDateTime(pattern = "MMM dd hh:mm a"),
                    messageId = "1",
                    isSent = true,
                    isSeen = true,
                    messageDuration = null

                ), onSelectChatMessage = {}, resendMessage = {}, markAsSeen = {}
            )
        }
    }
}


@Preview
@Composable
fun PreviewChatItemRecipientDark() {

    CyclistanceTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colors.background)
                .fillMaxSize(),
        ) {
            ChatItem(
                currentIndex = 1, selectedIndex = 1,
                isSender = false,
                state = ConversationState(

                ),
                isInternetAvailable = true,
                conversation = ConversationItemModel(
                    senderId = "1",
                    message = "Lorem ipsum dolor sit amet consectetur adipisicing elit. Maxime mollitia,\n" +
                              "molestiae quas vel sint commodi repudiandae consequuntur",
                    receiverId = "2",
                    timestamp = Date().toReadableDateTime(pattern = "MMM dd hh:mm a"),
                    messageId = "1",
                    messageDuration = null,
                    isSent = false,
                    isSeen = false
                ), resendMessage = {}, onSelectChatMessage = {}, markAsSeen = {})
        }
    }
}

@Preview
@Composable
fun PreviewChatItemRecipientLight() {

    CyclistanceTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colors.background)
                .fillMaxSize(),
        ) {
            ChatItem(
                isSender = false,
                currentIndex = 1, selectedIndex = 1,
                state = ConversationState(),
                isInternetAvailable = true,
                conversation = ConversationItemModel(
                    senderId = "1",
                    message = "i",
                    receiverId = "2",
                    timestamp = Date().toReadableDateTime(pattern = "MMM dd hh:mm a"),
                    messageId = "1",
                    isSent = true,
                    isSeen = true,
                    messageDuration = null
                ), resendMessage = {}, onSelectChatMessage = {}, markAsSeen = {})
        }
    }
}


