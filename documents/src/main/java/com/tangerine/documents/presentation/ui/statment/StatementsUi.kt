package com.tangerine.documents.presentation.ui.statment

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fintexinc.core.data.utils.date.formatToString
import com.fintexinc.core.presentation.ui.widget.TangerineSearchBar
import com.fintexinc.core.presentation.ui.widget.ToolBar
import com.fintexinc.core.presentation.ui.widget.modal.UniversalModalBottomSheet
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.components.DocumentItem
import com.fintexinc.core.ui.components.EmptyScreen
import com.fintexinc.core.ui.components.FilterButton
import com.fintexinc.core.ui.components.MultiSelectChips
import com.fintexinc.core.ui.font.FontStyles
import com.tangerine.documents.R
import com.tangerine.documents.presentation.ui.models.MonthFilterUi
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun StatementsScreen(
    state: StatementViewModel.State,
    onBackClicked: () -> Unit = {},
    onSearchQueryChanged: (String) -> Unit,
    onYearToggle: (Int) -> Unit,
    onYearFilterApplied: (List<Int>) -> Unit,
    onMonthFilterApplied: (List<Int>) -> Unit,
) {
    when (state) {
        is StatementViewModel.State.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return
        }

        is StatementViewModel.State.Data -> {
            Content(
                state = state,
                onBackClicked = onBackClicked,
                onSearchQueryChanged = onSearchQueryChanged,
                onYearToggle = onYearToggle,
                onYearFilterApplied = onYearFilterApplied,
                onMonthFilterApplied = onMonthFilterApplied,
            )
        }
    }
}

@Composable
private fun Content(
    state: StatementViewModel.State.Data,
    onBackClicked: () -> Unit = {},
    onSearchQueryChanged: (String) -> Unit,
    onYearToggle: (Int) -> Unit,
    onYearFilterApplied: (List<Int>) -> Unit,
    onMonthFilterApplied: (List<Int>) -> Unit,
) {
    val context = LocalContext.current
    val yearGroups = remember(state.documents) {
        state.documents
            .groupBy { it.documentDate.year }
            .map { (year, docs) ->
                val monthGroups = docs
                    .groupBy { it.documentDate.month }
                    .map { (month: Int, monthDocs) ->
                        MonthGroup(year, month, monthDocs)
                    }
                    .sortedByDescending { it.month }
                YearGroup(year, monthGroups)
            }
            .sortedByDescending { it.year }
    }

    val showYearBottomSheet = remember { mutableStateOf(false) }
    val showMonthBottomSheet = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Colors.Background),
    ) {
        LazyColumn(
            modifier = Modifier.statusBarsPadding(),
        ) {
            item {
                ToolBar(
                    text = stringResource(R.string.text_category_statements_title),
                    leftIcon = {
                        Icon(
                            modifier = Modifier
                                .wrapContentSize()
                                .clickable { onBackClicked() },
                            painter = painterResource(com.fintexinc.core.R.drawable.ic_back_arrow),
                            contentDescription = stringResource(R.string.description_icon_navigate_back),
                            tint = Colors.Primary,
                        )
                    },
                )
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))

                TangerineSearchBar(
                    searchText = state.searchText,
                    onSearchTextChange = { onSearchQueryChanged(it) },
                    isShowFilter = false,
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Row(modifier = Modifier.fillMaxWidth()) {
                    val yearFilterText = when {
                        state.selectedYears.isEmpty() -> stringResource(R.string.text_all_years)
                        state.selectedYears.size == 1 -> state.selectedYears.first().toString()
                        else -> "${state.selectedYears.size} years"
                    }
                    val filterMonthText = when {
                        state.selectedMonths.isEmpty() -> stringResource(R.string.text_all_months)
                        state.selectedMonths.size == 1 -> MonthFilterUi.fromMonthNumber(state.selectedMonths.first())
                            ?.let {
                                stringResource(it.stringResId)
                            } ?: state.selectedMonths.first().toString()

                        else -> "${state.selectedMonths.size} months"
                    }

                    FilterButton(
                        text = yearFilterText,
                        onClick = { showYearBottomSheet.value = true },
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    FilterButton(
                        text = filterMonthText,
                        onClick = { showMonthBottomSheet.value = true },
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            if (yearGroups.isEmpty()) {
                item {
                    EmptyScreen(
                        modifier = Modifier.matchParentSize(),
                        title = R.string.text_empty_documents,
                    )
                }
            } else {
                yearGroups.forEach { yearGroup ->
                    item(key = "year-${yearGroup.year}") {
                        YearHeader(
                            year = yearGroup.year,
                            isExpanded = state.expandedYears.contains(yearGroup.year),
                            onToggle = { onYearToggle(yearGroup.year) }
                        )
                    }

                    if (state.expandedYears.contains(yearGroup.year)) {
                        yearGroup.monthGroups.forEach { monthGroup: MonthGroup ->
                            item(key = "month-${monthGroup.year}-${monthGroup.month}") {
                                MonthHeader(month = monthGroup.month)
                            }

                            itemsIndexed(
                                items = monthGroup.documents,
                                key = { _, document -> document.id }
                            ) { index, document ->
                                DocumentItem(
                                    title = document.documentName,
                                    date = document.documentDate.formatToString(),
                                    onClick = {
                                        val intent = Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse("https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf")
                                        )
                                        context.startActivity(intent)
                                    },
                                    isLastItem = index == monthGroup.documents.lastIndex
                                )

                                if (index == monthGroup.documents.lastIndex) {
                                    Spacer(modifier = Modifier.height(16.dp))
                                }
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        if (showYearBottomSheet.value) {
            YearFilterModalBottomSheet(
                isShowing = showYearBottomSheet,
                years = state.availableYears,
                selectedYears = state.selectedYears.toList(),
                onYearsSelected = onYearFilterApplied,
                onDismiss = {
                    showYearBottomSheet.value = false
                }
            )
        }

        if (showMonthBottomSheet.value) {
            MonthFilterModalBottomSheet(
                isShowing = showMonthBottomSheet,
                months = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12),
                selectedMonths = state.selectedMonths.toList(),
                onMonthsSelected = { selectedMonths ->
                    onMonthFilterApplied(selectedMonths)
                },
                onDismiss = {
                    showMonthBottomSheet.value = false
                }
            )
        }
    }
}

@Composable
internal fun YearFilterModalBottomSheet(
    isShowing: MutableState<Boolean>,
    years: List<Int>,
    selectedYears: List<Int>,
    onYearsSelected: (List<Int>) -> Unit,
    onDismiss: () -> Unit
) {
    val allOptionName = stringResource(R.string.text_all_years)
    val options = listOf(allOptionName) + years.map { it.toString() }

    val selectedStates = remember(isShowing.value, years, selectedYears) {
        val isAllSelected = selectedYears.isEmpty() || selectedYears.size == years.size
        mutableStateListOf(
            isAllSelected,
            *years.map { year -> selectedYears.contains(year) }.toTypedArray()
        )
    }

    UniversalModalBottomSheet(
        isShowing = isShowing,
        title = stringResource(R.string.text_years),
        onDoneClick = {
            val selected: List<Int> = if (selectedStates.first()) {
                emptyList()
            } else {
                years.filterIndexed { index, _ -> selectedStates[index + 1] }
            }
            onYearsSelected(selected)
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
                options = options,
                selectedStates = selectedStates,
                onSelectionChanged = { index, isSelected ->
                    handleYearSelection(
                        selectedStates = selectedStates,
                        clickedIndex = index,
                        isSelected = isSelected
                    )
                }
            )
        }
    }
}

@Composable
internal fun MonthFilterModalBottomSheet(
    isShowing: MutableState<Boolean>,
    months: List<Int>,
    selectedMonths: List<Int>,
    onMonthsSelected: (List<Int>) -> Unit,
    onDismiss: () -> Unit
) {
    val allOptionName = stringResource(R.string.text_all_months)
    val monthNames = months.map { monthNumber ->
        MonthFilterUi.fromMonthNumber(monthNumber)?.let {
            stringResource(it.stringResId)
        } ?: monthNumber.toString()
    }
    val options = listOf(allOptionName) + monthNames

    val selectedStates = remember(isShowing.value, months, selectedMonths) {
        val isAllSelected = selectedMonths.isEmpty() || selectedMonths.size == months.size
        mutableStateListOf(
            isAllSelected,
            *months.map { month ->
                if (isAllSelected) false else selectedMonths.contains(month)
            }.toTypedArray()
        )
    }

    UniversalModalBottomSheet(
        isShowing = isShowing,
        title = stringResource(R.string.text_all_months),
        onDoneClick = {
            val selected: List<Int> = if (selectedStates.first()) {
                emptyList()
            } else {
                months.filterIndexed { index, _ -> selectedStates[index + 1] }
            }
            onMonthsSelected(selected)
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
                options = options,
                selectedStates = selectedStates,
                onSelectionChanged = { index, isSelected ->
                    handleMonthSelection(
                        selectedStates = selectedStates,
                        clickedIndex = index,
                        isSelected = isSelected
                    )
                }
            )
        }
    }
}

private fun handleMonthSelection(
    selectedStates: SnapshotStateList<Boolean>,
    clickedIndex: Int,
    isSelected: Boolean
) {
    if (clickedIndex == 0) {
        for (i in selectedStates.indices) {
            selectedStates[i] = i == 0 && isSelected
        }
    } else {
        selectedStates[0] = false
        selectedStates[clickedIndex] = isSelected

        if (selectedStates.drop(1).none { it }) {
            selectedStates[0] = true
        }
    }
}

private fun handleYearSelection(
    selectedStates: SnapshotStateList<Boolean>,
    clickedIndex: Int,
    isSelected: Boolean
) {
    if (clickedIndex == 0) {
        for (i in selectedStates.indices) {
            selectedStates[i] = i == 0 && isSelected
        }
    } else {
        selectedStates[0] = false
        selectedStates[clickedIndex] = isSelected

        if (selectedStates.drop(1).none { it }) {
            selectedStates[0] = true
        }
    }
}

@Composable
fun YearHeader(
    year: Int,
    isExpanded: Boolean,
    onToggle: () -> Unit
) {
    val rotationAngle by animateFloatAsState(
        targetValue = if (isExpanded) 0f else 180f,
        animationSpec = tween(durationMillis = 300),
        label = "chevron_rotation"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Colors.BackgroundSubdued)
            .clickable { onToggle() }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = year.toString(),
            color = Colors.Text,
            style = FontStyles.BodyLargeBold,
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
        )

        Icon(
            modifier = Modifier.rotate(rotationAngle),
            painter = painterResource(com.fintexinc.core.R.drawable.ic_chevron_down),
            contentDescription =
                stringResource(R.string.description_icon_open_close),
            tint = Colors.Text,
        )
    }
}


@Composable
private fun MonthHeader(
    month: Int,
) {
    val monthName = remember(month) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH, month - 1)
        val format = SimpleDateFormat("MMMM", Locale.getDefault())
        format.format(calendar.time).replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault())
            else it.toString()
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Colors.LightGray.copy(alpha = 0.3f)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = monthName,
            style = FontStyles.BodySmall,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        )
    }
}