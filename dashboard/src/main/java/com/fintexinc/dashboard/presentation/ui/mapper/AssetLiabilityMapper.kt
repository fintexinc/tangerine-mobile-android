package com.fintexinc.dashboard.presentation.ui.mapper

import com.fintexinc.core.data.model.DataPoint
import com.fintexinc.core.data.utils.currency.formatCurrency
import com.fintexinc.core.data.utils.date.formatEffectiveDate
import com.fintexinc.core.domain.model.Banking
import com.fintexinc.core.domain.model.Custom
import com.fintexinc.core.domain.model.Investment
import com.fintexinc.core.domain.model.Liability
import com.fintexinc.core.presentation.ui.widget.modal.NameValueChecked
import com.fintexinc.dashboard.R

fun Liability.toNameValue() = NameValueChecked(
    id = id,
    name = liabilityType.label,
    value = balance,
    subName = accountNumber,
    date = linkedDate,
    isChecked = true,
    iconResId = R.drawable.ic_liability
)

fun Custom.toNameValue() = NameValueChecked(
    id = id,
    name = assetName,
    value = assetValue,
    subName = linkedDate,
    date = linkedDate,
    isChecked = true,
    iconResId = R.drawable.ic_custom_asset
)

fun Banking.toNameValue() = NameValueChecked(
    id = id,
    name = accountName,
    value = accountBalance,
    subName = accountNumber,
    date = linkedDate,
    isChecked = true,
    iconResId = R.drawable.ic_asset_tangerine
)

fun Investment.toNameValue() = NameValueChecked(
    id = id,
    name = registeredName,
    value = MarketValue,
    subName = accountNumber,
    date = linkedDate,
    isChecked = true,
    iconResId = R.drawable.ic_asset_tangerine
)

fun NameValueChecked.toDataPoint() = DataPoint(
    id = id,
    name = name,
    subName = subName,
    value = value.formatCurrency(),
    iconResId = iconResId
)

fun NameValueChecked.toLiabilityDataPoint(): DataPoint {
    val formattedSubName = if (subName.matches(Regex("\\d{4}-\\d{2}-\\d{2}"))) {
        formatEffectiveDate(subName)
    } else {
        formatEffectiveDate(date)
    }

    return DataPoint(
        id = id,
        name = name,
        subName = formattedSubName,
        value = value.formatCurrency(),
        iconResId = iconResId
    )
}