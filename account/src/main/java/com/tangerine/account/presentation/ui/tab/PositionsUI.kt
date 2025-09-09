package com.tangerine.account.presentation.ui.tab

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.fintexinc.core.data.model.NameValue
import com.fintexinc.core.presentation.ui.widget.ColumnWithBorder
import com.fintexinc.core.presentation.ui.widget.list.SimpleNameValueItem
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.font.FontStyles

@Composable
fun PositionsUI(positions: List<NameValue>) {
    ColumnWithBorder(contentPadding = PaddingValues(0.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(
                    color = Colors.BackgroundSubdued,
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                )
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = "Core Portfolios\nBalanced Growth",
                style = FontStyles.BodyMedium,
                color = Colors.BrandBlack
            )
            Text(
                modifier = Modifier.wrapContentSize(),
                text = "20,000 CAD",
                style = FontStyles.BodyMedium,
                color = Colors.BrandBlack
            )
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(color = Colors.BorderSubdued)
        )
        positions.forEach { position ->
            SimpleNameValueItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 16.dp),
                item = position,
                isLastItem = position == positions.last()
            )
        }
    }
}