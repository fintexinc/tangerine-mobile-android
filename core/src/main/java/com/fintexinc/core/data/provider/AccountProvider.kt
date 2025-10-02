package com.fintexinc.core.data.provider

import com.fintexinc.core.data.mock.ACCOUNTS_MOCK
import com.fintexinc.core.data.mock.ACTIVITIES_MOCK
import com.fintexinc.core.data.mock.DOCUMENTS_MOCK
import com.fintexinc.core.data.mock.PERFORMANCE_MOCK
import com.fintexinc.core.domain.gateway.AccountGateway
import com.fintexinc.core.domain.model.Account
import com.fintexinc.core.domain.model.Document
import com.fintexinc.core.domain.model.PerformanceItem
import com.fintexinc.core.domain.model.Transaction
import kotlinx.serialization.json.Json

class AccountProvider : AccountGateway {

    override suspend fun getActivities(): List<Transaction> {
        return Json.decodeFromString(ACTIVITIES_MOCK)
    }
    override suspend fun getAccounts(): List<Account> {
        return Json.decodeFromString(ACCOUNTS_MOCK)
    }

    // Add database in future to get account by id or use API method for this
    override suspend fun getAccountById(id: String): Account {
        return getAccounts().first { it.accountId == id }
    }

    override suspend fun getDocuments(): List<Document> {
        return Json.decodeFromString(DOCUMENTS_MOCK)
    }

    override suspend fun getDocumentsByAccountId(accountId: String): List<Document> {
        return getDocuments().filter { it.accountId == accountId }
    }

    override suspend fun getPerformance(): List<PerformanceItem> {
        return Json.decodeFromString(PERFORMANCE_MOCK)
    }

    override suspend fun getPerformanceData(): List<PerformanceItem> {
        return Json.decodeFromString(PERFORMANCE_MOCK)
    }
}