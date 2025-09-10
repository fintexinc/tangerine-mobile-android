package com.tangerine.charts.compose_charts.extensions

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter

@OptIn(ExperimentalComposeUiApi::class)
fun Modifier.pointerInteropFilter(onTouchEvent: (MotionEvent) -> Boolean): Modifier {
    return this.pointerInteropFilter { event -> onTouchEvent.invoke(event.toCommon()) }
}

fun android.view.MotionEvent.toCommon(): MotionEvent {
    return MotionEvent(
        action = this.action, x = this.x, y = this.y
    )
}

fun Modifier.pointerInteropFilterCommon(onTouchEvent: (MotionEvent) -> Boolean): Modifier {
    return this.pointerInput(Unit) {
        awaitPointerEventScope {
            while (true) {
                val event = awaitPointerEvent()
                val position = event.changes.first().position
                if ((event.type == PointerEventType.Enter) || (event.type == PointerEventType.Exit) || (event.type == PointerEventType.Press) || (event.type == PointerEventType.Release)) {
                    onTouchEvent.invoke(
                        MotionEvent(
                            action = if (event.type == PointerEventType.Enter || event.type == PointerEventType.Press) MotionEvent.ACTION_DOWN else MotionEvent.ACTION_UP,
                            x = position.x,
                            y = position.y
                        )
                    )
                }
            }
        }
    }
}

data class MotionEvent(
    val action: Int, val x: Float, val y: Float
) {
    companion object {
        const val ACTION_DOWN: Int = 0
        const val ACTION_UP: Int = 1
    }
}