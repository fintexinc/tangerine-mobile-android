package com.tangerine.charts.compose_charts.extensions

import kotlin.math.pow
import kotlin.math.round

fun Double.format(decimalPlaces: Int): String {
    require(decimalPlaces >= 0) { "Decimal places must be non-negative." }

    if (decimalPlaces == 0) {
        return this.toInt().toString() // Handle whole numbers efficiently
    }

    val factor = 10.0.pow(decimalPlaces.toDouble())
    val roundedValue = round(this * factor) / factor
    return roundedValue.toString()
}