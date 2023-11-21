package com.myapp.cyclistance.feature_messaging.domain.model.ui.chats

import android.os.Parcelable
import com.myapp.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize

@StableState
@Parcelize
data class ChatsModel(
    val chats: List<ChatItemModel>
) : Parcelable{
    @StableState
    constructor(): this(
        chats = emptyList())
}
