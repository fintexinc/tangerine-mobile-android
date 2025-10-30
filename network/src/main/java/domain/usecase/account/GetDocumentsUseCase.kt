package domain.usecase.account

import domain.model.Document
import domain.repository.AccountRepository
import domain.usecase.base.BaseUseCase
import javax.inject.Inject

class GetDocumentsUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) : BaseUseCase<Nothing, List<Document>> {

    override suspend fun invoke(input: Nothing?): BaseUseCase.Result<List<Document>> {
        return try {
            val result = accountRepository.getDocuments()
            BaseUseCase.Result.Success(result)
        } catch (error: Throwable) {
            BaseUseCase.Result.Failure(error)
        }
    }
}

