package com.tangerine.account.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fintexinc.core.presentation.ui.modifier.clickableShape
import com.fintexinc.core.presentation.ui.widget.modal.UniversalModalBottomSheet
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.font.FontStyles
import com.tangerine.account.R
import com.tangerine.account.presentation.models.DateFilterUi
import java.util.Calendar

@Composable
internal fun DateFilterModalBottomSheet(
    isShowing: MutableState<Boolean>,
    selectedDates: List<DateFilterUi>,
    onDatesSelected: (List<DateFilterUi>, Int?, Int?) -> Unit,
    onDismiss: () -> Unit
) {
    val dateEnums = DateFilterUi.entries
    val dateOptions = dateEnums.map { stringResource(it.stringResId) }
    val dateIcons = dateEnums.map { it.iconResId }

    val selectedStates = remember(selectedDates) {
        mutableStateListOf(*dateEnums.map { it in selectedDates }.toTypedArray())
    }

    var previousSelectedIndex by remember { mutableIntStateOf(-1) }

    var selectedMonth by remember { mutableStateOf<Int?>(null) }
    var selectedYear by remember { mutableStateOf<Int?>(null) }

    val isByMonthSelected = selectedStates.getOrNull(dateEnums.indexOf(DateFilterUi.BY_MONTH)) == true
    var showDatePicker by remember { mutableStateOf(false) }

    LaunchedEffect(isByMonthSelected) {
        if (isByMonthSelected) {
            showDatePicker = true
        }
    }

    UniversalModalBottomSheet(
        isShowing = isShowing,
        title = stringResource(R.string.title_timeframe),
        onDoneClick = {
            val selected = dateEnums.filterIndexed { index, _ -> selectedStates[index] }
            onDatesSelected(selected, selectedMonth, selectedYear)
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
                options = dateOptions,
                selectedStates = selectedStates,
                icons = dateIcons,
                onSelectionChanged = { index, _ ->
                    previousSelectedIndex = selectedStates.indexOfFirst { it }

                    selectedStates.forEachIndexed { i, _ ->
                        selectedStates[i] = false
                    }
                    selectedStates[index] = true
                }
            )

            if (showDatePicker) {
                PickerDialog(
                    onDateSelected = { month, year ->
                        selectedMonth = month
                        selectedYear = year
                        showDatePicker = false
                    },
                    onDismiss = {
                        showDatePicker = false
                        val byMonthIndex = dateEnums.indexOf(DateFilterUi.BY_MONTH)

                        if (selectedMonth == null || selectedYear == null) {
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

@Composable
internal fun MultiSelectChips(
    options: List<String>,
    selectedStates: MutableList<Boolean>,
    onSelectionChanged: (Int, Boolean) -> Unit,
    icons: List<Int?> = listOf(),
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
            Row(
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
                        val canDeselect = newSelected || selectedStates.count { it } > 1

                        if (canDeselect) {
                            selectedStates[index] = newSelected
                            onSelectionChanged(index, newSelected)
                        }
                    }
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                icons.getOrNull(index)?.let { iconRes ->
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = null,
                        tint = Colors.TextInteractive,
                    )
                }
                Text(
                    text = option,
                    style = FontStyles.BodyMedium,
                    color = Colors.TextInteractive,
                )
            }
        }
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