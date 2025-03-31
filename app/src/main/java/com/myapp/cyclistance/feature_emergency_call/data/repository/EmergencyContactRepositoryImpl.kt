package com.myapp.cyclistance.feature_emergency_call.data.repository

import android.content.Context
import com.myapp.cyclistance.core.utils.constants.EmergencyCallConstants.CONTACTS_PURPOSELY_DELETE_KEY
import com.myapp.cyclistance.core.utils.contexts.dataStore
import com.myapp.cyclistance.core.utils.data_store_ext.editData
import com.myapp.cyclistance.core.utils.data_store_ext.getData
import com.myapp.cyclistance.feature_emergency_call.data.data_source.local.dao.EmergencyContactDao
import com.myapp.cyclistance.feature_emergency_call.data.mapper.EmergencyContactMapper.toEmergencyContact
import com.myapp.cyclistance.feature_emergency_call.data.mapper.EmergencyContactMapper.toEmergencyContactModel
import com.myapp.cyclistance.feature_emergency_call.domain.model.EmergencyCallModel
import com.myapp.cyclistance.feature_emergency_call.domain.model.EmergencyContactModel
import com.myapp.cyclistance.feature_emergency_call.domain.repository.EmergencyContactRepository

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
                emergencyContact = emergencyContact.toEmergencyContact())
        }
    }


    override suspend fun areContactsPurposelyDeleted(): Flow<Boolean> {
        return dataStore.getData(CONTACTS_PURPOSELY_DELETE_KEY, defaultValue = false)
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

    override fun getContact(id: Int): Flow<EmergencyContactModel> {
        return emergencyContactDao.getContact(id = id).map { it.toEmergencyContactModel() }
    }
}