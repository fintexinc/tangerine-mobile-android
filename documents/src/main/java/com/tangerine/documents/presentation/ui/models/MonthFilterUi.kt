package com.tangerine.documents.presentation.ui.models

import androidx.annotation.StringRes
import com.tangerine.documents.R

enum class MonthFilterUi(
    @StringRes val stringResId: Int,
    val monthNumber: Int? = null
) {
    ALL_MONTHS(R.string.text_all_months, null),
    JANUARY(R.string.text_month_january, 1),
    FEBRUARY(R.string.text_month_february, 2),
    MARCH(R.string.text_month_march, 3),
    APRIL(R.string.text_month_april, 4),
    MAY(R.string.text_month_may, 5),
    JUNE(R.string.text_month_june, 6),
    JULY(R.string.text_month_july, 7),
    AUGUST(R.string.text_month_august, 8),
    SEPTEMBER(R.string.text_month_september, 9),
    OCTOBER(R.string.text_month_october, 10),
    NOVEMBER(R.string.text_month_november, 11),
    DECEMBER(R.string.text_month_december, 12);

    companion object {
        fun fromMonthNumber(month: Int): MonthFilterUi? {
            return values().find { it.monthNumber == month }
        }

        fun getAllMonths(): List<MonthFilterUi> {
            return values().filter { it != ALL_MONTHS }
        }
    }
}