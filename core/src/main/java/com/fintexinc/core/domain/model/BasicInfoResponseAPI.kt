package com.fintexinc.core.domain.model

data class BasicInfo(
    val basicInfo: List<Investor>
)

data class Investor(
    val investorId: String,
    val investorName: String
)