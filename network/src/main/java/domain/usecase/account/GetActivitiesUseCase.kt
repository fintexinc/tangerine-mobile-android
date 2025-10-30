package domain.usecase.account

import domain.model.Transaction
import domain.repository.AccountRepository
import domain.usecase.base.BaseUseCase
import javax.inject.Inject

class GetActivitiesUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) : BaseUseCase<Nothing, List<Transaction>> {

    override suspend fun invoke(input: Nothing?): BaseUseCase.Result<List<Transaction>> {
        return try {
            val result = accountRepository.getActivities()
            BaseUseCase.Result.Success(result)
        } catch (error: Throwable) {
            BaseUseCase.Result.Failure(error)
        }
    }
}

