package com.tangerine.account.presentation.models

enum class TransactionUiType {
    PENDING,
    SETTLED
}

data class TransactionUi(
    val id: String,
    val description: String,
    val fromAccount: String,
    val amount: Double,
    val date: String,
    val type: TransactionUiType = TransactionUiType.PENDING,
    val additionalAmount1: String? = null,
    val additionalAmount2: String? = null,
    val rightColumnTitle: String? = null,
    val rightColumnSubtitle: String? = null
) {
    val formattedAmount: String
        get() = if (amount > 0) "+$${String.format("%.2f", amount)}"
        else if (amount == 0.0) ""
        else "-$${String.format("%.2f", kotlin.math.abs(amount))}"
}

data class TransactionGroup(
    val date: String,
    val transactions: List<TransactionUi>
)