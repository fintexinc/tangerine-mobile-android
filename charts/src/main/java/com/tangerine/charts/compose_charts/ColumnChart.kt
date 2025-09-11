package com.tangerine.charts.compose_charts

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.fintexinc.core.ui.font.FontStyles
import com.tangerine.charts.compose_charts.components.RCChartLabelHelper
import com.tangerine.charts.compose_charts.extensions.addRoundRect
import com.tangerine.charts.compose_charts.extensions.drawGridLines
import com.tangerine.charts.compose_charts.extensions.spaceBetween
import com.tangerine.charts.compose_charts.extensions.split
import com.tangerine.charts.compose_charts.models.AnimationMode
import com.tangerine.charts.compose_charts.models.BarPopupData
import com.tangerine.charts.compose_charts.models.BarProperties
import com.tangerine.charts.compose_charts.models.Bars
import com.tangerine.charts.compose_charts.models.DividerProperties
import com.tangerine.charts.compose_charts.models.GridProperties
import com.tangerine.charts.compose_charts.models.HorizontalIndicatorProperties
import com.tangerine.charts.compose_charts.models.IndicatorPosition
import com.tangerine.charts.compose_charts.models.LabelHelperProperties
import com.tangerine.charts.compose_charts.models.LabelProperties
import com.tangerine.charts.compose_charts.models.PopupProperties
import com.tangerine.charts.compose_charts.models.SelectedBars
import com.tangerine.charts.compose_charts.models.asRadiusPx
import com.tangerine.charts.compose_charts.utils.HorizontalLabels
import com.tangerine.charts.compose_charts.utils.ImplementRCAnimation
import com.tangerine.charts.compose_charts.utils.calculateOffset
import com.tangerine.charts.compose_charts.utils.checkRCMaxValue
import com.tangerine.charts.compose_charts.utils.checkRCMinValue
import com.tangerine.charts.compose_charts.utils.rememberComputedChartMaxValue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@Composable
fun ColumnChart(
    modifier: Modifier = Modifier,
    data: List<Bars>,
    barProperties: BarProperties = BarProperties(),
    onBarClick: ((BarPopupData) -> Unit)? = null,
    onBarLongClick: ((BarPopupData) -> Unit)? = null,
    labelProperties: LabelProperties = LabelProperties(
        textStyle = TextStyle.Default,
        enabled = true
    ),
    indicatorProperties: HorizontalIndicatorProperties = HorizontalIndicatorProperties(
        textStyle = TextStyle.Default
    ),
    dividerProperties: DividerProperties = DividerProperties(),
    gridProperties: GridProperties = GridProperties(),
    labelHelperProperties: LabelHelperProperties = LabelHelperProperties(),
    animationMode: AnimationMode = AnimationMode.Together { it * 200L },
    animationSpec: AnimationSpec<Float> = tween(500),
    animationDelay: Long = 100,
    textMeasurer: TextMeasurer = rememberTextMeasurer(),
    popupProperties: PopupProperties = PopupProperties(
        textStyle = TextStyle.Default.copy(
            color = Color.White,
            fontSize = 12.sp
        )
    ),
    barAlphaDecreaseOnPopup: Float = .4f,
    maxValue: Double = data.maxOfOrNull { it.values.maxOfOrNull { it.value } ?: 0.0 } ?: 0.0,
    minValue: Double = if (data.any { it.values.any { it.value < 0 } }) -maxValue else 0.0,
) {
    checkRCMinValue(minValue, data)
    checkRCMaxValue(maxValue, data)

    val density = LocalDensity.current

    val everyDataWidth = with(density) {
        data.maxOfOrNull { rowData ->
            rowData.values.map {
                (it.properties?.thickness
                    ?: barProperties.thickness).toPx() + (it.properties?.spacing
                    ?: barProperties.spacing).toPx()
            }.sum()
        } ?: 0f
    }
    val averageSpacingBetweenBars = with(density) {
        data.map { it.values }.flatten()
            .map { (it.properties?.spacing ?: barProperties.spacing).toPx() }.average()
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

    LaunchedEffect(selectedValue.value) {
        if (selectedValue.value != null) {
            delay(popupProperties.duration)
            popupAnimation.animateTo(0f, animationSpec = popupProperties.animationSpec)
            selectedValue.value = null
        }
    }

    ImplementRCAnimation(
        data = data,
        animationMode = animationMode,
        spec = { it.animationSpec ?: animationSpec },
        delay = animationDelay,
        before = {
            barWithRect.clear()
        }
    )
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
                val scope = rememberCoroutineScope()
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    Canvas(
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInput(Unit) {
                                if (popupProperties.enabled) {
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
                                                scope.launch {
                                                    if (popupAnimation.value != 1f && !popupAnimation.isRunning) {
                                                        popupAnimation.animateTo(
                                                            1f,
                                                            animationSpec = popupProperties.animationSpec
                                                        )
                                                    }
                                                }
                                            }
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
                                                if (popupProperties.enabled) {
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
                                                    scope.launch {
                                                        popupAnimation.snapTo(0f)
                                                        popupAnimation.animateTo(
                                                            1f,
                                                            animationSpec = popupProperties.animationSpec
                                                        )
                                                    }
                                                }
                                                onBarClick?.invoke(popupData)
                                            }
                                    },
                                    onLongPress = {
                                        val position = Offset(it.x, it.y)
                                        barWithRect
                                            .lastOrNull { popupData ->
                                                popupData.rect.contains(position)
                                            }
                                            ?.let { popupData ->
                                                onBarLongClick?.invoke(popupData)
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
                                    val stroke =
                                        (col.properties?.thickness
                                            ?: barProperties.thickness).toPx()
                                    val spacing =
                                        (col.properties?.spacing ?: barProperties.spacing).toPx()

                                    val barHeight =
                                        ((col.value * size.height) / (computedMaxValue - minValue)) * col.animator.value
                                    val everyBarWidth = (stroke + spacing)

                                    val barX =
                                        (valueIndex * everyBarWidth) + (barsAreaWidth - everyDataWidth).spaceBetween(
                                            itemCount = data.count(),
                                            index = dataIndex
                                        ) + xPadding + (averageSpacingBetweenBars / 2).toFloat()
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

                                    var radius: Bars.Data.Radius = Bars.Data.Radius.Rectangle(
                                        topLeft = 16.dp,
                                        topRight = 16.dp,
                                        bottomLeft = 16.dp,
                                        bottomRight = 16.dp
                                    )
                                    if (col.value < 0) {
                                        radius = radius.reverse()
                                    }

                                    path.addRoundRect(rect = rect, radius = radius.asRadiusPx(this))
                                    val alpha = if (rect == selectedValue.value?.rect) {
                                        1f - (barAlphaDecreaseOnPopup * popupAnimation.value)
                                    } else {
                                        1f
                                    }
                                    drawPath(
                                        path = path,
                                        brush = col.color ?: SolidColor(Color.Black),
                                        alpha = alpha,
                                        style = (col.properties?.style
                                            ?: barProperties.style).getStyle(density.density)
                                    )
                                }
                            }
                        }
                    }
                    selectedValue.value?.let { selectedValue ->
                        /*drawPopup(
                            selectedBars = selectedValue,
                            properties = popupProperties,
                            textMeasurer = textMeasurer,
                            progress = popupAnimation.value
                        )*/
                        val screenSizeWidth = LocalConfiguration.current.screenWidthDp
                        val screenSizeHeight = LocalConfiguration.current.screenHeightDp
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
                                            .background(color = Color.Green)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        modifier = Modifier
                                            .weight(1f)
                                            .wrapContentHeight(),
                                        text = "Liabilities",
                                        style = FontStyles.BodyExtraSmall
                                    )
                                    Text(
                                        modifier = Modifier.wrapContentSize(),
                                        text = "$250,000.00",
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
                                            .background(color = Color.Red)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        modifier = Modifier
                                            .weight(1f)
                                            .wrapContentHeight(),
                                        text = "Assets",
                                        style = FontStyles.BodyExtraSmall
                                    )
                                    Text(
                                        modifier = Modifier.wrapContentSize(),
                                        text = "$1000,000.00",
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
                                    Box(
                                        modifier = Modifier
                                            .width(12.dp)
                                            .height(2.dp)
                                            .background(color = Color.Green)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        modifier = Modifier
                                            .weight(1f)
                                            .wrapContentHeight(),
                                        text = "Net Worth",
                                        style = FontStyles.BodyExtraSmall
                                    )
                                    Text(
                                        modifier = Modifier.wrapContentSize(),
                                        text = "$1000,000.00",
                                        style = FontStyles.BodyExtraSmall
                                    )
                                }
                            }
                        }
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
                chartWidth = chartWidth.value,
                density = density,
                textMeasurer = textMeasurer,
                xPadding = xPadding
            )
        }
    }
}
