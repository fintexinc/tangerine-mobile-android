package com.fintexinc.core.presentation.ui.datapoint

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.fintexinc.core.R
import com.fintexinc.core.domain.model.DataPoint
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.font.FontStyles

@Composable
fun DataPointCollapsableUI(
    dataPoint: DataPoint
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp)
            .background(Colors.Background)
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                val color = Colors.BorderSubdued
                drawLine(
                    color = color,
                    start = Offset(0f, 0f),
                    end = Offset(0f, size.height),
                    strokeWidth = strokeWidth
                )
                drawLine(
                    color = color,
                    start = Offset(size.width, 0f),
                    end = Offset(size.width, size.height),
                    strokeWidth = strokeWidth
                )
            }
            .padding(vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentHeight()
            ) {
                Text(
                    text = dataPoint.name,
                    style = FontStyles.BodyLarge,
                    color = Colors.BrandBlack
                )
                Text(
                    text = dataPoint.subName,
                    style = FontStyles.BodyMedium,
                    color = Colors.TextSubdued
                )
            }
            if (dataPoint.value != null) {
                Text(
                    modifier = Modifier.wrapContentSize(),
                    text = dataPoint.value,
                    style = FontStyles.BodyLargeBold,
                    color = Colors.BrandBlack
                )
            } else {
                Icon(
                    modifier = Modifier.wrapContentSize(),
                    painter = painterResource(R.drawable.ic_arrow_right),
                    tint = Colors.BrandGray,
                    contentDescription = "Open Item"
                )
            }
        }
    }
}

@Composable
fun DataPointUI(
    dataPoint: DataPoint,
    isLastItem: Boolean = false
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Colors.Background)
            .padding(top = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentHeight()
            ) {
                Text(
                    text = dataPoint.name,
                    style = FontStyles.BodyLarge,
                    color = Colors.BrandBlack
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = dataPoint.subName,
                    style = FontStyles.BodyMedium,
                    color = Colors.TextSubdued
                )
            }
            if (dataPoint.value != null) {
                if (dataPoint.subValue != null) {
                    Column(
                        modifier = Modifier.padding(end = 12.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            modifier = Modifier.wrapContentSize(),
                            text = dataPoint.value,
                            style = FontStyles.BodyLargeBold,
                            color = Colors.BrandBlack
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            modifier = Modifier.wrapContentSize(),
                            text = dataPoint.subValue,
                            style = FontStyles.BodyMedium,
                            color = Colors.TextSubdued
                        )
                    }
                } else {
                    Text(
                        modifier = Modifier.wrapContentSize(),
                        text = dataPoint.value,
                        style = FontStyles.BodyLargeBold,
                        color = Colors.BrandBlack
                    )
                }
            } else {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(R.drawable.ic_arrow_right),
                    tint = Colors.BrandGray,
                    contentDescription = "Open Item"
                )
            }
        }
        if (!isLastItem) {
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(
                color = Colors.BorderSubdued,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }
    }
}