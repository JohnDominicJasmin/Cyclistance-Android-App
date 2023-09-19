package com.example.cyclistance.di.emergency_contact

import com.example.cyclistance.feature_emergency_call.domain.repository.EmergencyContactRepository
import com.example.cyclistance.feature_emergency_call.domain.use_case.EmergencyContactUseCase
import com.example.cyclistance.feature_emergency_call.domain.use_case.contact_purposely_deleted.AddDefaultContactUseCase
import com.example.cyclistance.feature_emergency_call.domain.use_case.contact_purposely_deleted.AreContactsPurposelyDeletedUseCase
import com.example.cyclistance.feature_emergency_call.domain.use_case.delete_contact.DeleteContactUseCase
import com.example.cyclistance.feature_emergency_call.domain.use_case.get_contact.GetContactUseCase
import com.example.cyclistance.feature_emergency_call.domain.use_case.get_contact.GetContactsUseCase
import com.example.cyclistance.feature_emergency_call.domain.use_case.upsert_contact.UpsertContactUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped


@Module
@InstallIn(ViewModelComponent::class)
object EmergencyViewModelModule {




    @Provides
    @ViewModelScoped
    fun providesEmergencyContactUseCase(repository: EmergencyContactRepository): EmergencyContactUseCase {
        return EmergencyContactUseCase(
            upsertContactUseCase = UpsertContactUseCase(repository),
            deleteContactUseCase = DeleteContactUseCase(repository),
            getContactsUseCase = GetContactsUseCase(repository),
            areContactsPurposelyDeletedUseCase = AreContactsPurposelyDeletedUseCase(repository),
            addDefaultContactUseCase = AddDefaultContactUseCase(repository),
            getContactUseCase = GetContactUseCase(repository)
        )
    }
}