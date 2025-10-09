package com.tangerine.documents.presentation.ui.models

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.tangerine.documents.R

data class DocumentCategory(
    val id: String,
    val title: String,
    val description: String,
    @DrawableRes val icon: Int,
    val hasNewBadge: Boolean = false
)

enum class DocumentCategoryType(
    val id: String,
    @StringRes val titleRes: Int,
    @StringRes val descriptionRes: Int,
    @DrawableRes val iconRes: Int,
) {
    STATEMENTS(
        id = "statements",
        titleRes = R.string.text_category_statements_title,
        descriptionRes = R.string.text_category_statements_description,
        iconRes = com.fintexinc.core.R.drawable.ic_file,
    ),
    TAX_DOCUMENTS(
        id = "tax_documents",
        titleRes = R.string.text_category_tax_documents_title,
        descriptionRes = R.string.text_category_tax_documents_description,
        iconRes = R.drawable.ic_tax,
    ),
    ACCOUNT_DOCUMENTS(
        id = "account_documents",
        titleRes = R.string.text_category_account_documents_title,
        descriptionRes = R.string.text_category_account_documents_description,
        iconRes = com.fintexinc.core.R.drawable.ic_file,
    );

    fun toDocumentCategory(context: Context, hasNewBadge: Boolean = false): DocumentCategory {
        return DocumentCategory(
            id = id,
            title = context.getString(titleRes),
            description = context.getString(descriptionRes),
            icon = iconRes,
            hasNewBadge = hasNewBadge,
        )
    }
}