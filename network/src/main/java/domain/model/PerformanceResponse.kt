package domain.model

data class PerformanceItem(
    val id: String,
    val accountId: String,
    val accountType: String,
    val value: Double,
    val currency: String,
    val date: PerformanceDate,
)

data class PerformanceDate(
    val day: Int,
    val month: Int,
    val year: Int,
)