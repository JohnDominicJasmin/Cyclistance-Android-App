package com.example.cyclistance.core.utils.constants

import androidx.datastore.preferences.core.stringPreferencesKey

object ReportAccountConstants {
    const val REPORT_ACCOUNT_COLLECTION = "reportAccount"
    const val REPORT_ACCOUNT_VM_STATE_KEY = "reportAccountVmState"
    const val BANNED_ACCOUNTS_COLLECTION = "bannedAccounts"
    val LAST_REPORT_ID_KEY = stringPreferencesKey("lastReportId")
}