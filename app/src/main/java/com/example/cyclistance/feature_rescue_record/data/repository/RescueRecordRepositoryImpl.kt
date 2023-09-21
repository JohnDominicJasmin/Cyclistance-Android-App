package com.example.cyclistance.feature_rescue_record.data.repository

import com.example.cyclistance.core.utils.constants.RescueRecordConstants.RESCUEE_ID_KEY
import com.example.cyclistance.core.utils.constants.RescueRecordConstants.RESCUER_ID_KEY
import com.example.cyclistance.core.utils.constants.RescueRecordConstants.RESCUE_RECORD_COLLECTION
import com.example.cyclistance.core.utils.constants.RescueRecordConstants.RIDE_DATE_KEY
import com.example.cyclistance.feature_rescue_record.data.mapper.RideHistoryMapper.toRideHistoryItem
import com.example.cyclistance.feature_rescue_record.domain.exceptions.RescueRecordExceptions
import com.example.cyclistance.feature_rescue_record.domain.model.ui.RideDetails
import com.example.cyclistance.feature_rescue_record.domain.model.ui.RideHistory
import com.example.cyclistance.feature_rescue_record.domain.repository.RescueRecordRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
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
    private val auth: FirebaseAuth

) : RescueRecordRepository {
    private val scope: CoroutineContext = Dispatchers.IO
    private var rideDetailsFlow: MutableStateFlow<RideDetails> = MutableStateFlow(RideDetails())

    override suspend fun addRescueRecord(rideDetails: RideDetails) {
        suspendCancellableCoroutine { continuation ->
            firestore
                .collection(RESCUE_RECORD_COLLECTION)
                .document(rideDetails.rideId)
                .set(rideDetails)
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

    override suspend fun getRideHistory(): RideHistory {

        val uid = "mbmckVyzZYezIE8KzjYcj4NTcrGn"
        return suspendCancellableCoroutine { continuation ->
            firestore
                .collection(RESCUE_RECORD_COLLECTION)
                .where(
                    Filter.or(
                        Filter.equalTo(RESCUER_ID_KEY, uid),
                        Filter.equalTo(RESCUEE_ID_KEY, uid)
                    )).orderBy(RIDE_DATE_KEY, Query.Direction.DESCENDING)
                .get().addOnSuccessListener { result ->
                    val rideHistoryItems = result.documents.map { it.toRideHistoryItem(uid = uid) }
                    continuation.resume(RideHistory(items = rideHistoryItems))
                }.addOnFailureListener {
                    continuation.resumeWithException(
                        RescueRecordExceptions.GetRideHistoryException(
                            message = it.message.toString()))
                }

        }
    }

    override suspend fun getRescueRecord(transactionId: String): RideDetails {
        return withContext(scope) {
            suspendCancellableCoroutine { continuation ->
                firestore
                    .collection(RESCUE_RECORD_COLLECTION)
                    .document(transactionId)
                    .get()
                    .addOnSuccessListener {
                        val rideDetails = it.toObject(RideDetails::class.java)!!
                        continuation.resume(rideDetails)
                    }.addOnFailureListener {
                        continuation.resumeWithException(
                            RescueRecordExceptions.GetRescueRecordException(
                                it.message.toString()))
                    }
            }
        }
    }

    override suspend fun getRescueDetails(): Flow<RideDetails> {
        return withContext(scope) {
            rideDetailsFlow
        }
    }

    override suspend fun addRescueDetails(details: RideDetails) {
        withContext(scope) {
            rideDetailsFlow.emit(details)
        }
    }
}