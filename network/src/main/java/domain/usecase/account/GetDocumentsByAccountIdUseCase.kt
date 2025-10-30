package domain.usecase.account

import domain.model.Document
import domain.repository.AccountRepository
import domain.usecase.base.BaseUseCase
import javax.inject.Inject

class GetDocumentsByAccountIdUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) : BaseUseCase<String, List<Document>> {

    override suspend fun invoke(input: String?): BaseUseCase.Result<List<Document>> {
        return try {
            val accountId = input ?: throw IllegalArgumentException("Account id must be provided")
            val result = accountRepository.getDocumentsByAccountId(accountId)
            BaseUseCase.Result.Success(result)
        } catch (error: Throwable) {
            BaseUseCase.Result.Failure(error)
        }
    }
}

