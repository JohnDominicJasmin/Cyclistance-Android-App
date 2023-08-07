package com.example.cyclistance.feature_messaging.domain.model.ui.chats

import android.os.Parcelable
import com.example.cyclistance.core.domain.model.UserDetails
import com.example.cyclistance.core.utils.annotations.StableState
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


@StableState
@Parcelize
data class MessagingUserItemModel(
    val userDetails: UserDetails = UserDetails(),
    val fcmToken: String = "",
    val isUserAvailable: Boolean = false
) : Parcelable {
    companion object {
        fun MessagingUserItemModel.toJsonString(): String {
            val encodedUrl = URLEncoder.encode(userDetails.photo, StandardCharsets.UTF_8.toString())
            return Gson().toJson(this.copy(userDetails = userDetails.copy(photo = encodedUrl)))

        }
    }
}




