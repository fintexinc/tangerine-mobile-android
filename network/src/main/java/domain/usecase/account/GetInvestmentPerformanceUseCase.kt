package domain.usecase.account

import domain.model.PerformanceItem
import domain.repository.AccountRepository
import domain.usecase.base.BaseUseCase
import javax.inject.Inject

class GetInvestmentPerformanceUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) : BaseUseCase<Nothing, List<PerformanceItem>> {

    override suspend fun invoke(input: Nothing?): BaseUseCase.Result<List<PerformanceItem>> {
        return try {
            val result = accountRepository.getPerformanceData()
            BaseUseCase.Result.Success(result)
        } catch (error: Throwable) {
            BaseUseCase.Result.Failure(error)
        }
    }
}