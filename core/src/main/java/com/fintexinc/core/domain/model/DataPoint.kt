package com.fintexinc.core.domain.model

data class DataPoint(
    val name: String,
    val subName: String,
    val value: String? = null,
    val subValue: String? = null
)