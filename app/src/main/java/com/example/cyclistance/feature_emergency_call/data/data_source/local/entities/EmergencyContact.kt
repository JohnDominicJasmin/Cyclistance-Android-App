package com.example.cyclistance.feature_emergency_call.data.data_source.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class EmergencyContact(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",
    val photoUri: String = "",
    val number: String = ""
)
