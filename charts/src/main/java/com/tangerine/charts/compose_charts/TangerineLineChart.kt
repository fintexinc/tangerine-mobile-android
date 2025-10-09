package com.tangerine.charts.compose_charts

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.fintexinc.core.domain.model.PerformanceItem
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.font.FontStyles
import com.tangerine.charts.compose_charts.extensions.format
import com.tangerine.charts.compose_charts.models.DotProperties
import com.tangerine.charts.compose_charts.models.DrawStyle
import com.tangerine.charts.compose_charts.models.GridProperties
import com.tangerine.charts.compose_charts.models.HorizontalIndicatorProperties
import com.tangerine.charts.compose_charts.models.IndicatorCount
import com.tangerine.charts.compose_charts.models.LabelHelperProperties
import com.tangerine.charts.compose_charts.models.LabelProperties
import com.tangerine.charts.compose_charts.models.Line
import com.tangerine.charts.compose_charts.models.PopupProperties
import org.threeten.bp.LocalDate
import org.threeten.bp.Month
import org.threeten.bp.format.TextStyle
import java.util.Locale

@Composable
fun TangerineLineChart(
    performance: List<PerformanceItem>,
    step: Double = 10000.0,
    period: Period,
    onIndexSelected: (Int) -> Unit
) {

    val data = getPerformanceByMonth(performance, period)

    LineChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        data = listOf(
            Line(
                label = "Performance",
                values = data.map { it.value },
                dotProperties = DotProperties(
                    enabled = true,
                    radius = 4.dp,
                    color = SolidColor(Color(0xFFEA7024))
                ),
                color = SolidColor(Color(0xFFEA7024)),
                firstGradientFillColor = Color(0xFFFEC388),
                secondGradientFillColor = Color(0x00FFFFFF),
                curvedEdges = true,
                drawStyle = DrawStyle.Stroke(2.dp),
            )
        ),
        gridProperties = GridProperties(false),
        indicatorProperties = HorizontalIndicatorProperties(
            count = IndicatorCount.StepBased(
                step
            ), contentBuilder = {
                (it / 1000).format(0) + "K"
            }
        ),
        labelProperties = LabelProperties(enabled = true, labels = data.map { it.key }),
        labelHelperProperties = LabelHelperProperties(false),
        popupProperties = PopupProperties(
            textStyle = FontStyles.BodySmall.copy(color = Colors.TextSubdued),
            containerColor = Colors.Background,
            mode = PopupProperties.Mode.PointMode(),
            contentVerticalPadding = 10.dp,
            contentHorizontalPadding = 12.dp
        ),
        onValueSelected = { index, value ->
            onIndexSelected(index)
        },
    )
}

private fun getPerformanceByMonth(
    performances: List<PerformanceItem>,
    period: Period
): Map<String, Double> {
    val today = LocalDate.now()
    val monthsToInclude = period.countOfMonths
    val startDate = today.minusMonths((monthsToInclude).toLong()).withDayOfMonth(1)
    val endDate = today.withDayOfMonth(today.lengthOfMonth()) // last day of current month

    return performances
        .filter {
            val perfDate = LocalDate.of(it.date.year, it.date.month, 1)
            !perfDate.isBefore(startDate) && !perfDate.isAfter(endDate)
        }
        .groupBy { Pair(it.date.year, it.date.month) }
        .mapValues { entry -> entry.value.sumOf { it.value } }
        .toList()
        .sortedBy { (yearMonth, _) -> yearMonth.first * 100 + yearMonth.second }
        .associate { (yearMonth, sum) ->
            val monthName = Month.of(yearMonth.second)
                .getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
            monthName to sum
        }
}