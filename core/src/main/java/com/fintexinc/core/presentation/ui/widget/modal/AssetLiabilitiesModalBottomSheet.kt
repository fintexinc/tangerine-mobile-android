package com.fintexinc.core.presentation.ui.widget.modal

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fintexinc.core.R
import com.fintexinc.core.presentation.ui.modifier.clickableShape
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.font.FontStyles

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssetLiabilitiesModalBottomSheet(
    isShowing: MutableState<Boolean>,
    title: String,
    assets: List<NameValueChecked>,
    liabilities: List<NameValueChecked>,
    updateCheckedStates: (List<NameValueChecked>, List<Boolean>, List<NameValueChecked>, List<Boolean>) -> Unit
) {
    val assetMainCheckState = remember {
        mutableStateOf(true)
    }
    val assetsCheckStates = remember {
        mutableStateListOf(*assets.map { it.isChecked }.toTypedArray())
    }
    val liabilitiesMainCheckState = remember {
        mutableStateOf(true)
    }
    val liabilitiesCheckStates = remember {
        mutableStateListOf(*liabilities.map { it.isChecked }.toTypedArray())
    }
    ModalBottomSheet(
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        onDismissRequest = {
            isShowing.value = false
            updateCheckedStates(assets, assetsCheckStates, liabilities, liabilitiesCheckStates)
        },
        shape = RoundedCornerShape(8.dp),
        containerColor = Colors.Background,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    modifier = Modifier
                        .weight(0.33f)
                        .wrapContentHeight()
                        .clickable {
                            isShowing.value = false
                        },
                    text = stringResource(R.string.text_cancel),
                    style = FontStyles.BodyLarge,
                    color = Colors.TextInteractive
                )
                Text(
                    modifier = Modifier
                        .weight(0.33f)
                        .wrapContentHeight(),
                    text = title,
                    textAlign = TextAlign.Center,
                    style = FontStyles.BodyLargeBold,
                    color = Colors.BrandBlack
                )
                Text(
                    modifier = Modifier
                        .weight(0.33f)
                        .wrapContentHeight(),
                    text = stringResource(R.string.text_done),
                    textAlign = TextAlign.End,
                    style = FontStyles.BodyLargeBold,
                    color = Colors.BrandBlack
                )
            }
            Spacer(
                modifier = Modifier
                    .height(1.dp)
                    .background(Colors.BorderSubdued)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 16.dp)
            ) {
                ChipsWithTitle(
                    title = stringResource(R.string.text_assets),
                    items = assets,
                    checkStates = assetsCheckStates,
                    onItemCheckedChange = { index, isChecked ->
                        assetsCheckStates[index] = isChecked
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
                ChipsWithTitle(
                    title = stringResource(R.string.text_liabilities),
                    items = liabilities,
                    checkStates = liabilitiesCheckStates,
                    onItemCheckedChange = { index, isChecked ->
                        liabilitiesCheckStates[index] = isChecked
                    }
                )
            }
        }

    }
}

@Composable
private fun ChipsWithTitle(
    title: String,
    items: List<NameValueChecked>,
    checkStates: MutableList<Boolean>,
    onItemCheckedChange: (Int, Boolean) -> Unit
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        text = title,
        style = FontStyles.BodyLargeBold,
        color = Colors.BrandBlack
    )
    Spacer(modifier = Modifier.height(8.dp))
    // Chips
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items.forEachIndexed { index, item ->
            val checked = checkStates[index]
            Text(
                modifier = Modifier
                    .background(
                        color = Colors.BackgroundInteractive,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .then(
                        if (checked) {
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
                        val newChecked = !checkStates[index]
                        checkStates[index] = newChecked
                        onItemCheckedChange(index, newChecked)
                    }
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                text = item.name,
                style = FontStyles.BodyMedium,
                color = Colors.TextInteractive
            )
        }
    }
}

data class NameValueChecked(
    val name: String,
    val subName: String,
    val date: String,
    val value: Double,
    val isChecked: Boolean = true
)