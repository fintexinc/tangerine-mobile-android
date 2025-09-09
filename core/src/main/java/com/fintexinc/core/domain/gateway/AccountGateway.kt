package com.fintexinc.core.domain.gateway

import com.fintexinc.core.domain.model.Account
import com.fintexinc.core.domain.model.Document
import com.fintexinc.core.domain.model.Transaction

interface AccountGateway {

    suspend fun getAccounts(): List<Account>
    suspend fun getAccountById(id: String): Account
    suspend fun getActivities(): List<Transaction>
    suspend fun getDocuments(): List<Document>
    suspend fun getDocumentsByAccountId(accountId: String): List<Document> {
        return getDocuments().filter { it.accountId == accountId }
    }
}