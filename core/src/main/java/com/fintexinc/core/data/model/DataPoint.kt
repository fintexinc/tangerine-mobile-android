package com.fintexinc.core.data.model

import androidx.annotation.DrawableRes

data class DataPoint(
    val id: String,
    val name: String,
    val subName: String,
    val value: String? = null,
    val subValue: String? = null,
    @param:DrawableRes val iconResId :Int? = null,
)