package com.fintexinc.core.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.font.FontStyles

@Composable
fun SectionHeader(
    title: String,
    isExpanded: Boolean,
    onExpandToggle: () -> Unit,
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Colors.BackgroundSubdued)
            .clickable { onExpandToggle() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = FontStyles.BodyLargeBold,
            color = Colors.Text,
        )

        Image(
            painter = painterResource(com.fintexinc.core.R.drawable.ic_chevron_down),
            contentDescription = stringResource(com.fintexinc.core.R.string.description_icon_add),
            modifier = Modifier.rotate(if (isExpanded) 180f else 0f),
        )
    }
}