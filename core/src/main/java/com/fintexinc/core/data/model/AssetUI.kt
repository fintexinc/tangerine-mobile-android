package com.fintexinc.core.data.model

import com.fintexinc.core.presentation.ui.widget.modal.NameValueChecked
import domain.model.Banking
import domain.model.Custom
import domain.model.Investment

data class BankingUI(
    val asset: Banking,
    val checkedState: NameValueChecked
)

data class InvestmentUI(
    val asset: Investment,
    val checkedState: NameValueChecked
)

data class CustomUI(
    val asset: Custom,
    val checkedState: NameValueChecked
)