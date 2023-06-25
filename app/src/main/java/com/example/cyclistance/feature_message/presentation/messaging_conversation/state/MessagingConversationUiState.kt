package com.example.cyclistance.feature_message.presentation.messaging_conversation.state

import android.os.Parcelable
import androidx.compose.ui.text.input.TextFieldValue
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
@StableState
data class MessagingConversationUiState(
    val messageAreaExpanded: Boolean = false,
    val chatItemSelectedIndex: Int = -1,
    val message: @RawValue TextFieldValue = TextFieldValue("")
) : Parcelable