package com.tangerine.account.presentation.ui.bottom_tab

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fintexinc.core.data.model.DataPoint
import com.fintexinc.core.presentation.ui.widget.TangerineSearchBar
import com.fintexinc.core.presentation.ui.widget.modal.UniversalModalBottomSheet
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.components.DocumentItem
import com.tangerine.account.R
import com.tangerine.account.presentation.models.DateFilterUi
import com.tangerine.account.presentation.models.DocumentTypeFilterUi
import com.tangerine.account.presentation.ui.components.DateFilterModalBottomSheet
import com.tangerine.account.presentation.ui.components.FilterButton
import com.tangerine.account.presentation.ui.components.MultiSelectChips
import com.tangerine.account.presentation.ui.components.handleUniversalSelection

@Composable
internal fun DocumentsUi(
    modifier: Modifier = Modifier,
) {
    var showDateFilter by remember { mutableStateOf(false) }
    var showDocumentFilter by remember { mutableStateOf(false) }
    var selectedDates by remember { mutableStateOf(listOf(DateFilterUi.ALL_DATES)) }
    var selectedDocumentTypes by remember { mutableStateOf(listOf(DocumentTypeFilterUi.ALL_DOCUMENTS)) }

    // TODO() mock data
    val documents = listOf(
        DataPoint(
            id = "1",
            name = "CRM2 Annual Charges and Compensation Report 2024",
            subName = "MAR 14, 2023",
            value = null,
            iconResId = com.fintexinc.core.R.drawable.ic_file
        ),
        DataPoint(
            id = "2",
            name = "CRM2 Annual Charges and Compensation Report 2024",
            subName = "MAR 14, 2023",
            value = null,
            iconResId = com.fintexinc.core.R.drawable.ic_file
        ),
        DataPoint(
            id = "3",
            name = "CRM2 Annual Charges and Compensation Report 2024",
            subName = "MAR 14, 2023",
            value = null,
            iconResId = com.fintexinc.core.R.drawable.ic_file
        ),
        DataPoint(
            id = "4",
            name = "CRM2 Annual Charges and Compensation Report 2024",
            subName = "MAR 14, 2023",
            value = null,
            iconResId = com.fintexinc.core.R.drawable.ic_file
        ),
        DataPoint(
            id = "5",
            name = "CRM2 Annual Charges and Compensation Report 2024",
            subName = "MAR 14, 2023",
            value = null,
            iconResId = com.fintexinc.core.R.drawable.ic_file
        ),
        DataPoint(
            id = "6",
            name = "CRM2 Annual Charges and Compensation Report 2024",
            subName = "MAR 14, 2023",
            value = null,
            iconResId = com.fintexinc.core.R.drawable.ic_file
        ),
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Colors.Background)
    ) {
        TangerineSearchBar(
            isShowFilter = false
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            FilterButton(
                text = stringResource(R.string.text_all_dates),
                onClick = { showDateFilter = true }
            )

            Spacer(modifier = Modifier.width(20.dp))

            FilterButton(
                text = stringResource(R.string.text_all_documents),
                onClick = { showDocumentFilter = true },
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            itemsIndexed(documents) { index, document ->
                DocumentItem(
                    title = document.name,
                    date = document.subName,
                    onClick = {},
                    isLastItem = index == documents.lastIndex,
                )
            }
        }
    }

    if (showDateFilter) {
        DateFilterModalBottomSheet(
            isShowing = remember { mutableStateOf(showDateFilter) }.apply {
                value = showDateFilter
            },
            selectedDates = selectedDates,
            onDatesSelected = { newDates ->
                selectedDates = newDates
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
