package com.myapp.cyclistance.feature_emergency_call.data.data_source.local.entities

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
@Keep
data class EmergencyContact(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",
    val photoUri: String = "",
    val number: String = ""
)
