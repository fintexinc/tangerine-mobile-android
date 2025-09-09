package com.fintexinc.core.data.provider

import com.fintexinc.core.data.mock.ASSETS_MOCK
import com.fintexinc.core.data.mock.LIABILITIES_MOCK
import com.fintexinc.core.domain.gateway.NetWorthGateway
import com.fintexinc.core.domain.model.AssetResponse
import com.fintexinc.core.domain.model.Liability
import kotlinx.serialization.json.Json

class NetWorthProvider: NetWorthGateway {
    override suspend fun getAssets(): AssetResponse {
        return Json.decodeFromString(ASSETS_MOCK)
    }

    override suspend fun getLiabilities(): List<Liability> {
        return Json.decodeFromString(LIABILITIES_MOCK)
    }
}