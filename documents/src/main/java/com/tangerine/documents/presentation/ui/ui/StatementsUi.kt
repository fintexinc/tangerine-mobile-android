package com.tangerine.documents.presentation.ui.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fintexinc.core.data.model.ItemType
import com.fintexinc.core.presentation.ui.widget.ToolBar
import com.fintexinc.core.presentation.ui.widget.add.AddItemSelection
import com.fintexinc.core.presentation.ui.widget.add.ItemTypeSelection
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.components.TextButton
import com.tangerine.documents.R
import java.util.Calendar

@Composable
fun StatementsScreen(
    onBackClicked: () -> Unit = {},
) {
    val showSelectAccountSelection = remember { mutableStateOf(false) }
    val showSelectYearSelection = remember { mutableStateOf(false) }
    val showSelectMonthSelection = remember { mutableStateOf(false) }

    val accountList = AccountType.entries.toList()
    val yearList = YearType.entries.toList()
    val monthList = Month.entries.toList()

    val selectedAccount = remember { mutableStateOf(accountList.first()) }
    val selectedYear = remember { mutableStateOf(yearList.first()) }
    val selectedMonth = remember { mutableStateOf(monthList.first()) }

    val hasError = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Colors.BackgroundSubdued),
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

            item { Spacer(modifier = Modifier.height(24.dp)) }

            item {
                AddItemSelection(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    title = stringResource(R.string.text_select_account),
                    text = selectedAccount.value.label,
                    onAddItemSelectionClicked = { showSelectAccountSelection.value = true },
                    backgroundColor = Color.Transparent,
                )
            }

            item {
                AddItemSelection(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    title = stringResource(R.string.text_choose_year),
                    text = selectedYear.value.label,
                    onAddItemSelectionClicked = { showSelectYearSelection.value = true },
                    backgroundColor = Color.Transparent,
                )
            }

            item {
                AddItemSelection(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    title = stringResource(R.string.text_choose_month),
                    text = selectedMonth.value.label,
                    onAddItemSelectionClicked = {
                        showSelectMonthSelection.value = true
                        hasError.value = false
                    },
                    backgroundColor = Color.Transparent,
                    errorRes = if (hasError.value) R.string.text_error else null,
                )
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Colors.Background)
                .padding(16.dp)
        ) {
            TextButton(
                text = stringResource(R.string.text_confirm),
                onClick = {
                    val isStatementAvailable = checkStatementAvailability(
                        year = selectedYear.value,
                        month = selectedMonth.value,
                    )

                    if (isStatementAvailable) {
                        onBackClicked()
                    } else {
                        hasError.value = true
                    }
                },
                color = Colors.BackgroundPrimary,
                modifier = Modifier.padding(bottom = 16.dp),
            )
        }

        if (showSelectAccountSelection.value) {
            ItemTypeSelection(
                itemTypeTitle = stringResource(R.string.text_account),
                itemTypes = accountList,
                onItemTypeSelected = { it: ItemType ->
                    selectedAccount.value = it as AccountType
                    showSelectAccountSelection.value = false
                },
                onCancel = { showSelectAccountSelection.value = false }
            )
        }

        if (showSelectYearSelection.value) {
            ItemTypeSelection(
                itemTypeTitle = stringResource(R.string.text_year),
                itemTypes = yearList,
                onItemTypeSelected = { it: ItemType ->
                    selectedYear.value = it as YearType
                    showSelectYearSelection.value = false
                },
                onCancel = { showSelectYearSelection.value = false }
            )
        }

        if (showSelectMonthSelection.value) {
            ItemTypeSelection(
                itemTypeTitle = stringResource(R.string.text_month),
                itemTypes = monthList,
                onItemTypeSelected = { it: ItemType ->
                    selectedMonth.value = it as Month
                    showSelectMonthSelection.value = false
                },
                onCancel = { showSelectMonthSelection.value = false }
            )
        }
    }
}

private fun checkStatementAvailability(
    year: YearType,
    month: Month
): Boolean {
    val currentDate = Calendar.getInstance()
    val currentYear = currentDate.get(Calendar.YEAR)
    val currentMonth = currentDate.get(Calendar.MONTH)

    val selectedYear = parseYear(year)
    val selectedMonth = month.ordinal

    return !(selectedYear == currentYear && selectedMonth == currentMonth)
}

private fun parseYear(year: YearType): Int {
    return year.label.toIntOrNull() ?: 0
}

enum class AccountType(override val label: String) : ItemType {
    JACK_TFSA("Jack Dawson TFSA ***7978"),
    JACK_RRSP("Jack Dawson RRSP ***7978"),
    JOINT_INVESTMENT("Joint Investment ***7978"),
    NON_REGISTERED("Non-Registered ***7978");
}

enum class YearType(override val label: String) : ItemType {
    YEAR_2025("2025"),
    YEAR_2024("2024"),
    YEAR_2023("2023"),
    YEAR_2022("2022"),
}

enum class Month(override val label: String) : ItemType {
    JANUARY("January"),
    FEBRUARY("February"),
    MARCH("March"),
    APRIL("April"),
    MAY("May"),
    JUNE("June"),
    JULY("July"),
    AUGUST("August"),
    SEPTEMBER("September"),
    OCTOBER("October"),
    NOVEMBER("November"),
    DECEMBER("December");
}