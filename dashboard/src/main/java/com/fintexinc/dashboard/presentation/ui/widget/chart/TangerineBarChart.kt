package com.fintexinc.dashboard.presentation.ui.widget.chart

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fintexinc.core.data.model.DateValue
import com.tangerine.charts.compose_charts.Period
import com.tangerine.charts.compose_charts.TangerineNetWorthChart
import com.tangerine.charts.compose_charts.models.Bars
import com.tangerine.charts.compose_charts.models.Bars.Data
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun TangerineBarChart(
    negativeValuesPosition: NegativeValuesPosition,
    period: Period,
    data: Pair<List<DateValue>, List<DateValue>>,
    legendLabels: Triple<String, String, String>,
    horizontalIndicatorStep: Double = 200000.0,
    radius: Dp = 16.dp,
    enabledColors: Pair<Color, Color>,
    disabledColors: Pair<Color, Color>,
) {
    val barsData = getMonthlyData(data, period)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 16.dp)
    ) {
        TangerineNetWorthChart(
            modifier = Modifier
                .fillMaxWidth()
                .height(188.dp),
            data = barsData,
            legendLabels = legendLabels,
            radius = radius,
            horizontalIndicatorStep = horizontalIndicatorStep,
            enabledColors = Pair(
                enabledColors.first,
                enabledColors.second
            ),
            disabledColors = Pair(
                disabledColors.first,
                disabledColors.second
            ),
            isNegativeValueReversed = negativeValuesPosition == NegativeValuesPosition.REVERSED
        )
    }
}

private fun filterAndGroupByMonth(
    objects: List<DateValue>,
    months: Int
): Map<String, List<DateValue>> {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val groupKeyFormat = SimpleDateFormat("yyyy-MM", Locale.getDefault())
    val displayFormat = SimpleDateFormat("MMM", Locale.getDefault())
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.MONTH, -months)
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    val threshold = calendar.time

    // Pair: groupKey to Pair(displayLabel, DateValue)
    val grouped = objects.mapNotNull { obj ->
        try {
            val objDate = sdf.parse(obj.date)
            if (objDate != null) {
                val objCal = Calendar.getInstance().apply {
                    time = objDate
                    set(Calendar.DAY_OF_MONTH, 1)
                }
                if (objCal.time.after(threshold) || objCal.time == threshold) {
                    val groupKey = groupKeyFormat.format(objCal.time)
                    val displayLabel = displayFormat.format(objCal.time)
                    Triple(groupKey, displayLabel, obj)
                } else null
            } else null
        } catch (e: Exception) {
            null
        }
    }
        .groupBy({ it.first to it.second }, { it.third }) // group by (groupKey, displayLabel)

    // Sort by groupKey (yyyy-MM) and return a LinkedHashMap with displayLabel as key
    return grouped
        .toSortedMap(compareBy { it.first })
        .mapKeys { it.key.second } // use displayLabel ("Oct", "Sep", etc.) as key
}

private fun getMonthlyData(
    data: Pair<List<DateValue>, List<DateValue>>,
    period: Period
): List<Bars> {
    val filteredFirstBarData = filterAndGroupByMonth(data.first, period.countOfMonths)
    val filteredSecondBarData = filterAndGroupByMonth(data.second, period.countOfMonths)
    val barsData = mutableListOf<Bars>()
    filteredFirstBarData.forEach {
        barsData.add(
            Bars(
                label = it.key,
                values = listOf(
                    Data(value = it.value.sumOf { it.value }),
                    Data(value = filteredSecondBarData[it.key]?.sumOf { it.value } ?: 0.0),
                )
            )
        )
    }
    // second pass to add missing months that are only in second data set
    filteredSecondBarData.forEach {
        if (!filteredFirstBarData.containsKey(it.key)) {
            barsData.add(
                Bars(
                    label = it.key,
                    values = listOf(
                        Data(value = 0.0),
                        Data(value = it.value.sumOf { it.value }),
                    )
                )
            )
        }
    }
    return barsData
}

enum class NegativeValuesPosition {
    REGULAR,
    REVERSED
}