package com.example.cyclistance.feature_rescue_record.domain.exceptions

object RescueRecordExceptions {
    class InsertRescueRecordException(message: String = "Insert rescue record failed") : RuntimeException()
}