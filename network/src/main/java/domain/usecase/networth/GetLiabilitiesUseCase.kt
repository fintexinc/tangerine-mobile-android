package domain.usecase.networth

import domain.model.Liability
import domain.repository.NetWorthRepository
import domain.usecase.base.BaseUseCase

class GetLiabilitiesUseCase(
    private val netWorthRepository: NetWorthRepository
) : BaseUseCase<Nothing, List<Liability>> {

    override suspend fun invoke(input: Nothing?): BaseUseCase.Result<List<Liability>> {
        return try {
            val result = netWorthRepository.getLiabilities()
            BaseUseCase.Result.Success(result)
        } catch (error: Throwable) {
            BaseUseCase.Result.Failure(error)
        }
    }
}

