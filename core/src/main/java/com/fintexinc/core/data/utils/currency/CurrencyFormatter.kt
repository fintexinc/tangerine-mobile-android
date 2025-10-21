package com.fintexinc.core.data.utils.currency

import java.text.NumberFormat
import java.util.Locale

fun Double.formatCurrency(
    maximumFractionDigits: Int = 0,
    minimumFractionDigits: Int = 0
): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale.US)
    formatter.maximumFractionDigits = maximumFractionDigits
    formatter.minimumFractionDigits = minimumFractionDigits
    return formatter.format(this)
}

fun Long.formatCurrency(): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale.US)
    return formatter.format(this)
}