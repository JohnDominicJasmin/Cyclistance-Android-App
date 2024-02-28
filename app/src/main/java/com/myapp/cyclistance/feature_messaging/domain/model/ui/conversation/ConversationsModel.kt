package com.myapp.cyclistance.feature_messaging.domain.model.ui.conversation

import android.os.Parcelable
import com.myapp.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize

@StableState
@Parcelize
data class ConversationsModel(
    val messages: List<ConversationItemModel>
) : Parcelable{
    @StableState
    constructor(): this(
        messages = emptyList())
}
