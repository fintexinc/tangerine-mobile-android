package com.fintexinc.dashboard.presentation.ui.models

data class AccountUI(
    val accountId: String,
    val name: String,
    val subName: String,
    val value: String,
    val valueChange: Double,
    val percentageChange: Double
)