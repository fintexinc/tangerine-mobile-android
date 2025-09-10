package com.fintexinc.core.data.utils.date

import java.util.Calendar

object DateUtils {
    fun getCurrentTimeMinusMonths(months: Int): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_MONTH, 0)
        cal.add(Calendar.MONTH, -months)
        return cal.timeInMillis
    }
}