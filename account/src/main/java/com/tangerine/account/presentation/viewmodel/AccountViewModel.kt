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
            val performanceData = accountGateway.getPerformanceData()

            val returnsItems = calculateReturns(performanceItems = performanceData)
            val holdingsItems = calculateHoldings(performanceData = performanceData)

            _state.value = State.Summary(
                data = account,
                returnsItems = returnsItems.toImmutableList(),
                holdingsItems = holdingsItems.toImmutableList()
            )
        }
    }

    private fun calculateReturns(
        performanceItems: List<PerformanceItem>,
        currentMonth: Int = 8,
        currentYear: Int = 2025
    ): List<ReturnsItemUi> {
        val monthlyTotals = performanceItems
            .groupBy { it.date }
            .mapValues { entry ->
                entry.value.sumOf { it.value }
            }

        val currentValue = monthlyTotals[PerformanceDate(currentMonth, currentYear)] ?: 0.0
        val oneMonthAgo = monthlyTotals[PerformanceDate(currentMonth - 1, currentYear)] ?: 0.0
        val threeMonthsAgo = monthlyTotals[PerformanceDate(currentMonth - 3, currentYear)] ?: 0.0
        val sixMonthsAgo = monthlyTotals[PerformanceDate(currentMonth - 6, currentYear)] ?: 0.0
        val yearStart = monthlyTotals[PerformanceDate(1, currentYear)] ?: 0.0
        val oneYearAgo = monthlyTotals[PerformanceDate(currentMonth, currentYear - 1)] ?: 0.0
        val portfolioStart = monthlyTotals.values.minOrNull() ?: 0.0

        return listOf(
            createReturnItem("1 month", currentValue, oneMonthAgo),
            createReturnItem("3 months", currentValue, threeMonthsAgo),
            createReturnItem("6 months", currentValue, sixMonthsAgo),
            createReturnItem("Year to date", currentValue, yearStart),
            createReturnItem("1 Year", currentValue, oneYearAgo),
            createReturnItem(
                label = "Current portfolio to date",
                currentValue = currentValue,
                previousValue = portfolioStart,
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
        val currentMarketValue = performanceData
            .maxByOrNull { it.date.year * 12 + it.date.month }
            ?.let { latestItem ->
                performanceData
                    .filter { it.date == latestItem.date }
                    .sumOf { it.value }
            } ?: 0.0

        return listOf(
            ReturnsItemUi(
                label = context.getString(R.string.text_market_value),
                amount = formatCurrency(currentMarketValue),
                hasInfoIcon = true,
                showArrow = false
            ),
            ReturnsItemUi(
                label = context.getString(R.string.text_distributions),
                amount = "+${formatCurrency(21234.56)}",
                hasInfoIcon = true,
                showArrow = false
            ),
            ReturnsItemUi(
                label = context.getString(R.string.text_book_value),
                amount = formatCurrency(2733.30),
                hasInfoIcon = true,
                showArrow = false
            ),
            ReturnsItemUi(
                label = context.getString(R.string.text_total_units),
                amount = "432", // TODO() mock data
                showArrow = false
            ),
            ReturnsItemUi(
                label = context.getString(R.string.text_unit_price),
                amount = formatCurrency(233.22),
                showArrow = false
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