package com.fintexinc.dashboard.presentation.ui.screen.asset.error

data class FieldValidation(
    val isValid: Boolean,
    val assetError: AssetError? = null
)