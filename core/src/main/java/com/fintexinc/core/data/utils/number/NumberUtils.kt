package com.fintexinc.core.data.utils.number

object NumberUtils {
    fun pairDifferences(values: List<Double>): List<Double> {
        val result = mutableListOf<Double>()
        var i = 0
        while (i + 1 < values.size) {
            result.add(values[i] - values[i + 1])
            i += 2
        }
        return result
    }
}