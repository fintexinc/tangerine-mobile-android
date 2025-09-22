package com.tangerine.account.presentation.models

import androidx.annotation.StringRes
import com.tangerine.account.R

enum class DateFilter(@StringRes val stringResId: Int, ) {
    ALL_DATES(R.string.filter_all_dates),
    LAST_30_DAYS(R.string.filter_last_30_days),
    LAST_60_DAYS(R.string.filter_last_60_days),
    LAST_90_DAYS(R.string.filter_last_90_days),
    TWELVE_MONTH(R.string.filter_12_month),
    CURRENT_MONTH(R.string.filter_current_month),
    BY_MONTH(R.string.filter_by_month)
}

enum class DocumentTypeFilter(@StringRes val stringResId: Int) {
    ALL_DOCUMENTS(R.string.filter_all_documents),
    ACCOUNT_DOCUMENTS(R.string.filter_account_documents),
    STATEMENTS(R.string.filter_statements),
    TAX_DOCUMENTS(R.string.filter_tax_documents)
}