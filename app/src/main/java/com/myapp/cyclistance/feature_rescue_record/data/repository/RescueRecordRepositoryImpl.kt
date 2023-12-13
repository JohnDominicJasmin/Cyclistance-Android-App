package com.myapp.cyclistance.feature_rescue_record.data.repository

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.myapp.cyclistance.core.utils.constants.RescueRecordConstants.RESCUEE_ID_KEY
import com.myapp.cyclistance.core.utils.constants.RescueRecordConstants.RESCUER_ID_KEY
import com.myapp.cyclistance.core.utils.constants.RescueRecordConstants.RESCUE_RECORD_COLLECTION
import com.myapp.cyclistance.core.utils.constants.RescueRecordConstants.RIDE_DATE_KEY
import com.myapp.cyclistance.core.utils.constants.UserProfileConstants.KEY_USER_RATINGS
import com.myapp.cyclistance.core.utils.constants.UtilConstants
import com.myapp.cyclistance.feature_rescue_record.data.local.dao.RescueRecordDao
import com.myapp.cyclistance.feature_rescue_record.data.mapper.RescueDetailInfoMapper.toRideDetailInfo
import com.myapp.cyclistance.feature_rescue_record.data.mapper.RescueDetailInfoMapper.toRideDetails
import com.myapp.cyclistance.feature_rescue_record.data.mapper.RideHistoryMapper.toRideHistoryItem
import com.myapp.cyclistance.feature_rescue_record.data.mapper.RideMetricsInfoMapper.toRideMetrics
import com.myapp.cyclistance.feature_rescue_record.data.mapper.RideMetricsInfoMapper.toRideMetricsInfo
import com.myapp.cyclistance.feature_rescue_record.domain.exceptions.RescueRecordExceptions
import com.myapp.cyclistance.feature_rescue_record.domain.model.ui.RescueRide
import com.myapp.cyclistance.feature_rescue_record.domain.model.ui.RideDetails
import com.myapp.cyclistance.feature_rescue_record.domain.model.ui.RideHistory
import com.myapp.cyclistance.feature_rescue_record.domain.model.ui.RideMetrics
import com.myapp.cyclistance.feature_rescue_record.domain.repository.RescueRecordRepository
import com.myapp.cyclistance.feature_user_profile.domain.model.UserStats
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class RescueRecordRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val rescueRecordDao: RescueRecordDao

) : RescueRecordRepository {
    private val scope: CoroutineContext = Dispatchers.IO


    override suspend fun addRescueRecord(rideDetails: RideDetails) {
        suspendCoroutine { continuation ->
            firestore
                .collection(RESCUE_RECORD_COLLECTION)
                .document(rideDetails.rideId)
                .set(rideDetails, SetOptions.merge())
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

    override suspend fun addRideMetrics(rideId: String, rideMetrics: RideMetrics) {
        suspendCoroutine { continuation ->
            firestore.collection(RESCUE_RECORD_COLLECTION)
                .document(rideId)
                .set(mapOf("rideMetrics" to rideMetrics), SetOptions.merge())
                .addOnSuccessListener {
                    continuation.resume(Unit)
                }.addOnFailureListener{
                    continuation.resumeWithException(
                        exception = RescueRecordExceptions.AddRideMetricsException(
                            message = it.message.toString()
                        )
                    )
                }
        }
    }

    override suspend fun upsertRideDetails(rideDetails: RideDetails) {
       withContext(scope){
              rescueRecordDao.upsertRideDetailInfo(rideDetails.toRideDetailInfo())
       }
    }

    override suspend fun getRideDetails(): Flow<List<RideDetails>> {
        return rescueRecordDao.getRideDetailInfo().map {rideDetails ->
            rideDetails.map { detailInfo ->
                detailInfo.toRideDetails()
            }
        }
    }

    override suspend fun upsertRideMetrics(rideMetrics: RideMetrics) {
        withContext(scope){
            rescueRecordDao.upsertRideMetricsInfo(rideMetrics.toRideMetricsInfo())
        }
    }

    override suspend fun getRideMetrics(): Flow<List<RideMetrics>> {
        return rescueRecordDao.getRideMetricsInfo().map { rideMetrics ->
            rideMetrics.map { metricsInfo ->
                metricsInfo.toRideMetrics()
            }
        }
    }

    override suspend fun updateStats(userStats: UserStats) {
        try {
            coroutineScope {
                val overallDistance = userStats.rescueOverallDistanceInMeters
                val averageSpeed = userStats.rescueAverageSpeedMps

                val updateRescuerStatsTask = async {
                    firestore
                        .collection(UtilConstants.USER_COLLECTION)
                        .document(userStats.rescuerId)
                        .update(
                            mapOf(
                                "userActivity.rescueFrequency" to FieldValue.increment(1),
                                "userActivity.overallDistanceOfRescueInMeters" to FieldValue.increment(overallDistance),
                                "userActivity.averageSpeeds" to FieldValue.arrayUnion(averageSpeed)
                            )
                        )
                }

                val updateRescueeStatsTask = async {
                    firestore.collection(UtilConstants.USER_COLLECTION)
                        .document(userStats.rescueeId)
                        .update(
                            mapOf(
                                "userActivity.requestAssistanceFrequency" to FieldValue.increment(1),
                                "reasonAssistance.${userStats.rescueDescription}" to FieldValue.increment(1),
                            )
                        )
                }

                awaitAll(updateRescuerStatsTask, updateRescueeStatsTask)
            }
        }  catch (e: Exception) {
            throw RescueRecordExceptions.UpdateUserStatsException(e.message ?: "Failed to update user stats")
        }
    }

    override suspend fun rateRescuer(rescuerId: String, rating: Double) {
        suspendCoroutine { continuation ->
            firestore.collection(UtilConstants.USER_COLLECTION)
                .document(rescuerId)
                .update(KEY_USER_RATINGS, FieldValue.arrayUnion(rating))
                .addOnSuccessListener {
                continuation.resume(Unit)
            }.addOnFailureListener{
                continuation.resumeWithException(
                    RescueRecordExceptions.RateRescuerException(message = it.message.toString())
                )
            }
        }
    }

    override suspend fun rateRescue(rescueId: String, rating: Double, ratingText: String) {
        suspendCoroutine { continuation ->
            firestore.collection(
                RESCUE_RECORD_COLLECTION
            ).document(rescueId)
                .update(
                    mapOf(
                        "rideSummary.rating" to rating,
                        "rideSummary.ratingText" to ratingText
                    )
                ).addOnSuccessListener {
                    continuation.resume(Unit)
                }.addOnFailureListener{
                    continuation.resumeWithException(
                        RescueRecordExceptions.InsertRescueRecordException(
                            it.message.toString()
                        )
                    )

                }
        }
    }



    override suspend fun getRideHistory(uid: String): RideHistory {

        return suspendCancellableCoroutine { continuation ->
            firestore
                .collection(RESCUE_RECORD_COLLECTION)
                .where(
                    Filter.or(
                        Filter.equalTo(RESCUER_ID_KEY, uid),
                        Filter.equalTo(RESCUEE_ID_KEY, uid)
                    ))
                .orderBy(RIDE_DATE_KEY, Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener { result ->
                    val rideHistoryItems = result.documents.map { it.toRideHistoryItem(uid = uid) }
                    continuation.resume(RideHistory(items = rideHistoryItems))
                }.addOnFailureListener {
                    continuation.resumeWithException(
                        RescueRecordExceptions.GetRideHistoryException(
                            message = it.message.toString()))
                }

        }
    }

    override suspend fun getRescueRecord(transactionId: String): RescueRide {
        return withContext(scope) {
            suspendCancellableCoroutine { continuation ->
                firestore
                    .collection(RESCUE_RECORD_COLLECTION)
                    .document(transactionId)
                    .get()
                    .addOnSuccessListener {
                        val rideDetails = it.toObject(RescueRide::class.java) ?: RescueRide()
                        continuation.resume(rideDetails)
                    }.addOnFailureListener {
                        continuation.resumeWithException(
                            RescueRecordExceptions.GetRescueRecordException(
                                it.message.toString()))
                    }
            }
        }
    }


}