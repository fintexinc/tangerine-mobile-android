package com.fintexinc.tangerine.transaction_details.models

import androidx.compose.ui.graphics.Color

data class DocumentUi(
    val id: String,
    val amount: String,
    val description: String,
    val status: String,
    val statusColor: Color,
    val sentFrom: String,
    val sentTo: String,
    val category: String,
    val transactionDate: String,
    val note: String? = null,
)