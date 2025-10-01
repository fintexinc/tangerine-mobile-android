package com.tangerine.account.presentation.viewmodel

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fintexinc.core.data.model.NameValue
import com.fintexinc.core.domain.gateway.AccountGateway
import com.fintexinc.core.domain.model.Account
import com.fintexinc.core.domain.model.Document
import com.fintexinc.core.domain.model.PerformanceDate
import com.fintexinc.core.domain.model.PerformanceItem
import com.fintexinc.core.domain.model.Transaction
import com.tangerine.account.R
import com.tangerine.account.presentation.models.ReturnsItemUi
import com.tangerine.account.presentation.ui.AccountTab
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject
import kotlin.math.abs
import kotlin.math.absoluteValue

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val accountGateway: AccountGateway,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    private val _state: MutableStateFlow<State> = MutableStateFlow<State>(State.Loading)
    val state: StateFlow<State>
        get() = _state.asStateFlow()

    fun getData(id: String) {
        viewModelScope.launch {
            val account = accountGateway.getAccountById(id)
            val allPerformanceData = accountGateway.getPerformanceData()

            val accountPerformanceData = allPerformanceData.filter {
                it.accountId == "ACCT-INV-001" // TODO()
            }

            val returnsItems = calculateReturns(performanceItems = accountPerformanceData)
            val holdingsItems = calculateHoldings(performanceData = accountPerformanceData)

            _state.value = State.Summary(
                data = account,
                returnsItems = returnsItems.toImmutableList(),
                holdingsItems = holdingsItems.toImmutableList()
            )
        }
    }

    private fun calculateReturns(
        performanceItems: List<PerformanceItem>,
    ): List<ReturnsItemUi> {
        val latestDate = performanceItems
            .maxByOrNull { it.date.year * 12 + it.date.month }
            ?.date
            ?: return emptyList()

        // Use last data from mock data for correct calculation here. For backend need to be changed
        val currentMonth = latestDate.month
        val currentYear = latestDate.year

        val monthlyTotals = performanceItems
            .groupBy { it.date }
            .mapValues { entry ->
                entry.value.sumOf { it.value }
            }

        fun getDateMonthsAgo(monthsAgo: Int): PerformanceDate {
            var month = currentMonth - monthsAgo
            var year = currentYear

            while (month <= 0) {
                month += 12
                year -= 1
            }

            return PerformanceDate(month, year)
        }

        val currentValue = monthlyTotals[PerformanceDate(currentMonth, currentYear)] ?: 0.0
        val oneMonthAgoValue = monthlyTotals[getDateMonthsAgo(1)] ?: 0.0
        val threeMonthsAgoValue = monthlyTotals[getDateMonthsAgo(3)] ?: 0.0
        val sixMonthsAgoValue = monthlyTotals[getDateMonthsAgo(6)] ?: 0.0
        val yearStartValue = monthlyTotals[PerformanceDate(1, currentYear)] ?: 0.0
        val oneYearAgoValue = monthlyTotals[PerformanceDate(currentMonth, currentYear - 1)] ?: 0.0

        val portfolioStartValue = performanceItems
            .minByOrNull { it.date.year * 12 + it.date.month }
            ?.let { earliestItem ->
                monthlyTotals[earliestItem.date] ?: 0.0
            } ?: 0.0

        return listOf(
            createReturnItem(
                context.getString(R.string.text_month_1),
                currentValue,
                oneMonthAgoValue
            ),
            createReturnItem(
                context.getString(R.string.text_months_3),
                currentValue,
                threeMonthsAgoValue
            ),
            createReturnItem(
                context.getString(R.string.text_months_6),
                currentValue,
                sixMonthsAgoValue
            ),
            createReturnItem(
                context.getString(R.string.text_year_to_date),
                currentValue,
                yearStartValue
            ),
            createReturnItem(
                context.getString(R.string.text_year_1),
                currentValue,
                oneYearAgoValue
            ),
            createReturnItem(
                label = context.getString(R.string.text_current_portfolio_to_date),
                currentValue = currentValue,
                previousValue = portfolioStartValue,
                hasInfoIcon = true
            )
        )
    }

    private fun createReturnItem(
        label: String,
        currentValue: Double,
        previousValue: Double,
        hasInfoIcon: Boolean = false
    ): ReturnsItemUi {
        val difference = currentValue - previousValue
        val percentageChange = if (previousValue != 0.0) {
            (difference / previousValue) * 100
        } else {
            0.0
        }

        return ReturnsItemUi(
            label = label,
            amount = formatCurrency(difference.absoluteValue),
            percentage = formatPercentage(percentageChange),
            isPositive = difference >= 0,
            hasInfoIcon = hasInfoIcon,
            showArrow = true
        )
    }

    private fun calculateHoldings(
        performanceData: List<PerformanceItem>
    ): List<ReturnsItemUi> {
        val latestDate = performanceData
            .maxByOrNull { it.date.year * 12 + it.date.month }
            ?.date ?: return emptyList()

        val currentMarketValue = performanceData
            .filter { it.date == latestDate }
            .sumOf { it.value }

        val bookValue = performanceData
            .minByOrNull { it.date.year * 12 + it.date.month }
            ?.value ?: 0.0

        val distributions = currentMarketValue - bookValue

        val totalUnits = 432.0 // TODO: get from API
        val unitPrice = currentMarketValue / totalUnits

        return listOf(
            ReturnsItemUi(
                label = context.getString(R.string.text_market_value),
                amount = formatCurrency(currentMarketValue),
                hasInfoIcon = true,
                showArrow = false,
                isPositive = null
            ),
            ReturnsItemUi(
                label = context.getString(R.string.text_distributions),
                amount = if (distributions >= 0) {
                    "+${formatCurrency(distributions)}"
                } else {
                    formatCurrency(abs(distributions))
                },
                hasInfoIcon = true,
                showArrow = false,
                isPositive = distributions >= 0
            ),
            ReturnsItemUi(
                label = context.getString(R.string.text_book_value),
                amount = formatCurrency(bookValue),
                hasInfoIcon = true,
                showArrow = false,
                isPositive = null
            ),
            ReturnsItemUi(
                label = context.getString(R.string.text_total_units),
                amount = totalUnits.toInt().toString(),
                showArrow = false,
                isPositive = null
            ),
            ReturnsItemUi(
                label = context.getString(R.string.text_unit_price),
                amount = formatCurrency(unitPrice),
                showArrow = false,
                isPositive = null
            )
        )
    }

    private fun formatCurrency(value: Double, locale: Locale = Locale.getDefault()): String {
        val formatter = NumberFormat.getCurrencyInstance(locale)
        return formatter.format(value)
    }

    private fun formatPercentage(value: Double): String {
        return if (value >= 0) {
            "+${String.format("%.2f", value)}%"
        } else {
            "${String.format("%.2f", value)}%"
        }
    }

    fun onTabChanged(tab: AccountTab, accountId: String) = viewModelScope.launch {
        when (tab) {
            AccountTab.BUY_FUNDS -> {
                getData(accountId)
            }

            AccountTab.AUTOMATIC_PURCHASES -> {
                _state.value = State.Activities(accountGateway.getActivities())
            }

            AccountTab.SELL_FUNDS -> {
                _state.value = State.Positions(
                    listOf(
                        NameValue(
                            name = "Management Expense Ratio (MER)",
                            value = "1.2%"
                        ),
                        NameValue(
                            name = "Distributions",
                            value = "Annually, December"
                        ),
                        NameValue(
                            name = "Account type",
                            value = "TFSA"
                        ),
                        NameValue(
                            name = "Units",
                            value = "54"
                        ),
                        NameValue(
                            name = "Total Market Value",
                            value = "$100,00-$249,999"
                        ),
                        NameValue(
                            name = "Total Book Value",
                            value = "$1000000"
                        ),
                        NameValue(
                            name = "Gain / Loss",
                            value = "+$1,000,000",
                            valueColor = Color(0xFF136F68)
                        )
                    )
                )
            }

            AccountTab.SWITCH_PORTFOLIO -> {
                _state.value = State.Documents(accountGateway.getDocumentsByAccountId(accountId))
            }
        }
    }

    sealed class State {
        object Loading : State()
        data class Summary(
            val data: Account,
            val returnsItems: ImmutableList<ReturnsItemUi> = persistentListOf(),
            val holdingsItems: ImmutableList<ReturnsItemUi> = persistentListOf(),
        ) : State()

        data class Positions(val data: List<NameValue>) : State()
        data class Activities(val data: List<Transaction>) : State()
        data class Documents(val data: List<Document>) : State()
    }
}