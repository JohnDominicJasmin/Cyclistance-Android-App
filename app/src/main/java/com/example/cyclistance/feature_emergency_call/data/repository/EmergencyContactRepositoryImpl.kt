package com.example.cyclistance.feature_emergency_call.data.repository

import com.example.cyclistance.feature_emergency_call.data.data_source.local.dao.EmergencyContactDao
import com.example.cyclistance.feature_emergency_call.data.mapper.EmergencyContactMapper.toEmergencyContact
import com.example.cyclistance.feature_emergency_call.data.mapper.EmergencyContactMapper.toEmergencyContactModel
import com.example.cyclistance.feature_emergency_call.domain.model.EmergencyCallModel
import com.example.cyclistance.feature_emergency_call.domain.model.EmergencyContactModel
import com.example.cyclistance.feature_emergency_call.domain.repository.EmergencyContactRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class EmergencyContactRepositoryImpl(
    private val emergencyContactDao: EmergencyContactDao
) : EmergencyContactRepository {

    private val scope: CoroutineContext = Dispatchers.IO

    override suspend fun upsertContact(emergencyContact: EmergencyContactModel) {
        withContext(scope) {
            emergencyContactDao.upsertContact(emergencyContact = emergencyContact.toEmergencyContact())
        }
    }

    override suspend fun deleteContact(emergencyContact: EmergencyContactModel) {
        withContext(scope) {
            emergencyContactDao.deleteContact(emergencyContact = emergencyContact.toEmergencyContact())
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