package com.example.cyclistance.feature_messaging.presentation.messaging.state

import android.os.Parcelable
import androidx.compose.ui.text.input.TextFieldValue
import com.example.cyclistance.core.utils.annotations.StableState
import com.example.cyclistance.feature_messaging.domain.model.ui.list_messages.ChatItemModel
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue


@Parcelize
@StableState
data class MessagingUiState(
    val messageAreaExpanded: Boolean = false,
    val chatItemSelectedIndex: Int = -1,
    val selectedConversationItem: ChatItemModel? = null,
    val message: @RawValue TextFieldValue = TextFieldValue("")
) : Parcelable
