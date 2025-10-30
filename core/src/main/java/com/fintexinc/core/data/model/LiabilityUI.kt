package com.fintexinc.core.data.model

import com.fintexinc.core.presentation.ui.widget.modal.NameValueChecked
import domain.model.Liability

data class LiabilityUI(
    val liability: Liability,
    val checkedState: NameValueChecked
)