package com.tangerine.account.presentation.ui.bottom_tab

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.fintexinc.core.presentation.ui.widget.TangerineSearchBar
import com.fintexinc.core.presentation.ui.widget.modal.UniversalModalBottomSheet
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.font.FontStyles
import com.tangerine.account.R
import com.tangerine.account.presentation.models.DateFilterUi
import com.tangerine.account.presentation.models.TransactionUi
import com.tangerine.account.presentation.models.TransactionGroup
import com.tangerine.account.presentation.models.TransactionStatusFilter
import com.tangerine.account.presentation.models.TransactionUiType
import com.tangerine.account.presentation.models.TransactionTypeFilterUi
import com.tangerine.account.presentation.ui.components.DateFilterModalBottomSheet
import com.tangerine.account.presentation.ui.components.FilterButton
import com.tangerine.account.presentation.ui.components.MultiSelectChips
import com.tangerine.account.presentation.ui.components.handleUniversalSelection

@Composable
internal fun TransactionsUi(
    modifier: Modifier = Modifier,
) {
    val settledGroups = listOf(
        TransactionGroup(
            date = "JAN 14, 2023",
            transactions = listOf(
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
                    additionalAmount1 = "$19.5200",
                )
            )
        ),
        TransactionGroup(
            date = "MAY 30, 2023",
            transactions = listOf(
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
        )
    )

    val pendingGroups = listOf(
        TransactionGroup(
            date = "OCT 14, 2023",
            transactions = listOf(
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
                )
            )
        )
    )

    var isPendingExpanded by remember { mutableStateOf(true) }
    var isSettledExpanded by remember { mutableStateOf(true) }

    var showDateFilter by remember { mutableStateOf(false) }
    var selectedDates by remember { mutableStateOf(listOf(DateFilterUi.ALL_DATES)) }

    var showTypeFilter by remember { mutableStateOf(false) }
    var selectedTypes by remember { mutableStateOf(listOf(TransactionTypeFilterUi.ALL_TYPES)) }

    var showStatusFilter by remember { mutableStateOf(false) }
    var selectedStatuses by remember { mutableStateOf(listOf(TransactionStatusFilter.ALL_STATUS)) }

    var searchText by remember { mutableStateOf("") }

    fun filterTransactionGroups(groups: List<TransactionGroup>, query: String): List<TransactionGroup> {
        if (query.isBlank()) return groups

        return groups.mapNotNull { group ->
            val filteredTransactions = group.transactions.filter { transaction ->
                transaction.description.contains(query, ignoreCase = true)
            }
            if (filteredTransactions.isNotEmpty()) {
                group.copy(transactions = filteredTransactions)
            } else {
                null
            }
        }
    }

    val filteredPendingGroups = remember(searchText, pendingGroups) {
        filterTransactionGroups(pendingGroups, searchText)
    }

    val filteredSettledGroups = remember(searchText, settledGroups) {
        filterTransactionGroups(settledGroups, searchText)
    }

    val hasSearchResults = filteredPendingGroups.isNotEmpty() || filteredSettledGroups.isNotEmpty()
    val isSearching = searchText.isNotBlank()

    Column {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(Colors.Background),
        ) {
            item {
                TangerineSearchBar(
                    searchText = searchText,
                    onSearchTextChange = { searchText = it },
                    isShowFilter = false,
                )
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }

            item {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                ) {
                    item {
                        FilterButton(
                            text = stringResource(selectedDates.firstOrNull()?.stringResId ?: R.string.filter_all_dates),
                            onClick = { showDateFilter = true },
                        )
                    }
                    item {
                        FilterButton(
                            text = stringResource(selectedTypes.firstOrNull()?.stringResId ?: R.string.type_all_types),
                            onClick = { showTypeFilter = true },
                        )
                    }
                    item {
                        FilterButton(
                            text = stringResource(selectedStatuses.firstOrNull()?.stringResId ?: R.string.status_all_status),
                            onClick = { showStatusFilter = true },
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }

            if (isSearching && !hasSearchResults) {
                //TODO() Empty screen here
            } else {
                item {
                    TransactionsSectionHeader(
                        title = stringResource(R.string.title_pending_transactions).uppercase(),
                        isExpanded = isPendingExpanded,
                        onExpandToggle = { isPendingExpanded = !isPendingExpanded },
                    )
                }

                if (isPendingExpanded) {
                    filteredPendingGroups.forEach { group ->
                        item {
                            TransactionDateHeader(date = group.date)
                        }

                        items(group.transactions) { transaction ->
                            TransactionItem(
                                transaction = transaction,
                                onClick = { },
                            )
                        }

                        if (group != filteredPendingGroups.last()) {
                            item { Spacer(modifier = Modifier.height(16.dp)) }
                        }
                    }
                }

                item {
                    TransactionsSectionHeader(
                        title = stringResource(R.string.title_settled_transactions).uppercase(),
                        isExpanded = isSettledExpanded,
                        onExpandToggle = { isSettledExpanded = !isSettledExpanded }
                    )
                }

                if (isSettledExpanded) {
                    filteredSettledGroups.forEach { group ->
                        item {
                            TransactionDateHeader(group.date)
                        }

                        items(group.transactions) { transaction ->
                            TransactionItem(
                                transaction = transaction,
                                onClick = { },
                            )

                            if (transaction != group.transactions.last()) {
                                HorizontalDivider(
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    thickness = 0.5.dp,
                                    color = Color.Gray.copy(alpha = 0.2f),
                                )
                            }
                        }
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
                onDatesSelected = { newDates ->
                    selectedDates = newDates
                    showDateFilter = false
                },
                onDismiss = { showDateFilter = false },
            )
        }

        if (showTypeFilter) {
            TypeFilterModalBottomSheet(
                isShowing = remember { mutableStateOf(showTypeFilter) }.apply {
                    value = showTypeFilter
                },
                selectedTypes = selectedTypes,
                onTypesSelected = { types ->
                    selectedTypes = types
                    showTypeFilter = false
                },
                onDismiss = {
                    showTypeFilter = false
                },
            )
        }

        if (showStatusFilter) {
            StatusFilterModalBottomSheet(
                isShowing = remember { mutableStateOf(showStatusFilter) }.apply {
                    value = showStatusFilter
                },
                selectedStatuses = selectedStatuses,
                onStatusesSelected = { statuses ->
                    selectedStatuses = statuses
                    showStatusFilter = false
                },
                onDismiss = {
                    showStatusFilter = false
                },
            )
        }
    }
}

@Composable
private fun TransactionDateHeader(date: String) {
    Text(
        text = date,
        style = FontStyles.BodySmall,
        color = Colors.Text,
        modifier = Modifier
            .fillMaxWidth()
            .background(Colors.BackgroundSubdued)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
private fun TransactionsSectionHeader(
    title: String,
    isExpanded: Boolean,
    onExpandToggle: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Colors.BackgroundSubdued)
            .clickable { onExpandToggle() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = FontStyles.BodyLargeBold,
            color = Colors.Text,
        )

        Image(
            painter = painterResource(com.fintexinc.core.R.drawable.ic_arrow_down),
            contentDescription = stringResource(com.fintexinc.core.R.string.description_icon_add),
            modifier = Modifier.rotate(if (isExpanded) 180f else 0f),
        )
    }
}

@Composable
private fun TransactionItem(
    transaction: TransactionUi,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.Top,
    ) {

        Image(
            painter = painterResource(
                when (transaction.type) {
                    TransactionUiType.PENDING -> R.drawable.ic_pending_transaction
                    TransactionUiType.SETTLED -> R.drawable.ic_cars_transportation
                }
            ),
            contentDescription = transaction.type.name,
        )


        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = transaction.description,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = FontStyles.BodyLarge,
                color = Colors.Text,
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = transaction.fromAccount,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = FontStyles.BodyMedium,
                color = Colors.TextSubdued,
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        TransactionAmountSection(transaction)
    }
}

@Composable
private fun TransactionAmountSection(transaction: TransactionUi) {
    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = transaction.formattedAmount,
            style = FontStyles.BodyLargeBold,
            color = Colors.Text,
        )

        if (transaction.type == TransactionUiType.SETTLED) {
            transaction.additionalAmount1?.let {
                Text(
                    text = transaction.additionalAmount1,
                    style = FontStyles.BodySmall,
                    color = Colors.TextSubdued,
                )
            }

            transaction.additionalAmount2?.let {
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = it,
                    style = FontStyles.BodySmallItalic,
                    color = Colors.TextSubdued,
                )
            }
        }
    }

    Spacer(modifier = Modifier.width(8.dp))

    Icon(
        painter = painterResource(com.fintexinc.core.R.drawable.ic_arrow_right),
        contentDescription = stringResource(R.string.description_icon_open),
        tint = Colors.IconSupplementary,
    )
}

@Composable
internal fun TypeFilterModalBottomSheet(
    isShowing: MutableState<Boolean>,
    selectedTypes: List<TransactionTypeFilterUi>,
    onTypesSelected: (List<TransactionTypeFilterUi>) -> Unit,
    onDismiss: () -> Unit
) {
    val typeEnums = TransactionTypeFilterUi.entries
    val typeOptions = typeEnums.map { stringResource(it.stringResId) }
    val allOptionName = stringResource(TransactionTypeFilterUi.ALL_TYPES.stringResId)

    val selectedStates = remember(selectedTypes) {
        mutableStateListOf(*typeEnums.map { it in selectedTypes }.toTypedArray())
    }

    UniversalModalBottomSheet(
        isShowing = isShowing,
        title = stringResource(R.string.title_type),
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

@Composable
internal fun StatusFilterModalBottomSheet(
    isShowing: MutableState<Boolean>,
    selectedStatuses: List<TransactionStatusFilter>,
    onStatusesSelected: (List<TransactionStatusFilter>) -> Unit,
    onDismiss: () -> Unit
) {
    val statusEnums = TransactionStatusFilter.entries
    val statusOptions = statusEnums.map { stringResource(it.stringResId) }
    val allOptionName = stringResource(TransactionStatusFilter.ALL_STATUS.stringResId)

    val selectedStates = remember(selectedStatuses) {
        mutableStateListOf(*statusEnums.map { it in selectedStatuses }.toTypedArray())
    }

    UniversalModalBottomSheet(
        isShowing = isShowing,
        title = stringResource(R.string.title_status),
        onDoneClick = {
            val selected = statusEnums.filterIndexed { index, _ -> selectedStates[index] }
            onStatusesSelected(selected)
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
                options = statusOptions,
                selectedStates = selectedStates,
                onSelectionChanged = { index, isSelected ->
                    handleUniversalSelection(
                        options = statusOptions,
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