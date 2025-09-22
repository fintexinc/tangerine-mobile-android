package com.fintexinc.dashboard.presentation.ui.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fintexinc.core.data.utils.currency.formatCurrency
import com.fintexinc.core.domain.model.Account
import com.fintexinc.core.data.model.DataPoint
import com.fintexinc.core.presentation.ui.datapoint.DataPointUI
import com.fintexinc.core.presentation.ui.widget.ColumnWithBorder
import com.fintexinc.core.presentation.ui.widget.RowWithShadow
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.font.FontStyles
import com.fintexinc.dashboard.R
import com.fintexinc.dashboard.presentation.ui.widget.chart.ChartPeriodSelector
import com.fintexinc.dashboard.presentation.ui.widget.chart.TangerinePieChart
import com.tangerine.charts.compose_charts.LineChart
import com.tangerine.charts.compose_charts.extensions.format
import com.tangerine.charts.compose_charts.models.DrawStyle
import com.tangerine.charts.compose_charts.models.GridProperties
import com.tangerine.charts.compose_charts.models.HorizontalIndicatorProperties
import com.tangerine.charts.compose_charts.models.IndicatorCount
import com.tangerine.charts.compose_charts.models.LabelHelperProperties
import com.tangerine.charts.compose_charts.models.LabelProperties
import com.tangerine.charts.compose_charts.models.Line
import com.tangerine.charts.compose_charts.models.Pie
import com.tangerine.charts.compose_charts.models.PopupProperties

@Composable
fun MyPortfolioUI(accounts: List<Account>, onOpenAccount: (accountId: String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(18.dp))
        RowWithShadow {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentHeight()
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    text = stringResource(R.string.text_new_market_insights),
                    style = FontStyles.BodyLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    text = stringResource(R.string.text_check_in_update),
                    style = FontStyles.BodyMedium,
                    color = Colors.TextSubdued
                )
            }
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(com.fintexinc.core.R.drawable.ic_close),
                tint = Colors.BrandBlack,
                contentDescription = stringResource(R.string.description_icon_close)
            )
        }
        Spacer(modifier = Modifier.height(18.dp))
        Charts()
        Spacer(modifier = Modifier.height(18.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 18.dp)
                .clickable {
                    // Filter accounts
                }
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                text = stringResource(R.string.text_my_accounts),
                style = FontStyles.TitleMediumBold
            )
            Icon(
                modifier = Modifier.wrapContentSize(),
                painter = painterResource(com.fintexinc.core.R.drawable.ic_filter),
                tint = Colors.BrandBlack,
                contentDescription = stringResource(R.string.description_icon_filter)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        // TODO: add logic based on mocked data

        val registeredAccounts = accounts.map {
            AccountUI(
                accountId = it.accountId,
                name = it.userId,
                subName = it.accountId,
                value = it.income.formatCurrency(),
                valueChange = 832.01,
                percentageChange = 4.39
            )
        }
        AccountListUI(
            title = stringResource(
                R.string.format_registered_accounts, registeredAccounts.size
            ),
            accounts = registeredAccounts,
            onOpenAccount = onOpenAccount
        )
        // TODO: ask for unregistered accounts
        Spacer(modifier = Modifier.height(10.dp))
        val nonRegisteredAccounts = listOf(
            AccountUI(
                registeredAccounts[0].accountId,
                name = "Jack Dawson TFSA",
                subName = "39024242",
                value = "20,000 CAD",
                valueChange = 832.01,
                percentageChange = 4.39
            ),
            AccountUI(
                registeredAccounts[1].accountId,
                name = "Jack Dawson TFSA",
                subName = "39024242",
                value = "20,000 CAD",
                valueChange = 832.01,
                percentageChange = 4.39
            ),
        )
        AccountListUI(
            title = stringResource(
                R.string.format_non_registered_accounts, nonRegisteredAccounts.size
            ),
            accounts = nonRegisteredAccounts,
            onOpenAccount = onOpenAccount
        )
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 18.dp),
            text = stringResource(R.string.text_top_holdings),
            style = FontStyles.TitleMediumBold
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 18.dp),
            text = stringResource(R.string.text_largest_positions),
            style = FontStyles.BodySmall,
            color = Colors.DarkGray,
        )
        Spacer(modifier = Modifier.height(10.dp))
        TopHoldingsUI()
        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
private fun AccountListUI(
    modifier: Modifier = Modifier,
    title: String,
    accounts: List<AccountUI>,
    onOpenAccount: (accountId: String) -> Unit
) {
    val expanded = remember {
        mutableStateOf(true)
    }
    ColumnWithBorder(contentPadding = PaddingValues(0.dp)) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .then(
                    if (expanded.value) {
                        Modifier.background(
                            color = Colors.BackgroundSubdued,
                            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                        )
                    } else {
                        Modifier.background(
                            color = Colors.BackgroundSubdued,
                            shape = RoundedCornerShape(16.dp)
                        )
                    }
                )
                .clickable {
                    expanded.value = !expanded.value
                }
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = title,
                style = FontStyles.BodyLargeBold,
                color = Colors.BrandBlack
            )
            Icon(
                painter = painterResource(com.fintexinc.core.R.drawable.ic_arrow_down),
                tint = Colors.BrandBlack,
                contentDescription = stringResource(R.string.description_icon_expand)
            )
        }
        if (expanded.value) {
            accounts.forEach { account ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clickable {
                            onOpenAccount(account.accountId)
                        }
                        .padding(horizontal = 12.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(
                        modifier = Modifier.weight(0.4F)
                    ) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            text = account.name,
                            style = FontStyles.BodyLarge,
                            color = Colors.Text
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            text = account.subName,
                            style = FontStyles.BodyMedium,
                            color = Colors.TextSubdued
                        )
                    }
                    Column(
                        modifier = Modifier
                            .weight(0.5f)
                            .wrapContentHeight()
                    ) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            text = account.value,
                            style = FontStyles.BodyLargeBold,
                            color = Colors.Text,
                            textAlign = TextAlign.End
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End
                        ) {
                            Icon(
                                modifier = Modifier.size(20.dp),
                                painter = painterResource(
                                    if (account.valueChange >= 0) {
                                        com.fintexinc.core.R.drawable.ic_arrow_up
                                    } else {
                                        com.fintexinc.core.R.drawable.ic_arrow_down
                                    }
                                ),
                                tint = if (account.valueChange >= 0) {
                                    Color(0xFF43A047)
                                } else {
                                    Color(0xFFD50000)
                                },
                                contentDescription = stringResource(R.string.description_icon_increase)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                modifier = Modifier.wrapContentSize(),
                                text = String.format(
                                    "$%.2f (%.2f%%)",
                                    account.valueChange,
                                    account.percentageChange
                                ),
                                style = FontStyles.BodyMedium,
                                color = Colors.TextSubdued,
                                textAlign = TextAlign.End
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        modifier = Modifier.wrapContentSize(),
                        painter = painterResource(com.fintexinc.core.R.drawable.ic_arrow_right),
                        tint = Colors.BackgroundPrimary,
                        contentDescription = stringResource(R.string.description_view_details)
                    )
                }
            }
        }
    }
}

@Composable
private fun Charts() {
    val pagerState = rememberPagerState { 4 }
    HorizontalPager(pagerState) {
        ColumnWithBorder {
            when (it) {
                0 -> PerformanceChartUI()
                1 -> SectionExposureChartUI()
                2 -> AssetMixChartUI()
                3 -> GeographicExposure()
            }
            Spacer(modifier = Modifier.height(18.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.Center,
            ) {
                repeat(pagerState.pageCount) { iteration ->
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .then(
                                if (pagerState.currentPage == iteration) {
                                    Modifier.background(Colors.BackgroundPrimary)
                                } else {
                                    Modifier
                                        .border(
                                            1.dp, Colors.BackgroundPrimary, CircleShape
                                        )
                                        .animateContentSize(tween(1000))
                                }
                            )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
    }
}

@Composable
private fun PerformanceChartUI() {
    Spacer(modifier = Modifier.height(12.dp))
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        text = stringResource(R.string.text_performance),
        style = FontStyles.BodyLarge
    )
    Spacer(modifier = Modifier.height(12.dp))
    Row(
        modifier = Modifier
            .wrapContentSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.wrapContentSize(),
            painter = painterResource(com.fintexinc.core.R.drawable.ic_arrow_up),
            tint = Color(0xFF43A047),
            contentDescription = stringResource(R.string.description_icon_increase)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            modifier = Modifier.wrapContentSize(),
            text = "$2,000.00",
            style = FontStyles.TitleSmall
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            modifier = Modifier.wrapContentSize(),
            text = stringResource(R.string.text_earned_this_month),
            style = FontStyles.BodyMedium,
            color = Colors.TextSubdued
        )
    }
    Spacer(modifier = Modifier.height(18.dp))
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
        labelHelperProperties = LabelHelperProperties(false),
        popupProperties = PopupProperties(
            textStyle = FontStyles.BodySmall.copy(color = Colors.TextSubdued),
            containerColor = Colors.Background,
            mode = PopupProperties.Mode.PointMode(),
            contentVerticalPadding = 10.dp,
            contentHorizontalPadding = 12.dp
        )
    )
    Spacer(modifier = Modifier.height(18.dp))
    ChartPeriodSelector(
        onPeriodSelected = {
            // Handle period selection
        }
    )
}

@Composable
private fun SectionExposureChartUI() {
    TangerinePieChart(
        title = stringResource(R.string.text_sector_exposure),
        pieData = listOf(
            Pie("Technology", 50.0, color = Color(0xFF0D7C75)),
            Pie("Finance", 30.0, color = Color(0xFFFEAC5B)),
            Pie("Healthcare", 20.0, color = Color(0xFFEC407A))
        )
    )
}

@Composable
private fun AssetMixChartUI() {
    TangerinePieChart(
        title = stringResource(R.string.text_asset_mix),
        pieData = listOf(
            Pie("Stocks", 40.0, color = Color(0xFFE57373)),
            Pie("Bonds", 40.0, color = Color(0xFF880E4F)),
            Pie("Futures", 20.0, color = Colors.BackgroundPrimary),
        )
    )
}

@Composable
private fun GeographicExposure() {
    TangerinePieChart(
        title = stringResource(R.string.text_geographic_exposure),
        pieData = listOf(
            Pie("North America", 65.0, color = Color(0xFF075BBC)),
            Pie("Europe", 35.0, color = Color(0xFFEC407A)),
        )
    )
}

@Composable
private fun TopHoldingsUI() {
    ColumnWithBorder {
        val dataPoints = listOf(
            DataPoint("1", "Bank of Nova Scotia", "BNS", "20,000 CAD", " (4.39%)"),
            DataPoint("2", "Bank of Nova Scotia", "BNS", "20,000 CAD", " (4.39%)"),
            DataPoint("3", "Bank of Nova Scotia", "BNS", "20,000 CAD", " (4.39%)"),
            DataPoint("4", "Bank of Nova Scotia", "BNS", "20,000 CAD", " (4.39%)"),
            DataPoint("5", "Bank of Nova Scotia", "BNS", "20,000 CAD", " (4.39%)")
        )
        dataPoints.forEachIndexed { index, dataPoint ->
            DataPointUI(
                dataPoint = dataPoint,
                isLastItem = dataPoints.size - 1 == index
            )
        }
    }
}

data class AccountUI(
    val accountId: String,
    val name: String,
    val subName: String,
    val value: String,
    val valueChange: Double,
    val percentageChange: Double
)