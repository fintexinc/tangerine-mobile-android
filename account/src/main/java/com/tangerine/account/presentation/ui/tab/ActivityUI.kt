package com.tangerine.account.presentation.ui.tab

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fintexinc.core.data.utils.formatDateOrToday
import com.fintexinc.core.domain.model.Transaction
import com.fintexinc.core.presentation.ui.widget.ColumnWithShadow
import com.fintexinc.core.presentation.ui.widget.TangerineSearchBar
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.font.FontStyles
import com.tangerine.account.R
import com.tangerine.account.presentation.ui.widget.ActivityList

@Composable
fun ActivityUI(data: List<Transaction>) {
    val searchText = remember {
        mutableStateOf("")
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        ColumnWithShadow(contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp)) {
            Row {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentHeight()
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        text = stringResource(R.string.text_your_next_contribution),
                        style = FontStyles.BodyLarge,
                        color = Colors.BrandBlack
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        text = stringResource(R.string.text_view_details),
                        style = FontStyles.BodyMedium,
                        color = Colors.TextSubdued
                    )
                }
                Icon(
                    modifier = Modifier.wrapContentSize(),
                    painter = painterResource(com.fintexinc.core.R.drawable.ic_close),
                    contentDescription = "Close Icon",
                    tint = Colors.BrandBlack
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        TangerineSearchBar()
        Spacer(modifier = Modifier.height(24.dp))
        ActivityList(
            activities = data.groupBy { it.transactionDate }.mapKeys { it.key.formatDateOrToday() }
        )
    }
}