package com.example.cyclistance.feature_mapping.data.remote.dto.user_dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class UserDto : Parcelable, ArrayList<UserItemDto>()