package com.fintexinc.dashboard.presentation.ui.screen.asset.error

import com.fintexinc.core.R

enum class AssetError(val messageResId: Int) {
    ASSET_TYPE_NOT_SELECTED(R.string.error_no_asset_type),
    ASSET_NAME_MISSING(R.string.error_asset_name_required),
    CURRENT_VALUE_MISSING(R.string.error_enter_a_value),
    CURRENT_VALUE_IS_NEGATIVE(R.string.error_value_cant_be_negative),
    EFFECTIVE_DATE_MISSING(R.string.error_select_effective_date),
    EFFECTIVE_DATE_IN_FUTURE(R.string.error_effective_date_in_future),
    REVISIT_DATE_IN_PAST(R.string.error_revisit_date_cant_be_in_past)
}