package com.tangerine.account.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fintexinc.core.presentation.ui.widget.modal.UniversalModalBottomSheet
import com.fintexinc.core.ui.components.MultiSelectChips
import com.tangerine.account.R
import com.tangerine.account.presentation.models.DateFilterUi
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.enums.EnumEntries

@Composable
internal fun DateFilterModalBottomSheet(
    isShowing: MutableState<Boolean>,
    selectedDates: List<DateFilterUi>,
    onDatesSelected: (List<DateFilterUi>, Int?, Int?) -> Unit,
    onDismiss: () -> Unit,
    dateEnums: EnumEntries<DateFilterUi>,
    selectedMonth: Int?,
    selectedYear: Int?,
) {
    val dateIcons = dateEnums.map { it.iconResId }

    val selectedStates = remember(selectedDates) {
        mutableStateListOf(*dateEnums.map { it in selectedDates }.toTypedArray())
    }

    var tempSelectedMonth by remember(selectedMonth) { mutableStateOf(selectedMonth) }
    var tempSelectedYear by remember(selectedYear) { mutableStateOf(selectedYear) }

    val baseOptions = dateEnums.map { stringResource(it.stringResId) }
    val updatedDateOptions = remember(tempSelectedMonth, tempSelectedYear, baseOptions) {
        baseOptions.mapIndexed { index, baseText ->
            if (dateEnums[index] == DateFilterUi.BY_MONTH && tempSelectedMonth != null && tempSelectedYear != null) {
                formatMonthYear(tempSelectedMonth!!, tempSelectedYear!!)
            } else {
                baseText
            }
        }
    }

    var previousSelectedIndex by remember { mutableIntStateOf(-1) }
    var showDatePicker by remember { mutableStateOf(false) }

    LaunchedEffect(tempSelectedMonth, tempSelectedYear) {
        if (tempSelectedMonth != null && tempSelectedYear != null) {
            val byMonthIndex = dateEnums.indexOf(DateFilterUi.BY_MONTH)
            if (!selectedStates[byMonthIndex]) {
                selectedStates.forEachIndexed { i, _ ->
                    selectedStates[i] = false
                }
                selectedStates[byMonthIndex] = true
            }
        }
    }

    UniversalModalBottomSheet(
        isShowing = isShowing,
        title = stringResource(R.string.title_timeframe),
        onDoneClick = {
            val selected = dateEnums.filterIndexed { index, _ -> selectedStates[index] }
            val byMonthIndex = dateEnums.indexOf(DateFilterUi.BY_MONTH)

            if (selectedStates[byMonthIndex]) {
                onDatesSelected(selected, tempSelectedMonth, tempSelectedYear)
            } else {
                onDatesSelected(selected, null, null)
            }
        },
        onDismiss = onDismiss,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            MultiSelectChips(
                options = updatedDateOptions,
                selectedStates = selectedStates,
                icons = dateIcons,
                onSelectionChanged = { index, _ ->
                    val byMonthIndex = dateEnums.indexOf(DateFilterUi.BY_MONTH)

                    previousSelectedIndex = selectedStates.indexOfFirst { it }

                    selectedStates.forEachIndexed { i, _ ->
                        selectedStates[i] = false
                    }
                    selectedStates[index] = true

                    if (index != byMonthIndex) {
                        tempSelectedMonth = null
                        tempSelectedYear = null
                    }

                    if (index == byMonthIndex) {
                        showDatePicker = true
                    }
                },
                onSelectedChipClick = { index ->
                    val byMonthIndex = dateEnums.indexOf(DateFilterUi.BY_MONTH)
                    if (index == byMonthIndex) {
                        showDatePicker = true
                    }
                }
            )

            if (showDatePicker) {
                PickerDialog(
                    onDateSelected = { month: Int, year ->
                        tempSelectedMonth = month
                        tempSelectedYear = year
                        showDatePicker = false
                    },
                    onDismiss = {
                        showDatePicker = false
                        val byMonthIndex = dateEnums.indexOf(DateFilterUi.BY_MONTH)

                        if (tempSelectedMonth == null || tempSelectedYear == null) {
                            selectedStates[byMonthIndex] = false

                            if (previousSelectedIndex != -1 && previousSelectedIndex != byMonthIndex) {
                                selectedStates[previousSelectedIndex] = true
                            } else {
                                selectedStates[0] = true
                            }
                        }
                    },
                )
            }
        }
    }
}

fun formatMonthYear(month: Int, year: Int): String {
    val calendar = Calendar.getInstance().apply {
        set(Calendar.MONTH, month - 1)
        set(Calendar.YEAR, year)
    }
    val monthFormat = SimpleDateFormat("MMM yyyy", Locale.getDefault())
    return monthFormat.format(calendar.time)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PickerDialog(
    onDateSelected: (month: Int, year: Int) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialDisplayMode = DisplayMode.Picker
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                datePickerState.selectedDateMillis?.let { millis ->
                    val calendar = Calendar.getInstance().apply {
                        timeInMillis = millis
                    }
                    val month = calendar.get(Calendar.MONTH) + 1
                    val year = calendar.get(Calendar.YEAR)
                    onDateSelected(month, year)
                }
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(
            state = datePickerState,
            title = {
                Text(
                    text = "Select Month and Year",
                    modifier = Modifier.padding(16.dp)
                )
            }
        )
    }
}

internal fun handleUniversalSelection(
    options: List<String>,
    selectedStates: MutableList<Boolean>,
    clickedIndex: Int,
    isSelected: Boolean,
    allOptionName: String,
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