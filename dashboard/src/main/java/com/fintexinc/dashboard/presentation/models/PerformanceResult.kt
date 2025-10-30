package com.fintexinc.dashboard.presentation.models

import androidx.compose.runtime.Immutable
import com.fintexinc.core.domain.model.PerformanceItem

@Immutable
data class PerformanceResult(
    val performanceItems: List<PerformanceItem> = emptyList(),
    val error: Throwable? = null
)