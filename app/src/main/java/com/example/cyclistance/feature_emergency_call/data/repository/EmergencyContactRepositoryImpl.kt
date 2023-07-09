package com.example.cyclistance.feature_emergency_call.data.repository

import android.content.Context
import com.example.cyclistance.core.utils.constants.EmergencyCallConstants.CONTACTS_PURPOSELY_DELETE_KEY
import com.example.cyclistance.core.utils.data_store_ext.editData
import com.example.cyclistance.core.utils.data_store_ext.getData
import com.example.cyclistance.feature_emergency_call.data.data_source.local.dao.EmergencyContactDao
import com.example.cyclistance.feature_emergency_call.data.data_source.local.entities.EmergencyContact
import com.example.cyclistance.feature_emergency_call.data.mapper.EmergencyContactMapper.toEmergencyContact
import com.example.cyclistance.feature_emergency_call.data.mapper.EmergencyContactMapper.toEmergencyContactModel
import com.example.cyclistance.feature_emergency_call.domain.model.EmergencyCallModel
import com.example.cyclistance.feature_emergency_call.domain.model.EmergencyContactModel
import com.example.cyclistance.feature_emergency_call.domain.repository.EmergencyContactRepository
import com.example.cyclistance.feature_mapping.data.repository.dataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class EmergencyContactRepositoryImpl(
    private val emergencyContactDao: EmergencyContactDao,
    private val context: Context
) : EmergencyContactRepository {

    private val scope: CoroutineContext = Dispatchers.IO
    private var dataStore = context.dataStore

    override suspend fun upsertContact(emergencyContact: EmergencyContactModel) {
        withContext(scope) {
            emergencyContactDao.upsertContact(
                emergencyContact = EmergencyContact(
                    name = emergencyContact.name,
                    photoUri = emergencyContact.photo,
                    number = emergencyContact.phoneNumber
                ))
        }
    }


    override suspend fun areContactsPurposelyDeleted(): Flow<Boolean> {
        return withContext(scope) {
            dataStore.getData(CONTACTS_PURPOSELY_DELETE_KEY, defaultValue = false)
        }
    }

    override suspend fun deleteContact(emergencyContact: EmergencyContactModel) {
        withContext(scope) {
            emergencyContactDao.deleteContact(emergencyContact = emergencyContact.toEmergencyContact())
        }
    }

    override suspend fun setContactsPurposelyDeleted() {
        withContext(scope) {
            dataStore.editData(CONTACTS_PURPOSELY_DELETE_KEY, true)
        }
    }

    override fun getContacts(): Flow<EmergencyCallModel> {
        return emergencyContactDao.getContacts().map { contacts ->
            EmergencyCallModel(
                contacts = contacts.map { it.toEmergencyContactModel() }
            )
        }
    }


}