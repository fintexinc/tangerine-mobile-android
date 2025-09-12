package com.fintexinc.core.domain.model

import com.fintexinc.core.data.model.ItemType
import com.fintexinc.core.data.serializer.LiabilityTypeSerializer
import kotlinx.serialization.Serializable

@Serializable
data class Liability(
    val id: String,
    val userId: String,
    @Serializable(with = LiabilityTypeSerializer::class)
    val liabilityType: LiabilityType,
    val accountNumber: String,
    val balance: Double,
    val limit: Double,
    val interestRate: Double,
    val currency: String,
    val linkedDate: String,
    val lastUpdated: String
)

enum class LiabilityType(override val label: String): ItemType {
    MORTGAGE("Mortgage"),
    HOME_EQUITY_LINE_OF_CREDIT("Home Equity Line of Credit"),
    AUTO_LOAN("Auto Loan"),
    CREDIT_CARD_DEBT("Credit Card Debt"),
    STUDENT_LOANS("Student Loans"),
    PERSONAL_LOANS("Personal Loans"),
    OTHER("Other")
}