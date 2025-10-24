package com.tangerine.charts.compose_charts

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
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Density
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
    val derived = rememberNetWorthChartDerived(
        data = data,
        horizontalIndicatorStep = horizontalIndicatorStep,
        maxValue = maxValue,
        minValue = minValue
    )

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        Column(modifier = modifier) {

            ChartHeaderIfNeeded(
                enabled = derived.labelHelper.enabled,
                data = data,
                textStyle = derived.labelHelper.textStyle,
                countPerLine = derived.labelHelper.labelCountPerLine
            )

            ChartArea(
                derived = derived,
                data = data,
                radius = radius,
                dividerProperties = dividerProperties,
                enabledColors = enabledColors,
                disabledColors = disabledColors,
                isNegativeValueReversed = isNegativeValueReversed,
                legendLabels = legendLabels,
                onBarClick = onBarClick
            )

            BottomHorizontalLabels(
                labels = derived.bottomLabels(data),
                indicatorProps = derived.indicatorProps,
                chartWidth = derived.chartWidth.floatValue,
                density = derived.density,
                textMeasurer = derived.textMeasurer,
                xPadding = derived.xPadding
            )
        }
    }
}

@Stable
private class Derived(
    val density: Density,
    val textMeasurer: TextMeasurer,
    val indicatorProps: HorizontalIndicatorProperties,
    val indicators: List<Double>,
    val indicatorAreaWidth: Float,
    val xPadding: Float,
    val labelHelper: LabelHelperProperties,
    val gridProps: GridProperties,
    val chartWidth: MutableFloatState,
    val barRects: SnapshotStateList<BarPopupData>,
    val selected: MutableState<SelectedBars?>,
    val computedMaxValue: Double
) {
    fun bottomLabels(data: List<Bars>): List<String> =
        LabelProperties(true).labels.ifEmpty { data.map { it.label } }
}

@Composable
private fun rememberNetWorthChartDerived(
    data: List<Bars>,
    horizontalIndicatorStep: Double,
    maxValue: Double,
    minValue: Double
): Derived {
    val density = LocalDensity.current
    val textMeasurer = rememberTextMeasurer()
    val labelHelper = remember { LabelHelperProperties() }

    val indicatorProps = remember(horizontalIndicatorStep) {
        HorizontalIndicatorProperties(
            count = IndicatorCount.StepBased(horizontalIndicatorStep),
            contentBuilder = {
                if (it >= 1_000_000.0) "$${(it / 1_000_000).format(2)}M"
                else "$" + (it / 1_000).format(0) + "K"
            }
        )
    }
    val gridProps = remember {
        GridProperties(
            xAxisProperties = GridProperties.AxisProperties(
                style = StrokeStyle.Dashed(floatArrayOf(30f, 30f), phase = 25f)
            ),
            yAxisProperties = GridProperties.AxisProperties(enabled = false)
        )
    }

    val computedMax = rememberComputedChartMaxValue(minValue, maxValue, indicatorProps.count)
    val indicators = remember(minValue, computedMax) {
        indicatorProps.indicators.ifEmpty {
            split(indicatorProps.count, minValue, computedMax)
        }
    }

    val indicatorAreaWidth = remember(indicators, indicatorProps.enabled) {
        if (!indicatorProps.enabled) 0f
        else indicators.maxOf {
            textMeasurer.measure(indicatorProps.contentBuilder(it)).size.width
        } + (indicatorProps.padding.value * density.density)
    }

    val xPadding = remember(indicatorProps.enabled, indicatorProps.position, indicatorAreaWidth) {
        if (indicatorProps.enabled && indicatorProps.position == IndicatorPosition.Horizontal.Start)
            indicatorAreaWidth else 0f
    }

    return remember {
        Derived(
            density = density,
            textMeasurer = textMeasurer,
            indicatorProps = indicatorProps,
            indicators = indicators,
            indicatorAreaWidth = indicatorAreaWidth,
            xPadding = xPadding,
            labelHelper = labelHelper,
            gridProps = gridProps,
            chartWidth = mutableFloatStateOf(0f),
            barRects = mutableStateListOf(),
            selected = mutableStateOf(null),
            computedMaxValue = computedMax
        )
    }
}

@Composable
private fun ChartArea(
    derived: Derived,
    data: List<Bars>,
    radius: Dp,
    dividerProperties: DividerProperties,
    enabledColors: Pair<Color, Color>,
    disabledColors: Pair<Color, Color>,
    isNegativeValueReversed: Boolean,
    legendLabels: Triple<String, String, String>,
    onBarClick: ((BarPopupData) -> Unit)?
) {
    Row(Modifier.fillMaxSize()) {
        Box(Modifier
            .fillMaxWidth()
            .wrapContentHeight()) {

            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .chartGestures(derived, onBarClick)
            ) {
                val barsAreaWidth = size.width - derived.indicatorAreaWidth
                derived.chartWidth.floatValue = barsAreaWidth
                val zeroY = size.height - calculateOffset(
                    maxValue = derived.computedMaxValue,
                    minValue = 0.0, // мы рисуем от 0 до max (min < 0 учтён в offset)
                    total = size.height,
                    value = 0.0f
                ).toFloat()

                drawIndicatorsIfNeeded(
                    derived = derived,
                    barsAreaWidth = barsAreaWidth
                )

                drawGridLines(
                    xPadding = derived.xPadding,
                    size = size.copy(width = barsAreaWidth),
                    dividersProperties = dividerProperties,
                    indicatorPosition = derived.indicatorProps.position,
                    xAxisProperties = derived.gridProps.xAxisProperties,
                    yAxisProperties = derived.gridProps.yAxisProperties,
                    gridEnabled = derived.gridProps.enabled
                )

                drawBars(
                    data = data,
                    size = size,
                    zeroY = zeroY,
                    everyDataWidth = with(derived.density) {
                        data.maxOfOrNull { row ->
                            row.values.sumOf { (it.properties.thickness.toPx() + it.properties.spacing.toPx()).toDouble() }
                        }?.toFloat() ?: 0f
                    },
                    xPadding = derived.xPadding,
                    radius = radius,
                    isNegativeValueReversed = isNegativeValueReversed,
                    enabledColors = enabledColors,
                    disabledColors = disabledColors,
                    selectedValue = derived.selected,
                    barRects = derived.barRects,
                    density = derived.density,
                    computedMax = derived.computedMaxValue,
                    minValue = 0.0, // для высоты столбца
                    barsAreaWidth = barsAreaWidth
                )
            }

            derived.selected.value?.let { sel ->
                ShowLegend(
                    selectedValue = sel,
                    selectedColors = enabledColors,
                    legendLabels = legendLabels
                )
            }

            LaunchedEffect(derived.selected.value) {
                delay(3000L)
                derived.selected.value = null
            }
        }
    }
}


private fun Modifier.chartGestures(
    derived: Derived,
    onBarClick: ((BarPopupData) -> Unit)?
): Modifier = this
    .pointerInput(derived.barRects) {
        detectHorizontalDragGestures { change, _ ->
            val hit =
                derived.barRects.lastOrNull { change.position.x in it.rect.left..it.rect.right }
            hit?.let { setSelectionFromHit(it, derived) }
        }
    }
    .pointerInput(derived.barRects) {
        detectTapGestures { offset ->
            val hit = derived.barRects.lastOrNull { it.rect.contains(Offset(offset.x, offset.y)) }
            hit?.let {
                setSelectionFromHit(it, derived)
                onBarClick?.invoke(it)
            }
        }
    }

private fun setSelectionFromHit(hit: BarPopupData, derived: Derived) {
    val i = derived.barRects.indexOf(hit)
    val pair = if (i % 2 == 0) derived.barRects[i + 1] else derived.barRects[i - 1]
    derived.selected.value = SelectedBars(
        bars = Pair(hit.bar, pair.bar),
        rect = hit.rect,
        offset = Offset(hit.rect.left, if (hit.bar.value < 0) hit.rect.bottom else hit.rect.top),
        dataIndexes = Pair(hit.dataIndex, pair.dataIndex),
        valueIndexes = Pair(hit.valueIndex, pair.valueIndex)
    )
}

private fun DrawScope.drawIndicatorsIfNeeded(
    derived: Derived,
    barsAreaWidth: Float
) {
    if (!derived.indicatorProps.enabled) return
    derived.indicators.forEachIndexed { index, value ->
        val layout = derived.textMeasurer.measure(
            derived.indicatorProps.contentBuilder(value),
            style = derived.indicatorProps.textStyle
        )
        val x = when (derived.indicatorProps.position) {
            IndicatorPosition.Horizontal.Start -> 0f
            IndicatorPosition.Horizontal.End -> barsAreaWidth + derived.indicatorProps.padding.value * derived.density.density
        }
        drawText(
            textLayoutResult = layout,
            topLeft = Offset(
                x = x,
                y = (size.height - layout.size.height).spaceBetween(
                    itemCount = derived.indicators.size, index = index
                )
            )
        )
    }
}

@Suppress("LongParameterList")
private fun DrawScope.drawBars(
    data: List<Bars>,
    size: Size,
    zeroY: Float,
    everyDataWidth: Float,
    xPadding: Float,
    radius: Dp,
    isNegativeValueReversed: Boolean,
    enabledColors: Pair<Color, Color>,
    disabledColors: Pair<Color, Color>,
    selectedValue: MutableState<SelectedBars?>,
    barRects: SnapshotStateList<BarPopupData>,
    density: Density,
    computedMax: Double,
    minValue: Double,
    barsAreaWidth: Float
) {
    data.forEachIndexed { dataIndex, row ->
        row.values.forEachIndexed { valueIndex, col ->
            if (col.value == 0.0) return@forEachIndexed  // guard — уменьшает вложенность

            val stroke = when (data.size) {
                1 -> col.properties.thickness.toPx() * 3f
                3 -> col.properties.thickness.toPx() * 1.5f
                6 -> col.properties.thickness.toPx()
                else -> col.properties.thickness.toPx() / 1.5f
            }
            val spacing = col.properties.spacing.toPx()
            val everyBarWidth = stroke + spacing

            val barHeight = ((col.value * size.height) / (computedMax - minValue))
            val barX = if (data.size == 1) {
                (valueIndex * everyBarWidth) + (barsAreaWidth / 2) - (stroke / 2) + everyBarWidth / 2
            } else {
                // Ваша ветка if/else давала одинаковый результат — убираем дубликат
                (valueIndex * everyBarWidth) +
                        (barsAreaWidth - everyDataWidth).spaceBetween(
                            itemCount = data.size,
                            index = dataIndex
                        ) +
                        xPadding
            }

            val rect = Rect(
                offset = Offset(x = barX, y = (zeroY - barHeight.toFloat().coerceAtLeast(0f))),
                size = Size(width = stroke, height = barHeight.absoluteValue.toFloat())
            )

            if (barRects.none { it.rect == rect }) {
                barRects.add(BarPopupData(col, rect, dataIndex, valueIndex))
            }

                with(density) { radius.toPx() }
            val barRadiusSpec: Bars.Data.Radius = Bars.Data.Radius.Circular(radius)

            val path = Path().apply {
                addRoundRect(
                    rect = rect,
                    radius = barRadiusSpec.asRadiusPx(this@drawBars)
                )
            }

            val color = pickBarColor(
                selectedValue = selectedValue.value,
                dataIndex = dataIndex,
                valueIndex = valueIndex,
                enabledColors = enabledColors,
                disabledColors = disabledColors
            )

            drawPath(
                path = path,
                brush = SolidColor(color),
                style = col.properties.style.getStyle(density.density)
            )
        }
    }
}

private fun pickBarColor(
    selectedValue: SelectedBars?,
    dataIndex: Int,
    valueIndex: Int,
    enabledColors: Pair<Color, Color>,
    disabledColors: Pair<Color, Color>
): Color {
    val pair = if (valueIndex % 2 == 0) 0 else 1
    val (a, b) = enabledColors
    val (da, db) = disabledColors

    return when {
        selectedValue == null -> if (pair == 0) a else b
        selectedValue.dataIndexes.first == dataIndex ||
                selectedValue.dataIndexes.second == dataIndex ->
            if (pair == 0) a else b

        else -> if (pair == 0) da else db
    }
}

@Composable
private fun ChartHeaderIfNeeded(
    enabled: Boolean,
    data: List<Bars>,
    textStyle: TextStyle,
    countPerLine: Int
) {
    if (!enabled) return
    RCChartLabelHelper(
        data = data,
        textStyle = textStyle,
        labelCountPerLine = countPerLine
    )
    Spacer(Modifier.height(24.dp))
}

@Composable
private fun BottomHorizontalLabels(
    labels: List<String>,
    indicatorProps: HorizontalIndicatorProperties,
    chartWidth: Float,
    density: Density,
    textMeasurer: TextMeasurer,
    xPadding: Float
) {
    HorizontalLabels(
        labelProperties = LabelProperties(true),
        labels = labels,
        indicatorProperties = indicatorProps,
        chartWidth = chartWidth,
        density = density,
        textMeasurer = textMeasurer,
        xPadding = xPadding
    )
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
                .width(240.dp)
                .wrapContentHeight()
                .shadow(8.dp)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(color = selectedColors.first)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentHeight(),
                    text = legendLabels.first,
                    style = FontStyles.BodySmall
                )
                Text(
                    modifier = Modifier.wrapContentSize(),
                    text = selectedValue.bars.first.value.formatCurrency(),
                    style = FontStyles.BodySmall
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
                        .size(16.dp)
                        .background(color = selectedColors.second)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentHeight(),
                    text = legendLabels.second,
                    style = FontStyles.BodySmall
                )
                Text(
                    modifier = Modifier.wrapContentSize(),
                    text = selectedValue.bars.second.value.formatCurrency(),
                    style = FontStyles.BodySmall
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
                        .width(16.dp)
                        .height(2.dp)
                        .background(color = Colors.TransactionIncome)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentHeight(),
                    text = legendLabels.third,
                    style = FontStyles.BodySmall
                )
                Text(
                    modifier = Modifier.wrapContentSize(),
                    text = barsDiff.formatCurrency(),
                    style = FontStyles.BodySmall
                )
            }
        }
    }
}
