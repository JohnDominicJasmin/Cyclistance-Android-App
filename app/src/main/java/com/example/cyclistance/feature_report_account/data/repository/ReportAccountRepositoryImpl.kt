package com.example.cyclistance.feature_report_account.data.repository

import com.example.cyclistance.core.utils.constants.ReportAccountConstants.REPORT_ACCOUNT_COLLECTION
import com.example.cyclistance.feature_report_account.domain.exceptions.ReportAccountExceptions
import com.example.cyclistance.feature_report_account.domain.model.ReportAccountDetails
import com.example.cyclistance.feature_report_account.domain.repository.ReportAccountRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class ReportAccountRepositoryImpl(
    private val firestore: FirebaseFirestore,
) : ReportAccountRepository{

    override suspend fun reportAccount(reportAccountDetails: ReportAccountDetails) {

        suspendCancellableCoroutine { continuation ->
            firestore
                .collection(REPORT_ACCOUNT_COLLECTION)
                .add(reportAccountDetails)
                .addOnSuccessListener {
                    continuation.resume(Unit)
                }.addOnFailureListener {
                    continuation.resumeWithException(
                        ReportAccountExceptions.InsertReportException(
                            it.message.toString()))
                }
        }
    }
}