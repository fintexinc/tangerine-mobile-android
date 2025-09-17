package com.fintexinc.dashboard.presentation.models

import com.fintexinc.core.domain.model.Transaction

data class GroupedTransactionsUi(
    val date: String,
    val transactions: List<Transaction>
)
