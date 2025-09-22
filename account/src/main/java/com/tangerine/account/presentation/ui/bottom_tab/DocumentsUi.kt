package com.tangerine.account.presentation.ui.bottom_tab

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fintexinc.core.data.model.DataPoint
import com.fintexinc.core.presentation.ui.modifier.clickableShape
import com.fintexinc.core.presentation.ui.widget.TangerineSearchBar
import com.fintexinc.core.presentation.ui.widget.modal.UniversalModalBottomSheet
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.font.FontStyles
import com.tangerine.account.R
import com.tangerine.account.presentation.models.DateFilterUi
import com.tangerine.account.presentation.models.DocumentTypeFilterUi
import com.tangerine.account.presentation.ui.components.FilterButton

@Composable
internal fun DocumentsUi(
    modifier: Modifier = Modifier,
) {
    var showDateFilter by remember { mutableStateOf(false) }
    var showDocumentFilter by remember { mutableStateOf(false) }
    var selectedDates by remember { mutableStateOf(listOf("All dates")) }
    var selectedDocumentTypes by remember { mutableStateOf(listOf("All documents")) }

    // TODO() mock data
    val documents = listOf(
        DataPoint(
            id = "1",
            name = "CRM2 Annual Charges and Compensation Report 2024",
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
            name = "CRM2 Annual Charges and Compensation Report 2024",
            subName = "MAR 14, 2023",
            value = null,
            iconResId = R.drawable.ic_file
        ),
        DataPoint(
            id = "4",
            name = "CRM2 Annual Charges and Compensation Report 2024",
            subName = "MAR 14, 2023",
            value = null,
            iconResId = R.drawable.ic_file
        ),
        DataPoint(
            id = "5",
            name = "CRM2 Annual Charges and Compensation Report 2024",
            subName = "MAR 14, 2023",
            value = null,
            iconResId = R.drawable.ic_file
        ),
        DataPoint(
            id = "6",
            name = "CRM2 Annual Charges and Compensation Report 2024",
            subName = "MAR 14, 2023",
            value = null,
            iconResId = R.drawable.ic_file
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
            onDismiss = { showDateFilter = false }
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
            onDismiss = { showDocumentFilter = false }
        )
    }
}

@Composable
private fun DocumentItem(
    title: String,
    date: String,
    onClick: () -> Unit,
    isLastItem: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Colors.Background)
            .clickable { onClick() }
            .padding(top = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .padding(top = 2.dp),
                painter = painterResource(R.drawable.ic_file),
                tint = Colors.BrandBlack,
                contentDescription = stringResource(R.string.description_documents),
            )
            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = title,
                            style = FontStyles.BodyLarge,
                            color = Colors.Text
                        )
                        Text(
                            text = date,
                            style = FontStyles.BodyMedium,
                            color = Colors.TextSubdued,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(com.fintexinc.core.R.drawable.ic_arrow_right),
                            tint = Colors.IconSupplementary,
                            contentDescription = stringResource(R.string.description_view_details),
                        )
                    }
                }
                if (!isLastItem) {
                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(
                        color = Colors.BorderSubdued,
                        modifier = Modifier
                    )
                }
            }
        }
    }
}

@Composable
private fun DateFilterModalBottomSheet(
    isShowing: MutableState<Boolean>,
    selectedDates: List<String>,
    onDatesSelected: (List<String>) -> Unit,
    onDismiss: () -> Unit,
) {
    val dateOptions = DateFilterUi.entries.map { stringResource(it.stringResId) }
    val allOptionName = stringResource(DateFilterUi.ALL_DATES.stringResId)

    val selectedStates = remember {
        mutableStateListOf(*dateOptions.map { it in selectedDates }.toTypedArray())
    }

    UniversalModalBottomSheet(
        isShowing = isShowing,
        title = stringResource(R.string.title_timeframe),
        onDoneClick = {
            val selected = dateOptions.filterIndexed { index, _ -> selectedStates[index] }
            onDatesSelected(selected)
        },
        onDismiss = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 16.dp)
        ) {
            MultiSelectChips(
                options = dateOptions,
                selectedStates = selectedStates,
                onSelectionChanged = { index, isSelected ->
                    handleUniversalSelection(
                        options = dateOptions,
                        selectedStates = selectedStates,
                        clickedIndex = index,
                        isSelected = isSelected,
                        allOptionName = allOptionName
                    )
                }
            )
        }
    }
}

@Composable
private fun DocumentTypeFilterModalBottomSheet(
    isShowing: MutableState<Boolean>,
    selectedTypes: List<String>,
    onTypesSelected: (List<String>) -> Unit,
    onDismiss: () -> Unit,
) {
    val documentTypeOptions = DocumentTypeFilterUi.entries.map { stringResource(it.stringResId) }
    val allOptionName = stringResource(DocumentTypeFilterUi.ALL_DOCUMENTS.stringResId)

    val selectedStates = remember {
        mutableStateListOf(*documentTypeOptions.map { it in selectedTypes }.toTypedArray())
    }

    UniversalModalBottomSheet(
        isShowing = isShowing,
        title = stringResource(R.string.title_document_type),
        onDoneClick = {
            val selected = documentTypeOptions.filterIndexed { index, _ -> selectedStates[index] }
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
                options = documentTypeOptions,
                selectedStates = selectedStates,
                onSelectionChanged = { index, isSelected ->
                    handleUniversalSelection(
                        options = documentTypeOptions,
                        selectedStates = selectedStates,
                        clickedIndex = index,
                        isSelected = isSelected,
                        allOptionName = allOptionName
                    )
                }
            )
        }
    }
}

private fun handleUniversalSelection(
    options: List<String>,
    selectedStates: MutableList<Boolean>,
    clickedIndex: Int,
    isSelected: Boolean,
    allOptionName: String
) {
    val allOptionIndex = options.indexOf(allOptionName)

    if (clickedIndex == allOptionIndex) {
        if (isSelected) {
            selectedStates.forEachIndexed { index, _ ->
                selectedStates[index] = index == allOptionIndex
            }
        } else {
            selectedStates[allOptionIndex] = false
        }
    } else {
        if (isSelected) {
            selectedStates[clickedIndex] = true
            if (allOptionIndex != -1) {
                selectedStates[allOptionIndex] = false
            }
        } else {
            selectedStates[clickedIndex] = false
        }
    }
}

@Composable
private fun MultiSelectChips(
    options: List<String>,
    selectedStates: MutableList<Boolean>,
    onSelectionChanged: (Int, Boolean) -> Unit
) {
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        options.forEachIndexed { index, option ->
            val isSelected = selectedStates[index]
            Text(
                modifier = Modifier
                    .background(
                        color = Colors.BackgroundInteractive,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .then(
                        if (isSelected) {
                            Modifier.border(
                                width = 1.dp,
                                color = Colors.TextInteractive,
                                shape = RoundedCornerShape(16.dp)
                            )
                        } else {
                            Modifier
                        }
                    )
                    .clickableShape(RoundedCornerShape(16.dp)) {
                        val newSelected = !selectedStates[index]
                        selectedStates[index] = newSelected
                        onSelectionChanged(index, newSelected)
                    }
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                text = option,
                style = FontStyles.BodyMedium,
                color = Colors.TextInteractive
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DocumentItemPreview() {
    DocumentItem(
        title = "title",
        date = "date",
        onClick = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun FilterButtonPreview() {
    DocumentsUi()
}
