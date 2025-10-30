package domain.usecase.account

import domain.model.Document
import domain.repository.AccountRepository
import domain.usecase.base.BaseUseCase
import javax.inject.Inject

class GetDocumentByIdUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) : BaseUseCase<String, Document> {

    override suspend fun invoke(input: String?): BaseUseCase.Result<Document> {
        return try {
            val documentId = input ?: throw IllegalArgumentException("Document id must be provided")
            val result = accountRepository.getDocumentsById(documentId)
            return if(result == null) {
                BaseUseCase.Result.Failure(IllegalArgumentException("Document not found"))
            } else {
                BaseUseCase.Result.Success(result)
            }
        } catch (error: Throwable) {
            BaseUseCase.Result.Failure(error)
        }
    }
}

