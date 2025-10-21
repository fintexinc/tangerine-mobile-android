package com.fintexinc.core.presentation.ui.widget.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fintexinc.core.R
import com.fintexinc.core.data.model.DataPoint
import com.fintexinc.core.presentation.ui.datapoint.DataPointCollapsableUI
import com.fintexinc.core.presentation.ui.modifier.clickableShape
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.font.FontStyles

@Composable
fun CollapsableLazyColumn(
    dataPoints: Map<String, List<DataPoint>>,
    expanded: MutableState<Boolean>,
    headerShape: Shape = RectangleShape,
    title: String,
    subtitle: String? = null,
    addItemText: String,
    isLastList: Boolean = false,
    onAddItemClick: () -> Unit,
    onItemClick: (DataPoint) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 18.dp)
                .clickable {
                    expanded.value = !expanded.value
                }
                .then(
                    if (expanded.value) {
                        Modifier.background(
                            color = Colors.Background,
                            shape = headerShape
                        )
                    } else {
                        Modifier
                            .background(
                                color = Colors.Background,
                                if (isLastList) RoundedCornerShape(
                                    bottomStart = 16.dp,
                                    bottomEnd = 16.dp
                                ) else RectangleShape
                            )
                    }
                )
                .padding(vertical = 12.dp, horizontal = 16.dp)

        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = title,
                style = FontStyles.TitleSmall,
                color = Colors.BrandBlack
            )
            if (subtitle != null) {
                Text(
                    modifier = Modifier.wrapContentSize(),
                    text = subtitle,
                    style = FontStyles.BodyLargeBold,
                    color = Colors.BrandBlack
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                painter = if (expanded.value) painterResource(
                    R.drawable.ic_chevron_up
                ) else painterResource(
                    R.drawable.ic_chevron_down
                ),
                tint = Colors.BrandBlack,
                contentDescription = stringResource(R.string.description_icon_expand_collapse),
            )
        }
    }
    if (expanded.value) {
        dataPoints.forEach { (entryTitle, entryList) ->
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 34.dp, vertical = 8.dp),
                text = entryTitle,
                style = FontStyles.BodySmall,
                color = Colors.Text
            )
            entryList.forEach { dataPoint ->
                DataPointCollapsableUI(
                    dataPoint = dataPoint,
                    onClick = onItemClick,
                    isLastItem = entryList.last() == dataPoint
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 18.dp)
                .background(color = Colors.Background)
                .padding(vertical = 12.dp, horizontal = 16.dp)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(
                        color = Colors.BackgroundSecondary,
                        shape = RoundedCornerShape(40.dp)
                    )
                    .clickableShape(RoundedCornerShape(40.dp)) {
                        onAddItemClick()
                    }
                    .padding(12.dp)
                    .align(Alignment.Center),
                text = addItemText,
                textAlign = TextAlign.Center,
                style = FontStyles.BodyLargeBold,
                color = Colors.TextInverse
            )
        }
    }
}