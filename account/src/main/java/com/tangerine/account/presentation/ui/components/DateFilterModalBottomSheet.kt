package com.tangerine.account.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fintexinc.core.presentation.ui.modifier.clickableShape
import com.fintexinc.core.presentation.ui.widget.modal.UniversalModalBottomSheet
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.font.FontStyles
import com.tangerine.account.R
import com.tangerine.account.presentation.models.DateFilterUi

@Composable
internal fun DateFilterModalBottomSheet(
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
        onDismiss = onDismiss,
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
                        allOptionName = allOptionName,
                    )
                }
            )
        }
    }
}

@Composable
internal fun MultiSelectChips(
    options: List<String>,
    selectedStates: MutableList<Boolean>,
    onSelectionChanged: (Int, Boolean) -> Unit,
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
                color = Colors.TextInteractive,
            )
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