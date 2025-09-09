package com.fintexinc.core.presentation.ui.widget.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.unit.dp
import com.fintexinc.core.data.model.NameValue
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.font.FontStyles

@Composable
fun SimpleNameValueItem(modifier: Modifier = Modifier, item: NameValue, isLastItem: Boolean = false) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f).wrapContentHeight(),
                text = item.name,
                style = FontStyles.BodyMedium,
                color = Colors.TextSubdued
            )
            Text(
                modifier = Modifier.wrapContentSize(),
                text = item.value,
                style = FontStyles.BodyLarge,
                color = item.valueColor
            )
        }
        if(!isLastItem) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(color = Colors.BorderSubdued)
            )
        }
    }
}