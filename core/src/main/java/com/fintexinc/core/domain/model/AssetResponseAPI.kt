package com.fintexinc.core.domain.model

import com.fintexinc.core.data.model.ItemType
import com.fintexinc.core.data.serializer.AssetTypeSerializer
import kotlinx.serialization.Serializable

@Serializable
data class AssetResponse(
    val investment: List<Investment>,
    val banking: List<Banking>,
    val custom: List<Custom>
)

@Serializable
data class Investment(
    val id: String,
    val userId: String,
    val accountNumber: String,
    val registeredName: String,
    val inceptionDate: String,
    val beneficiaries: List<Beneficiary>,
    val accountName: String,
    val accountType: String,
    val productType: String,
    val fund: Fund,
    val MarketValue: Double,
    val BookCost: Double,
    val GainLoss: Double,
    val GainLossPercentage: Double,
    val linkedDate: String,
    val lastUpdated: String

)

@Serializable
data class Banking(
    val id: String,
    val userId: String,
    val accountNumber: String,
    val accountName: String,
    val accountType: String,
    val accountBalance: Double,
    val currency: String,
    val linkedDate: String,
    val lastUpdated: String
)

@Serializable
data class Custom(
    val id: String,
    val userId: String,
    val assetName: String,
    @Serializable(with = AssetTypeSerializer::class)
    val assetType: AssetType,
    val annualizedRateOfReturn: Double,
    val assetValue: Double,
    val linkedDate: String,
    val lastUpdated: String
)

@Serializable
data class Beneficiary(
    val name: String,
    val relationship: String,
    val percentage: Double,
)

@Serializable
data class Fund(
    val fundCode: String,
    val fundName: String,
    val riskLevel: String,
    val currency: String,
    val units: Double,
    val navPerUnit: Double,
    val priceAsOf: String
)

enum class AssetType(override val label: String) : ItemType {
    CASH_AND_CASH_EQUIVALENTS("Cash & Cash Equivalents"),
    PERSONAL_INVESTMENT_ACCOUNTS("Personal Investment Accounts"),
    BUSINESS_INTEREST_AND_INVESTMENTS("Business Interest & Investments"),
    REAL_ESTATE("Real Estate"),
    VEHICLES("Vehicles"),
    LUXURY_ASSETS("Luxury Assets"),
    CRYPTOCURRENCY("Cryptocurrency"),
    OTHER("Other")
}