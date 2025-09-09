package com.fintexinc.core.domain.model

data class FundsResponseAPI(
    val id: String,
    val fundCode: String,
    val fundName: String,
    val fundType: String,
    val fundCategory: String,
    val fundFamily: String,
    val riskLevel: String,
    val currency: String,
    val units: Double,
    val navPerUnit: Double,
    val priceAsOf: String,
    val lastUpdated: String,
    val isActive: Boolean,
    val isDeleted: Boolean,
    val isArchived: Boolean,
    val isSuspended: Boolean,
    val isFrozen: Boolean,
    val isLocked: Boolean,
    val topHoldings: List<Holding>,
    val sectorAllocations: List<SectorAllocation>,
    val assetAllocations: List<AssetAllocation>,
    val geographicAllocations: List<GeographicAllocation>
)

data class Holding(
    val symbol: String,
    val name: String,
    val weightPercentage: Double
)

data class SectorAllocation(
    val sector: String,
    val weightPercentage: Double
)

data class AssetAllocation(
    val assetClass: String,
    val weightPercentage: Double
)

data class GeographicAllocation(
    val region: String,
    val weightPercentage: Double
)