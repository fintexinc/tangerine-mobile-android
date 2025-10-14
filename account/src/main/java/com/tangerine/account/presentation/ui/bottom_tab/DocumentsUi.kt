package com.tangerine.account.presentation.ui.bottom_tab

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fintexinc.core.presentation.ui.widget.TangerineSearchBar
import com.fintexinc.core.presentation.ui.widget.modal.UniversalModalBottomSheet
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.components.DocumentItem
import com.tangerine.account.R
import com.tangerine.account.presentation.models.DateFilterUi
import com.tangerine.account.presentation.models.DocumentTypeFilterUi
import com.tangerine.account.presentation.ui.components.DateFilterModalBottomSheet
import com.fintexinc.core.ui.components.EmptyScreen
import com.fintexinc.core.ui.components.FilterButton
import com.fintexinc.core.ui.components.MultiSelectChips
import com.tangerine.account.presentation.ui.components.handleUniversalSelection
import com.tangerine.account.presentation.viewmodel.DocumentDataPoint

@Composable
internal fun DocumentsUi(
    modifier: Modifier = Modifier,
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    documents: List<DocumentDataPoint>,
    navigateToTransactionDetailScreen: (String) -> Unit,
    onDateFilterChangedDocument: (List<DateFilterUi>, Int?, Int?) -> Unit,
    onTypeFilterChanged: (List<DocumentTypeFilterUi>) -> Unit,
) {
    val context = LocalContext.current

    var showDateFilter by remember { mutableStateOf(false) }
    var showDocumentFilter by remember { mutableStateOf(false) }
    var selectedDates by remember { mutableStateOf(listOf(DateFilterUi.ALL_DATES)) }
    var selectedDocumentTypes by remember { mutableStateOf(listOf(DocumentTypeFilterUi.ALL_DOCUMENTS)) }

    val filteredDocuments = remember(documents, searchQuery) {
        if (searchQuery.isBlank()) {
            documents
        } else {
            documents.filter { document ->
                document.name.contains(searchQuery, ignoreCase = true) ||
                        document.subName.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    val hasActiveFilters = searchQuery.isNotBlank() ||
            !selectedDates.contains(DateFilterUi.ALL_DATES) ||
            !selectedDocumentTypes.contains(DocumentTypeFilterUi.ALL_DOCUMENTS)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Colors.Background)
    ) {
        TangerineSearchBar(
            searchText = searchQuery,
            onSearchTextChange = onSearchQueryChanged,
            isShowFilter = false,
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            FilterButton(
                text = stringResource(
                    selectedDates.firstOrNull()?.stringResId ?: R.string.filter_all_dates
                ),
                onClick = { showDateFilter = true }
            )

            Spacer(modifier = Modifier.width(20.dp))

            FilterButton(
                text = stringResource(
                    selectedDocumentTypes.firstOrNull()?.stringResId ?: R.string.text_all_documents
                ),
                onClick = { showDocumentFilter = true },
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (filteredDocuments.isEmpty() && hasActiveFilters) {
            EmptyScreen(
                modifier = Modifier.fillMaxSize(),
                title = R.string.text_empty_documents,
            )
        } else {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                itemsIndexed(filteredDocuments) { index, document ->
                    DocumentItem(
                        title = document.name,
                        date = document.subName,
                        onClick = {
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf")
                            )
                            context.startActivity(intent)
                        },
                        isLastItem = index == filteredDocuments.lastIndex,
                    )
                }
            }
        }
    }

    if (showDateFilter) {
        DateFilterModalBottomSheet(
            isShowing = remember { mutableStateOf(showDateFilter) }.apply {
                value = showDateFilter
            },
            selectedDates = selectedDates,
            onDatesSelected = { newDates, month, year ->
                selectedDates = newDates
                onDateFilterChangedDocument(newDates, month, year)
                showDateFilter = false
            },
            onDismiss = { showDateFilter = false },
        )
    }

    if (showDocumentFilter) {
        DocumentTypeFilterModalBottomSheet(
            isShowing = remember { mutableStateOf(showDocumentFilter) }.apply {
                value = showDocumentFilter
            },
            selectedTypes = selectedDocumentTypes,
            onTypesSelected = { newTypes ->
                selectedDocumentTypes = newTypes
                onTypeFilterChanged(newTypes)
                showDocumentFilter = false
            },
            onDismiss = { showDocumentFilter = false },
        )
    }
}

@Composable
internal fun DocumentTypeFilterModalBottomSheet(
    isShowing: MutableState<Boolean>,
    selectedTypes: List<DocumentTypeFilterUi>,
    onTypesSelected: (List<DocumentTypeFilterUi>) -> Unit,
    onDismiss: () -> Unit
) {
    val typeEnums = DocumentTypeFilterUi.entries
    val typeOptions = typeEnums.map { stringResource(it.stringResId) }
    val allOptionName = stringResource(DocumentTypeFilterUi.ALL_DOCUMENTS.stringResId)

    val selectedStates = remember(selectedTypes) {
        mutableStateListOf(*typeEnums.map { it in selectedTypes }.toTypedArray())
    }

    UniversalModalBottomSheet(
        isShowing = isShowing,
        title = stringResource(R.string.title_document_type),
        onDoneClick = {
            val selected = typeEnums.filterIndexed { index, _ -> selectedStates[index] }
            onTypesSelected(selected)
        },
        onDismiss = onDismiss,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 16.dp)
        ) {
            MultiSelectChips(
                options = typeOptions,
                selectedStates = selectedStates,
                onSelectionChanged = { index, isSelected ->
                    handleUniversalSelection(
                        options = typeOptions,
                        selectedStates = selectedStates,
                        clickedIndex = index,
                        isSelected = isSelected,
                        allOptionName = allOptionName,
                    )
                }
            )
        }
    }
}
