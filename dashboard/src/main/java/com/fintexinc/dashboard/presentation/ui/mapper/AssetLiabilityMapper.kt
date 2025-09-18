package com.fintexinc.dashboard.presentation.ui.mapper

import com.fintexinc.core.data.utils.currency.formatCurrency
import com.fintexinc.core.data.utils.date.formatEffectiveDate
import com.fintexinc.core.domain.model.Banking
import com.fintexinc.core.domain.model.Custom
import com.fintexinc.core.domain.model.DataPoint
import com.fintexinc.core.domain.model.Investment
import com.fintexinc.core.domain.model.Liability
import com.fintexinc.core.presentation.ui.widget.modal.NameValueChecked
import com.fintexinc.dashboard.R

fun Liability.toNameValue(effectiveOnText: String) = NameValueChecked(
    name = liabilityType.label,
    value = balance,
    subName = effectiveOnText.format(formatEffectiveDate(linkedDate)),
    date = linkedDate,
    isChecked = true,
    iconResId = R.drawable.ic_liability
)

fun Custom.toNameValue() = NameValueChecked(
    name = assetName,
    value = assetValue,
    subName = id,
    date = linkedDate,
    isChecked = true,
    iconResId = R.drawable.ic_custom_asset_boat
)

fun Banking.toNameValue() = NameValueChecked(
    name = accountName,
    value = accountBalance,
    subName = accountNumber,
    date = linkedDate,
    isChecked = true,
    iconResId = R.drawable.ic_asset_tangerine
)

fun Investment.toNameValue() = NameValueChecked(
    name = registeredName,
    value = MarketValue,
    subName = accountNumber,
    date = linkedDate,
    isChecked = true,
    iconResId = R.drawable.ic_asset_tangerine
)

fun NameValueChecked.toDataPoint() = DataPoint(
    name = name,
    subName = subName,
    value = value.formatCurrency(),
    iconResId = iconResId
)