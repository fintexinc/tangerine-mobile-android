package domain.model

import kotlinx.serialization.Serializable

@Serializable
data class HistoryResponse(
    val history: List<HistoryItem>
)

@Serializable
data class HistoryItem(
    val value: Double,
    val date: String
)