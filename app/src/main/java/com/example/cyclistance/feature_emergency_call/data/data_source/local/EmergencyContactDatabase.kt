package com.example.cyclistance.feature_emergency_call.data.data_source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.cyclistance.feature_emergency_call.data.data_source.local.dao.EmergencyContactDao
import com.example.cyclistance.feature_emergency_call.data.data_source.local.entities.EmergencyContact

@Database(entities = [EmergencyContact::class], version = 1, exportSchema = false)
abstract class EmergencyContactDatabase : RoomDatabase() {
    abstract val dao: EmergencyContactDao

    companion object {
        const val DATABASE_NAME = "emergency_contact_db.db"
    }
}