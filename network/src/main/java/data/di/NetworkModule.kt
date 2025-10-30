package data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import data.repository.LocalAccountRepository
import data.repository.LocalNetWorthRepository
import domain.repository.AccountRepository
import domain.repository.NetWorthRepository

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    // TODO: Replace with remote implementation when available
    @Provides
    fun provideAccountRepository(): AccountRepository {
        return LocalAccountRepository()
    }

    // TODO: Replace with remote implementation when available
    @Provides
    fun provideNetWorthRepository(): NetWorthRepository {
        return LocalNetWorthRepository()
    }
}