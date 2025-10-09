package com.fintexinc.core.presentation.ui.widget.modal

import androidx.annotation.DrawableRes
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
fun UniversalModalBottomSheet(
    isShowing: MutableState<Boolean>,
    title: String,
    onDoneClick: (() -> Unit)? = null,
    onDismiss: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = {
            isShowing.value = false
            onDismiss?.invoke()
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
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    modifier = Modifier
                        .wrapContentHeight()
                        .clickable {
                            isShowing.value = false
                            onDismiss?.invoke()
                        },
                    text = stringResource(R.string.text_cancel),
                    style = FontStyles.BodyLargeBold,
                    color = Colors.TextInteractive
                )
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentHeight(),
                    text = title,
                    textAlign = TextAlign.Center,
                    style = FontStyles.BodyLargeBold,
                    color = Colors.BrandBlack
                )
                Text(
                    modifier = Modifier
                        .wrapContentHeight()
                        .clickable {
                            onDoneClick?.invoke()
                            isShowing.value = false
                        },
                    text = stringResource(R.string.text_done),
                    textAlign = TextAlign.End,
                    style = FontStyles.BodyLargeBold,
                    color = Colors.TextInteractive
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Colors.BorderSubdued)
            )
            Spacer(modifier = Modifier.height(16.dp))

            content()

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssetLiabilitiesModalBottomSheet(
    isShowing: MutableState<Boolean>,
    title: String,
    assets: List<NameValueChecked>,
    liabilities: List<NameValueChecked>,
    updateCheckedStates: (List<NameValueChecked>, List<NameValueChecked>) -> Unit
) {
    val assetsCheckStates = remember {
        mutableStateListOf(*assets.map { it.isChecked }.toTypedArray())
    }
    val liabilitiesCheckStates = remember {
        mutableStateListOf(*liabilities.map { it.isChecked }.toTypedArray())
    }

    UniversalModalBottomSheet(
        isShowing = isShowing,
        title = title,
        onDoneClick = {
            updateCheckedStates(
                getNameValueCheckedUpdatedStates(assets, assetsCheckStates),
                getNameValueCheckedUpdatedStates(liabilities, liabilitiesCheckStates)
            )
        }
    ) {
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

@Composable
private fun ChipsWithTitle(
    title: String,
    items: List<NameValueChecked>,
    checkStates: MutableList<Boolean>,
    onItemCheckedChange: (Int, Boolean) -> Unit
) {
    val allChecked = remember(items) { mutableStateOf(checkStates.all { checked -> checked }) }
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
        Text(
            modifier = Modifier
                .background(
                    color = Colors.BackgroundInteractive,
                    shape = RoundedCornerShape(16.dp)
                )
                .then(
                    if (allChecked.value) {
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
                    val newChecked = !allChecked.value
                    if (newChecked) {
                        for (i in checkStates.indices) {
                            checkStates[i] = true
                            onItemCheckedChange(i, true)
                        }
                    } else {
                        for (i in checkStates.indices) {
                            checkStates[i] = false
                            onItemCheckedChange(i, false)
                        }
                    }
                    allChecked.value = newChecked
                }
                .padding(horizontal = 12.dp, vertical = 8.dp),
            text = stringResource(R.string.text_all),
            style = FontStyles.BodyMedium,
            color = Colors.TextInteractive
        )
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
                        if(checkStates.all { checked -> checked }) {
                            allChecked.value = true
                        }
                        if(!newChecked && allChecked.value) {
                            allChecked.value = false
                        }
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

private fun getNameValueCheckedUpdatedStates(
    items: List<NameValueChecked>,
    checkStates: List<Boolean>
): List<NameValueChecked> {
    return items.mapIndexed { index, item ->
        item.copy(
            isChecked = checkStates.getOrNull(index) ?: item.isChecked
        )
    }
}

data class NameValueChecked(
    val id: String,
    val name: String,
    val subName: String,
    val date: String,
    val value: Double,
    val isChecked: Boolean = true,
    @param:DrawableRes val iconResId: Int? = null,
)