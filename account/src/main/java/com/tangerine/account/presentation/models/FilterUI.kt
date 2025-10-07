package com.tangerine.account.presentation.models

import androidx.annotation.StringRes
import com.tangerine.account.R

enum class DateFilterUi(@StringRes val stringResId: Int) {
    ALL_DATES(R.string.filter_all_dates),
    LAST_30_DAYS(R.string.filter_last_30_days),
    LAST_60_DAYS(R.string.filter_last_60_days),
    LAST_90_DAYS(R.string.filter_last_90_days),
    TWELVE_MONTH(R.string.filter_12_month),
    CURRENT_MONTH(R.string.filter_current_month),
    BY_MONTH(R.string.filter_by_month)
}

enum class DocumentTypeFilterUi(@StringRes val stringResId: Int) {
    ALL_DOCUMENTS(R.string.filter_all_documents),
    ACCOUNT_DOCUMENTS(R.string.filter_account_documents),
    STATEMENTS(R.string.filter_statements),
    TAX_DOCUMENTS(R.string.filter_tax_documents)
}

enum class TransactionTypeFilterUi(@StringRes val stringResId: Int) {
    ALL_TYPES(R.string.type_all_types),
    CONTRIBUTIONS_DEPOSITS(R.string.type_contributions_deposits),
    WITHDRAWALS(R.string.type_withdrawals),
    BUYS(R.string.type_buys),
    SELLS(R.string.type_sells),
    DIVIDENDS_INTEREST_1(R.string.type_dividends_interest_1),
}

enum class TransactionStatusFilter(@StringRes val stringResId: Int) {
    ALL_STATUS(R.string.status_all_status),
    COMPLETED(R.string.status_completed),
    PENDING(R.string.status_pending),
    CANCELLED(R.string.status_cancelled),
    FAILED(R.string.status_failed)
}