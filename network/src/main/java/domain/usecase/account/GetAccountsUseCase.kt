package domain.usecase.account

import domain.model.Account
import domain.repository.AccountRepository
import domain.usecase.base.BaseUseCase
import javax.inject.Inject

class GetAccountsUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) : BaseUseCase<Nothing, List<Account>> {

    override suspend fun invoke(input: Nothing?): BaseUseCase.Result<List<Account>> {
        return try {
            val result = accountRepository.getAccounts()
            BaseUseCase.Result.Success(result)
        } catch (error: Throwable) {
            BaseUseCase.Result.Failure(error)
        }
    }
}

