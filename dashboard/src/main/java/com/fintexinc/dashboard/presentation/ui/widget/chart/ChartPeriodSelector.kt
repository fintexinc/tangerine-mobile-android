package com.fintexinc.dashboard.presentation.ui.widget.chart

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.font.FontStyles

@Composable
fun ChartPeriodSelector(
    onPeriodSelected: (Period) -> Unit
) {
    val periods = listOf(
        Period.ONE_MONTH,
        Period.THREE_MONTHS,
        Period.SIX_MONTHS,
        Period.ONE_YEAR,
        Period.ALL
    )
    val selectedPeriod = remember {
        mutableStateOf<Period>(Period.SIX_MONTHS)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        periods.forEach { period ->
            ChartPeriodItem(
                period = period,
                selectedPeriod = selectedPeriod.value,
                onPeriodSelected = {
                    selectedPeriod.value = it
                    onPeriodSelected(it)
                }
            )
            if (period != periods.last()) {
                Spacer(modifier = Modifier.padding(horizontal = 12.dp))
            }
        }
    }
}

@Composable
private fun ChartPeriodItem(
    period: Period,
    selectedPeriod: Period,
    onPeriodSelected: (Period) -> Unit
) {
    Text(
        modifier = Modifier
            .wrapContentSize()
            .background(
                color = if (selectedPeriod == period) Colors.Background else Colors.BackgroundSubdued,
                shape = CircleShape
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
            ) {
                onPeriodSelected(period)
            }
            .then(
                if (selectedPeriod == period) {
                    Modifier
                        .border(1.dp, Colors.BackgroundPrimary, CircleShape)
                } else {
                    Modifier
                }
            )
            .padding(horizontal = 8.dp, vertical = 4.dp),
        text = period.label,
        style = FontStyles.BodySmall,
        textAlign = TextAlign.Center,
    )
}

enum class Period(val label: String) {
    ONE_MONTH("1M"),
    THREE_MONTHS("3M"),
    SIX_MONTHS("6M"),
    ONE_YEAR("1Y"),
    ALL("All")
}