package com.tangerine.account.presentation.ui.tab

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.fintexinc.core.domain.model.Account
import com.fintexinc.core.domain.model.PerformanceItem
import com.fintexinc.core.presentation.ui.widget.ColumnWithBorder
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.components.UniversalDataSection
import com.fintexinc.core.ui.font.FontStyles
import com.fintexinc.core.ui.models.DataSectionItemUi
import com.tangerine.account.R
import com.tangerine.account.presentation.models.ReturnsItemUi
import com.tangerine.charts.compose_charts.PerformanceChartUI
import kotlinx.collections.immutable.ImmutableList

@Composable
fun SummaryUI(
    account: Account,
    performanceData: List<PerformanceItem>,
    returnsData: ImmutableList<ReturnsItemUi>,
    holdingsData: ImmutableList<ReturnsItemUi>,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        AccountBalanceUI(performanceData)

        Spacer(modifier = Modifier.height(40.dp))

        val items = listOf(
            DataSectionItemUi(
                label = "Scotia U.S Equity Index\nTracker ETF",
                value = "50%",
                hasInfoIcon = false,
                isMultiline = true,
                showColorDot = true,
                dotColor = Color(0xFF921616),
            ),
            DataSectionItemUi(
                label = "Scotia International\nEquity Index Tracker ETF",
                value = "29%",
                hasInfoIcon = false,
                isMultiline = true,
                showColorDot = true,
                dotColor = Color(0xFFE57373),
            ),
            DataSectionItemUi(
                label = "Scotia Emerging Markets\nEquity Index EF",
                value = "15%",
                hasInfoIcon = false,
                isMultiline = true,
                showColorDot = true,
                dotColor = Color(0xFFFEAC5B),
            ),
            DataSectionItemUi(
                label = "Scotia Canadian Large Cap\nEquity Index Tracker ETF",
                value = "6%",
                isMultiline = true,
                showColorDot = true,
                dotColor = Color(0xFFB2EBF2),
            )
        )

        Text(
            text = stringResource(R.string.title_balanced_core_portfolio),
            color = Colors.Text,
            style = FontStyles.TitleLarge,
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        Spacer(modifier = Modifier.height(24.dp))

        UniversalDataSection(
            title = stringResource(R.string.title_allocation_holdings),
            items = items,
            showProgressBar = true,
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(R.string.text_info_allocations),
            style = FontStyles.BodySmallItalic,
            color = Colors.BorderInformation,
            modifier = Modifier.padding(horizontal = 26.dp),
        )

        Spacer(modifier = Modifier.height(24.dp))

        ReturnsOverTimeSection(
            title = stringResource(R.string.text_returns_over_time),
            returnsItems = returnsData,
            holdingsItems = holdingsData,
            footerText = "Portfolio as of May 7, 2025",
            modifier = Modifier.padding(16.dp)
        )

        Spacer(modifier = Modifier.height(120.dp))

    }
}

@Composable
private fun AccountBalanceUI(performanceData: List<PerformanceItem>) {
    ColumnWithBorder {
        PerformanceChartUI(
            title = stringResource(R.string.text_account_performance),
            performance = performanceData
        )
    }
}

@Composable
private fun ReturnsOverTimeSection(
    modifier: Modifier = Modifier,
    title: String,
    returnsItems: ImmutableList<ReturnsItemUi>,
    holdingsItems: ImmutableList<ReturnsItemUi>,
    footerText: String? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Colors.Background,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        Text(
            text = title,
            style = FontStyles.TitleSmall,
            color = Colors.Text,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        returnsItems.forEachIndexed { index, item ->
            ReturnsRow(
                item = item,
                modifier = Modifier.fillMaxWidth()
            )

            if (index < returnsItems.size - 1) {
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(
                    color = Colors.BorderSubdued,
                    thickness = 1.dp
                )
                Spacer(modifier = Modifier.height(16.dp))
            } else {
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(
                    color = Colors.BorderSubdued,
                    thickness = 1.dp
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Text(
            text = stringResource(R.string.text_how_calculate),
            color = Colors.TextInteractive,
            style = FontStyles.BodyLargeBold,
        )

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = stringResource(R.string.text_holding_summary),
            style = FontStyles.TitleSmall,
            color = Colors.Text,
        )

        Spacer(modifier = Modifier.height(24.dp))

        holdingsItems.forEachIndexed { index, item ->
            ReturnsRow(
                item = item,
                modifier = Modifier.fillMaxWidth()
            )

            if (index < holdingsItems.size - 1) {
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(
                    color = Colors.BorderSubdued,
                    thickness = 1.dp
                )
                Spacer(modifier = Modifier.height(16.dp))
            } else {
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(
                    color = Colors.BorderSubdued,
                    thickness = 1.dp
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        footerText?.let {
            Text(
                text = it,
                style = FontStyles.BodySmallItalic,
                color = Colors.TextSubdued,
            )
        }
    }
}

@Composable
private fun ReturnsRow(
    modifier: Modifier = Modifier,
    item: ReturnsItemUi,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f, fill = false)
        ) {
            Text(
                text = stringResource(item.label),
                style = FontStyles.BodyMedium,
                color = Colors.TextSubdued,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f, fill = false)
            )

            if (item.hasInfoIcon) {
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    painter = painterResource(id = com.fintexinc.core.R.drawable.ic_info),
                    contentDescription = stringResource(R.string.description_info_icon),
                    modifier = Modifier.size(16.dp),
                    tint = Colors.TextInteractive
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (item.showArrow && item.isPositive != null) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow),
                    contentDescription = null,
                    modifier = Modifier
                        .size(16.dp)
                        .rotate(if (item.isPositive) 180f else 0f),
                    tint = if (item.isPositive) Colors.TextSuccess else Colors.TextCritical,
                )
                Spacer(modifier = Modifier.width(8.dp))
            }

            Text(
                text = item.amount,
                style = FontStyles.BodyLarge,
                color = when {
                    item.showArrow && item.isPositive != null -> {
                        if (item.isPositive) Colors.TextSuccess else Colors.TextCritical
                    }

                    item.amount.startsWith("+") -> Colors.TextSuccess
                    item.amount.startsWith("-") -> Colors.TextCritical
                    else -> Colors.Text
                }
            )

            item.percentage?.let {
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = it,
                    style = FontStyles.BodyMedium,
                    color = if (item.isPositive == true) Colors.TextSuccess else Colors.TextCritical,
                    maxLines = 1,
                )
            }
        }
    }
}