package com.example.cyclistance.feature_emergency_call.data.data_source.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cyclistance.feature_emergency_call.data.data_source.local.entities.EmergencyContact
import kotlinx.coroutines.flow.Flow


@Dao
interface EmergencyContactDao {

    @Insert(entity = EmergencyContact::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertContact(emergencyContact: EmergencyContact)

    @Delete(entity = EmergencyContact::class)
    suspend fun deleteContact(emergencyContact: EmergencyContact)

    @Query(value = "SELECT * FROM EmergencyContact")
    fun getContacts(): Flow<List<EmergencyContact>>

    @Query(value = "SELECT * FROM EmergencyContact Where id = :id")
    fun getContact(id: Int): Flow<EmergencyContact>

}
