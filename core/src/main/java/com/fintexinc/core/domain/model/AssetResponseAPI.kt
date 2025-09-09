package com.fintexinc.core.domain.model

data class AssetResponse(
    val investment: List<Investment>,
    val banking: List<Banking>,
    val custom: List<Custom>
)

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

data class Custom (
    val id: String,
    val userId: String,
    val assetName: String,
    val assetType: String,
    val assetValue: Double,
    val linkedDate: String,
    val lastUpdated: String
)

data class Beneficiary(
    val name: String,
    val relationship: String,
    val percentage: Double,
)

data class Fund(
    val fundCode: String,
    val fundName: String,
    val riskLevel: String,
    val currency: String,
    val units: Double,
    val navPerUnit: Double,
    val priceAsOf: String
)