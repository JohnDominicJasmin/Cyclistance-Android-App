package com.example.cyclistance.feature_messaging.presentation.conversation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cyclistance.core.utils.formatter.FormatterUtils.toReadableDateTime
import com.example.cyclistance.feature_messaging.domain.model.ui.conversation.ConversationItemModel
import com.example.cyclistance.feature_messaging.presentation.common.MessageUserImage
import com.example.cyclistance.feature_messaging.presentation.conversation.state.ConversationState
import com.example.cyclistance.theme.CyclistanceTheme
import java.util.Date

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ChatItem(
    modifier: Modifier = Modifier,
    conversation: ConversationItemModel,
    isSender: Boolean,
    state: ConversationState,
    currentIndex: Int? = null,
    selectedIndex: Int? = null,
    onSelectChatMessage: (Int) -> Unit = {},
    contentAlignment: Alignment = Alignment.Center,
) {


    val timeStampAvailable by remember {
        derivedStateOf {
            conversation.messageDuration == null
        }
    }

    val isMessageSent by remember {
        derivedStateOf {
            conversation.timestamp != null
        }
    }

    val shouldShowSentIndicator by remember {
        derivedStateOf {
            isMessageSent.and(!isSender)
        }
    }

    val isSelected = remember(selectedIndex, currentIndex) { selectedIndex == currentIndex }
    val contentColor =
        if (isSender) MaterialTheme.colors.onSurface else MaterialTheme.colors.onPrimary
    val backgroundColor =
        if (isSender) MaterialTheme.colors.surface else MaterialTheme.colors.primaryVariant


    Box(modifier = modifier.fillMaxWidth(), contentAlignment = contentAlignment) {


        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center) {

            if(isSender) {
                MessageUserImage(
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                        .clip(CircleShape)
                        .size(36.dp)
                    ,
                    isOnline = state.userReceiverMessage?.isUserAvailable,
                    photoUrl = state.userReceiverMessage?.getPhoto(),
                )
            }

            Column(
                horizontalAlignment = if (isSender) Alignment.Start else Alignment.End,
                modifier = Modifier.fillMaxWidth()) {


                AnimatedVisibility(
                    visible = timeStampAvailable.and(isSelected),
                    enter = fadeIn() + expandVertically(animationSpec = tween(durationMillis = 320)),
                    exit = fadeOut() + shrinkVertically(animationSpec = tween(durationMillis = 300)),
                    modifier = Modifier.fillMaxWidth()) {


                    Text(
                        text = conversation.timestamp!!.toReadableDateTime(),
                        color = MaterialTheme.colors.onBackground,
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(vertical = 6.dp),
                        style = MaterialTheme.typography.caption.copy(
                            textAlign = TextAlign.Start))
                }



                Card(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .wrapContentSize(),
                    shape = RoundedCornerShape(12.dp),
                    contentColor = if (isSelected) contentColor.copy(alpha = 0.75f) else contentColor,
                    backgroundColor = if (isSelected) backgroundColor else backgroundColor.copy(
                        alpha = 0.8f),
                    elevation = if (isSelected) 2.dp else 0.dp,
                    onClick = { currentIndex?.let { onSelectChatMessage(it) } }) {

                    Text(
                        text = conversation.message,
                        modifier = Modifier
                            .padding(all = 12.dp),
                        style = MaterialTheme.typography.body1.copy(
                            textAlign = TextAlign.Start))
                }


                AnimatedVisibility(
                    visible = shouldShowSentIndicator.and(isSelected),
                    modifier = Modifier.padding(horizontal = 8.dp)) {

                    Text(
                        text = "Sent",
                        color = MaterialTheme.colors.onBackground.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.caption)
                }


            }
        }
    }
}

@Preview
@Composable
fun PreviewChatItemSenderDark() {

    CyclistanceTheme(darkTheme = true) {
        ChatItem(
            isSender = true,
            state = ConversationState(),
            conversation = ConversationItemModel(
                senderId = "1",
                message = "Lorem ipsum dolor sit amet consectetur adipisicing elit. Maxime mollitia,\n" +
                          "molestiae quas vel sint commodi repudiandae consequuntur",
                receiverId = "2",
                timestamp = Date(),
                messageId = "1",
            ))
    }
}


@Preview
@Composable
fun PreviewChatItemSenderLight() {

    CyclistanceTheme(darkTheme = false) {
        ChatItem(
            isSender = true,
            state = ConversationState(),
            conversation = ConversationItemModel(
                senderId = "1",
                message = "Lorem ipsum dolor sit amet consectetur adipisicing elit. Maxime mollitia,\n" +
                          "molestiae quas vel sint commodi repudiandae consequuntur",
                receiverId = "2",
                timestamp = Date(),
                messageId = "1",
            ))
    }
}


@Preview
@Composable
fun PreviewChatItemRecipientDark() {

    CyclistanceTheme(darkTheme = true) {
        ChatItem(
            isSender = false,
            state = ConversationState(),
            conversation = ConversationItemModel(
                senderId = "1",
                message = "Lorem ipsum dolor sit amet consectetur adipisicing elit. Maxime mollitia,\n" +
                          "molestiae quas vel sint commodi repudiandae consequuntur",
                receiverId = "2",
                timestamp = Date(),
                messageId = "1",
            ))
    }
}

@Preview
@Composable
fun PreviewChatItemRecipientLight() {

    CyclistanceTheme(darkTheme = false) {
        ChatItem(
            isSender = false,
            state = ConversationState(),
            conversation = ConversationItemModel(
                senderId = "1",
                message = "Lorem ipsum dolor sit amet consectetur adipisicing elit. Maxime mollitia,\n" +
                          "molestiae quas vel sint commodi repudiandae consequuntur",
                receiverId = "2",
                timestamp = Date(),
                messageId = "1",
            ))
    }
}


