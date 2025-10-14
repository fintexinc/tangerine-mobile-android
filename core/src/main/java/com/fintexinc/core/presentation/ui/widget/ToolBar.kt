package com.fintexinc.core.presentation.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.font.FontStyles

@Composable
fun ToolBar(
    modifier: Modifier = Modifier,
    text: String,
    textColor: Color = Colors.BrandBlack,
    backgroundColor: Color = Colors.Background,
    leftIcon: (@Composable () -> Unit)? = null,
    rightIcon: (@Composable () -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = backgroundColor)
            .drawBehind {
                val shadowHeight = 18.dp.toPx()
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.1f),
                            Color.Transparent
                        ),
                        startY = size.height,
                        endY = size.height + shadowHeight
                    ),
                    topLeft = Offset(0f, size.height),
                    size = Size(size.width, shadowHeight)
                )
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Spacer(modifier = Modifier.width(30.dp))

        leftIcon?.invoke()

        Text(
            text = text,
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 22.dp),
            style = FontStyles.TitleSmallRegular,
            color = textColor,
            textAlign = TextAlign.Center
        )

        rightIcon?.invoke()

        Spacer(modifier = Modifier.width(30.dp))

    }
}
