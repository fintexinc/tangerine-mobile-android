package domain.usecase.networth

import domain.model.AssetResponse
import domain.repository.NetWorthRepository
import domain.usecase.base.BaseUseCase

class GetAssetsUseCase(
    private val netWorthRepository: NetWorthRepository
) : BaseUseCase<Nothing, AssetResponse> {

    override suspend fun invoke(input: Nothing?): BaseUseCase.Result<AssetResponse> {
        return try {
            val result = netWorthRepository.getAssets()
            BaseUseCase.Result.Success(result)
        } catch (error: Throwable) {
            BaseUseCase.Result.Failure(error)
        }
    }
}
