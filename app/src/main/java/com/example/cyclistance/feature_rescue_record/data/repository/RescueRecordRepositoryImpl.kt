package com.example.cyclistance.feature_rescue_record.data.repository

import com.example.cyclistance.core.utils.constants.RescueRecordConstants.RESCUE_RECORD_COLLECTION
import com.example.cyclistance.feature_rescue_record.domain.exceptions.RescueRecordExceptions
import com.example.cyclistance.feature_rescue_record.domain.model.ui.RideDetails
import com.example.cyclistance.feature_rescue_record.domain.repository.RescueRecordRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class RescueRecordRepositoryImpl(
    private val firestore: FirebaseFirestore,

): RescueRecordRepository {

    override suspend fun addRescueRecord(rideDetails: RideDetails) {
        suspendCancellableCoroutine { continuation ->
            firestore
                .collection(RESCUE_RECORD_COLLECTION)
                .add(rideDetails)
                .addOnSuccessListener {
                    continuation.resume(Unit)
                }.addOnFailureListener {
                    continuation.resumeWithException(
                        RescueRecordExceptions.InsertRescueRecordException(
                            it.message.toString()
                        )
                    )
                }
        }
    }
}