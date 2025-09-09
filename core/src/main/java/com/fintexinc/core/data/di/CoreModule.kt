package com.fintexinc.core.data.di

import com.fintexinc.core.data.provider.AccountProvider
import com.fintexinc.core.data.provider.NetWorthProvider
import com.fintexinc.core.domain.gateway.AccountGateway
import com.fintexinc.core.domain.gateway.NetWorthGateway
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class CoreModule {

    @Provides
    fun provideAccountGateway(): AccountGateway {
        return AccountProvider()
    }

    @Provides
    fun provideNetWorthGateway(): NetWorthGateway {
        return NetWorthProvider()
    }
}