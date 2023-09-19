package com.example.cyclistance.feature_rescue_record.data.repository

import com.example.cyclistance.core.utils.constants.RescueRecordConstants.RESCUE_RECORD_COLLECTION
import com.example.cyclistance.feature_rescue_record.domain.exceptions.RescueRecordExceptions
import com.example.cyclistance.feature_rescue_record.domain.model.ui.RideDetails
import com.example.cyclistance.feature_rescue_record.domain.repository.RescueRecordRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class RescueRecordRepositoryImpl(
    private val firestore: FirebaseFirestore,

): RescueRecordRepository {
    private val scope: CoroutineContext = Dispatchers.IO
    private var rideDetailsFlow: MutableStateFlow<RideDetails> = MutableStateFlow(RideDetails())

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

    override suspend fun getRescueDetails(): Flow<RideDetails> {
       return withContext(scope){
           rideDetailsFlow
       }
    }

    override suspend fun addRescueDetails(details: RideDetails) {
        withContext(scope){
            rideDetailsFlow.emit(details)
        }
    }
}