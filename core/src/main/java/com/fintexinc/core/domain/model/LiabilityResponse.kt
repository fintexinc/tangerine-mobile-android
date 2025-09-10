package com.fintexinc.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Liability(
    val id: String,
    val userId: String,
    val liabilityType: String,
    val accountNumber: String,
    val balance: Double,
    val limit: Double,
    val interestRate: Double,
    val currency: String,
    val linkedDate: String,
    val lastUpdated: String
)