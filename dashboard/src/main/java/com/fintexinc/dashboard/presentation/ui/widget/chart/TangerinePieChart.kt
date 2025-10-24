package com.fintexinc.dashboard.presentation.ui.widget.chart

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fintexinc.core.presentation.ui.modifier.clickableShape
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.font.FontStyles
import com.tangerine.charts.compose_charts.PieChart
import com.tangerine.charts.compose_charts.models.Pie

@Composable
fun TangerinePieChart(
    title: String,
    pieData: List<Pie>,
    chipText: String = "All Accounts",
    onFilterClick: () -> Unit = {},
    isPieDataScrollable: Boolean = false,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(418.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier
                    .weight(1.0f)
                    .wrapContentHeight(),
                text = title,
                style = FontStyles.TitleMedium,
                maxLines = 2,
            )
            Row(
                modifier = Modifier
                    .wrapContentSize()
                    .shadow(1.dp, RoundedCornerShape(16.dp))
                    .background(
                        color = Colors.BackgroundInteractive,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clickableShape(RoundedCornerShape(16.dp)) {
                        onFilterClick()
                    }
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.wrapContentSize(),
                    text = chipText,
                    style = FontStyles.BodyMedium,
                    color = Colors.TextInteractive
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    painter = painterResource(com.fintexinc.core.R.drawable.ic_chevron_down),
                    tint = Colors.TextInteractive,
                    contentDescription = "",
                    modifier = Modifier.size(24.dp),
                )
            }
        }
        Spacer(modifier = Modifier.height(18.dp))
        PieChart(
            modifier = Modifier
                .fillMaxWidth()
                .height(145.dp),
            data = pieData,
            style = Pie.Style.Stroke(width = 24.dp),
            spaceDegree = 5F,
        )
        Spacer(modifier = Modifier.height(44.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (isPieDataScrollable) Modifier.weight(1f)
                    else Modifier.wrapContentHeight()
                )
        ) {
            val scrollState = rememberScrollState()

            Column(
                modifier = Modifier
                    .weight(1f)
                    .then(
                        if (isPieDataScrollable) Modifier.verticalScroll(scrollState)
                        else Modifier
                    )
                    .padding(horizontal = 8.dp)
            ) {
                pieData.forEach {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(color = it.color, shape = CircleShape)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            modifier = Modifier
                                .weight(1f)
                                .wrapContentHeight(),
                            text = it.label ?: "",
                            style = FontStyles.BodyMedium,
                            color = Colors.Text
                        )
                        Text(
                            modifier = Modifier.wrapContentHeight(),
                            text = "${it.data}%",
                            style = FontStyles.BodyMedium,
                            color = Colors.Text,
                            textAlign = TextAlign.End
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = Colors.BorderSubdued)
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            if (isPieDataScrollable) {
                Spacer(modifier = Modifier.width(6.dp))
                VerticalScrollbar(
                    modifier = Modifier,
                    scrollState = scrollState,
                    thumbColor = Colors.BorderSubdued,
                    thumbWidth = 4.dp
                )
            }
        }
    }
}

@Composable
fun VerticalScrollbar(
    modifier: Modifier = Modifier,
    scrollState: ScrollState,
    thumbColor: Color = Color(0xFF9E9E9E),
    thumbShape: Shape = RoundedCornerShape(4.dp),
    thumbWidth: Dp = 8.dp,
    thumbMinHeight: Dp = 48.dp,
    visible: Boolean = scrollState.maxValue > 0
) {
    if (!visible) return

    val density = LocalDensity.current

    Box(
        modifier = modifier
            .fillMaxHeight()
            .width(thumbWidth)
    ) {
        val viewportHeight = with(density) { scrollState.viewportSize.toDp() }
        val contentHeight =
            with(density) { (scrollState.maxValue + scrollState.viewportSize).toDp() }

        val thumbHeightRatio = (viewportHeight / contentHeight).coerceIn(0f, 1f)
        val thumbHeight = (viewportHeight * thumbHeightRatio).coerceAtLeast(thumbMinHeight)

        val scrollRatio = if (scrollState.maxValue > 0) {
            scrollState.value.toFloat() / scrollState.maxValue
        } else 0f

        val thumbOffsetMax = viewportHeight - thumbHeight
        val thumbOffset = thumbOffsetMax * scrollRatio

        Box(
            modifier = Modifier
                .offset(y = thumbOffset)
                .width(thumbWidth)
                .height(thumbHeight)
                .background(color = thumbColor, shape = thumbShape)
        )
    }
}