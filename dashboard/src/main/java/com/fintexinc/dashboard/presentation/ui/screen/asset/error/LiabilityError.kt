package com.fintexinc.dashboard.presentation.ui.screen.asset.error

import com.fintexinc.core.R

enum class LiabilityError(val messageResId: Int) {
    LIABILITY_TYPE_NOT_SELECTED(R.string.error_no_liability_type),
    LIABILITY_NAME_MISSING(R.string.error_liability_name_required),
    BALANCE_MISSING(R.string.error_enter_a_balance),
    BALANCE_NEGATIVE(R.string.error_value_cant_be_negative),
    EFFECTIVE_DATE_MISSING(R.string.error_select_effective_date),
    EFFECTIVE_DATE_IN_FUTURE(R.string.error_effective_date_in_future),
    REVISIT_DATE_IN_PAST(R.string.error_revisit_date_cant_be_in_past)
}