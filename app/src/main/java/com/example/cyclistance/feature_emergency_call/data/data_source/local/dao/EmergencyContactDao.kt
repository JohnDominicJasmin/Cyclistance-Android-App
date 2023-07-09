package com.example.cyclistance.feature_emergency_call.data.data_source.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.cyclistance.feature_emergency_call.data.data_source.local.entities.EmergencyContact
import kotlinx.coroutines.flow.Flow


@Dao
interface EmergencyContactDao {

    @Upsert(entity = EmergencyContact::class)
    suspend fun upsertContact(emergencyContact: EmergencyContact)

    @Delete(entity = EmergencyContact::class)
    suspend fun deleteContact(emergencyContact: EmergencyContact)

    @Query(value = "SELECT * FROM EmergencyContact")
    fun getContacts(): Flow<List<EmergencyContact>>


}
