package com.fintexinc.core.presentation.ui.modifier

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Shape


@Composable
fun Modifier.clickableShape(shape: Shape, onClick: () -> Unit): Modifier {
    return this.then(
        Modifier
            .clip(shape = shape)
            .clickable {
                onClick()
            }
            .clipToBounds()
    )
}