package data.repository

import data.mock.ASSETS_MOCK
import data.mock.LIABILITIES_MOCK
import domain.model.AssetResponse
import domain.model.Liability
import domain.repository.NetWorthRepository
import kotlinx.serialization.json.Json

class LocalNetWorthRepository : NetWorthRepository {

    override suspend fun getAssets(): AssetResponse {
        return Json.decodeFromString(ASSETS_MOCK)
    }

    override suspend fun getLiabilities(): List<Liability> {
        return Json.decodeFromString(LIABILITIES_MOCK)
    }
}