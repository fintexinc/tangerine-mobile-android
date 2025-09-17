package com.tangerine.account.presentation.ui.tab

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fintexinc.core.data.utils.date.formatToString
import com.fintexinc.core.domain.model.Document
import com.fintexinc.core.presentation.ui.widget.ColumnWithBorder
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.font.FontStyles
import com.tangerine.account.R

@Composable
fun DocumentsUI(documents: List<Document>, onOpenDocuments: () -> Unit = {}) {
    // TODO: add proper data management for these
    val documentGroups = listOf(
        Triple("Account Documents", "Investment account setup, agreements, and preference", 10),
        Triple("Statements", "Regular monthly and annual account summaries", 8),
        Triple("Tax Documents", "T3, T5, T5008 and other tax forms", 10),
    )
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(documentGroups) {
            DocumentItemUI(
                title = it.first,
                subTitle = it.second,
                documentCount = it.third,
                onOpenDocuments = onOpenDocuments
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
        item {
            Spacer(modifier = Modifier.height(12.dp))
        }
        item {
            Text(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentHeight()
                    .padding(horizontal = 16.dp),
                text = stringResource(R.string.text_all_documents),
                style = FontStyles.BodyLarge,
                color = Colors.BrandBlack
            )
        }
        item {
            Spacer(modifier = Modifier.height(12.dp))
        }
        item {
            val searchText = remember { mutableStateOf("") }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentHeight()
                        .background(color = Colors.LightGray, shape = RoundedCornerShape(12.dp))
                        .padding(8.dp),
                    value = searchText.value,
                    onValueChange = {
                        searchText.value = it
                    },
                    cursorBrush = SolidColor(Colors.BrandBlack), // Set cursor color
                    decorationBox = { innerTextField ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(com.fintexinc.core.R.drawable.ic_search),
                                contentDescription = stringResource(R.string.description_icon_search),
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Box(modifier = Modifier.weight(1f)) {
                                if (searchText.value.isEmpty()) {
                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .wrapContentHeight(),
                                        text = stringResource(R.string.text_search),
                                        style = FontStyles.BodyLarge,
                                        color = Colors.TextSubdued
                                    )
                                }
                                innerTextField()
                            }
                        }
                    },
                    textStyle = FontStyles.BodyLarge.copy(color = Colors.BrandBlack)
                )
            }
        }
        item {
            Spacer(modifier = Modifier.height(12.dp))
        }
        items(documents) {
            ColumnWithBorder {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier
                        .weight(1f)
                        .wrapContentHeight()
                    ) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            text = it.documentName,
                            style = FontStyles.BodyMedium,
                            color = Colors.BrandBlack
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            text = it.documentDate.formatToString(),
                            style = FontStyles.BodyMedium,
                            color = Colors.TextSubdued
                        )
                    }
                    Icon(
                        modifier = Modifier
                            .wrapContentSize(),
                        painter = painterResource(com.fintexinc.core.R.drawable.ic_arrow_right),
                        contentDescription = stringResource(R.string.description_icon_navigate_right),
                        tint = Colors.BrandBlack
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
        item {
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Composable
private fun DocumentItemUI(
    title: String,
    subTitle: String,
    documentCount: Int,
    onOpenDocuments: () -> Unit = {}
) {
    ColumnWithBorder {
        Row(modifier = Modifier.clickable {
            onOpenDocuments()
        }) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier.wrapContentSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.wrapContentSize(),
                        text = title,
                        style = FontStyles.BodyMedium,
                        color = Colors.BrandBlack,
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        modifier = Modifier
                            .wrapContentSize()
                            .background(
                                color = Colors.BackgroundHighlightSubdued,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 4.dp, vertical = 2.dp),
                        text = documentCount.toString(),
                        style = FontStyles.BodySmall,
                        color = Colors.TextHighlight
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    modifier = Modifier.wrapContentSize(),
                    text = subTitle,
                    style = FontStyles.BodyMedium,
                    color = Colors.TextSubdued,
                )
            }
            Icon(
                modifier = Modifier
                    .wrapContentSize(),
                painter = painterResource(com.fintexinc.core.R.drawable.ic_arrow_right),
                contentDescription = stringResource(R.string.description_icon_navigate_right),
                tint = Colors.BrandBlack
            )
        }
    }
}