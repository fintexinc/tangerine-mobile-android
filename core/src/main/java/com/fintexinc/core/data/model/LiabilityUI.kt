package com.fintexinc.core.data.model

import com.fintexinc.core.domain.model.Liability
import com.fintexinc.core.presentation.ui.widget.modal.NameValueChecked

data class LiabilityUI(
    val liability: Liability,
    val checkedState: NameValueChecked
)