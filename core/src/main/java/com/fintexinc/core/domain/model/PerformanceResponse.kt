package com.fintexinc.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PerformanceItem(
    val id: String,
    val accountId: String,
    val accountType: String,
    val value: Double,
    val currency: String,
    val date: PerformanceDate,
)

@Serializable
data class PerformanceDate(
    val day: Int,
    val month: Int,
    val year: Int,
)