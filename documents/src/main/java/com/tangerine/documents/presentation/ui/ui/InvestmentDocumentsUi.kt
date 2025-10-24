package com.tangerine.documents.presentation.ui.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fintexinc.core.data.model.DataPoint
import com.fintexinc.core.presentation.ui.modifier.clickableShape
import com.fintexinc.core.presentation.ui.widget.TangerineSearchBar
import com.fintexinc.core.presentation.ui.widget.ToolBar
import com.fintexinc.core.presentation.ui.widget.list.itemsWithBackground
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.components.DocumentItem
import com.fintexinc.core.ui.font.FontStyles
import com.tangerine.documents.R
import com.tangerine.documents.presentation.ui.models.DocumentCategory
import com.tangerine.documents.presentation.ui.models.DocumentCategoryType

@Composable
fun InvestmentDocumentsUi(
    onBackClicked: () -> Unit = {},
    onStatementsClick: () -> Unit,
    onOpenDocument: (DataPoint) -> Unit,
) {
    val searchQuery = remember { mutableStateOf("") }
    val statementsText = stringResource(R.string.text_category_statements_title)
    val context = LocalContext.current

    // TODO() mock data
    val documents = listOf(
        DataPoint(
            id = "1",
            name = "CRM1 Annual Charges and Compensation Report 2024",
            subName = "MAR 14, 2023",
            value = null,
            iconResId = com.fintexinc.core.R.drawable.ic_file,
        ),
        DataPoint(
            id = "2",
            name = "CRM2 Annual Charges and Compensation Report 2024",
            subName = "MAR 14, 2023",
            value = null,
            iconResId = com.fintexinc.core.R.drawable.ic_file,
        ),
        DataPoint(
            id = "3",
            name = "CRM3 Annual Charges and Compensation Report 2024",
            subName = "MAR 14, 2023",
            value = null,
            iconResId = com.fintexinc.core.R.drawable.ic_file,
        ),
        DataPoint(
            id = "4",
            name = "CRM4 Annual Charges and Compensation Report 2024",
            subName = "MAR 14, 2023",
            value = null,
            iconResId = com.fintexinc.core.R.drawable.ic_file,
        ),
        DataPoint(
            id = "5",
            name = "CRM5 Annual Charges and Compensation Report 2024",
            subName = "MAR 14, 2023",
            value = null,
            iconResId = com.fintexinc.core.R.drawable.ic_file,
        ),
        DataPoint(
            id = "6",
            name = "CRM6 Annual Charges and Compensation Report 2024",
            subName = "MAR 14, 2023",
            value = null,
            iconResId = com.fintexinc.core.R.drawable.ic_file,
        ),
    )

    val filteredDocuments = remember(searchQuery.value, documents) {
        if (searchQuery.value.isBlank()) {
            documents
        } else {
            documents.filter { document ->
                document.name.contains(searchQuery.value, ignoreCase = true) ||
                        document.subName.contains(searchQuery.value, ignoreCase = true)
            }
        }
    }

    val categories = rememberDocumentCategories()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Colors.BackgroundSubdued)
            .statusBarsPadding()
    ) {
        item {
            ToolBar(
                text = stringResource(R.string.title_investment_documents),
                leftIcon = {
                    Icon(
                        modifier = Modifier
                            .wrapContentSize()
                            .clickable {
                                onBackClicked()
                            },
                        painter = painterResource(com.fintexinc.core.R.drawable.ic_back_arrow),
                        contentDescription = stringResource(R.string.description_icon_navigate_back),
                        tint = Colors.Primary,
                    )
                },
            )
        }

        item {
            Spacer(modifier = Modifier.height(12.dp))
        }

        item {
            Text(
                text = stringResource(R.string.text_documents),
                color = Colors.Text,
                style = FontStyles.TitleSmall,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }

        item {
            Spacer(modifier = Modifier.height(12.dp))
        }

        itemsWithBackground(
            items = categories,
            key = { it.id },
            itemSpacing = 12.dp,
            clipIndividualItems = true,
        ) { category, isLastItem ->
            DocumentCategoryItem(
                category = category,
                onClick = { it ->
                    when (it.title) {
                        statementsText -> onStatementsClick()
                    }
                },
            )
        }

        if (documents.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                Text(
                    text = stringResource(R.string.text_all_documents),
                    color = Colors.Text,
                    style = FontStyles.TitleSmall,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }

            item {
                Spacer(modifier = Modifier.height(12.dp))
            }

            item {
                TangerineSearchBar(
                    horizontalPadding = 0.dp,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    isShowFilter = false,
                    searchText = searchQuery.value,
                    onSearchTextChange = { newQuery ->
                        searchQuery.value = newQuery
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            itemsWithBackground(
                items = filteredDocuments,
                key = { it.id },
                bottomPadding = 16.dp,
            ) { document, isLastItem ->
                DocumentItem(
                    title = document.name,
                    date = document.subName,
                    onClick = {
                        onOpenDocument(document)
                    },
                    isLastItem = isLastItem,
                    modifier = Modifier.wrapContentHeight(),
                )
            }

            item {
                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
}

@Composable
private fun DocumentCategoryItem(
    modifier: Modifier = Modifier,
    category: DocumentCategory,
    onClick: (DocumentCategory) -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(16.dp))
            .background(color = Colors.Background)
            .clickableShape(
                shape = RoundedCornerShape(16.dp),
                onClick = { onClick(category) })
            .padding(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = Colors.BackgroundSubdued,
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(category.icon),
                    tint = Colors.BrandBlack,
                    contentDescription = category.title,
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .defaultMinSize(minHeight = 64.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = category.title,
                        style = FontStyles.BodyLarge,
                        color = Colors.BrandBlack,
                    )

                    if (category.hasNewBadge) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .background(
                                    color = Colors.TextHighlight.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .padding(horizontal = 4.dp, vertical = 2.dp),
                        ) {
                            Text(
                                text = stringResource(R.string.text_new),
                                style = FontStyles.BodySmall,
                                color = Colors.TextHighlight,
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = category.description,
                    style = FontStyles.BodyMedium,
                    color = Colors.TextSubdued,
                    maxLines = 2,
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Icon(
                painter = painterResource(com.fintexinc.core.R.drawable.ic_arrow_right),
                tint = Colors.IconSupplementary,
                contentDescription = stringResource(R.string.description_icon_open),
            )
        }
    }
}

@Composable
fun rememberDocumentCategories(): List<DocumentCategory> {
    val context = LocalContext.current
    return remember {
        DocumentCategoryType.entries.map {
            it.toDocumentCategory(context)
        }
    }
}
