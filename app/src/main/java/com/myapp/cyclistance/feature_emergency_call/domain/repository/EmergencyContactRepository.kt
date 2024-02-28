package com.myapp.cyclistance.feature_emergency_call.domain.repository

import com.myapp.cyclistance.feature_emergency_call.domain.model.EmergencyCallModel
import com.myapp.cyclistance.feature_emergency_call.domain.model.EmergencyContactModel
import kotlinx.coroutines.flow.Flow

interface EmergencyContactRepository {
    suspend fun upsertContact(emergencyContact: EmergencyContactModel)
    suspend fun deleteContact(emergencyContact: EmergencyContactModel)
    fun getContacts(): Flow<EmergencyCallModel>
    fun getContact(id: Int): Flow<EmergencyContactModel>
    suspend fun areContactsPurposelyDeleted(): Flow<Boolean>
    suspend fun setContactsPurposelyDeleted()
}