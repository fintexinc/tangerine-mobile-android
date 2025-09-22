package com.fintexinc.core.data.model

import com.fintexinc.core.domain.model.Banking
import com.fintexinc.core.domain.model.Custom
import com.fintexinc.core.domain.model.Investment
import com.fintexinc.core.presentation.ui.widget.modal.NameValueChecked

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