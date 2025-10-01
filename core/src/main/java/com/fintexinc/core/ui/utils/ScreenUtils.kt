package com.fintexinc.core.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object ScreenUtils {

    /**
     * [percentage] is a float value between 0 and 1
     */
    @Composable
    fun GetPercentageOfScreenHeight(percentage: Float): Dp {
        return with(LocalDensity.current) {
            (LocalWindowInfo.current.containerSize.height.toDp() * percentage)
        }
    }
}