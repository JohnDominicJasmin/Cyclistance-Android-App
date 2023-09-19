package com.example.cyclistance.di.emergency_contact

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.cyclistance.feature_emergency_call.data.data_source.local.EmergencyContactDatabase
import com.example.cyclistance.feature_emergency_call.data.repository.EmergencyContactRepositoryImpl
import com.example.cyclistance.feature_emergency_call.domain.repository.EmergencyContactRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton



@Module
@InstallIn(SingletonComponent::class)
object EmergencyDataSourceModule {

    @Provides
    @Singleton
    fun providesEmergencyContactDatabase(application: Application): EmergencyContactDatabase {
        return Room.databaseBuilder(
            application,
            EmergencyContactDatabase::class.java,
            EmergencyContactDatabase.DATABASE_NAME).build()
    }

    @Provides
    @ViewModelScoped
    fun providesEmergencyContactRepository(
        @ApplicationContext context: Context,
        db: EmergencyContactDatabase): EmergencyContactRepository {
        return EmergencyContactRepositoryImpl(db.dao, context = context)
    }



}