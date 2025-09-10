package com.fintexinc.dashboard.presentation.ui.widget.chart

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.fintexinc.core.data.model.DateValue
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.font.FontStyles
import com.tangerine.charts.compose_charts.TangerineCashFlowChart
import com.tangerine.charts.compose_charts.TangerineNetWorthChart
import com.tangerine.charts.compose_charts.extensions.format
import com.tangerine.charts.compose_charts.models.AnimationMode
import com.tangerine.charts.compose_charts.models.Bars
import com.tangerine.charts.compose_charts.models.Bars.Data
import com.tangerine.charts.compose_charts.models.DividerProperties
import com.tangerine.charts.compose_charts.models.GridProperties
import com.tangerine.charts.compose_charts.models.HorizontalIndicatorProperties
import com.tangerine.charts.compose_charts.models.IndicatorCount
import com.tangerine.charts.compose_charts.models.LabelHelperProperties
import com.tangerine.charts.compose_charts.models.PopupProperties
import com.tangerine.charts.compose_charts.models.StrokeStyle
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun TangerineBarChart(
    type: ChartType,
    period: Period,
    data: Pair<List<DateValue>, List<DateValue>>,
    enabledColors: Pair<Color, Color>,
    disabledColors: Pair<Color, Color>,
) {
    val months = when (period) {
        Period.ONE_MONTH -> 1
        Period.THREE_MONTHS -> 3
        Period.SIX_MONTHS -> 6
        Period.ONE_YEAR -> 12
        Period.ALL -> 12
    }
    val filteredFirstBarData = filterAndGroupByMonth(data.first, months)
    val filteredSecondBarData = filterAndGroupByMonth(data.second, months)
    val barsData = mutableListOf<Bars>()
    filteredFirstBarData.forEach {
        barsData.add(
            Bars(
                label = it.key,
                values = listOf(
                    Data(
                        value = it.value.sumOf { it.value },
                        color = SolidColor(enabledColors.first)
                    ),
                    Data(
                        value = filteredSecondBarData[it.key]?.sumOf { it.value } ?: 0.0,
                        color =  SolidColor(enabledColors.second)
                    ),
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
                        Data(
                            value = 0.0,
                            color = SolidColor(enabledColors.first)
                        ),
                        Data(
                            value = it.value.sumOf { it.value },
                            color =  SolidColor(enabledColors.second)
                        ),
                    )
                )
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 16.dp)
    ) {
        when(type) {
            ChartType.NET_WORTH -> {
                TangerineNetWorthChart(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(148.dp),
                    barsData,
                    indicatorProperties = HorizontalIndicatorProperties(
                        count = IndicatorCount.StepBased(
                            200000.0
                        ), contentBuilder = {
                            if (it == 1000000.0) {
                                return@HorizontalIndicatorProperties "$1M"
                            }
                            "$" + (it / 1000).format(0) + "K"
                        }),
                    gridProperties = GridProperties(
                        xAxisProperties = GridProperties.AxisProperties(
                            style = StrokeStyle.Dashed(
                                intervals = floatArrayOf(
                                    30f,
                                    30f
                                ), phase = 25f
                            )
                        ),
                        yAxisProperties = GridProperties.AxisProperties(enabled = false)
                    ),
                    labelHelperProperties = LabelHelperProperties(false),
                    dividerProperties = DividerProperties(false),
                    animationMode = AnimationMode.Together(),
                    animationDelay = 0,
                    animationSpec = tween(0),
                    popupProperties = PopupProperties(
                        textStyle = FontStyles.BodySmall.copy(color = Colors.TextSubdued),
                        containerColor = Colors.Background,
                        cornerRadius = 12.dp,
                        mode = PopupProperties.Mode.PointMode(),
                        contentVerticalPadding = 10.dp,
                        contentHorizontalPadding = 12.dp
                    ),
                    enabledColors = Pair(SolidColor(enabledColors.first), SolidColor(enabledColors.second)),
                    disabledColors = Pair(SolidColor(disabledColors.first), SolidColor(disabledColors.second))
                )
            }
            ChartType.CASH_FLOW -> {
                TangerineCashFlowChart(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(148.dp),
                    barsData,
                    indicatorProperties = HorizontalIndicatorProperties(
                        count = IndicatorCount.StepBased(
                            200000.0
                        ), contentBuilder = {
                            if (it == 1000000.0) {
                                return@HorizontalIndicatorProperties "$1M"
                            }
                            "$" + (it / 1000).format(0) + "K"
                        }),
                    gridProperties = GridProperties(
                        xAxisProperties = GridProperties.AxisProperties(
                            style = StrokeStyle.Dashed(
                                intervals = floatArrayOf(
                                    30f,
                                    30f
                                ), phase = 25f
                            )
                        ),
                        yAxisProperties = GridProperties.AxisProperties(enabled = false)
                    ),
                    labelHelperProperties = LabelHelperProperties(false),
                    dividerProperties = DividerProperties(false),
                    animationMode = AnimationMode.Together(),
                    animationDelay = 0,
                    animationSpec = tween(0),
                    popupProperties = PopupProperties(
                        textStyle = FontStyles.BodySmall.copy(color = Colors.TextSubdued),
                        containerColor = Colors.Background,
                        cornerRadius = 12.dp,
                        mode = PopupProperties.Mode.PointMode(),
                        contentVerticalPadding = 10.dp,
                        contentHorizontalPadding = 12.dp
                    ),
                    enabledColors = Pair(SolidColor(enabledColors.first), SolidColor(enabledColors.second)),
                    disabledColors = Pair(SolidColor(disabledColors.first), SolidColor(disabledColors.second)),
                    minValue = -600000.0,
                    maxValue = 1000000.0
                )
            }
        }
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

private fun getColorForIndex(
    index: Int?,
    currentIndex: Int,
    colorEnabled: Color,
    colorDisabled: Color
): SolidColor {
    return if (index == currentIndex || index == null) {
        SolidColor(colorEnabled)
    } else {
        SolidColor(colorDisabled)
    }
}

enum class ChartType {
    NET_WORTH,
    CASH_FLOW
}