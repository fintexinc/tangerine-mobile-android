package com.tangerine.account.presentation.viewmodel

import androidx.annotation.DrawableRes
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
import com.tangerine.account.presentation.models.DateFilterUi
import com.tangerine.account.presentation.models.DateFilterUi.*
import com.tangerine.account.presentation.models.DocumentTypeFilterUi
import com.tangerine.account.presentation.models.ReturnsItemUi
import com.tangerine.account.presentation.models.TransactionGroup
import com.tangerine.account.presentation.models.TransactionStatusFilter
import com.tangerine.account.presentation.models.TransactionTypeFilterUi
import com.tangerine.account.presentation.models.TransactionUi
import com.tangerine.account.presentation.models.TransactionUiType
import com.tangerine.account.presentation.ui.AccountTab
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlin.math.abs
import kotlin.math.absoluteValue

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val accountGateway: AccountGateway,
) : ViewModel() {

    private val _state: MutableStateFlow<State> = MutableStateFlow<State>(State.Loading)
    val state: StateFlow<State>
        get() = _state.asStateFlow()

    fun getData(id: String) = viewModelScope.launch {
        val account = accountGateway.getAccountById(id)
        val allPerformanceData = accountGateway.getPerformanceData()

        val accountPerformanceData = allPerformanceData.filter {
            it.accountId == "ACCT-INV-001" // TODO()
        }

        val returnsItems = calculateReturns(performanceItems = accountPerformanceData)
        val holdingsItems = calculateHoldings(performanceData = accountPerformanceData)

        _state.value = State.Loaded(
            MainState(
                selectedTab = TopTab.SUMMARY,
                summary = account,
                performanceData = allPerformanceData,
                returnsItems = returnsItems.toImmutableList(),
                holdingsItems = holdingsItems.toImmutableList()
            )
        )

        loadTransactions()
        loadBottomSheetDocuments()
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
                label = R.string.text_month_1,
                currentValue = currentValue,
                previousValue = oneMonthAgoValue
            ),
            createReturnItem(
                label = R.string.text_months_3,
                currentValue = currentValue,
                previousValue = threeMonthsAgoValue
            ),
            createReturnItem(
                label = R.string.text_months_6,
                currentValue = currentValue,
                previousValue = sixMonthsAgoValue
            ),
            createReturnItem(
                label = R.string.text_year_to_date,
                currentValue = currentValue,
                previousValue = yearStartValue
            ),
            createReturnItem(
                label = R.string.text_year_1,
                currentValue = currentValue,
                previousValue = oneYearAgoValue
            ),
            createReturnItem(
                label = R.string.text_current_portfolio_to_date,
                currentValue = currentValue,
                previousValue = portfolioStartValue,
                hasInfoIcon = true
            )
        )
    }

    private fun createReturnItem(
        label: Int,
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
                label = R.string.text_market_value,
                amount = formatCurrency(currentMarketValue),
                hasInfoIcon = true,
                showArrow = false,
                isPositive = null
            ),
            ReturnsItemUi(
                label = R.string.text_distributions,
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
                label = R.string.text_book_value,
                amount = formatCurrency(bookValue),
                hasInfoIcon = true,
                showArrow = false,
                isPositive = null
            ),
            ReturnsItemUi(
                label = R.string.text_total_units,
                amount = totalUnits.toInt().toString(),
                showArrow = false,
                isPositive = null
            ),
            ReturnsItemUi(
                label = R.string.text_unit_price,
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
        val current = (_state.value as? State.Loaded)?.mainState ?: return@launch

        when (tab) {
            AccountTab.BUY_FUNDS -> {
                val account = accountGateway.getAccountById(accountId)
                _state.value = State.Loaded(
                    current.copy(
                        selectedTab = TopTab.SUMMARY,
                        summary = account
                    )
                )
            }

            AccountTab.AUTOMATIC_PURCHASES -> {
                val activities = accountGateway.getActivities()
                _state.value = State.Loaded(
                    current.copy(
                        selectedTab = TopTab.ACTIVITIES,
                        activities = activities
                    )
                )
            }

            AccountTab.SELL_FUNDS -> {
                _state.value = State.Loaded(
                    current.copy(
                        selectedTab = TopTab.POSITIONS,
                        positions = listOf(
                            NameValue("Management Expense Ratio (MER)", "1.2%"),
                            NameValue("Distributions", "Annually, December"),
                            NameValue("Account type", "TFSA"),
                            NameValue("Units", "54"),
                            NameValue("Total Market Value", "$100,00-$249,999"),
                            NameValue("Total Book Value", "$1000000"),
                            NameValue(
                                name = "Gain / Loss",
                                value = "+$1,000,000",
                                valueColor = Color(0xFF136F68)
                            )
                        )
                    )
                )
            }

            AccountTab.SWITCH_PORTFOLIO -> {
                val documents = accountGateway.getDocumentsByAccountId(accountId)
                _state.value = State.Loaded(
                    current.copy(
                        selectedTab = TopTab.DOCUMENTS,
                        documents = DocumentsState(
                            all = documents,
                            query = "",
                            filtered = documents
                        )
                    )
                )
            }
        }
    }

    private fun groupTransactions(transactions: List<TransactionUi>): Pair<List<TransactionGroup>, List<TransactionGroup>> {
        val pendingGroups = transactions
            .filter { it.type == TransactionUiType.PENDING }
            .groupBy { it.date }
            .map { (date, trans) ->
                TransactionGroup(date = date, transactions = trans)
            }
            .sortedByDescending { it.date }

        val settledGroups = transactions
            .filter { it.type == TransactionUiType.SETTLED }
            .groupBy { it.date }
            .map { (date, trans) ->
                TransactionGroup(date = date, transactions = trans)
            }
            .sortedByDescending { it.date }

        return Pair(pendingGroups, settledGroups)
    }

    fun loadTransactions() = viewModelScope.launch {
        val current = (_state.value as? State.Loaded)?.mainState ?: return@launch

        val allTransactions = getMockTransactions()
        val (pendingGroups, settledGroups) = groupTransactions(allTransactions)

        _state.value = State.Loaded(
            current.copy(
                bottomSheet = current.bottomSheet.copy(
                    transactions = TransactionsState(
                        all = allTransactions,
                        query = "",
                        pendingGroups = pendingGroups,
                        settledGroups = settledGroups
                    )
                )
            )
        )
    }

    fun onSearchTransactionsQueryChanged(query: String) {
        val current = (_state.value as? State.Loaded)?.mainState ?: return
        val bottomSheet = current.bottomSheet

        _state.value = State.Loaded(
            current.copy(
                bottomSheet = bottomSheet.copy(
                    transactions = bottomSheet.transactions.copy(query = query)
                )
            )
        )
        applyAllTransactionFilters()
    }

    //  Transaction filters

    private fun applyAllTransactionFilters() {
        val current = (_state.value as? State.Loaded)?.mainState ?: return
        val bottomSheet = current.bottomSheet
        val transactionsState = bottomSheet.transactions

        val searchFiltered = if (transactionsState.query.isBlank()) {
            transactionsState.all
        } else {
            transactionsState.all.filter { transaction ->
                transaction.description.contains(transactionsState.query, ignoreCase = true) ||
                        transaction.fromAccount.contains(transactionsState.query, ignoreCase = true)
            }
        }

        val filtered = searchFiltered.filter { transaction ->
            // Тип
            val typeOk = transactionsState.selectedTypes.contains(TransactionTypeFilterUi.ALL_TYPES) ||
                    transactionsState.selectedTypes.contains(transaction.transactionTransactionTypeFilter)

            val statusOk = transactionsState.selectedStatuses.contains(TransactionStatusFilter.ALL_STATUS) ||
                    transactionsState.selectedStatuses.contains(transaction.status)

            val dateOk = if (transactionsState.selectedDates.contains(DateFilterUi.ALL_DATES)) {
                true
            } else {
                val date = parseTransactionDate(transaction.date)
                transactionsState.selectedDates.any { filter ->
                    when (filter) {
                        LAST_30_DAYS -> isWithinDays(date, 30)
                        LAST_60_DAYS -> isWithinDays(date, 60)
                        LAST_90_DAYS -> isWithinDays(date, 90)
                        TWELVE_MONTH -> isThisYear(date)
                        CURRENT_MONTH -> isCurrentMonth(date)
                        BY_MONTH -> transactionsState.selectedMonth != null &&
                                transactionsState.selectedYear != null &&
                                isByMonth(date, transactionsState.selectedMonth, transactionsState.selectedYear)
                        else -> true
                    }
                }
            }

            typeOk && statusOk && dateOk
        }

        val (pendingGroups, settledGroups) = groupTransactions(filtered)

        _state.value = State.Loaded(
            current.copy(
                bottomSheet = bottomSheet.copy(
                    transactions = transactionsState.copy(
                        pendingGroups = pendingGroups,
                        settledGroups = settledGroups
                    )
                )
            )
        )
    }

    fun onTransactionTypeFilterChanged(types: List<TransactionTypeFilterUi>) = viewModelScope.launch {
        val current = (_state.value as? State.Loaded)?.mainState ?: return@launch
        _state.value = State.Loaded(
            current.copy(
                bottomSheet = current.bottomSheet.copy(
                    transactions = current.bottomSheet.transactions.copy(selectedTypes = types)
                )
            )
        )
        applyAllTransactionFilters()
    }

    fun onTransactionStatusFilterChanged(statuses: List<TransactionStatusFilter>) = viewModelScope.launch {
        val current = (_state.value as? State.Loaded)?.mainState ?: return@launch
        _state.value = State.Loaded(
            current.copy(
                bottomSheet = current.bottomSheet.copy(
                    transactions = current.bottomSheet.transactions.copy(selectedStatuses = statuses)
                )
            )
        )
        applyAllTransactionFilters()
    }

    fun onTransactionDateFilterChanged(
        dateFilters: List<DateFilterUi>,
        selectedMonth: Int? = null,
        selectedYear: Int? = null
    ) = viewModelScope.launch {
        val current = (_state.value as? State.Loaded)?.mainState ?: return@launch
        _state.value = State.Loaded(
            current.copy(
                bottomSheet = current.bottomSheet.copy(
                    transactions = current.bottomSheet.transactions.copy(
                        selectedDates = dateFilters,
                        selectedMonth = selectedMonth,
                        selectedYear = selectedYear
                    )
                )
            )
        )
        applyAllTransactionFilters()
    }

    private fun isCurrentMonth(date: Calendar): Boolean {
        val now = Calendar.getInstance()
        return date.get(Calendar.YEAR) == now.get(Calendar.YEAR) &&
                date.get(Calendar.MONTH) == now.get(Calendar.MONTH)
    }

    private fun isByMonth(date: Calendar, month: Int, year: Int): Boolean {
        return date.get(Calendar.YEAR) == year &&
                date.get(Calendar.MONTH) == (month - 1)
    }

    private fun parseTransactionDate(dateString: String): Calendar {
        val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH)
        val date = formatter.parse(dateString)
        return Calendar.getInstance().apply {
            time = date ?: Date()
        }
    }

    private fun isWithinDays(date: Calendar, days: Int): Boolean {
        val now = Calendar.getInstance()
        val startDate = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, -days)
        }
        return date.after(startDate) && date.before(now) || date == now
    }

    private fun isThisYear(date: Calendar): Boolean {
        val now = Calendar.getInstance()
        return date.get(Calendar.YEAR) == now.get(Calendar.YEAR)
    }

    //  Document filters

    private fun applyAllDocumentFilters() {
        val current = (_state.value as? State.Loaded)?.mainState ?: return
        val bottomSheet = current.bottomSheet
        val documentsState = bottomSheet.documents

        val filtered = documentsState.all.filter { document ->
            val typeOk = documentsState.selectedTypes.contains(DocumentTypeFilterUi.ALL_DOCUMENTS) ||
                    documentsState.selectedTypes.contains(document.type)

            val dateOk = if (documentsState.selectedDates.contains(ALL_DATES)) {
                true
            } else {
                val date = parseDocumentDate(document.subName)
                documentsState.selectedDates.any { filter ->
                    when (filter) {
                        LAST_30_DAYS -> isWithinDays(date, 30)
                        LAST_60_DAYS -> isWithinDays(date, 60)
                        LAST_90_DAYS -> isWithinDays(date, 90)
                        TWELVE_MONTH -> isThisYear(date)
                        CURRENT_MONTH -> isCurrentMonth(date)
                        BY_MONTH -> documentsState.selectedMonth != null &&
                                documentsState.selectedYear != null &&
                                isByMonth(date, documentsState.selectedMonth, documentsState.selectedYear)
                        else -> true
                    }
                }
            }

            typeOk && dateOk
        }

        _state.value = State.Loaded(
            current.copy(
                bottomSheet = bottomSheet.copy(
                    documents = documentsState.copy(filtered = filtered)
                )
            )
        )
    }

    fun onBottomSheetDocumentsDateFilterChanged(
        dateFilters: List<DateFilterUi>,
        selectedMonth: Int? = null,
        selectedYear: Int? = null
    ) = viewModelScope.launch {
        val current = (_state.value as? State.Loaded)?.mainState ?: return@launch
        _state.value = State.Loaded(
            current.copy(
                bottomSheet = current.bottomSheet.copy(
                    documents = current.bottomSheet.documents.copy(
                        selectedDates = dateFilters,
                        selectedMonth = selectedMonth,
                        selectedYear = selectedYear
                    )
                )
            )
        )
        applyAllDocumentFilters()
    }

    fun onBottomSheetDocumentsTypeFilterChanged(types: List<DocumentTypeFilterUi>) = viewModelScope.launch {
        val current = (_state.value as? State.Loaded)?.mainState ?: return@launch
        _state.value = State.Loaded(
            current.copy(
                bottomSheet = current.bottomSheet.copy(
                    documents = current.bottomSheet.documents.copy(selectedTypes = types)
                )
            )
        )
        applyAllDocumentFilters()
    }

    private fun parseDocumentDate(dateString: String): Calendar {
        val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH)
        val date = formatter.parse(dateString)
        return Calendar.getInstance().apply {
            time = date ?: Date()
        }
    }

    // ===================== MOCK DATA =====================

    private fun getMockTransactions(): List<TransactionUi> {
        return listOf(
            TransactionUi(
                id = "1",
                description = "Purchase 1- Tangerine data mock",
                fromAccount = "From: CHQSingleAutoKeep",
                amount = 66.00,
                date = "Oct 08, 2025",
                type = TransactionUiType.PENDING,
                transactionTransactionTypeFilter = TransactionTypeFilterUi.BUYS,
                status = TransactionStatusFilter.PENDING,
            ),
            TransactionUi(
                id = "2",
                description = "Purchase 2- Tangerine ...",
                fromAccount = "From: CHQSingleAutoKeep",
                amount = 50.00,
                date = "Oct 01, 2025",
                type = TransactionUiType.PENDING,
                transactionTransactionTypeFilter = TransactionTypeFilterUi.BUYS,
                status = TransactionStatusFilter.PENDING,
            ),
            TransactionUi(
                id = "3",
                description = "Transfer In 1 - Tangerine data mock",
                fromAccount = "Savings Account Transac data mock",
                amount = 2600.00,
                date = "Sep 23, 2025",
                type = TransactionUiType.SETTLED,
                additionalAmount1 = "$19.5200",
                additionalAmount2 = "133.1967",
                transactionTransactionTypeFilter = TransactionTypeFilterUi.SELLS,
                status = TransactionStatusFilter.COMPLETED,
            ),
            TransactionUi(
                id = "4",
                description = "Transfer In 2- Tangerine data mock",
                fromAccount = "Savings Account Transac data mock",
                amount = 2600.00,
                date = "Aug 24, 2025",
                type = TransactionUiType.SETTLED,
                additionalAmount1 = "$19.5200",
                transactionTransactionTypeFilter = TransactionTypeFilterUi.TRANSFER_OUT,
                status = TransactionStatusFilter.COMPLETED,
            ),
            TransactionUi(
                id = "5",
                description = "Data point 1 (Title)",
                fromAccount = "Data point 2",
                amount = 0.0,
                date = "Jul 30, 2025",
                type = TransactionUiType.PENDING,
                rightColumnTitle = "Data point 4",
                rightColumnSubtitle = "Data point 6"
            )
        )
    }

    fun loadBottomSheetDocuments() = viewModelScope.launch {
        val current = (_state.value as? State.Loaded)?.mainState ?: return@launch

        val allDocuments = getMockBottomSheetDocuments()

        _state.value = State.Loaded(
            current.copy(
                bottomSheet = current.bottomSheet.copy(
                    documents = BottomSheetDocumentsState(
                        all = allDocuments,
                        query = "",
                        filtered = allDocuments
                    )
                )
            )
        )
    }

    fun onBottomSheetDocumentsSearchQueryChanged(query: String) {
        val current = (_state.value as? State.Loaded)?.mainState ?: return
        val bottomSheet = current.bottomSheet

        _state.value = State.Loaded(
            current.copy(
                bottomSheet = bottomSheet.copy(
                    documents = bottomSheet.documents.copy(query = query)
                )
            )
        )
        applyAllDocumentFilters()
    }

    private fun getMockBottomSheetDocuments(): List<DocumentDataPoint> {
        return listOf(
            DocumentDataPoint(
                id = "1",
                name = "CRM1 Annual Charges and Compensation Report",
                subName = "Oct 08, 2025",
                value = null,
                iconResId = com.fintexinc.core.R.drawable.ic_file,
                type = DocumentTypeFilterUi.TAX_DOCUMENTS,
            ),
            DocumentDataPoint(
                id = "2",
                name = "CRM2 Annual Charges and Compensation Report",
                subName = "Oct 01, 2025",
                value = null,
                iconResId = com.fintexinc.core.R.drawable.ic_file,
                type = DocumentTypeFilterUi.TAX_DOCUMENTS,
            ),
            DocumentDataPoint(
                id = "3",
                name = "CRM3 Annual Charges and Compensation Report",
                subName = "Sep 23, 2025",
                value = null,
                iconResId = com.fintexinc.core.R.drawable.ic_file,
                type = DocumentTypeFilterUi.TAX_DOCUMENTS,
            ),
            DocumentDataPoint(
                id = "4",
                name = "CRM4 Annual Charges and Compensation Report",
                subName = "Aug 24, 2025",
                value = null,
                iconResId = com.fintexinc.core.R.drawable.ic_file,
                type = DocumentTypeFilterUi.TAX_DOCUMENTS,
            ),
            DocumentDataPoint(
                id = "5",
                name = "CRM5 Annual Charges and Compensation Report",
                subName = "Jul 30, 2025",
                value = null,
                iconResId = com.fintexinc.core.R.drawable.ic_file,
                type = DocumentTypeFilterUi.TAX_DOCUMENTS,
            ),
            DocumentDataPoint(
                id = "6",
                name = "CRM6 Annual Charges and Compensation Report",
                subName = "Jun 30, 2025",
                value = null,
                iconResId = com.fintexinc.core.R.drawable.ic_file,
                type = DocumentTypeFilterUi.STATEMENTS,
            ),
            DocumentDataPoint(
                id = "7",
                name = "CRM7 Annual Charges and Compensation Report",
                subName = "Sep 15, 2025",
                value = null,
                iconResId = com.fintexinc.core.R.drawable.ic_file,
                type = DocumentTypeFilterUi.TAX_DOCUMENTS,
            ),
        )
    }

    // ===================== STATE CLASSES =====================

    data class MainState(
        val selectedTab: TopTab = TopTab.SUMMARY,
        val summary: Account,
        val performanceData: List<PerformanceItem> = emptyList(),
        val positions: List<NameValue> = emptyList(),
        val activities: List<Transaction> = emptyList(),
        val documents: DocumentsState = DocumentsState(),
        val bottomSheet: BottomSheetState = BottomSheetState(),
        val returnsItems: ImmutableList<ReturnsItemUi> = persistentListOf(),
        val holdingsItems: ImmutableList<ReturnsItemUi> = persistentListOf(),
    )

    enum class TopTab { SUMMARY, POSITIONS, ACTIVITIES, DOCUMENTS }

    data class DocumentsState(
        val all: List<Document> = emptyList(),
        val query: String = "",
        val filtered: List<Document> = emptyList()
    )

    data class BottomSheetState(
        val transactions: TransactionsState = TransactionsState(),
        val details: DetailsState = DetailsState(),
        val documents: BottomSheetDocumentsState = BottomSheetDocumentsState()
    )

    data class BottomSheetDocumentsState(
        val all: List<DocumentDataPoint> = emptyList(),
        val query: String = "",
        val filtered: List<DocumentDataPoint> = emptyList(),
        val selectedTypes: List<DocumentTypeFilterUi> = listOf(DocumentTypeFilterUi.ALL_DOCUMENTS),
        val selectedDates: List<DateFilterUi> = listOf(DateFilterUi.ALL_DATES),
        val selectedMonth: Int? = null,
        val selectedYear: Int? = null,
    )

    data class TransactionsState(
        val all: List<TransactionUi> = emptyList(),
        val query: String = "",
        val pendingGroups: List<TransactionGroup> = emptyList(),
        val settledGroups: List<TransactionGroup> = emptyList(),
        val selectedTypes: List<TransactionTypeFilterUi> = listOf(TransactionTypeFilterUi.ALL_TYPES),
        val selectedStatuses: List<TransactionStatusFilter> = listOf(TransactionStatusFilter.ALL_STATUS),
        val selectedDates: List<DateFilterUi> = listOf(DateFilterUi.ALL_DATES),
        val selectedMonth: Int? = null,
        val selectedYear: Int? = null,
    )

    data class DetailsState(
        val accountDetails: String? = null
    )

    sealed class State {
        object Loading : State()
        data class Loaded(val mainState: MainState) : State()
    }
}

data class DocumentDataPoint(
    val id: String,
    val name: String,
    val subName: String,
    val value: String? = null,
    val subValue: String? = null,
    @param:DrawableRes val iconResId :Int? = null,
    val type: DocumentTypeFilterUi? = null,
)