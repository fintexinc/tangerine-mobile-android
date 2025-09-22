package com.tangerine.account.presentation.ui.tab

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fintexinc.core.data.model.NameValue
import com.fintexinc.core.data.utils.currency.formatCurrency
import com.fintexinc.core.domain.model.Account
import com.fintexinc.core.presentation.ui.widget.ColumnWithBorder
import com.fintexinc.core.presentation.ui.widget.list.SimpleNameValueItem
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.font.FontStyles
import com.tangerine.account.R
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
        Spacer(modifier = Modifier.height(12.dp))
        // TODO: figure out which data to show here
        AccountDetailsUI(
            listOf(
                NameValue(
                    name = "Net contributions",
                    value = "$10,000.00"
                ),
                NameValue(
                    name = "Transfers",
                    value = "$10,000.00"
                ),
                NameValue(
                    name = "Transferred Out",
                    value = "$10,000.00"
                ),
                NameValue(
                    name = "Account Capital Gain/Loss",
                    value = "$10,000.00"
                ),
                NameValue(
                    name = "Account Return",
                    value = "+33.33%"
                ),
            )
        )
        Spacer(modifier = Modifier.height(18.dp))
        InvestorProfileUI(
            account = account,
            title = stringResource(R.string.text_investor_profile),
            buttonText = stringResource(R.string.button_edit_profile)
        )
        Spacer(modifier = Modifier.height(18.dp))
        AccountDetailsUI(
            title = stringResource(R.string.text_account_details)
        )
        Spacer(modifier = Modifier.height(48.dp))
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
                painter = painterResource(R.drawable.ic_info),
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

@Composable
private fun AccountDetailsUI(details: List<NameValue>) {
    ColumnWithBorder {
        details.forEach { detail ->
            SimpleNameValueItem(
                item = detail,
                isLastItem = detail == details.last()
            )
        }
    }
}

@Composable
private fun InvestorProfileUI(
    account: Account,
    title: String,
    buttonText: String,
    onEditProfile: () -> Unit = {}
) {
    val expanded = remember {
        mutableStateOf(true)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 18.dp)
            .then(
                if (expanded.value) {
                    Modifier
                        .background(
                            color = Colors.BackgroundSubdued,
                            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = Colors.BorderSubdued,
                            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                        )
                } else {
                    Modifier
                        .background(
                            color = Colors.BackgroundSubdued,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = Colors.BorderSubdued,
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
            contentDescription = stringResource(R.string.description_icon_navigate_expand),
        )
    }
    if (expanded.value) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 18.dp)
                .background(
                    color = Colors.Background,
                    shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
                )
                .border(
                    width = 1.dp,
                    color = Colors.BorderSubdued,
                    shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
                )
                .padding(18.dp)
        ) {
            val data = listOf(
                NameValue(
                    name = stringResource(
                        R.string.text_income
                    ),
                    value = account.income.toDouble().formatCurrency()
                ),
                NameValue(
                    name = stringResource(
                        R.string.text_household_net_worth
                    ),
                    value = account.householdNetWorth.toDouble().formatCurrency()
                ),
                NameValue(
                    name = stringResource(
                        R.string.text_investment_knowledge
                    ),
                    value = account.investmentKnowledge
                ),
                NameValue(
                    name = stringResource(
                        R.string.text_risk_tolerance_a
                    ),
                    value = account.riskToleranceInitial
                ),
                NameValue(
                    name = stringResource(
                        R.string.text_risk_tolerance_b
                    ),
                    value = account.riskToleranceCurrent
                ),
                NameValue(
                    name = stringResource(
                        R.string.text_overall_risk_tolerance
                    ),
                    value = account.overallRiskTolerance
                ),
                NameValue(
                    name = stringResource(
                        R.string.text_investment_objective
                    ),
                    value = account.investmentObjective
                ),
                NameValue(
                    name = stringResource(
                        R.string.text_time_horizon
                    ),
                    value = account.timeHorizon
                ),
                NameValue(
                    name = stringResource(
                        R.string.text_living_expenses
                    ),
                    value = account.livingExpenses.toDouble().formatCurrency()
                )
            )
            data.forEach { item ->
                SimpleNameValueItem(
                    item = item
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .background(
                            color = Colors.Transparent,
                            shape = RoundedCornerShape(40.dp)
                        )
                        .border(
                            width = 2.dp,
                            color = Colors.Primary,
                            shape = RoundedCornerShape(40.dp)
                        )
                        .clickable {
                            onEditProfile()
                        }
                        .padding(12.dp)
                        .align(Alignment.Center),
                    text = buttonText,
                    textAlign = TextAlign.Center,
                    style = FontStyles.HeadingLarge,
                    color = Colors.Primary
                )
            }
        }
    }
}

@Composable
private fun AccountDetailsUI(
    title: String
) {
    val expanded = remember {
        mutableStateOf(true)
    }
    val data = listOf(
        NameValue(
            name = "Account type",
            value = "TFSA"
        ),
        NameValue(
            name = "Account Number",
            value = "T-20250814-001"
        ),
        NameValue(
            name = "Nickname",
            value = "Test"
        ),
        NameValue(
            name = "Account registration",
            value = "2025-08-16"
        ),
        NameValue(
            name = "Inception date",
            value = "2025-08-16"
        ),
        NameValue(
            name = "Beneficiaries",
            value = "Test"
        ),
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 18.dp)
            .then(
                if (expanded.value) {
                    Modifier
                        .background(
                            color = Colors.BackgroundSubdued,
                            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = Colors.BorderSubdued,
                            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                        )
                } else {
                    Modifier
                        .background(
                            color = Colors.BackgroundSubdued,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = Colors.BorderSubdued,
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
            contentDescription = stringResource(R.string.description_icon_navigate_expand),
        )
    }
    if (expanded.value) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 18.dp)
                .background(
                    color = Colors.Background,
                    shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
                )
                .border(
                    width = 1.dp,
                    color = Colors.BorderSubdued,
                    shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
                )
                .padding(18.dp)
        ) {
            data.forEach { item ->
                SimpleNameValueItem(
                    item = item,
                    isLastItem = item == data.last()
                )
            }
        }
    }
}