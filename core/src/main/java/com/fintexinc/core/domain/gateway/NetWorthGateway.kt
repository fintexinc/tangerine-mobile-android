package com.fintexinc.core.domain.gateway

import com.fintexinc.core.domain.model.AssetResponse
import com.fintexinc.core.domain.model.Liability

interface NetWorthGateway {

    suspend fun getAssets(): AssetResponse
    suspend fun getLiabilities(): List<Liability>
}