package com.tangerine.account.presentation.models

data class ReturnsItemUi(
    val label: String,
    val amount: String,
    val percentage: String? = null,
    val isPositive: Boolean? = null,
    val hasInfoIcon: Boolean = false,
    val showArrow: Boolean = true,
)