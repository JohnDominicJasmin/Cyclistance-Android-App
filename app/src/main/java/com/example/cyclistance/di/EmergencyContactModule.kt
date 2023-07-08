package com.example.cyclistance.di

import android.app.Application
import androidx.annotation.Keep
import androidx.room.Room
import com.example.cyclistance.feature_emergency_call.data.data_source.local.EmergencyContactDatabase
import com.example.cyclistance.feature_emergency_call.data.repository.EmergencyContactRepositoryImpl
import com.example.cyclistance.feature_emergency_call.domain.repository.EmergencyContactRepository
import com.example.cyclistance.feature_emergency_call.domain.use_case.EmergencyContactUseCase
import com.example.cyclistance.feature_emergency_call.domain.use_case.delete_contact.DeleteContactUseCase
import com.example.cyclistance.feature_emergency_call.domain.use_case.get_contacts.GetContactsUseCase
import com.example.cyclistance.feature_emergency_call.domain.use_case.upsert_contact.UpsertContactUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Keep
@Module
@InstallIn(SingletonComponent::class)
object EmergencyContactModule {

    @Provides
    @Singleton
    fun providesEmergencyContactDatabase(application: Application): EmergencyContactDatabase {
        return Room.databaseBuilder(
            application,
            EmergencyContactDatabase::class.java,
            EmergencyContactDatabase.DATABASE_NAME).build()
    }

    @Provides
    @Singleton
    fun providesEmergencyContactRepository(db: EmergencyContactDatabase): EmergencyContactRepository {
        return EmergencyContactRepositoryImpl(db.dao)
    }


    @Provides
    @Singleton
    fun providesEmergencyContactUseCase(repository: EmergencyContactRepository): EmergencyContactUseCase {
        return EmergencyContactUseCase(
            upsertContactUseCase = UpsertContactUseCase(repository),
            deleteContactUseCase = DeleteContactUseCase(repository),
            getContactsUseCase = GetContactsUseCase(repository)
        )
    }
}