package com.fintexinc.core.presentation.ui.widget.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fintexinc.core.ui.color.Colors

fun <T> LazyListScope.itemsWithBackground(
    items: List<T>,
    key: ((item: T) -> Any)? = null,
    contentType: (item: T) -> Any? = { null },
    itemSpacing: Dp = 0.dp,
    clipIndividualItems: Boolean = false,
    bottomPadding: Dp = 0.dp,
    itemContent: @Composable (item: T, isLastItem: Boolean) -> Unit,
) {
    itemsIndexed(
        items = items,
        key = if (key != null) { index, item -> key(item) } else null,
        contentType = { index, item -> contentType(item) }
    ) { index, item ->
        val shape = when {
            clipIndividualItems -> RoundedCornerShape(16.dp)
            items.size == 1 -> RoundedCornerShape(16.dp)
            index == 0 -> RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            index == items.lastIndex -> RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
            else -> RoundedCornerShape(0.dp)
        }

        val topPadding = if (index > 0) itemSpacing else 0.dp
        val innerBottomPadding = if (index == items.lastIndex) bottomPadding else 0.dp

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 16.dp)
                .padding(top = topPadding)
                .clip(shape)
                .background(
                    color = Colors.Background,
                    shape = shape
                )
                .padding(bottom = innerBottomPadding),
        ) {
            itemContent(item, index == items.lastIndex)
        }
    }
}
