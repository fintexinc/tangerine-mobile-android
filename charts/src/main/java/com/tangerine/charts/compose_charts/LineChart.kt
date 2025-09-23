package com.tangerine.charts.compose_charts

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.tangerine.charts.compose_charts.components.LabelHelper
import com.tangerine.charts.compose_charts.extensions.drawGridLines
import com.tangerine.charts.compose_charts.extensions.line_chart.PathData
import com.tangerine.charts.compose_charts.extensions.line_chart.drawLineGradient
import com.tangerine.charts.compose_charts.extensions.line_chart.getLinePath
import com.tangerine.charts.compose_charts.extensions.line_chart.getPopupValue
import com.tangerine.charts.compose_charts.extensions.split
import com.tangerine.charts.compose_charts.models.AnimationMode
import com.tangerine.charts.compose_charts.models.DividerProperties
import com.tangerine.charts.compose_charts.models.DotProperties
import com.tangerine.charts.compose_charts.models.DrawStyle
import com.tangerine.charts.compose_charts.models.GridProperties
import com.tangerine.charts.compose_charts.models.HorizontalIndicatorProperties
import com.tangerine.charts.compose_charts.models.IndicatorPosition
import com.tangerine.charts.compose_charts.models.LabelHelperProperties
import com.tangerine.charts.compose_charts.models.LabelProperties
import com.tangerine.charts.compose_charts.models.Line
import com.tangerine.charts.compose_charts.models.PopupProperties
import com.tangerine.charts.compose_charts.models.ZeroLineProperties
import com.tangerine.charts.compose_charts.utils.HorizontalLabels
import com.tangerine.charts.compose_charts.utils.calculateOffset
import com.tangerine.charts.compose_charts.utils.rememberComputedChartMaxValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class Popup(
    val properties: PopupProperties,
    val position: Offset,
    val value: Double,
    val dataIndex: Int,
    val valueIndex: Int
)

@Composable
fun LineChart(
    modifier: Modifier = Modifier,
    data: List<Line>,
    curvedEdges: Boolean = true,
    animationDelay: Long = 300,
    animationMode: AnimationMode = AnimationMode.Together(),
    dividerProperties: DividerProperties = DividerProperties(),
    gridProperties: GridProperties = GridProperties(),
    zeroLineProperties: ZeroLineProperties = ZeroLineProperties(),
    indicatorProperties: HorizontalIndicatorProperties = HorizontalIndicatorProperties(
        textStyle = TextStyle.Default,
        padding = 16.dp
    ),
    labelHelperProperties: LabelHelperProperties = LabelHelperProperties(),
    labelHelperPadding: Dp = 26.dp,
    textMeasurer: TextMeasurer = rememberTextMeasurer(),
    popupProperties: PopupProperties = PopupProperties(
        textStyle = TextStyle.Default.copy(
            color = Color.White,
            fontSize = 12.sp
        )
    ),
    dotsProperties: DotProperties = DotProperties(),
    labelProperties: LabelProperties = LabelProperties(enabled = false),
    maxValue: Double = data.maxOfOrNull { it.values.maxOfOrNull { it } ?: 0.0 } ?: 0.0,
    minValue: Double = if (data.any { it.values.any { it < 0.0 } }) data.minOfOrNull {
        it.values.minOfOrNull { it } ?: 0.0
    } ?: 0.0 else 0.0,
    onValueSelected: ((Double?) -> Unit)? = null,
) {
    if (data.isNotEmpty()) {
        require(minValue <= (data.minOfOrNull { it.values.minOfOrNull { it } ?: 0.0 } ?: 0.0)) {
            "Chart data must be at least $minValue (Specified Min Value)"
        }
        require(maxValue >= (data.maxOfOrNull { it.values.maxOfOrNull { it } ?: 0.0 } ?: 0.0)) {
            "Chart data must be at most $maxValue (Specified Max Value)"
        }
    }

    val density = LocalDensity.current
    val scope = rememberCoroutineScope()

    val pathMeasure = remember {
        PathMeasure()
    }

    val popupAnimation = remember {
        Animatable(0f)
    }

    val zeroLineAnimation = remember {
        Animatable(0f)
    }
    val chartWidth = remember {
        mutableFloatStateOf(0f)
    }

    val persistentIndicatorX = remember { mutableFloatStateOf(-1f) }
    val persistentIndicatorValue = remember { mutableStateOf<Double?>(null) }
    val persistentIndicatorPosition = remember { mutableStateOf<Offset?>(null) }

    val dotAnimators = remember {
        mutableStateListOf<List<Animatable<Float, AnimationVector1D>>>()
    }
    val popups = remember {
        mutableStateListOf<Popup>()
    }
    val popupsOffsetAnimators = remember {
        mutableStateListOf<Pair<Animatable<Float, AnimationVector1D>, Animatable<Float, AnimationVector1D>>>()
    }
    val linesPathData = remember {
        mutableStateListOf<PathData>()
    }

    val computedMaxValue = rememberComputedChartMaxValue(minValue, maxValue, indicatorProperties.count)
    val indicators = remember(indicatorProperties.indicators, minValue, maxValue) {
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
    LaunchedEffect(Unit) {
        if (zeroLineProperties.enabled) {
            zeroLineAnimation.snapTo(0f)
            zeroLineAnimation.animateTo(1f, animationSpec = zeroLineProperties.animationSpec)
        }
    }

    // make animators
    LaunchedEffect(data) {
        dotAnimators.clear()
        launch {
            data.forEach {
                val animators = mutableListOf<Animatable<Float, AnimationVector1D>>()
                repeat(it.values.size) {
                    animators.add(Animatable(0f))
                }
                dotAnimators.add(animators)
            }
        }
    }

    // animate
    LaunchedEffect(data) {
        delay(animationDelay)

        val animateStroke: suspend (Line) -> Unit = { line ->
            line.strokeProgress.animateTo(1f, animationSpec = line.strokeAnimationSpec)
        }
        val animateGradient: suspend (Line) -> Unit = { line ->
            delay(line.gradientAnimationDelay)
            line.gradientProgress.animateTo(1f, animationSpec = line.gradientAnimationSpec)
        }
        launch {
            data.forEachIndexed { index, line ->
                when (animationMode) {
                    is AnimationMode.OneByOne -> {
                        animateStroke(line)
                    }

                    is AnimationMode.Together -> {
                        launch {
                            delay(animationMode.delayBuilder(index))
                            animateStroke(line)
                        }
                    }
                }
            }
        }
        launch {
            data.forEachIndexed { index, line ->
                when (animationMode) {
                    is AnimationMode.OneByOne -> {
                        animateGradient(line)
                    }

                    is AnimationMode.Together -> {
                        launch {
                            delay(animationMode.delayBuilder(index))
                            animateGradient(line)
                        }
                    }
                }
            }
        }
    }
    LaunchedEffect(data, minValue, computedMaxValue) {
        linesPathData.clear()
    }

    suspend fun hidePopup() {
        popupAnimation.animateTo(0f, animationSpec = tween(300))
        popups.clear()
        popupsOffsetAnimators.clear()
    }

    fun PointerInputScope.showPopup(
        data: List<Line>,
        size: IntSize,
        position: Offset
    ) {
        popups.clear()

        data.forEachIndexed { dataIndex, line ->
            val properties = line.popupProperties ?: popupProperties
            if (!properties.enabled) return@forEachIndexed

            val positionX = position.x.coerceIn(0f, size.width.toFloat())
            val pathData = linesPathData[dataIndex]

            if (positionX >= pathData.xPositions[pathData.startIndex] &&
                positionX <= pathData.xPositions[pathData.endIndex]
            ) {
                val showOnPointsThreshold =
                    ((properties.mode as? PopupProperties.Mode.PointMode)?.threshold
                        ?: 0.dp).toPx()
                val pointX = pathData.xPositions.find {
                    it in positionX - showOnPointsThreshold..positionX + showOnPointsThreshold
                }

                if (properties.mode !is PopupProperties.Mode.PointMode || pointX != null) {
                    val relevantX =
                        if (properties.mode is PopupProperties.Mode.PointMode) (pointX?.toFloat()
                            ?: 0f) else positionX
                    val fraction = (relevantX / size.width)

                    val valueIndex = calculateValueIndexs(
                        fraction = fraction.toDouble(),
                        values = line.values,
                        pathData = pathData
                    )

                    val popupValue = getPopupValue(
                        points = line.values,
                        fraction = fraction.toDouble(),
                        rounded = line.curvedEdges ?: curvedEdges,
                        size = size.toSize(),
                        minValue = minValue,
                        maxValue = computedMaxValue
                    )

                    popups.add(
                        Popup(
                            position = popupValue.offset,
                            value = popupValue.calculatedValue,
                            properties = properties,
                            dataIndex = dataIndex,
                            valueIndex = valueIndex
                        )
                    )

                    if (popupsOffsetAnimators.count() < popups.count()) {
                        repeat(popups.count() - popupsOffsetAnimators.count()) {
                            popupsOffsetAnimators.add(
                                if (properties.mode is PopupProperties.Mode.PointMode) {
                                    Animatable(popupValue.offset.x) to Animatable(popupValue.offset.y)
                                } else {
                                    Animatable(0f) to Animatable(0f)
                                }
                            )
                        }
                    }
                }
            }
        }

        scope.launch {
            if (popupAnimation.value != 1f && !popupAnimation.isRunning) {
                popupAnimation.animateTo(1f, animationSpec = popupProperties.animationSpec)
            }
        }
    }

    fun showPersistentIndicator(
        data: List<Line>,
        size: IntSize,
        position: Offset
    ) {
        val positionX = position.x.coerceIn(0f, size.width.toFloat())

        val firstLine = data.firstOrNull() ?: return
        val pathData = linesPathData.firstOrNull() ?: return

        if (positionX >= pathData.xPositions[pathData.startIndex] &&
            positionX <= pathData.xPositions[pathData.endIndex]
        ) {
            val fraction = (positionX / size.width)

            val popupValue = getPopupValue(
                points = firstLine.values,
                fraction = fraction.toDouble(),
                rounded = firstLine.curvedEdges ?: curvedEdges,
                size = size.toSize(),
                minValue = minValue,
                maxValue = computedMaxValue
            )

            persistentIndicatorX.floatValue = positionX
            persistentIndicatorValue.value = popupValue.calculatedValue
            persistentIndicatorPosition.value = popupValue.offset

            onValueSelected?.invoke(popupValue.calculatedValue)
        }
    }

    Column(modifier = modifier) {
        if (labelHelperProperties.enabled) {
            LabelHelper(
                data = data.map { it.label to it.color },
                textStyle = labelHelperProperties.textStyle,
                labelCountPerLine = labelHelperProperties.labelCountPerLine
            )
            Spacer(modifier = Modifier.height(labelHelperPadding))
        }
        Row(modifier = Modifier.fillMaxSize().weight(1f)) {
            if (indicatorProperties.enabled) {
                if (indicatorProperties.position == IndicatorPosition.Horizontal.Start) {
                    Indicators(
                        indicatorProperties = indicatorProperties,
                        indicators = indicators
                    )
                    Spacer(modifier = Modifier.width(indicatorProperties.padding))
                }
            }
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                Canvas(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .pointerInput(data, minValue, computedMaxValue, linesPathData) {
                            detectDragGestures(
                                onDragStart = { offset ->
                                    showPersistentIndicator(
                                        data = data,
                                        size = size,
                                        position = offset
                                    )
                                },
                                onDrag = { change, _ ->
                                    showPersistentIndicator(
                                        data = data,
                                        size = size,
                                        position = change.position
                                    )
                                }
                            )
                        }
                        .pointerInput(data, minValue, computedMaxValue, linesPathData) {
                            detectTapGestures(
                                onTap = { offset ->
                                    showPersistentIndicator(
                                        data = data,
                                        size = size,
                                        position = offset
                                    )
                                }
                            )
                        }
                ) {
                    val chartAreaHeight = size.height
                    chartWidth.value = size.width
                    val drawZeroLine = {
                        val zeroY = chartAreaHeight - calculateOffset(
                            minValue = minValue,
                            maxValue = computedMaxValue,
                            total = chartAreaHeight,
                            value = 0f
                        ).toFloat()
                        drawLine(
                            brush = zeroLineProperties.color,
                            start = Offset(x = 0f, y = zeroY),
                            end = Offset(x = size.width * zeroLineAnimation.value, y = zeroY),
                            pathEffect = zeroLineProperties.style.pathEffect,
                            strokeWidth = zeroLineProperties.thickness.toPx()
                        )
                    }
                    if (linesPathData.isEmpty() || linesPathData.count() != data.count()) {
                        data.map {
                            val startIndex =
                                if (it.viewRange.startIndex < 0 || it.viewRange.startIndex >= it.values.size - 1) 0 else it.viewRange.startIndex
                            val endIndex =
                                if (it.viewRange.endIndex < 0 || it.viewRange.endIndex <= it.viewRange.startIndex
                                    || it.viewRange.endIndex > it.values.size - 1
                                ) it.values.size - 1 else it.viewRange.endIndex

                            getLinePath(
                                dataPoints = it.values.map { it.toFloat() },
                                maxValue = computedMaxValue.toFloat(),
                                minValue = minValue.toFloat(),
                                rounded = it.curvedEdges ?: curvedEdges,
                                size = size.copy(height = chartAreaHeight),
                                startIndex,
                                endIndex
                            )
                        }.also {
                            linesPathData.addAll(it)
                        }
                    }

                    drawGridLines(
                        dividersProperties = dividerProperties,
                        indicatorPosition = indicatorProperties.position,
                        xAxisProperties = gridProperties.xAxisProperties,
                        yAxisProperties = gridProperties.yAxisProperties,
                        size = size.copy(height = chartAreaHeight),
                        gridEnabled = gridProperties.enabled
                    )
                    if (zeroLineProperties.enabled && zeroLineProperties.zType == ZeroLineProperties.ZType.Under) {
                        drawZeroLine()
                    }
                    data.forEachIndexed { index, line ->
                        val pathData = linesPathData.getOrNull(index) ?: return@Canvas
                        val segmentedPath = Path()
                        pathMeasure.setPath(pathData.path, false)
                        pathMeasure.getSegment(
                            0f,
                            pathMeasure.length * line.strokeProgress.value,
                            segmentedPath
                        )
                        var pathEffect: PathEffect? = null
                        val stroke: Float = when (val drawStyle = line.drawStyle) {
                            is DrawStyle.Fill -> {
                                0f
                            }

                            is DrawStyle.Stroke -> {
                                pathEffect = drawStyle.strokeStyle.pathEffect
                                drawStyle.width.toPx()
                            }
                        }
                        drawPath(
                            path = segmentedPath,
                            brush = line.color,
                            style = Stroke(width = stroke, pathEffect = pathEffect)
                        )

                        var startOffset = 0f
                        var endOffset = size.width
                        if (pathData.startIndex > 0) {
                            startOffset = pathData.xPositions[pathData.startIndex].toFloat()
                        }

                        if (pathData.endIndex < line.values.size - 1) {
                            endOffset = pathData.xPositions[pathData.endIndex].toFloat()
                        }

                        if (line.firstGradientFillColor != null && line.secondGradientFillColor != null) {
                            drawLineGradient(
                                path = pathData.path,
                                color1 = line.firstGradientFillColor,
                                color2 = line.secondGradientFillColor,
                                progress = line.gradientProgress.value,
                                size = size.copy(height = chartAreaHeight),
                                startOffset,
                                endOffset
                            )
                        } else if (line.drawStyle is DrawStyle.Fill) {
                            var fillColor = Color.Unspecified
                            if (line.color is SolidColor) {
                                fillColor = line.color.value
                            }
                            drawLineGradient(
                                path = pathData.path,
                                color1 = fillColor,
                                color2 = fillColor,
                                progress = 1f,
                                size = size.copy(height = chartAreaHeight),
                                startOffset,
                                endOffset
                            )
                        }

                        if ((line.dotProperties?.enabled ?: dotsProperties.enabled)) {
                            drawDots(
                                dataPoints = line.values.mapIndexed { mapIndex, value ->
                                    (dotAnimators.getOrNull(
                                        index
                                    )?.getOrNull(mapIndex) ?: Animatable(0f)) to value.toFloat()
                                },
                                properties = line.dotProperties ?: dotsProperties,
                                linePath = segmentedPath,
                                maxValue = computedMaxValue.toFloat(),
                                minValue = minValue.toFloat(),
                                pathMeasure = pathMeasure,
                                scope = scope,
                                size = size.copy(height = chartAreaHeight),
                                startIndex = pathData.startIndex,
                                endIndex = pathData.endIndex
                            )
                        }
                    }
                    if (zeroLineProperties.enabled && zeroLineProperties.zType == ZeroLineProperties.ZType.Above) {
                        drawZeroLine()
                    }

                    if (persistentIndicatorX.floatValue > 0f) {
                        val indicatorX = persistentIndicatorX.floatValue
                        val indicatorPos = persistentIndicatorPosition.value

                        drawLine(
                            color = Color.Black,
                            start = Offset(indicatorX, 0f),
                            end = Offset(indicatorX, size.height),
                            strokeWidth = 1.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(14f, 8f), 0f) // обновленные значения
                        )

                        indicatorPos?.let { pos ->
                            val correctedPos = pos.copy(x = indicatorX)
                            drawCircle(
                                color = Color.White,
                                radius = 8.dp.toPx(),
                                center = correctedPos
                            )
                            drawCircle(
                                color = Color.Black,
                                radius = 6.dp.toPx(),
                                center = correctedPos
                            )
                        }
                    }

                    popups.forEachIndexed { index, popup ->
                        drawPopup(
                            popup = popup,
                            nextPopup = popups.getOrNull(index + 1),
                            textMeasurer = textMeasurer,
                            scope = scope,
                            progress = popupAnimation.value,
                            offsetAnimator = popupsOffsetAnimators.getOrNull(index)
                        )
                    }
                }
            }
            if (indicatorProperties.enabled) {
                if (indicatorProperties.position == IndicatorPosition.Horizontal.End) {
                    Spacer(modifier = Modifier.width(indicatorProperties.padding))
                    Indicators(
                        indicatorProperties = indicatorProperties,
                        indicators = indicators
                    )
                }
            }
        }
        HorizontalLabels(
            labelProperties = labelProperties,
            labels = labelProperties.labels,
            indicatorProperties = indicatorProperties,
            chartWidth = chartWidth.value,
            density = density,
            textMeasurer = textMeasurer,
            xPadding = xPadding
        )
    }
}

@Composable
private fun Indicators(
    modifier: Modifier = Modifier,
    indicators: List<Double>,
    indicatorProperties: HorizontalIndicatorProperties,
) {
    Column(
        modifier = modifier
            .fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        indicators.forEach {
            BasicText(
                text = indicatorProperties.contentBuilder(it),
                style = indicatorProperties.textStyle
            )
        }
    }
}

private fun calculateValueIndexs(
    fraction: Double,
    values: List<Double>,
    pathData: PathData
): Int {
    val xPosition = (fraction * pathData.path.getBounds().width).toFloat()
    val closestXIndex = pathData.xPositions.indexOfFirst { x ->
        x >= xPosition
    }
    return if (closestXIndex >= 0) closestXIndex else values.size - 1
}

private fun DrawScope.drawPopup(
    popup: Popup,
    nextPopup: Popup?,
    textMeasurer: TextMeasurer,
    scope: CoroutineScope,
    progress: Float,
    offsetAnimator: Pair<Animatable<Float, AnimationVector1D>, Animatable<Float, AnimationVector1D>>? = null
) {
    val offset = popup.position
    val popupProperties = popup.properties
    val measureResult = textMeasurer.measure(
        popupProperties.contentBuilder(popup.dataIndex, popup.valueIndex, popup.value),
        style = popupProperties.textStyle.copy(
            color = popupProperties.textStyle.color.copy(
                alpha = 1f * progress
            )
        )
    )
    var rectSize = measureResult.size.toSize()
    rectSize = rectSize.copy(
        width = (rectSize.width + (popupProperties.contentHorizontalPadding.toPx() * 2)),
        height = (rectSize.height + (popupProperties.contentVerticalPadding.toPx() * 2))
    )

    val conflictDetected =
        ((nextPopup != null) && offset.y in nextPopup.position.y - rectSize.height..nextPopup.position.y + rectSize.height) ||
                (offset.x + rectSize.width) > size.width


    val rectOffset = if (conflictDetected) {
        offset.copy(x = offset.x - rectSize.width)
    } else {
        offset
    }
    offsetAnimator?.also { (x, y) ->
        if (x.value == 0f || y.value == 0f || popupProperties.mode is PopupProperties.Mode.PointMode) {
            scope.launch {
                x.snapTo(rectOffset.x)
                y.snapTo(rectOffset.y)
            }
        } else {
            scope.launch {
                x.animateTo(rectOffset.x)
            }
            scope.launch {
                y.animateTo(rectOffset.y)
            }
        }

    }
    if (offsetAnimator != null) {
        val animatedOffset = if (popup.properties.mode is PopupProperties.Mode.PointMode) {
            rectOffset
        } else {
            Offset(
                x = offsetAnimator.first.value,
                y = offsetAnimator.second.value
            )
        }
        val rect = Rect(
            offset = animatedOffset,
            size = rectSize
        )
        drawPath(
            path = Path().apply {
                addRoundRect(
                    RoundRect(
                        rect = rect.copy(
                            top = rect.top,
                            left = rect.left,
                        ),
                        topLeft = CornerRadius(
                            if (conflictDetected) popupProperties.cornerRadius.toPx() else 0f,
                            if (conflictDetected) popupProperties.cornerRadius.toPx() else 0f
                        ),
                        topRight = CornerRadius(
                            if (!conflictDetected) popupProperties.cornerRadius.toPx() else 0f,
                            if (!conflictDetected) popupProperties.cornerRadius.toPx() else 0f
                        ),
                        bottomRight = CornerRadius(
                            popupProperties.cornerRadius.toPx(),
                            popupProperties.cornerRadius.toPx()
                        ),
                        bottomLeft = CornerRadius(
                            popupProperties.cornerRadius.toPx(),
                            popupProperties.cornerRadius.toPx()
                        ),
                    )
                )
            },
            color = popupProperties.containerColor,
            alpha = 1f * progress
        )
        drawText(
            textLayoutResult = measureResult,
            topLeft = animatedOffset.copy(
                x = animatedOffset.x + popupProperties.contentHorizontalPadding.toPx(),
                y = animatedOffset.y + popupProperties.contentVerticalPadding.toPx()
            )
        )
    }
}