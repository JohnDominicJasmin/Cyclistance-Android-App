package com.example.cyclistance.di.emergency_contact

import android.app.Application
import androidx.room.Room
import com.example.cyclistance.feature_emergency_call.data.data_source.local.EmergencyContactDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton



@Module
@InstallIn(SingletonComponent::class)
object EmergencySingletonModule {

    @Provides
    @Singleton
    fun providesEmergencyContactDatabase(application: Application): EmergencyContactDatabase {
        return Room.databaseBuilder(
            application,
            EmergencyContactDatabase::class.java,
            EmergencyContactDatabase.DATABASE_NAME).build()
    }


}