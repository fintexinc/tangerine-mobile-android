package domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Document(
    val id: String,
    val accountId: String,
    val documentType: String,
    val documentName: String,
    val documentDate: DocumentDate,
    val language: String,
    val contentType: String,
    val fileFormat: String,
    val fileSizeBytes: Long,
    val pageCount: Int,
    val downloadUrl: String,
    val documentStatus: String,
    val readStatus: String,
    val viewedAt: String,
    val documentDescription: String
)

@Serializable
data class DocumentDate(
    val year: Int,
    val month: Int,
    val day: Int
)