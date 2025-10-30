package domain.repository

import domain.model.Account
import domain.model.Document
import domain.model.Transaction
import domain.model.PerformanceItem

interface AccountRepository {
    suspend fun getAccounts(): List<Account>
    suspend fun getAccountById(id: String): Account
    suspend fun getActivities(): List<Transaction>
    suspend fun getDocuments(): List<Document>
    suspend fun getDocumentsByAccountId(accountId: String): List<Document> {
        return getDocuments().filter { it.accountId == accountId }
    }
    suspend fun getPerformanceData(): List<PerformanceItem>
    suspend fun getDocumentsById(documentId: String): Document?
}