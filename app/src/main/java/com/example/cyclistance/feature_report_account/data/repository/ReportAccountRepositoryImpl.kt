package com.example.cyclistance.feature_report_account.data.repository

import android.content.Context
import com.example.cyclistance.core.utils.constants.ReportAccountConstants.BANNED_ACCOUNTS_COLLECTION
import com.example.cyclistance.core.utils.constants.ReportAccountConstants.LAST_REPORT_ID_KEY
import com.example.cyclistance.core.utils.constants.ReportAccountConstants.REPORT_ACCOUNT_COLLECTION
import com.example.cyclistance.core.utils.contexts.dataStore
import com.example.cyclistance.core.utils.data_store_ext.editData
import com.example.cyclistance.core.utils.data_store_ext.getData
import com.example.cyclistance.core.utils.date.DateUtils.isInRange
import com.example.cyclistance.feature_report_account.domain.exceptions.ReportAccountExceptions
import com.example.cyclistance.feature_report_account.domain.model.BannedAccountDetails
import com.example.cyclistance.feature_report_account.domain.model.ReportAccountDetails
import com.example.cyclistance.feature_report_account.domain.repository.ReportAccountRepository
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.Date
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class ReportAccountRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val context: Context,
) : ReportAccountRepository{

    private var dataStore = context.dataStore
    private val scope: CoroutineContext = Dispatchers.IO

    override suspend fun isAccountBanned(userId: String): BannedAccountDetails {
        return suspendCoroutine { continuation ->
            firestore.collection(BANNED_ACCOUNTS_COLLECTION).where(Filter.equalTo("userId", userId))
                .get()
                .addOnSuccessListener {
                    if(it.documents.isEmpty()){
                        return@addOnSuccessListener
                    }
                    val data = it.documents.last()
                    val userId = data.getString("userId") ?: ""
                    val reason = data.getString("reason") ?: ""
                    val startingDate = data.getDate("startingDate") ?: Date()
                    val endDate = data.getDate("endDate") ?: Date()
                    val currentDate = Date()

                    val bannedAccountDetails = BannedAccountDetails(
                        userId = userId,
                        reason = reason,
                        startingDate = startingDate,
                        endDate = endDate,
                        isAccountStillBanned = currentDate.isInRange(startingDate, endDate)
                    )

                    continuation.resume(bannedAccountDetails)
                }.addOnFailureListener{
                    Timber.v("Cannot check Banned account details because of: ${it.message}")
                }
        }
    }



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



    override suspend fun getLastReportedId(): Flow<String> {
        return withContext(scope){
            dataStore.getData(key = LAST_REPORT_ID_KEY, defaultValue = "")
        }
    }

    override suspend fun setLastReportedId(id: String) {
        withContext(scope){
            dataStore.editData(LAST_REPORT_ID_KEY, id)
        }
    }
}