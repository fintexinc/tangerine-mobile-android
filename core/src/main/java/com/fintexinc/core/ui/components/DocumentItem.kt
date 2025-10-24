package com.fintexinc.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fintexinc.core.R
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.font.FontStyles

@Composable
fun DocumentItem(
    modifier: Modifier = Modifier,
    title: String,
    date: String,
    onClick: () -> Unit,
    isLastItem: Boolean = false,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(Colors.Background)
            .clickable { onClick() }
            .padding(top = 12.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.Top,
        ) {
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .padding(top = 2.dp),
                painter = painterResource(R.drawable.ic_file),
                tint = Colors.BrandBlack,
                contentDescription = stringResource(R.string.description_documents),
            )
            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = title,
                            style = FontStyles.BodyLarge,
                            color = Colors.Text,
                        )
                        Text(
                            text = date,
                            style = FontStyles.BodyMedium,
                            color = Colors.TextSubdued,
                            modifier = Modifier.padding(top = 4.dp),
                        )
                    }

                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(R.drawable.ic_arrow_right),
                        tint = Colors.IconSupplementary,
                        contentDescription = stringResource(R.string.description_view_details),
                    )
                }
            }

            if (!isLastItem) {
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(
                    color = Colors.BorderSubdued,
                    modifier = Modifier,
                )
            }
        }
    }
}