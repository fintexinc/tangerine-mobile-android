package com.fintexinc.core.data.model

import androidx.compose.ui.graphics.Color
import com.fintexinc.core.ui.color.Colors

data class NameValue(
    val name: String,
    val value: String,
    val valueColor: Color = Colors.BrandBlack
)