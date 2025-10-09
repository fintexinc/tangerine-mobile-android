package com.fintexinc.core.domain.model

import com.fintexinc.core.data.model.ItemType
import com.fintexinc.core.data.serializer.LiabilityTypeSerializer
import kotlinx.serialization.Serializable

@Serializable
data class Liability(
    val id: String,
    val userId: String,
    val liabilityName: String,
    @Serializable(with = LiabilityTypeSerializer::class)
    val liabilityType: LiabilityType,
    val accountNumber: String,
    val balance: Double,
    val limit: Double,
    val interestRate: Double,
    val currency: String,
    val linkedDate: String,
    val lastUpdated: String,
    // TODO: find out how to differentiate between tangerine and custom liability
    val isCustomLiability: Boolean = false
)

enum class LiabilityType(override val label: String): ItemType {
    MORTGAGE("Mortgage"),
    CREDIT_CARD("Credit Card"),
    LINE_OF_CREDIT("Line of Credit"),
    PERSONAL_LOAN("Personal Loan"),
    BUSINESS_LOAN("Business Loan"),
    OTHER("Other")
}