package com.tangerine.documents.presentation.ui.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fintexinc.core.presentation.ui.widget.ColumnWithBorder
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.font.FontStyles
import com.tangerine.documents.R

@Composable
fun AccountDocumentsUI(onBackClicked: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Colors.BackgroundSubdued)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(Colors.Background)
                .padding(vertical = 12.dp, horizontal = 30.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.wrapContentSize().clickable {
                    onBackClicked()
                },
                painter = painterResource(com.fintexinc.core.R.drawable.ic_back_arrow),
                contentDescription = stringResource(R.string.description_icon_back),
                tint = Colors.BackgroundPrimary,
            )
            Text(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentHeight(),
                text = stringResource(R.string.text_account_documents),
                style = FontStyles.HeadingMediumRegular,
                color = Colors.BrandBlack,
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        val documents = listOf(
            DocumentItem(
                name = "CRM1 Annual Charges and Compensation Report 2024",
                subName = "August 8th, 2027"
            ),
            DocumentItem(
                name = "CRM2 Annual Charges and Compensation Report 2024",
                subName = "August 8th, 2027"
            ),
            DocumentItem(
                name = "CRM3 Annual Charges and Compensation Report 2024",
                subName = "August 8th, 2027"
            ),
            DocumentItem(
                name = "CRM4 Annual Charges and Compensation Report 2024",
                subName = "August 8th, 2027"
            ),
            DocumentItem(
                name = "CRM5 Annual Charges and Compensation Report 2024",
                subName = "August 8th, 2027"
            )
        )
        DocumentList(
            documents = listOf(
                Documents(
                    title = "August",
                    items = documents
                ),
                Documents(
                    title = "July",
                    items = documents
                ),
            )
        )
    }
}

@Composable
private fun DocumentList(documents: List<Documents>) {
    val flatList = documents.flatMap { documents ->
        listOf(ListItem.Header(documents.title)) +
                documents.items.map { ListItem.Document(it) }
    }
    LazyColumn {
        items(flatList) { listItem ->
            when (listItem) {
                is ListItem.Header -> {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(vertical = 12.dp, horizontal = 16.dp),
                        text = listItem.title,
                        style = FontStyles.BodyLarge,
                        color = Colors.BrandBlack
                    )
                }

                is ListItem.Document -> {
                    ColumnWithBorder {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .wrapContentHeight()
                            ) {
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentHeight(),
                                    text = listItem.item.name,
                                    style = FontStyles.BodyMedium,
                                    color = Colors.BrandBlack
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentHeight(),
                                    text = listItem.item.subName,
                                    style = FontStyles.BodyMedium,
                                    color = Colors.TextSubdued
                                )
                            }
                            Icon(
                                modifier = Modifier
                                    .wrapContentSize(),
                                painter = painterResource(com.fintexinc.core.R.drawable.ic_arrow_right),
                                contentDescription = stringResource(R.string.description_icon_right),
                                tint = Colors.BrandBlack,
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

data class Documents(
    val title: String,
    val items: List<DocumentItem>
)

data class DocumentItem(
    val name: String,
    val subName: String
)


sealed class ListItem {
    data class Header(val title: String) : ListItem()
    data class Document(val item: DocumentItem) : ListItem()
}
