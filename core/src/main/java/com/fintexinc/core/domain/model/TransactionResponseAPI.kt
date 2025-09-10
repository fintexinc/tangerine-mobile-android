package com.fintexinc.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Transaction(
    val id: String,
    val accountId: String,
    val accountCategory: String,
    val accountType: String,
    val productType: String,
    val transactionDate: String,
    val tradeDate: String,
    val settlementDate: String,
    val postedDate: String,
    val transactionType: String,
    val tradeAction: String,
    val transactionAmount: Double,
    val grossAmount: Double,
    val feeAmount: Double,
    val netAmount: Double,
    val currency: String,
    val transactionDescription: String,
    val referenceNumber: String,
    val transactionStatus: String,
    val investmentDetails: InvestmentDetails
)

@Serializable
data class InvestmentDetails(
    val fundCode: String,
    val fundName: String,
    val units: Double,
    val navPerUnit: Double,
    val distributionType: String,
    val taxYear: Int
)

enum class TransactionStatus(val label: String) {
    PENDING("PENDING"),
    COMPLETED("COMPLETED"),
    SETTLED("SETTLED"),
}