package com.fintexinc.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Account(
    val id: String,
    val userId: String,
    val accountId: String,
    val income: Long,
    val householdNetWorth: Long,
    val investmentKnowledge: String,
    val riskToleranceInitial: String,
    val riskToleranceCurrent: String,
    val overallRiskTolerance: String,
    val investmentObjective: String,
    val timeHorizon: String,
    val livingExpenses: Long,
    val knowledgeChangeDate: String,
    val lastReviewedAt: String
)

