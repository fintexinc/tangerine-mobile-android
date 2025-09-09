package com.fintexinc.core.data.utils

import java.text.NumberFormat
import java.util.Locale

fun Double.formatCurrency(): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale.US)
    formatter.maximumFractionDigits = 2
    formatter.minimumFractionDigits = 2
    return formatter.format(this)
}