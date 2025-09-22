package com.tangerine.account.presentation.models

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.font.FontStyles

data class DataSectionItemUi(
    val label: String,
    val value: String,
    val valueColor: Color = Colors.Text,
    val hasInfoIcon: Boolean = false,
    val hasTrailingIcon: Boolean = false,
    val trailingIconRes: Int? = null,
    val valueStyle: TextStyle = FontStyles.BodyLarge,
    val labelStyle: TextStyle = FontStyles.BodyMedium,
    val isHighlighted: Boolean = false,
    val showColorDot: Boolean = false,
    val dotColor: Color = Color.Gray,
    val isMultiline: Boolean = false
)
