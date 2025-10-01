package com.tangerine.account.presentation.ui.tab

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fintexinc.core.domain.model.Account
import com.fintexinc.core.presentation.ui.widget.ColumnWithBorder
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.font.FontStyles
import com.tangerine.account.R
import com.tangerine.account.presentation.models.DataSectionItemUi
import com.tangerine.account.presentation.ui.components.UniversalDataSection
import com.tangerine.charts.compose_charts.LineChart
import com.tangerine.charts.compose_charts.extensions.format
import com.tangerine.charts.compose_charts.models.DrawStyle
import com.tangerine.charts.compose_charts.models.GridProperties
import com.tangerine.charts.compose_charts.models.HorizontalIndicatorProperties
import com.tangerine.charts.compose_charts.models.IndicatorCount
import com.tangerine.charts.compose_charts.models.LabelHelperProperties
import com.tangerine.charts.compose_charts.models.LabelProperties
import com.tangerine.charts.compose_charts.models.Line

@Composable
fun SummaryUI(account: Account) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        AccountBalanceUI()

        Spacer(modifier = Modifier.height(24.dp))

        val itemsData = listOf(
            DataSectionItemUi(
                label = stringResource(R.string.text_earning_to_date),
                value = "+$21,234.56",
                valueColor = Colors.TransactionIncome,
                hasInfoIcon = true,
                valueStyle = FontStyles.TitleSmall,
                isHighlighted = true
            ),
            DataSectionItemUi(
                label = stringResource(R.string.text_net_investment_to_date),
                value = "$756,412.30",
                hasInfoIcon = true
            ),
            DataSectionItemUi(
                label = stringResource(R.string.text_account_capital_gain_loss),
                value = "$10,000.00"
            ),
            DataSectionItemUi(
                label = stringResource(R.string.text_account_return),
                value = "+33.33%",
                hasInfoIcon = true
            ),
            DataSectionItemUi(
                label = stringResource(R.string.text_total_units),
                value = "650.4051"
            ),
            DataSectionItemUi(
                label = stringResource(R.string.text_unit_price),
                value = "$12.5046",
                hasInfoIcon = true
            )
        )

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
                label = "Scotia Emerging Markets\nEquityIndex EF",
                value = "15%",
                hasInfoIcon = false,
                isMultiline = true,
                showColorDot = true,
                dotColor = Color(0xFFFEAC5B),
            ),
            DataSectionItemUi(
                label = "Scotia Canadian Large Cap\nEquity Index Tracker ETF",
                value = "6%",
                isMultiline = false,
                showColorDot = true,
                dotColor = Color(0xFFB2EBF2),
            )
        )

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
            modifier = Modifier.padding(horizontal = 18.dp),
        )

        Spacer(modifier = Modifier.height(24.dp))

        UniversalDataSection(
            title = stringResource(R.string.text_account_return),
            items = itemsData,
            footerText = stringResource(R.string.text_portfolio_as_of_date)
        )

        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
private fun AccountBalanceUI() {
    ColumnWithBorder {
        val showNetworkContribution = remember {
            mutableStateOf(false)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.wrapContentSize(),
                text = stringResource(R.string.text_account_balance),
                style = FontStyles.BodyLarge,
                color = Colors.BrandBlack
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                modifier = Modifier
                    .wrapContentSize()
                    .clickable {
                        showNetworkContribution.value = !showNetworkContribution.value
                    },
                painter = painterResource(com.fintexinc.core.R.drawable.ic_info),
                contentDescription = stringResource(R.string.description_icon_navigate_info),
                tint = Colors.BrandBlack
            )
        }
        Spacer(modifier = Modifier.height(18.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            text = "$455,000",
            style = FontStyles.DisplaySmall
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.wrapContentSize(), verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.wrapContentSize(),
                painter = painterResource(com.fintexinc.core.R.drawable.ic_arrow_up),
                tint = Color(0xFF43A047),
                contentDescription = stringResource(R.string.description_icon_navigate_increased)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                modifier = Modifier.wrapContentSize(),
                text = "$2,000.00 (4.39)",
                style = FontStyles.TitleSmall
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                modifier = Modifier.wrapContentSize(),
                text = stringResource(R.string.text_past_month),
                style = FontStyles.BodyMedium,
                color = Colors.TextSubdued
            )
        }
        Spacer(modifier = Modifier.height(18.dp))
        AccountBalanceChartUI(showNetworkContribution.value)
    }
}

@Composable
private fun AccountBalanceChartUI(showNetworkContribution: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        LineChart(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            data = listOf(
                Line(
                    label = "Performance",
                    values = listOf(1000.0, 15000.0),
                    color = SolidColor(Color(0xFFEA7024)),
                    firstGradientFillColor = Color(0xFFFEC388),
                    secondGradientFillColor = Color(0x00FFFFFF),
                    curvedEdges = false,
                    drawStyle = DrawStyle.Fill
                )
            ),
            gridProperties = GridProperties(false),
            indicatorProperties = HorizontalIndicatorProperties(
                count = IndicatorCount.StepBased(
                    3000.0
                ), contentBuilder = {
                    (it / 1000).format(0) + "K"
                }, indicators = (listOf(1000.0, 6000.0, 15000.0))
            ),
            labelProperties = LabelProperties(enabled = true, labels = listOf("Jan", "Jun", "Dec")),
            labelHelperProperties = LabelHelperProperties(false)
        )
        if (showNetworkContribution) {

        }
    }
    Spacer(modifier = Modifier.height(18.dp))
    /*ChartPeriodSelector(
        onPeriodSelected = { period ->

        }
    )*/
}