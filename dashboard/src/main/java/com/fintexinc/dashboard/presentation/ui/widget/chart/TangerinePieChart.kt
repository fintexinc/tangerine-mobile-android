package com.fintexinc.dashboard.presentation.ui.widget.chart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.font.FontStyles
import com.tangerine.charts.compose_charts.PieChart
import com.tangerine.charts.compose_charts.models.Pie

@Composable
fun TangerinePieChart(title: String, pieData: List<Pie>) {
    Column(modifier = Modifier.fillMaxWidth().height(380.dp)) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            text = title,
            style = FontStyles.TitleMediumMedium
        )
        Spacer(modifier = Modifier.height(18.dp))
        PieChart(
            modifier = Modifier
                .fillMaxWidth()
                .height(145.dp),
            data = pieData,
            style = Pie.Style.Stroke(width = 24.dp),
            spaceDegree = 5F,
        )
        Spacer(modifier = Modifier.height(44.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 8.dp)
        ) {
            pieData.forEach {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(color = it.color, shape = CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .wrapContentHeight(),
                        text = it.label ?: "",
                        style = FontStyles.BodyMedium,
                        color = Colors.Text
                    )
                    Text(
                        modifier = Modifier.wrapContentHeight(),
                        text = "${it.data}%",
                        style = FontStyles.BodyMedium,
                        color = Colors.Text,
                        textAlign = TextAlign.End
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                HorizontalDivider(color = Colors.BorderSubdued)

                Spacer(modifier = Modifier.height(10.dp))

            }
        }
    }
}