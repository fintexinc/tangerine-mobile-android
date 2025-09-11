package com.tangerine.charts.compose_charts

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.fintexinc.core.data.utils.currency.formatCurrency
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.font.FontStyles
import com.tangerine.charts.compose_charts.components.RCChartLabelHelper
import com.tangerine.charts.compose_charts.extensions.addRoundRect
import com.tangerine.charts.compose_charts.extensions.drawGridLines
import com.tangerine.charts.compose_charts.extensions.format
import com.tangerine.charts.compose_charts.extensions.spaceBetween
import com.tangerine.charts.compose_charts.extensions.split
import com.tangerine.charts.compose_charts.models.BarPopupData
import com.tangerine.charts.compose_charts.models.Bars
import com.tangerine.charts.compose_charts.models.DividerProperties
import com.tangerine.charts.compose_charts.models.GridProperties
import com.tangerine.charts.compose_charts.models.HorizontalIndicatorProperties
import com.tangerine.charts.compose_charts.models.IndicatorCount
import com.tangerine.charts.compose_charts.models.IndicatorPosition
import com.tangerine.charts.compose_charts.models.LabelHelperProperties
import com.tangerine.charts.compose_charts.models.LabelProperties
import com.tangerine.charts.compose_charts.models.SelectedBars
import com.tangerine.charts.compose_charts.models.StrokeStyle
import com.tangerine.charts.compose_charts.models.asRadiusPx
import com.tangerine.charts.compose_charts.utils.HorizontalLabels
import com.tangerine.charts.compose_charts.utils.calculateOffset
import com.tangerine.charts.compose_charts.utils.rememberComputedChartMaxValue
import kotlinx.coroutines.delay
import kotlin.math.abs
import kotlin.math.absoluteValue

@Composable
fun TangerineNetWorthChart(
    modifier: Modifier = Modifier,
    data: List<Bars>,
    legendLabels: Triple<String, String, String>,
    horizontalIndicatorStep: Double = 200000.0,
    radius: Dp = 16.dp,
    dividerProperties: DividerProperties = DividerProperties(),
    maxValue: Double = data.maxOfOrNull { it.values.maxOfOrNull { it.value } ?: 0.0 } ?: 0.0,
    minValue: Double = if (data.any { it.values.any { it.value < 0 } }) -maxValue else 0.0,
    enabledColors: Pair<Color, Color>,
    disabledColors: Pair<Color, Color>,
    isNegativeValueReversed: Boolean = false,
    onBarClick: ((BarPopupData) -> Unit)? = null,
) {
    val textMeasurer: TextMeasurer = rememberTextMeasurer()
    val labelHelperProperties = LabelHelperProperties()
    val indicatorProperties = HorizontalIndicatorProperties(
        count = IndicatorCount.StepBased(
            horizontalIndicatorStep
        ), contentBuilder = {
            if (it >= 1000000.0) {
                return@HorizontalIndicatorProperties "$${(it / 1000000).format(2)}M"
            }
            "$" + (it / 1000).format(0) + "K"
        })
    val gridProperties = GridProperties(
        xAxisProperties = GridProperties.AxisProperties(
            style = StrokeStyle.Dashed(
                intervals = floatArrayOf(
                    30f,
                    30f
                ), phase = 25f
            )
        ),
        yAxisProperties = GridProperties.AxisProperties(enabled = false)
    )
    val labelProperties = LabelProperties(true)

    val density = LocalDensity.current

    val everyDataWidth = with(density) {
        data.maxOfOrNull { rowData ->
            rowData.values.map {
                it.properties.thickness.toPx() + it.properties.spacing.toPx()
            }.sum()
        } ?: 0f
    }

    val barWithRect = remember {
        mutableStateListOf<BarPopupData>()
    }

    val selectedValue = remember {
        mutableStateOf<SelectedBars?>(null)
    }

    val popupAnimation = remember {
        Animatable(0f)
    }

    val computedMaxValue =
        rememberComputedChartMaxValue(minValue, maxValue, indicatorProperties.count)
    val indicators = remember(minValue, computedMaxValue) {
        indicatorProperties.indicators.ifEmpty {
            split(
                count = indicatorProperties.count,
                minValue = minValue,
                maxValue = computedMaxValue
            )
        }
    }
    val indicatorAreaWidth = remember {
        if (indicatorProperties.enabled) {
            indicators.maxOf { textMeasurer.measure(indicatorProperties.contentBuilder(it)).size.width } + (indicatorProperties.padding.value * density.density)
        } else {
            0f
        }
    }

    val xPadding = remember {
        if (indicatorProperties.enabled && indicatorProperties.position == IndicatorPosition.Horizontal.Start) {
            indicatorAreaWidth
        } else {
            0f
        }
    }

    val chartWidth = remember {
        mutableFloatStateOf(0f)
    }

    /*
      Let's leave in case we need animation

      ImplementRCAnimation(
          data = data,
          animationMode = AnimationMode.Together(),
          spec = { tween(0) },
          delay = 0L,
          before = {
              barWithRect.clear()
          }
      )*/

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        Column(modifier = modifier) {
            if (labelHelperProperties.enabled) {
                RCChartLabelHelper(
                    data = data,
                    textStyle = labelHelperProperties.textStyle,
                    labelCountPerLine = labelHelperProperties.labelCountPerLine
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    Canvas(
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInput(Unit) {
                                detectHorizontalDragGestures { change, dragAmount ->
                                    barWithRect
                                        .lastOrNull { popupData ->
                                            change.position.x in popupData.rect.left..popupData.rect.right
                                        }
                                        ?.let { popupData ->
                                            val firstPopupDataIndex =
                                                barWithRect.indexOf(popupData)
                                            val secondPopupData =
                                                if (barWithRect.indexOf(popupData) % 2 == 0) {
                                                    barWithRect[firstPopupDataIndex + 1]
                                                } else {
                                                    barWithRect[firstPopupDataIndex - 1]
                                                }
                                            selectedValue.value = SelectedBars(
                                                bars = Pair(popupData.bar, secondPopupData.bar),
                                                rect = popupData.rect,
                                                offset = Offset(
                                                    popupData.rect.left,
                                                    if (popupData.bar.value < 0) popupData.rect.bottom else popupData.rect.top
                                                ),
                                                dataIndexes = Pair(
                                                    popupData.dataIndex,
                                                    secondPopupData.dataIndex
                                                ),
                                                valueIndexes = Pair(
                                                    popupData.valueIndex,
                                                    secondPopupData.valueIndex
                                                )
                                            )
                                        }
                                }
                            }
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onTap = {
                                        val position = Offset(it.x, it.y)
                                        barWithRect
                                            .lastOrNull { popupData ->
                                                popupData.rect.contains(position)
                                            }
                                            ?.let { popupData ->
                                                val firstPopupDataIndex =
                                                    barWithRect.indexOf(popupData)
                                                val secondPopupData =
                                                    if (barWithRect.indexOf(popupData) % 2 == 0) {
                                                        barWithRect[firstPopupDataIndex + 1]
                                                    } else {
                                                        barWithRect[firstPopupDataIndex - 1]
                                                    }
                                                selectedValue.value = SelectedBars(
                                                    bars = Pair(
                                                        popupData.bar,
                                                        secondPopupData.bar
                                                    ),
                                                    rect = popupData.rect,
                                                    offset = Offset(
                                                        popupData.rect.left,
                                                        if (popupData.bar.value < 0) popupData.rect.bottom else popupData.rect.top
                                                    ),
                                                    dataIndexes = Pair(
                                                        popupData.dataIndex,
                                                        secondPopupData.dataIndex
                                                    ),
                                                    valueIndexes = Pair(
                                                        popupData.valueIndex,
                                                        secondPopupData.valueIndex
                                                    )
                                                )
                                                onBarClick?.invoke(popupData)
                                            }
                                    }
                                )
                            }
                    ) {

                        val barsAreaWidth = size.width - (indicatorAreaWidth)
                        chartWidth.value = barsAreaWidth
                        val zeroY = size.height - calculateOffset(
                            maxValue = computedMaxValue,
                            minValue = minValue,
                            total = size.height,
                            value = 0.0f
                        ).toFloat()


                        if (indicatorProperties.enabled) {
                            indicators.forEachIndexed { index, indicator ->
                                val measureResult =
                                    textMeasurer.measure(
                                        indicatorProperties.contentBuilder(indicator),
                                        style = indicatorProperties.textStyle
                                    )
                                val x = when (indicatorProperties.position) {
                                    IndicatorPosition.Horizontal.Start -> 0f
                                    IndicatorPosition.Horizontal.End -> barsAreaWidth + indicatorProperties.padding.value * density.density
                                }
                                drawText(
                                    textLayoutResult = measureResult,
                                    topLeft = Offset(
                                        x = x,
                                        y = (size.height - measureResult.size.height).spaceBetween(
                                            itemCount = indicators.count(),
                                            index
                                        )
                                    )
                                )
                            }
                        }

                        drawGridLines(
                            xPadding = xPadding,
                            size = size.copy(width = barsAreaWidth),
                            dividersProperties = dividerProperties,
                            indicatorPosition = indicatorProperties.position,
                            xAxisProperties = gridProperties.xAxisProperties,
                            yAxisProperties = gridProperties.yAxisProperties,
                            gridEnabled = gridProperties.enabled
                        )

                        data.forEachIndexed { dataIndex, columnChart ->
                            columnChart.values.forEachIndexed { valueIndex, col ->
                                if (col.value != 0.0) {
                                    val stroke = when (data.size) {
                                        1 -> col.properties.thickness.toPx() * 3f

                                        3 -> col.properties.thickness.toPx() * 1.5f

                                        6 -> col.properties.thickness.toPx()

                                        else -> col.properties.thickness.toPx() / 1.5f
                                    }
                                    val spacing = col.properties.spacing.toPx()

                                    val barHeight =
                                        ((col.value * size.height) / (computedMaxValue - minValue)) //* col.animator.value(let's leave in case we need animation)
                                    val everyBarWidth = (stroke + spacing)

                                    val barX = if (data.size == 1) {
                                        (valueIndex * everyBarWidth) + (barsAreaWidth / 2) - (stroke / 2) + everyBarWidth / 2
                                    } else {
                                        if (barHeight < 0.0 && isNegativeValueReversed) {
                                            (valueIndex * everyBarWidth) + (barsAreaWidth - everyDataWidth).spaceBetween(
                                                itemCount = data.count(),
                                                index = dataIndex
                                            ) + xPadding
                                        } else {
                                            (valueIndex * everyBarWidth) + (barsAreaWidth - everyDataWidth).spaceBetween(
                                                itemCount = data.count(),
                                                index = dataIndex
                                            ) + xPadding
                                        }
                                    }

                                    val rect = Rect(
                                        offset = Offset(
                                            x = barX,
                                            y = (zeroY - barHeight.toFloat().coerceAtLeast(0f))
                                        ),
                                        size = Size(
                                            width = stroke,
                                            height = barHeight.absoluteValue.toFloat()
                                        ),
                                    )
                                    if (barWithRect.none { it.rect == rect }) {
                                        barWithRect.add(
                                            BarPopupData(
                                                col,
                                                rect,
                                                dataIndex,
                                                valueIndex
                                            )
                                        )
                                    }
                                    val path = Path()

                                    val radius: Bars.Data.Radius = Bars.Data.Radius.Rectangle(
                                        topLeft = radius,
                                        topRight = radius,
                                        bottomLeft = radius,
                                        bottomRight = radius
                                    )

                                    path.addRoundRect(rect = rect, radius = radius.asRadiusPx(this))
                                    val color = selectedValue.value?.let { selectedValue ->
                                        if (selectedValue.dataIndexes.first == dataIndex
                                            || selectedValue.dataIndexes.second == dataIndex
                                        ) {
                                            if (valueIndex % 2 == 0) {
                                                enabledColors.first
                                            } else {
                                                enabledColors.second
                                            }
                                        } else {
                                            disabledColors.let {
                                                if (valueIndex % 2 == 0) {
                                                    it.first
                                                } else {
                                                    it.second
                                                }
                                            }
                                        }
                                    } ?: run {
                                        if (valueIndex % 2 == 0) {
                                            enabledColors.first
                                        } else {
                                            enabledColors.second
                                        }
                                    }
                                    drawPath(
                                        path = path,
                                        brush = SolidColor(color),
                                        style = col.properties.style.getStyle(density.density)
                                    )
                                }
                            }
                        }
                    }
                    selectedValue.value?.let { selectedValue ->
                        ShowLegend(
                            selectedValue = selectedValue,
                            selectedColors = Pair(
                                enabledColors.first,
                                enabledColors.second
                            ),
                            legendLabels = legendLabels
                        )
                    }
                    LaunchedEffect(selectedValue.value) {
                        delay(3000L)
                        selectedValue.value = null
                    }
                }
            }
            HorizontalLabels(
                labelProperties = labelProperties,
                labels = labelProperties.labels.ifEmpty {
                    data
                        .map { it.label }
                },
                indicatorProperties = indicatorProperties,
                chartWidth = chartWidth.floatValue,
                density = density,
                textMeasurer = textMeasurer,
                xPadding = xPadding
            )
        }
    }
}

@Composable
private fun ShowLegend(
    selectedValue: SelectedBars,
    selectedColors: Pair<Color, Color>,
    legendLabels: Triple<String, String, String>,
) {
    val screenSizeWidth = LocalWindowInfo.current.containerSize.width
    val offset = with(LocalDensity.current) {
        var offsetX = (selectedValue.offset.x.toDp() - 90.dp)
        if (offsetX < 0.dp) {
            offsetX = 0.dp
        } else if ((offsetX + 250.dp) >= screenSizeWidth.dp) {
            offsetX = screenSizeWidth.dp - 250.dp
        }
        Pair(
            offsetX.roundToPx(),
            ((selectedValue.offset.y.toDp() - 100.dp)).roundToPx()
        )
    }
    Popup(
        offset = IntOffset(offset.first, offset.second),
    ) {
        Column(
            modifier = Modifier
                .width(180.dp)
                .wrapContentHeight()
                .shadow(8.dp)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(color = selectedColors.first)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentHeight(),
                    text = legendLabels.first,
                    style = FontStyles.BodyExtraSmall
                )
                Text(
                    modifier = Modifier.wrapContentSize(),
                    text = selectedValue.bars.first.value.formatCurrency(),
                    style = FontStyles.BodyExtraSmall
                )
            }
            Spacer(
                modifier = Modifier.height(12.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(color = selectedColors.second)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentHeight(),
                    text = legendLabels.second,
                    style = FontStyles.BodyExtraSmall
                )
                Text(
                    modifier = Modifier.wrapContentSize(),
                    text = selectedValue.bars.second.value.formatCurrency(),
                    style = FontStyles.BodyExtraSmall
                )
            }
            Spacer(
                modifier = Modifier.height(12.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val barsDiff =
                    (selectedValue.bars.first.value - abs(selectedValue.bars.second.value))
                Box(
                    modifier = Modifier
                        .width(12.dp)
                        .height(2.dp)
                        .background(color = Colors.TransactionIncome)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentHeight(),
                    text = legendLabels.third,
                    style = FontStyles.BodyExtraSmall
                )
                Text(
                    modifier = Modifier.wrapContentSize(),
                    text = barsDiff.formatCurrency(),
                    style = FontStyles.BodyExtraSmall
                )
            }
        }
    }
}
