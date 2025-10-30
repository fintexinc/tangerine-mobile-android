package domain.usecase.account

import domain.model.Account
import domain.repository.AccountRepository
import domain.usecase.base.BaseUseCase
import javax.inject.Inject

class GetAccountByIdUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) : BaseUseCase<String, Account> {

    override suspend fun invoke(input: String?): BaseUseCase.Result<Account> {
        return try {
            val id = input ?: throw IllegalArgumentException("Account id must be provided")
            val result = accountRepository.getAccountById(id)
            BaseUseCase.Result.Success(result)
        } catch (error: Throwable) {
            BaseUseCase.Result.Failure(error)
        }
    }
}

