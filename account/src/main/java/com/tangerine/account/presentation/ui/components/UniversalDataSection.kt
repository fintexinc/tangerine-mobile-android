package com.tangerine.account.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.font.FontStyles
import com.tangerine.account.R
import com.tangerine.account.presentation.models.DataSectionItemUi

@Composable
internal fun UniversalDataSection(
    modifier: Modifier = Modifier,
    title: String,
    items: List<DataSectionItemUi>,
    footerText: String? = null,
    showProgressBar: Boolean = false,
) {
    Column(
        modifier = modifier
            .padding(horizontal = 18.dp)
            .fillMaxWidth()
            .background(color = Colors.Background, shape = RoundedCornerShape(16.dp))
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = title,
            style = FontStyles.TitleSmall,
            modifier = Modifier.padding(bottom = 24.dp, start = 16.dp, end = 16.dp)
        )

        if (showProgressBar) {
            AllocationProgressBar(
                items = items,
                modifier = Modifier.padding(bottom = 24.dp, start = 16.dp, end = 16.dp)
            )
        }

        items.forEachIndexed { index, item ->
            DataSectionRow(
                label = item.label,
                value = item.value,
                valueColor = item.valueColor,
                hasInfoIcon = item.hasInfoIcon,
                hasTrailingIcon = item.hasTrailingIcon,
                trailingIconRes = item.trailingIconRes,
                valueStyle = item.valueStyle,
                labelStyle = item.labelStyle,
                isHighlighted = item.isHighlighted,
                showColorDot = item.showColorDot,
                dotColor = item.dotColor,
                isMultiline = item.isMultiline,
            )

            if (index < items.size - 1) {
                Spacer(modifier = Modifier.height(16.dp))

                HorizontalDivider(
                    color = Colors.BorderSubdued,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        footerText?.let {
            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider(
                color = Colors.BorderSubdued,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = it,
                style = FontStyles.BodySmallItalic,
                color = Colors.BorderInformation,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
internal fun DataSectionRow(
    label: String,
    value: String,
    valueColor: Color,
    valueNameColor: Color = Colors.BorderInformation,
    hasInfoIcon: Boolean,
    hasTrailingIcon: Boolean = false,
    trailingIconRes: Int? = null,
    valueStyle: TextStyle = FontStyles.BodyLarge,
    labelStyle: TextStyle = FontStyles.BodyMedium,
    isHighlighted: Boolean = false,
    showColorDot: Boolean = false,
    dotColor: Color = Color.Gray,
    isMultiline: Boolean = false,
    paddingValues: PaddingValues = PaddingValues(start = 16.dp, end = 16.dp)
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = if (isMultiline) Alignment.Top else Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = if (isMultiline) Alignment.Top else Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = label,
                style = if (isHighlighted) FontStyles.BodyMediumBold else labelStyle,
                color = if (isHighlighted) Colors.BrandBlack else valueNameColor,
                maxLines = if (isMultiline) Int.MAX_VALUE else 1
            )

            if (hasInfoIcon) {
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    painter = painterResource(com.fintexinc.core.R.drawable.ic_info),
                    contentDescription = stringResource(R.string.description_info_icon),
                    modifier = Modifier.size(16.dp),
                    tint = Colors.TextInteractive,
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = if (isMultiline) 4.dp else 0.dp)
        ) {
            if (showColorDot) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(
                            color = dotColor,
                            shape = CircleShape
                        )
                )
                Spacer(modifier = Modifier.width(6.dp))
            }

            Text(
                text = value,
                style = valueStyle,
                color = valueColor,
                textAlign = TextAlign.End
            )

            if (hasTrailingIcon && trailingIconRes != null) {
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    painter = painterResource(trailingIconRes),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = Colors.TextInteractive
                )
            }
        }
    }
}

@Composable
private fun AllocationProgressBar(
    items: List<DataSectionItemUi>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Colors.BorderSubdued)
        ) {
            items.forEachIndexed { index, item ->
                val percentage = item.value.replace("%", "").toFloatOrNull() ?: 0f

                Box(
                    modifier = Modifier
                        .weight(percentage)
                        .fillMaxHeight()
                        .background(item.dotColor)
                )

                if (index < items.lastIndex) {
                    Spacer(
                        modifier = Modifier
                            .width(2.dp)
                            .fillMaxHeight()
                            .background(Colors.Background)
                    )
                }
            }
        }
    }
}