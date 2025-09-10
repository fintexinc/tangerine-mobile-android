package com.fintexinc.dashboard.presentation.ui.mapper

import com.fintexinc.core.data.utils.currency.formatCurrency
import com.fintexinc.core.domain.model.Banking
import com.fintexinc.core.domain.model.Custom
import com.fintexinc.core.domain.model.DataPoint
import com.fintexinc.core.domain.model.Investment
import com.fintexinc.core.domain.model.Liability
import com.fintexinc.core.presentation.ui.widget.modal.NameValueChecked

fun Liability.toNameValue() = NameValueChecked(
    name = getByLiabilityType(liabilityType),
    value = balance,
    subName = accountNumber,
    date = linkedDate,
    isChecked = true
)

fun getByLiabilityType(type: String): String {
    return when (type.lowercase()) {
        "credit_card" -> "Credit Card"
        "mortgage" -> "Mortgage"
        "loan" -> "Loan"
        else -> "Other Liability"
    }
}

fun Custom.toNameValue() = NameValueChecked(
    name = assetName,
    value = assetValue,
    subName = id,
    date = linkedDate,
    isChecked = true
)

fun Banking.toNameValue() = NameValueChecked(
    name = accountName,
    value = accountBalance,
    subName = accountNumber,
    date = linkedDate,
    isChecked = true
)

fun Investment.toNameValue() = NameValueChecked(
    name = registeredName,
    value = MarketValue,
    subName = accountNumber,
    date = linkedDate,
    isChecked = true
)

fun NameValueChecked.toDataPoint() = DataPoint(
    name = name,
    subName = subName,
    value = value.formatCurrency()
)