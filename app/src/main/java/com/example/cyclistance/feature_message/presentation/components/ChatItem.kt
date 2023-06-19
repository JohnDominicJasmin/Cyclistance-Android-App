package com.example.cyclistance.feature_message.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cyclistance.feature_message.domain.model.ui.MessageContent
import com.example.cyclistance.theme.CyclistanceTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ChatItem(
    modifier: Modifier = Modifier,
    message: MessageContent,
    isSender: Boolean,
    currentIndex: Int? = null,
    selectedIndex: Int? = null,
    onClick: (Int) -> Unit = {},
    contentAlignment: Alignment = Alignment.Center,
) {


    val timeStampAvailable by remember {
        derivedStateOf {
            message.duration == null
        }
    }

    val isMessageSent by remember {
        derivedStateOf {
            message.dateSent != null
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

        Column(
            horizontalAlignment = if (isSender) Alignment.Start else Alignment.End,
            modifier = Modifier.fillMaxWidth()) {


            AnimatedVisibility(
                visible = timeStampAvailable.and(isSelected),
                enter = fadeIn() + expandVertically(animationSpec = tween(durationMillis = 320)),
                exit = fadeOut() + shrinkVertically(animationSpec = tween(durationMillis = 300)),
                modifier = Modifier.fillMaxWidth()) {


                Text(
                    text = message.dateSent!!,
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
                backgroundColor = if (isSelected) backgroundColor else backgroundColor.copy(alpha = 0.8f),
                elevation = if (isSelected) 2.dp else 0.dp,
                onClick = { currentIndex?.let { onClick(it) } }) {

                Text(
                    text = message.content,
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

@Preview
@Composable
fun PreviewChatItemSenderDark() {

    CyclistanceTheme(darkTheme = true) {
        ChatItem(
            isSender = true,
            message = MessageContent(
                senderId = "1",
                content = "Lorem ipsum dolor sit amet consectetur adipisicing elit. Maxime mollitia,\n" +
                          "molestiae quas vel sint commodi repudiandae consequuntur",
                recipientId = "2",
                dateSent = "11:40 am",
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
            message = MessageContent(
                senderId = "1",
                content = "Lorem ipsum dolor sit amet consectetur adipisicing elit. Maxime mollitia,\n" +
                          "molestiae quas vel sint commodi repudiandae consequuntur",
                recipientId = "2",
                dateSent = "11:40 am",
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
            message = MessageContent(
                senderId = "1",
                content = "Lorem ipsum dolor sit amet consectetur adipisicing elit. Maxime mollitia,\n" +
                          "molestiae quas vel sint commodi repudiandae consequuntur",
                recipientId = "2",
                dateSent = "11:40 am",
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
            message = MessageContent(
                senderId = "1",
                content = "Lorem ipsum dolor sit amet consectetur adipisicing elit. Maxime mollitia,\n" +
                          "molestiae quas vel sint commodi repudiandae consequuntur",
                recipientId = "2",
                dateSent = "11:40 am",
                messageId = "1",
            ))
    }
}


