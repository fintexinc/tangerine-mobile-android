package com.tangerine.charts.compose_charts

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fintexinc.core.R
import com.fintexinc.core.data.utils.currency.formatCurrency
import com.fintexinc.core.data.utils.date.DateUtils.monthName
import com.fintexinc.core.domain.model.PerformanceItem
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.font.FontStyles

@Composable
fun PerformanceChartUI(
    title: String,
    performance: List<PerformanceItem>
) {
    val period = remember {
        mutableStateOf(Period.SIX_MONTHS)
    }
    val performanceValue = remember {
        mutableDoubleStateOf(performance.last().value)
    }
    val asOfDateValue = remember {
        mutableStateOf(performance.last().date)
    }
    Spacer(modifier = Modifier.height(12.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier
                .weight(1.0f)
                .wrapContentHeight(),
            text = title,
            style = FontStyles.TitleMediumMedium
        )
        Icon(
            painter = painterResource(R.drawable.ic_info),
            contentDescription = stringResource(R.string.description_info_icon),
            modifier = Modifier.size(24.dp),
            tint = Colors.TextInteractive,
        )
    }
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = stringResource(
            id = R.string.format_performance_as_of_date,
            asOfDateValue.value.day, asOfDateValue.value.month.monthName(), asOfDateValue.value.year
        ),
        color = Colors.TextSubdued,
        style = FontStyles.BodySmallBold,
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
        modifier = Modifier.wrapContentSize(),
        text = performanceValue.doubleValue.formatCurrency(),
        style = FontStyles.DisplaySmall,
        color = Colors.Primary,
    )
    Spacer(modifier = Modifier.height(18.dp))

    TangerineLineChart(
        performance = performance,
        period = period.value,
        onIndexSelected = {
            if (it in performance.indices) {
                val performanceRanged = when (period.value) {
                    Period.ONE_MONTH -> {
                        performance.subList(performance.size - 3, performance.size)
                    }

                    Period.THREE_MONTHS -> {
                        performance.subList(performance.size - 5, performance.size)
                    }

                    Period.SIX_MONTHS -> {
                        performance.subList(performance.size - 8, performance.size)
                    }

                    Period.ONE_YEAR -> {
                        performance
                    }

                    else -> performance
                }
                performanceValue.doubleValue = performanceRanged[it].value
                asOfDateValue.value = performanceRanged[it].date
            }
        }
    )
    Spacer(modifier = Modifier.height(18.dp))
    ChartPeriodSelector(
        onPeriodSelected = {
            period.value = it
        }
    )
}