package com.fintexinc.core.data.utils.date

import com.fintexinc.core.domain.model.DocumentDate
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun String.formatDateOrToday(): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val outputFormat = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
    val today = Calendar.getInstance()

    val date: Date? = try {
        inputFormat.parse(this)
    } catch (e: Exception) {
        null
    }

    if (date != null) {
        val cal = Calendar.getInstance().apply { time = date }
        return if (cal.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
            cal.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)
        ) {
            "Today"
        } else {
            outputFormat.format(date)
        }
    }
    return this
}

// TODO: refactor once API is added and we know how document date is returned
fun DocumentDate.formatToString(): String {
    val months = arrayOf(
        "", "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )
    fun getDaySuffix(d: Int): String =
        if (d in 11..13) "th"
        else when (d % 10) {
            1 -> "st"
            2 -> "nd"
            3 -> "rd"
            else -> "th"
        }
    val monthName = if (month in 1..12) months[month] else "Unknown"
    val suffix = getDaySuffix(day)
    return "$monthName $day$suffix, $year"
}
