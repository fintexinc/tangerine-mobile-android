package com.tangerine.charts.compose_charts

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.AnimationVector4D
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.isUnspecified
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tangerine.charts.compose_charts.extensions.getAngleInDegree
import com.tangerine.charts.compose_charts.extensions.isDegreeBetween
import com.tangerine.charts.compose_charts.extensions.isInsideCircle
import com.tangerine.charts.compose_charts.models.Pie
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun PieChart(
    modifier: Modifier = Modifier,
    data: List<Pie>,
    spaceDegree: Float = 0f,
    onPieClick: (Pie) -> Unit = {},
    selectedScale: Float = 1.1f,
    selectedPaddingDegree: Float = 5f,
    colorAnimEnterSpec: AnimationSpec<Color> = tween(500),
    scaleAnimEnterSpec: AnimationSpec<Float> = tween(500),
    spaceDegreeAnimEnterSpec: AnimationSpec<Float> = tween(500),
    colorAnimExitSpec: AnimationSpec<Color> = colorAnimEnterSpec,
    scaleAnimExitSpec: AnimationSpec<Float> = scaleAnimEnterSpec,
    spaceDegreeAnimExitSpec: AnimationSpec<Float> = spaceDegreeAnimEnterSpec,
    style: Pie.Style = Pie.Style.Fill,
    borderWidth: Dp = 4.dp,
    borderColor: Color = Color(0xFFF7F7F7)
) {

    require(data.none { it.data < 0 }) {
        "Data must be at least 0"
    }

    val scope = rememberCoroutineScope()

    var pieChartCenter by remember {
        mutableStateOf(Offset.Zero)
    }

    var details by remember {
        mutableStateOf(emptyList<PieDetails>())
    }
    val pieces = remember {
        mutableListOf<PiePiece>()
    }

    val pathMeasure = remember {
        PathMeasure()
    }

    LaunchedEffect(data) {
        val currDetailsSize = details.size
        details = if (details.isNotEmpty()) {
            data.mapIndexed { mapIndex, chart ->
                if (mapIndex < currDetailsSize) {
                    PieDetails(
                        id = details[mapIndex].id,
                        pie = chart,
                        scale = details[mapIndex].scale,
                        color = details[mapIndex].color,
                        space = details[mapIndex].space
                    )
                } else {
                    PieDetails(pie = chart)
                }
            }
        } else {
            data.map { PieDetails(pie = it) }
        }
        pieces.clear()
    }

    LaunchedEffect(details) {
        details.forEach {
            if (it.pie.selected) {
                scope.launch {
                    it.color.animateTo(
                        it.pie.selectedColor,
                        animationSpec = it.pie.colorAnimEnterSpec ?: colorAnimEnterSpec
                    )
                }
                scope.launch {
                    it.scale.animateTo(
                        it.pie.selectedScale ?: selectedScale,
                        animationSpec = it.pie.scaleAnimEnterSpec ?: scaleAnimEnterSpec
                    )
                }
                scope.launch {
                    it.space.animateTo(
                        it.pie.selectedPaddingDegree ?: selectedPaddingDegree,
                        animationSpec = it.pie.spaceDegreeAnimEnterSpec ?: spaceDegreeAnimEnterSpec
                    )
                }
            } else {
                scope.launch {
                    it.color.animateTo(
                        it.pie.color,
                        animationSpec = it.pie.colorAnimExitSpec ?: colorAnimExitSpec
                    )
                }
                scope.launch {
                    it.scale.animateTo(
                        1f,
                        animationSpec = it.pie.scaleAnimExitSpec ?: scaleAnimExitSpec
                    )
                }
                scope.launch {
                    it.space.animateTo(
                        0f,
                        animationSpec = it.pie.spaceDegreeAnimExitSpec ?: spaceDegreeAnimExitSpec
                    )
                }
            }
        }
    }

    Canvas(modifier = modifier
        .pointerInput(Unit) {
            detectTapGestures { offset ->
                val angleInDegree = getAngleInDegree(
                    touchTapOffset = offset,
                    pieceOffset = pieChartCenter
                )

                pieces.firstOrNull { piece ->
                    isDegreeBetween(angleInDegree, piece.startFromDegree, piece.endToDegree)
                            && isInsideCircle(offset, pieChartCenter, piece.radius) }
                    ?.let {
                        val (id, _) = it
                        details.find { it.id == id }
                            ?.let {
                                onPieClick(it.pie)
                            }
                    }
            }
        }
    ) {
        pieChartCenter = center

        val radius: Float = when (style) {
            is Pie.Style.Fill -> {
                (minOf(size.width, size.height) / 1.5f)
            }

            is Pie.Style.Stroke -> {
                (minOf(size.width, size.height) / 2) - (style.width.toPx() / 2)
            }
        }

        val borderWidthPx = borderWidth.toPx()
        val total = details.sumOf { it.pie.data }

        data class PathToDraw(
            val path: Path,
            val color: Color,
            val style: DrawStyle,
            val isBorder: Boolean = false
        )

        val pathsToDraw = mutableListOf<PathToDraw>()

        details.forEachIndexed { index, detail ->
            val degree = ((detail.pie.data * 360) / total)

            val currentStyle = (detail.pie.style ?: style)
            val isStroke = currentStyle is Pie.Style.Stroke

            val strokeWidth = if (isStroke) {
                (currentStyle as Pie.Style.Stroke).width.toPx()
            } else {
                0f
            }

            val cornerRadius = strokeWidth / 2f
            val cornerAngle = if (isStroke && degree < 360.0) {
                Math.toDegrees(cornerRadius / (radius * detail.scale.value).toDouble()).toFloat()
            } else {
                0f
            }

            if (degree >= 360.0) {
                pieces.add(
                    PiePiece(
                        id = detail.id,
                        radius = radius * detail.scale.value,
                        startFromDegree = 0f,
                        endToDegree = 360f
                    )
                )

                val piecePath = Path().apply {
                    addOval(
                        oval = Rect(
                            center = center,
                            radius = radius * detail.scale.value
                        )
                    )
                }

                if (isStroke) {
                    pathsToDraw.add(
                        PathToDraw(
                            path = piecePath,
                            color = borderColor,
                            style = Stroke(
                                width = strokeWidth + borderWidthPx * 2,
                                cap = StrokeCap.Round
                            ),
                            isBorder = true
                        )
                    )
                }

                pathsToDraw.add(
                    PathToDraw(
                        path = piecePath,
                        color = detail.color.value,
                        style = if (isStroke) {
                            Stroke(width = strokeWidth)
                        } else {
                            Fill
                        }
                    )
                )
            } else {
                val beforeItems = data.filterIndexed { filterIndex, chart -> filterIndex < index }
                val startFromDegree = beforeItems.sumOf { (it.data * 360) / total }

                val arcRect = Rect(
                    center = center,
                    radius = radius * detail.scale.value
                )

                val arcStart = startFromDegree.toFloat() + detail.space.value + cornerAngle
                val arcSweep = degree.toFloat() - ((detail.space.value * 2) + spaceDegree + (cornerAngle * 2))

                if (isStroke && arcSweep > 0) {
                    val borderPath = Path().apply {
                        arcTo(arcRect, arcStart, arcSweep, true)
                    }

                    pathsToDraw.add(
                        PathToDraw(
                            path = borderPath,
                            color = borderColor,
                            style = Stroke(
                                width = strokeWidth + borderWidthPx * 2,
                                cap = StrokeCap.Round,
                                join = StrokeJoin.Round
                            ),
                            isBorder = true
                        )
                    )
                }

                val piecePath = Path().apply {
                    if (arcSweep > 0) {
                        arcTo(arcRect, arcStart, arcSweep, true)
                    }
                }

                if (currentStyle is Pie.Style.Fill) {
                    pathMeasure.setPath(piecePath, false)
                    piecePath.reset()
                    val start = pathMeasure.getPosition(0f)
                    if (!start.isUnspecified) {
                        piecePath.moveTo(start.x, start.y)
                    }
                    piecePath.lineTo(
                        (size.width / 2),
                        ((size.height / 2))
                    )
                    piecePath.arcTo(arcRect, arcStart, arcSweep, true)
                    piecePath.lineTo(
                        (size.width / 2),
                        (size.height / 2)
                    )
                }

                pieces.add(
                    PiePiece(
                        id = detail.id,
                        radius = radius * detail.scale.value,
                        startFromDegree = arcStart - cornerAngle,
                        endToDegree = if (arcStart + arcSweep + cornerAngle >= 360f) 360f else arcStart + arcSweep + cornerAngle,
                    )
                )

                pathsToDraw.add(
                    PathToDraw(
                        path = piecePath,
                        color = detail.color.value,
                        style = if (isStroke) {
                            Stroke(
                                width = strokeWidth,
                                cap = StrokeCap.Round,
                                join = StrokeJoin.Round
                            )
                        } else {
                            Fill
                        }
                    )
                )
            }
        }

        pathsToDraw.filter { it.isBorder }.forEach { pathData ->
            drawPath(
                path = pathData.path,
                color = pathData.color,
                style = pathData.style
            )
        }

        pathsToDraw.filter { !it.isBorder }.forEach { pathData ->
            drawPath(
                path = pathData.path,
                color = pathData.color,
                style = pathData.style
            )
        }
    }
}


private data class PieDetails(
    val id: String = Random.nextInt(0, 999999).toString(),
    val pie: Pie,
    val color: Animatable<Color, AnimationVector4D> = Animatable(pie.color),
    val scale: Animatable<Float, AnimationVector1D> = Animatable(1f),
    val space: Animatable<Float, AnimationVector1D> = Animatable(0f)
)

private data class PiePiece(
    val id: String,
    val radius: Float,
    val startFromDegree: Float,
    val endToDegree: Float,
)