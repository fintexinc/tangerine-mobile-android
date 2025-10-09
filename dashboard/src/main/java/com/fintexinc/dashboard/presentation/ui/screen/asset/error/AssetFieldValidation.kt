package com.fintexinc.dashboard.presentation.ui.screen.asset.error

data class AssetFieldValidation(
    val isValid: Boolean,
    val assetError: AssetError? = null
)