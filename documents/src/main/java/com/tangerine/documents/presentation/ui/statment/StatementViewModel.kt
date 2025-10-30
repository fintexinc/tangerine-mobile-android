package com.tangerine.documents.presentation.ui.statment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fintexinc.core.domain.gateway.AccountGateway
import com.fintexinc.core.domain.model.Document
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatementViewModel @Inject constructor(
    private val accountRepository: AccountGateway,
) : ViewModel() {
    private val _state = MutableStateFlow<State>(State.Loading)
    val state: StateFlow<State>
        get() = _state.asStateFlow()

    private var allDocuments: List<Document> = emptyList()

    init {
        viewModelScope.launch {
            _state.value = getData()
        }
    }

    private suspend fun getData(): State.Data {
        allDocuments = accountRepository.getDocuments().sortedWith(
            compareByDescending<Document> { it.documentDate.year }
                .thenByDescending { it.documentDate.month }
                .thenByDescending { it.documentDate.day }
        )

        val allYears = allDocuments.map { it.documentDate.year }.toSet().sortedDescending()
        val allMonths = allDocuments.map { it.documentDate.month }.toSet().sorted()

        return State.Data(
            documents = allDocuments,
            expandedYears = allYears,
            availableYears = allYears,
            selectedYears = emptyList(),
            availableMonths = allMonths,
            selectedMonths = emptyList(),
        )
    }

    fun toggleYearExpansion(year: Int) {
        val currentState = _state.value as? State.Data ?: return
        val expandedYears = currentState.expandedYears.toMutableSet()

        if (expandedYears.contains(year)) {
            expandedYears.remove(year)
        } else {
            expandedYears.add(year)
        }

        _state.value = currentState.copy(expandedYears = expandedYears.toList())
    }

    fun onSearchQueryChanged(query: String) {
        val currentState = _state.value as? State.Data ?: return

        val documentsToFilter = allDocuments.filter { doc ->
            currentState.selectedYears.contains(doc.documentDate.year) &&
                    currentState.selectedMonths.contains(doc.documentDate.month)
        }

        val filteredDocuments = if (query.isBlank()) {
            documentsToFilter
        } else {
            documentsToFilter.filter { document ->
                document.documentName.contains(query, ignoreCase = true)
            }
        }

        val yearsWithResults = filteredDocuments.map { it.documentDate.year }.toList()

        _state.value = currentState.copy(
            searchText = query,
            documents = filteredDocuments,
            expandedYears = yearsWithResults
        )
    }

    fun onYearFilterApplied(selectedYears: List<Int>) {
        val currentState = _state.value as? State.Data ?: return

        val filteredDocuments = allDocuments.filter { doc ->
            val yearMatches = selectedYears.isEmpty() || selectedYears.contains(doc.documentDate.year)
            val monthMatches = currentState.selectedMonths.isEmpty() || currentState.selectedMonths.contains(doc.documentDate.month)
            yearMatches && monthMatches
        }

        val finalDocuments = if (currentState.searchText.isNotBlank()) {
            filteredDocuments.filter { document ->
                document.documentName.contains(currentState.searchText, ignoreCase = true)
            }
        } else {
            filteredDocuments
        }
        val expandedYears = selectedYears.ifEmpty {
            finalDocuments.map { it.documentDate.year }.distinct()
        }

        _state.value = currentState.copy(
            documents = finalDocuments,
            selectedYears = selectedYears,
            expandedYears = expandedYears,
        )
    }

    fun onMonthFilterApplied(selectedMonths: List<Int>) {
        val currentState = _state.value as? State.Data ?: return

        val filteredDocuments = allDocuments.filter { doc ->
            val yearMatches = currentState.selectedYears.isEmpty() || currentState.selectedYears.contains(doc.documentDate.year)
            val monthMatches = selectedMonths.isEmpty() || selectedMonths.contains(doc.documentDate.month)
            yearMatches && monthMatches
        }

        val finalDocuments = if (currentState.searchText.isNotBlank()) {
            filteredDocuments.filter { document ->
                document.documentName.contains(currentState.searchText, ignoreCase = true)
            }
        } else {
            filteredDocuments
        }

        val yearsWithResults = finalDocuments.map { it.documentDate.year }.distinct()
        val expandedYears = if (currentState.selectedYears.isEmpty()) {
            yearsWithResults
        } else {
            currentState.selectedYears.filter { it in yearsWithResults }
        }

        _state.value = currentState.copy(
            documents = finalDocuments,
            selectedMonths = selectedMonths,
            expandedYears = expandedYears
        )
    }


    sealed class State {
        object Loading : State()
        data class Data(
            val documents: List<Document>,
            val searchText: String = "",
            val expandedYears: List<Int> = emptyList(),
            val availableYears: List<Int> = emptyList(),
            val selectedYears: List<Int> = emptyList(),
            val availableMonths: List<Int> = emptyList(),
            val selectedMonths: List<Int> = emptyList(),
        ) : State()
    }
}

data class YearGroup(
    val year: Int,
    val monthGroups: List<MonthGroup>
)

data class MonthGroup(
    val year: Int,
    val month: Int,
    val documents: List<Document>
)