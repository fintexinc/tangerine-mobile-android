package com.fintexinc.core.presentation.ui.widget.list

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fintexinc.core.R
import com.fintexinc.core.domain.model.DataPoint
import com.fintexinc.core.presentation.ui.datapoint.DataPointCollapsableUI
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.font.FontStyles

fun collapsableLazyColumn(
    scope: LazyListScope,
    dataPoints: List<DataPoint>,
    expanded: MutableState<Boolean>,
    title: String,
    subtitle: String? = null,
    addItemText: String,
    isLastList: Boolean = false,
    onAddItemClick: () -> Unit
) = with(scope) {
    item {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 18.dp)
                .clickable {
                    expanded.value = !expanded.value
                }
                .then(
                    if(expanded.value) {
                        Modifier.background(color = Colors.Transparent)
                    } else {
                        Modifier.background(
                            color = Colors.Background,
                            if (isLastList) RoundedCornerShape(
                                bottomStart = 16.dp,
                                bottomEnd = 16.dp
                            ) else RectangleShape
                        )
                            .then(
                                if (isLastList) {
                                    Modifier
                                        .border(
                                            width = 1.dp,
                                            color = Colors.BorderSubdued,
                                            shape = RoundedCornerShape(
                                                bottomStart = 16.dp,
                                                bottomEnd = 16.dp
                                            )
                                        )
                                } else {
                                    Modifier.drawBehind {
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
                                })
                    }
                )
                .padding(vertical = 12.dp, horizontal = 16.dp)

        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = title,
                style = FontStyles.BodyLargeBold,
                color = Colors.BrandBlack
            )
            if(subtitle != null) {
                Text(
                    modifier = Modifier.wrapContentSize(),
                    text = subtitle,
                    style = FontStyles.BodyLargeBold,
                    color = Colors.BrandBlack
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                painter = painterResource(R.drawable.ic_arrow_down),
                tint = Colors.BrandBlack,
                contentDescription = "Expand/Collapse Icon",
            )
        }
    }
    if (expanded.value) {
        items(dataPoints) {
            DataPointCollapsableUI(
                dataPoint = it
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .padding(18.dp)
                    .background(Colors.BorderSubdued)
            )
        }
        item {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .padding(18.dp)
                    .background(Colors.BorderSubdued)
            )
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 18.dp)
                    .background(color = Colors.Background)
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
                    .padding(vertical = 12.dp, horizontal = 16.dp)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .background(
                            color = Colors.Transparent,
                            shape = RoundedCornerShape(40.dp)
                        )
                        .border(
                            width = 2.dp,
                            color = Colors.Primary,
                            shape = RoundedCornerShape(40.dp)
                        )
                        .clickable {
                            onAddItemClick()
                        }
                        .padding(12.dp)
                        .align(Alignment.Center),
                    text = addItemText,
                    textAlign = TextAlign.Center,
                    style = FontStyles.HeadingLarge,
                    color = Colors.Primary
                )
            }
        }
        item {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .padding(18.dp)
                    .background(Colors.BorderSubdued)
            )
        }
    }
}