package com.example.cyclistance.feature_mapping.domain.model.api.user

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize

@StableState
@Parcelize
data class NearbyCyclist(
    val users: List<UserItem> = emptyList(),
) : Parcelable {
    fun findUser(id: String): UserItem {
        return users.find { it.id == id } ?: UserItem()
    }
}