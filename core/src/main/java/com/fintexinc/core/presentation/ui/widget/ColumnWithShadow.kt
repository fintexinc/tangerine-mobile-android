package com.fintexinc.core.presentation.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.fintexinc.core.ui.color.Colors

@Composable
fun ColumnWithShadow(
    contentPadding: PaddingValues = PaddingValues(18.dp),
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 18.dp)
            .shadow(4.dp, RoundedCornerShape(16.dp))
            .background(
                color = Colors.Background,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(contentPadding)
    ) {
        content()
    }
}

@Composable
fun ColumnWithBorder(
    contentPadding: PaddingValues = PaddingValues(18.dp),
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 18.dp)
            .background(
                color = Colors.Background,
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 1.dp,
                color = Colors.BorderSubdued,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(contentPadding)
    ) {
        content()
    }
}


@Composable
fun RowWithShadow(
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 18.dp)
            .shadow(4.dp, RoundedCornerShape(16.dp))
            .background(
                color = Colors.Background,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        content()
    }
}