package com.tangerine.account.presentation.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fintexinc.core.data.utils.currency.formatCurrency
import com.fintexinc.core.domain.model.Transaction
import com.fintexinc.core.domain.model.TransactionStatus
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.font.FontStyles

@Composable
fun ActivityList(activities: Map<String, List<Transaction>>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 24.dp)
    ) {
        val flatList = activities.map { activity ->
            listOf(ListItem.Header(activity.key)) +
                    activity.value.map { ListItem.Activity(it) }
        }.flatten()
        LazyColumn {
            items(flatList) { listItem ->
                when (listItem) {
                    is ListItem.Header -> {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(vertical = 12.dp),
                            text = listItem.title,
                            style = FontStyles.BodyLarge,
                            color = Colors.BrandBlack
                        )
                    }

                    is ListItem.Activity -> {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(vertical = 18.dp),
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
                                    text = listItem.item.investmentDetails.fundName,
                                    style = FontStyles.BodyMedium,
                                    color = Colors.TextSubdued
                                )
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentHeight(),
                                    text = listItem.item.transactionDescription,
                                    style = FontStyles.BodySmall,
                                    color = Colors.TextSubdued
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                if (listItem.item.transactionStatus == TransactionStatus.PENDING.label) {
                                    Text(
                                        modifier = Modifier
                                            .wrapContentSize()
                                            .background(color = Colors.BackgroundHighlightSubdued)
                                            .padding(horizontal = 4.dp, vertical = 2.dp),
                                        text = listItem.item.transactionStatus,
                                        style = FontStyles.BodySmall,
                                        color = Colors.TextHighlight
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                }
                            }
                            Text(
                                modifier = Modifier
                                    .wrapContentSize()
                                    .align(Alignment.CenterVertically),
                                text = listItem.item.transactionAmount.formatCurrency(),
                                style = FontStyles.BodyLarge,
                                color = Colors.BrandBlack
                            )
                        }
                    }
                }
            }
        }
    }
}

sealed class ListItem {
    data class Header(val title: String) : ListItem()
    data class Activity(val item: Transaction) : ListItem()
}