package com.fintexinc.dashboard.presentation.ui.screen.asset.error

data class LiabilityFieldValidation(
    val isValid: Boolean,
    val liabilityError: LiabilityError? = null
)