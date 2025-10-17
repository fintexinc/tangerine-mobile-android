package com.fintexinc.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.fintexinc.core.presentation.ui.modifier.clickableShape
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.font.FontStyles

@Composable
fun MultiSelectChips(
    options: List<String>,
    selectedStates: MutableList<Boolean>,
    onSelectionChanged: (Int, Boolean) -> Unit,
    icons: List<Int?> = listOf(),
    onSelectedChipClick: ((Int) -> Unit)? = null,
) {
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        options.forEachIndexed { index, option ->
            val isSelected = selectedStates[index]
            Row(
                modifier = Modifier
                    .background(
                        color = Colors.BackgroundInteractive,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .then(
                        if (isSelected) {
                            Modifier.border(
                                width = 1.dp,
                                color = Colors.TextInteractive,
                                shape = RoundedCornerShape(16.dp)
                            )
                        } else {
                            Modifier
                        }
                    )
                    .clickableShape(RoundedCornerShape(16.dp)) {
                        if (isSelected && onSelectedChipClick != null) {
                            onSelectedChipClick(index)
                            return@clickableShape
                        }

                        val newSelected = !selectedStates[index]
                        val canDeselect = newSelected || selectedStates.count { it } > 1

                        if (canDeselect) {
                            selectedStates[index] = newSelected
                            onSelectionChanged(index, newSelected)
                        }
                    }
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                icons.getOrNull(index)?.let { iconRes ->
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = null,
                        tint = Colors.TextInteractive,
                    )
                }
                Text(
                    text = option,
                    style = FontStyles.BodyMedium,
                    color = Colors.TextInteractive,
                )
            }
        }
    }
}
