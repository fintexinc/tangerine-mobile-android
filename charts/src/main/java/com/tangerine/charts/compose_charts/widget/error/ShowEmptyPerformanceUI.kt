package com.tangerine.charts.compose_charts.widget.error

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fintexinc.core.R
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.font.FontStyles

// Added imports for custom drawing
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size

@Composable
fun ShowEmptyPerformanceUI(
    onAddInvestmentAccountClicked: () -> Unit = { }
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .drawBehind {
                // dashed rounded border
                val strokeWidth = 1.dp.toPx()
                val corner = 8.dp.toPx()
                val dashArray = floatArrayOf(8.dp.toPx(), 4.dp.toPx())
                val inset = strokeWidth / 2f
                val rectSize = Size(size.width - strokeWidth, size.height - strokeWidth)
                drawRoundRect(
                    color = Colors.BorderSubdued,
                    topLeft = Offset(inset, inset),
                    size = rectSize,
                    cornerRadius = CornerRadius(corner, corner),
                    style = Stroke(width = strokeWidth, pathEffect = PathEffect.dashPathEffect(dashArray, 0f))
                )
            }
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            modifier = Modifier.wrapContentSize(),
            painter = painterResource(R.drawable.ic_no_chart_data),
            contentDescription = stringResource(R.string.description_no_investments)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            text = stringResource(R.string.text_no_investment_data),
            style = FontStyles.BodyMedium,
            color = Colors.Text,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clickable {
                    onAddInvestmentAccountClicked()
                },
            text = stringResource(R.string.text_add_investment_account),
            style = FontStyles.BodySmall,
            color = Colors.TextInteractive,
            textAlign = TextAlign.Center,
        )
    }
}