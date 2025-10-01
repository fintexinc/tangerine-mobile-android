package com.tangerine.account.presentation.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fintexinc.core.data.model.DataPoint
import com.fintexinc.core.data.model.NameValue
import com.fintexinc.core.domain.gateway.AccountGateway
import com.fintexinc.core.domain.model.Account
import com.fintexinc.core.domain.model.Document
import com.fintexinc.core.domain.model.Transaction
import com.tangerine.account.R
import com.tangerine.account.presentation.models.TransactionGroup
import com.tangerine.account.presentation.models.TransactionUi
import com.tangerine.account.presentation.models.TransactionUiType
import com.tangerine.account.presentation.ui.AccountTab
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val accountGateway: AccountGateway
) : ViewModel() {

    private val _state: MutableStateFlow<State> = MutableStateFlow<State>(State.Loading)
    val state: StateFlow<State> = _state.asStateFlow()

    fun getData(id: String) = viewModelScope.launch {
        val account = accountGateway.getAccountById(id)

        _state.value = State.Loaded(
            MainState(
                selectedTab = TopTab.SUMMARY,
                summary = account
            )
        )

        loadTransactions()
        loadBottomSheetDocuments()
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

        val filteredTransactions = if (query.isBlank()) {
            bottomSheet.transactions.all
        } else {
            bottomSheet.transactions.all.filter { transaction ->
                transaction.description.contains(query, ignoreCase = true) ||
                        transaction.fromAccount.contains(query, ignoreCase = true)
            }
        }

        val (pendingGroups, settledGroups) = groupTransactions(filteredTransactions)

        _state.value = State.Loaded(
            current.copy(
                bottomSheet = bottomSheet.copy(
                    transactions = bottomSheet.transactions.copy(
                        query = query,
                        pendingGroups = pendingGroups,
                        settledGroups = settledGroups
                    )
                )
            )
        )
    }

    // ===================== MOCK DATA =====================

    private fun getMockTransactions(): List<TransactionUi> {
        return listOf(
            TransactionUi(
                id = "1",
                description = "Purchase 1- Tangerine data mock",
                fromAccount = "From: CHQSingleAutoKeep",
                amount = 66.00,
                date = "Oct 14, 2023",
                type = TransactionUiType.PENDING
            ),
            TransactionUi(
                id = "2",
                description = "Purchase 2- Tangerine ...",
                fromAccount = "From: CHQSingleAutoKeep",
                amount = 50.00,
                date = "Oct 14, 2023",
                type = TransactionUiType.PENDING
            ),
            TransactionUi(
                id = "3",
                description = "Transfer In 1 - Tangerine data mock",
                fromAccount = "Savings Account Transac data mock",
                amount = 2600.00,
                date = "Jan 14, 2023",
                type = TransactionUiType.SETTLED,
                additionalAmount1 = "$19.5200",
                additionalAmount2 = "133.1967"
            ),
            TransactionUi(
                id = "4",
                description = "Transfer In 2- Tangerine data mock",
                fromAccount = "Savings Account Transac data mock",
                amount = 2600.00,
                date = "Jan 14, 2023",
                type = TransactionUiType.SETTLED,
                additionalAmount1 = "$19.5200"
            ),
            TransactionUi(
                id = "5",
                description = "Data point 1 (Title)",
                fromAccount = "Data point 2",
                amount = 0.0,
                date = "May 30, 2023",
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

        val filteredDocuments = if (query.isBlank()) {
            bottomSheet.documents.all
        } else {
            bottomSheet.documents.all.filter { document ->
                document.name.contains(query, ignoreCase = true) ||
                        document.subName.contains(query, ignoreCase = true)
            }
        }

        _state.value = State.Loaded(
            current.copy(
                bottomSheet = bottomSheet.copy(
                    documents = bottomSheet.documents.copy(
                        query = query,
                        filtered = filteredDocuments
                    )
                )
            )
        )
    }

    private fun getMockBottomSheetDocuments(): List<DataPoint> {
        return listOf(
            DataPoint(
                id = "1",
                name = "CRM1 Annual Charges and Compensation Report 2024",
                subName = "MAR 14, 2023",
                value = null,
                iconResId = R.drawable.ic_file
            ),
            DataPoint(
                id = "2",
                name = "CRM2 Annual Charges and Compensation Report 2024",
                subName = "MAR 14, 2023",
                value = null,
                iconResId = R.drawable.ic_file
            ),
            DataPoint(
                id = "3",
                name = "CRM3 Annual Charges and Compensation Report 2024",
                subName = "MAR 14, 2023",
                value = null,
                iconResId = R.drawable.ic_file
            ),
            DataPoint(
                id = "4",
                name = "CRM4 Annual Charges and Compensation Report 2024",
                subName = "MAR 14, 2023",
                value = null,
                iconResId = R.drawable.ic_file
            ),
            DataPoint(
                id = "5",
                name = "CRM5 Annual Charges and Compensation Report 2024",
                subName = "MAR 14, 2023",
                value = null,
                iconResId = R.drawable.ic_file
            ),
            DataPoint(
                id = "6",
                name = "CRM6 Annual Charges and Compensation Report 2024",
                subName = "MAR 14, 2023",
                value = null,
                iconResId = R.drawable.ic_file
            ),
        )
    }

    // ===================== STATE CLASSES =====================

    data class MainState(
        val selectedTab: TopTab = TopTab.SUMMARY,
        val summary: Account,
        val positions: List<NameValue> = emptyList(),
        val activities: List<Transaction> = emptyList(),
        val documents: DocumentsState = DocumentsState(),
        val bottomSheet: BottomSheetState = BottomSheetState()
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
        val all: List<DataPoint> = emptyList(),
        val query: String = "",
        val filtered: List<DataPoint> = emptyList()
    )

    data class TransactionsState(
        val all: List<TransactionUi> = emptyList(),
        val query: String = "",
        val pendingGroups: List<TransactionGroup> = emptyList(),
        val settledGroups: List<TransactionGroup> = emptyList(),
    )

    data class DetailsState(
        val accountDetails: String? = null
    )

    sealed class State {
        object Loading : State()
        data class Loaded(val mainState: MainState) : State()
    }
}
