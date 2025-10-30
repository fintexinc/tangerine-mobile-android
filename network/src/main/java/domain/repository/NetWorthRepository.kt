package domain.repository

import domain.model.AssetResponse
import domain.model.Liability

interface NetWorthRepository {

    suspend fun getAssets(): AssetResponse
    suspend fun getLiabilities(): List<Liability>
}