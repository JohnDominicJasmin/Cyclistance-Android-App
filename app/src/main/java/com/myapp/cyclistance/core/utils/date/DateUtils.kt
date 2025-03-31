package com.myapp.cyclistance.core.utils.date

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateUtils {

    private const val SECOND_MILLIS = 1000
    private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
    private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
    private const val DAY_MILLIS = 24 * HOUR_MILLIS

    private fun currentDate(): Date {
        val calendar = Calendar.getInstance()
        return calendar.time
    }

    fun formatToTimeAgo(timeMillis: Long): String {
        var time = Date(timeMillis).time
        if (time < 1000000000000L) {
            time *= 1000
        }

        val now = currentDate().time
        if (time > now || time <= 0) {
            return "few seconds ago"
        }

        val diff = now - time
        return when {
            diff < MINUTE_MILLIS -> "just now"
            diff < 2 * MINUTE_MILLIS -> "1m ago"
            diff < 60 * MINUTE_MILLIS -> "${diff / MINUTE_MILLIS}m ago"
            diff < 2 * HOUR_MILLIS -> "1h ago"
            diff < 24 * HOUR_MILLIS -> "${diff / HOUR_MILLIS}h ago"
            diff < 48 * HOUR_MILLIS -> "1d ago"
            else -> "${diff / DAY_MILLIS}d ago"
        }
    }

    fun Date.toReadableDateTime(pattern: String = "dd/MM/yyyy HH:mm"): String {
        return SimpleDateFormat(pattern, Locale.getDefault()).format(this)
    }

    fun Date.isInRange(startingDate: Date, endDate: Date) = this.after(startingDate) && this.before(endDate)
}