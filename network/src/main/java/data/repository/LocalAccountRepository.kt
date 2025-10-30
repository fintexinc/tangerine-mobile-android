package data.repository

import data.mock.ACCOUNTS_MOCK
import data.mock.ACTIVITIES_MOCK
import data.mock.DOCUMENTS_MOCK
import data.mock.PERFORMANCE_MOCK
import domain.model.Account
import domain.model.Document
import domain.model.PerformanceItem
import domain.model.Transaction
import domain.repository.AccountRepository
import kotlinx.serialization.json.Json

// TODO: this is a local mock repository implementation. We need to create NetworkAccountRepository
class LocalAccountRepository : AccountRepository {

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

    override suspend fun getPerformanceData(): List<PerformanceItem> {
        return Json.decodeFromString(PERFORMANCE_MOCK)
    }

    override suspend fun getDocumentsById(documentId: String): Document? {
        return getDocuments().find { it.id == documentId }
    }
}