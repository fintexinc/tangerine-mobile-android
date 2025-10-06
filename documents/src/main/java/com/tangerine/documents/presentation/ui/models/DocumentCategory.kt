package com.tangerine.documents.presentation.ui.models

import androidx.annotation.DrawableRes

data class DocumentCategory(
    val id: String,
    val title: String,
    val description: String,
    @DrawableRes val icon: Int,
    val hasNewBadge: Boolean = false
)