package com.fintexinc.dashboard.presentation.ui.mapper

import com.fintexinc.core.data.utils.date.formatDisplayDate
import com.fintexinc.core.domain.model.Transaction
import com.fintexinc.dashboard.presentation.models.GroupedTransactionsUi
import kotlin.collections.component1
import kotlin.collections.component2

fun List<Transaction>.groupByDate(): List<GroupedTransactionsUi> {
    return this
        .groupBy { transaction ->
            transaction.transactionDate
        }
        .map { (date, transactions) ->
            GroupedTransactionsUi(
                date = formatDisplayDate(date),
                transactions = transactions.sortedByDescending { it.transactionDate }
            )
        }
        .sortedByDescending { it.date }
}
