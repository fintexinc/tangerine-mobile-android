package com.fintexinc.core.data.utils.date

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object DateUtils {
    fun getCurrentTimeMinusMonths(months: Int): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_MONTH, 0)
        cal.add(Calendar.MONTH, -months)
        return cal.timeInMillis
    }

    fun isDateInFuture(date: String): Boolean {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.UK)
        val inputDate = sdf.parse(date)
        val currentDate = Calendar.getInstance().time
        return inputDate != null && inputDate.after(currentDate)
    }

    fun isDateInPast(date: String): Boolean {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.UK)
        val inputDate = sdf.parse(date)
        val currentDate = Calendar.getInstance().time
        return inputDate != null && inputDate.before(currentDate)
    }

    fun formatMillisToDateSimpleDateFormat(millis: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.UK)
        val date = java.util.Date(millis)
        return sdf.format(date)
    }

    fun Int.monthName(): String {
        val months = listOf(
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        )
        return if (this in 1..12) months[this - 1] else ""
    }
}