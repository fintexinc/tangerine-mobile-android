package domain.usecase.base

interface BaseUseCase<I: Any?, O: Any> {

    suspend operator fun invoke(input: I? = null): Result<O>

    sealed class Result<out O: Any> {
        data class Success<out O: Any>(val data: O): Result<O>()
        data class Failure(val throwable: Throwable): Result<Nothing>()
    }
}