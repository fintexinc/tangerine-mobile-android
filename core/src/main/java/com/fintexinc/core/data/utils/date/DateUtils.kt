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

    fun formatMillisToDateSimpleDateFormat(millis: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.UK)
        val date = java.util.Date(millis)
        return sdf.format(date)
    }
}